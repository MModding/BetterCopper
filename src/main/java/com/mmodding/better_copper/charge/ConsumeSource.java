package com.mmodding.better_copper.charge;

public enum ConsumeSource {

	ARMOR_CHARGE(5);

	private final int power;

	ConsumeSource(int power) {
		this.power = power;
	}

	public int getPower() {
		return this.power;
	}
}
