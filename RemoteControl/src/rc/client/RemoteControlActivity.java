package rc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import android.app.Activity;
import android.os.Bundle;

public class RemoteControlActivity extends Activity {
	Socket sock = null;
	PrintStream ps = null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startNetwork();
    }
    
    private void startNetwork() {
    	try {
			// to get the ip address of the server by the name

			InetAddress ip = InetAddress.getByName("192.168.42.254");

			// Connecting to the port 4242
			// Creates a socket with the server bind to it.
			sock = new Socket(ip, 4242);
			ps = new PrintStream(sock.getOutputStream());
			ps.println("say hi from client");
			ps.println("quit");

			BufferedReader is = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println(is.readLine());

		} catch (SocketException e) {
			System.out.println("SocketException " + e);
		} catch (IOException e) {
			System.out.println("IOException " + e);
		}
		// Finally closing the socket from the client side
		finally {
			try {
				sock.close();
			} catch (IOException ie) {
				System.out.println(" Close Error   :" + ie.getMessage());
			} 
		} 
    }
}