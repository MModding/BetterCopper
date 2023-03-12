package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.charge.Charge;
import com.mmodding.better_copper.init.RenderLayers;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	// Redirecting "getDirectItemGlintConsumer"
	@Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getDirectItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
	private VertexConsumer redirect(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint, ItemStack stack) {
		return RenderLayers.getDirectItemClintConsumer(provider, layer, solid, glint, Charge.isStackCharged(stack));
	}

	// Redirecting "getItemClintConsumer"
	@Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
	private VertexConsumer redirectItemClint(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean solid, boolean glint, ItemStack stack) {
		return RenderLayers.getItemClintConsumer(vertexConsumers, layer, solid, glint, Charge.isStackCharged(stack));
	}
}
