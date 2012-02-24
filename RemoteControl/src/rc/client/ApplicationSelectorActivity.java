package rc.client;

import java.lang.reflect.Field;

import media.Applications;
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
		Global.network.sendCommand(new Command(
				CommandWord.GET_AVAILABLE_APPLICATIONS));

		/*
		 * Shows an ImageView with the icon of an available application.
		 */
		gridviewAdapter = (ImageAdapter) gridview.getAdapter();
		int resID = 0;
		int disabledResId = 0;
		for (Applications app : Applications.values()) {
			try {
				Class res = R.drawable.class;
				Field field = res.getField(app.getName().toLowerCase()
						+ "_launcher");
				resID = field.getInt(null);
				field = res.getField(app.getName().toLowerCase()
						+ "_launcher_disabled");
				disabledResId = field.getInt(null);
			} catch (Exception e) {
				resID = R.drawable.vlc_launcher;
				Log.e(TAG, "Failure to get drawable id.", e);
			}

			gridviewAdapter.addItem(new ApplicationIconView(this, app, resID, disabledResId));
		}
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
			Log.i(TAG, "Available applications changed: "
					+ availableApplications.getAvailable());
			for(Applications app:availableApplications.getAvailable())  {
				gridviewAdapter.setItemEnabled(app, true);
			}
		}
	}

	/**
	 * On click on an application icon, start the associated activity.
	 */
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			ApplicationIconView app = (ApplicationIconView) gridviewAdapter.getItem(position);
			Global.network.sendCommand(new ObjectCommand<String>(
					CommandWord.SET_APPLICATION, app.getApplication().getName()));
			((TabWidgetActivity) ApplicationSelectorActivity.this.getParent())
					.setTab(TabWidgetActivity.PLAYTAB);
		}
	};
}
