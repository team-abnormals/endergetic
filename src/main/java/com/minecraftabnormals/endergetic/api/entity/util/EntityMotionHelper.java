package com.minecraftabnormals.endergetic.api.entity.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;

public class EntityMotionHelper {

	public static void knockbackEntity(Entity entity, float xzForce, float yForce, boolean reverse, boolean setVelocity) {
		if (reverse) {
			setOrAddVelocity(entity, -Mth.sin((float) Math.toRadians(entity.yRot)) * xzForce * 0.1F, yForce, Mth.cos((float) Math.toRadians(entity.yRot)) * xzForce * 0.1F, setVelocity);
		} else {
			setOrAddVelocity(entity, Mth.sin((float) Math.toRadians(entity.yRot)) * xzForce * 0.1F, yForce, -Mth.cos((float) Math.toRadians(entity.yRot)) * xzForce * 0.1F, setVelocity);
		}
	}

	private static void setOrAddVelocity(Entity entity, float x, float y, float z, boolean set) {
		if (set) {
			entity.lerpMotion(x, y, z);
		} else {
			entity.push(x, y, z);
		}
	}

}