package com.mmodding.better_copper.copper_capabilities.client;

import com.mmodding.better_copper.BetterCopperPackets;
import com.mmodding.better_copper.copper_capabilities.CopperCapabilitiesManager;
import com.mmodding.better_copper.copper_capabilities.CopperCapability;
import com.mmodding.better_copper.copper_capabilities.CopperCapabilityProgress;
import com.mmodding.better_copper.ducks.ClientPlayNetworkHandlerDuckInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@ClientOnly
public class ClientCopperCapabilitiesManager {

	private final CopperCapabilitiesManager manager = new CopperCapabilitiesManager();
	private @Nullable ClientCopperCapabilitiesManager.Listener listener;
	private @Nullable CopperCapability selectedTab;

	public static void useInstanceIfPresent(Consumer<ClientCopperCapabilitiesManager> action) {
		if (MinecraftClient.getInstance().getNetworkHandler() != null) {
			action.accept(ClientCopperCapabilitiesManager.getInstance());
		}
	}

	public static ClientCopperCapabilitiesManager getInstance() {
		if (MinecraftClient.getInstance().getNetworkHandler() != null) {
			return ((ClientPlayNetworkHandlerDuckInterface) MinecraftClient.getInstance().getNetworkHandler()).better_copper$getCopperCapabilitiesManager();
		} else {
			throw new RuntimeException("Client Network Handler Was Not Initialized");
		}
	}

	public void updateCapabilities(Set<Identifier> toRemove, Map<Identifier, CopperCapability.Task> toEarn, boolean shouldClearCurrent) {
		if (shouldClearCurrent) {
			this.manager.clear();
		}
		this.manager.removeAll(toRemove);
		this.manager.load(toEarn);
	}

	public CopperCapabilitiesManager getManager() {
		return this.manager;
	}

	public void selectTab(@Nullable CopperCapability tab, boolean local) {

		if (tab != null && local) {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeIdentifier(tab.getIdentifier());
			ClientPlayNetworking.send(BetterCopperPackets.C2S_OPEN_COPPER_CAPABILITIES_TAB, buf);
		}

		if (this.selectedTab != tab) {
			this.selectedTab = tab;
			if (this.listener != null) {
				this.listener.selectTab(tab);
			}
		}
	}

	public void setListener(@Nullable ClientCopperCapabilitiesManager.Listener listener) {
		this.listener = listener;
		this.manager.setListener(listener);
		if (listener != null) {
			listener.selectTab(this.selectedTab);
		}
	}

	@ClientOnly
	public interface Listener extends CopperCapabilitiesManager.Listener {
		void setProgress(CopperCapability copperCapability, CopperCapabilityProgress progress);

		void selectTab(@Nullable CopperCapability copperCapability);
	}
}
