package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.freedesktop.dbus.exceptions.DBusException;

/**
 * This class links the server with the client. It is charged with establishing
 * the connections, parsing the commands, and interacting with the multimedia
 * players.
 * 
 * @author TANGUY Arnaud & LESTEL Guillaume
 * 
 */

public class Server {

    // port number should be more than 1024
    public int PORT = 4242;

    private ServerSocket sersock = null;
    
    private ServerUDP servEcoute;

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

        // Afficher l'ip du serveur
        getIps();
        
        // Runs the UDP server in a separate thread
        System.out.println("Running UPD server on port "+(port+1));
        // Initialiser et démarrer le thread en charge de la connexion avec le
        // client
        Thread thread = new Thread(new ServerUDP(port+1));
        thread.start();
    }

    public List<String> getIps(){
        List<String> ips = new ArrayList<String>();

        try{
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {  // carte reseau trouvee
                NetworkInterface interfaceN = (NetworkInterface)interfaces.nextElement(); 
                Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
                while (ienum.hasMoreElements()) {  // retourne l adresse IPv4 et IPv6
                    InetAddress ia = ienum.nextElement();
                    String adress = ia.getHostAddress().toString();

                    if( adress.length() < 16){          //On s'assure ainsi que l'adresse IP est bien IPv4
                        if (!adress.startsWith("127")) {
                            System.out.println("adresse publique du serveur sur " + interfaceN.getName() + " : " + ia.getHostAddress());
                        } 
                    }

                    ips.add(adress);        
                }
            }
        }
        catch(Exception e){
            System.out.println("pas de carte reseau");
            e.printStackTrace();
        }

        return ips;
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
