package com.mmodding.better_copper.mixin.generation;

import com.llamalad7.mixinextras.sugar.Local;
import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.charge.Energy;
import com.mmodding.better_copper.charge.GenerationSource;
import com.mmodding.mmodding_lib.library.utils.Self;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.SmithingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin implements Self<ForgingScreenHandler> {

	@Inject(method = "quickTransfer", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;onQuickTransfer(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V"))
	private void quickTransfer(PlayerEntity player, int fromIndex, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 1) ItemStack itemStack2) {
		if (this.getObject() instanceof AnvilScreenHandler) {
			Energy.addEnergyToPowerBlock(player.world, Utils.getOpenScreenPos(), GenerationSource.FORGE, itemStack2.getCount());
		}
		else if (this.getObject() instanceof SmithingScreenHandler) {
			Energy.addEnergyToPowerBlock(player.world, Utils.getOpenScreenPos(), GenerationSource.SMITHING, itemStack2.getCount());
		}
	}
}
