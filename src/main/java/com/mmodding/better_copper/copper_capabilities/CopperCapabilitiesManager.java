package com.mmodding.better_copper.copper_capabilities;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CopperCapabilitiesManager {

	private static final Logger LOGGER = LogUtils.getLogger();
	private final Map<Identifier, CopperCapability> copperCapabilities = Maps.newHashMap();
	private final Set<CopperCapability> roots = Sets.newLinkedHashSet();
	private final Set<CopperCapability> dependents = Sets.newLinkedHashSet();
	private @Nullable CopperCapabilitiesManager.Listener listener;

	private void remove(CopperCapability copperCapability) {
		for (CopperCapability copperCapabilityChild : copperCapability.getChildren()) {
			this.remove(copperCapabilityChild);
		}

		LOGGER.info("Forgot about capability {}", copperCapability.getIdentifier());
		this.copperCapabilities.remove(copperCapability.getIdentifier());
		if (copperCapability.getParent() == null) {
			this.roots.remove(copperCapability);
			if (this.listener != null) {
				this.listener.onRootRemoved(copperCapability);
			}
		} else {
			this.dependents.remove(copperCapability);
			if (this.listener != null) {
				this.listener.onDependentRemoved(copperCapability);
			}
		}
	}

	public void removeAll(Set<Identifier> copperCapabilities) {
		for (Identifier identifier : copperCapabilities) {
			CopperCapability copperCapability = this.copperCapabilities.get(identifier);
			if (copperCapability == null) {
				LOGGER.warn("Told to remove capability {} but I don't know what that is", identifier);
			} else {
				this.remove(copperCapability);
			}
		}
	}

	public void load(Map<Identifier, CopperCapability.Task> taskMap) {
		Map<Identifier, CopperCapability.Task> taskMap2 = Maps.newHashMap(taskMap);

		while (!taskMap2.isEmpty()) {
			boolean bl = false;
			Iterator<Entry<Identifier, CopperCapability.Task>> iterator = taskMap2.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<Identifier, CopperCapability.Task> entry = iterator.next();
				Identifier identifier = entry.getKey();
				CopperCapability.Task task = entry.getValue();
				if (task.findParent(this.copperCapabilities::get)) {
					CopperCapability copperCapability = task.build(identifier);
					this.copperCapabilities.put(identifier, copperCapability);
					bl = true;
					iterator.remove();
					if (copperCapability.getParent() == null) {
						this.roots.add(copperCapability);
						if (this.listener != null) {
							this.listener.onRootAdded(copperCapability);
						}
					} else {
						this.dependents.add(copperCapability);
						if (this.listener != null) {
							this.listener.onDependentAdded(copperCapability);
						}
					}
				}
			}

			if (!bl) {
				for (Entry<Identifier, CopperCapability.Task> entry : taskMap2.entrySet()) {
					LOGGER.error("Couldn't load advancement {}: {}", entry.getKey(), entry.getValue());
				}
				break;
			}
		}

		LOGGER.info("Loaded {} advancements", this.copperCapabilities.size());
	}

	public void clear() {
		this.copperCapabilities.clear();
		this.roots.clear();
		this.dependents.clear();
		if (this.listener != null) {
			this.listener.onClear();
		}
	}

	public Iterable<CopperCapability> getRoots() {
		return this.roots;
	}

	public Collection<CopperCapability> getCopperCapabilities() {
		return this.copperCapabilities.values();
	}

	@Nullable
	public CopperCapability get(Identifier id) {
		return this.copperCapabilities.get(id);
	}

	public void setListener(@Nullable CopperCapabilitiesManager.Listener listener) {
		this.listener = listener;
		if (listener != null) {
			for (CopperCapability copperCapability : this.roots) {
				listener.onRootAdded(copperCapability);
			}

			for (CopperCapability copperCapability : this.dependents) {
				listener.onDependentAdded(copperCapability);
			}
		}
	}

	public interface Listener {

		void onRootAdded(CopperCapability root);

		void onRootRemoved(CopperCapability root);

		void onDependentAdded(CopperCapability dependent);

		void onDependentRemoved(CopperCapability dependent);

		void onClear();
	}
}
