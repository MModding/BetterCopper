package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.init.RenderLayers;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArmorRenderer.class)
public interface ArmorRendererMixin {

	// Redirect "getArmorGlintConsumer"
	@Redirect(method = "renderPart", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getArmorGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;"))
	private static VertexConsumer redirect(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack) {
		return RenderLayers.getArmorClintConsumer(provider, layer, solid, glint, stack.getOrCreateNbt().getInt("charge") != 0);
	}
}
