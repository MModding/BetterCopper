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

@Mixin(FurnaceOutputSlot.class)
public class FurnaceOutputSlotMixin {

	@Shadow
	@Final
	private PlayerEntity player;

	@Shadow
	private int amount;

	@Inject(method = "onCrafted(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
	private void injected(ItemStack stack, CallbackInfo ci) {
		Energy.addEnergyToPowerBlock(player.world, Utils.getOpenScreenPos(), GenerationSource.SMELTING, stack.getCount() * this.amount, Utils.getOpenScreenPos().up());
	}
}
