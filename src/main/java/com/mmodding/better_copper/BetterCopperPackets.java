package com.mmodding.better_copper;

import net.minecraft.util.Identifier;

public class BetterCopperPackets {

	public static final Identifier C2S_OPEN_COPPER_CAPABILITIES_TAB = BetterCopper.createId("networking/copper_capabilities/c2s/open_tab");
	public static final Identifier C2S_CLOSE_COPPER_CAPABILITIES_TAB = BetterCopper.createId("networking/copper_capabilities/s2c/close_tab");
	public static final Identifier S2C_OPEN_COPPER_CAPABILITIES_TAB = BetterCopper.createId("networking/copper_capabilities/s2c/open_tab");
	public static final Identifier S2C_UPDATE_DISPLAY = BetterCopper.createId("networking/copper_capabilities/s2c/update_display");
}
