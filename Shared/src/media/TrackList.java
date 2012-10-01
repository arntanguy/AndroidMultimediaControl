package media;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class TrackList implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<MetaData> trackList;

	public TrackList() {
		trackList = new ArrayList<MetaData>();
	}

	public void addTrack(MetaData md) {
		trackList.add(md);
	}

	public ArrayList<MetaData> getTrackList() {
		return trackList;
	}

	/**
	 * This is the default implementation of writeObject. Customize if
	 * necessary.
	 */
	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		aOutputStream.defaultWriteObject();
		aOutputStream.writeObject(trackList);
	}

	/**
	 * Always treat de-serialization as a full-blown constructor, by validating
	 * the final state of the de-serialized object.
	 */
	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		aInputStream.defaultReadObject();

		trackList = (ArrayList<MetaData>) aInputStream.readObject();
	}
}
