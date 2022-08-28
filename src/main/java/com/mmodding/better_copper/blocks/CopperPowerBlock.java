package com.mmodding.better_copper.blocks;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.better_copper.charge.ConsumeSource;
import com.mmodding.better_copper.charge.GenerationSource;
import com.mmodding.mmodding_lib.library.blocks.BlockRegistrable;
import com.mmodding.mmodding_lib.library.blocks.BlockWithItem;
import com.mmodding.mmodding_lib.library.blocks.CustomBlockWithEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.OxidizableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class CopperPowerBlock extends CustomBlockWithEntity implements BlockRegistrable, BlockWithItem {

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
			if (world.getBlockState(otherPos).getBlock() == this) return otherPos;
			if (world.getBlockState(otherPos).getBlock() instanceof OxidizableBlock) {
				return isLinkedTo(world, otherPos, i + 6);
			}
		}
		return null;
	}

	@Nullable
	public BlockPos isLinkedTo(World world, BlockPos pos) {
		return isLinkedTo(world, pos, 0);
	}

	public void addEnergyIfConnected(World world, BlockPos blockPos, GenerationSource generationSource, int count) {
		BlockPos linkedPos = isLinkedTo(world, blockPos);
		if (linkedPos != null) {
			BlockEntity blockEntity = world.getBlockEntity(linkedPos);
			if (blockEntity instanceof CopperPowerBlockEntity copperPowerBlockEntity) {
				copperPowerBlockEntity.addEnergy(generationSource.getPower() * count);
			}
		}
	}

	public int consumeEnergyIfConnected(World world, BlockPos blockPos, ConsumeSource consumeSource) {
		BlockPos linkedPos = isLinkedTo(world, blockPos);
		if (linkedPos != null) {
			BlockEntity blockEntity = world.getBlockEntity(linkedPos);
			if (blockEntity instanceof CopperPowerBlockEntity copperPowerBlockEntity) {
				return copperPowerBlockEntity.removeEnergy(consumeSource.getPower());
			}
		}
		return 0;
	}

	public void addEnergyIfConnected(World world, BlockPos blockPos, GenerationSource generationSource) {
		addEnergyIfConnected(world, blockPos, generationSource, 1);
	}

	public void addEnergyIfConnected(PlayerEntity player, GenerationSource generationSource, int count) {
		addEnergyIfConnected(player.world, Utils.getOpenScreenPos(), generationSource, count);
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
