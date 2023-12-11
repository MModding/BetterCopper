package com.mmodding.better_copper.copper_capabilities.client.gui;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public enum CopperCapabilityStatus {
	CLEARED,
	EXPOSED,
	WEATHERED,
	OXIDIZED;

	CopperCapabilityStatus() {}
}
