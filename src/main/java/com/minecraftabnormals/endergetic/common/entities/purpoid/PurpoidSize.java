package com.minecraftabnormals.endergetic.common.entities.purpoid;

public enum PurpoidSize {
	NORMAL(1.0F),
	PURP(0.5F),
	PURPAZOID(2.0F);

	private final float scale;

	PurpoidSize(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return this.scale;
	}
}
