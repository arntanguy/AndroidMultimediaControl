package rc.client;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import rc.network.Network;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import commands.Command;
import commands.CommandWord;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";

	private Button connectB;
	private EditText ipAdressT;
	private EditText portT;

	private GridView gridview;
	private ImageAdapter gridviewAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Global.network = new Network();
		Global.network.setPort(4242);

		setContentView(R.layout.main);

		connectB = (Button) findViewById(R.id.connectButton);
		connectB.setOnClickListener(connectClickListener);

		ipAdressT = (EditText) findViewById(R.id.ipAdressEditText);
		portT = (EditText) findViewById(R.id.portEditText);

		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
		gridview.setOnItemClickListener(itemClickListener);
		// XXX: generalize this
		// Add vlc to view
		Application app = new Application("vlc", R.drawable.sample_0);
		gridviewAdapter = (ImageAdapter) gridview.getAdapter();
		gridviewAdapter.addItem(app);

		Toast.makeText(this, "Toast it !!! Roast it !", Toast.LENGTH_SHORT)
				.show();

	}

	private class ConnectNetwork extends AsyncTask<String, Integer, Void> {
		private ProgressDialog dialog = null;

		protected Void doInBackground(String... IP) {
			int nb = IP.length;
			String ip = (nb > 0) ? IP[0] : "";
			Global.network.setIp(ip);
			Global.network
					.setPort(Integer.parseInt(portT.getText().toString()));
			try {
				Global.network.connect();
			} catch (SocketException e) {
				publishProgress(0);
				Log.e(TAG, "Socket exeption\n" + e.toString());
			} catch (UnknownHostException e) {
				publishProgress(1);
				Log.e(TAG, "Unknown Host\n" + e.toString());
			} catch (IOException e) {
				publishProgress(1);
				Log.e(TAG, "IO Exception\n" + e.toString());
			}
			return null;
		}

		@SuppressWarnings("unused")
		protected void onProgressUpdate(Integer progress) {
			Log.i(TAG, "Progress");
			if (progress == 1) {
				Toast.makeText(MainActivity.this, "Network connection failed!",
						Toast.LENGTH_SHORT).show();
			} else if (progress == 2) {
				Toast.makeText(MainActivity.this, "Unkown host!",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPostExecute(Void result) {
			Log.i(TAG, "Connection finished ");

			dialog.dismiss();
			if (Global.network.isConnected()) {
				Toast.makeText(MainActivity.this, "Connected",
						Toast.LENGTH_SHORT).show();
				gridview.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(MainActivity.this, "Network connection failed",
						Toast.LENGTH_SHORT).show();
				gridview.setVisibility(View.INVISIBLE);
			}

			Global.network.sendCommand(new Command(CommandWord.HELLO));
		}

		protected void onPreExecute() {
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Connecting...");
			dialog.show();
		}
	}

	private OnClickListener connectClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Global.network.setIp(ipAdressT.getText().toString());
			Global.network
					.setPort(Integer.parseInt(portT.getText().toString()));

			new ConnectNetwork().execute(ipAdressT.getText().toString());
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// Log.d(TAG,"Position Clicked ["+position+"] with item id ["+id+"]");
			Application app = (Application) gridviewAdapter.getItem(position);
			if (app.getName() == "vlc") {
				Intent intent = new Intent(MainActivity.this,
						RemoteControlActivity.class);
				startActivity(intent);
			}

		}
	};
}
