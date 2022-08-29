package com.mmodding.better_copper.charge;

import net.minecraft.item.ItemStack;

public interface Charge {

	default void charge(ItemStack stack, int charge) {
		stack.getOrCreateNbt().putInt("charge", this.getCharge(stack) + charge);
	}

	default int getCharge(ItemStack stack) {
		return stack.getOrCreateNbt().getInt("charge");
	}

	default boolean isCharged(ItemStack stack) {
		return getCharge(stack) != 0;
	}
}
