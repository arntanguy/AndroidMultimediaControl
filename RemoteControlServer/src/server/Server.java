package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.freedesktop.dbus.exceptions.DBusException;

/**
 * This class links the server with the client. It is charged with establishing
 * the connections, parsing the commands, and interacting with the multimedia
 * players.
 * 
 * @author TANGUY Arnaud
 * 
 */

public class Server {

	// port number should be more than 1024
	public int PORT = 4242;


	private ServerSocket sersock = null;
	private Socket sock = null;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public Server(int port)    {
		PORT = port;
		
		System.out.println("======================= Server ======================");

		// Initializing the ServerSocket
		try {
            sersock = new ServerSocket(PORT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println("impossible de lancer le serveur");
            System.exit(-1);
        }
	}

	public Server() throws DBusException {
		this(4242);
	}

	/**
	 * Initialize a Server socket, then wait for a client to connect. Create the
	 * link to DBUS.
	 * 
	 * @throws IOException
	 * @throws DBusException
	 */
	public void waitConnect() {

		// makes a socket connection to particular client after
		// which two way communication take place
		
		while (true)  {
		    try {
                new ServerThreadConnexion(sersock.accept(), this);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.err.println("Impossible d'accepter une nouvelle connexion");
                e.printStackTrace();
            }
		}

		
	}

	public static void main(String[] args) {
        // Initialiser le serveur
        Server serv = new Server(4242);
        
        serv.waitConnect();
        
    }


} // Server class
