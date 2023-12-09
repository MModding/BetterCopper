package com.mmodding.better_copper.ducks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.OrderedTick;

public interface WorldAccessDuckInterface {

	<T> OrderedTick<T> better_copper$callCreateTick(BlockPos pos, T type, int delay);
}
