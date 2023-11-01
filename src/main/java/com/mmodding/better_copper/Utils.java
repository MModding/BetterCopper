package com.mmodding.better_copper;

import com.mmodding.better_copper.magneticfield.LoopAreaHelper;
import com.mmodding.better_copper.magneticfield.MagneticField;
import com.mmodding.mmodding_lib.library.utils.TextureLocation;
import net.minecraft.text.component.LiteralComponent;
import net.minecraft.text.component.TextComponent;
import net.minecraft.text.component.TranslatableComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Utils {

	public static final String modIdentifier = "better_copper";
	public static BlockPos openScreenPos;
	public static LoopAreaHelper LAH = new LoopAreaHelper();
	public static List<MagneticField> FIELDS = new ArrayList<>();

	public static Identifier newIdentifier(String path) {
		return new Identifier(Utils.modIdentifier, path);
	}

	public static TextureLocation newTextureLocation(String path) {
		return new TextureLocation(Utils.modIdentifier, path);
	}

	public static void setOpenScreenPos(BlockPos screenPos) {
		openScreenPos = screenPos;
	}

	public static BlockPos getOpenScreenPos() {
		return openScreenPos;
	}

	public static TextComponent literal(String str) {
		return new LiteralComponent(str);
	}

	public static TextComponent translatable(String key) {
		return new TranslatableComponent(modIdentifier + "." + key);
	}
}
