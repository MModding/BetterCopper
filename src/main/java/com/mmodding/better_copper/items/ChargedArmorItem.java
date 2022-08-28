package com.mmodding.better_copper.items;

import com.mmodding.better_copper.armormaterials.CopperArmorMaterial;
import com.mmodding.better_copper.charge.ConsumeSource;
import com.mmodding.better_copper.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ChargedArmorItem extends ArmorItem {
	private ItemStack chargedArmorStack;

	public ChargedArmorItem(EquipmentSlot equipmentSlot, Item.Settings settings) {
		super(CopperArmorMaterial.getInstance(), equipmentSlot, settings);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!(entity instanceof LivingEntity livingEntity)) return;
		if (this.slot.getType() != EquipmentSlot.Type.ARMOR) return;
		this.chargedArmorStack = stack;
		int harvestEnergy = Blocks.COPPER_POWER_BLOCK.consumeEnergyIfConnected(livingEntity.world, livingEntity.getBlockPos(), ConsumeSource.ARMOR_CHARGE);
		if (harvestEnergy != 0) {
			System.out.println(harvestEnergy);
		}
		this.charge(this.chargedArmorStack, harvestEnergy);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		user.sendMessage(Text.of(Integer.toString(this.getCharge(this.chargedArmorStack))), true);
		return TypedActionResult.success(this.chargedArmorStack);
	}

	public void charge(ItemStack stack, int charge) {
		stack.getOrCreateNbt().putInt("charge", this.getCharge(stack) + charge);
	}

	public int getCharge(ItemStack stack) {
		return stack.getOrCreateNbt().getInt("charge");
	}

	public boolean isCharged(ItemStack stack) {
		return this.getCharge(stack) != 0;
	}

	public ItemStack getStack() {
		return this.chargedArmorStack;
	}
}
