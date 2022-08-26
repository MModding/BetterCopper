package com.mmodding.better_copper.client;

import com.mmodding.better_copper.blocks.entities.CopperPowerBlockEntity;
import com.mmodding.better_copper.client.render.Outliner;
import com.mmodding.better_copper.client.render.ValueBox;
import com.mmodding.better_copper.client.render.ValueBoxTransform;
import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.mixin.AreaHelperAccessor;
import com.mmodding.mmodding_lib.library.base.MModdingClientModInitializer;
import com.mmodding.mmodding_lib.library.config.Config;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.AreaHelper;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BetterCopperClient implements MModdingClientModInitializer {

	public static final Outliner BOX_OUTLINE = new Outliner();
	private static final Vec3d OFFSET = new Vec3d(0.5, 13 / 16d, 0.5);

	@Override
	public List<ClientElementsInitializer> getClientElementsInitializers() {
		List<ClientElementsInitializer> clientInitializers = new ArrayList<>();
		clientInitializers.add(new Blocks());
		return clientInitializers;
	}

	@Nullable
	@Override
	public Config getClientConfig() {
		return null;
	}

	@Override
	public void onInitializeClient(ModContainer modContainer) {
		MModdingClientModInitializer.super.onInitializeClient(modContainer);
		ClientTickEvents.END.register(this::onTick);
	}

	public void onTick(MinecraftClient minecraftClient) {
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

	protected void addBox(CopperPowerBlockEntity copperPowerBlockEntity, BlockPos blockPos, Direction face, boolean highlight) {
		Box box = new Box(Vec3d.ZERO, Vec3d.ZERO).expand(.5f).contract(0, 0, -.5f).offset(0, 0, -.125f);
		Text text = new LiteralText("" + copperPowerBlockEntity.getEnergy() + "");

		ValueBoxTransform.Centered slot = new ValueBoxTransform.Centered((powerBlock, side)
				-> (side.getAxis() == Direction.Axis.X) || (side.getAxis() == Direction.Axis.Z));
		ValueBox valueBox = new ValueBox.TextValueBox(text, box, blockPos, text).offsetLabel(Vec3d.ZERO.add(20, -10, 0))
				.withColors(0x5A5D5A, 0xB5B7B6).passive(!highlight);
		BOX_OUTLINE.showValueBox(blockPos, valueBox.transform(slot)).lineWidth(1 / 64f).highlightFace(face);
	}
}
