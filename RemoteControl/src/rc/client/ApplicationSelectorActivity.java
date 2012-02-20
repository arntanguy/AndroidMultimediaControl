package rc.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import commands.CommandWord;
import commands.ObjectCommand;

public class ApplicationSelectorActivity extends Activity {
	private static final String TAG = "ChoseApplicationActivity";

	private GridView gridview;
	private ImageAdapter gridviewAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Creating activity");

		setContentView(R.layout.choose_application);

		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
		gridview.setOnItemClickListener(itemClickListener);

		// XXX: generalize this
		// Add vlc to view
		ImageObject app = new ImageObject("vlc", R.drawable.vlc_launcher);
		//ImageObject app2 = new ImageObject("quodlibet", R.drawable.quodlibet_launcher);
		gridviewAdapter = (ImageAdapter) gridview.getAdapter();
		gridviewAdapter.addItem(app);
		//gridviewAdapter.addItem(app2);
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
