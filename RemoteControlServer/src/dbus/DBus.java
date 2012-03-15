package dbus;

import general.ApplicationControlInterface;
import media.MetaData;
import media.TrackList;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.exceptions.DBusException;

import player.Status;

import server.ServerThreadConnexion;

/**
 * Provides an abstraction level to the various DBus underlying implementations
 * of the different softwares. It provides common functions available in all
 * multimedia players.
 * 
 * @author TANGUY Arnaud
 * 
 */
abstract public class DBus implements ApplicationControlInterface {

	// dbus-send --print-reply --session --dest=org.mpris.vlc /Player
	// org.freedesktop.MediaPlayer.Pause

    // The path to the /Player, or /TrackList
	protected String playerPath;
    protected static String trackListObjectPath;

	// The service bus name, for instance org.mpris.vlc
	protected String serviceBusName;
	protected static DBusConnection conn = null;
	protected boolean connected = false;

	protected ServerThreadConnexion server;

	protected DBusInterface mediaPlayer;
	protected DBusInterface trackList;
	
	public DBus() {		
	}
	
	public DBus(ServerThreadConnexion serverThreadConnexion) {
		server = serverThreadConnexion;
	}

	/**
	 * Start playing when stopped Pause when playing Play when in pause
	 */
	abstract public void togglePlayPause();

	/**
	 * Pause if playing, do nothing otherwise
	 */
	abstract public void pause();

	abstract public void next();

	abstract public void previous();

	abstract public void setVolume(int value);

	abstract public void connect() throws DBusException;

	public boolean isConnected() {
		return connected;
	}

	abstract public void disconnect();

	abstract public void setPosition(int pos);

	abstract public int getPosition();
	
	abstract public MetaData getMetaData();
	
	abstract public MetaData getMetaData(int a);

	abstract public Status getStatus();
	
	abstract public int addTrack(String uri, boolean playImmediatly);
	
	abstract public void DelTrack(int a);
	
	abstract public int getCurrentTrack();
	
	abstract public void setTrack(int nb);

	abstract public int getLength();

	abstract public void setLoop(boolean a);

	abstract public void setRandom(boolean a);
	
	abstract public TrackList getTrackList();
}