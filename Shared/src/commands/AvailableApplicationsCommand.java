package commands;

import media.AvailableApplications;

public class AvailableApplicationsCommand extends Command {
	private static final long serialVersionUID = 1L;

	private AvailableApplications available;

	public AvailableApplicationsCommand(CommandWord command,
			AvailableApplications available) {
		super(command);
		this.available = available;
	}

	public AvailableApplications getAvailable() {
		return available;
	}

	public String toString() {
		return "AvailableApplications";
	}

}
