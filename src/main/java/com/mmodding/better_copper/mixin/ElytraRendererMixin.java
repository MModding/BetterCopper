package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.charge.Charge;
import com.mmodding.better_copper.init.RenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ElytraFeatureRenderer.class)
public class ElytraRendererMixin {

	// Redirecting "getArmorClintConsumer"
	@Redirect(method = "Lnet/minecraft/client/render/entity/feature/ElytraFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getArmorGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;"))
	private VertexConsumer redirect(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LivingEntity livingEntity) {
		return RenderLayers.getArmorClintConsumer(provider, layer, solid, glint, Charge.isStackCharged(livingEntity.getEquippedStack(EquipmentSlot.CHEST)));
	}
}
