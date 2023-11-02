package com.mmodding.better_copper.mixin.accessors;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.AreaHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AreaHelper.class)
public interface AreaHelperAccessor {

	@Invoker("getLowerCorner")
	BlockPos invokeGetLowerCorner(BlockPos pos);
}
