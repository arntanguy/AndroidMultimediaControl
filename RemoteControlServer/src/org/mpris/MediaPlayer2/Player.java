package org.mpris.MediaPlayer2;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.mpris.MediaPlayer2.Player")
public interface Player extends DBusInterface {
    public static class Seeked extends DBusSignal {
        public final long position;

        public Seeked(String path, long position) throws DBusException {
            super(path, position);
            this.position = position;
        }
    }

    public void Next();

    public void Previous();

    public void Pause();

    public void PlayPause();

    public void Stop();

    public void Play();

    public void Seek(long offset);

    public void SetPosition(String trackid, long position);

    public void OpenUri(String uri);

}
