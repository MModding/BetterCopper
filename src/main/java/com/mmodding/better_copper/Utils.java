package com.mmodding.better_copper;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class Utils {

	public static final String modIdentifier = "better_copper";
	public static BlockPos openScreenPos;

	public static Identifier newIdentifier(String path) {
		return new Identifier(Utils.modIdentifier, path);
	}

	public static void setOpenScreenPos(BlockPos screenPos) {
		openScreenPos = screenPos;
	}

	public static BlockPos getOpenScreenPos() {
		return openScreenPos;
	}
}
