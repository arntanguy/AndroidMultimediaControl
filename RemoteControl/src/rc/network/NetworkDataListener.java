package rc.network;

import media.MetaData;
import player.Status;

/**
 * The interface NetworkDataListener provides a description of functions to
 * implement to get a full description of the data changes recieved by the
 * Network to which they are attached.
 * 
 * @author TANGUY Arnaud
 * 
 */
public interface NetworkDataListener {
	public void statusChanged(Status status);

	public void timeChanged(Integer object);

	public void metaDataChanged(MetaData metaData);

	public void trackChanged();

}
