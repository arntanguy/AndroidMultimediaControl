package commands;

import java.util.HashMap;


public class CommandParser {
	private CommandWords commandWords;
	
	public CommandParser() {
		commandWords = new CommandWords();
	}
	
	public Command parse(String str) {
		int d = str.indexOf(" ");
		String firstWord = (d>0) ? str.substring(0,d) : str;
		String delims = "[ ]+";
		String[] tokens = str.split(delims);
		if(tokens.length == 0) return new Command(CommandWord.UNKNOWN);
		HashMap<String, String> args = new HashMap<String, String>();
		
		Command c = new Command(commandWords.getCommandWord(tokens[0]));
		delims = "[=]+";
		String[] split=null;
		for(int i=1; i<tokens.length; i++) {
			split = tokens[i].split(delims);
			if(split.length == 2) {
				args.put(split[0], split[1]);
			}
		}
		c.setParameters(args);
		return c;
	}
}
