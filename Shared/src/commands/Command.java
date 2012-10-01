package commands;

import java.io.Serializable;

/**
 * A basic command meant to be sent to communicate between the server and client.
 * It only contains a CommandWord, meaning an enum containing available command names.
 * 
 * @author TANGUY Arnaud
 *
 */
public class Command implements Serializable {
	private static final long serialVersionUID = 1L;
	private CommandWord command;

	public Command(CommandWord command) {
		this.command = command;
	}

	public Command() {
		this(CommandWord.UNKNOWN);
	}

	public CommandWord getCommand() {
		return command;
	}

	public String toString() {
		return command.toString();
	}
}
