package com.mmodding.better_copper.client.init;

import com.mmodding.mmodding_lib.library.initializers.ClientElementsInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;
import org.lwjgl.glfw.GLFW;

public class BetterCopperKeyBinds implements ClientElementsInitializer {

	public static final KeyBind COPPER_CAPABILITY_KEY = new KeyBind("key.copper_capabilities", GLFW.GLFW_KEY_B, KeyBind.GAMEPLAY_CATEGORY);

	@Override
	public void registerClient() {
		KeyBindingHelper.registerKeyBinding(COPPER_CAPABILITY_KEY);
	}
}
