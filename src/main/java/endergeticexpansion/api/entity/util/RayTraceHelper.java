package endergeticexpansion.api.entity.util;

import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class RayTraceHelper {

	public static RayTraceResult rayTrace(Entity entity, double distance, float delta) {
		return entity.world.rayTraceBlocks(new RayTraceContext(
			entity.getEyePosition(delta),
			entity.getEyePosition(delta).add(entity.getLook(delta).scale(distance)),
			RayTraceContext.BlockMode.COLLIDER,
			RayTraceContext.FluidMode.NONE,
			entity
		));
	}
	
	public static RayTraceResult rayTraceWithCustomDirection(Entity entity, float pitch, float yaw, double distance, float delta) {
		return entity.world.rayTraceBlocks(new RayTraceContext(
			entity.getEyePosition(delta),
			entity.getEyePosition(delta).add(getVectorForRotation(pitch, yaw).scale(distance)),
			RayTraceContext.BlockMode.COLLIDER,
			RayTraceContext.FluidMode.NONE,
			entity
		));
	}
	
	public static EntityRayTraceResult rayTraceEntityResult(Entity entity, float pitch, float yaw, double distance, double sqDistance, float delta) {
		Vector3d look = getVectorForRotation(pitch, yaw);
		Vector3d endVec = entity.getEyePosition(delta).add(look.scale(distance));
		AxisAlignedBB axisalignedbb = entity.getBoundingBox().expand(look.scale(distance)).grow(1.0D, 1.0D, 1.0D);
		EntityRayTraceResult entityRaytraceResult = func_221273_a(entity, entity.getEyePosition(delta), endVec, axisalignedbb, (result) -> {
			return !result.isSpectator() && result.canBeCollidedWith();
		}, sqDistance);
		return entityRaytraceResult;
	}
	
	public static final Vector3d getVectorForRotation(float pitch, float yaw) {
		float f = pitch * ((float) Math.PI / 180F);
		float f1 = -yaw * ((float) Math.PI / 180F);
		float f2 = MathHelper.cos(f1);
		float f3 = MathHelper.sin(f1);
		float f4 = MathHelper.cos(f);
		float f5 = MathHelper.sin(f);
		return new Vector3d((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
	}
	
	/**
	 * Copied from Vanilla
	 * It's a raytracing method, but Vanilla's is client only
	 */
	@Nullable
	public static EntityRayTraceResult func_221273_a(Entity p_221273_0_, Vector3d p_221273_1_, Vector3d p_221273_2_, AxisAlignedBB p_221273_3_, Predicate<Entity> p_221273_4_, double p_221273_5_) {
		World world = p_221273_0_.world;
		double d0 = p_221273_5_;
		Entity entity = null;
		Vector3d Vector3d = null;

		for(Entity entity1 : world.getEntitiesInAABBexcluding(p_221273_0_, p_221273_3_, p_221273_4_)) {
			AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow((double)entity1.getCollisionBorderSize());
			Optional<Vector3d> optional = axisalignedbb.rayTrace(p_221273_1_, p_221273_2_);
			if (axisalignedbb.contains(p_221273_1_)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					Vector3d = optional.orElse(p_221273_1_);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vector3d Vector3d1 = optional.get();
				double d1 = p_221273_1_.squareDistanceTo(Vector3d1);
				if (d1 < d0 || d0 == 0.0D) {
					if (entity1.getLowestRidingEntity() == p_221273_0_.getLowestRidingEntity()) {
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
			return new EntityRayTraceResult(entity, Vector3d);
		}
	}
}