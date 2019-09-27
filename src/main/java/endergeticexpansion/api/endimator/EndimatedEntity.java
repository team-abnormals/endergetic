package endergeticexpansion.api.endimator;

import javax.annotation.Nullable;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * Skeleton class for Endimator entities
 * @author - SmellyModder(Luke Tonon)
 */
public class EndimatedEntity extends CreatureEntity {
	public int frame;
	private int animationTick;
	public static final Endimation BLANK_ANIMATION = new Endimation();
	private Endimation animation = BLANK_ANIMATION;

	public EndimatedEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	@Override
	public void tick() {
		super.tick();
		this.frame++;
		if(!this.isAnimationPlaying(BLANK_ANIMATION)) {
			this.animationTick++;
			if(this.isWorldRemote()) {
				if(this.getAnimationTick() >= this.getPlayingAnimation().getAnimationTickDuration()) {
					this.resetPlayingAnimationToDefault();
				}
			}
		}
	}
	
	/**
	 * @param animation - The animation to check
	 * @return - Is the animation playing
	 */
	public boolean isAnimationPlaying(Endimation animation) {
		return this.getPlayingAnimation() == animation;
	}
	
	/**
	 * @return - Is the world remote; if true: client
	 */
	public boolean isWorldRemote() {
		return this.getEntityWorld().isRemote;
	}
	
	/**
	 * @return - Gets the playing animation
	 */
	public Endimation getPlayingAnimation() {
		return this.animation;
	}
	
	/**
	 * @return - Gets this entity's animations
	 */
	@Nullable
	public Endimation[] getAnimations() {
		return null;
	}
	
	/**
	 * @return  - The progress; measured in ticks, of the current playing animation
	 */
	public int getAnimationTick() {
		return this.animationTick;
	}
	
	/**
	 * Sets the progress of the current playing animation
	 * @param animationTick - Progress; measured in ticks
	 */
	public void setAnimationTick(int animationTick) {
		this.animationTick = animationTick;
	}
	
	/**
	 * Sets an animation to play
	 * @param animationToPlay - The animation to play
	 */
	public void setPlayingAnimation(Endimation animationToPlay) {
		this.animation = animationToPlay;
		this.setAnimationTick(0);
	}
	
	/**
	 * Resets the current animation to a blank one
	 */
	public void resetPlayingAnimationToDefault() {
		this.animation = BLANK_ANIMATION;
	}
}