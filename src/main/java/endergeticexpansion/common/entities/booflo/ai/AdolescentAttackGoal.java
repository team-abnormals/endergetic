package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;

import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;

public class AdolescentAttackGoal extends Goal {
	protected final EntityBoofloAdolescent attacker;
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
	private int failedPathFindingPenalty = 0;
	private boolean canPenalize = false;

	public AdolescentAttackGoal(EntityBoofloAdolescent attacker, double speedIn, boolean useLongMemory) {
		this.attacker = attacker;
		this.speedTowardsTarget = speedIn;
		this.longMemory = useLongMemory;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public boolean shouldExecute() {
		long i = this.attacker.world.getGameTime();
		if(i - this.field_220720_k < 20L) {
			return false;
		} else {
			this.field_220720_k = i;
			Entity target = this.attacker.getBoofloAttackTarget();
			if(target == null) {
				return false;
			} else if(!target.isAlive()) {
				return false;
			} else {
				if(canPenalize) {
					if(--this.delayCounter <= 0) {
						this.path = this.attacker.getNavigator().getPathToEntityLiving(target, 0);
						this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
						return this.path != null;
					} else {
						return true;
					}
				}
				this.path = this.attacker.getNavigator().getPathToEntityLiving(target, 0);
				if(this.path != null) {
					return true;
				} else {
					return this.getAttackReachSqr(target) >= this.attacker.getDistanceSq(target.posX, target.getBoundingBox().minY, target.posZ);
	            }
			}
		}
	}
	
	public boolean shouldContinueExecuting() {
		Entity target = this.attacker.getBoofloAttackTarget();
		if(target == null) {
			return false;
		} else if(!target.isAlive()) {
			return false;
		} else if(!this.longMemory) {
			return !this.attacker.getNavigator().noPath();
		} else if(!this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(target))) {
			return false;
		} else {
			return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity)target).isCreative();
		}
	}

	public void startExecuting() {
		this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
		this.attacker.setAggroed(true);
		this.delayCounter = 0;
	}
	  
	public void resetTask() {
		Entity target = this.attacker.getBoofloAttackTarget();
		if(!EntityPredicates.CAN_AI_TARGET.test(target)) {
			this.attacker.setAttackTarget((LivingEntity)null);
		}

		this.attacker.setAggroed(false);
		this.attacker.getNavigator().clearPath();
	}

	public void tick() {
		Entity target = this.attacker.getBoofloAttackTarget();
		this.attacker.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
		double d0 = this.attacker.getDistanceSq(target.posX, target.getBoundingBox().minY, target.posZ);
		--this.delayCounter;
		if((this.longMemory || this.attacker.getEntitySenses().canSee(target)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
			this.targetX = target.posX;
			this.targetY = target.getBoundingBox().minY;
			this.targetZ = target.posZ;
			this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
			if(this.canPenalize) {
				this.delayCounter += failedPathFindingPenalty;
				if(this.attacker.getNavigator().getPath() != null) {
					PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
					if(finalPathPoint != null && target.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
						failedPathFindingPenalty = 0;
					else
						failedPathFindingPenalty += 10;
				} else {
					failedPathFindingPenalty += 10;
				}
			}
			if(d0 > 1024.0D) {
				this.delayCounter += 10;
			} else if(d0 > 256.0D) {
	            this.delayCounter += 5;
			}

			if(!this.attacker.getNavigator().tryMoveToEntityLiving(target, this.speedTowardsTarget)) {
				this.delayCounter += 15;
			}
		}

		this.attackTick = Math.max(this.attackTick - 1, 0);
		this.checkAndPerformAttack(target, d0);
	}

	protected void checkAndPerformAttack(Entity enemy, double distToEnemySqr) {
		double d0 = this.getAttackReachSqr(enemy);
		if(distToEnemySqr <= d0 && this.attackTick <= 0) {
			this.attackTick = 20;
			this.attacker.attackEntityAsMob(enemy);
		}
	}

	protected double getAttackReachSqr(Entity attackTarget) {
		return (double)(this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
	}
}
