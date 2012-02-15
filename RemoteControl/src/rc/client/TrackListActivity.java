package rc.client;

import media.MetaData;
import media.TrackList;
import player.Status;
import rc.network.NetworkDataListener;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import commands.Command;
import commands.CommandWord;
import commands.ObjectCommand;

public class TrackListActivity extends ListActivity {
	private static final String TAG = "TrackListActivity";

	// Need handler for callbacks to the UI thread
	private final Handler uiHandler = new Handler();

	// overwrite the toString method of object to show what you want
	private ArrayAdapter<MetaData> adapter;

	private NetworkDataHandler trackListHandler;

	private ProgressDialog dialog;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Creating activity");
		super.onCreate(savedInstanceState);

		Global.network.sendCommand(new Command(CommandWord.GET_TRACKLIST));
		
		dialog = new ProgressDialog(TrackListActivity.this);
		dialog.setMessage("Fetching...");
		dialog.show();

		adapter = new ArrayAdapter<MetaData>(this,
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
		

		trackListHandler = new NetworkDataHandler();
		Global.network.addStatusListener(trackListHandler);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.i(TAG, "Position: "+position);
		Global.network.sendCommand(new ObjectCommand<Integer>(CommandWord.SET_TRACK, position));
		super.onListItemClick(l, v, position, id);
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
			Log.i(TAG, "Track list changed " + trackList.getTrackList());
			uiHandler.post(new UpdateTrackList(trackList));
			dialog.dismiss();
		}

	}

	// Create runnable for updating ui according to the new state
	private class UpdateTrackList implements Runnable {
		private TrackList trackList = null;

		public UpdateTrackList(TrackList tl) {
			trackList = tl;
		}

		@Override
		public void run() {
			adapter.clear();
			for (MetaData md : trackList.getTrackList()) {
				adapter.add(md);
			}
			adapter.notifyDataSetChanged();
		}
	}
}