package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.charge.Energy;
import com.mmodding.better_copper.charge.GenerationSource;
import com.mmodding.better_copper.magneticfield.LoopAreaHelper;
import com.mmodding.better_copper.mixin.EntityMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

	/*
	@Inject(method = "createLivingAttributes", at = @At("RETURN"), cancellable = true)
	private static void addLivingEntityAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.setReturnValue(cir.getReturnValue().add(EntityAttributes.GENERIC_ARMOR_CHARGE));
	}
	*/

	@Shadow
	public abstract boolean isAlive();

	@Shadow
	public abstract boolean isInsideWall();

	@Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setPose(Lnet/minecraft/entity/EntityPose;)V", shift = At.Shift.AFTER))
	private void onDeath(DamageSource source, CallbackInfo ci) {
		LivingEntity livingEntity = ((LivingEntity) (Object) this);
		Energy.addEnergyToPowerBlock(livingEntity.world, livingEntity.getBlockPos().down(), GenerationSource.DEATH);
	}

	@Inject(method = "baseTick", at = @At("HEAD"))
	private void injectBaseTick(CallbackInfo ci) {
		LivingEntity livingEntity = ((LivingEntity) (Object) this);
		if (this.isAlive() && !this.isInsideWall() && LoopAreaHelper.getInstance().isPlayerInField(this.getBoundingBox())) {
			if (isEntityWearing(livingEntity, new ArrayList<>(Arrays.asList(Items.IRON_HELMET.getDefaultStack(),
					Items.IRON_CHESTPLATE.getDefaultStack(), Items.IRON_LEGGINGS.getDefaultStack(), Items.IRON_BOOTS.getDefaultStack())))) {
				livingEntity.addVelocity(0D, 0.5D, 0D);
			} else if (isEntityWearing(livingEntity, new ArrayList<>(Arrays.asList(Items.GOLDEN_HELMET.getDefaultStack(),
					Items.GOLDEN_CHESTPLATE.getDefaultStack(), Items.GOLDEN_LEGGINGS.getDefaultStack(), Items.GOLDEN_BOOTS.getDefaultStack())))) {
				livingEntity.addVelocity(0D, 1D, 1D);
			} else if (isEntityWearing(livingEntity, new ArrayList<>(Arrays.asList(Items.NETHERITE_HELMET.getDefaultStack(),
					Items.NETHERITE_CHESTPLATE.getDefaultStack(), Items.NETHERITE_LEGGINGS.getDefaultStack(), Items.NETHERITE_BOOTS.getDefaultStack())))) {
				livingEntity.addVelocity(0D, 1.5D, 0D);
			}
		}
	}

	private boolean isEntityWearing(LivingEntity livingEntity, List<ItemStack> itemsEquipped) {
		Set<ItemStack> getItemsEquipped = new HashSet<>();
		livingEntity.getItemsEquipped().forEach(getItemsEquipped::add);
		return getItemsEquipped.contains(itemsEquipped.get(0)) &&
				getItemsEquipped.contains(itemsEquipped.get(1)) &&
				getItemsEquipped.contains(itemsEquipped.get(2)) &&
				getItemsEquipped.contains(itemsEquipped.get(3));
	}
}
