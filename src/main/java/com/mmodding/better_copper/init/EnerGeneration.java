package com.mmodding.better_copper.init;

public enum EnerGeneration {

	CRAFTING(2);

	private final int power;

	EnerGeneration(int power) {
		this.power = power;
	}

	public int getPower() {
		return this.power;
	}
}
