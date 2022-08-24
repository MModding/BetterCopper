package com.mmodding.better_copper.client.render;

import com.mmodding.better_copper.Helper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public abstract class ValueBoxTransform {

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

	public static abstract class Sided extends ValueBoxTransform {

		protected Direction direction = Direction.UP;

		public Sided fromSide(Direction direction) {
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
			TransformStack.cast(ms).rotateY(yRot).rotateX(xRot);
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
}
