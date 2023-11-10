package com.mmodding.better_copper.init;

import com.mmodding.better_copper.BetterCopper;
import com.mmodding.better_copper.blocks.entities.CopperCoreBlockEntity;
import com.mmodding.mmodding_lib.library.blockentities.CustomBlockEntityType;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;

public class BlockEntities implements ElementsInitializer {

	public static final CustomBlockEntityType<CopperCoreBlockEntity> COPPER_CORE = CustomBlockEntityType.create(
		CopperCoreBlockEntity::new, null,
		Blocks.COPPER_CORE,
		Blocks.EXPOSED_COPPER_CORE,
		Blocks.OXIDIZED_COPPER_CORE,
		Blocks.WEATHERED_COPPER_CORE,
		Blocks.WAXED_COPPER_CORE,
		Blocks.WAXED_EXPOSED_COPPER_CORE,
		Blocks.WAXED_OXIDIZED_COPPER_CORE,
		Blocks.WAXED_WEATHERED_COPPER_CORE
	);

	@Override
	public void register() {
		COPPER_CORE.register(BetterCopper.createId("copper_core"));
	}
}
