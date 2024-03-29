package com.mmodding.better_copper.copper_capabilities.client.gui;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public enum CopperCapabilityTabType {
	ABOVE(0, 0, 28, 32, 8),
	BELOW(84, 0, 28, 32, 8),
	LEFT(0, 64, 32, 28, 5),
	RIGHT(96, 64, 32, 28, 5);

	private final int u;
	private final int v;
	private final int width;
	private final int height;
	private final int tabCount;

	CopperCapabilityTabType(int u, int v, int width, int height, int tabCount) {
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
		this.tabCount = tabCount;
	}

	public int getTabCount() {
		return this.tabCount;
	}

	public void drawBackground(MatrixStack matrices, DrawableHelper tab, int x, int y, boolean selected, int index) {
		int i = this.u;
		if (index > 0) {
			i += this.width;
		}

		if (index == this.tabCount - 1) {
			i += this.width;
		}

		int j = selected ? this.v + this.height : this.v;
		tab.drawTexture(matrices, x + this.getTabX(index), y + this.getTabY(index), i, j, this.width, this.height);
	}

	public void drawIcon(int x, int y, int index, ItemRenderer itemRenderer, ItemStack icon) {
		int i = x + this.getTabX(index);
		int j = y + this.getTabY(index);
		switch (this) {
			case ABOVE:
				i += 6;
				j += 9;
				break;
			case BELOW:
				i += 6;
				j += 6;
				break;
			case LEFT:
				i += 10;
				j += 5;
				break;
			case RIGHT:
				i += 6;
				j += 5;
		}

		itemRenderer.renderInGui(icon, i, j);
	}

	public int getTabX(int index) {
        return switch (this) {
            case ABOVE, BELOW -> (this.width + 4) * index;
            case LEFT -> -this.width + 4;
            case RIGHT -> 248;
        };
	}

	public int getTabY(int index) {
        return switch (this) {
            case ABOVE -> -this.height + 4;
            case BELOW -> 136;
            case LEFT, RIGHT -> this.height * index;
        };
	}

	public boolean isClickOnTab(int screenX, int screenY, int index, double mouseX, double mouseY) {
		int i = screenX + this.getTabX(index);
		int j = screenY + this.getTabY(index);
		return mouseX > (double) i && mouseX < (double) (i + this.width) && mouseY > (double) j && mouseY < (double) (j + this.height);
	}
}
