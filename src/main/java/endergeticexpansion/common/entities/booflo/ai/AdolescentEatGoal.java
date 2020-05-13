package endergeticexpansion.common.entities.booflo.ai;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;

import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class AdolescentEatGoal extends Goal {
	private EntityBoofloAdolescent adolescent;
	private int eatingTicks;

	public AdolescentEatGoal(EntityBoofloAdolescent adolescent) {
		this.adolescent = adolescent;
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.adolescent.isPlayerNear()) {
			return false;
		}
		return this.adolescent.getRNG().nextInt(40) == 0 && this.adolescent.hasFruit() && this.isSafePos();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(this.adolescent.isPlayerNear()) {
			return false;
		}
		
		if(this.adolescent.isDescenting()) {
			return this.isSafePos() && this.adolescent.hasFruit();
		} else if(this.adolescent.isEating()) {
			return this.adolescent.onGround && this.adolescent.hasFruit() && this.eatingTicks < 61;
		}
		
		return false;
	}
	
	@Override
	public void startExecuting() {
		this.adolescent.setDescenting(true);
		this.adolescent.setAIMoveSpeed(0.0F);
		this.adolescent.getNavigator().clearPath();
	}
	
	@Override
	public void resetTask() {
		if(this.adolescent.isDescenting()) {
			this.adolescent.setDescenting(false);
		}
		if(this.adolescent.isEating()) {
			this.adolescent.setEating(false);
			this.adolescent.dropFruit();
			this.adolescent.resetEndimation();
		}
		this.eatingTicks = 0;
	}
	
	@Override
	public void tick() {
		this.adolescent.setAIMoveSpeed(0.0F);
		this.adolescent.getNavigator().clearPath();
		
		if(this.adolescent.isDescenting()) {
			if(this.adolescent.onGround) {
				this.adolescent.setEating(true);
				this.adolescent.setDescenting(false);
			}
		} else if(this.adolescent.isEating()) {
			this.eatingTicks++;
			
			if(this.eatingTicks % 10 == 0) {
				NetworkUtil.setPlayingAnimationMessage(this.adolescent, EntityBoofloAdolescent.EATING_ANIMATION);
				if(this.eatingTicks < 60) {
					this.adolescent.setPlayingEndimation(EntityBoofloAdolescent.EATING_ANIMATION);
				}
			}
			
			if(this.eatingTicks == 60) {
				this.adolescent.resetEndimation();
				this.adolescent.setHungry(false);
				this.adolescent.setHasFruit(false);
				this.adolescent.setEating(false);
				this.adolescent.setEaten(true);
			}
		}
	}

	private boolean isSafePos() {
		BlockPos pos = this.adolescent.getPosition();
		for(int i = 0; i < 10; i++) {
			pos = pos.down(i);
			if(Block.hasSolidSide(this.adolescent.world.getBlockState(pos), this.adolescent.world, pos, Direction.UP) && i >= 4) {
				if(this.adolescent.world.getBlockState(pos).getFluidState().isEmpty() && !this.adolescent.world.getBlockState(pos).isBurning(this.adolescent.world, pos)) {
					return true;
				}
			}
		}
		return false;
	}
}