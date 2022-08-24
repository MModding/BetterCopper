package com.mmodding.better_copper.init;

import com.mmodding.better_copper.Utils;
import net.minecraft.util.Identifier;

public enum SpecialTextures {

	BLANK("blank.png"),
	CHECKERED("checkerboard.png"),
	THIN_CHECKERED("thin_checkerboard.png"),
	CUTOUT_CHECKERED("cutout_checkerboard.png"),
	HIGHLIGHT_CHECKERED("highlighted_checkerboard.png"),
	SELECTION("selection.png");

	public static final String ASSET_PATH = "textures/special/";
	private final Identifier location;

	SpecialTextures(String filename) {
		location = Utils.newIdentifier(ASSET_PATH + filename);
	}

	public Identifier getLocation() {
		return location;
	}
}
