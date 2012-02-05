package rc.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

import commands.Command;

public class Network {
	private final static String TAG="Network";
	
	private Socket sock = null;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private String serverIp;
	private static final int DEFAULT_PORT = 4242;
	private int port;

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
			System.out.println((String)ois.readObject());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // XXX reads a line from the server
		// TODO really read, and not just
		// only the greeting message
		oos = new ObjectOutputStream(sock.getOutputStream());  
	}

	public void disconnect() throws IOException {
		oos.close();
		ois.close();
		sock.close();
	}

	public void sendCommand(Command c) {
		if (oos != null) {
			System.out.println("Sending command " + c);
			try {
				oos.writeObject(c);
			} catch (IOException e) {
				Log.e(TAG, "Error while serializing command "+c.toString());
				e.printStackTrace();
			}
		}
	}
}
