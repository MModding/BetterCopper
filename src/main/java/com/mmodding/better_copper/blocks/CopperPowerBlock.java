package com.mmodding.better_copper.blocks;

import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.mmodding_lib.library.blocks.BlockRegistrable;
import com.mmodding.mmodding_lib.library.blocks.BlockWithItem;
import com.mmodding.mmodding_lib.library.blocks.CustomBlockWithEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.OxidizableBlock;
import net.minecraft.block.entity.BlockEntity;
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

	public CopperPowerBlock(AbstractBlock.Settings settings, boolean hasItem, ItemGroup itemGroup) {
		this(settings, hasItem, itemGroup != null ? new QuiltItemSettings().group(itemGroup) : new QuiltItemSettings());
	}

	public CopperPowerBlock(Settings settings, boolean hasItem, Item.Settings itemSettings) {
		super(settings);
		if (hasItem) this.item = new BlockItem(this, itemSettings);
	}

	@Nullable
	private BlockPos isLinkedTo(World world, BlockPos pos, Direction[] directions, int i) {
		if (i >= 200) return null;
		for (Direction dir : directions) {
			BlockPos otherPos = pos.offset(dir);
			if (world.getBlockState(otherPos).getBlock() == this) return otherPos;
			if (world.getBlockState(otherPos).getBlock() instanceof OxidizableBlock) {
				return isLinkedTo(world, otherPos, directions, i + 6);
			}
		}
		return null;
	}

	@Nullable
	public BlockPos isLinkedTo(World world, BlockPos pos, Direction[] directions) {
		return isLinkedTo(world, pos, directions, 0);
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
