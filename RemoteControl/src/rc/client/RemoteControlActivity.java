package rc.client;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
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

		/**
		 * XXX: Don't just hide errors, manage them !
		 */
		network = new Network("192.168.1.137", 4242);
		try {
			network.connect();
		} catch (UnknownHostException e) {
			System.out.println("=== Unknown host " + e.getCause());
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("=== Problem with the socket ===");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("=== IOException ===");
			e.printStackTrace();
		}

		setContentView(R.layout.mediacontrols);
		// Warning : findViewById will only return non null views if the content
		// view is already set !!
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
			if (playB.isChecked()) {
				network.sendCommand("pause");
			} else {
				network.sendCommand("play");
			}
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

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (action == KeyEvent.ACTION_UP) {
				System.out.println("Volume up");
				network.sendCommand("volume up=5");
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_DOWN) {
				System.out.println("Volume down !");
				network.sendCommand("volume down=5");
			}
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}

}