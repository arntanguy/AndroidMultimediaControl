package dbus;
 

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

abstract public class DBus {

	// dbus-send --print-reply --session --dest=org.mpris.vlc /Player
	// org.freedesktop.MediaPlayer.Pause

	protected String objectPath = "/Player";
	protected String serviceBusName = "org.mpris.vlc";
	protected static DBusConnection conn = null;
	boolean connected = false;
	
	public DBus() {
	}
	/**
	 * Start playing when stopped
	 * Pause when playing
	 * Play when in pause
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
	
	abstract public int getPosition();
	
	abstract public int getTotalLenght();
}