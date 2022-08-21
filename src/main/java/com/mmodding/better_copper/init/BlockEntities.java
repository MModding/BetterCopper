package com.mmodding.better_copper.init;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.mmodding_lib.library.blockentities.CustomBlockEntityType;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;

public class BlockEntities implements ElementsInitializer {

	public static final CustomBlockEntityType<CopperPowerBlockEntity> COPPER_POWER_BLOCK_ENTITY = new CustomBlockEntityType<>(CopperPowerBlockEntity::new,
			Blocks.COPPER_POWER_BLOCK).createAndRegister(Utils.newIdentifier("copper_power_block"));

	@Override
	public void register() {}
}
