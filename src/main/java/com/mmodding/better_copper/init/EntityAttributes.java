package com.mmodding.better_copper.init;

import com.mmodding.better_copper.mixin.accessors.EntityAttributesAccessor;
import com.mmodding.mmodding_lib.library.initializers.ElementsInitializer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;

public class EntityAttributes implements ElementsInitializer {

	public static final EntityAttribute GENERIC_ARMOR_CHARGE = EntityAttributesAccessor.invokeRegister(
		"generic.armor_charge", new ClampedEntityAttribute("attribute.name.generic.armor_charge", 0.0, 0.0, Integer.MAX_VALUE).setTracked(true)
	);

	@Override
	public void register() {}
}
