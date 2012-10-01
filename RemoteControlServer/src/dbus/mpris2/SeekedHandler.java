package dbus.mpris2;

import org.freedesktop.dbus.DBusSigHandler;
import org.mpris.MediaPlayer2.Player;
import org.mpris.MediaPlayer2.Player.Seeked;

import server.ServerThreadConnexion;

public class SeekedHandler implements
DBusSigHandler<Player.Seeked> {
    private ServerThreadConnexion server;
    
    public SeekedHandler(ServerThreadConnexion server) {
        this.server = server;
        System.out.println("New seeked handler");
    }

    @Override
    public void handle(Seeked pos) {
        System.out.println("Seeked !!" + pos.position);
    }

}
