package com.mmodding.better_copper.init;

import com.mmodding.better_copper.BetterCopper;
import com.mmodding.better_copper.items.ChargedArmorItem;
import com.mmodding.better_copper.items.ChargedSwordItem;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import com.mmodding.mmodding_lib.library.items.settings.AdvancedItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemGroup;

public class Items implements ElementsInitializer {

	public static final ChargedArmorItem COPPER_HELMET = new ChargedArmorItem(EquipmentSlot.HEAD, new AdvancedItemSettings().group(ItemGroup.COMBAT).glint().glintPack(GlintPackViews.COPPER_CLINT));
	public static final ChargedArmorItem COPPER_CHESTPLATE = new ChargedArmorItem(EquipmentSlot.CHEST, new AdvancedItemSettings().group(ItemGroup.COMBAT).glint().glintPack(GlintPackViews.COPPER_CLINT));
	public static final ChargedArmorItem COPPER_LEGGINGS = new ChargedArmorItem(EquipmentSlot.LEGS, new AdvancedItemSettings().group(ItemGroup.COMBAT).glint().glintPack(GlintPackViews.COPPER_CLINT));
	public static final ChargedArmorItem COPPER_BOOTS = new ChargedArmorItem(EquipmentSlot.FEET, new AdvancedItemSettings().group(ItemGroup.COMBAT).glint().glintPack(GlintPackViews.COPPER_CLINT));
	public static final ChargedSwordItem COPPER_SWORD = new ChargedSwordItem(new AdvancedItemSettings().group(ItemGroup.COMBAT).glint().glintPack(GlintPackViews.COPPER_CLINT));

	@Override
	public void register() {
		COPPER_HELMET.register(BetterCopper.createId("copper_helmet"));
		COPPER_CHESTPLATE.register(BetterCopper.createId("copper_chestplate"));
		COPPER_LEGGINGS.register(BetterCopper.createId("copper_leggings"));
		COPPER_BOOTS.register(BetterCopper.createId("copper_boots"));
		COPPER_SWORD.register(BetterCopper.createId("copper_sword"));
	}
}
