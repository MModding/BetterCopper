package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.charge.GenerationSource;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.mixin.EntityMixin;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends EntityMixin {

	@Shadow
	private BlockState block;

	private final AtomicInteger tick = new AtomicInteger(0);

	@Inject(method = "tick", at = @At("HEAD"))
	private void injectTick(CallbackInfo ci) {
		if (this.block.getBlock() == Blocks.NETHERITE_COATED_GOLD_BLOCK) {
			if (this.tick.get() < 20) {
				this.tick.set(this.tick.get() + 1);
			} else {
				Blocks.COPPER_POWER_BLOCK.addEnergyWithParticlesIfConnected(this.world, this.getBlockPos(), GenerationSource.FALLING, this.getBlockPos().up());
				this.tick.set(0);
			}
		}
	}
}
