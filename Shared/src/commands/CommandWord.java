package commands;

public enum CommandWord {
	HELLO("hello"), PLAY("play"), PAUSE("pause"), QUIT("quit"), UNKNOWN(
			"unknown"), NEXT("next"), PREVIOUS("previous"), VOLUME("volume"), POSITION(
			"current_time"), SET_POSITION("move"), TRACK_CHANGED("track_changed"), META_DATA(
			"meta_data"), ERROR_DBUS_DISCONNECTED("error_dbus_disconnected"), STATUS(
			"status"), STATUS_CHANGED("status_changed"), MOVE("move");

	// Le String de commande.
	private String commandString;

	CommandWord(String commandString) {
		this.commandString = commandString;
	}

	public String toString() {
		return commandString;
	}
}
