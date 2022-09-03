package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.charge.GenerationSource;
import com.mmodding.better_copper.init.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingResultSlot.class)
public class CraftingMixin {

	@Shadow
	@Final
	private PlayerEntity player;

	@Inject(method = "onTakeItem", at = @At("HEAD"))
	private void injected(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
		Blocks.COPPER_POWER_BLOCK.addEnergyIfConnected(player.world, Utils.getOpenScreenPos(), GenerationSource.CRAFTING, stack.getCount());
	}

	@Inject(method = "takeStack", at = @At("HEAD"))
	private void injectTakeStack(int amount, CallbackInfoReturnable<ItemStack> cir) {
		Blocks.COPPER_POWER_BLOCK.addEnergyIfConnected(this.player.world, Utils.getOpenScreenPos(), GenerationSource.CRAFTING, amount);
	}

	@Inject(method = "onTake", at = @At("HEAD"))
	private void injectOnTake(int amount, CallbackInfo ci) {
		Blocks.COPPER_POWER_BLOCK.addEnergyIfConnected(this.player.world, Utils.getOpenScreenPos(), GenerationSource.CRAFTING, amount);
	}
}
