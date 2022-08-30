package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.init.RenderLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin {

	@Shadow
	protected abstract Identifier getArmorTexture(ArmorItem item, boolean legs, @Nullable String overlay);

	// Redirecting "renderArmorParts" #1
	@Redirect(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/ArmorItem;ZLnet/minecraft/client/render/entity/model/BipedEntityModel;ZFFFLjava/lang/String;)V", ordinal = 0))
	private void redirectRenderArmor1(ArmorFeatureRenderer instance, MatrixStack matrixStack, VertexConsumerProvider providers, int light, ArmorItem item, boolean usesSecondLayer, BipedEntityModel model, boolean legs, float red, float green, float blue, @Nullable String overlay, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot) {
		VertexConsumer vertexConsumer = RenderLayers.getArmorClintConsumer(
				providers, RenderLayer.getArmorCutoutNoCull(this.getArmorTexture(item, legs, overlay)), false, usesSecondLayer, entity.getEquippedStack(armorSlot).getOrCreateNbt().getInt("charge") != 0
		);
		model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
	}

	// Redirecting "renderArmorParts" #2
	@Redirect(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/ArmorItem;ZLnet/minecraft/client/render/entity/model/BipedEntityModel;ZFFFLjava/lang/String;)V", ordinal = 1))
	private void redirectRenderArmor2(ArmorFeatureRenderer instance, MatrixStack matrixStack, VertexConsumerProvider providers, int light, ArmorItem item, boolean usesSecondLayer, BipedEntityModel model, boolean legs, float red, float green, float blue, @Nullable String overlay, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot) {
		VertexConsumer vertexConsumer = RenderLayers.getArmorClintConsumer(
				providers, RenderLayer.getArmorCutoutNoCull(this.getArmorTexture(item, legs, overlay)), false, usesSecondLayer, entity.getEquippedStack(armorSlot).getOrCreateNbt().getInt("charge") != 0
		);
		model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
	}

	// Redirecting "renderArmorParts" #3
	@Redirect(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/ArmorItem;ZLnet/minecraft/client/render/entity/model/BipedEntityModel;ZFFFLjava/lang/String;)V", ordinal = 2))
	private void redirectRenderArmor3(ArmorFeatureRenderer instance, MatrixStack matrixStack, VertexConsumerProvider providers, int light, ArmorItem item, boolean usesSecondLayer, BipedEntityModel model, boolean legs, float red, float green, float blue, @Nullable String overlay, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot) {
		VertexConsumer vertexConsumer = RenderLayers.getArmorClintConsumer(
				providers, RenderLayer.getArmorCutoutNoCull(this.getArmorTexture(item, legs, overlay)), false, usesSecondLayer, entity.getEquippedStack(armorSlot).getOrCreateNbt().getInt("charge") != 0
		);
		model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
	}
}
