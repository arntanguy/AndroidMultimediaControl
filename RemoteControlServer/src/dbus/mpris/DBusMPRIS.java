package dbus.mpris;

import general.ApplicationControlInterface;

import java.util.HashMap;
import java.util.Map;

import media.MetaData;
import media.TrackList;

import org.freedesktop.MediaPlayer;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

import server.ServerThreadConnexion;
import dbus.DBus;
import player.Status;

/**
 * Implementation of the MPRIS dbus standard. This standard is meant to
 * uniformize the use of dbus through the media applications implementing
 * the protocol. Thus, this class will work with a lot of players, such as VLC,
 * Quodlibet, Clementine, and many others.
 * 
 * @author TANGUY Arnaud
 * 
 */
public class DBusMPRIS extends DBus implements ApplicationControlInterface {
    protected MediaPlayer mediaPlayer;
    protected MediaPlayer trackList;

    protected TrackChangeHandler handler;
    protected StatusChangeHandler statusHandler;
    protected TrackListChangeHandler trackListChangeHandler;

    /**
     * When using this constructor, the method setServer should be used to have
     * all the functionalities working, in particular the signals
     */
    public DBusMPRIS() {
        this(null);
    }

    public DBusMPRIS(ServerThreadConnexion serverThreadConnexion) {
        setServer(serverThreadConnexion);
    }

    @Override
    public void connect() throws DBusException {
        try {
            conn = DBusConnection.getConnection(DBusConnection.SESSION);
            System.out.println(conn.getRemoteObject(serviceBusName, playerPath)
                    .getClass());
            mediaPlayer = (MediaPlayer) conn.getRemoteObject(serviceBusName,
                    playerPath);
            trackList = (MediaPlayer) conn.getRemoteObject(serviceBusName,
                    trackListObjectPath);
            if (server != null) {
                handler = new TrackChangeHandler(server);
                statusHandler = new StatusChangeHandler(server);
                trackListChangeHandler = new TrackListChangeHandler(server);
                conn.addSigHandler(MediaPlayer.TrackChange.class, handler);
                conn.addSigHandler(MediaPlayer.StatusChange.class,
                        statusHandler);
                conn.addSigHandler(MediaPlayer.TrackListChange.class,
                        trackListChangeHandler);
            }

        } catch (DBusException e) {
            connected = false;
            throw e;
        }
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

    /**
     * Start playing when stopped Pause when playing Play when in pause
     */
    public void togglePlayPause() {
        System.out.println("DBUS Play");
        // If stopped, play
        if (mediaPlayer.GetStatus().playingStatus == 2) {
            mediaPlayer.Play();

        } else { // Toggle play/pause
            mediaPlayer.Pause();
        }
    }

    @Override
    public void play() {
        mediaPlayer.Play();
    }

    /**
     * Pause if playing, do nothing otherwise
     */
    @Override
    public void pause() {
        if (mediaPlayer.GetStatus().playingStatus == 0) {
            mediaPlayer.Pause();
        }
    }

    @Override
    public void next() {
        mediaPlayer.Next();
    }

    @Override
    public void previous() {
        mediaPlayer.Prev();
    }

    @Override
    public void setVolume(int value) {
        if (value == 0)
            return;
        int volume = mediaPlayer.VolumeGet();
        System.out.println("Volume == " + volume);
        if (volume + value > 100) {
            volume = 100;
        } else if (volume + value < 0) {
            volume = 0;
        } else {
            volume = volume + value;
        }
        mediaPlayer.VolumeSet(volume);
    }

    @Override
    public void setPosition(int pos) {
        mediaPlayer.PositionSet(pos);
    }

    @Override
    public int getPosition() {
        return mediaPlayer.PositionGet();
    }

    public MetaData getMetaData() {
        Map<String, Variant> dmap = mediaPlayer.GetMetadata();
        Map<String, String> map = new HashMap<String, String>(dmap.size());
        for (String key : dmap.keySet()) {
            map.put(key, dmap.get(key).getValue().toString());
        }
        return new MetaData(map);
    }

    public Status getStatus() {
        return mediaPlayer.GetStatus().toStatus();
    }

    public int addTrack(String uri, boolean playImmediatly) {
        return trackList.AddTrack(uri, playImmediatly);
    }

    public void DelTrack(int a) {
        trackList.DelTrack(a);
    }

    public MetaData getMetaData(int a) {
        return new MetaData(getMetaDataMap(a));
    }

    public int getCurrentTrack() {
        return trackList.GetCurrentTrack();
    }

    public int getLength() {
        return trackList.GetLength();
    }

    public void setLoop(boolean a) {
        trackList.SetLoop(a);
    }

    public void setRandom(boolean a) {
        trackList.SetRandom(a);
    }

    public TrackList getTrackList() {
        TrackList t = new TrackList();
        for (int i = 0; i < getLength(); i++) {
            t.addTrack(getMetaData(i));
        }
        return t;
    }

    @Override
    public void setTrack(int nb) {
        int current = getCurrentTrack();
        if (current > nb) {
            for (int i = 0; i < current - nb; i++) {
                mediaPlayer.Prev();
            }
        } else {
            for (int i = 0; i < nb - current; i++) {
                mediaPlayer.Next();
            }
        }
    }

    protected Map<String, String> getMetaDataMap(int a) {
        Map<String, Variant> dmap = trackList.GetMetadata(a);
        Map<String, String> map = new HashMap<String, String>(dmap.size());
        for (String key : dmap.keySet()) {
            map.put(key, dmap.get(key).getValue().toString());
        }
        return map;
    }

    @Override
    public String getCoverArtPath() {
        // TODO Auto-generated method stub
        return null;
    }

}
