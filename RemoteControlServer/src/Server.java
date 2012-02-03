import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {

	// port number should be more than 1024
	public int PORT = 4242;
	
	private boolean run = true;
	private ServerSocket sersock = null;
	private Socket sock = null;
	PrintStream ios = null;
	BufferedReader is = null;
	
	public Server(int port) {
		PORT = port;
	}
	
	public Server() {
		this(4242);
		System.out.println(" Waiting for command !! ");
			
			// Gives the Server Details Machine name, Port number
			System.out.println("Server Started  :" + sersock);
			
				
	}
	
	public void connect() throws IOException {
		// Initialising the ServerSocket
		sersock = new ServerSocket(PORT);

		// makes a socket connection to particular client after
		// which two way communication take place
		sock = sersock.accept();
		
		System.out.println("Client Connected  :" + sock);			 

		// Send message to the client i.e Response
		ios = new PrintStream(sock.getOutputStream());
		ios.println("Hello from server");
		
		// Receive message from client i.e Request from client
		is = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}
	
	public void disconnect() throws IOException {
		ios.close();
		is.close();
		sock.close();
		sersock.close();
	}
	
	public void parseCommands() throws IOException {
		CommandParser parser = new CommandParser();
		System.out.println("Parsing commands");
		while (run) { 
			String str = is.readLine();

			if (!str.isEmpty()) {
				Command c = parser.parse(str);
				switch (c.getCommand()) {
				case PLAY:
					System.out.println("Play");
					try {
						new DBus();
					} catch (Exception e) {
						System.out.println("Erreur DBUS");
						e.printStackTrace();
					}
					break;
				case PAUSE:
					System.out.println("Pause");
					break;
				case NEXT:
					System.out.println("Next");
					break;
				case PREVIOUS:
					System.out.println("Previous");
					break;
				case QUIT:
					System.out.println("Quit");
					ios.close();
					is.close();
					run = false;
					break;
				}
			}
		}
	}
} // Server class
