package commands;

public class ErrorCommand extends Command {
	private static final long serialVersionUID = 1L;

	private String errorTitle;
	private String errorMessage;

	public ErrorCommand() {
		super();
	}

	public ErrorCommand(CommandWord command) {
		super(command);
	}

	public ErrorCommand(CommandWord command, String errorTitle, String error) {
		this(command);
		this.errorMessage = error;
	}

	public String getTitle() {
		return errorTitle;
	}

	public String getMessage() {
		return errorMessage;
	}

	public String toString() {
		return errorTitle + "\n" + errorMessage;
	}

}
