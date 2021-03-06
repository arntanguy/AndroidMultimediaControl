package rc.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import media.MetaData;
import player.Status;
import android.util.Log;

import commands.AvailableApplicationsCommand;
import commands.Command;
import commands.ErrorCommand;
import commands.MetaDataCommand;
import commands.ObjectCommand;
import commands.StatusCommand;
import commands.TrackListCommand;

/**
 * This class manages all network relations with the server. It serves as a link
 * between the Android application and the computer server It provides
 * activities with updates on the state of the multimedia player through the use
 * of listeners
 * 
 * @author TANGUY Arnaud
 * 
 */
public class Network {
	private final static String TAG = "Network";

	private Socket sock = null;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private String serverIp;
	private static final int DEFAULT_PORT = 4242;
	private int port;

	private CommandParser commandParser = null;

	private ArrayList<NetworkDataListener> networkDataListeners = null;

	public Network(String ip, int port) {
		serverIp = ip;
		this.port = port;
		networkDataListeners = new ArrayList<NetworkDataListener>();
	}

	public Network(String ip) {
		this(ip, DEFAULT_PORT);
	}

	public Network() {
		this("", DEFAULT_PORT);
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setIp(String ip) {
		this.serverIp = ip;
	}

	/**
	 * Create a socket with the server to bind to it Also initialise an
	 * ObjectInputStream (ois) and an ObjectOutputStream (oos) to read and write
	 * data on the socket
	 * 
	 * @throws SocketException
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public void connect() throws SocketException, IOException,
			UnknownHostException {
		// Set the adress of the server
		InetAddress ip = InetAddress.getByName(serverIp);

		// Creates a socket with the server bind to it.
		sock = new Socket(ip, port);
		ois = new ObjectInputStream(sock.getInputStream());

		oos = new ObjectOutputStream(sock.getOutputStream());

		commandParser = new CommandParser();
	}

	public void disconnect() throws IOException {
		oos.close();
		ois.close();
		sock.close();
	}

	/**
	 * Parse the command sent by the server. It will give feedback through the
	 * use of listeners (NetworkDataListener) It is meant to be used in a
	 * thread.
	 */
	public class CommandParser implements Runnable {
		private boolean run = true;
		private Command c = null;
		private MetaDataCommand metaDataC = null;
		private StatusCommand statusC = null;
		private Status status = null;
		private ErrorCommand errorC = null;
		private ObjectCommand<Integer> oc = null;
		private TrackListCommand trackListC = null;
		private AvailableApplicationsCommand availableApplicationsC = null;

		@Override
		public void run() {
			Log.i(TAG, "Starting CommandParser thread");
			while (run) {
				c = null;
				try {
					c = (Command) ois.readObject();
				} catch (Exception e) {
				}

				if (c != null) {
					Log.i(TAG, "Command recieved " + c.toString());
					switch (c.getCommand()) {
					case GET_AVAILABLE_APPLICATIONS:
						Log.i(TAG, "Available applications retrieved");
						availableApplicationsC = (AvailableApplicationsCommand) c;
						for (NetworkDataListener l : networkDataListeners) {
							l.availableApplicationsChanged(availableApplicationsC
									.getAvailable());
						}
						System.out.println(availableApplicationsC
								.getAvailable());
						break;

					case TRACK_CHANGED:
						metaDataC = (MetaDataCommand) c;
						MetaData metaData = metaDataC.getMetaData();
						Log.i(TAG, metaData.toString());
						for (NetworkDataListener l : networkDataListeners) {
							l.metaDataChanged(metaData);
							l.trackChanged();
						}
						break;
					case ERROR_DBUS_DISCONNECTED:
						errorC = (ErrorCommand) c;
						Log.e(TAG, errorC.toString());
						break;
					
					case ERROR_APPLICATION_NOT_STARTED:
					    errorC = (ErrorCommand) c;
					    Log.e(TAG, errorC.toString());
					    for (NetworkDataListener l : networkDataListeners) {
                            l.error(errorC);
                        }
					    break;

					case STATUS_CHANGED:
						statusC = (StatusCommand) c;
						status = statusC.getStatus();
						Log.i(TAG, "Status changed");
						for (NetworkDataListener l : networkDataListeners) {
							l.statusChanged(status);
						}
						break;

					case GET_POSITION:
						oc = (ObjectCommand) c;
						for (NetworkDataListener l : networkDataListeners) {
							l.timeChanged(oc.getObject());
						}
						break;

					case GET_META_DATA:
						metaDataC = (MetaDataCommand) c;
						MetaData metaD = metaDataC.getMetaData();
						for (NetworkDataListener l : networkDataListeners) {
							l.metaDataChanged(metaD);
						}
						break;
					case GET_STATUS:
						statusC = (StatusCommand) c;
						status = statusC.getStatus();
						Log.i(TAG, "Status changed");
						for (NetworkDataListener l : networkDataListeners) {
							l.statusChanged(status);
						}
						break;
					case GET_TRACKLIST:
						trackListC = (TrackListCommand) c;
						Log.i(TAG, "Command tracklist recieved");
						Log.i(TAG, trackListC.getTrackList().toString());
						for (NetworkDataListener l : networkDataListeners) {
							l.trackListChanged(trackListC.getTrackList());
						}
						break;
					}
				}
			} // while
		} // run
	} // CommandParser

	public CommandParser getCommandParser() {
		return commandParser;
	}

	/**
	 * Sends a command to the server.
	 * 
	 * @param c
	 *            The Command object (or sub-class) to be sent to the server.
	 */
	public void sendCommand(Command c) {
		if (oos != null) {
			Log.i(TAG, "Sending command " + c);
			try {
				oos.writeObject(c);
			} catch (IOException e) {
				Log.e(TAG, "Error while serializing command " + c.toString());
				e.printStackTrace();
			}
		}
	}

	public boolean isConnected() {
		return (sock != null) ? sock.isConnected() : false;
	}

	/**
	 * Adds a listener. It will be used to send feedback on recieved commands,
	 * pass data thoughout the various activities.
	 * 
	 * @param listener
	 *            The listener you want to attach.
	 */
	public void addStatusListener(NetworkDataListener listener) {
		networkDataListeners.add(listener);
	}

	/**
	 * Remove a status listener. Since Android activities can be killed whenever
	 * the system need memory space, we have to remove attached listener in
	 * order to avoid sending data to inexistent instances
	 * 
	 * @param listener
	 *            The lister to be removed
	 */
	public void removeStatusListener(NetworkDataListener listener) {
		networkDataListeners.remove(listener);
	}

	/**
	 * Lists properties of network interfaces
	 * 
	 * @author Arnaud TANGUY
	 * 
	 */
	public static class NetworkInterfacesProperties {
		/**
		 * Shows a list of all available network interfaces
		 * 
		 * @throws SocketException
		 */
		public static void Show() throws SocketException {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(networkInterfaces))
				displayInterfaceInformation(netint);
		}

		/**
		 * Display informations about current interface (such as name, IP...)
		 * 
		 * @param netint
		 *            The Network interface
		 * @throws SocketException
		 */
		public static void displayInterfaceInformation(NetworkInterface netint)
				throws SocketException {
			System.out.printf("Display name: %s\n", netint.getDisplayName());
			System.out.printf("Name: %s\n", netint.getName());
			Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
			for (InetAddress inetAddress : Collections.list(inetAddresses)) {
				System.out.printf("InetAddress: %s\n", inetAddress);
			}
			System.out.printf("\n");
		}
	}
}
