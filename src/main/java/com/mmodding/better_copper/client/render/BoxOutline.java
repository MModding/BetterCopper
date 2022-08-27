package com.mmodding.better_copper.client.render;

import com.mmodding.better_copper.helpers.Helper;
import com.mmodding.better_copper.init.SpecialTextures;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;

/**
 * Credit: Create
 */
public abstract class BoxOutline {

	protected OutlineParams params;
	protected Matrix3f transformNormals;
	protected Box box;

	public BoxOutline(Box box) {
		params = new OutlineParams();
		this.setBounds(box);
	}

	public void render(MatrixStack ms, SuperRenderTypeBuffer buffer, float pt) {
		renderBB(ms, buffer, box);
	}

	public void tick() {}

	public OutlineParams getParams() {
		return params;
	}

	public void setBounds(Box box) {
		this.box = box;
	}

	public void renderBB(MatrixStack ms, SuperRenderTypeBuffer buffer, Box bb) {
		Vec3d projectedView = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
		boolean noCull = bb.contains(projectedView);
		bb = bb.expand(noCull ? -1 / 128d : 1 / 128d);
		noCull |= params.disableCull;

		Vec3d xyz = new Vec3d(bb.minX, bb.minY, bb.minZ);
		Vec3d Xyz = new Vec3d(bb.maxX, bb.minY, bb.minZ);
		Vec3d xYz = new Vec3d(bb.minX, bb.maxY, bb.minZ);
		Vec3d XYz = new Vec3d(bb.maxX, bb.maxY, bb.minZ);
		Vec3d xyZ = new Vec3d(bb.minX, bb.minY, bb.maxZ);
		Vec3d XyZ = new Vec3d(bb.maxX, bb.minY, bb.maxZ);
		Vec3d xYZ = new Vec3d(bb.minX, bb.maxY, bb.maxZ);
		Vec3d XYZ = new Vec3d(bb.maxX, bb.maxY, bb.maxZ);

		Vec3d start = xyz;
		renderAACuboidLine(ms, buffer, start, Xyz);
		renderAACuboidLine(ms, buffer, start, xYz);
		renderAACuboidLine(ms, buffer, start, xyZ);

		start = XyZ;
		renderAACuboidLine(ms, buffer, start, xyZ);
		renderAACuboidLine(ms, buffer, start, XYZ);
		renderAACuboidLine(ms, buffer, start, Xyz);

		start = XYz;
		renderAACuboidLine(ms, buffer, start, xYz);
		renderAACuboidLine(ms, buffer, start, Xyz);
		renderAACuboidLine(ms, buffer, start, XYZ);

		start = xYZ;
		renderAACuboidLine(ms, buffer, start, XYZ);
		renderAACuboidLine(ms, buffer, start, xyZ);
		renderAACuboidLine(ms, buffer, start, xYz);

		renderFace(ms, buffer, Direction.NORTH, xYz, XYz, Xyz, xyz, noCull);
		renderFace(ms, buffer, Direction.SOUTH, XYZ, xYZ, xyZ, XyZ, noCull);
		renderFace(ms, buffer, Direction.EAST, XYz, XYZ, XyZ, Xyz, noCull);
		renderFace(ms, buffer, Direction.WEST, xYZ, xYz, xyz, xyZ, noCull);
		renderFace(ms, buffer, Direction.UP, xYZ, XYZ, XYz, xYz, noCull);
		renderFace(ms, buffer, Direction.DOWN, xyz, Xyz, XyZ, xyZ, noCull);

	}

	protected void renderFace(MatrixStack ms, SuperRenderTypeBuffer buffer, Direction direction, Vec3d p1, Vec3d p2, Vec3d p3, Vec3d p4, boolean noCull) {
		if (params.faceTexture.isEmpty())
			return;

		Identifier faceTexture = params.faceTexture.get().getLocation();
		float alphaBefore = params.alpha;
		params.alpha = (direction == params.getHighlightedFace() && params.hightlightedFaceTexture.isPresent()) ? 1 : 0.5f;

		RenderLayer translucentType = RenderLayer.getEntityTranslucent(faceTexture, !noCull);
		VertexConsumer builder = buffer.getLateBuffer(translucentType);

		Direction.Axis axis = direction.getAxis();
		Vec3d uDiff = p2.subtract(p1);
		Vec3d vDiff = p4.subtract(p1);
		float maxU = (float) Math.abs(axis == Direction.Axis.X ? uDiff.z : uDiff.x);
		float maxV = (float) Math.abs(axis == Direction.Axis.Y ? vDiff.z : vDiff.y);
		putQuadUV(ms, builder, p1, p2, p3, p4, 0, 0, maxU, maxV, Direction.UP);
		params.alpha = alphaBefore;
	}

