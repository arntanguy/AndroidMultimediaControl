package rc.network;

import media.AvailableApplications;
import media.MetaData;
import media.TrackList;
import player.Status;

import commands.ErrorCommand;

/**
 * The interface NetworkDataListener provides a description of functions to
 * implement to get a full description of the data changes received by the
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
	
	public void trackListChanged(TrackList trackList);
	
	public void availableApplicationsChanged(AvailableApplications availableApplications);
	
	public void error(ErrorCommand error);
}
