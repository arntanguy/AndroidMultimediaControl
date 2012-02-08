package rc.client;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import commands.Command;
import commands.CommandWord;

public class RemoteControlActivity extends Activity {
	private Network network;

	private ImageButton previousB;
	private ImageButton nextB;
	private ToggleButton playB;
	private ImageButton forwardB;
	private ImageButton backwardB;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * XXX: Don't just hide errors, manage them !
		 */
		//network = new Network("192.168.1.137", 4242);
		network = new Network("157.169.101.67", 4242);
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
		
		// Start the command parser thread
        Thread t = new Thread(network.getCommandParser(), "CommandParser Thread");
		t.start();
        
		setContentView(R.layout.mediacontrols);
		// Warning : findViewById will only return non null views if the content
		// view is already set !!
		previousB = (ImageButton) findViewById(R.id.previousButton);
		nextB = (ImageButton) findViewById(R.id.nextButton);
		playB = (ToggleButton) findViewById(R.id.playButton);
		forwardB = (ImageButton) findViewById(R.id.forwardButton);
		backwardB = (ImageButton) findViewById(R.id.backwardsButton);


		playB.setOnClickListener(playClickListener);
		nextB.setOnClickListener(nextClickListener);
		previousB.setOnClickListener(previousClickListener);
		forwardB.setOnClickListener(forwardClickListener);
		backwardB.setOnClickListener(backwardsClickListener);
	}

	private OnClickListener playClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (playB.isChecked()) {
				network.sendCommand(new Command(CommandWord.PAUSE));
			} else {
				network.sendCommand(new Command(CommandWord.PLAY));
			}
		}
	};

	private OnClickListener nextClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			network.sendCommand(new Command(CommandWord.NEXT));
		}
	};

	private OnClickListener previousClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			network.sendCommand(new Command(CommandWord.PREVIOUS));
		}
	};
	
	private OnClickListener forwardClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Move forward 10seconds
			Command c = new Command(CommandWord.MOVE);
			c.addParameter("value", "10000");
			network.sendCommand(c);
		}
	};
	
	private OnClickListener backwardsClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Command c = new Command(CommandWord.MOVE);
			c.addParameter("value", "-10000");
			network.sendCommand(c);
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
				Command c = new Command(CommandWord.VOLUME);
				c.addParameter("up", "5");
				network.sendCommand(c);
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_DOWN) {
				System.out.println("Volume down !");

				Command c = new Command(CommandWord.VOLUME);
				c.addParameter("down", "5");
				network.sendCommand(c);
			}
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}

}