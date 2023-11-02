package com.mmodding.better_copper.charge;

import net.minecraft.item.ItemStack;

public interface Charge {

	static void charge(ItemStack stack, int charge) {
		stack.getOrCreateNbt().putInt("charge", Charge.getCharge(stack) + charge);
	}

	static int getCharge(ItemStack stack) {
		if (stack.getNbt() == null) return 0;
		return stack.getOrCreateNbt().getInt("charge");
	}

	static boolean isStackCharged(ItemStack stack) {
		if (stack.getNbt() == null) return false;
		return stack.getNbt().getInt("charge") != 0;
	}

	static boolean isCharged(ItemStack stack) {
		if (stack.getNbt() == null) return false;
		return stack.getNbt().getInt("charge") != 0;
	}
}
