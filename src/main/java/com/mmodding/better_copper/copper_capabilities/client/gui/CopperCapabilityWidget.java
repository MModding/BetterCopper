package com.mmodding.better_copper.copper_capabilities.client.gui;

import com.mmodding.better_copper.copper_capabilities.CopperCapability;
import com.mmodding.better_copper.copper_capabilities.CopperCapabilityDisplay;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.ArrayList;
import java.util.List;

@ClientOnly
public class CopperCapabilityWidget extends DrawableHelper {

	private static final Identifier WIDGETS_TEXTURE = new Identifier("textures/gui/advancements/widgets.png");
	private static final int[] SPLIT_OFFSET_CANDIDATES = new int[]{0, 10, -10, 25, -25};

	private final CopperCapabilityTab tab;
	private final CopperCapability copperCapability;
	private final CopperCapabilityDisplay display;
	private final OrderedText title;
	private final int width;
	private final List<OrderedText> description;
	private final MinecraftClient client;
	private final List<CopperCapabilityWidget> children = new ArrayList<>();

	private @Nullable CopperCapabilityWidget parent;
	private @Nullable AdvancementProgress progress;
	private final int x;
	private final int y;

	public CopperCapabilityWidget(CopperCapabilityTab tab, MinecraftClient client, CopperCapability copperCapability, CopperCapabilityDisplay display) {
		this.tab = tab;
		this.copperCapability = copperCapability;
		this.display = display;
		this.client = client;
		this.title = Language.getInstance().reorder(client.textRenderer.trimToWidth(display.getTitle(), 163));
		this.x = MathHelper.floor(display.getX() * 28.0F);
		this.y = MathHelper.floor(display.getY() * 27.0F);
		int l = 29 + client.textRenderer.getWidth(this.title);
		this.description = Language.getInstance()
			.reorder(this.wrapDescription(Texts.setStyleIfAbsent(display.getDescription().copy(), Style.EMPTY.withColor(display.getFrame().getTitleFormat())), l));

		for (OrderedText orderedText : this.description) {
			l = Math.max(l, client.textRenderer.getWidth(orderedText));
		}

		this.width = l + 3 + 5;
	}

	private static float getMaxWidth(TextHandler textHandler, List<StringVisitable> lines) {
		return (float) lines.stream().mapToDouble(textHandler::getWidth).max().orElse(0.0);
	}

	private List<StringVisitable> wrapDescription(Text text, int width) {
		TextHandler textHandler = this.client.textRenderer.getTextHandler();
		List<StringVisitable> list = null;
		float f = Float.MAX_VALUE;

		for (int i : SPLIT_OFFSET_CANDIDATES) {
			List<StringVisitable> list2 = textHandler.wrapLines(text, width - i, Style.EMPTY);
			float g = Math.abs(getMaxWidth(textHandler, list2) - (float) width);
			if (g <= 10.0F) {
				return list2;
			}

			if (g < f) {
				f = g;
				list = list2;
			}
		}

		return list;
	}

	@Nullable
	private CopperCapabilityWidget getParent(CopperCapability copperCapability) {
		do {
			copperCapability = copperCapability.getParent();
		} while (copperCapability != null && copperCapability.getDisplay() == null);

		return copperCapability != null && copperCapability.getDisplay() != null ? this.tab.getWidget(copperCapability) : null;
	}

	public void renderLines(MatrixStack matrices, int x, int y, boolean border) {
		if (this.parent != null) {
			int i = x + this.parent.x + 13;
			int j = x + this.parent.x + 26 + 4;
			int k = y + this.parent.y + 13;
			int l = x + this.x + 13;
			int m = y + this.y + 13;
			int n = border ? -16777216 : -1;
			if (border) {
				this.drawHorizontalLine(matrices, j, i, k - 1, n);
				this.drawHorizontalLine(matrices, j + 1, i, k, n);
				this.drawHorizontalLine(matrices, j, i, k + 1, n);
				this.drawHorizontalLine(matrices, l, j - 1, m - 1, n);
				this.drawHorizontalLine(matrices, l, j - 1, m, n);
				this.drawHorizontalLine(matrices, l, j - 1, m + 1, n);
				this.drawVerticalLine(matrices, j - 1, m, k, n);
				this.drawVerticalLine(matrices, j + 1, m, k, n);
			} else {
				this.drawHorizontalLine(matrices, j, i, k, n);
				this.drawHorizontalLine(matrices, l, j, m, n);
				this.drawVerticalLine(matrices, j, m, k, n);
			}
		}

		for (CopperCapabilityWidget copperCapabilityWidget : this.children) {
			copperCapabilityWidget.renderLines(matrices, x, y, border);
		}
	}

