package com.mmodding.better_copper.init;

import com.mmodding.better_copper.Utils;
import com.mmodding.better_copper.items.ChargedArmorItem;
import com.mmodding.better_copper.items.ChargedSwordItem;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import com.mmodding.mmodding_lib.library.utils.RegistrationUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class Items implements ElementsInitializer {

	public static final Item COPPER_HELMET = new ChargedArmorItem(EquipmentSlot.HEAD, new QuiltItemSettings().group(ItemGroup.COMBAT));
	public static final Item COPPER_CHESTPLATE = new ChargedArmorItem(EquipmentSlot.CHEST, new QuiltItemSettings().group(ItemGroup.COMBAT));
	public static final Item COPPER_LEGGINGS = new ChargedArmorItem(EquipmentSlot.LEGS, new QuiltItemSettings().group(ItemGroup.COMBAT));
	public static final Item COPPER_BOOTS = new ChargedArmorItem(EquipmentSlot.FEET, new QuiltItemSettings().group(ItemGroup.COMBAT));
	public static final Item COPPER_SWORD = new ChargedSwordItem(new QuiltItemSettings().group(ItemGroup.COMBAT));

	@Override
	public void register() {
		RegistrationUtils.registerItem(Utils.newIdentifier("copper_helmet"), Items.COPPER_HELMET);
		RegistrationUtils.registerItem(Utils.newIdentifier("copper_chestplate"), Items.COPPER_CHESTPLATE);
		RegistrationUtils.registerItem(Utils.newIdentifier("copper_leggings"), Items.COPPER_LEGGINGS);
		RegistrationUtils.registerItem(Utils.newIdentifier("copper_boots"), Items.COPPER_BOOTS);
		RegistrationUtils.registerItem(Utils.newIdentifier("copper_sword"), Items.COPPER_SWORD);
	}
}
