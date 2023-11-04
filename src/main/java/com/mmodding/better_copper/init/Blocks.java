package com.mmodding.better_copper.init;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.blocks.CopperPowerBlock;
import com.mmodding.better_copper.blocks.CopperRailBlock;
import com.mmodding.better_copper.blocks.NetheriteCoatedGoldBlock;
import com.mmodding.better_copper.blocks.OxidizableCopperRailBlock;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Oxidizable;
import net.minecraft.item.ItemGroup;

public class Blocks implements ElementsInitializer, ClientElementsInitializer {

	public static final CopperPowerBlock COPPER_POWER_BLOCK = new CopperPowerBlock(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.COPPER_BLOCK), true, ItemGroup.BUILDING_BLOCKS);
	public static final NetheriteCoatedGoldBlock NETHERITE_COATED_GOLD_BLOCK = new NetheriteCoatedGoldBlock(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.GOLD_BLOCK), true, ItemGroup.BUILDING_BLOCKS);
	public static final OxidizableCopperRailBlock COPPER_RAIL = new OxidizableCopperRailBlock(Oxidizable.OxidizationLevel.UNAFFECTED, AbstractBlock.Settings.copy(net.minecraft.block.Blocks.RAIL), true, ItemGroup.TRANSPORTATION);
	public static final OxidizableCopperRailBlock EXPOSED_COPPER_RAIL = new OxidizableCopperRailBlock(Oxidizable.OxidizationLevel.EXPOSED, AbstractBlock.Settings.copy(net.minecraft.block.Blocks.RAIL), true, ItemGroup.TRANSPORTATION);
	public static final OxidizableCopperRailBlock WEATHERED_COPPER_RAIL = new OxidizableCopperRailBlock(Oxidizable.OxidizationLevel.WEATHERED, AbstractBlock.Settings.copy(net.minecraft.block.Blocks.RAIL), true, ItemGroup.TRANSPORTATION);
	public static final OxidizableCopperRailBlock OXIDIZED_COPPER_RAIL = new OxidizableCopperRailBlock(Oxidizable.OxidizationLevel.OXIDIZED, AbstractBlock.Settings.copy(net.minecraft.block.Blocks.RAIL), true, ItemGroup.TRANSPORTATION);
	public static final CopperRailBlock WAXED_COPPER_RAIL = new CopperRailBlock(1.2, AbstractBlock.Settings.copy(COPPER_RAIL), true, ItemGroup.TRANSPORTATION);
	public static final CopperRailBlock WAXED_EXPOSED_COPPER_RAIL = new CopperRailBlock(1.0, AbstractBlock.Settings.copy(EXPOSED_COPPER_RAIL), true, ItemGroup.TRANSPORTATION);
	public static final CopperRailBlock WAXED_WEATHERED_COPPER_RAIL = new CopperRailBlock(0.8, AbstractBlock.Settings.copy(WEATHERED_COPPER_RAIL), true, ItemGroup.TRANSPORTATION);
	public static final CopperRailBlock WAXED_OXIDIZED_COPPER_RAIL = new CopperRailBlock(0.6, AbstractBlock.Settings.copy(OXIDIZED_COPPER_RAIL), true, ItemGroup.TRANSPORTATION);

	@Override
	public void register() {
		COPPER_POWER_BLOCK.register(Utils.newIdentifier("copper_power_block"));
		NETHERITE_COATED_GOLD_BLOCK.register(Utils.newIdentifier("netherite_coated_gold_block"));
		COPPER_RAIL.register(Utils.newIdentifier("copper_rail"));
		EXPOSED_COPPER_RAIL.register(Utils.newIdentifier("exposed_copper_rail"));
		WEATHERED_COPPER_RAIL.register(Utils.newIdentifier("weathered_copper_rail"));
		OXIDIZED_COPPER_RAIL.register(Utils.newIdentifier("oxidized_copper_rail"));
		WAXED_COPPER_RAIL.register(Utils.newIdentifier("waxed_copper_rail"));
		WAXED_EXPOSED_COPPER_RAIL.register(Utils.newIdentifier("waxed_weathered_copper_rail"));
		WAXED_WEATHERED_COPPER_RAIL.register(Utils.newIdentifier("waxed_exposed_copper_rail"));
		WAXED_OXIDIZED_COPPER_RAIL.register(Utils.newIdentifier("waxed_oxidized_copper_rail"));
	}

	@Override
	public void registerClient() {
		COPPER_RAIL.cutout();
		EXPOSED_COPPER_RAIL.cutout();
		WEATHERED_COPPER_RAIL.cutout();
		OXIDIZED_COPPER_RAIL.cutout();
		WAXED_COPPER_RAIL.cutout();
		WAXED_EXPOSED_COPPER_RAIL.cutout();
		WAXED_WEATHERED_COPPER_RAIL.cutout();
		WAXED_OXIDIZED_COPPER_RAIL.cutout();
	}
}
