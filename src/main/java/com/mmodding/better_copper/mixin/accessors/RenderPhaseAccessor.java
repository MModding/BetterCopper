package com.mmodding.better_copper.mixin.accessors;

import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPhase.class)
public interface RenderPhaseAccessor {

	@Accessor
	static RenderPhase.Shader getARMOR_GLINT_SHADER() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Shader getARMOR_ENTITY_GLINT_SHADER() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Shader getTRANSLUCENT_GLINT_SHADER() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Shader getGLINT_SHADER() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Shader getDIRECT_GLINT_SHADER() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Shader getENTITY_GLINT_SHADER() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Shader getDIRECT_ENTITY_GLINT_SHADER() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.WriteMaskState getCOLOR_MASK() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Cull getDISABLE_CULLING() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.DepthTest getEQUAL_DEPTH_TEST() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Transparency getGLINT_TRANSPARENCY() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Texturing getGLINT_TEXTURING() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Texturing getENTITY_GLINT_TEXTURING() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Layering getVIEW_OFFSET_Z_LAYERING() {
		throw new AssertionError();
	}

	@Accessor
	static RenderPhase.Target getITEM_TARGET() {
		throw new AssertionError();
	}
}
