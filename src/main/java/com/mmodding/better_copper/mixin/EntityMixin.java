package com.mmodding.better_copper.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class EntityMixin {

	@Shadow
	public BlockPos getBlockPos() {
		return null;
	}

	@Shadow
	public World world;
}
