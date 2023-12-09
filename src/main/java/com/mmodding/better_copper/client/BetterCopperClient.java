package com.mmodding.better_copper.client;

import com.mmodding.better_copper.client.init.BetterCopperClientEvents;
import com.mmodding.better_copper.client.init.BetterCopperClientPacketReceivers;
import com.mmodding.better_copper.client.init.BetterCopperGlintPacks;
import com.mmodding.better_copper.client.init.BetterCopperKeyBinds;
import com.mmodding.better_copper.init.BetterCopperBlocks;
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
		clientInitializers.add(new BetterCopperBlocks());
		clientInitializers.add(new BetterCopperGlintPacks());
		clientInitializers.add(new BetterCopperKeyBinds());
		clientInitializers.add(new BetterCopperClientEvents());
		clientInitializers.add(new BetterCopperClientPacketReceivers());
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
