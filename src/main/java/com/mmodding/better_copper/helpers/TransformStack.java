package com.mmodding.better_copper.helpers;

import net.minecraft.client.util.math.MatrixStack;

/**
 * Credit: FlyWheel
 */
public interface TransformStack extends Rotate<TransformStack> {
	static TransformStack cast(MatrixStack stack) {
		return (TransformStack) stack;
	}
}
