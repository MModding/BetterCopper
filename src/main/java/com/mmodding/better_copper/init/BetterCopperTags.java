package com.mmodding.better_copper.init;

import com.mmodding.better_copper.BetterCopper;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class BetterCopperTags {

	public static final TagKey<Block> COPPER_CORES = TagKey.of(Registry.BLOCK_KEY, BetterCopper.createId("copper_cores"));
}
