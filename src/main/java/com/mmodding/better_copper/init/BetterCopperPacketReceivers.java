package com.mmodding.better_copper.init;

import com.mmodding.better_copper.BetterCopperPackets;
import com.mmodding.better_copper.copper_capabilities.CopperCapabilitiesTracker;
import com.mmodding.better_copper.copper_capabilities.CopperCapability;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class BetterCopperPacketReceivers implements ElementsInitializer {

	@Override
	public void register() {

		ServerPlayNetworking.registerGlobalReceiver(
			BetterCopperPackets.C2S_OPEN_COPPER_CAPABILITIES_TAB,
			(server, player, handler, buf, sender) -> {
				Identifier identifier = buf.readIdentifier();
				CopperCapability copperCapability =  BetterCopperResourceLoaders.COPPER_CAPABILITIES_LOADER.get(identifier);
				if (copperCapability != null) {
					CopperCapabilitiesTracker.getInstance(player).setDisplayTab(copperCapability);
				}
			}
		);

		ServerPlayNetworking.registerGlobalReceiver(
			BetterCopperPackets.C2S_CLOSE_COPPER_CAPABILITIES_TAB,
			(server, player, handler, buf, sender) -> {
				CopperCapabilitiesTracker.getInstance(player).setDisplayTab(null);
			}
		);
	}
}
