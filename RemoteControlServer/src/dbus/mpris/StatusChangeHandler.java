package dbus.mpris;

import org.freedesktop.MPRISStatus;
import org.freedesktop.MediaPlayer;
import org.freedesktop.MediaPlayer.StatusChange;
import org.freedesktop.dbus.DBusSigHandler;

import server.Server;
import server.ServerThreadConnexion;

import commands.CommandWord;
import commands.StatusCommand;

/**
 * This class is used as an handler for DBus signal fired by the player when a
 * status change occurs. Status changes are fired by actions such as putting the
 * player in pause...
 * 
 * @author TANGUY Arnaud
 * 
 */
public class StatusChangeHandler implements
		DBusSigHandler<MediaPlayer.StatusChange> {
	ServerThreadConnexion server;

	public StatusChangeHandler(ServerThreadConnexion server2) {
		server = server2;
	}

	@Override
	public void handle(StatusChange tc) {
		System.out.println("Sending command status_changed to client");
		StatusCommand c = new StatusCommand(CommandWord.STATUS_CHANGED);
		MPRISStatus status = tc.STATUS;
		c.setStatus(status.toStatus());
		server.sendCommand(c);
	}
}