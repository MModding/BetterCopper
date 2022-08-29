package com.mmodding.better_copper.init;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.blocks.CopperPowerBlock;
import com.mmodding.mmodding_lib.library.blocks.CustomFallingBlock;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.item.ItemGroup;

public class Blocks implements ElementsInitializer, ClientElementsInitializer {

	public static final CopperPowerBlock COPPER_POWER_BLOCK = new CopperPowerBlock(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.COPPER_BLOCK), true, ItemGroup.BUILDING_BLOCKS);
	public static final CustomFallingBlock NETHERITE_COATED_GOLD_BLOCK = new CustomFallingBlock(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.GOLD_BLOCK), true, ItemGroup.BUILDING_BLOCKS);

	@Override
	public void register() {
		COPPER_POWER_BLOCK.register(Utils.newIdentifier("copper_power_block"));
		NETHERITE_COATED_GOLD_BLOCK.register(Utils.newIdentifier("netherite_coated_gold_block"));
	}

	@Override
	public void registerClient() {}
}
