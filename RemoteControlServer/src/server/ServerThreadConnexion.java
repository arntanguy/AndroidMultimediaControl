package server;

import general.ApplicationControlInterface;
import general.ConnectedApplications;
import general.Factory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import commands.AvailableApplicationsCommand;
import commands.Command;
import commands.CommandWord;
import commands.MetaDataCommand;
import commands.ObjectCommand;
import commands.StatusCommand;
import commands.TrackListCommand;
import dbus.mpris.DBusVlc;

public class ServerThreadConnexion implements Runnable {
	private Thread tread;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private boolean run = true;

	// Interface through wich all links with applications will be handled
	private ApplicationControlInterface applicationControl = null;

	public ServerThreadConnexion(Socket client, Server serv) {
		this.socket = client;

		// Initialiser et démarrer le thread en charge de la connexion avec le
		// client
		tread = new Thread(this);
		tread.start();
	}

	@Override
	public void run() {
		System.out.println("Client Connected  :" + socket);

		// Initialiser le flux d'écriture
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new String("== Hello from SerializedServer ==="));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		// Receive message from client i.e Request from client
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Lancer l'écoute et l'analyse des commandes
		try {
			parseCommands();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sends a Command (or sub-class) to the client.
	 * 
	 * @param command
	 */
	public void sendCommand(Command command) {
		try {
			System.out.println("Sending command: "+command.getCommand().toString());
			oos.writeObject(command);
		} catch (IOException e) {
			System.err.println("=== Erreur de sérialization de la commande "
					+ command + "===");
			e.printStackTrace();
		}
	}

	/**
	 * Parse commands received from the server, apply commands to the
	 * application though the use of communication wrappers, encapsuling
	 * applicationControl, mplayer api...
	 * 
	 * @throws IOException
	 */
	public void parseCommands() throws IOException {
		System.out.println("Parsing commands");
		ObjectCommand<Integer> oc = null;
		while (run) {
			Command c = null;
			try {
				c = (Command) ois.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				c = null;
			}
			if (c != null) {
				if (applicationControl == null) {
					if (c.getCommand() == CommandWord.SET_APPLICATION) {
						System.out.println("Set application "
							+ c.getCommand().toString());
						ObjectCommand<String> appC = (ObjectCommand) c;
						applicationControl = Factory.getApplicationControl(appC
								.getObject());
						try {
							applicationControl.setServer(this);
							applicationControl.connect();
						} catch (Exception e) {
							System.err
									.println("Failed to establish connection with "
											+ appC.getObject());
							e.printStackTrace();
						}
					}
				} else {
					switch (c.getCommand()) {
					case HELLO:
						System.out.println("Hello from client");
						break;

					case GET_AVAILABLE_APPLICATIONS:
						System.out.println("Sending command get_available_applications");
						sendCommand(new AvailableApplicationsCommand(
								CommandWord.GET_AVAILABLE_APPLICATIONS,
								ConnectedApplications.getAvailable()));
						break;

					case PLAY:
						System.out.println("Play");
						applicationControl.togglePlayPause();
						break;
					case PAUSE:
						System.out.println("Pause");
						applicationControl.togglePlayPause();
						break;
					case GOTO_NEXT:
						System.out.println("Next");
						applicationControl.next();
						break;
					case GOTO_PREVIOUS:
						System.out.println("Previous");
						applicationControl.previous();
						break;
					case GET_POSITION:
						sendCommand(new ObjectCommand<Integer>(
								CommandWord.GET_POSITION,
								applicationControl.getPosition()));
						break;
					case SET_POSITION:
						oc = (ObjectCommand<Integer>) c;
						if (oc != null)
							applicationControl.setPosition(oc.getObject());
						break;
					case MOVE:
						oc = (ObjectCommand<Integer>) c;
						if (oc != null)
							applicationControl.setPosition(oc.getObject()
									+ applicationControl.getPosition());
						break;
					case SET_VOLUME:
						System.out.println("Volume");
						oc = (ObjectCommand<Integer>) c;
						if (oc != null)
							applicationControl.setVolume(oc.getObject());
						break;

					case GET_META_DATA:
						MetaDataCommand mc = new MetaDataCommand(
								CommandWord.GET_META_DATA);
						mc.setMetaData(applicationControl.getMetaData());
						sendCommand(mc);
						break;

					case GET_STATUS:
						sendCommand(new StatusCommand(CommandWord.GET_STATUS,
								applicationControl.getStatus()));
						break;

					/**
					 * Playlist commands
					 */
					case GET_TRACKLIST:
						System.out
								.println("Sending command get_tracklist to android");
						System.out.println(applicationControl.getTrackList()
								.getTrackList().toString());
						sendCommand(new TrackListCommand(
								CommandWord.GET_TRACKLIST,
								applicationControl.getTrackList()));
						break;
					case SET_TRACK:
						oc = (ObjectCommand<Integer>) c;
						applicationControl.setTrack(oc.getObject());
						break;

					case QUIT:
						disconnect();
						run = false;
						break;
					}
				}
			}
		}

	}

	public void disconnect() throws IOException {
		ois.close();
		oos.close();
		socket.close();
	}

}
