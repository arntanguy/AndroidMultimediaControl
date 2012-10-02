package rc.network;

import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * This class gives basic Ping-Pong functionnality used to query a simple UDP
 * server (ServerUDP) in order to determine its IP Address
 * 
 * @author Arnaud TANGUY
 * 
 */
public class UDPLookup {
	public static final String TAG = "UDPLookup";
	public static final int RECEIVING_TIMEOUT = 1000;
	private DatagramSocket socket;
	private Context mContext;
	private int mPort;

	public UDPLookup(Context c, int p) {
		mContext = c;
		mPort = p;
	}

	public void stop() {
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DatagramPacket getServerIp() {
		try {
			// Broadcast a Ping message to try and get an host to answer
			DatagramPacket packet = sendBroadcast("Ping");
			System.out.println("Lookup done: "
					+ packet.getAddress().getHostAddress());
			return packet;
		} catch (InterruptedIOException ie) {
			Log.d("ERROR", "No server found");
			try {
				socket.close();
			} catch (Exception e2) {
			}
			ie.printStackTrace();
		} catch (Exception e) {
			Log.d("ERROR", "Verify your Wifi connection");
			try {
				socket.close();
			} catch (Exception e2) {
			}
			e.printStackTrace();
		}

		stop();
		return null;
	}

	/**
	 * Gets the multicast adress
	 * 
	 * @return Multicast adress
	 * @throws Exception
	 */
	private InetAddress getBroadcastAddress() throws Exception {
		WifiManager wifi = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	/**
	 * Send a packet on all IP range by using the UDP protocol in broadcast mode
	 * 
	 * @param data
	 *            The string to send
	 * @return The DatagramPacket of the first server to answer
	 * @throws Exception
	 */
	private DatagramPacket sendBroadcast(String data) throws Exception {
		socket = new DatagramSocket(mPort);
		socket.setBroadcast(true);
		InetAddress broadcastAdress = getBroadcastAddress();
		DatagramPacket packet = new DatagramPacket(data.getBytes(),
				data.length(), broadcastAdress, mPort);
		// Broadcast packet
		socket.send(packet);

		/*
		 * Receiving buffer
		 */
		byte[] buf = new byte[4];
		packet = new DatagramPacket(buf, buf.length);
		// Stop broadcasting
		socket.setBroadcast(false);
		socket.setSoTimeout(RECEIVING_TIMEOUT);

		byte[] receiveData = new byte[4];

		// Wait for request
		while (socket != null && !socket.isClosed()) {
			try {
				DatagramPacket packetReceived = new DatagramPacket(receiveData,
						receiveData.length);
				try {
					socket.receive(packetReceived);
				} catch (Exception e) {
				}
				String requete = new String(packetReceived.getData());
				InetAddress IPAddress = packetReceived.getAddress();
				int port = packetReceived.getPort();
				System.out.println("Packet received " + IPAddress + " " + port
						+ " " + requete);

				// If we received pong, it means than the server answered, so
				// the packet contains its IP :)
				if (requete.contains("Pong")) {
					System.out.println("Returning server ip: "
							+ packetReceived.getAddress().getHostAddress());
					stop();
					socket = null;
					return packetReceived;
				}
			} catch (Exception e) {
				System.err.println("Erreur de communication UDP");
				e.printStackTrace();
			}
		}
		stop();
		return null;
	}
}
