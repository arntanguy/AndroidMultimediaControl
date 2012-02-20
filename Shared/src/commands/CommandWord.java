package commands;

public enum CommandWord {
	HELLO("hello"), PLAY("play"), PAUSE("pause"), QUIT("quit"), UNKNOWN(
			"unknown"), GOTO_NEXT("next"), GOTO_PREVIOUS("previous"), SET_VOLUME(
			"volume"), GET_POSITION("current_time"), SET_POSITION("move"), TRACK_CHANGED(
			"track_changed"), GET_META_DATA("meta_data"), ERROR_DBUS_DISCONNECTED(
			"error_dbus_disconnected"), GET_STATUS("status"), STATUS_CHANGED(
			"status_changed"), MOVE("move"), GET_TRACKLIST("get_track_list"), SET_TRACK(
			"set_track"), SET_APPLICATION("set_application");

	// Le String de commande.
	private String commandString;

	CommandWord(String commandString) {
		this.commandString = commandString;
	}

	public String toString() {
		return commandString;
	}
}
