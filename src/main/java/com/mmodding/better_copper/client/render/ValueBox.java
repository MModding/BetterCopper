package com.mmodding.better_copper.client.render;

import com.mmodding.better_copper.Helper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;

import java.util.function.BiPredicate;

/**
 * Credit: Create
 */
public class ValueBox extends BoxOutline {

	Box targetBox;
	Box prevBox;

	protected Text label;
	protected Text sublabel = LiteralText.EMPTY;
	protected Text scrollTooltip = LiteralText.EMPTY;
	protected Vec3d labelOffset = Vec3d.ZERO;

	protected int passiveColor;
	protected int highlightColor;
	public boolean isPassive;

	protected BlockPos pos;
	protected ValueBox.Transform transform;
	protected BlockState blockState;

	public ValueBox(Text label, Box box, BlockPos pos) {
		super(box);
		this.setBounds(box);
		prevBox = box.expand(0);
		targetBox = box.expand(0);
		this.label = label;
		this.pos = pos;
		this.blockState = MinecraftClient.getInstance().world.getBlockState(pos);
	}

	public void setBounds(Box box) {
		this.box = box;
	}

	public ValueBox transform(ValueBox.Transform transform) {
		this.transform = transform;
		return this;
	}

	public ValueBox offsetLabel(Vec3d offset) {
		this.labelOffset = offset;
		return this;
	}

	public ValueBox withColors(int passive, int highlight) {
		this.passiveColor = passive;
		this.highlightColor = highlight;
		return this;
	}

	public ValueBox passive(boolean passive) {
		this.isPassive = passive;
		return this;
	}

	@Override
	public void tick() {
		prevBox = box;
		setBounds(interpolateBBs(box, targetBox, .5f));
	}

	@Override
	public void render(MatrixStack ms, SuperRenderTypeBuffer buffer, float pt) {
		renderBox(ms, buffer, interpolateBBs(prevBox, box, pt));
		boolean hasTransform = transform != null;
		if (transform instanceof ValueBox.Transform.Sided && params.getHighlightedFace() != null)
			((ValueBox.Transform.Sided) transform).fromSide(params.getHighlightedFace());
		if (hasTransform && !transform.shouldRender(blockState))
			return;

		ms.push();
		ms.translate(pos.getX(), pos.getY(), pos.getZ());
		if (hasTransform)
			transform.transform(blockState, ms);
		transformNormals = ms.peek().getNormal().copy();
		params.colored(isPassive ? passiveColor : highlightColor);
		super.render(ms, buffer, pt);

		float fontScale = hasTransform ? -transform.getFontScale() : -1 / 64f;
		ms.scale(fontScale, fontScale, fontScale);

		ms.push();
		renderContents(ms, buffer);
		ms.pop();

		if (!isPassive) {
			ms.push();
			ms.translate(17.5, -.5, 7);
			ms.translate(labelOffset.x, labelOffset.y, labelOffset.z);

			renderHoveringText(ms, buffer, label);
			if (!sublabel.getString().isEmpty()) {
				ms.translate(0, 10, 0);
				renderHoveringText(ms, buffer, sublabel);
			}
			if (!scrollTooltip.getString().isEmpty()) {
				ms.translate(0, 10, 0);
				renderHoveringText(ms, buffer, scrollTooltip, 0x998899, 0x111111);
			}

			ms.pop();
		}

		ms.pop();
	}

	private static Box interpolateBBs(Box current, Box target, float pt) {
		return new Box(MathHelper.lerp(pt, current.minX, target.minX), MathHelper.lerp(pt, current.minY, target.minY),
				MathHelper.lerp(pt, current.minZ, target.minZ), MathHelper.lerp(pt, current.maxX, target.maxX),
				MathHelper.lerp(pt, current.maxY, target.maxY), MathHelper.lerp(pt, current.maxZ, target.maxZ));
	}

	public void renderContents(MatrixStack ms, VertexConsumerProvider buffer) {}

	public static class TextValueBox extends ValueBox {
		Text text;

		public TextValueBox(Text label, Box box, BlockPos pos, Text text) {
			super(label, box, pos);
			this.text = text;
		}