	public void renderWidgets(MatrixStack matrices, int x, int y) {
		// if (this.progress != null && this.progress.isDone()) {
		// float f = this.progress == null ? 0.0F : this.progress.getProgressBarPercentage();
		CopperCapabilityStatus copperCapabilityStatus;
		// if (f >= 1.0F) {
		// 	copperCapabilityStatus = CopperCapabilityStatus.CLEARED;
		// } else {
		copperCapabilityStatus = CopperCapabilityStatus.OXIDIZED;
		// }

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
		this.drawTexture(matrices, x + this.x + 3, y + this.y, this.display.getFrame().getTextureV(), 128 + copperCapabilityStatus.getSpriteIndex() * 26, 26, 26);
		this.client.getItemRenderer().renderInGui(this.display.getIcon(), x + this.x + 8, y + this.y + 5);
		// }

		for (CopperCapabilityWidget copperCapabilityWidget : this.children) {
			copperCapabilityWidget.renderWidgets(matrices, x, y);
		}
	}

	public int getWidth() {
		return this.width;
	}

	public void setProgress(AdvancementProgress progress) {
		this.progress = progress;
	}

	public void addChild(CopperCapabilityWidget widget) {
		this.children.add(widget);
	}

	public void drawTooltip(MatrixStack matrices, int originX, int originY, float alpha, int x, int y) {
		boolean bl = x + originX + this.x + this.width + 26 >= this.tab.getScreen().width;
		String string = this.progress == null ? null : this.progress.getProgressBarFraction();
		int i = string == null ? 0 : this.client.textRenderer.getWidth(string);
		boolean bl2 = 113 - originY - this.y - 26 <= 6 + this.description.size() * 9;
		float f = this.progress == null ? 0.0F : this.progress.getProgressBarPercentage();
		int j = MathHelper.floor(f * (float) this.width);
		CopperCapabilityStatus copperCapabilityStatus;
		CopperCapabilityStatus copperCapabilityStatus2;
		CopperCapabilityStatus copperCapabilityStatus3;
		if (f >= 1.0F) {
			j = this.width / 2;
			copperCapabilityStatus = CopperCapabilityStatus.CLEARED;
			copperCapabilityStatus2 = CopperCapabilityStatus.CLEARED;
			copperCapabilityStatus3 = CopperCapabilityStatus.CLEARED;
		} else if (j < 2) {
			j = this.width / 2;
			copperCapabilityStatus = CopperCapabilityStatus.OXIDIZED;
			copperCapabilityStatus2 = CopperCapabilityStatus.OXIDIZED;
			copperCapabilityStatus3 = CopperCapabilityStatus.OXIDIZED;
		} else if (j > this.width - 2) {
			j = this.width / 2;
			copperCapabilityStatus = CopperCapabilityStatus.CLEARED;
			copperCapabilityStatus2 = CopperCapabilityStatus.CLEARED;
			copperCapabilityStatus3 = CopperCapabilityStatus.OXIDIZED;
		} else {
			copperCapabilityStatus = CopperCapabilityStatus.CLEARED;
			copperCapabilityStatus2 = CopperCapabilityStatus.OXIDIZED;
			copperCapabilityStatus3 = CopperCapabilityStatus.OXIDIZED;
		}

		int k = this.width - j;
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		int l = originY + this.y;
		int m;
		if (bl) {
			m = originX + this.x - this.width + 26 + 6;
		} else {
			m = originX + this.x;
		}

		int n = 32 + this.description.size() * 9;
		if (!this.description.isEmpty()) {
			if (bl2) {
				this.renderDescriptionBackground(matrices, m, l + 26 - n, this.width, n, 10, 200, 26, 0, 52);
			} else {
				this.renderDescriptionBackground(matrices, m, l, this.width, n, 10, 200, 26, 0, 52);
			}
		}

		this.drawTexture(matrices, m, l, 0, copperCapabilityStatus.getSpriteIndex() * 26, j, 26);
		this.drawTexture(matrices, m + j, l, 200 - k, copperCapabilityStatus2.getSpriteIndex() * 26, k, 26);
		this.drawTexture(
			matrices, originX + this.x + 3, originY + this.y, this.display.getFrame().getTextureV(), 128 + copperCapabilityStatus3.getSpriteIndex() * 26, 26, 26
		);
		if (bl) {
			this.client.textRenderer.drawWithShadow(matrices, this.title, (float) (m + 5), (float) (originY + this.y + 9), -1);
			if (string != null) {
				this.client.textRenderer.drawWithShadow(matrices, string, (float) (originX + this.x - i), (float) (originY + this.y + 9), -1);
			}
		} else {
			this.client.textRenderer.drawWithShadow(matrices, this.title, (float) (originX + this.x + 32), (float) (originY + this.y + 9), -1);
			if (string != null) {
				this.client.textRenderer.drawWithShadow(matrices, string, (float) (originX + this.x + this.width - i - 5), (float) (originY + this.y + 9), -1);
			}
		}

		if (bl2) {
			for (int o = 0; o < this.description.size(); ++o) {
				this.client.textRenderer.draw(matrices, this.description.get(o), (float) (m + 5), (float) (l + 26 - n + 7 + o * 9), -5592406);
			}
		} else {
			for (int o = 0; o < this.description.size(); ++o) {
				this.client.textRenderer.draw(matrices, this.description.get(o), (float) (m + 5), (float) (originY + this.y + 9 + 17 + o * 9), -5592406);
			}
		}

		this.client.getItemRenderer().renderInGui(this.display.getIcon(), originX + this.x + 8, originY + this.y + 5);
	}

