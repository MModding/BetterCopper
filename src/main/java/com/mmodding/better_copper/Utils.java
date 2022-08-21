package com.mmodding.better_copper;

import com.mmodding.better_copper.init.Blocks;
import net.minecraft.block.OxidizableBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Utils {

	public static final String modIdentifier = "better_copper";

	public static Identifier newIdentifier(String path) {
		return new Identifier(Utils.modIdentifier, path);
	}

	@Nullable
	public static BlockPos getLinkedPowerBlock(World world, BlockPos pos, Direction[] directions, int i) {
		if (i >= 200) return null;
		for (Direction dir : directions) {
			BlockPos otherPos = pos.offset(dir);
			if (world.getBlockState(otherPos).getBlock() instanceof OxidizableBlock) {
				if (world.getBlockState(otherPos).getBlock() == Blocks.COPPER_POWER_BLOCK) return otherPos;
				return getLinkedPowerBlock(world, otherPos, directions, i + 6);
			}
		}
		return null;
	}

	public static boolean isInNetwork(World world, BlockPos pos, Direction[] directions) {
		return getLinkedPowerBlock(world, pos, directions, 0) != null;
	}
}
