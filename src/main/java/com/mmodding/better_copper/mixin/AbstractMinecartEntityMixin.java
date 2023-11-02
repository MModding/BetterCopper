package com.mmodding.better_copper.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mmodding.better_copper.ducks.CopperRailElement;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractMinecartEntity.class)
public class AbstractMinecartEntityMixin {

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;"))
	private Vec3d changeVelocity(Vec3d velocity, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? velocity.multiply(rail.getVelocityX(), rail.getVelocityY(), rail.getVelocityZ()) : original.call(velocity, x, y, z);
	}
}
