package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.registry.EEItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;

public class BoofloHuntPuffBugGoal extends Goal {
	private static final float SPEED = 1.0F;
	private BoofloEntity booflo;
	private Path path;
	private int delayCounter;
	private double targetX, targetY, targetZ;

	public BoofloHuntPuffBugGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		if(!(this.booflo.isBoofed() && !this.booflo.isPregnant() && this.booflo.getBoofloAttackTarget() instanceof PuffBugEntity && this.booflo.getBoofloAttackTarget().isAlive() && !this.booflo.hasCaughtPuffBug())) {
			return false;
		}
		this.path = this.booflo.getNavigator().getPathToEntity(this.booflo.getBoofloAttackTarget(), 0);
		if(this.path != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		Entity target = this.booflo.getBoofloAttackTarget();
		return this.booflo.getPassengers().isEmpty() && !this.booflo.hasCaughtPuffBug() && this.booflo.isBoofed() && !this.booflo.isPregnant() && target != null && target.isAlive() && target instanceof PuffBugEntity;
	}
	
	@Override
	public void startExecuting() {
		this.booflo.getNavigator().setPath(this.path, SPEED);
		this.booflo.setAggroed(true);
		this.delayCounter = 0;
		
		if(this.booflo.hasCaughtFruit()) {
			this.booflo.setCaughtFruit(false);
			this.booflo.entityDropItem(EEItems.BOLLOOM_FRUIT.get());
		}
	}
	
	@Override
	public void tick() {
		PuffBugEntity target = (PuffBugEntity) this.booflo.getBoofloAttackTarget();
		
		double distToEnemySqr = this.booflo.getDistanceSq(target.getPosX(), target.getBoundingBox().minY, target.getPosZ());
		
		this.delayCounter--;
		
		if(this.delayCounter <= 0 || target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.booflo.getRNG().nextFloat() < 0.05F) {
			this.booflo.getLookController().setLookPosition(target.getPosX(), target.getPosY(), target.getPosZ(), 10.0F, 10.0F);
			
			this.delayCounter = 4 + this.booflo.getRNG().nextInt(7);
			
			if(distToEnemySqr > 256.0D) {
				this.delayCounter += 5;
			}

			if(!this.booflo.getNavigator().tryMoveToXYZ(target.getPosX(), target.getPosY(), target.getPosZ(), SPEED)) {
				this.delayCounter += 5;
			}
		}
		
		if(this.booflo.getPassengers().isEmpty() && this.booflo.getRNG().nextFloat() < 0.1F) {
			this.tryToCatch(target, distToEnemySqr);
		}
	}
	
	@Override
	public void resetTask() {
		this.booflo.setAggroed(false);
		this.booflo.getNavigator().clearPath();
	}
	
	protected void tryToCatch(PuffBugEntity enemy, double distToEnemySqr) {
		double attackRange = (this.booflo.getWidth() * 2.0F * this.booflo.getWidth() * 2.0F + enemy.getWidth()) * 0.75F;
		if(distToEnemySqr <= attackRange) {
			this.booflo.catchPuffBug(enemy);
		}
	}
}