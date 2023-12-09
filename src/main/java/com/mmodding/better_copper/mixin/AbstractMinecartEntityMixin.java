package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.blocks.CopperRailElement;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends EntityMixin {

	@Inject(method = "getVelocityMultiplier", at = @At("TAIL"), cancellable = true)
	private void changeVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
		BlockState state = this.world.getBlockState(this.getBlockPos());
		if (state.getBlock() instanceof CopperRailElement rail) {
			cir.setReturnValue(cir.getReturnValueF() * rail.getVelocityMultiplier());
		}
	}

	/* @WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d addEastVelocity(Vec3d vec3d2, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? vec3d2.add(rail.getVelocityMultiplier() * x, rail.getVelocityY() * y, rail.getVelocityZ() * z) : original.call(vec3d2, x, y, z);
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 1))
	private Vec3d addWestVelocity(Vec3d vec3d2, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? vec3d2.add(rail.getVelocityMultiplier() * x, rail.getVelocityY() * y, rail.getVelocityZ() * z) : original.call(vec3d2, x, y, z);
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 2))
	private Vec3d addNorthVelocity(Vec3d vec3d2, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? vec3d2.add(rail.getVelocityMultiplier() * x, rail.getVelocityY() * y, rail.getVelocityZ() * z) : original.call(vec3d2, x, y, z);
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 3))
	private Vec3d addSouthVelocity(Vec3d vec3d2, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? vec3d2.add(rail.getVelocityMultiplier() * x, rail.getVelocityY() * y, rail.getVelocityZ() * z) : original.call(vec3d2, x, y, z);
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 4))
	private void resetVelocity(AbstractMinecartEntity entity, Vec3d velocity, Operation<Void> original, @Local BlockState state) {
		if (state.getBlock() instanceof CopperRailBlock rail) {
			entity.setVelocity(velocity.x * rail.getVelocityMultiplier(), velocity.y * rail.getVelocityY(), velocity.z * rail.getVelocityZ());
		} else {
			original.call(entity, velocity);
		}
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 5))
	private void setPlayerVelocity(AbstractMinecartEntity entity, Vec3d velocity, Operation<Void> original, @Local BlockState state) {
		if (state.getBlock() instanceof CopperRailBlock rail) {
			entity.setVelocity(velocity.x * rail.getVelocityMultiplier(), velocity.y * rail.getVelocityY(), velocity.z * rail.getVelocityZ());
		} else {
			original.call(entity, velocity);
		}
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(DDD)V", ordinal = 1))
	private void lastResetVelocity(AbstractMinecartEntity entity, double x, double y, double z, Operation<Void> original, @Local BlockState state) {
		if (state.getBlock() instanceof CopperRailBlock rail) {
			entity.setVelocity(x * rail.getVelocityMultiplier(), y * rail.getVelocityY(), z * rail.getVelocityZ());
		} else {
			original.call(entity, x, y, z);
		}
	}

	@WrapOperation(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 1))
	private Vec3d changeVelocity(Vec3d velocity, double x, double y, double z, Operation<Vec3d> original, @Local BlockState state) {
		return state.getBlock() instanceof CopperRailElement rail ? velocity.multiply(rail.getVelocityMultiplier() * x, rail.getVelocityY() * y, rail.getVelocityZ() * z) : original.call(velocity, x, y, z);
	} */
}
