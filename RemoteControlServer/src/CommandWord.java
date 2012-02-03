
public enum CommandWord {
	PLAY("play"), PAUSE("pause"), QUIT("quit"), UNKNOWN("unknown"), NEXT("next"), PREVIOUS("previous");
	

    // Le String de commande.
    private String commandString;

    CommandWord(String commandString) {
        this.commandString = commandString;
    }

    public String toString() {
        return commandString;
    }
}
