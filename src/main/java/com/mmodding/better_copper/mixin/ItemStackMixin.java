package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.charge.Charge;
import com.mmodding.better_copper.init.GlintPackViews;
import com.mmodding.mmodding_lib.interface_injections.ItemGlintPack;
import com.mmodding.mmodding_lib.library.glint.GlintPackView;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemGlintPack {

	@Shadow
	public abstract Item getItem();

	@Override
	@Nullable
	public GlintPackView getGlintPackView() {
		return this.getItem() instanceof Charge ? GlintPackViews.COPPER_CLINT : this.getItem().getGlintPackView();
	}
}
