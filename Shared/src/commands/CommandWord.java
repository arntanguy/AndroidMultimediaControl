package commands;
public enum CommandWord {
	PLAY("play"), PAUSE("pause"), QUIT("quit"), UNKNOWN("unknown"), NEXT("next"), PREVIOUS(
			"previous"), VOLUME("volume"), CURRENT_TIME("current_time"), MOVE("move");

	// Le String de commande.
	private String commandString;

	CommandWord(String commandString) {
		this.commandString = commandString;
	}

	public String toString() {
		return commandString;
	}
}
