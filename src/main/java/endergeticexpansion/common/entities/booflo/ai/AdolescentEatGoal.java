package endergeticexpansion.common.entities.booflo.ai;

import endergeticexpansion.api.endimator.EndimatedEntity;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class AdolescentEatGoal extends Goal {
	private EntityBoofloAdolescent booflo;
	private int eatingTicks;

	public AdolescentEatGoal(EntityBoofloAdolescent booflo) {
		this.booflo = booflo;
	}
	
	@Override
	public boolean shouldExecute() {
		return this.booflo.getRNG().nextInt(40) == 0 && this.booflo.hasFruit() && this.isSafePos() && !this.booflo.isPlayerNear();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(this.booflo.isDescenting()) {
			return this.isSafePos() && this.booflo.hasFruit() && !this.booflo.isPlayerNear();
		} else if(this.booflo.isEating()) {
			return this.booflo.onGround && this.booflo.hasFruit() && !this.booflo.isPlayerNear() && this.eatingTicks < 61;
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		this.booflo.setDescenting(true);
		this.booflo.setAIMoveSpeed(0.0F);
		this.booflo.getNavigator().clearPath();
	}
	
	@Override
	public void resetTask() {
		if(this.booflo.isDescenting()) {
			this.booflo.setDescenting(false);
		}
		if(this.booflo.isEating()) {
			this.booflo.setEating(false);
			this.booflo.dropFruit();
			this.booflo.setPlayingAnimation(EntityBoofloAdolescent.BLANK_ANIMATION);
		}
		this.eatingTicks = 0;
	}
	
	@Override
	public void tick() {
		this.booflo.setAIMoveSpeed(0.0F);
		this.booflo.getNavigator().clearPath();
		
		if(this.booflo.isDescenting()) {
			if(this.booflo.onGround) {
				this.booflo.setEating(true);
				this.booflo.setDescenting(false);
			}
		} else if(this.booflo.isEating()) {
			this.eatingTicks++;
			
			if(this.eatingTicks % 10 == 0) {
				NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBoofloAdolescent.EATING_ANIMATION);
				if(this.eatingTicks < 60) {
					this.booflo.setPlayingAnimation(EntityBoofloAdolescent.EATING_ANIMATION);
				}
			}
			
			if(this.eatingTicks == 60) {
				this.booflo.setPlayingAnimation(EndimatedEntity.BLANK_ANIMATION);
				this.booflo.setHungry(false);
				this.booflo.setHasFruit(false);
				this.booflo.setEating(false);
				this.booflo.setEaten(true);
			}
		}
	}

	private boolean isSafePos() {
		BlockPos pos = this.booflo.getPosition();
		for(int i = 0; i < 10; i++) {
			pos = pos.down(i);
			if(Block.hasSolidSide(this.booflo.world.getBlockState(pos), this.booflo.world, pos, Direction.UP) && i >= 4) {
				if(this.booflo.world.getBlockState(pos).getFluidState().isEmpty() && !this.booflo.world.getBlockState(pos).isBurning(this.booflo.world, pos)) {
					return true;
				}
			}
		}
		return false;
	}
}