package com.mmodding.better_copper.client;

import com.mmodding.better_copper.client.render.Outliner;
import com.mmodding.better_copper.client.render.PowerValueRenderer;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.mmodding_lib.library.base.MModdingClientModInitializer;
import com.mmodding.mmodding_lib.library.config.Config;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
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
		ClientTickEvents.END.register(BetterCopperClient::onTick);
	}

	protected static boolean isGameActive() {
		return !(MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().player == null);
	}

	public static void onTick(MinecraftClient client) {
		if (!isGameActive()) return;
		PowerValueRenderer.tick();
	}
}
