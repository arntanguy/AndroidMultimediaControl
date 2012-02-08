package dbus;

public class Status {
	protected final int playingStatus;
	protected final int playingLinearity;
	protected final int repeatStatus;
	protected final int stopStatus;

	public Status(int playingStatus, int playingLinearity, int repeatStatus, int stopStatus) {
		this.playingStatus = playingStatus;
		this.playingLinearity = playingLinearity;
		this.repeatStatus = repeatStatus;
		this.stopStatus = stopStatus;
	}
}
