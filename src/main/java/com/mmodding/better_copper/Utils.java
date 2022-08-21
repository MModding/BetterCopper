package com.mmodding.better_copper;

import net.minecraft.util.Identifier;

public class Utils {

	public static final String modIdentifier = "better_copper";

	public static Identifier newIdentifier(String path) {
		return new Identifier(Utils.modIdentifier, path);
	}
}
