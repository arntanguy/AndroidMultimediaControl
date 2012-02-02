package org.freedesktop;
import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;  
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

// dbus-send --print-reply --session --dest=org.mpris.vlc /Player org.freedesktop.MediaPlayer.Pause

@DBusInterfaceName("org.freedesktop.MediaPlayer")  
public interface MediaPlayer extends DBusInterface
{
   public static class TrackChange extends DBusSignal
   {
      public final Map<String,Variant> a;
      public TrackChange(String path, Map<String,Variant> a) throws DBusException
      {
         super(path, a);
         this.a = a;
      }
   }
   public static class StatusChange extends DBusSignal
   {
      public final Struct2 a;
      public StatusChange(String path, Struct2 a) throws DBusException
      {
         super(path, a);
         this.a = a;
      }
   }
   public static class CapsChange extends DBusSignal
   {
      public final int a;
      public CapsChange(String path, int a) throws DBusException
      {
         super(path, a);
         this.a = a;
      }
   }

  public Struct1 GetStatus();
  public void Prev();
  public void Next();
  public void Stop();
  public void Play();
  public void Pause();
  public void Repeat(boolean a);
  public void VolumeSet(int a);
  public int VolumeGet();
  public void PositionSet(int a);
  public int PositionGet();
  public Map<String,Variant> GetMetadata();
  public int GetCaps();

}
