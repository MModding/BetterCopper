package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.charge.GenerationSource;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.mixin.EntityMixin;
import com.mmodding.mmodding_lib.library.utils.TickOperations;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends EntityMixin implements TickOperations {

	@Shadow
	private BlockState block;

	private int tick = 0;

	@Inject(method = "tick", at = @At("HEAD"))
	private void injectTick(CallbackInfo ci) {
		if (this.block.getBlock() == Blocks.NETHERITE_COATED_GOLD_BLOCK) {
			this.checkTickForOperation(20, () -> Blocks.COPPER_POWER_BLOCK.addEnergyWithParticlesIfConnected(
					this.world, this.getBlockPos(), GenerationSource.FALLING, this.getBlockPos().up()
			));
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
