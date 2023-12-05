package com.mmodding.better_copper.blocks;

import com.mmodding.better_copper.ducks.CopperRailElement;
import com.mmodding.mmodding_lib.library.blocks.BlockRegistrable;
import com.mmodding.mmodding_lib.library.blocks.BlockWithItem;
import net.minecraft.block.RailBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.concurrent.atomic.AtomicBoolean;

public class CopperRailBlock extends RailBlock implements CopperRailElement, BlockRegistrable, BlockWithItem {

	private final AtomicBoolean registered = new AtomicBoolean(false);

	private BlockItem item = null;

	private final float velocity;

	public CopperRailBlock(float velocity, Settings settings, boolean hasItem, ItemGroup itemGroup) {
		this(velocity, settings, hasItem, itemGroup != null ? new QuiltItemSettings().group(itemGroup) : new QuiltItemSettings());
	}

	public CopperRailBlock(float velocity, Settings settings, boolean hasItem, Item.Settings itemSettings) {
		super(settings);
		if (hasItem) this.item = new BlockItem(this, itemSettings);
		this.velocity = velocity;
	}

	@Override
	public float getVelocityMultiplier() {
		return this.velocity;
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
