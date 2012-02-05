package commands;

import java.util.HashMap;
import java.util.Map;

public class Command {
	private CommandWord command;
	private HashMap<String, String> parameters;

	public Command(CommandWord command) {
		this.command = command;
		this.parameters = new HashMap<String, String>();
	}

	public Command() {
		this(CommandWord.UNKNOWN);
	}

	public CommandWord getCommand() {
		return command;
	}

	public void setParameters(HashMap<String, String> args) {
		parameters = args;
	}
	/**
	 * 
	 * @param key
	 * @return string
	 * 				contains the parameter value
	 */
	public String getParameterValue(String key) {
		return parameters.get(key);
	}
	
}