	/**
	 * Renders the description background.
	 *
	 * @implNote This splits the area into 9 parts (4 corners, 4 edges and 1
	 * central box) and draws each of them.
	 */
	protected void renderDescriptionBackground(MatrixStack matrices, int x, int y, int width, int height, int cornerSize, int textureWidth, int textureHeight, int u, int v) {
		this.drawTexture(matrices, x, y, u, v, cornerSize, cornerSize);
		this.drawTextureRepeatedly(
			matrices, x + cornerSize, y, width - cornerSize - cornerSize, cornerSize, u + cornerSize, v, textureWidth - cornerSize - cornerSize, textureHeight
		);
		this.drawTexture(matrices, x + width - cornerSize, y, u + textureWidth - cornerSize, v, cornerSize, cornerSize);
		this.drawTexture(matrices, x, y + height - cornerSize, u, v + textureHeight - cornerSize, cornerSize, cornerSize);
		this.drawTextureRepeatedly(
			matrices,
			x + cornerSize,
			y + height - cornerSize,
			width - cornerSize - cornerSize,
			cornerSize,
			u + cornerSize,
			v + textureHeight - cornerSize,
			textureWidth - cornerSize - cornerSize,
			textureHeight
		);
		this.drawTexture(
			matrices, x + width - cornerSize, y + height - cornerSize, u + textureWidth - cornerSize, v + textureHeight - cornerSize, cornerSize, cornerSize
		);
		this.drawTextureRepeatedly(
			matrices, x, y + cornerSize, cornerSize, height - cornerSize - cornerSize, u, v + cornerSize, textureWidth, textureHeight - cornerSize - cornerSize
		);
		this.drawTextureRepeatedly(
			matrices,
			x + cornerSize,
			y + cornerSize,
			width - cornerSize - cornerSize,
			height - cornerSize - cornerSize,
			u + cornerSize,
			v + cornerSize,
			textureWidth - cornerSize - cornerSize,
			textureHeight - cornerSize - cornerSize
		);
		this.drawTextureRepeatedly(
			matrices,
			x + width - cornerSize,
			y + cornerSize,
			cornerSize,
			height - cornerSize - cornerSize,
			u + textureWidth - cornerSize,
			v + cornerSize,
			textureWidth,
			textureHeight - cornerSize - cornerSize
		);
	}

	/**
	 * Draws a textured rectangle repeatedly to cover the area of {@code
	 * width} and {@code height}. The last texture is clipped to fit the area.
	 */
	protected void drawTextureRepeatedly(MatrixStack matrices, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight) {
		for (int i = 0; i < width; i += textureWidth) {
			int j = x + i;
			int k = Math.min(textureWidth, width - i);

			for (int l = 0; l < height; l += textureHeight) {
				int m = y + l;
				int n = Math.min(textureHeight, height - l);
				this.drawTexture(matrices, j, m, u, v, k, n);
			}
		}
	}

	public boolean shouldRender(int originX, int originY, int mouseX, int mouseY) {
		// if (this.progress != null && this.progress.isDone()) {
		int i = originX + this.x;
		int j = i + 26;
		int k = originY + this.y;
		int l = k + 26;
		return mouseX >= i && mouseX <= j && mouseY >= k && mouseY <= l;
		// } else {
		// 	return false;
		// }
	}

	public void addToTree() {
		if (this.parent == null && this.copperCapability.getParent() != null) {
			this.parent = this.getParent(this.copperCapability);
			if (this.parent != null) {
				this.parent.addChild(this);
			}
		}
	}

	public int getY() {
		return this.y;
	}

	public int getX() {
		return this.x;
	}
}
