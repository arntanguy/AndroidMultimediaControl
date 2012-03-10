package org.mpris.MediaPlayer2;

import java.util.List;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.mpris.MediaPlayer2")

public interface Playlists extends DBusInterface {
    public static class PlaylistChanged extends DBusSignal {
        public final Struct2 playlist;

        public PlaylistChanged(String path, Struct2 playlist)
                throws DBusException {
            super(path, playlist);
            this.playlist = playlist;
        }
    }

    public void ActivatePlaylist(DBusInterface playlist_id);

    public List<Struct1> GetPlaylists(UInt32 index, UInt32 max_count,
            String order, boolean reverse_order);

}
