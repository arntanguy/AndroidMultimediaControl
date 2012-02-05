package server;
import java.io.IOException;

import org.freedesktop.dbus.exceptions.DBusException;



public class ServerMain {
	private static Server server;
	
	public static void main(String args[]) {
		try {
			server = new Server();
		} catch (DBusException e1) {
			System.err.println("=== Couldn't start DBUS, shutting down... ===");
			e1.printStackTrace();
			System.exit(1);
		}
		try {
			server.connect();
		} catch (IOException e) {
			System.out.println("=== Error, Couldn't connect the server ===");
			e.printStackTrace();
			System.exit(2);
		} catch (DBusException e) {
			System.out.println("=== Error, Couldn't establish the link with DBus ===");
			e.printStackTrace();
		}
		try {
			server.parseCommands();
		} catch (IOException e) {
			System.err.println("=== Couldn't parse commands, shutting down ===");
			e.printStackTrace();
			System.exit(3);
		}
	}
}
