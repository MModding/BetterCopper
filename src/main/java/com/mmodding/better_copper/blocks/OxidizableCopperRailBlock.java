package com.mmodding.better_copper.blocks;

import com.mmodding.mmodding_lib.library.blocks.BlockRegistrable;
import com.mmodding.mmodding_lib.library.blocks.BlockWithItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.RailBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.concurrent.atomic.AtomicBoolean;

public class OxidizableCopperRailBlock extends RailBlock implements Oxidizable, CopperRailElement, BlockRegistrable, BlockWithItem {

	private final AtomicBoolean registered = new AtomicBoolean(false);

	private BlockItem item = null;

	private final Oxidizable.OxidizationLevel oxidizationLevel;

	public OxidizableCopperRailBlock(Settings settings, boolean hasItem, ItemGroup itemGroup) {
		this(OxidizationLevel.UNAFFECTED, settings, hasItem, itemGroup);
	}

	public OxidizableCopperRailBlock(Oxidizable.OxidizationLevel oxidizationLevel, Settings settings, boolean hasItem, ItemGroup itemGroup) {
		this(oxidizationLevel, settings, hasItem, itemGroup != null ? new QuiltItemSettings().group(itemGroup) : new QuiltItemSettings());
	}

	public OxidizableCopperRailBlock(Oxidizable.OxidizationLevel oxidizationLevel, Settings settings, boolean hasItem, Item.Settings itemSettings) {
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

	@Override
	public float getVelocityMultiplier() {
		return switch (this.getDegradationLevel()) {
			case UNAFFECTED -> 1.2f;
			case EXPOSED -> 1.0f;
			case WEATHERED -> 0.8f;
			case OXIDIZED -> 0.6f;
		};
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
