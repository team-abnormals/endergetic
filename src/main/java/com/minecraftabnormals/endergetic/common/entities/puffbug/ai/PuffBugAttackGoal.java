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

public class PuffBugAttackGoal extends Goal {
	public static final float SHOOT_RANGE = 8.0F;
	private final PuffBugEntity puffbug;
	@Nullable
	private Path path;
	private int delayCounter;
	private int ticksChased;

	public PuffBugAttackGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.setMutexFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		LivingEntity target = this.puffbug.getAttackTarget();
		if (target == null) {
			return false;
		} else if (!PuffBugEntity.CAN_ANGER.test(target)) {
			return false;
		} else if (!this.puffbug.isInflated()) {
			return false;
		}

		Path newPath = this.puffbug.getNavigator().getPathToEntity(target, 0);
		if (newPath != null) {
			this.path = newPath;
		}

		return !this.puffbug.isPassenger() && this.puffbug.getLaunchDirection() == null && this.path != null;
	}

	@Override
	public void startExecuting() {
		this.puffbug.getNavigator().setPath(this.path, 2.5F);
		this.puffbug.setAggroed(true);
		this.delayCounter = 0;
		this.ticksChased = 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.puffbug.isInflated()) {
			return false;
		}
		return this.puffbug.getLaunchDirection() == null && this.puffbug.getAttackTarget() != null && PuffBugEntity.CAN_ANGER.test(this.puffbug.getAttackTarget());
	}

	@Override
	public void tick() {
		this.delayCounter--;
		this.ticksChased++;

		Entity target = this.puffbug.getAttackTarget();
		this.puffbug.getLookController().setLookPositionWithEntity(target, 20.0F, 20.0F);

		if (this.delayCounter <= 0) {
			Path path = this.puffbug.getNavigator().getPathToPos(target.getPosition().up(4), 4);
			if (path != null && this.puffbug.getNavigator().setPath(path, 3.5F)) {
				this.delayCounter += 5;
			}

			if (this.puffbug.getDistance(target) <= SHOOT_RANGE) {
				Vector3d distance = new Vector3d(target.getPosX() - this.puffbug.getPosX(), target.getPosY() - this.puffbug.getPosY(), target.getPosZ() - this.puffbug.getPosZ());

				float pitch = -((float) (MathHelper.atan2(distance.getY(), (double) MathHelper.sqrt(distance.getX() * distance.getX() + distance.getZ() * distance.getZ())) * (double) (180F / (float) Math.PI)));
				float yaw = (float) (MathHelper.atan2(distance.getZ(), distance.getX()) * (double) (180F / (float) Math.PI)) - 90F;

				double startingDistance = SHOOT_RANGE;
				startingDistance = startingDistance * startingDistance;
				RayTraceResult blockTrace = this.traceBlocks(MathHelper.wrapDegrees(pitch), yaw);

				if (blockTrace != null) {
					startingDistance = blockTrace.getHitVec().squareDistanceTo(this.puffbug.getEyePosition(1.0F));
				}

				RayTraceResult rayTrace = RayTraceHelper.rayTraceEntityResult(this.puffbug, MathHelper.wrapDegrees(pitch), yaw, SHOOT_RANGE, startingDistance, 1.0F);

				if (this.ticksChased >= 30 && this.canFitNewCollisionShape() && rayTrace != null && rayTrace.getType() != Type.BLOCK && pitch > 30.0F) {
					this.puffbug.setLaunchDirection(MathHelper.wrapDegrees(pitch), yaw);
					this.puffbug.getNavigator().clearPath();
				}
			}
		}
	}

	public void resetTask() {
		Entity target = this.puffbug.getAttackTarget();
		if (!EntityPredicates.CAN_AI_TARGET.test(target)) {
			this.puffbug.setAttackTarget(null);
		}
		this.puffbug.setAggroed(false);
		this.puffbug.getNavigator().clearPath();
	}

	private boolean canFitNewCollisionShape() {
		return this.puffbug.isChild() ? this.puffbug.world.hasNoCollisions(this.getBoundingBoxForSize(PuffBugEntity.PROJECTILE_SIZE_CHILD).offset(0.0F, 0.225F, 0.0F)) : this.puffbug.world.hasNoCollisions(this.getBoundingBoxForSize(PuffBugEntity.PROJECTILE_SIZE).offset(0.0F, 0.225F, 0.0F));
	}

	private AxisAlignedBB getBoundingBoxForSize(EntitySize size) {
		float f = size.width / 2.0F;
		Vector3d vec3d = new Vector3d(this.puffbug.getPosX() - (double) f, this.puffbug.getPosY(), this.puffbug.getPosZ() - (double) f);
		Vector3d vec3d1 = new Vector3d(this.puffbug.getPosX() + (double) f, this.puffbug.getPosY() + (double) size.height, this.puffbug.getPosZ() + (double) f);
		return new AxisAlignedBB(vec3d, vec3d1);
	}

	public RayTraceResult traceBlocks(float pitch, float yaw) {
		Vector3d eyeVec = this.puffbug.getEyePosition(1.0F);
		Vector3d lookVec = RayTraceHelper.getVectorForRotation(pitch, yaw);
		Vector3d vec3d2 = eyeVec.add(lookVec.getX() * SHOOT_RANGE, lookVec.getY() * SHOOT_RANGE, lookVec.getZ() * SHOOT_RANGE);
		return this.puffbug.world.rayTraceBlocks(new RayTraceContext(eyeVec, vec3d2, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, this.puffbug));
	}
}