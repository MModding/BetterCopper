package com.mmodding.better_copper.client.init;

import com.mmodding.better_copper.client.render.ChargedValueRenderer;
import com.mmodding.better_copper.client.render.Outliner;
import com.mmodding.better_copper.client.render.SuperRenderTypeBuffer;
import com.mmodding.better_copper.copper_capabilities.client.ClientCopperCapabilitiesManager;
import com.mmodding.better_copper.copper_capabilities.client.gui.CopperCapabilityScreen;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public class BetterCopperClientEvents implements ClientElementsInitializer {

	public static final Outliner BOX_OUTLINE = new Outliner();

	private void clientEndTick(MinecraftClient client) {
		BOX_OUTLINE.tickOutlines();
		ChargedValueRenderer.tick(client);

		while (BetterCopperKeyBinds.COPPER_CAPABILITY_KEY.isPressed()) {
			ClientCopperCapabilitiesManager.useInstanceIfPresent(manager -> client.setScreen(new CopperCapabilityScreen(manager)));
		}
	}

	private void afterTranslucentRenderWorld(WorldRenderContext worldRenderContext) {
		Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
		float pt = MinecraftClient.getInstance().getTickDelta();
		MatrixStack matrixStack = worldRenderContext.matrixStack();
		matrixStack.push();
		matrixStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		SuperRenderTypeBuffer buffer = SuperRenderTypeBuffer.INSTANCE;

		BOX_OUTLINE.renderOutlines(matrixStack, buffer, pt);
		buffer.draw();
		RenderSystem.enableCull();
		matrixStack.pop();
	}

	@Override
	public void registerClient() {
		ClientTickEvents.END.register(this::clientEndTick);
		WorldRenderEvents.AFTER_TRANSLUCENT.register(this::afterTranslucentRenderWorld);
	}
}
