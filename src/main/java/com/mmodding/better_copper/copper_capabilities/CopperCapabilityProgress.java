package com.mmodding.better_copper.copper_capabilities;

import com.mmodding.better_copper.copper_capabilities.client.gui.CopperCapabilityStatus;

public class CopperCapabilityProgress implements Comparable<CopperCapabilityProgress> {

	final int maxLevelProgress;
	final int currentProgress = 0;

	private CopperCapabilityProgress(int maxLevelProgress) {
		this.maxLevelProgress = maxLevelProgress;
	}

	private CopperCapabilityProgress() {
		this.maxLevelProgress = 0;
	}

	public int compareTo(CopperCapabilityProgress copperCapabilityProgress) {
		// TODO: Complete the method
		return 0;
	}

	public float getProgressPercentage() {
		return (float) this.currentProgress / this.maxLevelProgress;
	}

	public String getProgressFraction() {
		return this.currentProgress + "/" + this.maxLevelProgress;
	}

	public CopperCapabilityStatus getStatus() {
		if (getProgressPercentage() < 0.25F) {
			return CopperCapabilityStatus.OXIDIZED;
		} else if (getProgressPercentage() < 0.5F) {
			return CopperCapabilityStatus.WEATHERED;
		} else if (getProgressPercentage() < 0.75F) {
			return CopperCapabilityStatus.EXPOSED;
		} else {
			return CopperCapabilityStatus.CLEARED;
		}
	}
}
