package general;

import media.Applications;
import dbus.mpris.DBusQuodlibet;
import dbus.mpris.DBusVlc;
import dbus.mpris2.DBusBanshee;

public class Factory {	

	public static ApplicationControlInterface getApplicationControl(String applicationName) {
		return getApplicationControl(Applications.get(applicationName));
	}
	
	public static ApplicationControlInterface getApplicationControl(Applications application) {
		switch(application) {
		case VLC:
			return new DBusVlc();
		case QUODLIBET:
			return new DBusQuodlibet();
		case BANSHEE:
			return new DBusBanshee();
		}
		return null;
	}
}
