package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;
import java.util.Random;

import javax.annotation.Nullable;

import endergeticexpansion.common.entities.booflo.BoofloEntity;
import endergeticexpansion.common.entities.puffbug.PuffBugEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;

public class BoofloAttackGoal extends Goal {
	private final int UPPER_DISTANCE = 16;
	private final BoofloEntity booflo;
	private Path path;
	private BlockPos upperAirPos;
	private int delayCounter;

	public BoofloAttackGoal(BoofloEntity booflo) {
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
			if(this.booflo.getBoofloAttackTarget() instanceof PuffBugEntity) {
				return false;
			}
			
			this.upperAirPos = this.getUpperPosToTarget(target, this.booflo.getRNG());
			if(this.upperAirPos == null) {
				Path newPath = this.booflo.getNavigator().getPathToEntity(target, 0);
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
		} else if(this.booflo.getPosition().distanceSq(this.upperAirPos) > UPPER_DISTANCE) {
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
		
		if(target != null && this.upperAirPos != null) {
			this.booflo.getLookController().setLookPosition(this.upperAirPos.getX(), this.upperAirPos.getY(), this.upperAirPos.getZ(), 10.0F, 10.0F);
			double distToEnemySqr = this.booflo.getDistanceSq(target.getPosX(), target.getBoundingBox().minY, target.getPosZ());
		
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
	}
	
	@Nullable
	private BlockPos getUpperPosToTarget(Entity target, Random rand) {
		BlockPos startingPos = target.getPosition();
		BlockPos targetPos = BlockPos.ZERO;
		boolean isOpenBelow = true;
		for(int y = 0; y < UPPER_DISTANCE; y++) {
			if(!target.world.getBlockState(startingPos.up(y)).getCollisionShape(target.world, startingPos.up(y)).isEmpty()) {
				isOpenBelow = false;
			}
			
			if(target.world.getBlockState(startingPos.up(y)).getCollisionShape(target.world, startingPos.up(y)).isEmpty() && y > 9) {
				targetPos = startingPos.up(y);
			}
		}
		return isOpenBelow ? targetPos : null;
	}
}