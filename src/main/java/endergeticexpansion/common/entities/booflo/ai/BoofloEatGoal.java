package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.entity.ai.goal.Goal;

public class BoofloEatGoal extends Goal {
	private EntityBooflo booflo;
	private float originalYaw;

	public BoofloEatGoal(EntityBooflo booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		if(this.booflo.isPlayerNear()) {
			return false;
		}
		return this.booflo.isAnimationPlaying(EntityBooflo.BLANK_ANIMATION) && this.booflo.hasCaughtFruit() && !this.booflo.isBoofed() && this.booflo.onGround && !this.booflo.isInLove();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		boolean flag = true;
		if(!booflo.hasCaughtFruit()) {
			if(this.booflo.getAnimationTick() < 140) {
				flag = false;
			}
		}
		if(this.booflo.isPlayerNear()) {
			return false;
		}
		return this.booflo.isAnimationPlaying(EntityBooflo.EAT) && flag && !this.booflo.isBoofed() && this.booflo.onGround;
	}
	
	@Override
	public void startExecuting() {
		NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBooflo.EAT);
		this.originalYaw = this.booflo.rotationYaw;
	}
	
	@Override
	public void resetTask() {
		this.originalYaw = 0;
		if(this.booflo.hasCaughtFruit()) {
			this.booflo.setCaughtFruit(false);
			this.booflo.entityDropItem(EEItems.BOLLOOM_FRUIT.get());
		}
		NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBooflo.BLANK_ANIMATION);
	}

	@Override
	public void tick() {
		this.booflo.rotationYaw = this.originalYaw;
		this.booflo.prevRotationYaw = this.originalYaw;
	}
}