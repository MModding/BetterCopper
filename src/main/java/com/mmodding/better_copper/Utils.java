package com.mmodding.better_copper;

import com.mmodding.better_copper.magneticfield.LoopAreaHelper;
import com.mmodding.better_copper.magneticfield.MagneticField;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
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

	public static void setOpenScreenPos(BlockPos screenPos) {
		openScreenPos = screenPos;
	}

	public static BlockPos getOpenScreenPos() {
		return openScreenPos;
	}

	public static MutableText literal(String str) {
		return new LiteralText(str);
	}

	public static MutableText translatable(String key) {
		return new TranslatableText(modIdentifier + "." + key);
	}
}
