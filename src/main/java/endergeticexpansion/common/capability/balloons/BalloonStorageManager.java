package endergeticexpansion.common.capability.balloons;

public class BalloonStorageManager implements IBalloonStorage {
	
	private int balloons = 0;
	
	@Override
	public void setBalloonsTied(int balloons) {
		this.balloons = balloons;
	}

	@Override
	public int getBalloonsTied() {
		return this.balloons;
	}

	@Override
	public void incrementBalloons(int balloons) {
		this.balloons += balloons;
	}

	@Override
	public void decrementBalloons(int balloons) {
		this.balloons -= balloons;
	}

}
