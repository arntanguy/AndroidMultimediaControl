package dbus.mpris;

import java.util.HashMap;
import java.util.Map;

import media.MetaData;
import media.TrackList;

import org.freedesktop.MPRISStatus;
import org.freedesktop.MediaPlayer;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

import server.ServerThreadConnexion;
import dbus.DBus;

/**
 * Implementation of the MPRIS dbus standard. This standard is meant to
 * uniformize the use of dbus through the multimedia applications implementing
 * the protocol. Thus, this class will work with a lot of players, such as VLC,
 * Quodlibet, Clementine, and many others.
 * 
 * @author TANGUY Arnaud
 * 
 */
public class DBusMPRIS extends DBus {
	private MediaPlayer mediaPlayer;
	private MediaPlayer trackList;
	private TrackChangeHandler handler;
	private StatusChangeHandler statusHandler;
	private final static String trackListObjectPath = "/TrackList";
	
	public DBusMPRIS(ServerThreadConnexion serverThreadConnexion) {
		super(serverThreadConnexion);
		objectPath = "/Player";
		serviceBusName = "org.mpris.vlc";
		handler = new TrackChangeHandler(server);
		statusHandler = new StatusChangeHandler(server);
	}

	/**
	 * Start playing when stopped Pause when playing Play when in pause
	 */
	@Override
	public void togglePlayPause() {
		System.out.println("DBUS Play");
		// If stopped, play
		if (mediaPlayer.GetStatus().playingStatus == 2) {
			mediaPlayer.Play();

		} else { // Toggle play/pause
			mediaPlayer.Pause();
		}
	}

	/**
	 * Pause if playing, do nothing otherwise
	 */
	@Override
	public void pause() {
		if (mediaPlayer.GetStatus().playingStatus == 0) {
			mediaPlayer.Pause();
		}
	}

	@Override
	public void next() {
		mediaPlayer.Next();
	}

	@Override
	public void previous() {
		mediaPlayer.Prev();
	}

	@Override
	public void connect() throws DBusException {
		try {
			conn = DBusConnection.getConnection(DBusConnection.SESSION);
			mediaPlayer = (MediaPlayer) conn.getRemoteObject(serviceBusName,
					objectPath);
			trackList = (MediaPlayer) conn.getRemoteObject(serviceBusName,
					trackListObjectPath);
			conn.addSigHandler(MediaPlayer.TrackChange.class, handler);
			conn.addSigHandler(MediaPlayer.StatusChange.class, statusHandler);
		} catch (DBusException e) {
			connected = false;
			throw e;
		}
		connected = true;
	}

	@Override
	public void setVolume(int value) {
		if (value == 0)
			return;
		int volume = mediaPlayer.VolumeGet();
		System.out.println("Volume == " + volume);
		if (volume + value > 100) {
			volume = 100;
		} else if (volume + value < 0) {
			volume = 0;
		} else {
			volume = volume + value;
		}
		mediaPlayer.VolumeSet(volume);
	}

	@Override
	public void disconnect() {
		conn.disconnect();
	}

	@Override
	public void setPosition(int pos) {
		mediaPlayer.PositionSet(pos);
	}

	@Override
	public int getPosition() {
		return mediaPlayer.PositionGet();
	}

	@Override
	public int getTotalLenght() {
		// TODO Auto-generated method stub
		return 0;
	}

	public MetaData getMetaData() {
		Map<String, Variant> dmap = mediaPlayer.GetMetadata();
		Map<String, String> map = new HashMap<String, String>(dmap.size());
		for (String key : dmap.keySet()) {
			map.put(key, dmap.get(key).getValue().toString());
		}
		return new MetaData(map);
	}

	public MPRISStatus getStatus() {
		return mediaPlayer.GetStatus();
	}
	
	public int addTrack(String uri, boolean playImmediatly) {
		return trackList.AddTrack(uri, playImmediatly);
	}
	
	public void DelTrack(int a) {
			trackList.DelTrack(a);
	}
	
	public MetaData getMetaData(int a) {
		Map<String, Variant> dmap = trackList.GetMetadata(a);
		Map<String, String> map = new HashMap<String, String>(dmap.size());
		for (String key : dmap.keySet()) {
			map.put(key, dmap.get(key).getValue().toString());
		}
		return new MetaData(map);
	}

	public int getCurrentTrack() {
		return trackList.GetCurrentTrack();
	}

	public int getLength() {
		return trackList.GetLength();
	}

	public void setLoop(boolean a) {
		trackList.SetLoop(a);
	}

	public void setRandom(boolean a) {
		trackList.SetRandom(a);
	}
	
	public TrackList getTrackList() {
		TrackList t = new TrackList();
		for(int i=1; i<getLength(); i++) {
			t.addTrack(getMetaData(i));
		}
		return t;
	}

}
