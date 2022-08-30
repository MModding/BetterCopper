package com.mmodding.better_copper.magneticfield;

import com.mmodding.better_copper.mixin.accessors.AreaHelperAccessor;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.AreaHelper;

import java.util.Optional;
import java.util.function.Predicate;

public class MagneticField extends WorldBorder {

	public MagneticField(World world, BlockPos blockPos, MinecraftClient minecraftClient, Camera camera, Identifier fieldTexture) {
		Optional<AreaHelper> square = getNewSquare(world, blockPos, Direction.Axis.X);
		if (square.isPresent()) {
			AreaHelper areaHelper = square.get();
			int width = ((AreaHelperAccessor) areaHelper).getWidth();
			int height = ((AreaHelperAccessor) areaHelper).getHeight();
			if (width == height) {
				BlockPos center = getCenter(((AreaHelperAccessor) areaHelper).invokeGetLowerCorner(blockPos), areaHelper, width);
				setCenter(center.getX(), center.getZ());
				setSize(width);
				setDamagePerBlock(0);
				render(minecraftClient, camera, fieldTexture);
			}
		}
	}

	public static BlockPos getCenter(BlockPos lowerCorner, AreaHelper areaHelper, int width) {
		BlockPos.Mutable mutable = (BlockPos.Mutable) lowerCorner;
		for (int i = 0; i <= width; ++i) {
			mutable.move(((AreaHelperAccessor) areaHelper).getNegativeDir(), -1);
		}
		return mutable;
	}

	public static Optional<AreaHelper> getNewSquare(World world, BlockPos blockPos, Direction.Axis axis) {
		return getOrEmpty(world, blockPos, AreaHelper::isValid, axis);
	}

	public static Optional<AreaHelper> getOrEmpty(World world, BlockPos blockPos, Predicate<AreaHelper> predicate, Direction.Axis axis) {
		Optional<AreaHelper> optional = Optional.of(new AreaHelper(world, blockPos, axis)).filter(predicate);
		if (optional.isPresent()) return optional;
		Direction.Axis axis2 = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
		return Optional.of(new AreaHelper(world, blockPos, axis2)).filter(predicate);
	}

