package media;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import tools.SerializationTool;

public class MetaData implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<String, String> metaData;

	public MetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

	public MetaData() {
	}

	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

	public String getArtist() {
		return metaData.get("artist");
	}

	public String getTitle() {
		//return metaData.get("title");
		return getTitleFromLocation();
	}

	public String getLocation() {
		return metaData.get("location");
	}

	public String getTitleFromLocation() {
		String url = null;
		try {
			url = URLDecoder.decode(getLocation(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return (url != null) ? url.substring(url.lastIndexOf('/') + 1, url
				.lastIndexOf('.')) : "";
	}

	public int getLength() {
		return Integer.parseInt(metaData.get("length"));
	}

	/**
	 * This is the default implementation of writeObject. Customise if
	 * necessary.
	 */
	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		aOutputStream.defaultWriteObject();
		aOutputStream.writeObject(SerializationTool.mapToString(metaData));
	}

	/**
	 * Always treat de-serialization as a full-blown constructor, by validating
	 * the final state of the de-serialized object.
	 */
	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		aInputStream.defaultReadObject();

		metaData = SerializationTool.stringToMap((String) aInputStream
				.readObject());
	}

	public String toString() {
		return getTitle();
	}
}
