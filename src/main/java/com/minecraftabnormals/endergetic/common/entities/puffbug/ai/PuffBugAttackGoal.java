package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;

import net.minecraft.entity.ai.goal.Goal.Flag;

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
				Vector3d distance = new Vector3d(target.getX() - this.puffbug.getX(), target.getY() - this.puffbug.getY(), target.getZ() - this.puffbug.getZ());

				float pitch = -((float) (MathHelper.atan2(distance.y(), (double) MathHelper.sqrt(distance.x() * distance.x() + distance.z() * distance.z())) * (double) (180F / (float) Math.PI)));
				float yaw = (float) (MathHelper.atan2(distance.z(), distance.x()) * (double) (180F / (float) Math.PI)) - 90F;

				double startingDistance = SHOOT_RANGE;
				startingDistance = startingDistance * startingDistance;
				RayTraceResult blockTrace = this.traceBlocks(MathHelper.wrapDegrees(pitch), yaw);

				if (blockTrace != null) {
					startingDistance = blockTrace.getLocation().distanceToSqr(this.puffbug.getEyePosition(1.0F));
				}

				RayTraceResult rayTrace = RayTraceHelper.rayTraceEntityResult(this.puffbug, MathHelper.wrapDegrees(pitch), yaw, SHOOT_RANGE, startingDistance, 1.0F);

				if (this.ticksChased >= 30 && this.canFitNewCollisionShape() && rayTrace != null && rayTrace.getType() != Type.BLOCK && pitch > 30.0F) {
					this.puffbug.setLaunchDirection(MathHelper.wrapDegrees(pitch), yaw);
					this.puffbug.getNavigation().stop();
				}
			}
		}
	}

	public void stop() {
		Entity target = this.puffbug.getTarget();
		if (!EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(target)) {
			this.puffbug.setTarget(null);
		}
		this.puffbug.setAggressive(false);
		this.puffbug.getNavigation().stop();
	}

	private boolean canFitNewCollisionShape() {
		return this.puffbug.isBaby() ? this.puffbug.level.noCollision(this.getBoundingBoxForSize(PuffBugEntity.PROJECTILE_SIZE_CHILD).move(0.0F, 0.225F, 0.0F)) : this.puffbug.level.noCollision(this.getBoundingBoxForSize(PuffBugEntity.PROJECTILE_SIZE).move(0.0F, 0.225F, 0.0F));
	}

	private AxisAlignedBB getBoundingBoxForSize(EntitySize size) {
		float f = size.width / 2.0F;
		Vector3d vec3d = new Vector3d(this.puffbug.getX() - (double) f, this.puffbug.getY(), this.puffbug.getZ() - (double) f);
		Vector3d vec3d1 = new Vector3d(this.puffbug.getX() + (double) f, this.puffbug.getY() + (double) size.height, this.puffbug.getZ() + (double) f);
		return new AxisAlignedBB(vec3d, vec3d1);
	}

	public RayTraceResult traceBlocks(float pitch, float yaw) {
		Vector3d eyeVec = this.puffbug.getEyePosition(1.0F);
		Vector3d lookVec = RayTraceHelper.getVectorForRotation(pitch, yaw);
		Vector3d vec3d2 = eyeVec.add(lookVec.x() * SHOOT_RANGE, lookVec.y() * SHOOT_RANGE, lookVec.z() * SHOOT_RANGE);
		return this.puffbug.level.clip(new RayTraceContext(eyeVec, vec3d2, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, this.puffbug));
	}
}