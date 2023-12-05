package com.mmodding.better_copper.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mmodding.better_copper.charge.Charge;
import com.mmodding.better_copper.charge.ConsumeSource;
import com.mmodding.better_copper.charge.Energy;
import com.mmodding.better_copper.materials.CopperArmorMaterial;
import com.mmodding.mmodding_lib.library.items.CustomArmorItem;
import com.mmodding.mmodding_lib.library.utils.TickOperations;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ChargedArmorItem extends CustomArmorItem implements Charge, TickOperations {

	private static final UUID ATTRIBUTE_MODIFIER_UUID = UUID.fromString("9237f704-0ddb-4118-a5d6-366d1cf54931");

	private int tick = 0;

	public ChargedArmorItem(EquipmentSlot equipmentSlot, Item.Settings settings) {
		super(CopperArmorMaterial.getInstance(), equipmentSlot, settings);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		this.checkTickForOperation(20, () -> {
			if (!(entity instanceof LivingEntity livingEntity)) return;
			if (this.slot.getType() != EquipmentSlot.Type.ARMOR) return;
			Charge.charge(stack, Energy.removeEnergyFromPowerBlock(livingEntity.world, livingEntity.getBlockPos(), ConsumeSource.ARMOR_CHARGE, livingEntity.getBlockPos()));
		});
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.literal("Charge : " + Charge.getCharge(stack)).formatted(Formatting.ITALIC, Formatting.GRAY));
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
		if (Charge.isCharged(stack)) {
			float multiplier = (float) Charge.getCharge(stack) / 50;

			EntityAttributeModifier modifier = new EntityAttributeModifier(
				ChargedArmorItem.ATTRIBUTE_MODIFIER_UUID,
				"Charged Armor multiplier",
				multiplier,
				EntityAttributeModifier.Operation.MULTIPLY_TOTAL
			);

			ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> attributeModifiers = ImmutableMultimap.builder();

			attributeModifiers.putAll(super.getAttributeModifiers(stack, slot));
			attributeModifiers.put(EntityAttributes.GENERIC_ARMOR, modifier);

			return attributeModifiers.build();
		}
		else {
			return super.getAttributeModifiers(stack, slot);
		}
	}

	@Override
	public int getTickValue() {
		return this.tick;
	}

	@Override
	public void setTickValue(int tickValue) {
		this.tick = tickValue;
	}
}
