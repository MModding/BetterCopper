package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.magneticfield.MagneticField;
import com.mmodding.better_copper.mixin.accessors.WorldRendererAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {

	@Inject(method = "onPlaced", at = @At("HEAD"))
	private void injectPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
		if (!state.isOf(Blocks.NETHERITE_COATED_GOLD_BLOCK)) return;
		new MagneticField(world, pos, MinecraftClient.getInstance(), MinecraftClient.getInstance().getEntityRenderDispatcher().camera, WorldRendererAccessor.getFORCEFIELD());
	}
}
