package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.charge.Energy;
import com.mmodding.better_copper.charge.GenerationSource;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningEntity.class)
public class LightningMixin {

	@Inject(method = "cleanOxidization", at = @At("HEAD"))
	private static void cleanOxidization(World world, BlockPos pos, CallbackInfo ci) {
		BlockState blockState = world.getBlockState(pos);
		BlockPos particlePos = pos.up();
		if (blockState.isOf(Blocks.LIGHTNING_ROD)) {
			particlePos = pos.offset(blockState.get(LightningRodBlock.FACING));
		}
		Energy.addEnergyToPowerBlock(world, pos, GenerationSource.LIGHTNING_STRIKE, particlePos);
	}
}
