package endergeticexpansion.common.capability.balloons;

public interface IBalloonStorage {
	
	public void setBalloonsTied(int balloons);
	public void incrementBalloons(int balloons);
	public void decrementBalloons(int balloons);
	public int getBalloonsTied();
	
}
