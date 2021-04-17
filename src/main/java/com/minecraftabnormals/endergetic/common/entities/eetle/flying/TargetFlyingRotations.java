package com.minecraftabnormals.endergetic.common.entities.eetle.flying;

public final class TargetFlyingRotations {
	public static final TargetFlyingRotations ZERO = new TargetFlyingRotations(0.0F, 0.0F);
	private final float targetFlyPitch;
	private final float targetFlyRoll;

	public TargetFlyingRotations(float targetFlyPitch, float targetFlyRoll) {
		this.targetFlyPitch = targetFlyPitch;
		this.targetFlyRoll = targetFlyRoll;
	}

	public float getTargetFlyPitch() {
		return this.targetFlyPitch;
	}

	public float getTargetFlyRoll() {
		return this.targetFlyRoll;
	}
}
