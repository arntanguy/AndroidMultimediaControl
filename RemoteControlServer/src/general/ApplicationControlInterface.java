package general;

import player.Status;
import media.MetaData;
import media.TrackList;
import server.ServerThreadConnexion;

public interface ApplicationControlInterface {
    public void connect() throws Exception;

    public void disconnect();

    public void setServer(ServerThreadConnexion server);

    public boolean isConnected();

    /**
     * Start playing when stopped Pause when playing Play when in pause
     */
    public void togglePlayPause();

    public void play();

    /**
     * Pause if playing, do nothing otherwise
     */
    public void pause();

    public void next();

    public void previous();

    public void setVolume(int value);

    public void setPosition(int pos);

    public int getPosition();

    public MetaData getMetaData();

    public MetaData getMetaData(int a);
    
    public String getCoverArtPath();

    public Status getStatus();

    public int addTrack(String uri, boolean playImmediatly);

    public void DelTrack(int a);

    public int getCurrentTrack();

    public void setTrack(int nb);

    public int getLength();

    public void setLoop(boolean a);

    public void setRandom(boolean a);

    public TrackList getTrackList();
}
