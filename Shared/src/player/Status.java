package player;

import java.io.Serializable;

/**
 * Status of "Media Player" as a struct of 4 ints:
 * 
 * First integer: 0 = Playing, 1 = Paused, 2 = Stopped. 
 * Second interger: 0 = Playing linearly , 1 = Playing randomly. 
 * Third integer: 0 = Go to the next element once the current has finished 
 * playing, 1 = Repeat the current element 
 * Fourth integer: 0 = Stop playing once the last element
 * has been played, 1 = Never give up playing
 * 
 */
public class Status implements Serializable {
	private static final long serialVersionUID = 1L;
	
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
	
	public boolean isPlaying() {
		return playingStatus == 0;
	}
	public boolean isPaused() {
		return playingStatus == 1;
	}
	public boolean isStopped() {
		return playingStatus == 2;
	}
	
	public boolean isLinearPlaying() {
		return playingLinearity == 0;
	}
	public boolean isRandomPlaying() {
		return playingLinearity == 1;
	}
	
	public boolean isNotRepeating() {
		return repeatStatus == 0;
	}
	public boolean isRepeating() {
		return repeatStatus == 1;
	}
	
	public boolean stopAfterCurrent() {
		return stopStatus == 0;
	}
	public boolean neverStopPlaying() {
		return stopStatus == 1;
	}

}
