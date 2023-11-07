package com.mmodding.better_copper.init;

import com.mmodding.better_copper.BetterCopper;
import com.mmodding.better_copper.items.ChargedArmorItem;
import com.mmodding.better_copper.items.ChargedSwordItem;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemGroup;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class Items implements ElementsInitializer {

	public static final ChargedArmorItem COPPER_HELMET = new ChargedArmorItem(EquipmentSlot.HEAD, new QuiltItemSettings().group(ItemGroup.COMBAT));
	public static final ChargedArmorItem COPPER_CHESTPLATE = new ChargedArmorItem(EquipmentSlot.CHEST, new QuiltItemSettings().group(ItemGroup.COMBAT));
	public static final ChargedArmorItem COPPER_LEGGINGS = new ChargedArmorItem(EquipmentSlot.LEGS, new QuiltItemSettings().group(ItemGroup.COMBAT));
	public static final ChargedArmorItem COPPER_BOOTS = new ChargedArmorItem(EquipmentSlot.FEET, new QuiltItemSettings().group(ItemGroup.COMBAT));
	public static final ChargedSwordItem COPPER_SWORD = new ChargedSwordItem(new QuiltItemSettings().group(ItemGroup.COMBAT));

	@Override
	public void register() {
		COPPER_HELMET.register(BetterCopper.createId("copper_helmet"));
		COPPER_CHESTPLATE.register(BetterCopper.createId("copper_chestplate"));
		COPPER_LEGGINGS.register(BetterCopper.createId("copper_leggings"));
		COPPER_BOOTS.register(BetterCopper.createId("copper_boots"));
		COPPER_SWORD.register(BetterCopper.createId("copper_sword"));
	}
}
