package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;

public class BoofloHuntFruitGoal extends Goal {
	private final BoofloEntity booflo;
	protected int attackTick;
	private final double speedTowardsTarget;
	private Path path;
	private int delayCounter;
	private double targetX;
	private double targetY;
	private double targetZ;
	private long field_220720_k;
	
	public BoofloHuntFruitGoal(BoofloEntity booflo, double speed) {
		this.booflo = booflo;
		this.speedTowardsTarget = speed;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}
	
	public boolean shouldExecute() {
		long i = this.booflo.world.getGameTime();
		if(i - this.field_220720_k < 20L) {
			return false;
		} else {
			this.field_220720_k = i;
			Entity target = this.booflo.getBoofloAttackTarget();
			if(target == null) {
				return false;
			} else if(!target.isAlive()) {
				return false;
			} else if(this.booflo.hasAggressiveAttackTarget()) {
				return false;
			} else if(!this.booflo.isBoofed()) {
				return false;
			} else {
				this.path = this.booflo.getNavigator().getPathToPos(target.func_233580_cy_(), 0);
				if(this.path != null) {
					return true;
				} else {
					return this.getAttackReachSqr(target) >= this.booflo.getDistanceSq(target.getPosX(), target.getBoundingBox().minY, target.getPosZ());
	            }
			}
		}
	}
	
	public boolean shouldContinueExecuting() {
		Entity target = this.booflo.getBoofloAttackTarget();
		if(target == null) {
			return false;
		} else if(!target.isAlive()) {
			return false;
		} else if(!this.booflo.isBoofed()) {
			return false;
		} else if(!this.booflo.isWithinHomeDistanceFromPosition(target.func_233580_cy_())) {
			return false;
		} else {
			return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity)target).isCreative();
		}
	}

	public void startExecuting() {
		this.booflo.getNavigator().setPath(this.path, 1.0F);
		this.booflo.setAggroed(true);
		this.delayCounter = 0;
	}
	  
	public void resetTask() {
		Entity target = this.booflo.getBoofloAttackTarget();
		if(!EntityPredicates.CAN_AI_TARGET.test(target)) {
			this.booflo.setBoofloAttackTargetId(0);
		}
		this.booflo.setAggroed(false);
		this.booflo.getNavigator().clearPath();
	}

	public void tick() {
		this.delayCounter--;
		Entity target = this.booflo.getBoofloAttackTarget();
		
		double distToEnemySqr = this.booflo.getDistanceSq(target.getPosX(), target.getBoundingBox().minY, target.getPosZ());
		this.booflo.getLookController().setLookPosition(target.getPosX(), target.getPosY(), target.getPosZ(), 10.0F, 10.0F);
		
		if(this.delayCounter <= 0 || target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.booflo.getRNG().nextFloat() < 0.05F) {
			this.targetX = target.getPosX();
			this.targetY = target.getBoundingBox().minY;
			this.targetZ = target.getPosZ();
			
			this.delayCounter = 4 + this.booflo.getRNG().nextInt(7);
			
			if(distToEnemySqr > 1024.0D) {
				this.delayCounter += 5;
			} else if(distToEnemySqr > 256.0D) {
	            this.delayCounter += 5;
			}

			if(!this.booflo.getNavigator().tryMoveToEntityLiving(target, this.speedTowardsTarget)) {
				this.delayCounter += 5;
			}
		}

		this.attackTick = Math.max(this.attackTick - 1, 0);
		this.tryToCapturePrey(target, distToEnemySqr);
	}

	protected void tryToCapturePrey(Entity prey, double distToEnemySqr) {
		double attackReach = this.getAttackReachSqr(prey);
		if (distToEnemySqr <= attackReach && this.attackTick <= 0) {
			this.attackTick = 20;
			if (prey instanceof BolloomFruitEntity) {
				((BolloomFruitEntity)prey).onBroken(false);
				this.booflo.setCaughtFruit(true);
				this.booflo.setHungry(false);
				prey.remove();
			}
		}
	}

	protected double getAttackReachSqr(Entity attackTarget) {
		return (this.booflo.getWidth() * 2.0F * this.booflo.getWidth() * 2.0F + attackTarget.getWidth()) * 0.85F;
	}
}