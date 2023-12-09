package com.mmodding.better_copper.copper_capabilities;

import com.mmodding.better_copper.BetterCopperPackets;
import com.mmodding.better_copper.ducks.ServerPlayerEntityDuckInterface;
import com.mmodding.better_copper.init.BetterCopperResourceLoaders;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CopperCapabilitiesTracker {

	private final Set<CopperCapability> visibleCopperCapabilities = new LinkedHashSet<>();
	private final Set<CopperCapability> visibilityUpdates = new LinkedHashSet<>();
	private final ServerPlayerEntity owner;
	private @Nullable CopperCapability currentDisplayTab;
	private boolean dirty = true;

	public static CopperCapabilitiesTracker getInstance(ServerPlayerEntity owner) {
		return ((ServerPlayerEntityDuckInterface) owner).better_copper$getCopperCapabilitiesTracker();
	}

	public CopperCapabilitiesTracker(ServerPlayerEntity owner) {
		this.owner = owner;
		this.load();
	}

	private void load() {
		BetterCopperResourceLoaders.COPPER_CAPABILITIES_LOADER.getCopperCapabilities().forEach(this::updateDisplay);
	}

	public void reload() {
		this.visibleCopperCapabilities.clear();
		this.visibilityUpdates.clear();
		this.dirty = true;
		this.currentDisplayTab = null;
		this.load();
	}

	public void sendUpdate(ServerPlayerEntity player) {
		if (this.dirty || !this.visibilityUpdates.isEmpty()) {
			Set<CopperCapability> capabilitySet = new LinkedHashSet<>();
			Set<Identifier> identifierSet = new LinkedHashSet<>();

			for(CopperCapability copperCapability : this.visibilityUpdates) {
				if (this.visibleCopperCapabilities.contains(copperCapability)) {
					capabilitySet.add(copperCapability);
				} else {
					identifierSet.add(copperCapability.getIdentifier());
				}
			}

			Map<Identifier, CopperCapability.Task> tasks = new HashMap<>();

			capabilitySet.forEach(copperCapability -> tasks.put(copperCapability.getIdentifier(), copperCapability.createTask()));

			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeCollection(identifierSet, PacketByteBuf::writeIdentifier);
			buf.writeMap(tasks, PacketByteBuf::writeIdentifier, (current, task) -> task.writeComplete(current));
			buf.writeBoolean(this.dirty);
			ServerPlayNetworking.send(player, BetterCopperPackets.S2C_UPDATE_DISPLAY, buf);

			this.visibilityUpdates.clear();
		}

		this.dirty = false;
	}

	public void setDisplayTab(@Nullable CopperCapability copperCapability) {
		CopperCapability currentCopperCapability = this.currentDisplayTab;
		if (copperCapability != null && copperCapability.getParent() == null && copperCapability.getDisplay() != null) {
			this.currentDisplayTab = copperCapability;
		} else {
			this.currentDisplayTab = null;
		}

		if (currentCopperCapability != this.currentDisplayTab) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeNullable(this.currentDisplayTab != null ? this.currentDisplayTab.getIdentifier() : null, PacketByteBuf::writeIdentifier);
            ServerPlayNetworking.send(this.owner, BetterCopperPackets.S2C_OPEN_COPPER_CAPABILITIES_TAB, buf);
		}
	}

	private void updateDisplay(CopperCapability copperCapability) {
		this.visibleCopperCapabilities.add(copperCapability);
		this.visibilityUpdates.add(copperCapability);

		for (CopperCapability currentCopperCapability : copperCapability.getChildren()) {
			this.updateDisplay(currentCopperCapability);
		}
	}
}
