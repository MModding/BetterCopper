package com.mmodding.better_copper.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.mmodding.better_copper.armormaterials.CopperArmorMaterial;
import com.mmodding.better_copper.init.EntityAttributes;
import com.mmodding.better_copper.items.ChargedArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;
import java.util.UUID;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin {

	@Shadow
	@Final
	private static UUID[] MODIFIERS;

	@Shadow
	public abstract ArmorMaterial getMaterial();

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void injectAttributes(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Item.Settings settings, CallbackInfo ci, ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder) {
		if (Objects.equals(armorMaterial, CopperArmorMaterial.getInstance())) {
			UUID uUID = MODIFIERS[equipmentSlot.getEntitySlotId()];
			if (((ArmorItem) (Object) this) instanceof ChargedArmorItem chargedArmorItem) {
				builder.put(EntityAttributes.GENERIC_ARMOR_CHARGE, new EntityAttributeModifier(uUID, "Armor charge", chargedArmorItem.getCharge(chargedArmorItem.getStack()), EntityAttributeModifier.Operation.ADDITION));
			}
		}
	}

	@Inject(method = "getProtection", at = @At(value = "HEAD"), cancellable = true)
	private void changeProtection(CallbackInfoReturnable<Integer> cir) {
		if (Objects.equals(this.getMaterial(), CopperArmorMaterial.getInstance())) {
			if (((ArmorItem) (Object) this) instanceof ChargedArmorItem chargedArmorItem) {
				if (chargedArmorItem.isCharged(chargedArmorItem.getStack())) {
					cir.setReturnValue(cir.getReturnValue() * chargedArmorItem.getCharge(chargedArmorItem.getStack()) / 50);
				}
			}
		}
	}
}
