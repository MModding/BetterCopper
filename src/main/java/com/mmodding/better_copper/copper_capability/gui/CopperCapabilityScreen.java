package com.mmodding.better_copper.copper_capability.gui;

import com.google.common.collect.Maps;
import com.mmodding.better_copper.copper_capability.CopperCapability;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.AdvancementTabC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class CopperCapabilityScreen extends Screen {
	private static final Identifier WINDOW_TEXTURE = new Identifier("textures/gui/advancements/window.png");
	private static final Identifier TABS_TEXTURE = new Identifier("textures/gui/advancements/tabs.png");
	private static final Text CAPABILITIES_TEXT = Text.translatable("gui.copper_capabilities");
	private static final Text EMPTY_TEXT = Text.translatable("copper_capabilities.empty");
	private static final Text TIP_TEXT = Text.translatable("copper_capabilities.tip");
	private final Map<CopperCapability, CopperCapabilityTab> tabs = Maps.newLinkedHashMap();
	@Nullable
	private CopperCapabilityTab selectedTab;
	private boolean movingTab;

	public CopperCapabilityScreen() {
		super(ChatNarratorManager.NO_TITLE);
	}

	@Override
	protected void init() {
		this.tabs.clear();
		this.selectedTab = null;
	}

	@Override
	public void removed() {
		ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.getNetworkHandler();
		if (clientPlayNetworkHandler != null) {
			clientPlayNetworkHandler.sendPacket(AdvancementTabC2SPacket.close());
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			int i = (this.width - 252) / 2;
			int j = (this.height - 140) / 2;

			for (CopperCapabilityTab copperCapabilityTab : this.tabs.values()) {
				if (copperCapabilityTab.isClickOnTab(i, j, mouseX, mouseY)) {
					break;
				}
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.client.options.advancementsKey.matchesKey(keyCode, scanCode)) {
			this.client.setScreen(null);
			this.client.mouse.lockCursor();
			return true;
		} else {
			return super.keyPressed(keyCode, scanCode, modifiers);
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		int i = (this.width - 252) / 2;
		int j = (this.height - 140) / 2;
		this.renderBackground(matrices);
		this.drawCapabilitiesTree(matrices, mouseX, mouseY, i, j);
		this.drawWidgets(matrices, i, j);
		this.drawWidgetTooltip(matrices, mouseX, mouseY, i, j);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (button != 0) {
			this.movingTab = false;
			return false;
		} else {
			if (!this.movingTab) {
				this.movingTab = true;
			} else if (this.selectedTab != null) {
				this.selectedTab.move(deltaX, deltaY);
			}

			return true;
		}
	}

	private void drawCapabilitiesTree(MatrixStack matrices, int mouseX, int mouseY, int x, int y) {
		CopperCapabilityTab copperCapabilityTab = this.selectedTab;
		if (copperCapabilityTab == null) {
			fill(matrices, x + 9, y + 18, x + 9 + 234, y + 18 + 113, -16777216);
			int i = x + 9 + 117;
			drawCenteredText(matrices, this.textRenderer, EMPTY_TEXT, i, y + 18 + 56 - 9 / 2, -1);
			drawCenteredText(matrices, this.textRenderer, TIP_TEXT, i, y + 18 + 113 - 9, -1);
		} else {
			MatrixStack matrixStack = RenderSystem.getModelViewStack();
			matrixStack.push();
			matrixStack.translate(x + 9, y + 18, 0.0);
			RenderSystem.applyModelViewMatrix();
			copperCapabilityTab.render(matrices);
			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();
			RenderSystem.depthFunc(515);
			RenderSystem.disableDepthTest();
		}
	}

	public void drawWidgets(MatrixStack matrices, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, WINDOW_TEXTURE);
		this.drawTexture(matrices, x, y, 0, 0, 252, 140);
		if (this.tabs.size() > 1) {
			RenderSystem.setShaderTexture(0, TABS_TEXTURE);

			for (CopperCapabilityTab copperCapabilityTab : this.tabs.values()) {
				copperCapabilityTab.drawBackground(matrices, x, y, copperCapabilityTab == this.selectedTab);
			}

			RenderSystem.defaultBlendFunc();

			for (CopperCapabilityTab copperCapabilityTab : this.tabs.values()) {
				copperCapabilityTab.drawIcon(x, y, this.itemRenderer);
			}

			RenderSystem.disableBlend();
		}

		this.textRenderer.draw(matrices, CAPABILITIES_TEXT, (float) (x + 8), (float) (y + 6), 4210752);
	}

	private void drawWidgetTooltip(MatrixStack matrices, int mouseX, int mouseY, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.selectedTab != null) {
			MatrixStack matrixStack = RenderSystem.getModelViewStack();
			matrixStack.push();
			matrixStack.translate(x + 9, y + 18, 400.0);
			RenderSystem.applyModelViewMatrix();
			RenderSystem.enableDepthTest();
			this.selectedTab.drawWidgetTooltip(matrices, mouseX - x - 9, mouseY - y - 18, x, y);
			RenderSystem.disableDepthTest();
			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();
		}

		if (this.tabs.size() > 1) {
			for (CopperCapabilityTab copperCapabilityTab : this.tabs.values()) {
				if (copperCapabilityTab.isClickOnTab(x, y, mouseX, mouseY)) {
					this.renderTooltip(matrices, copperCapabilityTab.getTitle(), mouseX, mouseY);
				}
			}
		}
	}

	@Nullable
	public CopperCapabilityWidget copperCapabilityWidget(CopperCapability copperCapability) {
		CopperCapabilityTab copperCapabilityTab = this.getTab(copperCapability);
		return copperCapabilityTab == null ? null : copperCapabilityTab.getWidget(copperCapability);
	}

	@Nullable
	private CopperCapabilityTab getTab(CopperCapability copperCapability) {
		return this.tabs.get(copperCapability);
	}
}
