package org.freedesktop;

import org.freedesktop.dbus.Position;

/**
 * First integer: 0 = Playing, 1 = Paused, 2 = Stopped. 
 * Second interger: 0 = * Playing linearly , 1 = Playing randomly. 
 * Third integer: 0 = Go to the next element once the current has 
 * finished playing , 1 = Repeat the current element 
 * Fourth integer: 0 = Stop playing once the last element
 * has been played, 1 = Never give up playing
 * 
 */
import org.freedesktop.dbus.Struct;

import dbus.Status;

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
