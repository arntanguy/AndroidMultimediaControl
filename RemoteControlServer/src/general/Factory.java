package general;

import server.ServerThreadConnexion;
import dbus.mpris.DBusQuodlibet;
import dbus.mpris.DBusVlc;
import media.Applications;

public class Factory {
	private ServerThreadConnexion server;
	
	public Factory(ServerThreadConnexion server) {
		this.server = server;
	}
	
	public ApplicationControlInterface getApplicationControl(String applicationName) {
		Applications app = Applications.get(applicationName);
		switch(app) {
		case VLC:
			return new DBusVlc(server);
		case QUODLIBET:
			return new DBusQuodlibet(server);
		}
		return null;
	}
}
