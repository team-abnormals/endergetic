package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.endergetic.common.entities.eetle.ChargerEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.FlyingRotations;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.TargetFlyingRotations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class GliderEetleDiveGoal extends Goal {
	private final GliderEetleEntity glider;
	@Nullable
	private Vector3d divePos;
	@Nullable
	private Vector3d divingMotion;
	private float prevHealth;
	private int ticksGrabbed;
	private float targetYaw, targetPitch;
	private int ticksDiving;

	public GliderEetleDiveGoal(GliderEetleEntity glider) {
		this.glider = glider;
		this.prevHealth = glider.getHealth();
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		GliderEetleEntity glider = this.glider;
		LivingEntity attackTarget = glider.getTarget();
		if (attackTarget == null || !attackTarget.isAlive() || GliderEetleEntity.isEntityLarge(attackTarget) || glider.isInWater() || !glider.isFlying() || !glider.isVehicle() || glider.getPassengers().get(0) != attackTarget || glider.getHealth() - this.prevHealth <= -3.0F) {
			this.ticksGrabbed = 0;
		} else {
			this.ticksGrabbed++;
		}
		this.prevHealth = glider.getHealth();
		if (this.ticksGrabbed >= 30) {
			World world = glider.level;
			BlockPos pos = glider.blockPosition();
			int distanceFromGround = distanceFromGround(glider, world, pos.mutable());
			if (distanceFromGround > 3 && distanceFromGround < 11) {
				pos = pos.below(distanceFromGround);
				if (world.getEntitiesOfClass(ChargerEetleEntity.class, new AxisAlignedBB(pos).inflate(4.0D)).size() < 3) {
					Random random = glider.getRandom();
					for (int i = 0; i < 5; i++) {
						BlockPos offsetPos = pos.offset(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));
						if (world.loadedAndEntityCanStandOn(offsetPos.below(), glider) && world.noCollision(new AxisAlignedBB(offsetPos))) {
							Vector3d gliderPos = new Vector3d(glider.getX(), glider.getEyeY(), glider.getZ());
							if (world.clip(new RayTraceContext(gliderPos, Vector3d.atLowerCornerOf(offsetPos), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, glider)).getType() == RayTraceResult.Type.MISS) {
								this.divePos = Vector3d.atLowerCornerOf(offsetPos);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void start() {
		GliderEetleEntity glider = this.glider;
		glider.getNavigation().stop();
		Vector3d target = this.divePos;
		double xDif = (target.x() + 0.5F) - glider.getX();
		double yDif = target.y() - glider.getEyeY();
		double zDif = (target.z() + 0.5F) - glider.getZ();
		float magnitude = MathHelper.sqrt(xDif * xDif + yDif * yDif + zDif * zDif);
		double toDeg = 180.0F / Math.PI;
		this.targetYaw = (float) (MathHelper.atan2(zDif, xDif) * toDeg) - 90.0F;
		this.targetPitch = (float) -(MathHelper.atan2(yDif, magnitude) * toDeg);
		this.divingMotion = new Vector3d(xDif, yDif, zDif).normalize().multiply(1.0F, 1.3F, 1.0F);
	}

	@Override
	public boolean canContinueToUse() {
		GliderEetleEntity glider = this.glider;
		LivingEntity attackTarget = glider.getTarget();
		return attackTarget != null && attackTarget.isAlive() && !GliderEetleEntity.isEntityLarge(attackTarget) && glider.isFlying() && this.ticksDiving < 30;
	}

	@Override
	public void tick() {
		this.ticksDiving++;
		GliderEetleEntity glider = this.glider;
		int ticksDiving = this.ticksDiving;
		if (ticksDiving == 5) {
			glider.setDeltaMovement(glider.getDeltaMovement().add(this.divingMotion));
		}
		glider.setDiving(true);
		glider.setMoving(true);
		glider.setTargetFlyingRotations(new TargetFlyingRotations(this.targetPitch, glider.getTargetFlyingRotations().getTargetFlyRoll()));
		glider.yRot = FlyingRotations.clampedRotate(glider.yRot, this.targetYaw, 15.0F);
		glider.getLookControl().setLookAt(this.divePos);
		if (ticksDiving > 5 && (glider.isOnGround() || glider.horizontalCollision)) {
			LivingEntity attackTarget = glider.getTarget();
			if (attackTarget != null && glider.hasPassenger(attackTarget)) {
				glider.makeGrounded();
				glider.groundedAttacker = attackTarget;
				attackTarget.hurt(DamageSource.FLY_INTO_WALL, glider.getRandom().nextInt(6) + 8);
			}
		}
	}

	@Override
	public void stop() {
		this.divePos = null;
		this.divingMotion = null;
		this.prevHealth = this.glider.getHealth();
		this.ticksGrabbed = this.ticksDiving = 0;
		this.targetPitch = this.targetYaw = 0.0F;
	}

	private static int distanceFromGround(GliderEetleEntity glider, World world, BlockPos.Mutable pos) {
		int y = pos.getY();
		for (int i = 0; i <= 11; i++) {
			pos.setY(y - i);
			if (world.loadedAndEntityCanStandOn(pos, glider)) {
				return i;
			}
		}
		return 11;
	}
}
