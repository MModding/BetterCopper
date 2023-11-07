package com.mmodding.better_copper;

import com.mmodding.better_copper.init.BlockEntities;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.init.EntityAttributes;
import com.mmodding.better_copper.init.Items;
import com.mmodding.mmodding_lib.library.base.AdvancedModContainer;
import com.mmodding.mmodding_lib.library.base.MModdingModInitializer;
import com.mmodding.mmodding_lib.library.config.Config;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import com.mmodding.mmodding_lib.library.utils.TextureLocation;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BetterCopper implements MModdingModInitializer {

	@Override
	public List<ElementsInitializer> getElementsInitializers() {
		List<ElementsInitializer> initializers = new ArrayList<>();
		initializers.add(new Blocks());
		initializers.add(new BlockEntities());
		initializers.add(new Items());
		initializers.add(new EntityAttributes());
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
