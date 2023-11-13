package com.mmodding.better_copper.copper_capabilities.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class CopperCapabilitiesComponent implements Component, AutoSyncedComponent {

	private final Map<Identifier, Integer> copperCapabilities = new HashMap<>();

	@Override
	public void readFromNbt(NbtCompound tag) {
		tag.getKeys().forEach((identifier) -> this.copperCapabilities.put(Identifier.tryParse(identifier), tag.getInt(identifier)));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		this.copperCapabilities.forEach((identifier, value) -> tag.putInt(identifier.toString(), value));
	}

	public boolean hasCopperCapability(Identifier identifier) {
		return this.copperCapabilities.containsKey(identifier);
	}

	public int getCopperCapability(Identifier identifier) {
		return this.copperCapabilities.get(identifier);
	}

	public void setCopperCapability(Identifier identifier, int value) {
		this.copperCapabilities.put(identifier, value);
	}
}
