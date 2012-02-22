package dbus.mpris;

import media.MetaData;
import media.MetaDataQuodlibet;
import server.ServerThreadConnexion;

public class DBusQuodlibet extends DBusMPRIS {

	public DBusQuodlibet(ServerThreadConnexion serverThreadConnexion) {
		super(serverThreadConnexion);
		serviceBusName = "org.mpris.quodlibet";
	}
	
	public DBusQuodlibet() {
		super();
	}

	public MetaData getMetaData(int a) {
		return new MetaDataQuodlibet(getMetaDataMap(a));
	}
}
