package com.mmodding.better_copper.blocks;

import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.better_copper.charge.ConsumeSource;
import com.mmodding.better_copper.charge.GenerationSource;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.mmodding_lib.library.blocks.BlockRegistrable;
import com.mmodding.mmodding_lib.library.blocks.BlockWithItem;
import com.mmodding.mmodding_lib.library.blocks.CustomBlockWithEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class CopperPowerBlock extends CustomBlockWithEntity implements BlockRegistrable, BlockWithItem {

	public static final TagKey<Block> OXIDIZABLE_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier("mmodding_lib", "oxidizable"));
	private final AtomicBoolean registered = new AtomicBoolean(false);
	private BlockItem item = null;

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public CopperPowerBlock(AbstractBlock.Settings settings, boolean hasItem, ItemGroup itemGroup) {
		this(settings, hasItem, itemGroup != null ? new QuiltItemSettings().group(itemGroup) : new QuiltItemSettings());
	}

	public CopperPowerBlock(Settings settings, boolean hasItem, Item.Settings itemSettings) {
		super(settings);
		if (hasItem) this.item = new BlockItem(this, itemSettings);
	}

	@Nullable
	private BlockPos isLinkedTo(World world, BlockPos pos, int i) {
		if (i >= 200) return null;
		for (Direction dir : Direction.values()) {
			BlockPos otherPos = pos.offset(dir);
			BlockState otherState = world.getBlockState(otherPos);
			if (otherState.isOf(Blocks.COPPER_POWER_BLOCK)) return otherPos;
			if (otherState.isIn(OXIDIZABLE_BLOCKS)) return isLinkedTo(world, otherPos, i + 6);
		}
		return null;
	}

	@Nullable
	public BlockPos isLinkedTo(World world, BlockPos pos) {
		return isLinkedTo(world, pos, 0);
	}

	@Nullable
	public BlockPos addEnergyIfConnected(World world, BlockPos blockPos, GenerationSource generationSource, int count) {
		BlockPos linkedPos = isLinkedTo(world, blockPos);
		if (linkedPos != null) {
			BlockEntity blockEntity = world.getBlockEntity(linkedPos);
			if (blockEntity instanceof CopperPowerBlockEntity copperPowerBlockEntity) {
				copperPowerBlockEntity.addEnergy(generationSource.getPower() * count);
			}
		}
		return linkedPos;
	}

	public Pair<Integer, BlockPos> consumeEnergyIfConnected(World world, BlockPos blockPos, ConsumeSource consumeSource) {
		BlockPos linkedPos = isLinkedTo(world, blockPos);
		if (linkedPos != null) {
			BlockEntity blockEntity = world.getBlockEntity(linkedPos);
			if (blockEntity instanceof CopperPowerBlockEntity copperPowerBlockEntity) {
				return new Pair<>(copperPowerBlockEntity.removeEnergy(consumeSource.getPower()), linkedPos);
			}
		}
		return new Pair<>(0, BlockPos.ORIGIN);
	}

	@Nullable
	public BlockPos addEnergyWithParticlesIfConnected(World world, BlockPos blockPos, GenerationSource generationSource, BlockPos particlePos) {
		BlockPos foundPos = addEnergyIfConnected(world, blockPos, generationSource, 1);
		if (foundPos != null) spawnEnergyParticles(world, particlePos);
		return foundPos;
	}

	@Nullable
	public BlockPos addEnergyWithParticlesIfConnected(World world, BlockPos blockPos, GenerationSource generationSource, int count) {
		BlockPos foundPos = addEnergyIfConnected(world, blockPos, generationSource, count);
		if (foundPos != null) spawnEnergyParticles(world, blockPos);
		return foundPos;
	}

	@Nullable
	public BlockPos addEnergyWithParticlesIfConnected(World world, BlockPos blockPos, GenerationSource generationSource, int count, BlockPos particlePos) {
		BlockPos foundPos = addEnergyIfConnected(world, blockPos, generationSource, count);
		if (foundPos != null) spawnEnergyParticles(world, particlePos);
		return foundPos;
	}

	public Pair<Integer, BlockPos> consumeEnergyWithParticlesIfConnected(World world, BlockPos blockPos, ConsumeSource consumeSource, BlockPos particlePos) {
		Pair<Integer, BlockPos> foundPos = consumeEnergyIfConnected(world, blockPos, consumeSource);
		if (foundPos.getLeft() != 0) spawnEnergyParticles(world, particlePos);
		return foundPos;
	}

	public void spawnEnergyParticles(World world, BlockPos pos) {
		double d = (double) pos.getX() + 0.5;
		double e = (double) pos.getY() + 0.7;
		double f = (double) pos.getZ() + 0.5;
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, d, e, f, 0.1, 0.3, 0.1);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CopperPowerBlockEntity(pos, state);
	}

	@Override
	public BlockItem getItem() {
		return this.item;
	}

	@Override
	public boolean isNotRegistered() {
		return !this.registered.get();
	}

	@Override
	public void setRegistered() {
		this.registered.set(true);
	}
}
