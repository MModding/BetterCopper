package com.mmodding.better_copper.copper_capabilities.client.gui;

import com.google.common.collect.Maps;
import com.mmodding.better_copper.BetterCopper;
import com.mmodding.better_copper.BetterCopperPackets;
import com.mmodding.better_copper.copper_capabilities.CopperCapability;
import com.mmodding.better_copper.copper_capabilities.client.ClientCopperCapabilitiesManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import java.util.Map;

@ClientOnly
public class CopperCapabilityScreen extends Screen implements ClientCopperCapabilitiesManager.Listener {

	private static final Identifier WINDOW_TEXTURE = BetterCopper.createId("textures/gui/copper_capabilities/window.png");
	private static final Identifier TABS_TEXTURE = new Identifier("textures/gui/advancements/tabs.png");
	private static final Text CAPABILITIES_TEXT = Text.translatable("gui.copper_capabilities");
	private static final Text EMPTY_TEXT = Text.translatable("copper_capabilities.better_copper.empty");
	private static final Text TIP_TEXT = Text.translatable("copper_capabilities.better_copper.tip");

	private final ClientCopperCapabilitiesManager copperCapabilitiesHandler;
	private final Map<CopperCapability, CopperCapabilityTab> tabs = Maps.newLinkedHashMap();

	private @Nullable CopperCapabilityTab selectedTab;
	private boolean movingTab;

	public CopperCapabilityScreen(ClientCopperCapabilitiesManager copperCapabilitiesHandler) {
		super(ChatNarratorManager.NO_TITLE);
		this.copperCapabilitiesHandler = copperCapabilitiesHandler;
	}

	@Override
	protected void init() {
		this.tabs.clear();
		this.selectedTab = null;
		this.copperCapabilitiesHandler.setListener(this);
		if (this.selectedTab == null && !this.tabs.isEmpty()) {
			this.copperCapabilitiesHandler.selectTab(this.tabs.values().iterator().next().getRoot(), true);
		} else {
			this.copperCapabilitiesHandler.selectTab(this.selectedTab == null ? null : this.selectedTab.getRoot(), true);
		}
	}

	@Override
	public void removed() {
		this.copperCapabilitiesHandler.setListener(null);
		assert this.client != null;
		ClientPlayNetworking.send(BetterCopperPackets.C2S_CLOSE_COPPER_CAPABILITIES_TAB, PacketByteBufs.empty());
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			int i = (this.width - 252) / 2;
			int j = (this.height - 140) / 2;

			for (CopperCapabilityTab copperCapabilityTab : this.tabs.values()) {
				if (copperCapabilityTab.isClickOnTab(i, j, mouseX, mouseY)) {
					this.copperCapabilitiesHandler.selectTab(copperCapabilityTab.getRoot(), true);
					break;
				}
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		assert this.client != null;
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

	@Override
	public void onRootAdded(CopperCapability root) {
		CopperCapabilityTab copperCapabilityTab = CopperCapabilityTab.create(this.client, this, this.tabs.size(), root);
		if (copperCapabilityTab != null) {
			this.tabs.put(root, copperCapabilityTab);
		}
	}

	@Override
	public void onRootRemoved(CopperCapability root) {
	}

	@Override
	public void onDependentAdded(CopperCapability dependent) {
		CopperCapabilityTab copperCapabilityTab = this.getTab(dependent);
		if (copperCapabilityTab != null) {
			copperCapabilityTab.addCopperCapability(dependent);
		}
	}

	@Override
	public void onDependentRemoved(CopperCapability dependent) {
	}

	@Override
	public void selectTab(@Nullable CopperCapability copperCapability) {
		this.selectedTab = this.tabs.get(copperCapability);
	}

	@Override
	public void onClear() {
		this.tabs.clear();
		this.selectedTab = null;
	}

	@Nullable
	public CopperCapabilityWidget getCopperCapabilityWidget(CopperCapability copperCapability) {
		CopperCapabilityTab copperCapabilityTab = this.getTab(copperCapability);
		return copperCapabilityTab == null ? null : copperCapabilityTab.getWidget(copperCapability);
	}

	@Nullable
	private CopperCapabilityTab getTab(CopperCapability copperCapability) {
		while (copperCapability.getParent() != null) {
			copperCapability = copperCapability.getParent();
		}
		return this.tabs.get(copperCapability);
	}
}
