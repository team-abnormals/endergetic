package endergeticexpansion.common.entities.puffbug.ai;

import java.util.EnumSet;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PuffBugPullOutGoal extends Goal {
	private EntityPuffBug puffbug;
	private int pulls;
	
	public PuffBugPullOutGoal(EntityPuffBug puffbug) {
		this.puffbug = puffbug;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		return !this.puffbug.isInflated() && this.puffbug.isNoEndimationPlaying() && this.puffbug.stuckInBlock;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return !this.puffbug.isInflated() && this.puffbug.stuckInBlock;
	}
	
	@Override
	public void startExecuting() {
		this.pulls = this.puffbug.getRNG().nextInt(4) + 2;
	}
	
	@Override
	public void tick() {
		if(this.puffbug.isNoEndimationPlaying()) {
			if(this.pulls > 0) {
				NetworkUtil.setPlayingAnimationMessage(this.puffbug, EntityPuffBug.PULL_ANIMATION);
				this.pulls--;
			}
		} else if(this.puffbug.isEndimationPlaying(EntityPuffBug.PULL_ANIMATION)) {
			if(this.pulls <= 0 && this.puffbug.getAnimationTick() == 5) {
				this.puffbug.disableProjectile();
				this.puffbug.stuckInBlockState = null;
				
				float[] rotations = this.puffbug.getRotationController().getRotations(1.0F);
				
				float motionX = MathHelper.sin(rotations[1] * ((float) Math.PI / 180F)) * MathHelper.cos(rotations[0] * ((float) Math.PI / 180F));
				float motionY = -MathHelper.sin(rotations[0] * ((float) Math.PI / 180F));
				float motionZ = -MathHelper.cos(rotations[1] * ((float) Math.PI / 180F)) * MathHelper.cos(rotations[0] * ((float) Math.PI / 180F));
				
				Vec3d popOutMotion = new Vec3d(motionX, motionY, motionZ).normalize().scale(0.25F);
				
				this.puffbug.setMotion(popOutMotion);
			}
		}
	}
	
	@Override
	public void resetTask() {
		NetworkUtil.setPlayingAnimationMessage(this.puffbug, EntityPuffBug.BLANK_ANIMATION);
	}
}