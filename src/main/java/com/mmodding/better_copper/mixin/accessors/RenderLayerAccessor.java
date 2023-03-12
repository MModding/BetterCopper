package com.mmodding.better_copper.mixin.accessors;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderLayer.class)
public interface RenderLayerAccessor {

	@Invoker("of")
	static RenderLayer.MultiPhase of(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, RenderLayer.MultiPhaseParameters phaseData) {
		throw new AssertionError();
	}
}
