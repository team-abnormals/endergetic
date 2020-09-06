package com.minecraftabnormals.endergetic.core.interfaces;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;

/**
 * Implemented on entities to have them have their balloon attachments be unique.
 * @author SmellyModder (Luke Tonon)
 */
public interface CustomBalloonPositioner {
	/**
	 * Called when the balloon attaches to this entity.
	 * This is called from BOTH sides.
	 * @param balloon - The balloon being attached.
	 */
	void onBalloonAttached(BolloomBalloonEntity balloon);

	/**
	 * Called on the server when a balloon detaches from this entity.
	 * @param balloon - The balloon detaching.
	 */
	void onBalloonDetachedServer(BolloomBalloonEntity balloon);

	/**
	 * Called on the client when a balloon detaches from this entity.
	 * This method is isolated to just the client since the balloon attachment update packet detaches all client balloons before syncing them.
	 * @param balloon - The balloon detaching.
	 */
	void onBalloonDetachedClient(BolloomBalloonEntity balloon);

	/**
	 * Called when the balloon is attached to this entity every tick.
	 * @param balloon - The balloon attached.
	 */
	void updateAttachedPosition(BolloomBalloonEntity balloon);
}
