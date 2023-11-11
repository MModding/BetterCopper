package com.mmodding.better_copper.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mmodding.better_copper.blocks.CopperRailBlock;
import com.mmodding.better_copper.ducks.CopperRailElement;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractMinecartEntity.class)
public class AbstractMinecartEntityMixin {

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d addEastVelocity(Vec3d vec3d2, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? vec3d2.add(rail.getVelocityX() * x, rail.getVelocityY() * y, rail.getVelocityZ() * z) : original.call(vec3d2, x, y, z);
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 1))
	private Vec3d addWestVelocity(Vec3d vec3d2, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? vec3d2.add(rail.getVelocityX() * x, rail.getVelocityY() * y, rail.getVelocityZ() * z) : original.call(vec3d2, x, y, z);
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 2))
	private Vec3d addNorthVelocity(Vec3d vec3d2, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? vec3d2.add(rail.getVelocityX() * x, rail.getVelocityY() * y, rail.getVelocityZ() * z) : original.call(vec3d2, x, y, z);
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 3))
	private Vec3d addSouthVelocity(Vec3d vec3d2, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? vec3d2.add(rail.getVelocityX() * x, rail.getVelocityY() * y, rail.getVelocityZ() * z) : original.call(vec3d2, x, y, z);
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 4))
	private void resetVelocity(AbstractMinecartEntity entity, Vec3d velocity, Operation<Void> original, @Local BlockState state) {
		if (state.getBlock() instanceof CopperRailBlock rail) {
			entity.setVelocity(velocity.x * rail.getVelocityX(), velocity.y * rail.getVelocityY(), velocity.z * rail.getVelocityZ());
		} else {
			original.call(entity, velocity);
		}
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 5))
	private void setPlayerVelocity(AbstractMinecartEntity entity, Vec3d velocity, Operation<Void> original, @Local BlockState state) {
		if (state.getBlock() instanceof CopperRailBlock rail) {
			entity.setVelocity(velocity.x * rail.getVelocityX(), velocity.y * rail.getVelocityY(), velocity.z * rail.getVelocityZ());
		} else {
			original.call(entity, velocity);
		}
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(DDD)V", ordinal = 1))
	private void lastResetVelocity(AbstractMinecartEntity entity, double x, double y, double z, Operation<Void> original, @Local BlockState state) {
		if (state.getBlock() instanceof CopperRailBlock rail) {
			entity.setVelocity(x * rail.getVelocityX(), y * rail.getVelocityY(), z * rail.getVelocityZ());
		} else {
			original.call(entity, x, y, z);
		}
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 1))
	private Vec3d changeVelocity(Vec3d velocity, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? velocity.multiply(rail.getVelocityX() * x, rail.getVelocityY() * y, rail.getVelocityZ() * z) : original.call(velocity, x, y, z);
	}
}
