package commands;

import media.TrackList;

public class TrackListCommand extends Command {
	private static final long serialVersionUID = 1L;

	private TrackList trackList;
	
	public TrackListCommand(CommandWord command, TrackList trackList) {
		super(command);
		this.trackList = trackList;
	}
	
	public TrackList getTrackList() {
		return trackList;
	}

}
