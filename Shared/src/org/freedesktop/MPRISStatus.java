package org.freedesktop;

import org.freedesktop.dbus.Position;
import org.freedesktop.dbus.Struct;

import player.Status;


/**
 * Represent the status of MPRIS-based players
 * The toStatus function is meant to generalize this type in a more general way.
 * 
 * @author TANGUY Arnaud
 *
 */
public final class MPRISStatus extends Struct {
	@Position(0)
	public final int playingStatus;
	@Position(1)
	public final int playingLinearity;
	@Position(2)
	public final int repeatStatus;
	@Position(3)
	public final int stopStatus;

	public MPRISStatus(int playingStatus, int playingLinearity, int repeatStatus, int stopStatus) {
		this.playingStatus = playingStatus;
		this.playingLinearity = playingLinearity;
		this.repeatStatus = repeatStatus;
		this.stopStatus = stopStatus;
	}
	
	public Status toStatus() {
		return new Status(playingStatus, playingLinearity, repeatStatus, stopStatus);
	}
}
