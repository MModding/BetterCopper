package com.mmodding.better_copper.charge;

import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.mmodding_lib.library.blocks.BlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface Charge {

	default void charge(ItemStack stack, int charge) {
		stack.getOrCreateNbt().putInt("charge", this.getCharge(stack) + charge);
	}

	default int getCharge(ItemStack stack) {
		if (stack.getNbt() == null) return 0;
		return stack.getOrCreateNbt().getInt("charge");
	}

	static boolean isStackCharged(ItemStack stack) {
		if (stack.getNbt() == null) return false;
		return stack.getNbt().getInt("charge") != 0;
	}

	default boolean isCharged(ItemStack stack) {
		if (stack.getNbt() == null) return false;
		return stack.getNbt().getInt("charge") != 0;
	}

	@Nullable
	private BlockPos getLinkedPowerBlock(World world, BlockPos pos, int i) {
		if (pos == null) return null;
		if (i >= 200) return null;
		for (Direction dir : Direction.values()) {
			BlockPos otherPos = pos.offset(dir);
			BlockState otherState = world.getBlockState(otherPos);
			if (otherState.isOf(Blocks.COPPER_POWER_BLOCK)) return otherPos;
			if (otherState.isIn(BlockTags.OXIDIZABLE)) return getLinkedPowerBlock(world, otherPos, i + 6);
		}
		return null;
	}

	@Nullable
	default BlockPos addEnergyIfConnected(World world, BlockPos blockPos, GenerationSource generationSource, int count, BlockPos particlePos) {
		BlockPos linkedPos = getLinkedPowerBlock(world, blockPos, 0);
		if (linkedPos != null) {
			System.out.println("Found power block");
			BlockEntity blockEntity = world.getBlockEntity(linkedPos);
			if (blockEntity instanceof CopperPowerBlockEntity copperPowerBlockEntity) {
				copperPowerBlockEntity.addEnergy(generationSource.getPower() * count);
				spawnEnergyParticles(world, particlePos);
			}
		}
		return linkedPos;
	}

	@Nullable
	default BlockPos addEnergyIfConnected(World world, BlockPos blockPos, GenerationSource generationSource, int count) {
		return addEnergyIfConnected(world, blockPos, generationSource, count, blockPos);
	}

	@Nullable
	default BlockPos addEnergyIfConnected(World world, BlockPos blockPos, GenerationSource generationSource, BlockPos particlePos) {
		return addEnergyIfConnected(world, blockPos, generationSource, 1, particlePos);
	}

	@Nullable
	default BlockPos addEnergyIfConnected(World world, BlockPos blockPos, GenerationSource generationSource) {
		return addEnergyIfConnected(world, blockPos, generationSource, 1, blockPos);
	}

	default Pair<Integer, BlockPos> consumeEnergyIfConnected(World world, BlockPos blockPos, ConsumeSource consumeSource, int count, BlockPos particlePos) {
		BlockPos linkedPos = getLinkedPowerBlock(world, blockPos, 0);
		if (linkedPos != null) {
			BlockEntity blockEntity = world.getBlockEntity(linkedPos);
			if (blockEntity instanceof CopperPowerBlockEntity copperPowerBlockEntity) {
				spawnEnergyParticles(world, particlePos);
				return new Pair<>(copperPowerBlockEntity.removeEnergy(consumeSource.getPower() * count), linkedPos);
			}
		}
		return new Pair<>(0, BlockPos.ORIGIN);
	}

	default void spawnEnergyParticles(World world, BlockPos pos) {
		double d = (double) pos.getX() + 0.5;
		double e = (double) pos.getY() + 0.7;
		double f = (double) pos.getZ() + 0.5;
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, d, e, f, 0.1, 0.3, 0.1);
	}
}
