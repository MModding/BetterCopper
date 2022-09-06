package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.charge.Energy;
import com.mmodding.better_copper.charge.GenerationSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceOutputSlot.class)
public class FurnaceMixin {

	@Shadow
	@Final
	private PlayerEntity player;

	@Inject(method = "onTakeItem", at = @At("HEAD"))
	private void injected(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
		Energy.addEnergyToPowerBlock(player.world, Utils.getOpenScreenPos(), GenerationSource.SMELTING, stack.getCount());
	}

	@Inject(method = "takeStack", at = @At("HEAD"))
	private void injectTakeStack(int amount, CallbackInfoReturnable<ItemStack> cir) {
		Energy.addEnergyToPowerBlock(this.player.world, Utils.getOpenScreenPos(), GenerationSource.SMELTING, amount);
	}
}
