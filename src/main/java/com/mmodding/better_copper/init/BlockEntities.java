package com.mmodding.better_copper.init;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.mmodding_lib.library.blockentities.CustomBlockEntityType;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;

public class BlockEntities implements ElementsInitializer {

	public static final CustomBlockEntityType<CopperPowerBlockEntity> COPPER_POWER = CustomBlockEntityType.create(
		CopperPowerBlockEntity::new, null, Blocks.COPPER_POWER_BLOCK
	);

	@Override
	public void register() {
		Utils.newIdentifier("copper_power_block");
	}
}
