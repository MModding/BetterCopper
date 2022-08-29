package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.charge.GenerationSource;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.init.EntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

/*
	@Inject(method = "createLivingAttributes", at = @At("RETURN"), cancellable = true)
	private static void addLivingEntityAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.setReturnValue(cir.getReturnValue().add(EntityAttributes.GENERIC_ARMOR_CHARGE));
	}
*/

	@Inject(method = "onDeath", at = @At("HEAD"))
	private void onDeath(DamageSource source, CallbackInfo ci) {
		LivingEntity livingEntity = ((LivingEntity) (Object) this);
		Blocks.COPPER_POWER_BLOCK.addEnergyWithParticlesIfConnected(livingEntity.world, livingEntity.getBlockPos(), GenerationSource.DEATH, 1);
	}
}
