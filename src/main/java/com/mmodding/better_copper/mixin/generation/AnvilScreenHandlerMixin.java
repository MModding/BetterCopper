package com.mmodding.better_copper.mixin.generation;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.charge.Energy;
import com.mmodding.better_copper.charge.GenerationSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin {

	@Inject(method = "onTakeOutput", at = @At("HEAD"))
	private void injected(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
		Energy.addEnergyToPowerBlock(player.world, Utils.getOpenScreenPos(), GenerationSource.FORGE, stack.getCount(), Utils.getOpenScreenPos());
	}
}
