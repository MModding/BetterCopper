package com.mmodding.better_copper.init;

import com.mmodding.better_copper.Utils;
import net.minecraft.util.Identifier;

public enum SpecialTextures {

	BLANK("blank.png");

	public static final String ASSET_PATH = "textures/";
	private final Identifier location;

	SpecialTextures(String filename) {
		location = Utils.newIdentifier(ASSET_PATH + filename);
	}

	public Identifier getLocation() {
		return location;
	}
}
