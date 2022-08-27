package com.mmodding.better_copper.client.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Credit: Create
 */
public class Outliner {

	private final Map<Object, OutlineEntry> outlines = Collections.synchronizedMap(new HashMap<>());

	public BoxOutline.OutlineParams showValueBox(Object slot, ValueBox box) {
		outlines.put(slot, new OutlineEntry(box));
		return box.getParams();
	}

	public void tickOutlines() {
		Iterator<OutlineEntry> iterator = outlines.values()
				.iterator();
		while (iterator.hasNext()) {
			OutlineEntry entry = iterator.next();
			entry.tick();
			if (!entry.isAlive())
				iterator.remove();
		}
	}

	public void renderOutlines(MatrixStack ms, SuperRenderTypeBuffer buffer, float pt) {
		outlines.forEach((key, entry) -> {
			BoxOutline outline = entry.getOutline();
			BoxOutline.OutlineParams params = outline.getParams();
			params.alpha = 1;
			if (entry.isFading()) {
				int prevTicks = entry.ticksTillRemoval + 1;
				float fadeticks = OutlineEntry.fadeTicks;
				float lastAlpha = prevTicks >= 0 ? 1 : 1 + (prevTicks / fadeticks);
				float currentAlpha = 1 + (entry.ticksTillRemoval / fadeticks);
				float alpha = MathHelper.lerp(pt, lastAlpha, currentAlpha);

				params.alpha = alpha * alpha * alpha;
				if (params.alpha < 1 / 8f)
					return;
			}
			outline.render(ms, buffer, pt);
		});
	}

	public static class OutlineEntry {

		static final int fadeTicks = 8;
		private final BoxOutline outline;
		private int ticksTillRemoval;

		public OutlineEntry(BoxOutline outline) {
			this.outline = outline;
			ticksTillRemoval = 1;
		}

		public void tick() {
			ticksTillRemoval--;
			outline.tick();
		}

		public boolean isAlive() {
			return ticksTillRemoval >= -fadeTicks;
		}

		public boolean isFading() {
			return ticksTillRemoval < 0;
		}

		public BoxOutline getOutline() {
			return outline;
		}
	}
}
