package com.mmodding.better_copper.materials;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class CopperToolsMaterial implements ToolMaterial {

	private static final CopperToolsMaterial INSTANCE = new CopperToolsMaterial();

	public static CopperToolsMaterial getInstance() {
		return INSTANCE;
	}

	@Override
	public int getDurability() {
		return 250;
	}

	@Override
	public float getMiningSpeedMultiplier() {
		return 6.0F;
	}

	@Override
	public float getAttackDamage() {
		return 1.5F;
	}

	@Override
	public int getMiningLevel() {
		return 1;
	}

	@Override
	public int getEnchantability() {
		return 0;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofItems(Items.COPPER_INGOT);
	}
}
