package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;

import endergeticexpansion.api.entity.util.RayTraceHelper;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.RayTraceResult.Type;

public class BoofloSwimmingGoal extends Goal {
	private EntityBooflo booflo;
	
	public BoofloSwimmingGoal(EntityBooflo booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if(RayTraceHelper.rayTrace(this.booflo, 1.5D, 1.0F).getType() == Type.BLOCK) {
			return false;
		}
		return this.booflo.hasPath() && this.booflo.isNoEndimationPlaying() && this.booflo.isMovingInAir() && this.booflo.isBoofed();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.booflo.isEndimationPlaying(EntityBooflo.SWIM) && this.booflo.isBoofed();
	}
	
	@Override
	public void startExecuting() {
		NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBooflo.SWIM);
	}
}