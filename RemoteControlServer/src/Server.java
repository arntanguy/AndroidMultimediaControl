import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {

	// port number should be more than 1024

	public static final int PORT = 4242;
	private static boolean run = true;
	private static ServerSocket sersock = null;
	private static Socket sock = null;

	public static void main(String args[]) {

		System.out.println(" Wait !! ");

		try {
			// Initialising the ServerSocket
			sersock = new ServerSocket(PORT);

			// Gives the Server Details Machine name, Port number
			System.out.println("Server Started  :" + sersock);

			try {

				// makes a socket connection to particular client after
				// which two way communication take place
				sock = sersock.accept();

				System.out.println("Client Connected  :" + sock);

				// Receive message from client i.e Request from client
				BufferedReader is = null;
				try {
					is = new BufferedReader(new InputStreamReader(sock
							.getInputStream()));
				} catch (IOException e1) {
					System.out.println("Unable to read data stream ! \n"
							+ e1.getStackTrace());
					return;
				}

				while (run) {
					System.out.println("run");
					String str = is.readLine();
					System.out.println(str);
					if (str.equals("quit")) {
						run = false;
					}
				}

				// Send message to the client i.e Response
				PrintStream ios = new PrintStream(sock.getOutputStream());
				ios.println("Hello from server");
				ios.close();

			} catch (SocketException se) {
				System.out.println("Server Socket problem  " + se.getMessage());
			} catch (Exception e) {
				System.out.println("Couldn't start " + e.getMessage());
			}

			// Usage of some methods in Socket class
			System.out.println(" Connection from :  " + sock.getInetAddress());

		} catch (Exception e) {
			System.out.println("Erreur de connection.");
		}
	}

	private void quit() {
		// Close the Socket connection
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} // Server class
