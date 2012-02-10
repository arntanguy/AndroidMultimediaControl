package rc.client;

import java.util.Map;

import media.MetaData;

import player.Status;
import rc.network.NetworkDataListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import commands.Command;
import commands.CommandWord;
import commands.ObjectCommand;

/**
 * Creates a Multimedia player activity. It will contain informations regarding
 * the file being playing, such as cover, metadata It will give access to
 * standard audio controls such as play, pause, next...
 * 
 * @author TANGUY Arnaud
 * 
 */
public class MediaPlayerActivity extends Activity {
	protected static final String TAG = "MediaPlayerActivity";

	// Need handler for callbacks to the UI thread
	private final Handler uiHandler = new Handler();

	// Handler used as a timer to show the current position in the track
	private Handler mHandler = new Handler();
	private long startTime = 0;

	private ImageView previousB;
	private ImageView nextB;
	private ImageView playB;
	private ImageView forwardB;
	private ImageView backwardB;
	private ImageView playListButton;
	private SeekBar progressBar;
	private TextView trackNameTextView;
	private TextView artistNameTextView;

	// Will handle the data updated through the network
	private NetworkDataHandler statusHandler;
	private boolean isPlaying = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mediaplayer);
		// Warning : findViewById will only return non null views if the content
		// view is already set !!
		previousB = (ImageView) findViewById(R.id.previousButton);
		nextB = (ImageView) findViewById(R.id.nextButton);
		playB = (ImageView) findViewById(R.id.playButton);
		forwardB = (ImageView) findViewById(R.id.forwardButton);
		backwardB = (ImageView) findViewById(R.id.backwardsButton);
		playListButton = (ImageView) findViewById(R.id.playListButton);
		progressBar = (SeekBar) findViewById(R.id.progressBar);
		trackNameTextView = (TextView) findViewById(R.id.trackNameTextView);
		artistNameTextView = (TextView) findViewById(R.id.artistNameTextView);

		playB.setOnClickListener(playClickListener);
		nextB.setOnClickListener(nextClickListener);
		previousB.setOnClickListener(previousClickListener);
		forwardB.setOnClickListener(forwardClickListener);
		backwardB.setOnClickListener(backwardsClickListener);
		playListButton.setOnClickListener(playListClickListener);
		progressBar.setOnSeekBarChangeListener(progressBarChangeListener);

		statusHandler = new NetworkDataHandler();
		Global.network.addStatusListener(statusHandler);

		initializePlayer();
	}

	/**
	 * Retrieve the full playing status from the running application : - Playing
	 * state - Meta data (including length) - Current position
	 */
	private void initializePlayer() {
		Global.network.sendCommand(new Command(CommandWord.GET_STATUS));
		Global.network.sendCommand(new Command(CommandWord.GET_META_DATA));
		Global.network.sendCommand(new Command(CommandWord.GET_POSITION));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Global.network.removeStatusListener(statusHandler);
	}

	/**
	 * Thread used to update the seekbar position without relying on the
	 * network, only local time.
	 */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long oldStartTime = startTime;
			startTime = System.currentTimeMillis();
			long millis = startTime - oldStartTime;
			Log.i(TAG, "Run thread " + millis);
			progressBar.setProgress((int) (progressBar.getProgress() + millis));
			// Schedule another call 1s later
			mHandler.postDelayed(this, 1000);
		}
	};

	/**
	 * Opens the playlist activity
	 */
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

	/**
	 * Toggle Play/Pause commands. Handles the switching of images between
	 * play/pause on the button
	 */
	private OnClickListener playClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Global.network.sendCommand(new Command(CommandWord.GET_POSITION));
			if (isPlaying) {
				playB.setImageResource(R.drawable.ic_media_play);
				Global.network.sendCommand(new Command(CommandWord.PAUSE));
			} else {
				playB.setImageResource(R.drawable.ic_media_pause);
				Global.network.sendCommand(new Command(CommandWord.PLAY));

			}
		}
	};

	private OnClickListener nextClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Global.network.sendCommand(new Command(CommandWord.GOTO_NEXT));
		}
	};

	private OnClickListener previousClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Global.network.sendCommand(new Command(CommandWord.GOTO_PREVIOUS));
		}
	};

	// XXX don't hardcode the forward value
	private OnClickListener forwardClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Move forward 10seconds
			Global.network.sendCommand(new ObjectCommand(CommandWord.MOVE,
					10000));
		}
	};

	// XXX don't hardcode the backwards value
	private OnClickListener backwardsClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Global.network.sendCommand(new ObjectCommand(CommandWord.MOVE,
					-10000));
		}
	};

	private void setPlayPauseRessource() {
		if (!isPlaying) {
			playB.setImageResource(R.drawable.ic_media_play);
		} else {
			playB.setImageResource(R.drawable.ic_media_pause);
		}
	}

	// Create runnable for updating ui according to the new state
	final Runnable updateStatus = new Runnable() {
		public void run() {
			setPlayPauseRessource();
		}
	};
	
	private class UpdateMetadata implements Runnable {
		private MetaData metaData = null;
		
		public UpdateMetadata(MetaData metaData) {
			this.metaData = metaData;
		}
		
		@Override
		public void run() {
			artistNameTextView.setText(metaData.getArtist());
			trackNameTextView.setText(metaData.getTitleFromLocation());
		}
	}

	/**
	 * Manages the changes on the seekbar, to update its status with the
	 * application one over the network instance.
	 */
	private OnSeekBarChangeListener progressBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			Global.network.sendCommand(new ObjectCommand<Integer>(
					CommandWord.SET_POSITION, seekBar.getProgress()));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
		}
	};

	/**
	 * NetworkDataHandler is an observer for the Network class. It will recieve
	 * new data when available, status update on the player state, and various
	 * informations about the network.
	 */
	private class NetworkDataHandler implements NetworkDataListener {
		@Override
		public void statusChanged(Status status) {
			if (status.isPaused()) {
				isPlaying = false;
				Log.i(TAG, "Paused");
				mHandler.removeCallbacks(mUpdateTimeTask);

			} else if (status.isPlaying()) {
				isPlaying = true;
				Log.i(TAG, "Playing");
				startTime = System.currentTimeMillis();
				// Remove all existing callbacks, to ensure that only our
				// own will be used
				mHandler.removeCallbacks(mUpdateTimeTask);
				mHandler.postDelayed(mUpdateTimeTask, 1000);
			}
			uiHandler.post(updateStatus);
		}

		@Override
		public void timeChanged(Integer newTime) {
			progressBar.setProgress(newTime);
		}

		@Override
		public void metaDataChanged(MetaData metaData) {
			if (metaData.getLength() != 0) {
				progressBar.setMax(metaData.getLength());
				uiHandler.post(new UpdateMetadata(metaData));
			}
		}

		@Override
		public void trackChanged() {
			startTime = System.currentTimeMillis();
			progressBar.setProgress(0);
		}
	}

	/**
	 * Used to control hardware keys, in this case the volume keys.
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (action == KeyEvent.ACTION_UP) {
				System.out.println("Volume up");
				Global.network.sendCommand(new ObjectCommand<Integer>(
						CommandWord.SET_VOLUME, 5));
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_DOWN) {
				System.out.println("Volume down !");
				Global.network.sendCommand(new ObjectCommand<Integer>(
						CommandWord.SET_VOLUME, -5));
			}
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}
}