package dbus.mpris;

import media.MetaData;
import media.MetaDataQuodlibet;
import server.ServerThreadConnexion;

public class DBusQuodlibet extends DBusMPRIS {

	public DBusQuodlibet() {
		this(null);
	}

	public DBusQuodlibet(ServerThreadConnexion serverThreadConnexion) {
		super(serverThreadConnexion);
		serviceBusName = "org.mpris.quodlibet";
		playerPath = "/Player";
		trackListObjectPath = "/TrackList";
	}

	public MetaData getMetaData(int a) {
		return new MetaDataQuodlibet(getMetaDataMap(a));
	}
}
