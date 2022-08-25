package com.mmodding.better_copper;

import com.mmodding.better_copper.init.BlockEntities;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.init.Items;
import com.mmodding.mmodding_lib.library.base.MModdingModContainer;
import com.mmodding.mmodding_lib.library.base.MModdingModInitializer;
import com.mmodding.mmodding_lib.library.config.Config;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;

import java.util.ArrayList;
import java.util.List;

public class BetterCopper implements MModdingModInitializer {

	public static MModdingModContainer mod;

	@Override
	public List<ElementsInitializer> getElementsInitializers() {
		List<ElementsInitializer> initializers = new ArrayList<>();
		initializers.add(new Blocks());
		initializers.add(new BlockEntities());
		initializers.add(new Items());
		return initializers;
	}

	@Nullable
	@Override
	public Config getConfig() {
		return null;
	}

	@Override
	public void onInitialize(ModContainer mod) {
		MModdingModInitializer.super.onInitialize(mod);
		BetterCopper.mod = MModdingModContainer.from(mod);
	}
}
