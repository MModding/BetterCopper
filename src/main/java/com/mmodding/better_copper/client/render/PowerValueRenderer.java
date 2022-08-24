package com.mmodding.better_copper.client.render;

import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.better_copper.client.BetterCopperClient;
import com.mmodding.better_copper.mixin.AreaHelperAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.AreaHelper;

public abstract class PowerValueRenderer {

	private static final Vec3d OFFSET = new Vec3d(0.5, 13 / 16d, 0.5);

	public static void tick() {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		HitResult target = minecraftClient.crosshairTarget;
		if (!(target instanceof BlockHitResult blockHitResult))
			return;

		ClientWorld world = minecraftClient.world;
		if (world == null) return;

		BlockPos blockPos = blockHitResult.getBlockPos();
		Direction face = blockHitResult.getSide();
		if (!(world.getBlockEntity(blockPos) instanceof CopperPowerBlockEntity copperPowerBlockEntity)) return;

		AreaHelper areaHelper = new AreaHelper(world, blockPos, face.getAxis());
		Vec3d vec3d = Vec3d.of(((AreaHelperAccessor) areaHelper).invokeGetLowerCorner(blockPos));

		boolean highlight = target.getPos().subtract(vec3d).distanceTo(OFFSET) < (.4f / 2);
		addBox(copperPowerBlockEntity, blockPos, face, highlight);
	}

	protected static void addBox(CopperPowerBlockEntity copperPowerBlockEntity, BlockPos blockPos, Direction face, boolean highlight) {
		Box box = new Box(Vec3d.ZERO, Vec3d.ZERO).expand(.5f).contract(0, 0, -.5f).offset(0, 0, -.125f);
		Text text = new LiteralText("" + copperPowerBlockEntity.getEnergy() + "");
		ValueBoxTransform.Centered slot = new ValueBoxTransform.Centered((powerBlock, side) -> true);
		ValueBox valueBox;

		valueBox = new ValueBox.TextValueBox(text, box, blockPos, text);
		valueBox.offsetLabel(Vec3d.ZERO.add(20, -10, 0)).withColors(0x5A5D5A, 0xB5B7B6).passive(!highlight);
		BetterCopperClient.BOX_OUTLINE.showValueBox(blockPos, valueBox.transform(slot)).lineWidth(1 / 64f).highlightFace(face);
	}
}
