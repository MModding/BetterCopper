package com.mmodding.better_copper.blocks.entities;

import com.mmodding.better_copper.init.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class CopperPowerBlockEntity extends BlockEntity {

	private int energy;

	public CopperPowerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntities.COPPER_POWER_BLOCK_ENTITY.getBlockEntityTypeIfCreated(), blockPos, blockState);
	}

	public int getEnergy() {
		return this.energy;
	}

	public void addEnergy(int energy) {
		this.energy += energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.energy = nbt.getInt("energy");
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("energy", this.energy);
	}
}
