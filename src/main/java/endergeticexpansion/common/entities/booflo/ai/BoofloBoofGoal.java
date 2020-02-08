package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class BoofloBoofGoal extends Goal {
	private EntityBooflo booflo;
	
	public BoofloBoofGoal(EntityBooflo booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		boolean onGround = this.booflo.onGround;
		boolean flagChance = !this.booflo.hasAggressiveAttackTarget() ? this.booflo.getRNG().nextFloat() < 0.25F && this.booflo.getAnimationTick() == 20 : this.booflo.getAnimationTick() >= 20;
		
		if(this.booflo.hasAggressiveAttackTarget() && !this.booflo.isBoofed()) {
			return this.booflo.isNoEndimationPlaying();
		}
		
		if(!onGround) {
			if(this.shouldJumpForFall() && !this.booflo.isBoofed() && this.booflo.getRideControlDelay() <= 0) {
				if(this.booflo.isBeingRidden()) {
					this.booflo.setDelayExpanding(true);
					this.booflo.setDelayDecrementing(false);
				}
				return true;
			}
		}
		return this.booflo.getPassengers().isEmpty() && !onGround && !this.booflo.isTempted() && flagChance && this.booflo.isEndimationPlaying(EntityBooflo.HOP);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.booflo.isEndimationPlaying(EntityBooflo.INFLATE);
	}
	
	@Override
	public void startExecuting() {
		this.booflo.setBoofed(true);
		NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBooflo.INFLATE);
	}
	
	private boolean shouldJumpForFall() {
		BlockPos pos = this.booflo.getPosition();
		for(int i = 0; i < 12; i++) {
			pos = pos.down(i);
			IFluidState fluidState = this.booflo.world.getFluidState(pos);
			if(!Block.hasSolidSide(this.booflo.world.getBlockState(pos), this.booflo.world, pos, Direction.UP) && i > 6 || fluidState.isTagged(FluidTags.LAVA)) {
				return true;
			} else if(Block.hasSolidSide(this.booflo.world.getBlockState(pos), this.booflo.world, pos, Direction.UP)) {
				return false;
			}
		}
		return false;
	}
}