package com.mmodding.better_copper.mixin;

import net.minecraft.item.ArmorItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin {

	@Shadow
	@Final
	private static UUID[] MODIFIERS;

	/*
	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void injectAttributes(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Item.Settings settings, CallbackInfo ci, ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder) {
		if (Objects.equals(armorMaterial, CopperArmorMaterial.getInstance())) {
			UUID uUID = MODIFIERS[equipmentSlot.getEntitySlotId()];
			if (((ArmorItem) (Object) this) instanceof ChargedArmorItem chargedArmorItem) {
				builder.put(EntityAttributes.GENERIC_ARMOR_CHARGE, new EntityAttributeModifier(uUID, "Armor charge", 0, EntityAttributeModifier.Operation.ADDITION));
			}
		}
	}
	*/

}
