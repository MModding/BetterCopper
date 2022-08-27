package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.init.GenerationSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceOutputSlot.class)
public class FurnaceMixin {

	@Inject(method = "onTakeItem", at = @At("HEAD"))
	private void injected(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
		Blocks.COPPER_POWER_BLOCK.addEnergyIfConnected(player, GenerationSource.SMELTING);
	}
}
