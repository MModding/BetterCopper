package com.mmodding.better_copper;

import com.mmodding.better_copper.init.*;
import com.mmodding.mmodding_lib.library.base.AdvancedModContainer;
import com.mmodding.mmodding_lib.library.base.MModdingModInitializer;
import com.mmodding.mmodding_lib.library.config.Config;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import com.mmodding.mmodding_lib.library.utils.TextureLocation;
import com.mojang.logging.LogUtils;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BetterCopper implements MModdingModInitializer {

	public static final Logger LOGGER = LogUtils.getLogger();

	@Override
	public List<ElementsInitializer> getElementsInitializers() {
		List<ElementsInitializer> initializers = new ArrayList<>();
		initializers.add(new BetterCopperBlocks());
		initializers.add(new BetterCopperBlockEntities());
		initializers.add(new BetterCopperItems());
		initializers.add(new BetterCopperGlintPackViews());
		initializers.add(new BetterCopperEntityAttributes());
		initializers.add(new BetterCopperResourceLoaders());
		initializers.add(new BetterCopperPacketReceivers());
		return initializers;
	}

	@Nullable
	@Override
	public Config getConfig() {
		return null;
	}

	@Override
	public void onInitialize(AdvancedModContainer mod) {}

	public static String id() {
		return "better_copper";
	}

	public static Identifier createId(String path) {
		return new Identifier(BetterCopper.id(), path);
	}

	public static TextureLocation createTextureLocation(String path) {
		return new TextureLocation(BetterCopper.id(), path);
	}
}
