package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.init.RenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin {

	// Redirecting "getArmorClintConsumer"
	@Redirect(method = "renderArmorParts", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getArmorGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;"))
	private VertexConsumer redirect(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item) {
		return RenderLayers.getArmorClintConsumer(provider, layer, solid, glint, item.getDefaultStack().getOrCreateNbt().getInt("charge") != 0);
	}
}
