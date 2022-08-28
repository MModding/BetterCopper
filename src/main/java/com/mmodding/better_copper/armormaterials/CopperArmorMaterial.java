package com.mmodding.better_copper.armormaterials;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;

public class CopperArmorMaterial implements ArmorMaterial {

	private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
	private static final int[] PROTECTION_VALUES = new int[]{2, 3, 4, 2};

	@Override
	public int getDurability(EquipmentSlot slot) {
		return BASE_DURABILITY[slot.getEntitySlotId()] * 6;
	}

	@Override
	public int getProtectionAmount(EquipmentSlot slot) {
		return PROTECTION_VALUES[slot.getEntitySlotId()];
	}

	@Override
	public int getEnchantability() {
		return 16;
	}

	@Override
	public SoundEvent getEquipSound() {
		return SoundEvents.ITEM_ARMOR_EQUIP_CHAIN;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return new Lazy<>(() -> Ingredient.ofItems(Items.COPPER_INGOT)).get();
	}

	@Override
	public String getName() {
		return "copper";
	}

	@Override
	public float getToughness() {
		return 1.0F;
	}

	@Override
	public float getKnockbackResistance() {
		return 0.0F;
	}
}
