package com.mmodding.better_copper.init;

import com.mmodding.better_copper.BetterCopper;
import com.mmodding.better_copper.blocks.entities.CopperCoreBlockEntity;
import com.mmodding.mmodding_lib.library.blockentities.CustomBlockEntityType;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;

public class BetterCopperBlockEntities implements ElementsInitializer {

	public static final CustomBlockEntityType<CopperCoreBlockEntity> COPPER_CORE = CustomBlockEntityType.create(
		CopperCoreBlockEntity::new, null,
		BetterCopperBlocks.COPPER_CORE,
		BetterCopperBlocks.EXPOSED_COPPER_CORE,
		BetterCopperBlocks.OXIDIZED_COPPER_CORE,
		BetterCopperBlocks.WEATHERED_COPPER_CORE,
		BetterCopperBlocks.WAXED_COPPER_CORE,
		BetterCopperBlocks.WAXED_EXPOSED_COPPER_CORE,
		BetterCopperBlocks.WAXED_OXIDIZED_COPPER_CORE,
		BetterCopperBlocks.WAXED_WEATHERED_COPPER_CORE
	);

	@Override
	public void register() {
		COPPER_CORE.register(BetterCopper.createId("copper_core"));
	}
}
