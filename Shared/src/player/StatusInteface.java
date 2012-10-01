package player;

public interface StatusInteface {
    public boolean isPlaying();
    public boolean isPaused();
    public boolean isStopped();
    public boolean isLinearPlaying();
    public boolean isRandomPlaying(); 
    public boolean isNotRepeating(); 
    public boolean isRepeating(); 
    public boolean stopAfterCurrent(); 
    public boolean neverStopPlaying();
    
    public void setPlaying();
    public void setPaused();
    public void setStopped();
    public void setLinearPlaying();
    public void setRandomPlaying();
    public void setRepeating();
    public void setNotRepeating();
    public void setStopAfetPlaying();
    public void setNeverStopPlayign();
    
}
