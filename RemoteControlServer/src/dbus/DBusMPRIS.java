package dbus;

import org.freedesktop.MediaPlayer;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

public class DBusMPRIS extends DBus {
	MediaPlayer mediaPlayer;
	
	public DBusMPRIS() {
		super();
		objectPath = "/Player";
		serviceBusName = "org.mpris.vlc";
	}

	/**
	 * Start playing when stopped Pause when playing Play when in pause
	 */
	@Override
	public void togglePlayPause() {
		System.out.println("DBUS Play");
		// If stopped, play
		if (mediaPlayer.GetStatus().playingStatus == 2) {
			mediaPlayer.Play();
		} else { // Toggle play/pause
			mediaPlayer.Pause();
		}
	}

	/**
	 * Pause if playing, do nothing otherwise
	 */
	@Override
	public void pause() {
		if (mediaPlayer.GetStatus().playingStatus == 0) {
			mediaPlayer.Pause();
		}
	}

	@Override
	public void next() {
		mediaPlayer.Next();
	}

	@Override
	public void previous() {
		mediaPlayer.Prev();
	}

	@Override
	public void connect() throws DBusException {
		try {
		conn = DBusConnection.getConnection(DBusConnection.SESSION);
		mediaPlayer = (MediaPlayer) conn.getRemoteObject(serviceBusName,
				objectPath);
		} catch(DBusException e) {
			connected = false;
			throw e;
		}
		connected = true;
	}

	@Override
	public void setVolume(int value) {
		if(value == 0) return;
		int volume = mediaPlayer.VolumeGet();
		if(volume + value > 100) { 
			volume = 100;
		} else if(volume+value < 0) {
			volume = 0;
		} else {
			volume = volume + value;
		}
		mediaPlayer.VolumeSet(volume);
	}

	@Override
	public void disconnect() {
		conn.disconnect();
	}
	
	@Override
	public void setPosition(int pos) {
		mediaPlayer.PositionSet(pos);
	}
	
	@Override
	public int getPosition() {
		return 	mediaPlayer.PositionGet();
	}

	@Override
	public int getTotalLenght() {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
