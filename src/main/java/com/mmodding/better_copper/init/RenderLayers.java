package com.mmodding.better_copper.init;

import com.mmodding.better_copper.mixin.accessors.RenderLayerAccessor;
import com.mmodding.better_copper.mixin.accessors.RenderPhaseAccessor;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

public class RenderLayers implements ElementsInitializer, ClientElementsInitializer {

	public static final Identifier CHARGED_ITEM_GLINT = new Identifier("textures/misc/forcefield.png");

	private static final RenderLayer ARMOR_CLINT = RenderLayerAccessor.of(
			"armor_clint",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(RenderPhaseAccessor.getARMOR_GLINT_SHADER())
					.texture(new RenderPhase.Texture(CHARGED_ITEM_GLINT, true, false))
					.writeMaskState(RenderPhaseAccessor.getCOLOR_MASK())
					.cull(RenderPhaseAccessor.getDISABLE_CULLING())
					.depthTest(RenderPhaseAccessor.getEQUAL_DEPTH_TEST())
					.transparency(RenderPhaseAccessor.getGLINT_TRANSPARENCY())
					.texturing(RenderPhaseAccessor.getGLINT_TEXTURING())
					.layering(RenderPhaseAccessor.getVIEW_OFFSET_Z_LAYERING())
					.build(false)
	);

	private static final RenderLayer ARMOR_ENTITY_CLINT = RenderLayerAccessor.of(
			"armor_entity_clint",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(RenderPhaseAccessor.getARMOR_ENTITY_GLINT_SHADER())
					.texture(new RenderPhase.Texture(CHARGED_ITEM_GLINT, true, false))
					.writeMaskState(RenderPhaseAccessor.getCOLOR_MASK())
					.cull(RenderPhaseAccessor.getDISABLE_CULLING())
					.depthTest(RenderPhaseAccessor.getEQUAL_DEPTH_TEST())
					.transparency(RenderPhaseAccessor.getGLINT_TRANSPARENCY())
					.texturing(RenderPhaseAccessor.getENTITY_GLINT_TEXTURING())
					.layering(RenderPhaseAccessor.getVIEW_OFFSET_Z_LAYERING())
					.build(false)
	);

	private static final RenderLayer CLINT_TRANSLUCENT = RenderLayerAccessor.of(
			"clint_translucent",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(RenderPhaseAccessor.getTRANSLUCENT_GLINT_SHADER())
					.texture(new RenderPhase.Texture(CHARGED_ITEM_GLINT, true, false))
					.writeMaskState(RenderPhaseAccessor.getCOLOR_MASK())
					.cull(RenderPhaseAccessor.getDISABLE_CULLING())
					.depthTest(RenderPhaseAccessor.getEQUAL_DEPTH_TEST())
					.transparency(RenderPhaseAccessor.getGLINT_TRANSPARENCY())
					.texturing(RenderPhaseAccessor.getGLINT_TEXTURING())
					.target(RenderPhaseAccessor.getITEM_TARGET())
					.build(false)
	);

	private static final RenderLayer CLINT = RenderLayerAccessor.of(
			"clint",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(RenderPhaseAccessor.getGLINT_SHADER())
					.texture(new RenderPhase.Texture(CHARGED_ITEM_GLINT, true, false))
					.writeMaskState(RenderPhaseAccessor.getCOLOR_MASK())
					.cull(RenderPhaseAccessor.getDISABLE_CULLING())
					.depthTest(RenderPhaseAccessor.getEQUAL_DEPTH_TEST())
					.transparency(RenderPhaseAccessor.getGLINT_TRANSPARENCY())
					.texturing(RenderPhaseAccessor.getGLINT_TEXTURING())
					.build(false)
	);

	private static final RenderLayer DIRECT_CLINT = RenderLayerAccessor.of(
			"clint_direct",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(RenderPhaseAccessor.getDIRECT_GLINT_SHADER())
					.texture(new RenderPhase.Texture(CHARGED_ITEM_GLINT, true, false))
					.writeMaskState(RenderPhaseAccessor.getCOLOR_MASK())
					.cull(RenderPhaseAccessor.getDISABLE_CULLING())
					.depthTest(RenderPhaseAccessor.getEQUAL_DEPTH_TEST())
					.transparency(RenderPhaseAccessor.getGLINT_TRANSPARENCY())
					.texturing(RenderPhaseAccessor.getGLINT_TEXTURING())
					.build(false)
	);

