package com.mmodding.better_copper.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	public abstract Box getBoundingBox();

	@Shadow
	abstract public BlockPos getBlockPos();

	@Shadow
	public World world;
}
