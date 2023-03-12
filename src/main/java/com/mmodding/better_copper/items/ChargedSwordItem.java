package com.mmodding.better_copper.items;

import com.mmodding.better_copper.charge.Charge;
import com.mmodding.better_copper.charge.ConsumeSource;
import com.mmodding.better_copper.charge.Energy;
import com.mmodding.better_copper.materials.CopperToolsMaterial;
import com.mmodding.mmodding_lib.library.utils.TickOperations;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ChargedSwordItem extends SwordItem implements Charge, TickOperations {

	private int tick = 0;

	public ChargedSwordItem(Item.Settings settings) {
		super(CopperToolsMaterial.getInstance(), 3, -2.4f, settings);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		this.checkTickForOperation(20, () -> {
			if (!(entity instanceof LivingEntity livingEntity)) return;
			this.charge(stack, Energy.removeEnergyFromPowerBlock(livingEntity.world, livingEntity.getBlockPos(), ConsumeSource.ARMOR_CHARGE, livingEntity.getBlockPos()));
			if (this.isCharged(stack)) {
				this.changeAttributes(stack, slot);
			}
		});
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.literal("Charge : " + this.getCharge(stack)).formatted(Formatting.ITALIC, Formatting.GRAY));
	}

	public void changeAttributes(ItemStack stack, int slot) {
		try {
			float multiplier = (float) this.getCharge(stack) / 50;
			this.attackDamage = this.getAttackDamage() * multiplier;
			EquipmentSlot equipmentSlot = EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.HAND, slot);
			Collection<EntityAttributeModifier> entityAttributeModifiers = this.getAttributeModifiers(equipmentSlot).get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
			entityAttributeModifiers.forEach(entityAttributeModifier -> {
				UUID uuID = entityAttributeModifier.getId();
				String name = entityAttributeModifier.getName();
				double value = entityAttributeModifier.getValue();
				if (name.equals("Weapon modifier")) {
					EntityAttributeModifier modifier = new EntityAttributeModifier(uuID, name, this.attackDamage, EntityAttributeModifier.Operation.ADDITION);
					stack.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, modifier, equipmentSlot);
				}
			});
		} catch (IllegalArgumentException ignored) {}
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
