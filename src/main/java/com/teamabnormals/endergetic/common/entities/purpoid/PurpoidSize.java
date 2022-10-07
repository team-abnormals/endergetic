package com.teamabnormals.endergetic.common.entities.purpoid;

public enum PurpoidSize {
	NORMAL(1.0F, 1.0F),
	PURP(0.615F, 0.5F),
	PURPAZOID(2.0F, 3.0F);

	private final float scale;
	private final float healthMultiplier;

	PurpoidSize(float scale, float healthMultiplier) {
		this.scale = scale;
		this.healthMultiplier = healthMultiplier;
	}

	public float getScale() {
		return this.scale;
	}

	public float getHealthMultiplier() {
		return this.healthMultiplier;
	}
}
