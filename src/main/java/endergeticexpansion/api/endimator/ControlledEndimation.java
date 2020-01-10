package endergeticexpansion.api.endimator;

import endergeticexpansion.api.EndergeticAPI.ClientInfo;
import net.minecraft.util.math.MathHelper;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class ControlledEndimation {
	private int tick, prevTick;
	public int tickDuration;
	private boolean shouldDecrement;
	public boolean isPaused;
	
	public ControlledEndimation(int tickDuration, int startingValue) {
		this.tick = this.prevTick = startingValue;
		this.tickDuration = tickDuration;
	}
	
	public void update() {
		this.prevTick = this.tick;
	}
	
	public void tick() {
		if(this.isPaused) return;
		
		if(this.shouldDecrement) {
			if(this.tick > 0) {
				this.tick--;
			}
		} else {
			if(this.tick < this.tickDuration) {
				this.tick++;
			}
		}
	}
	
	public void setDecrementing(boolean shouldDecrement) {
		this.shouldDecrement = shouldDecrement;
	}
	
	public boolean isDescrementing() {
		return this.shouldDecrement;
	}
	
	public float getTick() {
		return this.tick;
	}
	
	public void setValue(int amount) {
		this.tick = this.prevTick = amount;
	}
	
	public void addValue(int amount) {
		this.tick += amount;
	}
	
	public void setTimerPaused(boolean paused) {
		this.isPaused = paused;
	}
	
	public void resetTimer() {
		this.tick = this.prevTick = 0;
		this.setTimerPaused(true);
	}
	
	public float getAnimationProgress() {
		return MathHelper.lerp(ClientInfo.getPartialTicks(), this.prevTick, this.tick) / tickDuration;
	}
}