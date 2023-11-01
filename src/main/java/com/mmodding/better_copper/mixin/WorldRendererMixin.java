package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
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
	private MinecraftClient client;

	@Shadow
	@Final
	private static Identifier FORCEFIELD;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWorldBorder(Lnet/minecraft/client/render/Camera;)V", ordinal = 0))
	private void injectRenderWB1(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		if (!Utils.FIELDS.isEmpty())
			Utils.FIELDS.forEach(magneticField -> magneticField.render(this.client, camera, FORCEFIELD));
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWorldBorder(Lnet/minecraft/client/render/Camera;)V", ordinal = 1))
	private void injectRenderWB2(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		if (!Utils.FIELDS.isEmpty())
			Utils.FIELDS.forEach(magneticField -> magneticField.render(this.client, camera, FORCEFIELD));
	}
}
