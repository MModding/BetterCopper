package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.init.RenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

	// Redirect getDirectItemGlintConsumer #1
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getDirectItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 0))
	private VertexConsumer redirectItemGlint1(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint, ItemStack stack) {
		return RenderLayers.getDirectItemClintConsumer(provider, layer, solid, glint, stack.getOrCreateNbt().getInt("charge") != 0);
	}

	// Redirect getDirectItemGlintConsumer #2
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getDirectItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 1))
	private VertexConsumer redirectItemGlint2(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint, ItemStack stack) {
		return RenderLayers.getDirectItemClintConsumer(provider, layer, solid, glint, stack.getOrCreateNbt().getInt("charge") != 0);
	}
}
