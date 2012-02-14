package rc.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

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
		gridviewAdapter = (ImageAdapter) gridview.getAdapter();
		gridviewAdapter.addItem(app);
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
			if (app.getName() == "vlc") {
				Intent intent = new Intent(ApplicationSelectorActivity.this,
						MediaPlayerActivity.class);
				startActivity(intent);
			}

		}
	};
}
