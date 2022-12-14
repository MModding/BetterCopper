package com.mmodding.better_copper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Helper {

	public static final Vec3d CENTER_OF_ORIGIN = new Vec3d(.5, .5, .5);

	public static Vec3d rotateCentered(Vec3d vec, double deg, Direction.Axis axis) {
		Vec3d shift = getCenterOf(BlockPos.ZERO);
		return rotate(vec.subtract(shift), deg, axis).add(shift);
	}

	public static Vec3d voxelSpace(double x, double y, double z) {
		return new Vec3d(x, y, z).multiply(1 / 16f);
	}

	public static float horizontalAngle(Direction facing) {
		if (facing.getAxis().isVertical())
			return 0;
		float angle = facing.asRotation();
		if (facing.getAxis() == Direction.Axis.X)
			angle = -angle;
		return angle;
	}

	public static float verticalAngle(Direction facing) {
		return facing == Direction.UP ? -90 : facing == Direction.DOWN ? 90 : 0;
	}

	public static Vec3d rotate(Vec3d vec, double deg, Direction.Axis axis) {
		if (deg == 0)
			return vec;
		if (vec == Vec3d.ZERO)
			return vec;

		float angle = (float) (deg / 180f * Math.PI);
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double x = vec.x;
		double y = vec.y;
		double z = vec.z;

		if (axis == Direction.Axis.X)
			return new Vec3d(x, y * cos - z * sin, z * cos + y * sin);
		if (axis == Direction.Axis.Y)
			return new Vec3d(x * cos + z * sin, y, z * cos - x * sin);
		if (axis == Direction.Axis.Z)
			return new Vec3d(x * cos - y * sin, y * cos + x * sin, z);
		return vec;
	}

	public static Vec3d getCenterOf(Vec3i pos) {
		if (pos.equals(Vec3i.ZERO))
			return CENTER_OF_ORIGIN;
		return Vec3d.of(pos).add(.5f, .5f, .5f);
	}

	public static Vec3d axisAlingedPlaneOf(Vec3d vec) {
		vec = vec.normalize();
		return new Vec3d(1, 1, 1).subtract(Math.abs(vec.x), Math.abs(vec.y), Math.abs(vec.z));
	}

	public static int mixColors(int color1, int color2, float w) {
		int a1 = (color1 >> 24);
		int r1 = (color1 >> 16) & 0xFF;
		int g1 = (color1 >> 8) & 0xFF;
		int b1 = color1 & 0xFF;
		int a2 = (color2 >> 24);
		int r2 = (color2 >> 16) & 0xFF;
		int g2 = (color2 >> 8) & 0xFF;
		int b2 = color2 & 0xFF;

		return ((int) (a1 + (a2 - a1) * w) << 24) + ((int) (r1 + (r2 - r1) * w) << 16) + ((int) (g1 + (g2 - g1) * w) << 8) + ((int) (b1 + (b2 - b1) * w));
	}
}
