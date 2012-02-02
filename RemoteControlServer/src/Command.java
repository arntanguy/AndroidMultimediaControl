
public class Command {
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
}
