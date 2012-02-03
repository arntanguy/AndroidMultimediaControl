/**
 *
 * @author sourcemorph
 */

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.MediaPlayer;

public class DBus {

	// dbus-send --print-reply --session --dest=org.mpris.vlc /Player
	// org.freedesktop.MediaPlayer.Pause

	private static final String ObjectPath = "/Player";
	private static final String ServiceBusName = "org.mpris.vlc";
	private static DBusConnection conn = null;
	MediaPlayer mediaPlayer;

	public DBus() throws DBusException {

		System.out.println("DBUS");
		conn = DBusConnection.getConnection(DBusConnection.SESSION);
		mediaPlayer = (MediaPlayer) conn.getRemoteObject(ServiceBusName,
				ObjectPath);
		conn.disconnect();
	}

	public void play() {		
		mediaPlayer.Pause();
	}

	public void next() {
		mediaPlayer.Next();
	}

	public void previous() {
		mediaPlayer.Prev();
	}
}