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
	 * Start playing when stopped
	 * Pause when playing
	 * Play when in pause
	 */
	public void togglePlayPause() {
		System.out.println("DBUS Play");
		// If stopped, play
		if(mediaPlayer.GetStatus().playingStatus == 2) {
			mediaPlayer.Play();
		} else { // Toggle play/pause
			mediaPlayer.Pause();
		}
	}
	/**
	 * Pause if playing, do nothing otherwise
	 */
	public void pause() {
		if(mediaPlayer.GetStatus().playingStatus == 0) {
			mediaPlayer.Pause();
		}
	}
	
	public void next() {
		mediaPlayer.Next();
	}

	public void previous() {
		mediaPlayer.Prev();
	}

	public void connect() throws DBusException {
		conn = DBusConnection.getConnection(DBusConnection.SESSION);
		mediaPlayer = (MediaPlayer) conn.getRemoteObject(serviceBusName,
				objectPath);
	}

	public void disconnect() {
		conn.disconnect();
	}
}
