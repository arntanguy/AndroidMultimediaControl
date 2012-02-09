package rc.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import android.util.Log;

import commands.Command;
import commands.ErrorCommand;
import commands.MetaDataCommand;
import commands.ObjectCommand;
import commands.StatusCommand;

import player.Status;

public class Network {
	private final static String TAG = "Network";

	private Socket sock = null;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private String serverIp;
	private static final int DEFAULT_PORT = 4242;
	private int port;
	
	private CommandParser commandParser = null;

	private ArrayList<StatusListener> statusListeners = null;
	
	public Network(String ip, int port) {
		serverIp = ip;
		this.port = port;
		statusListeners = new ArrayList<StatusListener>();
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

	public class CommandParser implements Runnable {
		private boolean run = true;
		private Command c = null;
		private MetaDataCommand metaDataC = null;
		private StatusCommand statusC = null;
		private ErrorCommand errorC = null;
		private ObjectCommand<Integer> oc = null;

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
					System.out.println("Command recieved");
					switch (c.getCommand()) {
					case TRACK_CHANGED:
						metaDataC = (MetaDataCommand) c;
						Map<String, String> metaData = metaDataC.getMetaData();
						for (String key : metaData.keySet()) {
							System.out.println(key + "\t" + metaData.get(key));
						}
						break;
					case ERROR_DBUS_DISCONNECTED:
						errorC = (ErrorCommand) c;
						Log.e(TAG, errorC.toString());
						break;

					case STATUS_CHANGED:
						statusC = (StatusCommand) c;
						Status s = statusC.getStatus();
						Log.i(TAG, "Status changed");
						for(StatusListener l : statusListeners) {
							l.statusChanged(s);
						}
						break;

					case CURRENT_TIME:
						oc = (ObjectCommand<Integer>) c;
						for(StatusListener l : statusListeners) {
							l.timeChanged(oc.getObject());
						}
					case META_DATA:
						metaDataC = (MetaDataCommand) c;
						Map<String, String> metaD = metaDataC.getMetaData();
						for(StatusListener l : statusListeners) {
							l.metaDataChanged(metaD);
						}
						break;
					case STATUS:
						// MPRISStatus status;
						break;

					/*
					 * Context context = getApplicationContext(); CharSequence
					 * text = "Hello toast!"; int duration = Toast.LENGTH_SHORT;
					 * 
					 * Toast toast = Toast.makeText(context, text, duration);
					 * toast.show(); break;
					 */
					}
				}
			}
		}
	}

	public CommandParser getCommandParser() {
		return commandParser;
	}

	public void sendCommand(Command c) {
		if (oos != null) {
			System.out.println("Sending command " + c);
			try {
				oos.writeObject(c);
			} catch (IOException e) {
				Log.e(TAG, "Error while serializing command " + c.toString());
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnected() {
		return (sock!=null) ? sock.isConnected(): false;
	}

	public void addStatusListener(StatusListener listener) {
		statusListeners.add(listener);
	}

	public void removeStatusListener(StatusListener listener) {
		statusListeners.remove(listener);
	}
}
