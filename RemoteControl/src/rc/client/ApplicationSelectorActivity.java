package rc.client;

import media.AvailableApplications;
import media.MetaData;
import media.TrackList;
import player.Status;
import rc.network.NetworkDataListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import commands.Command;
import commands.CommandWord;
import commands.ObjectCommand;

public class ApplicationSelectorActivity extends Activity {
	private static final String TAG = "ChoseApplicationActivity";

	private GridView gridview;
	private ImageAdapter gridviewAdapter;

	private NetworkDataListener networkHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Creating activity");

		setContentView(R.layout.choose_application);

		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
		gridview.setOnItemClickListener(itemClickListener);

		networkHandler = new NetworkDataHandler();
		Global.network.addStatusListener(networkHandler);
		Global.network.sendCommand(new Command(CommandWord.GET_AVAILABLE_APPLICATIONS));
		
		// XXX: generalize this
		// Add vlc to view
		ImageObject app = new ImageObject("vlc", R.drawable.vlc_launcher);
		gridviewAdapter = (ImageAdapter) gridview.getAdapter();
		gridviewAdapter.addItem(app);
	}


	/**
	 * NetworkDataHandler is an observer for the Network class. It will recieve
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
		}

		@Override
		public void availableApplicationsChanged(
				AvailableApplications availableApplications) {	
			Log.i(TAG, "Available applications changed: "+availableApplications.getAvailable());
		}
	}

	/**
	 * On click on an application icon, start the associated activity.
	 */
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// Log.d(TAG,"Position Clicked ["+position+"] with item id ["+id+"]");
			ImageObject app = (ImageObject) gridviewAdapter.getItem(position);
			Global.network.sendCommand(new ObjectCommand<String>(CommandWord.SET_APPLICATION, app.getName()));
			if (app.getName() == "vlc") {
				((TabWidgetActivity) ApplicationSelectorActivity.this
						.getParent()).setTab(TabWidgetActivity.PLAYTAB);
				;
			}

		}
	};
}
