package commands;

import media.MetaData;

public class MetaDataCommand extends Command {
	private static final long serialVersionUID = 1L;

	private MetaData metaData;
	
	public MetaDataCommand(CommandWord command) {
		super(command);
	}

	public MetaDataCommand() {
	}

	public void setMetaData(MetaData map) {
		metaData = map;
	}

	public MetaData getMetaData() {
		return metaData;
	}
}
