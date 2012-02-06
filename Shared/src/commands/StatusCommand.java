package commands;

import org.freedesktop.MPRISStatus;

public class StatusCommand extends Command {

	private static final long serialVersionUID = 1L;
	
	private MPRISStatus status;
	
	public StatusCommand(CommandWord command, MPRISStatus s) {
		this(command);
		status = s;
	}
	
	public StatusCommand(CommandWord command) {
		super(command);
	}

	public StatusCommand() {
	}
	
	public void setStatus(MPRISStatus s) {
		status = s;
	}
	
	public MPRISStatus getStatus() {
		return status;
	}

}
