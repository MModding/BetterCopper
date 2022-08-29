package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.init.RenderLayers;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

	@Shadow
	private ShieldEntityModel modelShield;

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

	// Redirect renderCanvas
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V"))
	private void redirect(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner, List<Pair<BannerPattern, DyeColor>> patterns, boolean glint, ItemStack stack) {
		SpriteIdentifier spriteIdentifier = BlockItem.getBlockEntityNbtFromStack(stack) != null ? ModelLoader.SHIELD_BASE : ModelLoader.SHIELD_BASE_NO_PATTERN;
		List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.getPatternsFromNbt(ShieldItem.getColor(stack), BannerBlockEntity.getPatternListTag(stack));
		RenderLayers.renderCanvas(matrices, vertexConsumers, light, overlay, this.modelShield.getPlate(), spriteIdentifier, false, list, stack.hasGlint(), stack.getOrCreateNbt().getInt("charge") != 0);
	}
}
