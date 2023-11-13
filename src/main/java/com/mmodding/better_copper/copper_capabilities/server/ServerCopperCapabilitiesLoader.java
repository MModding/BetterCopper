package com.mmodding.better_copper.copper_capabilities.server;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mmodding.better_copper.copper_capabilities.CopperCapability;
import com.mojang.logging.LogUtils;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ServerCopperCapabilitiesLoader extends JsonDataLoader {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = new GsonBuilder().create();
	// private MyManager manager = new MyManager();

	public ServerCopperCapabilitiesLoader() {
		super(GSON, "copper_capabilities");
	}

	protected void apply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler) {
		Map<Identifier, CopperCapability.Task> taskMap = Maps.newHashMap();
		map.forEach((id, json) -> {
			try {
				JsonObject jsonObject = JsonHelper.asObject(json, "copper_capabilities");
				CopperCapability.Task task = CopperCapability.Task.fromJson(jsonObject);
				taskMap.put(id, task);
			} catch (Exception exception) {
				LOGGER.error("Parsing error loading custom copper capability {}: {}", id, exception.getMessage());
			}
		});
		// MyManager myManager = new MyManager();
		// myManager.load(taskMap);
		// for(CopperCapability copperCapability : myManager.getRoots()) {
		// 		if (copperCapability.getDisplay() != null) {
		// 			CopperCapabilityPositioner.arrangeForTree(copperCapability);
		// 		}
		// }
		// this.manager = myManager;
	}

	@Nullable
	public CopperCapability get(Identifier id) {
		// return this.manager.get(id);
		return null;
	}

	public Collection<CopperCapability> getCopperCapabilities() {
		// return this.manager.getCopperCapabilities();
		return Collections.emptyList();
	}
}
