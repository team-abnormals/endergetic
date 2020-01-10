package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;

import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;

public class BoofloHuntGoal extends Goal {
	private final EntityBooflo booflo;
	protected int attackTick;
	private final double speedTowardsTarget;
	private final boolean longMemory;
	private Path path;
	private int delayCounter;
	private double targetX;
	private double targetY;
	private double targetZ;
	private long field_220720_k;
	
	public BoofloHuntGoal(EntityBooflo booflo, double speed, boolean useLongMemory) {
		this.booflo = booflo;
		this.speedTowardsTarget = speed;
		this.longMemory = useLongMemory;
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
			} else if(!this.booflo.isBoofed()) {
				return false;
			} else {
				this.path = this.booflo.getNavigator().getPathToPos(new BlockPos(target), 0);
				if(this.path != null) {
					return true;
				} else {
					return this.getAttackReachSqr(target) >= this.booflo.getDistanceSq(target.posX, target.getBoundingBox().minY, target.posZ);
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
		} else if(!this.longMemory) {
			return !this.booflo.getNavigator().noPath();
		} else if(!this.booflo.isWithinHomeDistanceFromPosition(new BlockPos(target))) {
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
		Entity target = this.booflo.getBoofloAttackTarget();
		
		double distToEnemySqr = this.booflo.getDistanceSq(target.posX, target.getBoundingBox().minY, target.posZ);
		
		this.delayCounter--;
		
		if((this.longMemory || this.booflo.getEntitySenses().canSee(target)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.booflo.getRNG().nextFloat() < 0.05F)) {
			this.targetX = target.posX;
			this.targetY = target.getBoundingBox().minY;
			this.targetZ = target.posZ;
			
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
		if(distToEnemySqr <= attackReach && this.attackTick <= 0) {
			this.attackTick = 20;
			if(prey instanceof EntityBolloomFruit) {
				((EntityBolloomFruit)prey).onBroken(this.booflo, false);
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