
public class CommandParser {
	private CommandWords commandWords;
	
	public CommandParser() {
		commandWords = new CommandWords();
	}
	
	public Command parse(String str) {
		int d = str.indexOf(" ");
		String firstWord = (d>0) ? str.substring(0,d) : str;
		switch(commandWords.getCommandWord(firstWord)) {
		case PLAY:
			return new Command(CommandWord.PLAY);
		case QUIT:
			return new Command(CommandWord.QUIT);
		}
		return new Command(CommandWord.UNKNOWN);
	}
}
