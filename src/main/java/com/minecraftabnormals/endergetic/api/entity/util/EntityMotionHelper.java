package com.minecraftabnormals.endergetic.api.entity.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class EntityMotionHelper {

	public static void knockbackEntity(Entity entity, float xzForce, float yForce, boolean reverse, boolean setVelocity) {
		if(reverse) {
			setOrAddVelocity(entity, -MathHelper.sin((float) Math.toRadians(entity.rotationYaw)) * xzForce * 0.1F, yForce, MathHelper.cos((float) Math.toRadians(entity.rotationYaw)) * xzForce * 0.1F, setVelocity);
		} else {
			setOrAddVelocity(entity, MathHelper.sin((float) Math.toRadians(entity.rotationYaw)) * xzForce * 0.1F, yForce, -MathHelper.cos((float) Math.toRadians(entity.rotationYaw)) * xzForce * 0.1F, setVelocity);
		}
	}
	
	private static void setOrAddVelocity(Entity entity, float x, float y, float z, boolean set) {
		if(set) {
			entity.setVelocity(x, y, z);
		} else {
			entity.addVelocity(x, y, z);
		}
	}
	
}