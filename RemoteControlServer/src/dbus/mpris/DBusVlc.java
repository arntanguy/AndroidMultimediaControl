package dbus.mpris;

import media.MetaData;
import media.MetaDataVlc;
import server.ServerThreadConnexion;

public class DBusVlc extends DBusMPRIS {

	public DBusVlc() {
		this(null);
	}

	public DBusVlc(ServerThreadConnexion serverThreadConnexion) {
		super(serverThreadConnexion);
		serviceBusName = "org.mpris.vlc";
		playerPath = "/Player";
		trackListObjectPath = "/TrackList";
	}
	
	public MetaData getMetaData(int a) {
		return new MetaDataVlc(getMetaDataMap(a));
	}
}
