package rc.network;

import java.util.Map;

import player.Status;

public interface NetworkDataListener {
	public void statusChanged(Status status);

	public void timeChanged(Integer object);

	public void metaDataChanged(Map<String, String> metaData);

	public void trackChanged();
}
