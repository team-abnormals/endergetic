package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;
import java.util.Random;

import javax.annotation.Nullable;

import endergeticexpansion.common.entities.booflo.EntityBooflo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;

public class BoofloAttackGoal extends Goal {
	private final EntityBooflo booflo;
	protected int attackTick;
	private Path path;
	private int delayCounter;
	private BlockPos upperAirPos;

	public BoofloAttackGoal(EntityBooflo booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}
	
	public boolean shouldExecute() {
		Entity target = this.booflo.getBoofloAttackTarget();
		if(target == null) {
			return false;
		} else if(!target.isAlive()) {
			return false;
		} else if(!this.booflo.hasAggressiveAttackTarget()) {
			return false;
		} else if(!this.booflo.isBoofed()) {
			return false;
		} else {
			this.upperAirPos = this.getUpperPosToTarget(target, this.booflo.getRNG());
			if(this.upperAirPos == null) {
				Path newPath = this.booflo.getNavigator().getPathToEntityLiving(target, 0);
				this.upperAirPos = newPath != null ? newPath.func_224770_k() : null;
				return upperAirPos != null;
			}
			
			this.path = this.booflo.getNavigator().getPathToPos(this.upperAirPos, 0);
			
			if(this.path != null && this.booflo.hasAggressiveAttackTarget()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean shouldContinueExecuting() {
		Entity target = this.booflo.getBoofloAttackTarget();
		if(target == null) {
			return false;
		} else if(!target.isAlive()) {
			return false;
		} else if(!this.booflo.isBoofed()) {
			return false;
		} else if(this.booflo.getPosition().distanceSq(this.upperAirPos) > 16) {
			return false;
		} else {
			return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity)target).isCreative();
		}
	}

	public void startExecuting() {
		this.booflo.getNavigator().setPath(this.path, 1.35F);
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
		
		if(target != null) {
			this.booflo.getLookController().setLookPosition(this.upperAirPos.getX(), this.upperAirPos.getY(), this.upperAirPos.getZ(), 10.0F, 10.0F);
			double distToEnemySqr = this.booflo.getDistanceSq(target.posX, target.getBoundingBox().minY, target.posZ);
		
			if(this.delayCounter <= 0 && !target.isInvisible()) {
				this.delayCounter = 4 + this.booflo.getRNG().nextInt(7);
				
				if(distToEnemySqr > 256.0D) {
		            this.delayCounter += 5;
				}

				if(this.path != null && !this.booflo.getNavigator().tryMoveToXYZ(this.path.func_224770_k().getX(), this.path.func_224770_k().getY(), this.path.func_224770_k().getZ(), 1.35F)) {
					this.delayCounter += 5;
				}
			}
		}

		this.attackTick = Math.max(this.attackTick - 1, 0);
	}
	
	@Nullable
	private BlockPos getUpperPosToTarget(Entity target, Random rand) {
		BlockPos startingPos = target.getPosition();
		BlockPos targetPos = BlockPos.ZERO;
		boolean isOpenBelow = true;
		int randHeight = 9;
		for(int y = 0; y < 16; y++) {
			if(!target.world.getBlockState(startingPos.up(y)).getCollisionShape(target.world, startingPos.up(y)).isEmpty()) {
				isOpenBelow = false;
			}
			
			if(target.world.getBlockState(startingPos.up(y)).getCollisionShape(target.world, startingPos.up(y)).isEmpty() && y > randHeight) {
				targetPos = startingPos.up(y);
			}
		}
		return isOpenBelow ? targetPos : null;
	}
}