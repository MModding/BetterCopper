package com.mmodding.better_copper.blocks;

import com.mmodding.better_copper.ducks.WorldAccessAccess;
import com.mmodding.better_copper.magneticfield.MagneticField;
import com.mmodding.mmodding_lib.library.blocks.CustomFallingBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetheriteCoatedGoldBlock extends CustomFallingBlock {

	private final AtomicBoolean registered = new AtomicBoolean(false);
	private BlockItem item = null;
	private MagneticField magneticField;

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public NetheriteCoatedGoldBlock(AbstractBlock.Settings settings, boolean hasItem, ItemGroup itemGroup) {
		this(settings, hasItem, itemGroup != null ? new QuiltItemSettings().group(itemGroup) : new QuiltItemSettings());
	}

	public NetheriteCoatedGoldBlock(Settings settings, boolean hasItem, Item.Settings itemSettings) {
		super(settings);
		if (hasItem) this.item = new BlockItem(this, itemSettings);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		world.getBlockTickScheduler().scheduleTick(((WorldAccessAccess) world).better_copper$callCreateTick(pos, this, 2));
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		if (this.magneticField == null) this.magneticField = new MagneticField(world, pos);
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
