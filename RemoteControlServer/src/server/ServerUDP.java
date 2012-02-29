package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerUDP extends Thread {
	private static final int TIMEOUT_RECEPTION_REPONSE = 10000;
	private DatagramSocket socket;

	public ServerUDP(int port) {
		// Construire la socket réseau UDP
		try {
			// On essaye de créer notre socket serveur UDP
			socket = new DatagramSocket(port);
			socket.setSoTimeout(TIMEOUT_RECEPTION_REPONSE);
			this.start();
		} catch (SocketException se) {
			System.err.println("Impossible de lancer le serveur UDP");
			se.printStackTrace();
			return;
		}
	}

	@Override
	public void run() {
		// On initialise les trames qui vont servir à recevoir et envoyer les
		// paquets
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];

		// Tant qu'on est connecté, on attend une requête et on y répond
		while (socket != null && !socket.isClosed()) {
			try {
				DatagramPacket paquetRecu = new DatagramPacket(receiveData,
						receiveData.length);
				try {
					socket.receive(paquetRecu);
				} catch (Exception e) {
					System.out.println("No UDP request recieved");
				}
				String requete = new String(paquetRecu.getData());
				InetAddress IPAddress = paquetRecu.getAddress();
				int port = paquetRecu.getPort();
				// Si on reçoit un "ping", on répond "pong" à celui qui nous l'a
				// envoyé
				if (requete.contains("Ping")) {
					System.out
							.println("Requesting package from server to get its IP");
					sendData = "Pong".getBytes();
					DatagramPacket paquetRetour = new DatagramPacket(sendData,
							sendData.length, IPAddress, port);
					socket.send(paquetRetour);
					socket.close();
				}
			} catch (Exception e) {
				System.err.println("Erreur de communication UDP");
				e.printStackTrace();
			}
		}

	}

	public void closeUDP() {
		socket.close();

		System.out.println("Serveur UDP fermé");
	}
}
