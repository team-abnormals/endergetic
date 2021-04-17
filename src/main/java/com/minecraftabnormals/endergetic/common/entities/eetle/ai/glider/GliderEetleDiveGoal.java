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
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		GliderEetleEntity glider = this.glider;
		LivingEntity attackTarget = glider.getAttackTarget();
		if (attackTarget == null || !attackTarget.isAlive() || GliderEetleEntity.isEntityLarge(attackTarget) || glider.isInWater() || !glider.isFlying() || !glider.isBeingRidden() || glider.getPassengers().get(0) != attackTarget || glider.getHealth() - this.prevHealth <= -3.0F) {
			this.ticksGrabbed = 0;
		} else {
			this.ticksGrabbed++;
		}
		this.prevHealth = glider.getHealth();
		if (this.ticksGrabbed >= 30) {
			World world = glider.world;
			BlockPos pos = glider.getPosition();
			int distanceFromGround = distanceFromGround(glider, world, pos.toMutable());
			if (distanceFromGround > 3 && distanceFromGround < 11) {
				pos = pos.down(distanceFromGround);
				if (world.getEntitiesWithinAABB(ChargerEetleEntity.class, new AxisAlignedBB(pos).grow(4.0D)).size() < 3) {
					Random random = glider.getRNG();
					for (int i = 0; i < 5; i++) {
						BlockPos offsetPos = pos.add(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));
						if (world.isTopSolid(offsetPos.down(), glider) && world.hasNoCollisions(new AxisAlignedBB(offsetPos))) {
							Vector3d gliderPos = new Vector3d(glider.getPosX(), glider.getPosYEye(), glider.getPosZ());
							if (world.rayTraceBlocks(new RayTraceContext(gliderPos, Vector3d.copy(offsetPos), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, glider)).getType() == RayTraceResult.Type.MISS) {
								this.divePos = Vector3d.copy(offsetPos);
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
	public void startExecuting() {
		GliderEetleEntity glider = this.glider;
		glider.getNavigator().clearPath();
		Vector3d target = this.divePos;
		double xDif = (target.getX() + 0.5F) - glider.getPosX();
		double yDif = target.getY() - glider.getPosYEye();
		double zDif = (target.getZ() + 0.5F) - glider.getPosZ();
		float magnitude = MathHelper.sqrt(xDif * xDif + yDif * yDif + zDif * zDif);
		double toDeg = 180.0F / Math.PI;
		this.targetYaw = (float) (MathHelper.atan2(zDif, xDif) * toDeg) - 90.0F;
		this.targetPitch = (float) -(MathHelper.atan2(yDif, magnitude) * toDeg);
		this.divingMotion = new Vector3d(xDif, yDif, zDif).normalize().mul(1.0F, 1.3F, 1.0F);
	}

	@Override
	public boolean shouldContinueExecuting() {
		GliderEetleEntity glider = this.glider;
		LivingEntity attackTarget = glider.getAttackTarget();
		return attackTarget != null && attackTarget.isAlive() && !GliderEetleEntity.isEntityLarge(attackTarget) && glider.isFlying() && this.ticksDiving < 30;
	}

	@Override
	public void tick() {
		this.ticksDiving++;
		GliderEetleEntity glider = this.glider;
		int ticksDiving = this.ticksDiving;
		if (ticksDiving == 5) {
			glider.setMotion(glider.getMotion().add(this.divingMotion));
		}
		glider.setDiving(true);
		glider.setMoving(true);
		glider.setTargetFlyingRotations(new TargetFlyingRotations(this.targetPitch, glider.getTargetFlyingRotations().getTargetFlyRoll()));
		glider.rotationYaw = FlyingRotations.clampedRotate(glider.rotationYaw, this.targetYaw, 15.0F);
		glider.getLookController().setLookPosition(this.divePos);
		if (ticksDiving > 5 && (glider.isOnGround() || glider.collidedHorizontally)) {
			LivingEntity attackTarget = glider.getAttackTarget();
			if (attackTarget != null && glider.isPassenger(attackTarget)) {
				glider.makeGrounded();
				glider.groundedAttacker = attackTarget;
				attackTarget.attackEntityFrom(DamageSource.FLY_INTO_WALL, glider.getRNG().nextInt(6) + 8);
			}
		}
	}

	@Override
	public void resetTask() {
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
			if (world.isTopSolid(pos, glider)) {
				return i;
			}
		}
		return 11;
	}
}
