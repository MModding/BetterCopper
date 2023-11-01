package com.mmodding.better_copper.client.init;

import com.mmodding.better_copper.Utils;
import com.mmodding.mmodding_lib.library.glint.client.GlintPack;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;

public class GlintPacks implements ClientElementsInitializer {

	@Override
	public void registerClient() {
		GlintPack.create(Utils.newTextureLocation("glint_pack/copper_clint")).register(Utils.newIdentifier("copper_clint"));
	}
}
