package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * A simple UDP server used to answer to UDP broadcast Ping queries, used in
 * order to get the server IP
 * 
 * @author Arnaud TANGUY
 * 
 */
public class ServerUDP extends Thread {
    private Thread thread;
	private static final int TIMEOUT_RECEPTION_REPONSE = 10000;
	private DatagramSocket socket;
	private static final String TAG = "ServerUDP";

	public ServerUDP(int port) {
		/* Building UDP socket */
		try {
			socket = new DatagramSocket(port);
			socket.setSoTimeout(TIMEOUT_RECEPTION_REPONSE);
		    
		} catch (SocketException se) {
			System.err
					.println(TAG
							+ ": Unable to start UDP server. Auto-ip lookup will not work!");
			se.printStackTrace();
			return;
		}
	}

	/**
	 * Run the server thread, and start the network loop
	 */
	@Override
	public void run() {
		System.out.println(TAG + ": Running UDP Lookup ");

		// Initializing array used to send and receive Ping and Pong
		byte[] receiveData = new byte[4];
		byte[] sendData = new byte[4];
		DatagramPacket paquetReceived = null;

		// Wait for request
		while (socket != null && !socket.isClosed()) {
			try {
				paquetReceived = new DatagramPacket(receiveData,
						receiveData.length);
				try {
					socket.receive(paquetReceived);
				} catch (Exception e) {
				}
				if (paquetReceived != null) {
					String requete = new String(paquetReceived.getData(), 0,
							paquetReceived.getLength());
					InetAddress IPAddress = paquetReceived.getAddress();
					int port = paquetReceived.getPort();

					/*
					 * On receiving Ping, answer to the sender, so that he can
					 * get your IP ;)
					 */
					if (requete.contains("Ping")) {
						System.out.println(TAG
								+ ": Ping received from client IP: "
								+ IPAddress + "\tPort: " + port);
						sendData = "Pong".getBytes();
						DatagramPacket paquetRetour = new DatagramPacket(
								sendData, sendData.length, IPAddress, port);
						socket.send(paquetRetour);
						System.out.println(TAG + ": Pong sent to " + IPAddress);
					}
					receiveData = new byte[4];
				}
			} catch (Exception e) {
				System.err.println(TAG + ": Erreur de communication UDP");
				e.printStackTrace();
			}
		}
	}

	public void closeUDP() {
		socket.close();
		System.out.println(TAG + ": Serveur UDP fermé");
	}
}
