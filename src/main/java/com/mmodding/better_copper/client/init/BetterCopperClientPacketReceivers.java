package com.mmodding.better_copper.client.init;

import com.google.common.collect.Sets;
import com.mmodding.better_copper.BetterCopperPackets;
import com.mmodding.better_copper.copper_capabilities.CopperCapability;
import com.mmodding.better_copper.copper_capabilities.client.ClientCopperCapabilitiesManager;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import com.mmodding.mmodding_lib.library.network.support.NetworkSupport;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import java.util.Map;
import java.util.Set;

public class BetterCopperClientPacketReceivers implements ClientElementsInitializer {

	@Override
	public void registerClient() {

		ClientPlayNetworking.registerGlobalReceiver(
			BetterCopperPackets.S2C_OPEN_COPPER_CAPABILITIES_TAB,
			(client, handler, buf, sender) -> ClientCopperCapabilitiesManager.useInstanceIfPresent(
				manager -> {
					Identifier identifier = buf.readNullable(PacketByteBuf::readIdentifier);
					manager.selectTab(identifier != null ? manager.getManager().get(identifier) : null, false);
				}
			)
		);

		ClientPlayNetworking.registerGlobalReceiver(
			BetterCopperPackets.S2C_UPDATE_DISPLAY,
			(client, handler, buf, sender) -> ClientCopperCapabilitiesManager.useInstanceIfPresent(
				manager -> {
					Set<Identifier> identifiers = buf.readCollection(Sets::newLinkedHashSetWithExpectedSize, PacketByteBuf::readIdentifier);
					Map<Identifier, CopperCapability.Task> tasks = buf.readMap(PacketByteBuf::readIdentifier, NetworkSupport::readComplete);
					boolean dirty = buf.readBoolean();
					manager.updateCapabilities(identifiers, tasks, dirty);
				}
			)
		);
	}
}
