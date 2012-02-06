package commands;

import java.util.Map;

import tools.SerializationTool;

public class MetaDataCommand extends Command {
	private static final long serialVersionUID = 1L;
	
	private String serializedMetaData = null;
	
	public MetaDataCommand(CommandWord command) {
		super(command);
	}

	public MetaDataCommand() {
	}
	
	public void setMetaData(Map<String, String> map) {
		serializedMetaData = SerializationTool.mapToString((Map<String, String>) map);
	}
	
	public Map<String, String> getMetaData() {
		return SerializationTool.stringToMap(serializedMetaData);
	}
}
