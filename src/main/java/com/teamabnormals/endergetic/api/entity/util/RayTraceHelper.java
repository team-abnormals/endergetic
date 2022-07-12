package com.teamabnormals.endergetic.api.entity.util;

import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class RayTraceHelper {

	public static HitResult rayTrace(Entity entity, double distance, float delta) {
		return entity.level.clip(new ClipContext(
				entity.getEyePosition(delta),
				entity.getEyePosition(delta).add(entity.getViewVector(delta).scale(distance)),
				ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE,
				entity
		));
	}

	public static HitResult rayTraceWithCustomDirection(Entity entity, float pitch, float yaw, double distance, float delta) {
		return entity.level.clip(new ClipContext(
				entity.getEyePosition(delta),
				entity.getEyePosition(delta).add(getVectorForRotation(pitch, yaw).scale(distance)),
				ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE,
				entity
		));
	}

	public static EntityHitResult rayTraceEntityResult(Entity entity, float pitch, float yaw, double distance, double sqDistance, float delta) {
		Vec3 look = getVectorForRotation(pitch, yaw);
		Vec3 endVec = entity.getEyePosition(delta).add(look.scale(distance));
		AABB axisalignedbb = entity.getBoundingBox().expandTowards(look.scale(distance)).inflate(1.0D, 1.0D, 1.0D);
		EntityHitResult entityRaytraceResult = getEntityHitResult(entity, entity.getEyePosition(delta), endVec, axisalignedbb, (result) -> {
			return !result.isSpectator() && result.isPickable();
		}, sqDistance);
		return entityRaytraceResult;
	}

	public static final Vec3 getVectorForRotation(float pitch, float yaw) {
		float f = pitch * ((float) Math.PI / 180F);
		float f1 = -yaw * ((float) Math.PI / 180F);
		float f2 = Mth.cos(f1);
		float f3 = Mth.sin(f1);
		float f4 = Mth.cos(f);
		float f5 = Mth.sin(f);
		return new Vec3((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
	}

	/**
	 * Copied from Vanilla
	 * It's a raytracing method, but Vanilla's is client only
	 */
	@Nullable
	public static EntityHitResult getEntityHitResult(Entity p_221273_0_, Vec3 p_221273_1_, Vec3 p_221273_2_, AABB p_221273_3_, Predicate<Entity> p_221273_4_, double p_221273_5_) {
		Level world = p_221273_0_.level;
		double d0 = p_221273_5_;
		Entity entity = null;
		Vec3 Vector3d = null;

		for (Entity entity1 : world.getEntities(p_221273_0_, p_221273_3_, p_221273_4_)) {
			AABB axisalignedbb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius());
			Optional<Vec3> optional = axisalignedbb.clip(p_221273_1_, p_221273_2_);
			if (axisalignedbb.contains(p_221273_1_)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					Vector3d = optional.orElse(p_221273_1_);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vec3 Vector3d1 = optional.get();
				double d1 = p_221273_1_.distanceToSqr(Vector3d1);
				if (d1 < d0 || d0 == 0.0D) {
					if (entity1.getRootVehicle() == p_221273_0_.getRootVehicle()) {
						if (d0 == 0.0D) {
							entity = entity1;
							Vector3d = Vector3d1;
						}
					} else {
						entity = entity1;
						Vector3d = Vector3d1;
						d0 = d1;
					}
				}
			}
		}

		if (entity == null) {
			return null;
		} else {
			return new EntityHitResult(entity, Vector3d);
		}
	}
}