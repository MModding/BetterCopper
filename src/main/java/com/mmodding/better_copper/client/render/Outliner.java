package com.mmodding.better_copper.client.render;

import java.util.Collections;
import java.util.HashMap;
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
