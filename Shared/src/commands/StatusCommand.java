package commands;

import player.Status;

public class StatusCommand extends Command {

	private static final long serialVersionUID = 1L;
	
	private Status status;
	
	public StatusCommand(CommandWord command, Status s) {
		this(command);
		status = s;
	}
	
	public StatusCommand(CommandWord command) {
		super(command);
	}

	public StatusCommand() {
	}
	
	public void setStatus(Status s) {
		status = s;
	}
	
	public Status getStatus() {
		return status;
	}

}
