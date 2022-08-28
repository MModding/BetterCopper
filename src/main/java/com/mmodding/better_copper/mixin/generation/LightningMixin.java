package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.init.GenerationSource;
import com.mmodding.better_copper.mixin.LightningEntityAccessor;
import net.minecraft.entity.LightningEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningEntity.class)
public class LightningMixin {

	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo ci) {
		LightningEntity lightningEntity = (LightningEntity) (Object) this;
		if (((LightningEntityAccessor) lightningEntity).getAmbientTick() == 2 && (!lightningEntity.world.isClient))
			Blocks.COPPER_POWER_BLOCK.addEnergyIfConnected(lightningEntity.world, lightningEntity.getBlockPos(), GenerationSource.LIGHTNING_STRIKE);
	}
}
