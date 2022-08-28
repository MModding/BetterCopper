package com.mmodding.better_copper.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mmodding.better_copper.armormaterials.CopperArmorMaterial;
import com.mmodding.better_copper.charge.Charge;
import com.mmodding.better_copper.charge.ConsumeSource;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.init.EntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.UUID;

public class ChargedArmorItem extends ArmorItem implements Charge {

	private static final UUID[] MODIFIERS = new UUID[]{
			UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
			UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
			UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
			UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
	};
	private final ItemStack chargedArmorStack = new ItemStack(this);
	public static ArmorMaterial copperArmorMaterial = new CopperArmorMaterial();
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public ChargedArmorItem(EquipmentSlot equipmentSlot, Item.Settings settings) {
		super(copperArmorMaterial, equipmentSlot, settings);

		UUID uUID = MODIFIERS[equipmentSlot.getEntitySlotId()];
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(
				net.minecraft.entity.attribute.EntityAttributes.GENERIC_ARMOR,
				new EntityAttributeModifier(uUID, "Armor modifier", getChargedProtectionAmount(equipmentSlot), EntityAttributeModifier.Operation.ADDITION)
		);
		builder.put(
				net.minecraft.entity.attribute.EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
				new EntityAttributeModifier(uUID, "Armor toughness", copperArmorMaterial.getToughness(), EntityAttributeModifier.Operation.ADDITION)
		);
		builder.put(
				EntityAttributes.GENERIC_ARMOR_CHARGE,
				new EntityAttributeModifier(uUID, "Armor charge", getCharge(chargedArmorStack), EntityAttributeModifier.Operation.ADDITION)
		);
		this.attributeModifiers = builder.build();
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == this.slot ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}

	private int getChargedProtectionAmount(EquipmentSlot equipmentSlot) {
		if (!isCharged(chargedArmorStack))
			return copperArmorMaterial.getProtectionAmount(equipmentSlot);
		return copperArmorMaterial.getProtectionAmount(equipmentSlot) * (getCharge(chargedArmorStack) / 50);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!(entity instanceof LivingEntity livingEntity)) return;
		if (this.slot.getType() != EquipmentSlot.Type.ARMOR) return;
		int harvestEnergy = Blocks.COPPER_POWER_BLOCK.consumeEnergyIfConnected(livingEntity.world, livingEntity.getBlockPos(), ConsumeSource.ARMOR_CHARGE);
		charge(chargedArmorStack, harvestEnergy);
	}
}
