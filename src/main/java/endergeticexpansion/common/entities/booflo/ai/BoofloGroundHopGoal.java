package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;

import endergeticexpansion.api.entity.util.RayTraceHelper;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.entities.booflo.EntityBooflo.GroundMoveHelperController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.RayTraceResult.Type;

public class BoofloGroundHopGoal extends Goal {
	private final EntityBooflo booflo;
	private int ticksPassed;

	public BoofloGroundHopGoal(EntityBooflo booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if(RayTraceHelper.rayTrace(this.booflo, 1.5D, 1.0F).getType() == Type.BLOCK) {
			return false;
		}
		return !this.booflo.isBoofed() && this.booflo.hopDelay == 0 && this.booflo.isAnimationPlaying(EntityBooflo.BLANK_ANIMATION) && !this.booflo.isPassenger() && this.booflo.getPassengers().isEmpty();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.ticksPassed <= 10;
	}
	
	@Override
	public void startExecuting() {
		NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBooflo.HOP);
	}
	
	@Override
	public void resetTask() {
		this.ticksPassed = 0;
	}

	@Override
	public void tick() {
		this.ticksPassed++;
		
		((GroundMoveHelperController) this.booflo.getMoveHelper()).setSpeed(1.25D);
	}
}