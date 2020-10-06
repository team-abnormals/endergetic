package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;

public class AdolescentAttackGoal extends Goal {
	protected final BoofloAdolescentEntity attacker;
	protected int attackTick;
	private final double speedTowardsTarget;
	private final boolean longMemory;
	private Path path;
	private int delayCounter;
	private double targetX;
	private double targetY;
	private double targetZ;
	protected final int attackInterval = 20;
	private long field_220720_k;
	private boolean canPenalize = false;

	public AdolescentAttackGoal(BoofloAdolescentEntity attacker, double speedIn, boolean useLongMemory) {
		this.attacker = attacker;
		this.speedTowardsTarget = speedIn;
		this.longMemory = useLongMemory;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public boolean shouldExecute() {
		long i = this.attacker.world.getGameTime();
		if (i - this.field_220720_k < 20L) {
			return false;
		} else if (!this.attacker.isHungry()) {
			return false;
		} else {
			this.field_220720_k = i;
			Entity target = this.attacker.getBoofloAttackTarget();
			if (target == null) {
				return false;
			} else if (!target.isAlive()) {
				return false;
			} else if (this.attacker.hasFruit()) {
				return false;
			} else {
				if (this.canPenalize) {
					if (--this.delayCounter <= 0) {
						this.path = this.attacker.getNavigator().getPathToEntity(target, 0);
						this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
						return this.path != null;
					} else {
						return true;
					}
				}
				this.path = this.attacker.getNavigator().getPathToEntity(target, 0);
				if (this.path != null) {
					return true;
				} else {
					return this.getAttackReachSqr(target) >= this.attacker.getDistanceSq(target.getPosX(), target.getBoundingBox().minY, target.getPosZ());
				}
			}
		}
	}

	public boolean shouldContinueExecuting() {
		Entity target = this.attacker.getBoofloAttackTarget();
		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else if (this.attacker.hasFruit()) {
			return false;
		} else if (!this.attacker.isHungry()) {
			return false;
		} else if (!this.longMemory) {
			return !this.attacker.getNavigator().noPath();
		} else if (!this.attacker.isWithinHomeDistanceFromPosition(target.getPosition())) {
			return false;
		} else {
			return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity) target).isCreative();
		}
	}

	public void startExecuting() {
		this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
		this.attacker.setAggroed(true);
		this.delayCounter = 0;
	}

	public void resetTask() {
		Entity target = this.attacker.getBoofloAttackTarget();
		if (!EntityPredicates.CAN_AI_TARGET.test(target)) {
			this.attacker.setBoofloAttackTarget(null);
		}
		this.attacker.setAggroed(false);
		this.attacker.getNavigator().clearPath();
	}

	public void tick() {
		Entity target = this.attacker.getBoofloAttackTarget();
		this.attacker.getLookController().setLookPositionWithEntity(target, 10.0F, 10.0F);

		double distToEnemySqr = this.attacker.getDistanceSq(target.getPosX(), target.getBoundingBox().minY, target.getPosZ());

		this.delayCounter--;

		if ((this.longMemory || this.attacker.getEntitySenses().canSee(target)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
			this.targetX = target.getPosX();
			this.targetY = target.getBoundingBox().minY;
			this.targetZ = target.getPosZ();

			this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);

			if (distToEnemySqr > 1024.0D) {
				this.delayCounter += 10;
			} else if (distToEnemySqr > 256.0D) {
				this.delayCounter += 5;
			}

			if (!this.attacker.getNavigator().tryMoveToEntityLiving(target, this.speedTowardsTarget)) {
				this.delayCounter += 15;
			}
		}

		this.attackTick = Math.max(this.attackTick - 1, 0);
		this.tryToCapturePrey(target, distToEnemySqr);
	}

	protected void tryToCapturePrey(Entity prey, double distToEnemySqr) {
		double attackReach = this.getAttackReachSqr(prey);
		if (distToEnemySqr <= attackReach && this.attackTick <= 0) {
			this.attackTick = 20;
			this.attacker.setHasFruit(true);
			if (prey instanceof BolloomFruitEntity) {
				((BolloomFruitEntity) prey).onBroken(false);
				prey.remove();
			}
		}
	}

	protected double getAttackReachSqr(Entity attackTarget) {
		return (this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
	}

	@Nullable
	public Path getPathToEntity(Entity entity) {
		BlockPos pos = entity.getPosition();
		for (int y = 0; y < 8; y++) {
			pos = pos.down(y);
			if (!entity.getEntityWorld().isRemote) {
				if (entity.getEntityWorld().getBlockState(pos).isSolid() || !entity.getEntityWorld().getBlockState(pos).getFluidState().isEmpty()) {
					return this.attacker.getNavigator().getPathToPos(pos, 0);
				}
			}
		}
		return this.attacker.getNavigator().getPathToPos(entity.getPosition(), 0);
	}
}