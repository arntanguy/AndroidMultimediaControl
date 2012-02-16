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

	private static final int CONNECTTAB = 0;
	private static final int PLAYTAB = 1;
	private static final int PLAYLISTTAB = 2;

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
		spec = tabHost.newTabSpec("songs").setIndicator("Server",
				res.getDrawable(R.drawable.ic_tab_network)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MediaPlayerActivity.class);
		spec = tabHost.newTabSpec("artists").setIndicator("Now Playing",
				res.getDrawable(R.drawable.ic_tab_artists)).setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, TrackListActivity.class);
		spec = tabHost.newTabSpec("albums").setIndicator("Playlist",
				res.getDrawable(R.drawable.ic_tab_playlist)).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
		// disableTabs();

		getTabWidget().getChildAt(PLAYTAB).setOnClickListener(new TabClickListener(PLAYTAB));
		getTabWidget().getChildAt(PLAYLISTTAB).setOnClickListener(new TabClickListener(PLAYLISTTAB));

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

	public void enableTabs() {
		tabHost.getTabWidget().getChildTabViewAt(PLAYTAB).setEnabled(true);
		tabHost.getTabWidget().getChildTabViewAt(PLAYLISTTAB).setEnabled(true);
	}

	public void disableTabs() {
		tabHost.getTabWidget().getChildTabViewAt(PLAYTAB).setEnabled(false);
		tabHost.getTabWidget().getChildTabViewAt(PLAYLISTTAB).setEnabled(false);
	}

	public void setTab(int i) {
		Log.i(TAG, "setTab(" + i + ")");
		tabHost.setCurrentTab(i);
	}
}