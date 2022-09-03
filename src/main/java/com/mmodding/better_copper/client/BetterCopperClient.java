package com.mmodding.better_copper.client;

import com.mmodding.better_copper.client.render.ChargedValueRenderer;
import com.mmodding.better_copper.client.render.Outliner;
import com.mmodding.better_copper.client.render.SuperRenderTypeBuffer;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.init.RenderLayers;
import com.mmodding.better_copper.magneticfield.LoopAreaHelper;
import com.mmodding.better_copper.mixin.accessors.WorldRendererAccessor;
import com.mmodding.mmodding_lib.library.base.MModdingClientModInitializer;
import com.mmodding.mmodding_lib.library.config.Config;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import com.mmodding.mmodding_lib.library.utils.RenderLayerUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientLifecycleEvents;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BetterCopperClient implements MModdingClientModInitializer {

	public static final Outliner BOX_OUTLINE = new Outliner();

	@Override
	public List<ClientElementsInitializer> getClientElementsInitializers() {
		List<ClientElementsInitializer> clientInitializers = new ArrayList<>();
		clientInitializers.add(new Blocks());
		clientInitializers.add(new RenderLayers());
		return clientInitializers;
	}

	@Nullable
	@Override
	public Config getClientConfig() {
		return null;
	}

	@Override
	public void onInitializeClient(ModContainer modContainer) {
		MModdingClientModInitializer.super.onInitializeClient(modContainer);
		ClientLifecycleEvents.READY.register(client -> {
			RenderLayerUtils.addEntityBuilder(RenderLayers.getArmorClint());
			RenderLayerUtils.addEntityBuilder(RenderLayers.getArmorEntityClint());
			RenderLayerUtils.addEntityBuilder(RenderLayers.getClintTranslucent());
			RenderLayerUtils.addEntityBuilder(RenderLayers.getClint());
			RenderLayerUtils.addEntityBuilder(RenderLayers.getDirectClint());
			RenderLayerUtils.addEntityBuilder(RenderLayers.getEntityClint());
			RenderLayerUtils.addEntityBuilder(RenderLayers.getDirectEntityClint());
		});
		ClientTickEvents.END.register(this::onTick);
		WorldRenderEvents.AFTER_TRANSLUCENT.register(this::onRenderWorld);
	}

	private void onRenderWorld(WorldRenderContext worldRenderContext) {
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

	private void onTick(MinecraftClient minecraftClient) {
		if (LoopAreaHelper.FIELDS != null && !LoopAreaHelper.FIELDS.isEmpty())
			LoopAreaHelper.FIELDS.forEach(magneticField -> magneticField.render(minecraftClient, LoopAreaHelper.getRenderCamera(), WorldRendererAccessor.getFORCEFIELD()));
		BOX_OUTLINE.tickOutlines();
		ChargedValueRenderer.tick(minecraftClient);
	}
}
