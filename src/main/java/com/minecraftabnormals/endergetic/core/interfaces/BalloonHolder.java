package com.minecraftabnormals.endergetic.core.interfaces;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;

import java.util.List;

public interface BalloonHolder {
	List<BolloomBalloonEntity> getBalloons();

	void attachBalloon(BolloomBalloonEntity balloon);

	void detachBalloon(BolloomBalloonEntity balloonEntity);

	void detachBalloons();
}