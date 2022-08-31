package com.mmodding.better_copper.magneticfield;

import com.mmodding.better_copper.init.Blocks;
import com.mmodding.better_copper.mixin.accessors.WorldRendererAccessor;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;

import java.util.Optional;

public class MagneticField extends WorldBorder {

	public MagneticField(World world, BlockPos blockPos, MinecraftClient minecraftClient) {
		Optional<LoopAreaHelper> loop = new LoopAreaHelper().getNewLoop(world, blockPos, Direction.Axis.X, Blocks.NETHERITE_COATED_GOLD_BLOCK);
		if (loop.isPresent()) {
			LoopAreaHelper loopAreaHelper = loop.get();
			if (loopAreaHelper.xSize == loopAreaHelper.zSize) {
				BlockPos center = loopAreaHelper.lowerCorner.offset(Direction.Axis.X, (loopAreaHelper.xSize - 1) / 2).offset(Direction.Axis.Z, (loopAreaHelper.zSize - 1) / 2);
				setCenter(center.getX(), center.getZ());
				setSize(loopAreaHelper.xSize);
				setDamagePerBlock(0);
				LoopAreaHelper.FIELDS.add(this);
				render(minecraftClient, LoopAreaHelper.getRenderCamera(), WorldRendererAccessor.getFORCEFIELD());
			}
		}
	}

	public void render(MinecraftClient minecraftClient, Camera camera, Identifier texture) {
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		double d = minecraftClient.options.method_38521() * 16;
		if (!(camera.getPos().x < getBoundEast() - d)
				|| !(camera.getPos().x > getBoundWest() + d)
				|| !(camera.getPos().z < getBoundSouth() - d)
				|| !(camera.getPos().z > getBoundNorth() + d)) {
			double e = 1.0 - getDistanceInsideBorder(camera.getPos().x, camera.getPos().z) / d;
			e = Math.pow(e, 4.0);
			e = MathHelper.clamp(e, 0.0, 1.0);
			double f = camera.getPos().x;
			double g = camera.getPos().z;
			double h = minecraftClient.gameRenderer.method_32796();
			RenderSystem.enableBlend();
			RenderSystem.enableDepthTest();
			RenderSystem.blendFuncSeparate(
					GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE, GlStateManager.class_4535.ONE, GlStateManager.class_4534.ZERO
			);
			RenderSystem.setShaderTexture(0, texture);
			RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
			MatrixStack matrixStack = RenderSystem.getModelViewStack();
			matrixStack.push();
			RenderSystem.applyModelViewMatrix();
			int i = getStage().getColor();
			float j = (float) (i >> 16 & 0xFF) / 255.0F;
			float k = (float) (i >> 8 & 0xFF) / 255.0F;
			float l = (float) (i & 0xFF) / 255.0F;
			RenderSystem.setShaderColor(j, k, l, (float) e);
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.polygonOffset(-3.0F, -3.0F);
			RenderSystem.enablePolygonOffset();
			RenderSystem.disableCull();
			float m = (float) (Util.getMeasuringTimeMs() % 3000L) / 3000.0F;
			float n = 0.0F;
			float o = 0.0F;
			float p = (float) (h - MathHelper.fractionalPart(camera.getPos().y));
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
			double q = Math.max(MathHelper.floor(g - d), getBoundNorth());
			double r = Math.min(MathHelper.ceil(g + d), getBoundSouth());
			if (f > getBoundEast() - d) {
				float s = 0.0F;

				for (double t = q; t < r; s += 0.5F) {
					double u = Math.min(1.0, r - t);
					float v = (float) u * 0.5F;
					bufferBuilder.vertex(getBoundEast() - f, -h, t - g).texture(m - s, m + p).next();
					bufferBuilder.vertex(getBoundEast() - f, -h, t + u - g).texture(m - (v + s), m + p).next();
					bufferBuilder.vertex(getBoundEast() - f, h, t + u - g).texture(m - (v + s), m + 0.0F).next();
					bufferBuilder.vertex(getBoundEast() - f, h, t - g).texture(m - s, m + 0.0F).next();
					++t;
				}
			}

			if (f < getBoundWest() + d) {
				float s = 0.0F;

				for (double t = q; t < r; s += 0.5F) {
					double u = Math.min(1.0, r - t);
					float v = (float) u * 0.5F;
					bufferBuilder.vertex(getBoundWest() - f, -h, t - g).texture(m + s, m + p).next();
					bufferBuilder.vertex(getBoundWest() - f, -h, t + u - g).texture(m + v + s, m + p).next();
					bufferBuilder.vertex(getBoundWest() - f, h, t + u - g).texture(m + v + s, m + 0.0F).next();
					bufferBuilder.vertex(getBoundWest() - f, h, t - g).texture(m + s, m + 0.0F).next();
					++t;
				}
			}

			q = Math.max(MathHelper.floor(f - d), getBoundWest());
			r = Math.min(MathHelper.ceil(f + d), getBoundEast());
			if (g > getBoundSouth() - d) {
				float s = 0.0F;

				for (double t = q; t < r; s += 0.5F) {
					double u = Math.min(1.0, r - t);
					float v = (float) u * 0.5F;
					bufferBuilder.vertex(t - f, -h, getBoundSouth() - g).texture(m + s, m + p).next();
					bufferBuilder.vertex(t + u - f, -h, getBoundSouth() - g).texture(m + v + s, m + p).next();
					bufferBuilder.vertex(t + u - f, h, getBoundSouth() - g).texture(m + v + s, m + 0.0F).next();
					bufferBuilder.vertex(t - f, h, getBoundSouth() - g).texture(m + s, m + 0.0F).next();
					++t;
				}
			}

			if (g < getBoundNorth() + d) {
				float s = 0.0F;

				for (double t = q; t < r; s += 0.5F) {
					double u = Math.min(1.0, r - t);
					float v = (float) u * 0.5F;
					bufferBuilder.vertex(t - f, -h, getBoundNorth() - g).texture(m - s, m + p).next();
					bufferBuilder.vertex(t + u - f, -h, getBoundNorth() - g).texture(m - (v + s), m + p).next();
					bufferBuilder.vertex(t + u - f, h, getBoundNorth() - g).texture(m - (v + s), m + 0.0F).next();
					bufferBuilder.vertex(t - f, h, getBoundNorth() - g).texture(m - s, m + 0.0F).next();
					++t;
				}
			}

			bufferBuilder.end();
			BufferRenderer.draw(bufferBuilder);
			RenderSystem.enableCull();
			RenderSystem.polygonOffset(0.0F, 0.0F);
			RenderSystem.disablePolygonOffset();
			RenderSystem.disableBlend();
			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();
			RenderSystem.depthMask(true);
		}
	}
}
