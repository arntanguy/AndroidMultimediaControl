package dbus.mpris2;

import org.freedesktop.dbus.DBusSigHandler;
import org.mpris.MediaPlayer2.Playlists;
import org.mpris.MediaPlayer2.Playlists.PlaylistChanged;

import server.ServerThreadConnexion;

public class PlaylistChangeHandler implements
        DBusSigHandler<Playlists.PlaylistChanged> {    
    ServerThreadConnexion server;

    public PlaylistChangeHandler(ServerThreadConnexion server) {
        this.server = server;
    }

    @Override
    public void handle(PlaylistChanged arg0) {
        System.out.println("Play list changed!");
    }

}
