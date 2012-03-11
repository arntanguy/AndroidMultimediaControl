package dbus.mpris;

import general.ApplicationControlInterface;
import media.MetaData;
import media.TrackList;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.mpris.MediaPlayer2.Player;

import player.Status;
import server.ServerThreadConnexion;
import dbus.DBus;

public class DBusMPRIS2 extends DBus implements ApplicationControlInterface {
    protected Player mediaPlayer;
    // protected MediaPlayer trackList;

    protected TrackChangeHandler handler;
    protected StatusChangeHandler statusHandler;
    protected TrackListChangeHandler trackListChangeHandler;

    public DBusMPRIS2(ServerThreadConnexion serverThreadConnexion) {
        super(serverThreadConnexion);
    }

    @Override
    public void connect() throws DBusException {
        try {
            conn = DBusConnection.getConnection(DBusConnection.SESSION);
            System.out.println(conn.getRemoteObject(serviceBusName, playerPath)
                    .getClass());
            mediaPlayer = (Player) conn.getRemoteObject(serviceBusName,
                    playerPath);
            // trackList = (Player) conn.getRemoteObject(serviceBusName,
            // trackListObjectPath);
            if (server != null) {
                handler = new TrackChangeHandler(server);
                statusHandler = new StatusChangeHandler(server);
                trackListChangeHandler = new TrackListChangeHandler(server);
                // conn.addSigHandler(Player.TrackChange.class, handler);
                // conn.addSigHandler(Player.StatusChange.class, statusHandler);
                // conn.addSigHandler(Player.TrackListChange.class,
                // trackListChangeHandler);

            }

        } catch (DBusException e) {
            connected = false;
            throw e;
        }
        System.out
                .println("DBUS link established, application has dbus support");
        connected = true;
    }

    @Override
    public void disconnect() {
        conn.disconnect();
    }

    @Override
    public void setServer(ServerThreadConnexion server) {
        super.server = server;
    }

    @Override
    public void togglePlayPause() {
        mediaPlayer.PlayPause();
    }

    @Override
    public void play() {
        mediaPlayer.Play();
    }

    @Override
    public void pause() {
        mediaPlayer.Pause();
    }

    @Override
    public void next() {
        mediaPlayer.Next();
    }

    @Override
    public void previous() {
        mediaPlayer.Previous();
    }

    @Override
    public void setVolume(int value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPosition(int pos) {
        mediaPlayer.Seek(pos);
    }

    @Override
    public int getPosition() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getTotalLenght() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public MetaData getMetaData() {
        // TODO Auto-generated method stub
        return new MetaData();
    }

    @Override
    public MetaData getMetaData(int a) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Status getStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int addTrack(String uri, boolean playImmediatly) {
        // TODO Auto-generated method stub
        mediaPlayer.OpenUri(uri);
        if (playImmediatly)
            play();
        return 0;
    }

    @Override
    public void DelTrack(int a) {
        // TODO Auto-generated method stub
    }

    @Override
    public int getCurrentTrack() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTrack(int nb) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setLoop(boolean a) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setRandom(boolean a) {
        // TODO Auto-generated method stub

    }

    @Override
    public TrackList getTrackList() {
        // TODO Auto-generated method stub
        return new TrackList();
    }
}
