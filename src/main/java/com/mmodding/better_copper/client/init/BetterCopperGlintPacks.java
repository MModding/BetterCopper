package com.mmodding.better_copper.client.init;

import com.mmodding.better_copper.BetterCopper;
import com.mmodding.mmodding_lib.library.glint.client.GlintPack;
import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;

public class BetterCopperGlintPacks implements ClientElementsInitializer {

	@Override
	public void registerClient() {
		GlintPack.create(BetterCopper.createTextureLocation("glint_pack/copper_clint")).register(BetterCopper.createId("copper_clint"));
	}
}
