package com.mmodding.better_copper.copper_capabilities.client;

import com.google.common.collect.Maps;
import com.mmodding.better_copper.copper_capabilities.CopperCapabilitiesManager;
import com.mmodding.better_copper.copper_capabilities.CopperCapability;
import com.mojang.logging.LogUtils;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Map.Entry;

@ClientOnly
public class ClientCopperCapabilitiesManager {

	private static final Logger LOGGER = LogUtils.getLogger();
	private final MinecraftClient client;
	private final CopperCapabilitiesManager manager = new CopperCapabilitiesManager();
	private final Map<CopperCapability, AdvancementProgress> copperCapabilityProgresses = Maps.newHashMap();
	private @Nullable ClientCopperCapabilitiesManager.Listener listener;
	private @Nullable CopperCapability selectedTab;

	public ClientCopperCapabilitiesManager(MinecraftClient client) {
		this.client = client;
	}

	/*
	public void onCapabilities(AdvancementUpdateS2CPacket packet) {
		if (packet.shouldClearCurrent()) {
			this.manager.clear();
			this.copperCapabilityProgresses.clear();
		}

		this.manager.removeAll(packet.getAdvancementIdsToRemove());
		this.manager.load(packet.getAdvancementsToEarn());

		for(Entry<Identifier, AdvancementProgress> entry : packet.getAdvancementsToProgress().entrySet()) {
			CopperCapability copperCapability = this.manager.get((Identifier) entry.getKey());
			if (copperCapability != null) {
				AdvancementProgress advancementProgress = (AdvancementProgress) entry.getValue();
				advancementProgress.init(copperCapability.getCriteria(), copperCapability.getRequirements());
				this.advancementProgresses.put(copperCapability, advancementProgress);
				if (this.listener != null) {
					this.listener.setProgress(copperCapability, advancementProgress);
				}
			} else {
				LOGGER.warn("Server informed client about progress for unknown capability {}", entry.getKey());
			}
		}
	}
	*/

	public CopperCapabilitiesManager getManager() {
		return this.manager;
	}

	public void selectTab(@Nullable CopperCapability tab, boolean local) {
		// ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.getNetworkHandler();
		// if (clientPlayNetworkHandler != null && tab != null && local) {
		// 		clientPlayNetworkHandler.sendPacket(AdvancementTabC2SPacket.open(tab));
		// }

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
			for (Entry<CopperCapability, AdvancementProgress> entry : this.copperCapabilityProgresses.entrySet()) {
				listener.setProgress(entry.getKey(), entry.getValue());
			}
			listener.selectTab(this.selectedTab);
		}
	}

	@ClientOnly
	public interface Listener extends CopperCapabilitiesManager.Listener {
		void setProgress(CopperCapability copperCapability, AdvancementProgress progress);

		void selectTab(@Nullable CopperCapability copperCapability);
	}
}
