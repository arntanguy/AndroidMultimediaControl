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
public class Status implements Serializable, StatusInteface {
	private static final long serialVersionUID = 1L;
	
	public enum PlayingState {
	    PLAYING(0), PAUSED(1), STOPPED(2);
	    
	    private int val;
	    
	    PlayingState(int value) {
	        val = value;
	    }
	    
	    public static PlayingState fromValue(int value) {
	        if(value == PLAYING.getValue()) return PLAYING;
	        else if (value == PAUSED.getValue()) return PAUSED;
	        else return STOPPED;      
	    }
	    
	    public int getValue() {
	        return val;
	    }
	}
	
	public enum LinearityState {
        LINEAR_PLAYING(0), RANDOM_PLAYING(1),
        GO_NEXT(0), REPEAT(1),
        STOP_AFTER_CURRENT(0), NEVER_STOP(1);
        
        private int val;
        
        LinearityState(int value) {
            val = value;
        }
        
        public static LinearityState fromValue(int value) {
            if(value == LINEAR_PLAYING.getValue()) return LINEAR_PLAYING;
            else return RANDOM_PLAYING;      
        }
        
        public int getValue() {
            return val;
        }
    }
	
	public enum RepeatState {
        GO_NEXT(0), REPEAT(1);
        
        private int val;
        
        RepeatState(int value) {
            val = value;
        }
        
        public static RepeatState fromValue(int value) {
            if(value == GO_NEXT.getValue()) return GO_NEXT;
            else return REPEAT;      
        }
        
        public int getValue() {
            return val;
        }
    }
	
	public enum StopState {
        STOP_AFTER_CURRENT(0), NEVER_STOP(1);
        
        private int val;
        
        StopState(int value) {
            val = value;
        }
        
        public static StopState fromValue(int value) {
            if(value == STOP_AFTER_CURRENT.getValue()) return STOP_AFTER_CURRENT;
            else return NEVER_STOP;      
        }
        
        public int getValue() {
            return val;
        }
    }
    
	protected PlayingState playingStatus;
	protected LinearityState playingLinearity;
	protected RepeatState repeatStatus;
	protected StopState stopStatus;

	public Status() {
	}

	public Status(int playingStatus, int playingLinearity, int repeatStatus, int stopStatus) {
		this.playingStatus = PlayingState.fromValue(playingStatus);
		this.playingLinearity = LinearityState.fromValue(playingLinearity);
		this.repeatStatus = RepeatState.fromValue(repeatStatus);
		this.stopStatus = StopState.fromValue(stopStatus);
	}
	
	public boolean isPlaying() {
		return playingStatus == PlayingState.PLAYING;
	}
	public boolean isPaused() {
		return playingStatus == PlayingState.PAUSED;
	}
	public boolean isStopped() {
		return playingStatus == PlayingState.STOPPED;
	}
	
	public boolean isLinearPlaying() {
		return playingLinearity == LinearityState.LINEAR_PLAYING;
	}
	public boolean isRandomPlaying() {
		return playingLinearity == LinearityState.RANDOM_PLAYING;
	}
	
	public boolean isNotRepeating() {
		return repeatStatus == RepeatState.GO_NEXT;
	}
	public boolean isRepeating() {
		return repeatStatus == RepeatState.REPEAT;
	}
	
	public boolean stopAfterCurrent() {
		return stopStatus == StopState.STOP_AFTER_CURRENT;
	}
	public boolean neverStopPlaying() {
		return stopStatus == StopState.NEVER_STOP;
	}

    @Override
    public void setPlaying() {
        playingStatus = PlayingState.PLAYING;
    }

    @Override
    public void setPaused() {
        playingStatus = PlayingState.PAUSED;        
    }

    @Override
    public void setStopped() {
        playingStatus = PlayingState.STOPPED;        
    }

    @Override
    public void setLinearPlaying() {
        playingLinearity = LinearityState.LINEAR_PLAYING;
    }

    @Override
    public void setRandomPlaying() {
        playingLinearity = LinearityState.RANDOM_PLAYING;        
    }

    @Override
    public void setRepeating() {
        repeatStatus = RepeatState.REPEAT;
    }

    @Override
    public void setNotRepeating() {
        repeatStatus = RepeatState.GO_NEXT;
    }

    @Override
    public void setStopAfetPlaying() {
        stopStatus = StopState.STOP_AFTER_CURRENT;
    }

    @Override
    public void setNeverStopPlayign() {
        stopStatus = StopState.NEVER_STOP;
    }

}
