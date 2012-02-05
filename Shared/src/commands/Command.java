package commands;

import java.io.Serializable;
import java.util.HashMap;

public class Command implements Serializable {
	private static final long serialVersionUID = 1L;
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

	public void addParameter(String key, String value) {
		// TODO Auto-generated method stub
		parameters.put(key, value);
	}
	/**
	 * 
	 * @param key
	 * @return string contains the parameter value
	 */
	public String getParameterValue(String key) {
		return parameters.get(key);
	}
	
	public String toString() {
		return command.toString();
	}
}
