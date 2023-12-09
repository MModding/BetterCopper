package com.mmodding.better_copper.mixin.accessors;

import com.mmodding.better_copper.ducks.WorldAccessDuckInterface;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.tick.OrderedTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldAccess.class)
public interface WorldAccessAccessor extends WorldAccessDuckInterface {

	@Shadow
	private <T> OrderedTick<T> createTick(BlockPos pos, T type, int delay) {
		throw new LinkageError();
	}

	@Override
	default <T> OrderedTick<T> better_copper$callCreateTick(BlockPos pos, T type, int delay) {
		return this.createTick(pos, type, delay);
	}
}
