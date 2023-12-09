package com.mmodding.better_copper.ducks;

import com.mmodding.better_copper.copper_capabilities.client.ClientCopperCapabilitiesManager;
import com.mmodding.mmodding_lib.library.utils.InternalOf;

@InternalOf(targets = ClientCopperCapabilitiesManager.class)
public interface ClientPlayNetworkHandlerDuckInterface {

	ClientCopperCapabilitiesManager better_copper$getCopperCapabilitiesManager();
}
