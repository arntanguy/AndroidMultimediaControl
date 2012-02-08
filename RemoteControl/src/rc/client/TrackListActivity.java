package rc.client;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import commands.Command;
import commands.CommandWord;

public class TrackListActivity  extends ListActivity {
	private static final String TAG = "TrackListActivity";
	
	// overwrite the toString method of object to show what you want
	ArrayAdapter<String> adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Creating activity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracklist);
				
		Global.network.sendCommand(new Command(CommandWord.HELLO));
	/*		
		adapter=new ArrayAdapter<String>(this,
			    android.R.layout.simple_list_item_1,   listItems);
			setListAdapter(adapter);
*/
	}

	private void populateList() {
		
	}
}
