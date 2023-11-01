package com.mmodding.better_copper.magneticfield;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Credit: CustomPortalAPI
 */
public class LoopAreaHelper {

	protected HashSet<Block> VALID_LOOP = null;
	public BlockPos lowerCorner;
	protected WorldAccess world;
	protected int xSize = -1;
	protected int zSize = -1;
	protected final int maxXSize = 21;
	protected final int maxZSize = 21;

	public LoopAreaHelper() {}

	public LoopAreaHelper init(WorldAccess world, BlockPos blockPos, Direction.Axis axis, Block... foundations) {
		VALID_LOOP = Sets.newHashSet(foundations);
		this.world = world;
		this.lowerCorner = this.getLowerCorner(blockPos);
		if (lowerCorner == null) {
			lowerCorner = blockPos;
			xSize = zSize = 1;
		} else {
			this.xSize = this.getSize(Direction.Axis.X);
			if (this.xSize > 0) {
				this.zSize = this.getSize(Direction.Axis.Z);
				if (!checkForValidLoop(xSize, zSize)) {
					lowerCorner = null;
					xSize = zSize = 1;
				}
			}
		}
		return this;
	}

	public Optional<LoopAreaHelper> getNewLoop(WorldAccess worldAccess, BlockPos blockPos, Direction.Axis axis, Block... foundations) {
		return getOrEmpty(worldAccess, blockPos, LoopAreaHelper::isValidLoop, axis, foundations);
	}

	public Optional<LoopAreaHelper> getOrEmpty(WorldAccess worldAccess, BlockPos blockPos, Predicate<LoopAreaHelper> predicate, Direction.Axis axis, Block... foundations) {
		return Optional.of(init(worldAccess, blockPos, axis, foundations)).filter(predicate);
	}

	public boolean isValidLoop() {
		return this.lowerCorner != null && xSize >= 2 && zSize >= 2 && xSize < maxXSize && zSize < maxZSize;
	}

	protected BlockPos getLowerCorner(BlockPos blockPos) {
		if (!world.getBlockState(blockPos).isAir()) return null;
		return getLimitForAxis(getLimitForAxis(blockPos, Direction.Axis.X), Direction.Axis.Z);
	}

	protected BlockPos getLimitForAxis(BlockPos blockPos, Direction.Axis axis) {
		if (blockPos == null || axis == null) return null;
		int offset = 1;
		while (world.getBlockState(blockPos.offset(axis, -offset)).isAir()) {
			offset++;
			if (offset > 20) return null;
			if ((axis.equals(Direction.Axis.Y) && blockPos.getY() - offset < world.getBottomY()) || (!axis.equals(Direction.Axis.Y) && !world.getWorldBorder().contains(blockPos.offset(axis, -offset))))
				return null;
		}
		return blockPos.offset(axis, -(offset - 1));
	}

	protected int getSize(Direction.Axis axis) {
		for (int i = 1; i <= 21; i++) {
			BlockState blockState = this.world.getBlockState(this.lowerCorner.offset(axis, i));
			if (!blockState.isAir()) {
				if (VALID_LOOP.contains(blockState.getBlock())) {
					return i >= 2 ? i : 0;

				}
				break;
			}
		}
		return 0;
	}

	protected boolean checkForValidLoop(int size1, int size2) {
		BlockPos checkPos = lowerCorner.mutableCopy();
		for (int i = 0; i < size1; i++) {
			if (!VALID_LOOP.contains(world.getBlockState(checkPos.offset(Direction.Axis.Z, -1)).getBlock()) || !VALID_LOOP.contains(world.getBlockState(checkPos.offset(Direction.Axis.Z, size2)).getBlock()))
				return false;
			checkPos = checkPos.offset(Direction.Axis.X, 1);
		}
		checkPos = lowerCorner.mutableCopy();
		for (int i = 0; i < size2; i++) {
			if (!VALID_LOOP.contains(world.getBlockState(checkPos.offset(Direction.Axis.X, -1)).getBlock()) || !VALID_LOOP.contains(world.getBlockState(checkPos.offset(Direction.Axis.X, size1)).getBlock()))
				return false;
			checkPos = checkPos.offset(Direction.Axis.Z, 1);
		}
		return true;
	}
}
