package com.mmodding.better_copper.init;

import com.mmodding.better_copper.copper_capabilities.resource.CopperCapabilitiesLoader;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import net.minecraft.resource.ResourceType;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class BetterCopperResourceLoaders implements ElementsInitializer {

	public static final CopperCapabilitiesLoader COPPER_CAPABILITIES_LOADER = new CopperCapabilitiesLoader();

	@Override
	public void register() {
		ResourceLoader.get(ResourceType.SERVER_DATA).registerReloader(BetterCopperResourceLoaders.COPPER_CAPABILITIES_LOADER);
	}
}