	public void renderAACuboidLine(MatrixStack ms, SuperRenderTypeBuffer buffer, Vec3d start, Vec3d end) {
		float lineWidth = params.getLineWidth();
		if (lineWidth == 0)
			return;

		VertexConsumer builder = buffer.getBuffer(RenderLayer.getSolid());

		Vec3d diff = end.subtract(start);
		if (diff.x + diff.y + diff.z < 0) {
			Vec3d temp = start;
			start = end;
			end = temp;
			diff = diff.multiply(-1);
		}

		Vec3d extension = diff.normalize().multiply(lineWidth / 2);
		Vec3d plane = Helper.axisAlingedPlaneOf(diff);
		Direction face = Direction.getFacing(diff.x, diff.y, diff.z);
		Direction.Axis axis = face.getAxis();

		start = start.subtract(extension);
		end = end.add(extension);
		plane = plane.multiply(lineWidth / 2);

		Vec3d a1 = plane.add(start);
		Vec3d b1 = plane.add(end);
		plane = Helper.rotate(plane, -90, axis);
		Vec3d a2 = plane.add(start);
		Vec3d b2 = plane.add(end);
		plane = Helper.rotate(plane, -90, axis);
		Vec3d a3 = plane.add(start);
		Vec3d b3 = plane.add(end);
		plane = Helper.rotate(plane, -90, axis);
		Vec3d a4 = plane.add(start);
		Vec3d b4 = plane.add(end);

		if (params.disableNormals) {
			face = Direction.UP;
			putQuad(ms, builder, b4, b3, b2, b1, face);
			putQuad(ms, builder, a1, a2, a3, a4, face);
			putQuad(ms, builder, a1, b1, b2, a2, face);
			putQuad(ms, builder, a2, b2, b3, a3, face);
			putQuad(ms, builder, a3, b3, b4, a4, face);
			putQuad(ms, builder, a4, b4, b1, a1, face);
			return;
		}

		putQuad(ms, builder, b4, b3, b2, b1, face);
		putQuad(ms, builder, a1, a2, a3, a4, face.getOpposite());
		Vec3d vec = a1.subtract(a4);
		face = Direction.getFacing(vec.x, vec.y, vec.z);
		putQuad(ms, builder, a1, b1, b2, a2, face);
		vec = Helper.rotate(vec, -90, axis);
		face = Direction.getFacing(vec.x, vec.y, vec.z);
		putQuad(ms, builder, a2, b2, b3, a3, face);
		vec = Helper.rotate(vec, -90, axis);
		face = Direction.getFacing(vec.x, vec.y, vec.z);
		putQuad(ms, builder, a3, b3, b4, a4, face);
		vec = Helper.rotate(vec, -90, axis);
		face = Direction.getFacing(vec.x, vec.y, vec.z);
		putQuad(ms, builder, a4, b4, b1, a1, face);
	}

	public void putQuad(MatrixStack ms, VertexConsumer builder, Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, Direction normal) {
		putQuadUV(ms, builder, v1, v2, v3, v4, 0, 0, 1, 1, normal);
	}

	public void putQuadUV(MatrixStack ms, VertexConsumer builder, Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, float minU, float minV, float maxU, float maxV, Direction normal) {
		putVertex(ms, builder, v1, minU, minV, normal);
		putVertex(ms, builder, v2, maxU, minV, normal);
		putVertex(ms, builder, v3, maxU, maxV, normal);
		putVertex(ms, builder, v4, minU, maxV, normal);
	}

	protected void putVertex(MatrixStack ms, VertexConsumer builder, Vec3d pos, float u, float v, Direction normal) {
		putVertex(ms, builder, (float) pos.x, (float) pos.y, (float) pos.z, u, v, normal);
	}

	protected void putVertex(MatrixStack pose, VertexConsumer builder, float x, float y, float z, float u, float v, Direction normal) {
		Color rgb = params.rgb;
		if (transformNormals == null)
			transformNormals = pose.peek().getNormal();

		int xOffset = 0;
		int yOffset = 0;
		int zOffset = 0;

		if (normal != null) {
			xOffset = normal.getOffsetX();
			yOffset = normal.getOffsetY();
			zOffset = normal.getOffsetZ();
		}

		builder.vertex(pose.peek().getModel(), x, y, z)
				.color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), rgb.getAlpha() * params.alpha)
				.texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(params.lightMap)
				.normal(pose.peek().getNormal(), xOffset, yOffset, zOffset).next();

		transformNormals = null;
	}

	public static class OutlineParams {
		protected Optional<SpecialTextures> faceTexture;
		protected Optional<SpecialTextures> hightlightedFaceTexture;
		protected Direction highlightedFace;
		protected boolean fadeLineWidth;
		protected boolean disableCull;
		protected boolean disableNormals;
		protected float alpha;
		protected int lightMap;
		protected Color rgb;
		private float lineWidth;

		public OutlineParams() {
			faceTexture = hightlightedFaceTexture = Optional.empty();
			alpha = 1;
			lineWidth = 1 / 32f;
			fadeLineWidth = true;
			rgb = Color.WHITE;
			lightMap = LightmapTextureManager.MAX_LIGHT_COORDINATE;
		}

		public OutlineParams colored(int color) {
			rgb = new Color(color, false);
			return this;
		}

		public OutlineParams highlightFace(@Nullable Direction face) {
			highlightedFace = face;
			return this;
		}

		public OutlineParams lineWidth(float width) {
			this.lineWidth = width;
			return this;
		}

		public float getLineWidth() {
			return fadeLineWidth ? alpha * lineWidth : lineWidth;
		}

		public Direction getHighlightedFace() {
			return highlightedFace;
		}
	}
}
