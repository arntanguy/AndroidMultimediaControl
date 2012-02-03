package rc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class Network {
	Socket sock = null;
	PrintStream ps = null;
	InetAddress serverIp = null;
	private int port = 4242;

	public Network() {
	}

	public void connect() {
		try {
			// Set the adress of the server
			serverIp = InetAddress.getByName("192.168.5.75");

			// Creates a socket with the server bind to it.
			sock = new Socket(serverIp, port);
			BufferedReader is = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println(is.readLine()); // XXX reads a line from the server
			System.out.println(is.readLine()); // TODO really read, and not just only the greeting message


			ps = new PrintStream(sock.getOutputStream());
		} catch (SocketException e) {
			System.out.println("SocketException " + e);
		} catch (IOException e) {
			System.out.println("IOException " + e);
		}

	}
	public void disconnect() {
		try {
			sock.close();
		} catch (IOException ie) {
			System.out.println(" Close Error   :" + ie.getMessage());
		} 
	}
	
	public void sendCommand(String c) {
		ps.println(c);
	}
	
}
