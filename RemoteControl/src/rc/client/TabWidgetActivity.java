package rc.client;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.Toast;

public class TabWidgetActivity extends TabActivity {
	private static final String TAG = "TabWidgetActivity";
	private TabHost tabHost;

	public static final int CONNECTTAB = 0;
	public static final int APPLICATIONTAB = 1;
	public static final int PLAYTAB = 2;
	public static final int PLAYLISTTAB = 3;

	private Intent trackListIntent = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, MediaPlayerActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		intent = new Intent().setClass(this, ConnectActivity.class);
		spec = tabHost
				.newTabSpec("server")
				.setIndicator("Server",
						res.getDrawable(R.drawable.ic_tab_network))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, ApplicationSelectorActivity.class);
		spec = tabHost
				.newTabSpec("application")
				.setIndicator("Application",
						res.getDrawable(R.drawable.ic_tab_network))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MediaPlayerActivity.class);
		spec = tabHost
				.newTabSpec("mediaplayer")
				.setIndicator("Now Playing",
						res.getDrawable(R.drawable.ic_tab_artists))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		trackListIntent = new Intent().setClass(this, TrackListActivity.class);
		spec = tabHost
				.newTabSpec("tracklist")
				.setIndicator("Playlist",
						res.getDrawable(R.drawable.ic_tab_playlist))
				.setContent(trackListIntent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

		getTabWidget().getChildAt(APPLICATIONTAB).setOnClickListener(
				new TabClickListener(APPLICATIONTAB));
		getTabWidget().getChildAt(PLAYTAB).setOnClickListener(
				new TabClickListener(PLAYTAB));
		getTabWidget().getChildAt(PLAYLISTTAB).setOnClickListener(
				new TabClickListener(PLAYLISTTAB));

	}

	private class TabClickListener implements OnClickListener {
		private int tab;

		public TabClickListener(int tab) {
			this.tab = tab;
		}

		@Override
		public void onClick(View arg0) {
			Log.i(TAG, "Tab disabled clic " + tabHost.getCurrentTab());
			if (Global.network != null && Global.network.isConnected()) {
				tabHost.setCurrentTab(tab);
			} else {
				Log.i(TAG, "Tab disabled");
				Toast.makeText(TabWidgetActivity.this,
						"You must be connected!", Toast.LENGTH_SHORT).show();

			}
		}
	}

	public void setTab(int i) {
		Log.i(TAG, "setTab(" + i + ")");
		tabHost.setCurrentTab(i);
	}
	
	/**
	 * Used to send a bundle containing the cached metaData to others applications, namely tracklist
	 * @param bundle
	 */
	public void sendMetaDataBundle(Bundle bundle) {
		trackListIntent.putExtras(bundle);
	}
}