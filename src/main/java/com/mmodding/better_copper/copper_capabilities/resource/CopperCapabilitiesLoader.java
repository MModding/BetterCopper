package com.mmodding.better_copper.copper_capabilities.resource;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mmodding.better_copper.BetterCopper;
import com.mmodding.better_copper.copper_capabilities.CopperCapabilitiesManager;
import com.mmodding.better_copper.copper_capabilities.CopperCapability;
import com.mmodding.better_copper.copper_capabilities.CopperCapabilityPositioner;
import com.mmodding.mmodding_lib.library.resources.loaders.IdentifiableJsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CopperCapabilitiesLoader implements IdentifiableJsonDataLoader {

	private static final Gson GSON = new GsonBuilder().create();
	private CopperCapabilitiesManager manager = new CopperCapabilitiesManager();

	@NotNull
	@Override
	public Identifier getQuiltId() {
		return BetterCopper.createId("copper_capabilities");
	}

	@Override
	public Gson getGson() {
		return CopperCapabilitiesLoader.GSON;
	}

	@Override
	public String getDataType() {
		return "copper_capabilities";
	}

	@Override
	public CompletableFuture<Void> apply(Map<Identifier, JsonElement> data, ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.runAsync(() -> {
			Map<Identifier, CopperCapability.Task> map = Maps.newHashMap();
			data.forEach((id, json) -> {
				try {
					String[] partParts = id.getPath().split("/");
					JsonObject jsonObject = JsonHelper.asObject(json, "copper_capabilities");
					CopperCapability.Task task = CopperCapability.Task.fromJson(partParts[0], jsonObject);
					map.put(id, task);
				} catch (Exception exception) {
					BetterCopper.LOGGER.error("Parsing error loading custom copper capability {}: {}", id, exception.getMessage());
				}
			});
			CopperCapabilitiesManager copperCapabilitiesManager = new CopperCapabilitiesManager();
			copperCapabilitiesManager.load(map);
			for (CopperCapability copperCapability : copperCapabilitiesManager.getRoots()) {
				if (copperCapability.getDisplay() != null) {
					CopperCapabilityPositioner.arrangeForTree(copperCapability);
				}
			}
			this.manager = copperCapabilitiesManager;
		}, executor);
	}

	@Nullable
	public CopperCapability get(Identifier id) {
		return this.manager.get(id);
	}

	public Collection<CopperCapability> getCopperCapabilities() {
		return this.manager.getCopperCapabilities();
	}
}
