package com.mmodding.better_copper.client;

import com.mmodding.better_copper.client.init.ClientEvents;
import com.mmodding.better_copper.client.init.GlintPacks;
import com.mmodding.better_copper.client.init.KeyBinds;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.mmodding_lib.library.base.AdvancedModContainer;
import com.mmodding.mmodding_lib.library.base.MModdingClientModInitializer;
import com.mmodding.mmodding_lib.library.config.Config;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.ArrayList;
import java.util.List;

@ClientOnly
public class BetterCopperClient implements MModdingClientModInitializer {

	@Override
	public List<ClientElementsInitializer> getClientElementsInitializers() {
		List<ClientElementsInitializer> clientInitializers = new ArrayList<>();
		clientInitializers.add(new Blocks());
		clientInitializers.add(new GlintPacks());
		clientInitializers.add(new KeyBinds());
		clientInitializers.add(new ClientEvents());
		return clientInitializers;
	}

	@Nullable
	@Override
	public Config getClientConfig() {
		return null;
	}

	@Override
	public void onInitializeClient(AdvancedModContainer modContainer) {}
}
