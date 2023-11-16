package com.mmodding.better_copper.blocks.entities;

import com.mmodding.better_copper.init.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class CopperCoreBlockEntity extends BlockEntity {

	private final Function<Integer, String> formatter = i -> Integer.toString(i);
	private int energy;

	public CopperCoreBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntities.COPPER_CORE, blockPos, blockState);
	}

	public int getEnergy() {
		return this.energy;
	}

	public void addEnergy(int energy) {
		this.energy += energy;
	}

	public int removeEnergy(int energyToRemove) {
		int energyToReturn = this.energy;
		if (this.energy == 0 || this.energy < energyToRemove) {
			this.energy = 0;
			return energyToReturn;
		}
		this.energy -= energyToRemove;
		return energyToRemove;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public String formatEnergy() {
		return formatter.apply(this.getEnergy());
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

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt("energy", this.energy);
		return nbt;
	}
}
