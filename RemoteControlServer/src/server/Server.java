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

import dbus.mpris.DBusMPRIS;

/** SERIALIZING DATA PASSED THROUGH
 * 
 * @author user
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
	}

	public Server() throws DBusException {
		this(4242);
		System.out.println(" Waiting for command !! ");
	}

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
		} catch(DBusException e) {
			sendCommand(new ErrorCommand(CommandWord.ERROR_DBUS_DISCONNECTED, "DBUS not running", e.getMessage()));
		}
		System.out.println(dbus);
		updateClientState();
	}

	private void updateClientState() {
		/*MetaDataCommand metaDataC = new MetaDataCommand(CommandWord.META_DATA);
		metaDataC.setMetaData(dbus.getMetaData());
		sendCommand(metaDataC);
		
		StatusCommand statusC = new StatusCommand(CommandWord.STATUS, dbus.getStatus());
		sendCommand(statusC);
	*/
	}

	public void disconnect() throws IOException {
		ois.close();
		oos.close();
		sock.close();
		sersock.close();
	}

	public void parseCommands() throws IOException {
		System.out.println("Parsing commands");
		while (run) {
			Command c = null;
			try {
				c = (Command)ois.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				c = null;
			}
			if(c!=null) {
				if (!dbus.isConnected()) {
					try {
						dbus.connect();
					} catch (DBusException e) {
						System.out.println("DBUS Failed to connect");
						System.out.println(e.getMessage());
						sendCommand(new ErrorCommand(CommandWord.ERROR_DBUS_DISCONNECTED, "DBUS not running", e.getMessage()));
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
					case CURRENT_TIME:
						dbus.getPosition();
						break;
					case MOVE:
						int pos = dbus.getPosition();
						String p1 = c.getParameterValue("value");
						if(p1 != null) {
							dbus.setPosition(pos+Integer.parseInt(p1));
						}
						System.out.println(Integer.parseInt("-12"));
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
						
					case META_DATA:
						MetaDataCommand mc = new MetaDataCommand(CommandWord.META_DATA);
						mc.setMetaData(dbus.getMetaData());
						sendCommand(mc);
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

	public void sendCommand(Command command) {
		try {
			oos.writeObject(command);
		} catch (IOException e) {
			System.err.println("=== Erreur de sérialization de la commande "+command+ "===");
			e.printStackTrace();
		}  
	}
} // Server class
