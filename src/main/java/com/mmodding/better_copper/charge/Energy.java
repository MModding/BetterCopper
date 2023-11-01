package com.mmodding.better_copper.charge;

import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.mmodding_lib.library.tags.MModdingBlockTags;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Energy {

	@Nullable
	private static BlockPos getNearestPowerBlockPos(World world, BlockPos blockPos, int i, Set<BlockPos> visitedPos) {
		if (blockPos == null || i >= 200) return null;
		visitedPos.add(blockPos);
		for (Direction dir : Direction.values()) {
			BlockPos otherPos = blockPos.offset(dir);
			if (!visitedPos.contains(otherPos)) {
				if (world.getBlockState(otherPos).isOf(Blocks.COPPER_POWER_BLOCK)) return otherPos;
				if (world.getBlockState(otherPos).isIn(MModdingBlockTags.OXIDIZABLE)) {
					BlockPos contPos = getNearestPowerBlockPos(world, otherPos, i + 6, visitedPos);
					if (contPos != null) return contPos;
				}
			}
		}
		return null;
	}

	public static void addEnergyToPowerBlock(World world, BlockPos blockPos, GenerationSource generationSource, int count, BlockPos particlePos) {
		BlockPos powerBlockPos = getNearestPowerBlockPos(world, blockPos, 0, new HashSet<>());
		if (powerBlockPos == null) return;
		BlockEntity blockEntity = world.getBlockEntity(powerBlockPos);
		if (blockEntity instanceof CopperPowerBlockEntity copperPowerBlockEntity) {
			copperPowerBlockEntity.addEnergy(generationSource.getPower() * count);
			if (world.isClient) spawnEnergyParticles(world, particlePos);
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

	public static int removeEnergyFromPowerBlock(World world, BlockPos blockPos, ConsumeSource consumeSource, int count, BlockPos particlePos) {
		BlockPos powerBlockPos = getNearestPowerBlockPos(world, blockPos, 0, new HashSet<>());
		if (powerBlockPos == null) return 0;
		BlockEntity blockEntity = world.getBlockEntity(powerBlockPos);
		if (blockEntity instanceof CopperPowerBlockEntity copperPowerBlockEntity) {
			int energyConsumed = copperPowerBlockEntity.removeEnergy(consumeSource.getPower() * count);
			if (energyConsumed > 0 && world.isClient)
				spawnEnergyParticles(world, particlePos);
			return energyConsumed;
		}
		return 0;
	}

	public static int removeEnergyFromPowerBlock(World world, BlockPos blockPos, ConsumeSource consumeSource, BlockPos particlePos) {
		return removeEnergyFromPowerBlock(world, blockPos, consumeSource, 1, particlePos);
	}

	public static void spawnEnergyParticles(World world, BlockPos pos) {
		double d = (double) pos.getX() + 0.5;
		double e = (double) pos.getY() + 0.7;
		double f = (double) pos.getZ() + 0.5;
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, d, e, f, 0.1, 0.3, 0.1);
	}
}
