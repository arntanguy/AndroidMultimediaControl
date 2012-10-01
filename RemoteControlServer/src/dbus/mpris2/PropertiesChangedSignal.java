package dbus.mpris2;

import java.util.List;
import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.freedesktop.DBus.Properties")
public interface PropertiesChangedSignal extends DBusInterface {

	public static class PropertiesChanged extends DBusSignal {
		public final Map<String, Variant> changed_properties;
		public final List<String> invalidated_properties;

		public PropertiesChanged(String path,
				Map<String, Variant> changed_properties,
				List<String> invalidated_properties) throws DBusException {
			super(path, changed_properties, invalidated_properties);
			this.changed_properties = changed_properties;
			this.invalidated_properties = invalidated_properties;
			System.out.println("Properties changed SIGNAL");
		}
	}
}
