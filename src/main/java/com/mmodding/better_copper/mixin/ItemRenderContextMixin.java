package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.charge.Charge;
import com.mmodding.better_copper.init.RenderLayers;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.ItemRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderContext.class)
public class ItemRenderContextMixin {

	@Shadow
	private ItemStack itemStack;

	// Redirect "getItemGlintConsumer" #1
	@Redirect(method = "quadVertexConsumer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 0))
	private VertexConsumer redirectClint1(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean solid, boolean glint) {
		return RenderLayers.getItemClintConsumer(vertexConsumers, layer, solid, glint, Charge.isStackCharged(this.itemStack));
	}

	// Redirect "getItemGlintConsumer" #2
	@Redirect(method = "quadVertexConsumer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 1))
	private VertexConsumer redirectClint2(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean solid, boolean glint) {
		return RenderLayers.getItemClintConsumer(vertexConsumers, layer, solid, glint, Charge.isStackCharged(this.itemStack));
	}

	// Redirect "getDirectItemGlintConsumer" #1
	@Redirect(method = "quadVertexConsumer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getDirectItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 0))
	private VertexConsumer redirectDirectClint1(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean solid, boolean glint) {
		return RenderLayers.getDirectItemClintConsumer(vertexConsumers, layer, solid, glint, Charge.isStackCharged(this.itemStack));
	}

	// Redirect "getDirectItemGlintConsumer" #2
	@Redirect(method = "quadVertexConsumer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getDirectItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 1))
	private VertexConsumer redirectDirectClint2(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean solid, boolean glint) {
		return RenderLayers.getDirectItemClintConsumer(vertexConsumers, layer, solid, glint, Charge.isStackCharged(this.itemStack));
	}
}
