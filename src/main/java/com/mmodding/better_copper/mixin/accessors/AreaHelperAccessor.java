package com.mmodding.better_copper.mixin.accessors;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.dimension.AreaHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AreaHelper.class)
public interface AreaHelperAccessor {

	@Accessor
	Direction getNegativeDir();

	@Invoker("getHeight")
	int getHeight();

	@Invoker("getWidth")
	int getWidth();

	@Invoker("getLowerCorner")
	BlockPos invokeGetLowerCorner(BlockPos pos);
}
