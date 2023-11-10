package com.mmodding.better_copper.blocks;

import com.mmodding.better_copper.blocks.entities.CopperCoreBlockEntity;
import com.mmodding.better_copper.charge.Charge;
import com.mmodding.better_copper.ducks.BetterCopperTags;
import com.mmodding.mmodding_lib.library.blocks.BlockRegistrable;
import com.mmodding.mmodding_lib.library.blocks.BlockWithItem;
import com.mmodding.mmodding_lib.library.blocks.CustomBlockWithEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.concurrent.atomic.AtomicBoolean;

public class OxidizableCopperCoreBlock extends CustomBlockWithEntity implements Oxidizable, Charge, BlockRegistrable, BlockWithItem {

	private final Oxidizable.OxidizationLevel oxidizationLevel;
	private final AtomicBoolean registered = new AtomicBoolean(false);
	private BlockItem item = null;

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public OxidizableCopperCoreBlock(AbstractBlock.Settings settings, boolean hasItem, ItemGroup itemGroup) {
		this(OxidizationLevel.UNAFFECTED, settings, hasItem, itemGroup);
	}

	public OxidizableCopperCoreBlock(Oxidizable.OxidizationLevel oxidizationLevel, AbstractBlock.Settings settings, boolean hasItem, ItemGroup itemGroup) {
		this(oxidizationLevel, settings, hasItem, itemGroup != null ? new QuiltItemSettings().group(itemGroup) : new QuiltItemSettings());
	}

	public OxidizableCopperCoreBlock(Oxidizable.OxidizationLevel oxidizationLevel, Settings settings, boolean hasItem, Item.Settings itemSettings) {
		super(settings);
		if (hasItem) this.item = new BlockItem(this, itemSettings);
		this.oxidizationLevel = oxidizationLevel;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		this.tickDegradation(state, world, pos, random);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
	}

	public Oxidizable.OxidizationLevel getDegradationLevel() {
		return this.oxidizationLevel;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CopperCoreBlockEntity(pos, state);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.hasBlockEntity() && !state.isIn(BetterCopperTags.COPPER_CORES)) {
			world.removeBlockEntity(pos);
		}
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
