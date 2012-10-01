package dbus.mpris2;

import org.freedesktop.dbus.DBusSigHandler;

import dbus.mpris2.PropertiesChangedSignal.PropertiesChanged;

public class PropertiesHandler implements DBusSigHandler<dbus.mpris2.PropertiesChangedSignal.PropertiesChanged>  {

	@Override
	public void handle(PropertiesChanged arg0) {
		// TODO Auto-generated method stub
		System.out.println("SIGNAL RECIEVED!!! AT LAST");
	}

}
