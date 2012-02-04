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

	public DBus() {
		System.out.println("DBUS");
	}

	public void play() {
		System.out.println("DBUS Play");
		mediaPlayer.Pause();
	}

	public void next() {
		mediaPlayer.Next();
	}

	public void previous() {
		mediaPlayer.Prev();
	}

	public void connect() throws DBusException {
		conn = DBusConnection.getConnection(DBusConnection.SESSION);
		mediaPlayer = (MediaPlayer) conn.getRemoteObject(ServiceBusName,
				ObjectPath);
	}

	public void disconnect() {
		conn.disconnect();
	}
}