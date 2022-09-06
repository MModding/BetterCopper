package com.mmodding.better_copper.charge;

import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.mmodding_lib.library.blocks.BlockTags;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Energy {

	private static final List<BlockPos> powerBlocks = new ArrayList<>();

	private static void populatePowerBlocks(World world, BlockPos blockPos, int i) {
		if (blockPos == null) return;
		if (i >= 200) return;
		for (Direction dir : Direction.values()) {
			BlockPos otherPos = blockPos.offset(dir);
			if (world.getBlockState(otherPos).isOf(Blocks.COPPER_POWER_BLOCK)) {
				powerBlocks.add(otherPos);
				return;
			}
			if (world.getBlockState(otherPos).isIn(BlockTags.OXIDIZABLE)) populatePowerBlocks(world, blockPos, i + 6);
		}
	}

	public static void addEnergyToPowerBlock(World world, BlockPos blockPos, GenerationSource generationSource, int count, BlockPos particlePos) {
		powerBlocks.clear();
		populatePowerBlocks(world, blockPos, 0);
		if (powerBlocks.isEmpty()) return;
		BlockEntity blockEntity = world.getBlockEntity(powerBlocks.get(0));
		if (blockEntity instanceof CopperPowerBlockEntity copperPowerBlockEntity) {
			copperPowerBlockEntity.addEnergy(generationSource.getPower() * count);
			spawnEnergyParticles(world, particlePos);
		}
	}

	public static void addEnergyToPowerBlock(World world, BlockPos blockPos, GenerationSource generationSource, int count) {
		addEnergyToPowerBlock(world, blockPos, generationSource, count, blockPos);
	}

	public static void addEnergyToPowerBlock(World world, BlockPos blockPos, GenerationSource generationSource, BlockPos particlePos) {
		addEnergyToPowerBlock(world, blockPos, generationSource, 1, particlePos);
	}

	public static void addEnergyToPowerBlock(World world, BlockPos blockPos, GenerationSource generationSource) {
		addEnergyToPowerBlock(world, blockPos, generationSource, 1, blockPos);
	}

	public static void spawnEnergyParticles(World world, BlockPos pos) {
		double d = (double) pos.getX() + 0.5;
		double e = (double) pos.getY() + 0.7;
		double f = (double) pos.getZ() + 0.5;
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, d, e, f, 0.1, 0.3, 0.1);
	}
}
