package rc.client;

import media.MetaData;
import media.TrackList;
import player.Status;
import rc.network.NetworkDataListener;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import commands.Command;
import commands.CommandWord;

public class TrackListActivity extends ListActivity {
	private static final String TAG = "TrackListActivity";

	// overwrite the toString method of object to show what you want
	ArrayAdapter<String> adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Creating activity");
		super.onCreate(savedInstanceState);

	//	setContentView(R.layout.tracklist);

		Global.network.sendCommand(new Command(CommandWord.GET_TRACKLIST));

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);

	}

	/**
	 * NetworkDataHandler is an observer for the Network class. It will receive
	 * new data when available, status update on the player state, and various
	 * informations about the network.
	 */
	private class NetworkDataHandler implements NetworkDataListener {
		@Override
		public void statusChanged(Status status) {
		}

		@Override
		public void timeChanged(Integer newTime) {
		}

		@Override
		public void metaDataChanged(MetaData metaData) {
		}

		@Override
		public void trackChanged() {
		}

		@Override
		public void trackListChanged(TrackList trackList) {
			Log.i(TAG, "Track list changed "+trackList.getTrackList());
			adapter.clear();
			for (MetaData md : trackList.getTrackList()) {
				adapter.add(md.getTitleFromLocation());
			}
		}
	}
}
