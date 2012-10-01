package dbus.mpris2;

import general.ApplicationControlInterface;

import java.util.HashMap;
import java.util.Map;

import media.MetaData;
import media.TrackList;

import org.freedesktop.DBus.Properties;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
import org.mpris.MediaPlayer2.Player;
import org.mpris.MediaPlayer2.Playlists;

import player.Status;
import server.ServerThreadConnexion;
import dbus.DBus;

public class DBusMPRIS2 extends DBus implements ApplicationControlInterface {
    protected Player mediaPlayer;
    protected Properties mediaProperties;
    protected SeekedHandler seekedHandler;
    protected PlaylistChangeHandler playlistChangeHandler;

    protected String playerPropertiesInterface;

    public DBusMPRIS2(ServerThreadConnexion serverThreadConnexion) {
        super(serverThreadConnexion);
    }

    @Override
    public void connect() throws DBusException {
        try {
            conn = DBusConnection.getConnection(DBusConnection.SESSION);
            mediaPlayer = (Player) conn.getRemoteObject(serviceBusName,
                    playerPath);
            // signal sender=:1.77 -> dest=(null destination) serial=266
            // path=/org/mpris/MediaPlayer2;
            // interface=org.freedesktop.DBus.Properties;
            // member=PropertiesChanged
            mediaProperties = (Properties) conn.getRemoteObject(serviceBusName,
                    playerPath);

            if (server != null) {
                PropertiesHandler pHandler = new PropertiesHandler();

                playlistChangeHandler = new PlaylistChangeHandler(server);
                seekedHandler = new SeekedHandler(server);
                // conn.addSigHandler(Player.TrackChange.class, handler);
                // conn.addSigHandler(Player.StatusChange.class, statusHandler);
                conn.addSigHandler(Playlists.PlaylistChanged.class,
                        playlistChangeHandler);
                conn.addSigHandler(Player.Seeked.class, seekedHandler);
                conn.addSigHandler(org.freedesktop.PropertiesChangedSignal.PropertiesChanged.class, pHandler);
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
        System.out.println("Set volume");
        mediaProperties.Set(playerPropertiesInterface, "Volume", 1);
    }

    @Override
    public void setPosition(int pos) {
        mediaPlayer.Seek(pos);
    }

    @Override
    public int getPosition() {
        System.out.println("Position: "
                + mediaProperties.Get(playerPropertiesInterface, "Position"));
        return Integer.parseInt(mediaProperties.Get(playerPropertiesInterface,
                "Position").toString());
    }

    protected Map<String, String> getMetaDataMap() {
        Map<String, Variant> tdmap = mediaProperties
                .GetAll(playerPropertiesInterface);
        for (String key : tdmap.keySet()) {
            System.out.println(key + ": " + tdmap.get(key).getValue());
        }
        Variant value = tdmap.get("Metadata");
        Map<String, Variant> dmap = (Map<String, Variant>) value.getValue();

        Map<String, String> map = new HashMap<String, String>(dmap.size());
        for (String key : dmap.keySet()) {
            map.put(key.toString(), dmap.get(key).getValue().toString());
            System.out.println("Key: " + key.toString() + " Value: "
                    + dmap.get(key).toString());
        }
        return map;
    }

    @Override
    public MetaData getMetaData() {
        return new MetaData(getMetaDataMap());
    }

    @Override
    public MetaData getMetaData(int a) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Status getStatus() {
        // TODO Auto-generated method stub
        System.out.println("######## Status : "
                + mediaProperties.Get(playerPropertiesInterface,
                        "PlaybackStatus"));
        String playbackStatus = mediaProperties.Get(playerPropertiesInterface,
                "PlaybackStatus");
        Status status = new Status();
        if (playbackStatus.equals("Playing"))
            status.setPlaying();
        else if (playbackStatus.equals("Paused"))
            status.setPaused();
        else
            status.setStopped();

        String loopStatus = mediaProperties.Get(playerPropertiesInterface,
                "LoopStatus");
        /*
         * "None" if the playback will stop when there are no more tracks to
         * play "Track" if the current track will start again from the begining
         * once it has finished playing "Playlist" if the playback loops through
         * a list of tracks
         */
        if (loopStatus.equals("None"))
            status.setNotRepeating();
        else
            status.setRepeating();

        boolean shuffel = mediaProperties.Get(playerPropertiesInterface,
                "Shuffle");
        if (shuffel)
            status.setRandomPlaying();
        else
            status.setLinearPlaying();

        return status;
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
        // In metadata u'mpris:length': 261892000L,
        return getMetaData().getLength();
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
