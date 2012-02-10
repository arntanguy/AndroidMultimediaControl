package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.freedesktop.dbus.exceptions.DBusException;

import commands.Command;
import commands.CommandWord;
import commands.ErrorCommand;
import commands.MetaDataCommand;
import commands.ObjectCommand;
import commands.StatusCommand;

import dbus.mpris.DBusMPRIS;

/**
 * This class links the server with the client. It is charged with establishing
 * the connections, parsing the commands, and interacting with the multimedia
 * players.
 * 
 * @author TANGUY Arnaud
 * 
 */

public class Server {

	// port number should be more than 1024
	public int PORT = 4242;

	private boolean run = true;
	private ServerSocket sersock = null;
	private Socket sock = null;
	DBusMPRIS dbus = null;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public Server(int port) throws DBusException {
		PORT = port;
		dbus = new DBusMPRIS(this);
		System.out
				.println("======================= Server ======================");
		System.out.println(" Waiting for command...");
	}

	public Server() throws DBusException {
		this(4242);
	}

	/**
	 * Initialize a Server socket, then wait for a client to connect. Create the
	 * link to DBUS.
	 * 
	 * @throws IOException
	 * @throws DBusException
	 */
	public void connect() throws IOException, DBusException {
		// Initializing the ServerSocket
		sersock = new ServerSocket(PORT);

		// makes a socket connection to particular client after
		// which two way communication take place
		sock = sersock.accept();

		System.out.println("Client Connected  :" + sock);

		// Send message to the client i.e Response
		oos = new ObjectOutputStream(sock.getOutputStream());
		oos.writeObject(new String("== Hello from SerializedServer ==="));

		// Receive message from client i.e Request from client
		ois = new ObjectInputStream(sock.getInputStream());

		try {
			dbus.connect();
		} catch (DBusException e) {
			sendCommand(new ErrorCommand(CommandWord.ERROR_DBUS_DISCONNECTED,
					"DBUS not running", e.getMessage()));
		}
	}

	public void disconnect() throws IOException {
		ois.close();
		oos.close();
		sock.close();
		sersock.close();
	}

	/**
	 * Parse commands received from the server, apply commands to the
	 * application though the use of communication wrappers, encapsuling dbus,
	 * mplayer api...
	 * 
	 * @throws IOException
	 */
	public void parseCommands() throws IOException {
		System.out.println("Parsing commands");
		ObjectCommand<Integer> oc = null;
		while (run) {
			Command c = null;
			try {
				c = (Command) ois.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				c = null;
			}
			if (c != null) {
				if (!dbus.isConnected()) {
					try {
						dbus.connect();
					} catch (DBusException e) {
						System.out.println("DBUS Failed to connect");
						System.out.println(e.getMessage());
						sendCommand(new ErrorCommand(
								CommandWord.ERROR_DBUS_DISCONNECTED,
								"DBUS not running", e.getMessage()));
					}
				}
				if (dbus.isConnected()) {
					switch (c.getCommand()) {
					case HELLO:
						System.out.println("Hello from client");
						break;
					case PLAY:
						System.out.println("Play");
						dbus.togglePlayPause();
						break;
					case PAUSE:
						System.out.println("Pause");
						dbus.togglePlayPause();
						break;
					case GOTO_NEXT:
						System.out.println("Next");
						dbus.next();
						break;
					case GOTO_PREVIOUS:
						System.out.println("Previous");
						dbus.previous();
						break;
					case GET_POSITION:
						sendCommand(new ObjectCommand<Integer>(
								CommandWord.GET_POSITION, dbus.getPosition()));
						break;
					case SET_POSITION:
						oc = (ObjectCommand<Integer>) c;
						if (oc != null)
							dbus.setPosition(oc.getObject());
						break;
					case MOVE:
						oc = (ObjectCommand<Integer>) c;
						if (oc != null)
							dbus.setPosition(oc.getObject()
									+ dbus.getPosition());
						break;
					case SET_VOLUME:
						System.out.println("Volume");
						oc = (ObjectCommand<Integer>) c;
						if (oc != null)
							dbus.setVolume(oc.getObject());
						break;

					case GET_META_DATA:
						MetaDataCommand mc = new MetaDataCommand(
								CommandWord.GET_META_DATA);
						mc.setMetaData(dbus.getMetaData());
						sendCommand(mc);
						break;

					case GET_STATUS:
						sendCommand(new StatusCommand(CommandWord.GET_STATUS,
								dbus.getStatus().toStatus()));
						break;

					case QUIT:
						disconnect();
						run = false;
						break;
					}
				}
			}
		}

	}

	/**
	 * Sends a Command (or sub-class) to the client.
	 * @param command
	 */
	public void sendCommand(Command command) {
		try {
			oos.writeObject(command);
		} catch (IOException e) {
			System.err.println("=== Erreur de sérialization de la commande "
					+ command + "===");
			e.printStackTrace();
		}
	}
} // Server class
