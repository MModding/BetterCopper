package com.mmodding.better_copper.copper_capabilities;

import com.mmodding.better_copper.BetterCopper;
import com.mmodding.better_copper.copper_capabilities.component.CopperCapabilitiesComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.mob.MobEntity;

import java.util.Optional;

public class CopperCapabilities implements EntityComponentInitializer {

	private static final ComponentKey<CopperCapabilitiesComponent> COPPER_CAPABILITIES = ComponentRegistry.getOrCreate(BetterCopper.createId("copper_capabilities"), CopperCapabilitiesComponent.class);

	public static boolean hasCopperCapability(MobEntity mob, CopperCapability copperCapability) {
		return CopperCapabilities.getCopperCapabilityLevel(mob, copperCapability) > 0;
	}

	public static int getCopperCapabilityLevel(MobEntity mob, CopperCapability copperCapability) {
		Optional<CopperCapabilitiesComponent> component = COPPER_CAPABILITIES.maybeGet(mob);

		if (component.isPresent()) {
			if (component.get().hasCopperCapability(copperCapability.getIdentifier())) {
				return component.get().getCopperCapability(copperCapability.getIdentifier());
			}
			else {
				throw new IllegalArgumentException("CopperCapability " + copperCapability.getIdentifier() + "does not exist");
			}
		}
		else {
			throw new IllegalArgumentException("Mob does not have a CopperCapabilitiesComponent");
		}
	}

	public static void increaseCopperCapabilityLevel(MobEntity mob, CopperCapability copperCapability, int level) {
		CopperCapabilities.setCopperCapabilityLevel(mob, copperCapability, CopperCapabilities.getCopperCapabilityLevel(mob, copperCapability) + level);
	}

	public static void setCopperCapabilityLevel(MobEntity mob, CopperCapability copperCapability, int level) {
		Optional<CopperCapabilitiesComponent> component = COPPER_CAPABILITIES.maybeGet(mob);

		if (component.isPresent()) {
			if (component.get().hasCopperCapability(copperCapability.getIdentifier())) {
				component.get().setCopperCapability(copperCapability.getIdentifier(), level);
			}
			else {
				throw new IllegalArgumentException("CopperCapability " + copperCapability.getIdentifier() + "does not exist");
			}
		}
		else {
			throw new IllegalArgumentException("Mob does not have a CopperCapabilitiesComponent");
		}
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(MobEntity.class, COPPER_CAPABILITIES, mob -> new CopperCapabilitiesComponent());
	}
}
