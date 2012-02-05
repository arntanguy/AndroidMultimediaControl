package dbus.mpris;

import org.freedesktop.MediaPlayer;
import org.freedesktop.MediaPlayer.TrackChange;
import org.freedesktop.dbus.DBusSigHandler;

public class TrackChangeHandler implements DBusSigHandler<MediaPlayer.TrackChange> {
	@Override
	public void handle(TrackChange arg0) {
		// TODO Auto-generated method stub
		System.out.println("Track changed ");
	}
}