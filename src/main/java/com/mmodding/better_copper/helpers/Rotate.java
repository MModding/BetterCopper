package com.mmodding.better_copper.helpers;

import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

/**
 * Credit: FlyWheel
 */
public interface Rotate<Self> {
	Self multiply(Quaternion quaternion);

	default Self rotateX(double angle) {
		return multiply(Vec3f.POSITIVE_X, angle);
	}

	default Self rotateY(double angle) {
		return multiply(Vec3f.POSITIVE_Y, angle);
	}

	default Self multiply(Vec3f axis, double angle) {
		if (angle == 0)
			return (Self) this;
		return multiply(axis.getDegreesQuaternion((float) angle));
	}
}
