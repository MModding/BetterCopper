package com.mmodding.better_copper.copper_capabilities.client.gui;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public enum CopperCapabilityStatus {
	CLEARED(0),
	EXPOSED(1),
	WEATHERED(2),
	OXIDIZED(3);

	private final int spriteIndex;

	private CopperCapabilityStatus(int spriteIndex) {
		this.spriteIndex = spriteIndex;
	}

	public int getSpriteIndex() {
		return this.spriteIndex;
	}
}

