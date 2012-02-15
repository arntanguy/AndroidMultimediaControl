package dbus.mpris;

import org.freedesktop.MediaPlayer;
import org.freedesktop.MediaPlayer.TrackListChange;
import org.freedesktop.dbus.DBusSigHandler;

import server.ServerThreadConnexion;

import commands.Command;
import commands.CommandWord;

public class TrackListChangeHandler implements
		DBusSigHandler<MediaPlayer.TrackListChange> {

	private ServerThreadConnexion server;
	
	public TrackListChangeHandler(ServerThreadConnexion s) {
		server = s;
	}

	@Override
	public void handle(TrackListChange arg0) {
		server.sendCommand(new Command(CommandWord.GET_TRACKLIST));
	}
	

}
