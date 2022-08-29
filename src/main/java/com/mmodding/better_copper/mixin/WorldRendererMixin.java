package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.init.RenderLayers;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Shadow
	@Final
	private BufferBuilderStorage bufferBuilders;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw(Lnet/minecraft/client/render/RenderLayer;)V", ordinal = 16))
	private void injected(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		VertexConsumerProvider.Immediate immediate = bufferBuilders.getEntityVertexConsumers();
		immediate.draw(RenderLayers.getArmorClint());
		immediate.draw(RenderLayers.getArmorEntityClint());
		immediate.draw(RenderLayers.getClint());
		immediate.draw(RenderLayers.getDirectClint());
		immediate.draw(RenderLayers.getClintTranslucent());
		immediate.draw(RenderLayers.getEntityClint());
		immediate.draw(RenderLayers.getDirectEntityClint());
	}
}
