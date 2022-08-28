package com.mmodding.better_copper.mixin;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityAttributes.class)
public interface EntityAttributesAccessor {

	@Invoker("register")
	static EntityAttribute invokeRegister(String id, EntityAttribute attribute) {
		throw new AssertionError();
	}
}
