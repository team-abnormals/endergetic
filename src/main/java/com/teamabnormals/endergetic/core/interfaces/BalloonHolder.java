package com.teamabnormals.endergetic.core.interfaces;

import com.teamabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;

import java.util.List;

public interface BalloonHolder {
	List<BolloomBalloonEntity> getBalloons();

	void attachBalloon(BolloomBalloonEntity balloon);

	void detachBalloon(BolloomBalloonEntity balloonEntity);

	void detachBalloons();
}