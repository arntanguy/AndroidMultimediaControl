package org.freedesktop;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

// dbus-send --print-reply --session --dest=org.mpris.vlc /Player org.freedesktop.MediaPlayer.Pause

/**
 * This interface describes the capabilities of players implementing the DBUS
 * MPIS interface. This interface is meant to use most of the multimedia players
 * in the same way.
 * 
 */
@DBusInterfaceName("org.freedesktop.MediaPlayer")
public interface MediaPlayer extends DBusInterface {
	/**
	 * Signal is emitted when the "Media Player" plays another "Track". Argument
	 * of the signal is the metadata attached to the new "Track"
	 */
	public static class TrackChange extends DBusSignal {
		public final Map<String, Variant> METADATA;

		public TrackChange(String path, Map<String, Variant> METADATA)
				throws DBusException {
			super(path, METADATA);
			this.METADATA = METADATA;
		}
	}

	/**
	 * Signal is emitted when the status of the "Media Player" change. The
	 * argument has the same meaning as the value returned by GetStatus.
	 */
	public static class StatusChange extends DBusSignal {
		public final MPRISStatus a;

		public StatusChange(String path, MPRISStatus a) throws DBusException {
			super(path, a);
			this.a = a;
		}
	}

	/**
	 * Signal is emitted when the "Media Player" changes capabilities, see
	 * GetCaps method.
	 */
	public static class CapsChange extends DBusSignal {
		public final int a;

		public CapsChange(String path, int a) throws DBusException {
			super(path, a);
			this.a = a;
		}
	}

	/**
	 * Return the status of "Media Player" as a struct of 4 ints:
	 * 
	 * First integer: 0 = Playing, 1 = Paused, 2 = Stopped. Second interger: 0 =
	 * Playing linearly , 1 = Playing randomly. Third integer: 0 = Go to the
	 * next element once the current has finished playing , 1 = Repeat the
	 * current element Fourth integer: 0 = Stop playing once the last element
	 * has been played, 1 = Never give up playing
	 * 
	 * 
	 * @return MPRISStatus Class holding the status information.
	 */
	public MPRISStatus GetStatus();

	/**
	 * Goes to the previous element (what if we're at the beginning? See above)
	 */
	public void Prev();

	/**
	 * Goes to the next element (What if we're at the end? -- NOTE Nothing
	 * terrible needs to happen, the player should just ignore it. However UIs,
	 * and maybe not only UIs, can receive a hint as to whether there is a next
	 * track using the Caps API)
	 */
	public void Next();

	/**
	 * Stop playing.
	 */
	public void Stop();

	/**
	 * If playing : rewind to the beginning of current track, else : start
	 * playing.
	 */
	public void Play();

	/**
	 * If playing : pause. If paused : unpause
	 */
	public void Pause();

	/**
	 * Toggle the current track repeat.
	 * 
	 * @param a
	 *            boolean: TRUE to repeat the current track, FALSE to stop
	 *            repeating.
	 */
	public void Repeat(boolean a);

	/**
	 * VolumeSet : Sets the volume (argument must be in [0;100])
	 * 
	 * @param a
	 */
	public void VolumeSet(int a);

	/**
	 * @return Returns the current volume (must be in [0;100])
	 */
	public int VolumeGet();

	/**
	 * Sets the playing position
	 * 
	 * @param a
	 *            position : must be in [0;<track_length>] in milliseconds
	 */
	public void PositionSet(int a);

	/**
	 * @return the playing position (will be [0;<track_length>] in milliseconds)
	 */
	public int PositionGet();

	/**
	 * Gives all meta data available for the currently played element.
	 * 
	 * Guidelines for field names are at
	 * http://wiki.xmms2.xmms.se/wiki/MPRIS_Metadata .
	 */
	public Map<String, Variant> GetMetadata();

	/**
	 * Return the "media player"'s current capabilities : CAN_GO_NEXT There is a
	 * current next track, or at least something that equals to it (that is, the
	 * remote can call the 'Next' method on the interface, and expect something
	 * to happen, heh) CAN_GO_PREV Same as for NEXT, just previous
	 * track/something CAN_PAUSE Can currently pause. This might not always be
	 * possible, and is yet another hint for frontends as to what to indicate
	 * CAN_PLAY Whether playback can currently be started. This might not be the
	 * case if e.g. the playlist is empty in a player, or similar conditions.
	 * Here, again, it is entirely up to the player to decide when it can play
	 * or not, and it should signalize this using the caps API. CAN_SEEK Whether
	 * seeking is possible with the currently played stream (UIs/frontends can
	 * then enable/disable seeking controls) CAN_PROVIDE_METADATA Whether
	 * metadata can be acquired for the currently played stream/source using
	 * GetMetadata at all. CAN_HAS_TRACKLIST Whether the media player can hold a
	 * list of several items Note that the caps are a bitfield, currently
	 * defined as:
	 * 
	 * @return the player current capabilities
	 */
	public int GetCaps();

}
