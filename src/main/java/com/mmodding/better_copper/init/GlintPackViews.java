package com.mmodding.better_copper.init;

import com.mmodding.better_copper.BetterCopper;
import com.mmodding.better_copper.charge.Charge;
import com.mmodding.mmodding_lib.library.glint.DynamicGlintPackView;
import com.mmodding.mmodding_lib.library.glint.GlintPackView;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import com.mmodding.mmodding_lib.library.utils.RegistrationUtils;

public class GlintPackViews implements ElementsInitializer {

	public static final GlintPackView COPPER_CLINT = new DynamicGlintPackView(stack -> Charge.isCharged(stack) ? BetterCopper.createId("copper_clint") : BetterCopper.createId("null"));

	@Override
	public void register() {
		RegistrationUtils.registerGlintPackView(BetterCopper.createId("copper_clint"), COPPER_CLINT);
	}
}
