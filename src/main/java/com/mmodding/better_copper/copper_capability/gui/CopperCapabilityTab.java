package com.mmodding.better_copper.copper_capability.gui;

import com.google.common.collect.Maps;
import com.mmodding.better_copper.copper_capability.CopperCapability;
import com.mmodding.better_copper.copper_capability.CopperCapabilityDisplay;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.Map;

@ClientOnly
public class CopperCapabilityTab extends DrawableHelper {

	private final MinecraftClient client;
	private final CopperCapabilityScreen screen;
	private final CopperCapabilityTabType type;
	private final int index;
	private final CopperCapability root;
	private final CopperCapabilityDisplay display;
	private final ItemStack icon;
	private final Text title;
	private final CopperCapabilityWidget rootWidget;
	private final Map<CopperCapability, CopperCapabilityWidget> widgets = Maps.newLinkedHashMap();
	private double originX;
	private double originY;
	private int minPanX = Integer.MAX_VALUE;
	private int minPanY = Integer.MAX_VALUE;
	private int maxPanX = Integer.MIN_VALUE;
	private int maxPanY = Integer.MIN_VALUE;
	private float alpha;
	private boolean initialized;

	public CopperCapabilityTab(MinecraftClient client, CopperCapabilityScreen screen, CopperCapabilityTabType type, int index, CopperCapability root, CopperCapabilityDisplay display) {
		this.client = client;
		this.screen = screen;
		this.type = type;
		this.index = index;
		this.root = root;
		this.display = display;
		this.icon = display.getIcon();
		this.title = display.getTitle();
		this.rootWidget = new CopperCapabilityWidget(this, client, root, display);
		this.addWidget(this.rootWidget, root);
	}

	public CopperCapabilityTabType getType() {
		return this.type;
	}

	public int getIndex() {
		return this.index;
	}

	public CopperCapability getRoot() {
		return this.root;
	}

	public Text getTitle() {
		return this.title;
	}

	public CopperCapabilityDisplay getDisplay() {
		return this.display;
	}

	public void drawBackground(MatrixStack matrices, int x, int y, boolean selected) {
		this.type.drawBackground(matrices, this, x, y, selected, this.index);
	}

	public void drawIcon(int x, int y, ItemRenderer itemRenderer) {
		this.type.drawIcon(x, y, this.index, itemRenderer, this.icon);
	}

	public void render(MatrixStack matrices) {
		if (!this.initialized) {
			this.originX = 117 - (double) (this.maxPanX + this.minPanX) / 2;
			this.originY = 56 - (double) (this.maxPanY + this.minPanY) / 2;
			this.initialized = true;
		}

		matrices.push();
		matrices.translate(0.0, 0.0, 950.0);
		RenderSystem.enableDepthTest();
		RenderSystem.colorMask(false, false, false, false);
		fill(matrices, 4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		matrices.translate(0.0, 0.0, -950.0);
		RenderSystem.depthFunc(518);
		fill(matrices, 234, 113, 0, 0, -16777216);
		RenderSystem.depthFunc(515);
		Identifier identifier = this.display.getBackground();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		if (identifier != null) {
			RenderSystem.setShaderTexture(0, identifier);
		} else {
			RenderSystem.setShaderTexture(0, TextureManager.MISSING_IDENTIFIER);
		}

		int i = MathHelper.floor(this.originX);
		int j = MathHelper.floor(this.originY);
		int k = i % 16;
		int l = j % 16;

		for (int m = -1; m <= 15; ++m) {
			for (int n = -1; n <= 8; ++n) {
				drawTexture(matrices, k + 16 * m, l + 16 * n, 0.0F, 0.0F, 16, 16, 16, 16);
			}
		}

		this.rootWidget.renderLines(matrices, i, j, true);
		this.rootWidget.renderLines(matrices, i, j, false);
		this.rootWidget.renderWidgets(matrices, i, j);
		RenderSystem.depthFunc(518);
		matrices.translate(0.0, 0.0, -950.0);
		RenderSystem.colorMask(false, false, false, false);
		fill(matrices, 4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.depthFunc(515);
		matrices.pop();
	}

	public void drawWidgetTooltip(MatrixStack matrices, int mouseX, int mouseY, int x, int y) {
		matrices.push();
		matrices.translate(0.0, 0.0, -200.0);
		fill(matrices, 0, 0, 234, 113, MathHelper.floor(this.alpha * 255.0F) << 24);
		boolean bl = false;
		int i = MathHelper.floor(this.originX);
		int j = MathHelper.floor(this.originY);
		if (mouseX > 0 && mouseX < 234 && mouseY > 0 && mouseY < 113) {
			for (CopperCapabilityWidget copperCapabilityWidget : this.widgets.values()) {
				if (copperCapabilityWidget.shouldRender(i, j, mouseX, mouseY)) {
					bl = true;
					copperCapabilityWidget.drawTooltip(matrices, i, j, this.alpha, x, y);
					break;
				}
			}
		}

		matrices.pop();
		if (bl) {
			this.alpha = MathHelper.clamp(this.alpha + 0.02F, 0.0F, 0.3F);
		} else {
			this.alpha = MathHelper.clamp(this.alpha - 0.04F, 0.0F, 1.0F);
		}
	}

	public boolean isClickOnTab(int screenX, int screenY, double mouseX, double mouseY) {
		return this.type.isClickOnTab(screenX, screenY, this.index, mouseX, mouseY);
	}

	@Nullable
	public static CopperCapabilityTab create(MinecraftClient minecraft, CopperCapabilityScreen screen, int index, CopperCapability root) {
		if (root.getDisplay() == null) {
			return null;
		} else {
			for (CopperCapabilityTabType copperCapabilityTabType : CopperCapabilityTabType.values()) {
				if (index < copperCapabilityTabType.getTabCount()) {
					return new CopperCapabilityTab(minecraft, screen, copperCapabilityTabType, index, root, root.getDisplay());
				}

				index -= copperCapabilityTabType.getTabCount();
			}

			return null;
		}
	}

	public void move(double offsetX, double offsetY) {
		if (this.maxPanX - this.minPanX > 234) {
			this.originX = MathHelper.clamp(this.originX + offsetX, (double) (-(this.maxPanX - 234)), 0.0);
		}

		if (this.maxPanY - this.minPanY > 113) {
			this.originY = MathHelper.clamp(this.originY + offsetY, (double) (-(this.maxPanY - 113)), 0.0);
		}
	}

	public void addCopperCapability(CopperCapability copperCapability) {
		if (copperCapability.getDisplay() != null) {
			CopperCapabilityWidget copperCapabilityWidget = new CopperCapabilityWidget(this, this.client, copperCapability, copperCapability.getDisplay());
			this.addWidget(copperCapabilityWidget, copperCapability);
		}
	}

	private void addWidget(CopperCapabilityWidget widget, CopperCapability copperCapability) {
		this.widgets.put(copperCapability, widget);
		int i = widget.getX();
		int j = i + 28;
		int k = widget.getY();
		int l = k + 27;
		this.minPanX = Math.min(this.minPanX, i);
		this.maxPanX = Math.max(this.maxPanX, j);
		this.minPanY = Math.min(this.minPanY, k);
		this.maxPanY = Math.max(this.maxPanY, l);

		for (CopperCapabilityWidget copperCapabilityWidget : this.widgets.values()) {
			copperCapabilityWidget.addToTree();
		}
	}

	@Nullable
	public CopperCapabilityWidget getWidget(CopperCapability copperCapability) {
		return this.widgets.get(copperCapability);
	}

	public CopperCapabilityScreen getScreen() {
		return this.screen;
	}
}
