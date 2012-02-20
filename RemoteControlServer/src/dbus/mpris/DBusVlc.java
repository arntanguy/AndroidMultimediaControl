package dbus.mpris;

import media.MetaData;
import media.MetaDataVlc;

import org.freedesktop.MediaPlayer;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

import server.ServerThreadConnexion;

public class DBusVlc extends DBusMPRIS {
	
	
	public DBusVlc(ServerThreadConnexion serverThreadConnexion) {
		super(serverThreadConnexion);
		serviceBusName = "org.mpris.vlc";
	}
	
	@Override
	public void connect() throws DBusException {
		try {
			conn = DBusConnection.getConnection(DBusConnection.SESSION);
			mediaPlayer = (MediaPlayer) conn.getRemoteObject(serviceBusName,
					playerPath);
			trackList = (MediaPlayer) conn.getRemoteObject(serviceBusName,
					trackListObjectPath);
			conn.addSigHandler(MediaPlayer.TrackChange.class, handler);
			conn.addSigHandler(MediaPlayer.StatusChange.class, statusHandler);
			conn.addSigHandler(MediaPlayer.TrackListChange.class,
					trackListChangeHandler);
		} catch (DBusException e) {
			connected = false;
			throw e;
		}
		connected = true;
	}

	public MetaData getMetaData(int a) {
		return new MetaDataVlc(getMetaDataMap(a));
	}
}
