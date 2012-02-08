package dbus.mpris;

import org.freedesktop.MPRISStatus;
import org.freedesktop.MediaPlayer;
import org.freedesktop.MediaPlayer.StatusChange;
import org.freedesktop.dbus.DBusSigHandler;

import server.Server;

import commands.CommandWord;
import commands.StatusCommand;

public class StatusChangeHandler implements DBusSigHandler<MediaPlayer.StatusChange> {
	Server server;
	
	public StatusChangeHandler(Server s) {
		server = s;
	}
	
	@Override
	public void handle(StatusChange tc) {
		System.out.println("Sending command track_changed to client");
		StatusCommand c = new StatusCommand(CommandWord.STATUS_CHANGED);
		MPRISStatus status = tc.STATUS;
		c.setStatus(status.toStatus());
		server.sendCommand(c);
	}
}