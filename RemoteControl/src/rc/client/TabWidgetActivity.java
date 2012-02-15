package rc.client;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class TabWidgetActivity extends TabActivity {
	private static final String TAG="TabWidgetActivity";
	private TabHost tabHost;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    Resources res = getResources(); // Resource object to get Drawables
	    tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, MediaPlayerActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    intent = new Intent().setClass(this, ConnectActivity.class);
	    spec = tabHost.newTabSpec("songs").setIndicator("Server",
	                      res.getDrawable(R.drawable.ic_tab_network))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, MediaPlayerActivity.class);
	    spec = tabHost.newTabSpec("artists").setIndicator("Now Playing",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, TrackListActivity.class);
	    spec = tabHost.newTabSpec("albums").setIndicator("Playlist",
	                      res.getDrawable(R.drawable.ic_tab_playlist))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    

	    tabHost.setCurrentTab(0);
	}

	public void setTab(int i) {
		Log.i(TAG, "setTab("+i+")");
		tabHost.setCurrentTab(i);
	}
}