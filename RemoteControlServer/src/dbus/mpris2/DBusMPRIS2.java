package dbus.mpris2;

import java.util.HashMap;
import java.util.Map;

import general.ApplicationControlInterface;
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
            System.out.println(conn.getRemoteObject(serviceBusName, playerPath)
                    .getClass());
            mediaPlayer = (Player) conn.getRemoteObject(serviceBusName,
                    playerPath);
            //signal sender=:1.77 -> dest=(null destination) serial=266 path=/org/mpris/MediaPlayer2; interface=org.freedesktop.DBus.Properties; member=PropertiesChanged
            mediaProperties = (Properties) conn.getRemoteObject(serviceBusName,
                    playerPath);
          //  mediaProperties = (Properties) conn.getRemoteObject(serviceBusName,
          //          playerPath);
              
            // playlist = (Player) conn.getRemoteObject(serviceBusName,
            // playlistObjectPath);
            if (server != null) {
                playlistChangeHandler = new PlaylistChangeHandler(server);
                seekedHandler = new SeekedHandler(server);
                // conn.addSigHandler(Player.TrackChange.class, handler);
                // conn.addSigHandler(Player.StatusChange.class, statusHandler);
                conn.addSigHandler(Playlists.PlaylistChanged.class, playlistChangeHandler);
                conn.addSigHandler(Player.Seeked.class, seekedHandler);

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
        // TODO Auto-generated method stub
        System.out.println("Position: "+mediaProperties.Get(playerPropertiesInterface, "Position"));
        return Integer.parseInt(mediaProperties.Get(playerPropertiesInterface, "Position").toString());
    }

    @Override
    public int getTotalLenght() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {u'mpris:artUrl': u'file:///home/arnaud/.cache/media-art/album-28e74852b80d9b6e632a56f9771c0801.jpg',
 u'mpris:length': 261892000L,
 u'mpris:trackid': u'/org/bansheeproject/Banshee/Track/552643',
 u'xesam:album': u'Viva Santana! CD 2',
 u'xesam:albumArtist': [u'Santana'],
 u'xesam:artist': [u'Santana'],
 u'xesam:genre': [u'Folk-Rock'],
 u'xesam:title': u'Brotherhood',
 u'xesam:trackNumber': 1,
 u'xesam:url': u'file:///media/DATA/Musique/Santana%20Discografia%20Mp3%20320kbps/4%20Some%20Extra%20live,%20Bootlegs%20&%20Other%20albums/1988%20Viva%20Santana%20(Live)%20@320/CD2/01%20Brotherhood.mp3'}
     */
    @Override
    public MetaData getMetaData() {
    /*    Map<String, Variant> dmap = mediaProperties.Get(playerPropertiesInterface, "Metadata");
        if(dmap == null) {
            System.out.println("Metadata null!");
            return null;
        }
        Map<String, String> map = new HashMap<String, String>(dmap.size());
        for (String key : dmap.keySet()) {
            map.put(key, dmap.get(key).getValue().toString());
        }
        return new MetaData(map); */
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
