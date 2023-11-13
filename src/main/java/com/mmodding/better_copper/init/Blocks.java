package com.mmodding.better_copper.init;

import com.mmodding.better_copper.BetterCopper;
import com.mmodding.better_copper.blocks.*;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import net.minecraft.block.Oxidizable;
import net.minecraft.item.ItemGroup;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class Blocks implements ElementsInitializer, ClientElementsInitializer {

	public static final OxidizableCopperCoreBlock COPPER_CORE = new OxidizableCopperCoreBlock(QuiltBlockSettings.copy(net.minecraft.block.Blocks.COPPER_BLOCK), true, ItemGroup.BUILDING_BLOCKS);
	public static final OxidizableCopperCoreBlock EXPOSED_COPPER_CORE = new OxidizableCopperCoreBlock(Oxidizable.OxidizationLevel.EXPOSED, QuiltBlockSettings.copy(net.minecraft.block.Blocks.EXPOSED_COPPER), true, ItemGroup.BUILDING_BLOCKS);
	public static final OxidizableCopperCoreBlock WEATHERED_COPPER_CORE = new OxidizableCopperCoreBlock(Oxidizable.OxidizationLevel.WEATHERED, QuiltBlockSettings.copy(net.minecraft.block.Blocks.WEATHERED_COPPER), true, ItemGroup.BUILDING_BLOCKS);
	public static final OxidizableCopperCoreBlock OXIDIZED_COPPER_CORE = new OxidizableCopperCoreBlock(Oxidizable.OxidizationLevel.OXIDIZED, QuiltBlockSettings.copy(net.minecraft.block.Blocks.OXIDIZED_COPPER), true, ItemGroup.BUILDING_BLOCKS);
	public static final CopperCoreBlock WAXED_COPPER_CORE = new CopperCoreBlock(QuiltBlockSettings.copy(COPPER_CORE), true, ItemGroup.BUILDING_BLOCKS);
	public static final CopperCoreBlock WAXED_EXPOSED_COPPER_CORE = new CopperCoreBlock(QuiltBlockSettings.copy(EXPOSED_COPPER_CORE), true, ItemGroup.BUILDING_BLOCKS);
	public static final CopperCoreBlock WAXED_WEATHERED_COPPER_CORE = new CopperCoreBlock(QuiltBlockSettings.copy(WEATHERED_COPPER_CORE), true, ItemGroup.BUILDING_BLOCKS);
	public static final CopperCoreBlock WAXED_OXIDIZED_COPPER_CORE = new CopperCoreBlock(QuiltBlockSettings.copy(OXIDIZED_COPPER_CORE), true, ItemGroup.BUILDING_BLOCKS);
	public static final NetheriteCoatedGoldBlock NETHERITE_COATED_GOLD_BLOCK = new NetheriteCoatedGoldBlock(QuiltBlockSettings.copy(net.minecraft.block.Blocks.GOLD_BLOCK), true, ItemGroup.BUILDING_BLOCKS);
	public static final OxidizableCopperRailBlock COPPER_RAIL = new OxidizableCopperRailBlock(QuiltBlockSettings.copy(net.minecraft.block.Blocks.RAIL), true, ItemGroup.TRANSPORTATION);
	public static final OxidizableCopperRailBlock EXPOSED_COPPER_RAIL = new OxidizableCopperRailBlock(Oxidizable.OxidizationLevel.EXPOSED, QuiltBlockSettings.copy(net.minecraft.block.Blocks.RAIL), true, ItemGroup.TRANSPORTATION);
	public static final OxidizableCopperRailBlock WEATHERED_COPPER_RAIL = new OxidizableCopperRailBlock(Oxidizable.OxidizationLevel.WEATHERED, QuiltBlockSettings.copy(net.minecraft.block.Blocks.RAIL), true, ItemGroup.TRANSPORTATION);
	public static final OxidizableCopperRailBlock OXIDIZED_COPPER_RAIL = new OxidizableCopperRailBlock(Oxidizable.OxidizationLevel.OXIDIZED, QuiltBlockSettings.copy(net.minecraft.block.Blocks.RAIL), true, ItemGroup.TRANSPORTATION);
	public static final CopperRailBlock WAXED_COPPER_RAIL = new CopperRailBlock(1.2, QuiltBlockSettings.copy(COPPER_RAIL), true, ItemGroup.TRANSPORTATION);
	public static final CopperRailBlock WAXED_EXPOSED_COPPER_RAIL = new CopperRailBlock(1.0, QuiltBlockSettings.copy(EXPOSED_COPPER_RAIL), true, ItemGroup.TRANSPORTATION);
	public static final CopperRailBlock WAXED_WEATHERED_COPPER_RAIL = new CopperRailBlock(0.8, QuiltBlockSettings.copy(WEATHERED_COPPER_RAIL), true, ItemGroup.TRANSPORTATION);
	public static final CopperRailBlock WAXED_OXIDIZED_COPPER_RAIL = new CopperRailBlock(0.6, QuiltBlockSettings.copy(OXIDIZED_COPPER_RAIL), true, ItemGroup.TRANSPORTATION);

	@Override
	public void register() {
		COPPER_CORE.register(BetterCopper.createId("copper_core"));
		EXPOSED_COPPER_CORE.register(BetterCopper.createId("exposed_copper_core"));
		WEATHERED_COPPER_CORE.register(BetterCopper.createId("weathered_copper_core"));
		OXIDIZED_COPPER_CORE.register(BetterCopper.createId("oxidized_copper_core"));
		WAXED_COPPER_CORE.register(BetterCopper.createId("waxed_copper_core"));
		WAXED_EXPOSED_COPPER_CORE.register(BetterCopper.createId("waxed_exposed_copper_core"));
		WAXED_WEATHERED_COPPER_CORE.register(BetterCopper.createId("waxed_weathered_copper_core"));
		WAXED_OXIDIZED_COPPER_CORE.register(BetterCopper.createId("waxed_oxidized_copper_core"));
		NETHERITE_COATED_GOLD_BLOCK.register(BetterCopper.createId("netherite_coated_gold_block"));
		COPPER_RAIL.register(BetterCopper.createId("copper_rail"));
		EXPOSED_COPPER_RAIL.register(BetterCopper.createId("exposed_copper_rail"));
		WEATHERED_COPPER_RAIL.register(BetterCopper.createId("weathered_copper_rail"));
		OXIDIZED_COPPER_RAIL.register(BetterCopper.createId("oxidized_copper_rail"));
		WAXED_COPPER_RAIL.register(BetterCopper.createId("waxed_copper_rail"));
		WAXED_EXPOSED_COPPER_RAIL.register(BetterCopper.createId("waxed_weathered_copper_rail"));
		WAXED_WEATHERED_COPPER_RAIL.register(BetterCopper.createId("waxed_exposed_copper_rail"));
		WAXED_OXIDIZED_COPPER_RAIL.register(BetterCopper.createId("waxed_oxidized_copper_rail"));
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
