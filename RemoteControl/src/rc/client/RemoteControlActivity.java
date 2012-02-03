package rc.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

public class RemoteControlActivity extends Activity {
	Network network;

	Button previousB;
	Button nextB;
	ToggleButton playB;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		network = new Network();
		network.connect();

		setContentView(R.layout.mediacontrols);

		previousB = (Button) findViewById(R.id.previousButton);
		nextB = (Button) findViewById(R.id.nextButton);
		playB = (ToggleButton) findViewById(R.id.playButton);

		playB.setOnClickListener(playClickListener);
		nextB.setOnClickListener(nextClickListener);
		previousB.setOnClickListener(previousClickListener);
	}

	private OnClickListener playClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			network.sendCommand("play");
		}
	};

	private OnClickListener nextClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			network.sendCommand("next");
		}
	};

	private OnClickListener previousClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			network.sendCommand("previous");
		}
	};

}