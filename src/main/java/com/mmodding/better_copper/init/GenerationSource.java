package com.mmodding.better_copper.init;

public enum GenerationSource {

	SMELTING(2),
	CRAFTING(3),
	GROTH(4),
	DEATH(5),
	LIGHTNING_STRIKE(10);

	private final int power;

	GenerationSource(int power) {
		this.power = power;
	}

	public int getPower() {
		return this.power;
	}
}
