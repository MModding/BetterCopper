package com.mmodding.better_copper.blocks.entities;

import com.mmodding.better_copper.init.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CopperPowerBlockEntity extends BlockEntity {

	public CopperPowerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntities.COPPER_POWER_BLOCK_ENTITY.getBlockEntityTypeIfCreated(), blockPos, blockState);
	}
}
