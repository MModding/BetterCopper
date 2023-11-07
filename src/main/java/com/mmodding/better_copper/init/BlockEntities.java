package com.mmodding.better_copper.init;

import com.mmodding.better_copper.BetterCopper;
import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.mmodding_lib.library.blockentities.CustomBlockEntityType;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;

public class BlockEntities implements ElementsInitializer {

	public static final CustomBlockEntityType<CopperPowerBlockEntity> COPPER_POWER = CustomBlockEntityType.create(
		CopperPowerBlockEntity::new, null, Blocks.COPPER_POWER_BLOCK
	);

	@Override
	public void register() {
		COPPER_POWER.register(BetterCopper.createId("copper_power_block"));
	}
}
