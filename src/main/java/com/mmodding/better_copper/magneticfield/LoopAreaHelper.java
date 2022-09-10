package com.mmodding.better_copper.magneticfield;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.util.math.*;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * Credit: CustomPortalAPI
 */
public class LoopAreaHelper {

	private static final LoopAreaHelper INSTANCE = new LoopAreaHelper();

	private Camera CAMERA;
	public List<MagneticField> FIELDS = new ArrayList<>();

	protected HashSet<Block> VALID_LOOP = null;
	public BlockPos lowerCorner;
	protected WorldAccess world;
	protected int xSize = -1;
	protected int zSize = -1;
	protected final int maxXSize = 21;
	protected final int maxZSize = 21;

	public static LoopAreaHelper getInstance() {
		return INSTANCE;
	}

	public void setRenderCamera(Camera camera) {
		CAMERA = camera;
	}

	public Camera getRenderCamera() {
		return CAMERA;
	}

	public boolean isPlayerInField(Box boundingBox) {
		AtomicBoolean bl = new AtomicBoolean(false);
		FIELDS.forEach(magneticField -> {
			if (magneticField.contains(boundingBox)) {
				bl.set(true);
			}
		});
		return bl.get();
	}

	public LoopAreaHelper init(WorldAccess world, BlockPos blockPos, Direction.Axis axis, Block... foundations) {
		VALID_LOOP = Sets.newHashSet(foundations);
		this.world = world;
		this.lowerCorner = this.getLowerCorner(blockPos);
		if (lowerCorner == null) {
			lowerCorner = blockPos;
			xSize = zSize = 1;
		} else {
			this.xSize = this.getSize(Direction.Axis.X);
			if (this.xSize > 0) {
				this.zSize = this.getSize(Direction.Axis.Z);
				if (!checkForValidLoop(xSize, zSize)) {
					lowerCorner = null;
					xSize = zSize = 1;
				}
			}
		}
		return this;
	}

	public Optional<LoopAreaHelper> getNewLoop(WorldAccess worldAccess, BlockPos blockPos, Direction.Axis axis, Block... foundations) {
		return getOrEmpty(worldAccess, blockPos, LoopAreaHelper::isValidLoop, axis, foundations);
	}

	public Optional<LoopAreaHelper> getOrEmpty(WorldAccess worldAccess, BlockPos blockPos, Predicate<LoopAreaHelper> predicate, Direction.Axis axis, Block... foundations) {
		return Optional.of(init(worldAccess, blockPos, axis, foundations)).filter(predicate);
	}

	public boolean isValidLoop() {
		return this.lowerCorner != null && xSize >= 2 && zSize >= 2 && xSize < maxXSize && zSize < maxZSize;
	}

	protected BlockPos getLowerCorner(BlockPos blockPos) {
		if (!validStateInsideLoop(world.getBlockState(blockPos), VALID_LOOP)) return null;
		return getLimitForAxis(getLimitForAxis(blockPos, Direction.Axis.X), Direction.Axis.Z);
	}

	protected BlockPos getLimitForAxis(BlockPos blockPos, Direction.Axis axis) {
		if (blockPos == null || axis == null) return null;
		int offset = 1;
		while (validStateInsideLoop(world.getBlockState(blockPos.offset(axis, -offset)), VALID_LOOP)) {
			offset++;
			if (offset > 20) return null;
			if ((axis.equals(Direction.Axis.Y) && blockPos.getY() - offset < world.getBottomY()) || (!axis.equals(Direction.Axis.Y) && !world.getWorldBorder().contains(blockPos.offset(axis, -offset))))
				return null;
		}
		return blockPos.offset(axis, -(offset - 1));
	}

	protected int getSize(Direction.Axis axis) {
		for (int i = 1; i <= 21; i++) {
			BlockState blockState = this.world.getBlockState(this.lowerCorner.offset(axis, i));
			if (!validStateInsideLoop(blockState, VALID_LOOP)) {
				if (VALID_LOOP.contains(blockState.getBlock())) {
					return i >= 2 ? i : 0;

				}
				break;
			}
		}
		return 0;
	}

	protected boolean checkForValidLoop(int size1, int size2) {
		BlockPos checkPos = lowerCorner.mutableCopy();
		for (int i = 0; i < size1; i++) {
			if (!VALID_LOOP.contains(world.getBlockState(checkPos.offset(Direction.Axis.Z, -1)).getBlock()) || !VALID_LOOP.contains(world.getBlockState(checkPos.offset(Direction.Axis.Z, size2)).getBlock()))
				return false;
			checkPos = checkPos.offset(Direction.Axis.X, 1);
		}
		checkPos = lowerCorner.mutableCopy();
		for (int i = 0; i < size2; i++) {
			if (!VALID_LOOP.contains(world.getBlockState(checkPos.offset(Direction.Axis.X, -1)).getBlock()) || !VALID_LOOP.contains(world.getBlockState(checkPos.offset(Direction.Axis.X, size1)).getBlock()))
				return false;
			checkPos = checkPos.offset(Direction.Axis.Z, 1);
		}
		return true;
	}

	public static boolean validStateInsideLoop(BlockState blockState, HashSet<Block> foundations) {
		return blockState.isAir();
	}

	public Vec3d getEntityOffsetInLoop(BlockLocating.Rectangle arg, Entity entity, Direction.Axis portalAxis) {
		EntityDimensions entityDimensions = entity.getDimensions(entity.getPose());
		double xSize = arg.width - entityDimensions.width;
		double zSize = arg.height - entityDimensions.width;

		double deltaX = MathHelper.lerp(entity.getX(), arg.lowerLeft.getX(), arg.lowerLeft.getX() + xSize);
		double deltaY = MathHelper.lerp(entity.getY(), arg.lowerLeft.getY() - 1, arg.lowerLeft.getY() + 1);
		double deltaZ = MathHelper.lerp(entity.getZ(), arg.lowerLeft.getZ(), arg.lowerLeft.getZ() + zSize);

		return new Vec3d(deltaX, deltaY, deltaZ);
	}
}
