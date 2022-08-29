package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.charge.GenerationSource;
import com.mmodding.better_copper.init.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(SugarCaneBlock.class)
public class SugarCaneMixin {

	@Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", shift = At.Shift.BY, by = 1))
	private void inject(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		Blocks.COPPER_POWER_BLOCK.addEnergyWithParticlesIfConnected(world, pos.up(), GenerationSource.GROTH, pos.up().up());
	}
}