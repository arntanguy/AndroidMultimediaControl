package rc.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import android.util.Log;

import commands.Command;
import commands.TrackChangedCommand;

public class Network {
	private final static String TAG = "Network";

	private Socket sock = null;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private String serverIp;
	private static final int DEFAULT_PORT = 4242;
	private int port;
	
	private CommandParser commandParser = null;

	public Network(String ip, int port) {
		serverIp = ip;
		this.port = port;
	}

	public Network(String ip) {
		this(ip, DEFAULT_PORT);
	}

	public Network() {
		this("192.168.1.137", DEFAULT_PORT);
	}

	public void connect() throws SocketException, IOException,
			UnknownHostException {
		// Set the adress of the server
		InetAddress ip = InetAddress.getByName(serverIp);

		// Creates a socket with the server bind to it.
		sock = new Socket(ip, port);
		ois = new ObjectInputStream(sock.getInputStream());

		try {
			System.out.println((String) ois.readObject());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // XXX reads a line from the server
		// TODO really read, and not just
		// only the greeting message
		oos = new ObjectOutputStream(sock.getOutputStream());
		
		commandParser = new CommandParser();
	}

	public void disconnect() throws IOException {
		oos.close();
		ois.close();
		sock.close();
	}

	public class CommandParser  implements Runnable {
		private boolean run = true;
		@Override
		public void run() {
			System.out.println("Run Thread");
			while (run) {
				Command c = null;
					try {
						c = (Command) ois.readObject();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				if (c != null) {
					System.out.println("Command recieved");
					switch (c.getCommand()) {
					case TRACK_CHANGED:
						TrackChangedCommand tc = (TrackChangedCommand) c;
						Map<String, String> metaData = tc.getMetaData();
						for(String key: metaData.keySet()) {
							System.out.println(key + "\t" + metaData.get(key));
						}
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
}
