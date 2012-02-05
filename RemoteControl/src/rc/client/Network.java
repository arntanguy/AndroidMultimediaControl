package rc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import commands.Command;
import commands.CommandParser;

public class Network {
	private Socket sock = null;
	private PrintStream ps = null;
	private BufferedReader is = null;
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
		is = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		System.out.println(is.readLine()); // XXX reads a line from the server
		// TODO really read, and not just
		// only the greeting message

		ps = new PrintStream(sock.getOutputStream());
	}

	public void disconnect() throws IOException {
		ps.close();
		is.close();
		sock.close();
	}

	public void sendCommand(String c) {
		if (ps != null) {
			System.out.println("Sending command " + c);
			ps.println(c);
		}
	}
}
