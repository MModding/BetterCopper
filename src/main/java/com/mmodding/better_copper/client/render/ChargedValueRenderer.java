package com.mmodding.better_copper.client.render;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.better_copper.client.BetterCopperClient;
import com.mmodding.better_copper.mixin.AreaHelperAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.AreaHelper;

public class ChargedValueRenderer {

	private static final Vec3d OFFSET = new Vec3d(0.5, 13 / 16d, 0.5);

	public static void tick(MinecraftClient minecraftClient) {
		if (minecraftClient.world == null || minecraftClient.player == null) return;

		HitResult target = minecraftClient.crosshairTarget;
		if (!(target instanceof BlockHitResult blockHitResult)) return;

		BlockPos blockPos = blockHitResult.getBlockPos();
		Direction face = blockHitResult.getSide();
		if (!(minecraftClient.world.getBlockEntity(blockPos) instanceof CopperPowerBlockEntity copperPowerBlockEntity))
			return;

		AreaHelper areaHelper = new AreaHelper(minecraftClient.world, blockPos, face.getAxis());
		BlockPos lowerCorner = ((AreaHelperAccessor) areaHelper).invokeGetLowerCorner(blockPos);

		boolean highlight = (lowerCorner == null ? target.getPos().distanceTo(OFFSET)
				: target.getPos().subtract(Vec3d.of(lowerCorner)).distanceTo(OFFSET)) < (.4f / 2);
		addBox(copperPowerBlockEntity, blockPos, face, highlight);
	}

	private static void addBox(CopperPowerBlockEntity copperPowerBlockEntity, BlockPos blockPos, Direction face, boolean highlight) {
		Box box = new Box(Vec3d.ZERO, Vec3d.ZERO).expand(.5f).contract(0, 0, -.5f).offset(0, 0, -.125f);
		MutableText genericEnergy = Utils.translatable("generic.energy");
		MutableText energy = Utils.literal(copperPowerBlockEntity.formatEnergy());

		ValueBox.Transform.Centered slot = new ValueBox.Transform.Centered((powerBlock, side)
				-> (side.getAxis() == Direction.Axis.X) || (side.getAxis() == Direction.Axis.Z));
		ValueBox valueBox = new ValueBox.TextValueBox(genericEnergy, box, blockPos, energy)
				.offsetLabel(Vec3d.ZERO.add(20, -10, 0)).withColors(0x5A5D5A, 0xB5B7B6).passive(!highlight);
		BetterCopperClient.BOX_OUTLINE.showValueBox(blockPos, valueBox.transform(slot)).lineWidth(1 / 64f).highlightFace(face);
	}
}
