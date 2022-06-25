package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

public class PuffBugAttackGoal extends Goal {
	public static final float SHOOT_RANGE = 8.0F;
	private final PuffBugEntity puffbug;
	@Nullable
	private Path path;
	private int delayCounter;
	private int ticksChased;

	public PuffBugAttackGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		LivingEntity target = this.puffbug.getTarget();
		if (target == null) {
			return false;
		} else if (!PuffBugEntity.CAN_ANGER.test(target)) {
			return false;
		} else if (!this.puffbug.isInflated()) {
			return false;
		}

		Path newPath = this.puffbug.getNavigation().createPath(target, 0);
		if (newPath != null) {
			this.path = newPath;
		}

		return !this.puffbug.isPassenger() && this.puffbug.getLaunchDirection() == null && this.path != null;
	}

	@Override
	public void start() {
		this.puffbug.getNavigation().moveTo(this.path, 2.5F);
		this.puffbug.setAggressive(true);
		this.delayCounter = 0;
		this.ticksChased = 0;
	}

	@Override
	public boolean canContinueToUse() {
		if (!this.puffbug.isInflated()) {
			return false;
		}
		return this.puffbug.getLaunchDirection() == null && this.puffbug.getTarget() != null && PuffBugEntity.CAN_ANGER.test(this.puffbug.getTarget());
	}

	@Override
	public void tick() {
		this.delayCounter--;
		this.ticksChased++;

		Entity target = this.puffbug.getTarget();
		this.puffbug.getLookControl().setLookAt(target, 20.0F, 20.0F);

		if (this.delayCounter <= 0) {
			Path path = this.puffbug.getNavigation().createPath(target.blockPosition().above(4), 4);
			if (path != null && this.puffbug.getNavigation().moveTo(path, 3.5F)) {
				this.delayCounter += 5;
			}

			if (this.puffbug.distanceTo(target) <= SHOOT_RANGE) {
				Vec3 distance = new Vec3(target.getX() - this.puffbug.getX(), target.getY() - this.puffbug.getY(), target.getZ() - this.puffbug.getZ());

				float pitch = -((float) (Mth.atan2(distance.y(), Mth.sqrt((float) (distance.x() * distance.x() + distance.z() * distance.z()))) * (double) (180F / (float) Math.PI)));
				float yaw = (float) (Mth.atan2(distance.z(), distance.x()) * (double) (180F / (float) Math.PI)) - 90F;

				double startingDistance = SHOOT_RANGE;
				startingDistance = startingDistance * startingDistance;
				HitResult blockTrace = this.traceBlocks(Mth.wrapDegrees(pitch), yaw);

				if (blockTrace != null) {
					startingDistance = blockTrace.getLocation().distanceToSqr(this.puffbug.getEyePosition(1.0F));
				}

				HitResult rayTrace = RayTraceHelper.rayTraceEntityResult(this.puffbug, Mth.wrapDegrees(pitch), yaw, SHOOT_RANGE, startingDistance, 1.0F);

				if (this.ticksChased >= 30 && this.canFitNewCollisionShape() && rayTrace != null && rayTrace.getType() != Type.BLOCK && pitch > 30.0F) {
					this.puffbug.setLaunchDirection(Mth.wrapDegrees(pitch), yaw);
					this.puffbug.getNavigation().stop();
				}
			}
		}
	}

	public void stop() {
		Entity target = this.puffbug.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
			this.puffbug.setTarget(null);
		}
		this.puffbug.setAggressive(false);
		this.puffbug.getNavigation().stop();
	}

	private boolean canFitNewCollisionShape() {
		return this.puffbug.isBaby() ? this.puffbug.level.noCollision(this.getBoundingBoxForSize(PuffBugEntity.PROJECTILE_SIZE_CHILD).move(0.0F, 0.225F, 0.0F)) : this.puffbug.level.noCollision(this.getBoundingBoxForSize(PuffBugEntity.PROJECTILE_SIZE).move(0.0F, 0.225F, 0.0F));
	}

	private AABB getBoundingBoxForSize(EntityDimensions size) {
		float f = size.width / 2.0F;
		Vec3 vec3d = new Vec3(this.puffbug.getX() - (double) f, this.puffbug.getY(), this.puffbug.getZ() - (double) f);
		Vec3 vec3d1 = new Vec3(this.puffbug.getX() + (double) f, this.puffbug.getY() + (double) size.height, this.puffbug.getZ() + (double) f);
		return new AABB(vec3d, vec3d1);
	}

	public HitResult traceBlocks(float pitch, float yaw) {
		Vec3 eyeVec = this.puffbug.getEyePosition(1.0F);
		Vec3 lookVec = RayTraceHelper.getVectorForRotation(pitch, yaw);
		Vec3 vec3d2 = eyeVec.add(lookVec.x() * SHOOT_RANGE, lookVec.y() * SHOOT_RANGE, lookVec.z() * SHOOT_RANGE);
		return this.puffbug.level.clip(new ClipContext(eyeVec, vec3d2, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.puffbug));
	}
}