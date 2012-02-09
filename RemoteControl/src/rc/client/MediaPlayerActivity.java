package rc.client;

import player.Status;
import rc.network.StatusListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

import commands.Command;
import commands.CommandWord;

public class MediaPlayerActivity extends Activity {
	protected static final String TAG = "MediaPlayerActivity";

	private Button previousB;
	private Button nextB;
	private ToggleButton playB;
	private Button forwardB;
	private Button backwardB;
	private Button playListButton;

	StatusHandler statusHandler;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Start the command parser thread
		Thread t = new Thread(Global.network.getCommandParser(),
				"CommandParser Thread");
		t.start();

		setContentView(R.layout.mediacontrols);
		// Warning : findViewById will only return non null views if the content
		// view is already set !!
		previousB = (Button) findViewById(R.id.previousButton);
		nextB = (Button) findViewById(R.id.nextButton);
		playB = (ToggleButton) findViewById(R.id.playButton);
		forwardB = (Button) findViewById(R.id.forwardButton);
		backwardB = (Button) findViewById(R.id.backwardsButton);
		playListButton = (Button) findViewById(R.id.playListButton);

		playB.setOnClickListener(playClickListener);
		nextB.setOnClickListener(nextClickListener);
		previousB.setOnClickListener(previousClickListener);
		forwardB.setOnClickListener(forwardClickListener);
		backwardB.setOnClickListener(backwardsClickListener);
		playListButton.setOnClickListener(playListClickListener);
		
		statusHandler = new StatusHandler();
		Global.network.addStatusListener(statusHandler);
	}

	private OnClickListener playListClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i(TAG, "b2 pressed - about to launch sub-activity");
			Intent intent = new Intent(MediaPlayerActivity.this,
					TrackListActivity.class);
			// Next create the bundle and initialize it
			Bundle bundle = new Bundle();

			// Add the parameters to bundle as
			bundle.putString("NAME", "my name");

			bundle.putString("COMPANY", "wissen");

			// Add this bundle to the intent
			intent.putExtras(bundle);
			startActivity(intent);

			Log
					.i(TAG,
							"b2 pressed - sucessfully launched sub-activity (startSubActivity called)");
		}
	};
	private OnClickListener playClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (playB.isChecked()) {
				Global.network.sendCommand(new Command(CommandWord.PAUSE));
			} else {
				Global.network.sendCommand(new Command(CommandWord.PLAY));
			}
		}
	};

	private OnClickListener nextClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Global.network.sendCommand(new Command(CommandWord.NEXT));
		}
	};

	private OnClickListener previousClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Global.network.sendCommand(new Command(CommandWord.PREVIOUS));
		}
	};

	private OnClickListener forwardClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Move forward 10seconds
			Command c = new Command(CommandWord.MOVE);
			c.addParameter("value", "10000");
			Global.network.sendCommand(c);
		}
	};

	private OnClickListener backwardsClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Command c = new Command(CommandWord.MOVE);
			c.addParameter("value", "-10000");
			Global.network.sendCommand(c);
		}
	};
	
	private class StatusHandler implements StatusListener {
		@Override
		public void statusChanged(Status status) {
				if(status.isPaused()) {
					Log.i(TAG, "Paused");
				} else if(status.isPlaying()) {
					Log.i(TAG, "Playing");
				}
		}
		
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (action == KeyEvent.ACTION_UP) {
				System.out.println("Volume up");
				Command c = new Command(CommandWord.VOLUME);
				c.addParameter("up", "5");
				Global.network.sendCommand(c);
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_DOWN) {
				System.out.println("Volume down !");

				Command c = new Command(CommandWord.VOLUME);
				c.addParameter("down", "5");
				Global.network.sendCommand(c);
			}
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}
}