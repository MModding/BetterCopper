package com.mmodding.better_copper.blocks.entities;

import com.mmodding.better_copper.charge.Energy;
import com.mmodding.better_copper.charge.GenerationSource;
import com.mmodding.better_copper.init.BlockEntities;
import com.mmodding.mmodding_lib.library.tags.MModdingBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class CopperCoreBlockEntity extends BlockEntity implements GameEventListener {

	private final Function<Integer, String> formatter = i -> Integer.toString(i);
	private int energy;

	public CopperCoreBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntities.COPPER_CORE, blockPos, blockState);
	}

	@Override
	public boolean shouldListenImmediately() {
		return true;
	}

	@Override
	public PositionSource getPositionSource() {
		return new BlockPositionSource(this.pos);
	}

	@Override
	public int getRange() {
		return 50;
	}

	@Override
	public boolean listen(ServerWorld world, GameEvent.Message eventMessage) {
		if (this.isRemoved()) return false;
		GameEvent.Context context = eventMessage.getContext();
		if (eventMessage.getEvent() != GameEvent.ENTITY_DIE) return false;
		Entity entity = context.sourceEntity();
		if (!(entity instanceof LivingEntity livingEntity)) return false;
		if (!isSourcePosOxidizable(livingEntity.world, this.pos, livingEntity.getBlockPos().down(), 0, 360, new HashSet<>()))
			return false;
		this.addEnergy(GenerationSource.DEATH.getPower());
		if (livingEntity.world.isClient) Energy.spawnEnergyParticles(livingEntity.world, livingEntity.getBlockPos());
		return true;
	}

	private static boolean isSourcePosOxidizable(World world, BlockPos startPos, BlockPos endPos, int iterator, int limit, Set<BlockPos> visitedPos) {
		if (startPos == null || iterator > limit) return false;
		if (startPos.equals(endPos)) return true;
		visitedPos.add(startPos);
		for (Direction dir : Direction.values()) {
			BlockPos otherPos = startPos.offset(dir);
			if (!visitedPos.contains(otherPos) && world.getBlockState(otherPos).isIn(MModdingBlockTags.OXIDIZABLE)) {
				if (otherPos.equals(endPos)) return true;
				return isSourcePosOxidizable(world, otherPos, endPos, iterator + 6, limit, visitedPos);
			}
		}
		return false;
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