	public void render(MinecraftClient minecraftClient, Camera camera, Identifier texture) {
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		MagneticField magneticField = this;
		double d = minecraftClient.options.method_38521() * 16;
		if (!(camera.getPos().x < magneticField.getBoundEast() - d)
				|| !(camera.getPos().x > magneticField.getBoundWest() + d)
				|| !(camera.getPos().z < magneticField.getBoundSouth() - d)
				|| !(camera.getPos().z > magneticField.getBoundNorth() + d)) {
			double e = 1.0 - magneticField.getDistanceInsideBorder(camera.getPos().x, camera.getPos().z) / d;
			e = Math.pow(e, 4.0);
			e = MathHelper.clamp(e, 0.0, 1.0);
			double f = camera.getPos().x;
			double g = camera.getPos().z;
			double h = minecraftClient.gameRenderer.method_32796();
			RenderSystem.enableBlend();
			RenderSystem.enableDepthTest();
			RenderSystem.blendFuncSeparate(
					GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ONE, GlStateManager.class_4534.ZERO
			);
			RenderSystem.setShaderTexture(0, texture);
			RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
			MatrixStack matrixStack = RenderSystem.getModelViewStack();
			matrixStack.push();
			RenderSystem.applyModelViewMatrix();
			int i = magneticField.getStage().getColor();
			float j = (float) (i >> 16 & 0xFF) / 255.0F;
			float k = (float) (i >> 8 & 0xFF) / 255.0F;
			float l = (float) (i & 0xFF) / 255.0F;
			RenderSystem.setShaderColor(j, k, l, (float) e);
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.polygonOffset(-3.0F, -3.0F);
			RenderSystem.enablePolygonOffset();
			RenderSystem.disableCull();
			float m = (float) (Util.getMeasuringTimeMs() % 3000L) / 3000.0F;
			float n = 0.0F;
			float o = 0.0F;
			float p = (float) (h - MathHelper.fractionalPart(camera.getPos().y));
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
			double q = Math.max(MathHelper.floor(g - d), magneticField.getBoundNorth());
			double r = Math.min(MathHelper.ceil(g + d), magneticField.getBoundSouth());
			if (f > magneticField.getBoundEast() - d) {
				float s = 0.0F;

				for (double t = q; t < r; s += 0.5F) {
					double u = Math.min(1.0, r - t);
					float v = (float) u * 0.5F;
					bufferBuilder.vertex(magneticField.getBoundEast() - f, -h, t - g).texture(m - s, m + p).next();
					bufferBuilder.vertex(magneticField.getBoundEast() - f, -h, t + u - g).texture(m - (v + s), m + p).next();
					bufferBuilder.vertex(magneticField.getBoundEast() - f, h, t + u - g).texture(m - (v + s), m + 0.0F).next();
					bufferBuilder.vertex(magneticField.getBoundEast() - f, h, t - g).texture(m - s, m + 0.0F).next();
					++t;
				}
			}

			if (f < magneticField.getBoundWest() + d) {
				float s = 0.0F;

				for (double t = q; t < r; s += 0.5F) {
					double u = Math.min(1.0, r - t);
					float v = (float) u * 0.5F;
					bufferBuilder.vertex(magneticField.getBoundWest() - f, -h, t - g).texture(m + s, m + p).next();
					bufferBuilder.vertex(magneticField.getBoundWest() - f, -h, t + u - g).texture(m + v + s, m + p).next();
					bufferBuilder.vertex(magneticField.getBoundWest() - f, h, t + u - g).texture(m + v + s, m + 0.0F).next();
					bufferBuilder.vertex(magneticField.getBoundWest() - f, h, t - g).texture(m + s, m + 0.0F).next();
					++t;
				}
			}

			q = Math.max(MathHelper.floor(f - d), magneticField.getBoundWest());
			r = Math.min(MathHelper.ceil(f + d), magneticField.getBoundEast());
			if (g > magneticField.getBoundSouth() - d) {
				float s = 0.0F;

				for (double t = q; t < r; s += 0.5F) {
					double u = Math.min(1.0, r - t);
					float v = (float) u * 0.5F;
					bufferBuilder.vertex(t - f, -h, magneticField.getBoundSouth() - g).texture(m + s, m + p).next();
					bufferBuilder.vertex(t + u - f, -h, magneticField.getBoundSouth() - g).texture(m + v + s, m + p).next();
					bufferBuilder.vertex(t + u - f, h, magneticField.getBoundSouth() - g).texture(m + v + s, m + 0.0F).next();
					bufferBuilder.vertex(t - f, h, magneticField.getBoundSouth() - g).texture(m + s, m + 0.0F).next();
					++t;
				}
			}

			if (g < magneticField.getBoundNorth() + d) {
				float s = 0.0F;

				for (double t = q; t < r; s += 0.5F) {
					double u = Math.min(1.0, r - t);
					float v = (float) u * 0.5F;
					bufferBuilder.vertex(t - f, -h, magneticField.getBoundNorth() - g).texture(m - s, m + p).next();
					bufferBuilder.vertex(t + u - f, -h, magneticField.getBoundNorth() - g).texture(m - (v + s), m + p).next();
					bufferBuilder.vertex(t + u - f, h, magneticField.getBoundNorth() - g).texture(m - (v + s), m + 0.0F).next();
					bufferBuilder.vertex(t - f, h, magneticField.getBoundNorth() - g).texture(m - s, m + 0.0F).next();
					++t;
				}
			}

			bufferBuilder.end();
			BufferRenderer.draw(bufferBuilder);
			RenderSystem.enableCull();
			RenderSystem.polygonOffset(0.0F, 0.0F);
			RenderSystem.disablePolygonOffset();
			RenderSystem.disableBlend();
			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();
			RenderSystem.depthMask(true);
		}
	}
}
