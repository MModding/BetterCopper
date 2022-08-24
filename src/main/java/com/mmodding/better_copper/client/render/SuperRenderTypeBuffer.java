package com.mmodding.better_copper.client.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.render.*;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.util.Util;

import java.util.SortedMap;

public class SuperRenderTypeBuffer implements VertexConsumerProvider {

	public static final SuperRenderTypeBuffer INSTANCE = new SuperRenderTypeBuffer();

	private final SuperRenderTypeBufferPhase defaultBuffer;
	private final SuperRenderTypeBufferPhase lateBuffer;

	public SuperRenderTypeBuffer() {
		defaultBuffer = new SuperRenderTypeBufferPhase();
		lateBuffer = new SuperRenderTypeBufferPhase();
	}

	@Override
	public VertexConsumer getBuffer(RenderLayer type) {
		return defaultBuffer.bufferSource.getBuffer(type);
	}

	public VertexConsumer getLateBuffer(RenderLayer type) {
		return lateBuffer.bufferSource.getBuffer(type);
	}

	private static class SuperRenderTypeBufferPhase {

		private final BlockBufferBuilderStorage fixedBufferPack = new BlockBufferBuilderStorage();
		private final SortedMap<RenderLayer, BufferBuilder> fixedBuffers = Util.make(new Object2ObjectLinkedOpenHashMap<>(), map -> {
			map.put(TexturedRenderLayers.getEntitySolid(), fixedBufferPack.get(RenderLayer.getSolid()));
			map.put(TexturedRenderLayers.getEntityCutout(), fixedBufferPack.get(RenderLayer.getCutout()));
			map.put(TexturedRenderLayers.getBannerPatterns(), fixedBufferPack.get(RenderLayer.getCutoutMipped()));
			map.put(TexturedRenderLayers.getEntityTranslucentCull(), fixedBufferPack.get(RenderLayer.getTranslucent()));
			put(map, TexturedRenderLayers.getShieldPatterns());
			put(map, TexturedRenderLayers.getBeds());
			put(map, TexturedRenderLayers.getShulkerBoxes());
			put(map, TexturedRenderLayers.getSign());
			put(map, TexturedRenderLayers.getChest());
			put(map, RenderLayer.getTranslucentNoCrumbling());
			put(map, RenderLayer.getArmorGlint());
			put(map, RenderLayer.getArmorEntityGlint());
			put(map, RenderLayer.getGlint());
			put(map, RenderLayer.getDirectGlint());
			put(map, RenderLayer.getGlintTranslucent());
			put(map, RenderLayer.getEntityGlint());
			put(map, RenderLayer.getDirectEntityGlint());
			put(map, RenderLayer.getWaterMask());
			put(map, RenderLayer.getSolid());
			ModelLoader.BLOCK_DESTRUCTION_RENDER_LAYERS.forEach((renderLayer) -> {
				put(map, renderLayer);
			});
		});
		private final VertexConsumerProvider.Immediate bufferSource = VertexConsumerProvider.immediate(fixedBuffers, new BufferBuilder(256));

		private static void put(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> map, RenderLayer type) {
			map.put(type, new BufferBuilder(type.getExpectedBufferSize()));
		}

	}
}
