package dbus.mpris2;

import org.freedesktop.PropertiesChangedSignal.PropertiesChanged;
import org.freedesktop.dbus.DBusSigHandler;



public class PropertiesHandler implements DBusSigHandler<org.freedesktop.PropertiesChangedSignal.PropertiesChanged>  {

	@Override
	public void handle(PropertiesChanged arg0) {
		// TODO Auto-generated method stub
		System.out.println("SIGNAL RECIEVED!!! AT LAST");
	}

}
