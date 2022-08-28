package com.mmodding.better_copper.items;

import com.mmodding.better_copper.armormaterials.CopperArmorMaterial;
import com.mmodding.better_copper.charge.Charge;
import com.mmodding.better_copper.charge.ConsumeSource;
import com.mmodding.better_copper.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ChargedArmorItem extends ArmorItem implements Charge {

	private ItemStack chargedArmorStack;

	public ChargedArmorItem(EquipmentSlot equipmentSlot, Item.Settings settings) {
		super(CopperArmorMaterial.getInstance(), equipmentSlot, settings);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!(entity instanceof LivingEntity livingEntity)) return;
		if (this.slot.getType() != EquipmentSlot.Type.ARMOR) return;
		this.chargedArmorStack = stack;
		charge(this.chargedArmorStack, Blocks.COPPER_POWER_BLOCK.consumeEnergyWithParticlesIfConnected(livingEntity.world, livingEntity.getBlockPos(), ConsumeSource.ARMOR_CHARGE, livingEntity.getBlockPos()).getLeft());
	}

	public ItemStack getStack() {
		return this.chargedArmorStack;
	}
}
