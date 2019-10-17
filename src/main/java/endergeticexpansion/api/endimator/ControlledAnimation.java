package endergeticexpansion.api.endimator;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author - SmellyModder(Luke Tonon)
 */
@OnlyIn(Dist.CLIENT)
public class ControlledAnimation {
	private int ticksPassed, prevTicksPassed;
	private int duration;
	
	/**
	 * @param duration - total tick length of animation
	 */
	public ControlledAnimation(int duration) {
		this.duration = duration;
		this.ticksPassed = 0;
		this.prevTicksPassed = 0;
	}
	
	public ControlledAnimation() {
		this.duration = 0;
		this.ticksPassed = 0;
		this.prevTicksPassed = 0;
	}
	
	/**
	 * 
	 * @param duration - total tick length of animation
	 */
	public void setDuration(int duration) {
		this.duration = duration;
		this.ticksPassed = 0;
		this.prevTicksPassed = 0;
	}
	
	/*
	 * Updates the timer
	 */
	public void updateTick() {
		this.prevTicksPassed = this.ticksPassed;
		if(this.ticksPassed == this.getDuration()) {
			this.onTicksReachedDuration();
		}
	}
	
	/*
	 * Increments the timer
	 */
	public void incrementTicks() {
		if (this.ticksPassed < this.duration) {
			this.ticksPassed++;
		}
	}
	
	/*
	 * Decrements the timer
	 */
	public void decrementTicks() {
		if (this.duration - this.ticksPassed > 0) {
			this.ticksPassed++;
		} else {
			this.ticksPassed = 0;
		}
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public int getTicksPassed() {
		return this.ticksPassed;
	}
	
	/*
	 * Fired when the animation meets the full duration
	 */
	public void onTicksReachedDuration() {}
	
	public float getAnimationProgress(float partialTicks) {
		return MathHelper.lerp(partialTicks, this.prevTicksPassed, this.ticksPassed) / this.duration;
	}
	
	public float getAnimationProgressSin(float partialTicks, float range, float speed, float offset) {
		return range * MathHelper.sin(speed * this.getAnimationProgress(partialTicks)) - offset;
	}
}