	private static final RenderLayer ENTITY_CLINT = RenderLayerAccessor.of(
			"entity_clint",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(RenderPhaseAccessor.getENTITY_GLINT_SHADER())
					.texture(new RenderPhase.Texture(CHARGED_ITEM_GLINT, true, false))
					.writeMaskState(RenderPhaseAccessor.getCOLOR_MASK())
					.cull(RenderPhaseAccessor.getDISABLE_CULLING())
					.depthTest(RenderPhaseAccessor.getEQUAL_DEPTH_TEST())
					.transparency(RenderPhaseAccessor.getGLINT_TRANSPARENCY())
					.target(RenderPhaseAccessor.getITEM_TARGET())
					.texturing(RenderPhaseAccessor.getENTITY_GLINT_TEXTURING())
					.build(false)
	);

	private static final RenderLayer DIRECT_ENTITY_CLINT = RenderLayerAccessor.of(
			"entity_clint_direct",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(RenderPhaseAccessor.getDIRECT_ENTITY_GLINT_SHADER())
					.texture(new RenderPhase.Texture(CHARGED_ITEM_GLINT, true, false))
					.writeMaskState(RenderPhaseAccessor.getCOLOR_MASK())
					.cull(RenderPhaseAccessor.getDISABLE_CULLING())
					.depthTest(RenderPhaseAccessor.getEQUAL_DEPTH_TEST())
					.transparency(RenderPhaseAccessor.getGLINT_TRANSPARENCY())
					.texturing(RenderPhaseAccessor.getENTITY_GLINT_TEXTURING())
					.build(false)
	);

	public static RenderLayer getArmorClint() {
		return ARMOR_CLINT;
	}

	public static RenderLayer getArmorEntityClint() {
		return ARMOR_ENTITY_CLINT;
	}

	public static RenderLayer getClintTranslucent() {
		return CLINT_TRANSLUCENT;
	}

	public static RenderLayer getClint() {
		return CLINT;
	}

	public static RenderLayer getDirectClint() {
		return DIRECT_CLINT;
	}

	public static RenderLayer getEntityClint() {
		return ENTITY_CLINT;
	}

	public static RenderLayer getDirectEntityClint() {
		return DIRECT_ENTITY_CLINT;
	}

	public static VertexConsumer getArmorClintConsumer(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint, boolean clint) {
		if (glint) {
			return VertexConsumers.union(provider.getBuffer(solid ? RenderLayer.getArmorGlint() : RenderLayer.getArmorEntityGlint()), provider.getBuffer(layer));
		} else if (clint) {
			return VertexConsumers.union(provider.getBuffer(solid ? getArmorClint() : getArmorEntityClint()), provider.getBuffer(layer));
		}
		return provider.getBuffer(layer);
	}

	public static VertexConsumer getItemClintConsumer(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean solid, boolean glint, boolean clint) {
		if (glint) {
			return MinecraftClient.isFabulousGraphicsOrBetter() && layer == TexturedRenderLayers.getItemEntityTranslucentCull()
					? VertexConsumers.union(vertexConsumers.getBuffer(RenderLayer.getGlintTranslucent()), vertexConsumers.getBuffer(layer))
					: VertexConsumers.union(vertexConsumers.getBuffer(solid ? RenderLayer.getGlint() : RenderLayer.getEntityGlint()), vertexConsumers.getBuffer(layer));
		} else if (clint) {
			return MinecraftClient.isFabulousGraphicsOrBetter() && layer == TexturedRenderLayers.getItemEntityTranslucentCull()
					? VertexConsumers.union(vertexConsumers.getBuffer(getClintTranslucent()), vertexConsumers.getBuffer(layer))
					: VertexConsumers.union(vertexConsumers.getBuffer(solid ? getClint() : getEntityClint()), vertexConsumers.getBuffer(layer));
		}
		return vertexConsumers.getBuffer(layer);
	}

	public static VertexConsumer getDirectItemClintConsumer(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint, boolean clint) {
		if (glint) {
			return VertexConsumers.union(provider.getBuffer(solid ? RenderLayer.getDirectGlint() : RenderLayer.getDirectEntityGlint()), provider.getBuffer(layer));
		} else if (clint) {
			return VertexConsumers.union(provider.getBuffer(solid ? getDirectClint() : getDirectEntityClint()), provider.getBuffer(layer));
		}
		return provider.getBuffer(layer);
	}

	@Override
	public void register() {
	}

	@Override
	public void registerClient() {
	}
}
