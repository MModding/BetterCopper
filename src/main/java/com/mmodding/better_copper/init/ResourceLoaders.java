package com.mmodding.better_copper.init;

import com.mmodding.better_copper.copper_capabilities.resource.CopperCapabilitiesLoader;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import net.minecraft.resource.ResourceType;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class ResourceLoaders implements ElementsInitializer {

	@Override
	public void register() {
		ResourceLoader.get(ResourceType.SERVER_DATA).registerReloader(new CopperCapabilitiesLoader());
	}
}
