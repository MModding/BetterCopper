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
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
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

	@Shadow
	protected abstract boolean usesSecondLayer(EquipmentSlot slot);

	// Redirecting "renderArmorParts" #1
	@Redirect(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/ArmorItem;ZLnet/minecraft/client/render/entity/model/BipedEntityModel;ZFFFLjava/lang/String;)V", ordinal = 0))
	private void redirectRenderArmor1(ArmorFeatureRenderer instance, MatrixStack matrixStack, VertexConsumerProvider providers, int light, ArmorItem item, boolean usesSecondLayer, BipedEntityModel model, boolean legs, float red, float green, float blue, @Nullable String overlay, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot) {
		ItemStack itemStack = entity.getEquippedStack(armorSlot);
		ArmorItem armorItem = (ArmorItem) itemStack.getItem();
		int i = ((DyeableArmorItem) armorItem).getColor(itemStack);
		float f = (float) (i >> 16 & 0xFF) / 255.0F;
		float g = (float) (i >> 8 & 0xFF) / 255.0F;
		float h = (float) (i & 0xFF) / 255.0F;
		renderArmorParts(matrices, vertexConsumers, light, armorItem, itemStack.hasGlint(), model, this.usesSecondLayer(armorSlot), f, g, h, null, itemStack.getOrCreateNbt().getInt("charge") != 0);
	}

	// Redirecting "renderArmorParts" #2
	@Redirect(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/ArmorItem;ZLnet/minecraft/client/render/entity/model/BipedEntityModel;ZFFFLjava/lang/String;)V", ordinal = 1))
	private void redirectRenderArmor2(ArmorFeatureRenderer instance, MatrixStack matrixStack, VertexConsumerProvider providers, int light, ArmorItem item, boolean usesSecondLayer, BipedEntityModel model, boolean legs, float red, float green, float blue, @Nullable String overlay, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot) {
		ItemStack itemStack = entity.getEquippedStack(armorSlot);
		renderArmorParts(matrices, vertexConsumers, light, (ArmorItem) itemStack.getItem(), itemStack.hasGlint(), model, this.usesSecondLayer(armorSlot), 1.0F, 1.0F, 1.0F, "overlay", itemStack.getOrCreateNbt().getInt("charge") != 0);
	}

	// Redirecting "renderArmorParts" #3
	@Redirect(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/ArmorItem;ZLnet/minecraft/client/render/entity/model/BipedEntityModel;ZFFFLjava/lang/String;)V", ordinal = 2))
	private void redirectRenderArmor3(ArmorFeatureRenderer instance, MatrixStack matrixStack, VertexConsumerProvider providers, int light, ArmorItem item, boolean usesSecondLayer, BipedEntityModel model, boolean legs, float red, float green, float blue, @Nullable String overlay, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot) {
		ItemStack itemStack = entity.getEquippedStack(armorSlot);
		renderArmorParts(matrices, vertexConsumers, light, (ArmorItem) itemStack.getItem(), itemStack.hasGlint(), model, this.usesSecondLayer(armorSlot), 1.0F, 1.0F, 1.0F, null, itemStack.getOrCreateNbt().getInt("charge") != 0);
	}

	private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean isGlint, BipedEntityModel model, boolean legs, float red, float green, float blue, @Nullable String overlay, boolean isCharged) {
		VertexConsumer vertexConsumer = RenderLayers.getArmorClintConsumer(
				vertexConsumers, RenderLayer.getArmorCutoutNoCull(this.getArmorTexture(item, legs, overlay)), false, isGlint, isCharged
		);
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
	}
}
