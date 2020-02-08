package endergeticexpansion.api.endimator.entity;

import endergeticexpansion.api.endimator.Endimation;

public interface IEndimatedEntity {
	public static final Endimation BLANK_ANIMATION = new Endimation();
	
	/**
	 * @return - The entity's animations
	 */
	Endimation[] getEndimations();
	
	/**
	 * @return - The endimation currently being played
	 */
	Endimation getPlayingEndimation();
	
	/**
	 * @param endimationToPlay - The endimation to play
	 */
	void setPlayingEndimation(Endimation endimationToPlay);
	
	default boolean isNoEndimationPlaying() {
		return this.getPlayingEndimation() == BLANK_ANIMATION;
	}
}