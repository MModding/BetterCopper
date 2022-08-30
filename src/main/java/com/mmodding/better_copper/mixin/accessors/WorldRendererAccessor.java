package com.mmodding.better_copper.mixin.accessors;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {

	@Accessor
	public static Identifier getFORCEFIELD() {
		throw new AssertionError();
	}
}