		@Override
		public void renderContents(MatrixStack ms, VertexConsumerProvider buffer) {
			super.renderContents(ms, buffer);
			TextRenderer font = MinecraftClient.getInstance().textRenderer;
			float scale = 4;
			ms.scale(scale, scale, 1);
			ms.translate(-4, -4, 5);

			int stringWidth = font.getWidth(text);
			float numberScale = (float) font.fontHeight / stringWidth;
			boolean singleDigit = stringWidth < 10;
			if (singleDigit) numberScale = numberScale / 2;
			float verticalMargin = (stringWidth - font.fontHeight) / 2f;

			ms.scale(numberScale, numberScale, numberScale);
			ms.translate(singleDigit ? ((double) stringWidth / 2) : 0, singleDigit ? -verticalMargin : verticalMargin, 0);

			renderHoveringText(ms, buffer, text, 0xEDEDED, 0x4f4f4f);
		}
	}

	protected void renderHoveringText(MatrixStack ms, VertexConsumerProvider buffer, Text text) {
		renderHoveringText(ms, buffer, text, highlightColor, Helper.mixColors(passiveColor, 0, 0.75f));
	}

	protected void renderHoveringText(MatrixStack ms, VertexConsumerProvider buffer, Text text, int color, int shadowColor) {
		ms.push();
		drawString(ms, buffer, text, 0, 0, color);
		ms.translate(0, 0, -.25);
		drawString(ms, buffer, text, 1, 1, shadowColor);
		ms.pop();
	}

	private static void drawString(MatrixStack ms, VertexConsumerProvider buffer, Text text, float x, float y, int color) {
		MinecraftClient.getInstance().textRenderer.draw(text, x, y, color, false, ms.peek()
				.getModel(), buffer, false, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
	}

	public static abstract class Transform {

		protected float scale = getScale();

		protected abstract Vec3d getLocalOffset(BlockState state);

		protected abstract void rotate(BlockState state, MatrixStack ms);

		public boolean testHit(BlockState state, Vec3d localHit) {
			Vec3d offset = getLocalOffset(state);
			if (offset == null)
				return false;
			return localHit.distanceTo(offset) < scale / 2;
		}

		public void transform(BlockState state, MatrixStack ms) {
			Vec3d position = getLocalOffset(state);
			if (position == null)
				return;
			ms.translate(position.x, position.y, position.z);
			rotate(state, ms);
			ms.scale(scale, scale, scale);
		}

		public boolean shouldRender(BlockState state) {
			return state.getMaterial() != Material.AIR && getLocalOffset(state) != null;
		}

		protected float getScale() {
			return .4f;
		}

		protected float getFontScale() {
			return 1 / 64f;
		}

		public static abstract class Sided extends ValueBox.Transform {

			protected Direction direction = Direction.UP;

			public ValueBox.Transform.Sided fromSide(Direction direction) {
				this.direction = direction;
				return this;
			}

			@Override
			protected Vec3d getLocalOffset(BlockState state) {
				Vec3d location = getSouthLocation();
				location = Helper.rotateCentered(location, Helper.horizontalAngle(getSide()), Direction.Axis.Y);
				location = Helper.rotateCentered(location, Helper.verticalAngle(getSide()), Direction.Axis.X);
				return location;
			}

			protected abstract Vec3d getSouthLocation();

			@Override
			protected void rotate(BlockState state, MatrixStack ms) {
				float yRot = Helper.horizontalAngle(getSide()) + 180;
				float xRot = getSide() == Direction.UP ? 90 : getSide() == Direction.DOWN ? 270 : 0;
				ms.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yRot));
				ms.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(xRot));
			}

			@Override
			public boolean shouldRender(BlockState state) {
				return super.shouldRender(state) && isSideActive(state, getSide());
			}

			@Override
			public boolean testHit(BlockState state, Vec3d localHit) {
				return isSideActive(state, getSide()) && super.testHit(state, localHit);
			}

			protected boolean isSideActive(BlockState state, Direction direction) {
				return true;
			}

			public Direction getSide() {
				return direction;
			}
		}

		public static class Centered extends ValueBox.Transform.Sided {

			private final BiPredicate<BlockState, Direction> allowedDirections;

			public Centered(BiPredicate<BlockState, Direction> allowedDirections) {
				this.allowedDirections = allowedDirections;
			}

			@Override
			protected Vec3d getSouthLocation() {
				return Helper.voxelSpace(8, 8, 16);
			}

			@Override
			protected boolean isSideActive(BlockState blockState, Direction direction) {
				return allowedDirections.test(blockState, direction);
			}
		}
	}
}
