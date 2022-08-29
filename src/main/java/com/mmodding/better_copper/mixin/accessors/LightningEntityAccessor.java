package com.mmodding.better_copper.mixin.accessors;

import net.minecraft.entity.LightningEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LightningEntity.class)
public interface LightningEntityAccessor {

	@Accessor
	int getAmbientTick();
}
