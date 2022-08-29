package com.mmodding.better_copper.items;

import com.mmodding.better_copper.armormaterials.CopperArmorMaterial;
import com.mmodding.better_copper.charge.Charge;
import com.mmodding.better_copper.charge.ConsumeSource;
import com.mmodding.better_copper.init.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ChargedArmorItem extends ArmorItem implements Charge {

	private final AtomicInteger tick = new AtomicInteger(0);

	public ChargedArmorItem(EquipmentSlot equipmentSlot, Item.Settings settings) {
		super(CopperArmorMaterial.getInstance(), equipmentSlot, settings);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (this.tick.get() < 20) {
			this.tick.set(this.tick.get() + 1);
		} else {
			if (!(entity instanceof LivingEntity livingEntity)) return;
			if (this.slot.getType() != EquipmentSlot.Type.ARMOR) return;
			this.charge(stack, Blocks.COPPER_POWER_BLOCK.consumeEnergyWithParticlesIfConnected(livingEntity.world, livingEntity.getBlockPos(), ConsumeSource.ARMOR_CHARGE, livingEntity.getBlockPos()).getLeft());
			if (this.isCharged(stack)) {
				this.changeProtection(stack, slot);
			}
			this.tick.set(0);
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.of("Charge : " + this.getCharge(stack)));
	}

	public void changeProtection(ItemStack stack, int slot) {
		float charge = (float) this.getCharge(stack) / 50;
		this.protection = (int) (this.getProtection() * charge);
		EquipmentSlot equipmentSlot = EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, slot);
		Collection<EntityAttributeModifier> entityAttributeModifiers = this.getAttributeModifiers(equipmentSlot).get(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ARMOR);
		entityAttributeModifiers.forEach(entityAttributeModifier -> {
			UUID uUID = entityAttributeModifier.getId();
			String name = entityAttributeModifier.getName();
			if (name.equals("Armor modifier")) {
				EntityAttributeModifier modifier = new EntityAttributeModifier(uUID, name, this.protection, EntityAttributeModifier.Operation.ADDITION);
				stack.addAttributeModifier(EntityAttributes.GENERIC_ARMOR, modifier, equipmentSlot);
			}
		});
	}
}
