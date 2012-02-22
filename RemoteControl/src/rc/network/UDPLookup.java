package rc.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

/**
 * Look for the server ip address using an UDP Broadcast. Inspired by the article
 * http://sberfini.developpez.com/tutoriaux/android/broadcast-udp/
 */
public class UDPLookup {
	private static final int TIMEOUT_RECEPTION_REPONSE = 10000;
	private int port;
	private Context mContext;

	public UDPLookup(Context applicationContext, String port) {
		mContext = applicationContext;
		try {
			this.port = Integer.parseInt(port);
		} catch (Exception e) {
			this.port = 4243;
		}
	}

	private InetAddress getAdresseBroadcast() throws UnknownHostException {
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifiManager.getDhcpInfo();

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	public String seekIP(String requete) throws Exception {
		DatagramSocket socket = new DatagramSocket(port);
		socket.setBroadcast(true);
		InetAddress broadcastAdress = getAdresseBroadcast();
		DatagramPacket packet = new DatagramPacket(requete.getBytes(),
				requete.length(), broadcastAdress, port);
		socket.send(packet);

		byte[] buf = new byte[1024];
		packet = new DatagramPacket(buf, buf.length);
		socket.setSoTimeout(TIMEOUT_RECEPTION_REPONSE);

		String monAdresse = getMonAdresseIP();
		socket.receive(packet);
		while (packet.getAddress().getHostAddress().contains(monAdresse)) {
			socket.receive(packet);
		}

		socket.close();

		return packet.getAddress().getHostAddress();
	}

	public String getMonAdresseIP() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
						return inetAddress.getHostAddress();
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}
}
