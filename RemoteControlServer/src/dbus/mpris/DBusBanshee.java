package dbus.mpris;

import org.freedesktop.dbus.exceptions.DBusException;
import org.mpris.MediaPlayer2.Player;

import server.ServerThreadConnexion;

//dbus-send --print-reply --session --dest=org.mpris.Player2.banshee /org/mpris/Player2 org.mpris.Player2.Player.PlayPause

public class DBusBanshee extends DBusMPRIS2 {
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
}
