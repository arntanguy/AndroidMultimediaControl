import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.freedesktop.dbus.exceptions.DBusException;

import commands.Command;
import commands.CommandParser;
import dbus.DBusMPRIS;

public class Server {

	// port number should be more than 1024
	public int PORT = 4242;

	private boolean run = true;
	private ServerSocket sersock = null;
	private Socket sock = null;
	PrintStream ios = null;
	BufferedReader is = null;
	DBusMPRIS dbus = null;

	public Server(int port) throws DBusException {
		PORT = port;
		dbus = new DBusMPRIS();
	}

	public Server() throws DBusException {
		this(4242);
		System.out.println(" Waiting for command !! ");
	}

	public void connect() throws IOException, DBusException {
		// Initialising the ServerSocket
		sersock = new ServerSocket(PORT);

		// makes a socket connection to particular client after
		// which two way communication take place
		sock = sersock.accept();

		System.out.println("Client Connected  :" + sock);

		// Send message to the client i.e Response
		ios = new PrintStream(sock.getOutputStream());
		ios.println("Hello from server");

		// Receive message from client i.e Request from client
		is = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		dbus.connect();
		System.out.println(dbus);
	}

	public void disconnect() throws IOException {
		ios.close();
		is.close();
		sock.close();
		sersock.close();
	}

	public void parseCommands() throws IOException {
		CommandParser parser = new CommandParser();
		System.out.println("Parsing commands");
		while (run) {
			String str = is.readLine();

			if (!str.isEmpty()) {
				Command c = parser.parse(str);
				if (!dbus.isConnected()) {
					try {
						dbus.connect();
					} catch (DBusException e) {
						System.out.println("DBUS Failed to connect");
						e.printStackTrace();
					}
				}
				if (dbus.isConnected()) {
					switch (c.getCommand()) {
					case PLAY:
						System.out.println("Play");
						dbus.togglePlayPause();
						break;
					case PAUSE:
						System.out.println("Pause");
						dbus.togglePlayPause();
						break;
					case NEXT:
						System.out.println("Next");
						dbus.next();
						break;
					case PREVIOUS:
						System.out.println("Previous");
						dbus.previous();
						break;
					case VOLUME:
						System.out.println("Volume");
						String o = c.getParameterValue("up");
						if (o != null) {
							dbus.setVolume(Integer.parseInt(o));
							System.out.println("up=" + o);
						}
						String d = c.getParameterValue("down");
						if (d != null) {
							dbus.setVolume(-Integer.parseInt((String) d));
						}
						break;
					case QUIT:
						System.out.println("Quit");
						ios.close();
						is.close();
						run = false;
						break;
					}
				}
			}
		}
	}
} // Server class
