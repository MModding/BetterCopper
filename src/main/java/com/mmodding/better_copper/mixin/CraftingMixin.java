package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.init.EnerGeneration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public class CraftingMixin {

	@Inject(method = "onTakeItem", at = @At("HEAD"))
	private void injected(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
		Blocks.COPPER_POWER_BLOCK.addEnergyIfConnected(player.world, Utils.getOpenScreenPos(), EnerGeneration.CRAFTING);
	}
}
