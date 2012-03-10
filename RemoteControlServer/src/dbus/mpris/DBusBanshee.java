package dbus.mpris;

import org.mpris.MediaPlayer2.Player;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

import server.ServerThreadConnexion;

//dbus-send --print-reply --session --dest=org.mpris.Player2.banshee /org/mpris/Player2 org.mpris.Player2.Player.PlayPause

public class DBusBanshee extends DBusMPRIS {
    protected Player mediaPlayer;

    public DBusBanshee() {
        this(null);
    }

    public DBusBanshee(ServerThreadConnexion serverThreadConnexion) {
        super(serverThreadConnexion);
        serviceBusName = "org.mpris.MediaPlayer2.banshee";
        playerPath = "/org/mpris/MediaPlayer2";
        trackListObjectPath = "/TrackList";
        try {
            connect();
        } catch (DBusException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connect() throws DBusException {
        try {
            conn = DBusConnection.getConnection(DBusConnection.SESSION);
            System.out.println(conn.getRemoteObject(serviceBusName, playerPath)
                    .getClass());
            mediaPlayer = (Player) conn.getRemoteObject(serviceBusName,
                    playerPath);
           // trackList = (Player) conn.getRemoteObject(serviceBusName,
           //        trackListObjectPath);
            if (server != null) {
                handler = new TrackChangeHandler(server);
                statusHandler = new StatusChangeHandler(server);
                trackListChangeHandler = new TrackListChangeHandler(server);
                //conn.addSigHandler(Player.TrackChange.class, handler);
                //conn.addSigHandler(Player.StatusChange.class, statusHandler);
                //conn.addSigHandler(Player.TrackListChange.class,
                //        trackListChangeHandler);
           
            }

        } catch (DBusException e) {
            connected = false;
            throw e;
        }
        connected = true;
    }

}
