package com.teamabnormals.endergetic.core.interfaces;

import com.teamabnormals.endergetic.common.entity.bolloom.BolloomBalloon;

import java.util.List;

public interface BalloonHolder {
	List<BolloomBalloon> getBalloons();

	void attachBalloon(BolloomBalloon balloon);

	void detachBalloon(BolloomBalloon balloonEntity);

	void detachBalloons();
}