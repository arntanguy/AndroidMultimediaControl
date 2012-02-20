package media;

import java.util.Map;

public class MetaDataQuodlibet extends MetaData {

	private static final long serialVersionUID = 1L;

	public MetaDataQuodlibet(Map<String, String> metaData) {
		super(metaData);
	}
	
	@Override
	public int getLength() {
		String length = metaData.get("mtime");
		return (length!=null) ? Integer.parseInt(length) : 0;
	}
}
