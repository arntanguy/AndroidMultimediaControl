package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.freedesktop.dbus.exceptions.DBusException;

import commands.Command;
import commands.CommandWord;
import commands.ErrorCommand;
import commands.MetaDataCommand;
import commands.ObjectCommand;
import commands.StatusCommand;
import dbus.mpris.DBusMPRIS;

public class ServerThreadConnexion implements Runnable  {
    private Thread tread; 
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Server serveur;
    private boolean run = true;
    private DBusMPRIS dbus = null;
    
    public ServerThreadConnexion(Socket client, Server serv) {
        this.socket = client;
        this.serveur = serv;
        
        //Initialiser et démarrer le thread en charge de la connexion avec le client
        tread = new Thread(this);
        tread.start();
    }

    @Override
    public void run() {
        System.out.println("Client Connected  :" + socket);
        
        // Initialiser le flux d'écriture 
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new String("== Hello from SerializedServer ==="));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        // Receive message from client i.e Request from client
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // TODO délocaliser dans parsecommand pour lancer le wrapper adapté
        try {
            dbus = new DBusMPRIS(this);
            dbus.connect();
        } catch (DBusException e) {
            sendCommand(new ErrorCommand(CommandWord.ERROR_DBUS_DISCONNECTED,
                    "DBUS not running", e.getMessage()));
        }
        
        // Lancer l'écoute et l'analyse des commandes
        try {
            parseCommands();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Sends a Command (or sub-class) to the client.
     * @param command
     */
    public void sendCommand(Command command) {
        try {
            oos.writeObject(command);
        } catch (IOException e) {
            System.err.println("=== Erreur de sérialization de la commande "
                    + command + "===");
            e.printStackTrace();
        }
    }
    
    /**
     * Parse commands received from the server, apply commands to the
     * application though the use of communication wrappers, encapsuling dbus,
     * mplayer api...
     * 
     * @throws IOException
     */
    public void parseCommands() throws IOException {
        System.out.println("Parsing commands");
        ObjectCommand<Integer> oc = null;
        while (run) {
            Command c = null;
            try {
                c = (Command) ois.readObject();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                c = null;
            }
            if (c != null) {
                if (!dbus.isConnected()) {
                    try {
                        dbus.connect();
                    } catch (DBusException e) {
                        System.out.println("DBUS Failed to connect");
                        System.out.println(e.getMessage());
                        sendCommand(new ErrorCommand(
                                CommandWord.ERROR_DBUS_DISCONNECTED,
                                "DBUS not running", e.getMessage()));
                    }
                }
                if (dbus.isConnected()) {
                    switch (c.getCommand()) {
                    case HELLO:
                        System.out.println("Hello from client");
                        break;
                    case PLAY:
                        System.out.println("Play");
                        dbus.togglePlayPause();
                        break;
                    case PAUSE:
                        System.out.println("Pause");
                        dbus.togglePlayPause();
                        break;
                    case GOTO_NEXT:
                        System.out.println("Next");
                        dbus.next();
                        break;
                    case GOTO_PREVIOUS:
                        System.out.println("Previous");
                        dbus.previous();
                        break;
                    case GET_POSITION:
                        sendCommand(new ObjectCommand<Integer>(
                                CommandWord.GET_POSITION, dbus.getPosition()));
                        break;
                    case SET_POSITION:
                        oc = (ObjectCommand<Integer>) c;
                        if (oc != null)
                            dbus.setPosition(oc.getObject());
                        break;
                    case MOVE:
                        oc = (ObjectCommand<Integer>) c;
                        if (oc != null)
                            dbus.setPosition(oc.getObject()
                                    + dbus.getPosition());
                        break;
                    case SET_VOLUME:
                        System.out.println("Volume");
                        oc = (ObjectCommand<Integer>) c;
                        if (oc != null)
                            dbus.setVolume(oc.getObject());
                        break;

                    case GET_META_DATA:
                        MetaDataCommand mc = new MetaDataCommand(
                                CommandWord.GET_META_DATA);
                        mc.setMetaData(dbus.getMetaData());
                        sendCommand(mc);
                        break;

                    case GET_STATUS:
                        sendCommand(new StatusCommand(CommandWord.GET_STATUS,
                                dbus.getStatus().toStatus()));
                        break;

                    case QUIT:
                        disconnect();
                        run = false;
                        break;
                    }
                }
            }
        }
        

    }
    public void disconnect() throws IOException {
        ois.close();
        oos.close();
        socket.close();
    }

}
