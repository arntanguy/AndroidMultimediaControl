package commands;

import media.AvailableApplications;

public class AvailableApplicationsCommand extends Command {
	private static final long serialVersionUID = 1L;

	private AvailableApplications available;
	
	public AvailableApplicationsCommand(CommandWord getAvailableApplications,
			AvailableApplications available) {
		this.available = available;
	}

	public AvailableApplications getAvailable() {
		return available;
	}
	
	
}
