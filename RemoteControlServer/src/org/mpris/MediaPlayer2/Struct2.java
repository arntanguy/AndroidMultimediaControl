package org.mpris.MediaPlayer2;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Position;
import org.freedesktop.dbus.Struct;

public final class Struct2 extends Struct {
    @Position(0)
    public final DBusInterface a;
    @Position(1)
    public final String b;
    @Position(2)
    public final String c;

    public Struct2(DBusInterface a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}
