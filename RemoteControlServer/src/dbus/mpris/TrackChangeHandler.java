package dbus.mpris;

import java.util.HashMap;
import java.util.Map;

import media.MetaData;

import org.freedesktop.MediaPlayer;
import org.freedesktop.MediaPlayer.TrackChange;
import org.freedesktop.dbus.DBusSigHandler;

import server.Server;

import commands.CommandWord;
import commands.MetaDataCommand;

/**
 * This class is used as an handler for DBus signal fired when the player goes
 * to a different track.
 * 
 * @author TANGUY Arnaud
 * 
 */
public class TrackChangeHandler implements
		DBusSigHandler<MediaPlayer.TrackChange> {
	Server server;

	public TrackChangeHandler(Server s) {
		server = s;
	}

	@Override
	public void handle(TrackChange tc) {
		System.out.println("Sending command track_changed to client");
		MetaDataCommand c = new MetaDataCommand(CommandWord.TRACK_CHANGED);
		Map<String, String> map = new HashMap<String, String>(tc.METADATA
				.size());
		for (String key : tc.METADATA.keySet()) {
			map.put(key, tc.METADATA.get(key).getValue().toString());
		}
		c.setMetaData(new MetaData(map));
		server.sendCommand(c);
	}
}