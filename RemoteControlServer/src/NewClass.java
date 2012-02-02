
/**
 *
 * @author sourcemorph
 */

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.MediaPlayer;

public class NewClass {

	// dbus-send --print-reply --session --dest=org.mpris.vlc /Player org.freedesktop.MediaPlayer.Pause

	private static final String ObjectPath = "/Player";
	private static final String ServiceBusName = "org.mpris.vlc";
	private static DBusConnection conn = null;

	public NewClass() throws DBusException {

		System.out.println("New Class");
		conn = DBusConnection.getConnection(DBusConnection.SESSION);
		MediaPlayer c = (MediaPlayer) conn.getRemoteObject(ServiceBusName, ObjectPath);
		c.Pause();
		conn.disconnect();
	}
}