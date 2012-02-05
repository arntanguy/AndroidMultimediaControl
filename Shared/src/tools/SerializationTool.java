package tools;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public class SerializationTool {
	public static String serializeMap(Map<String, String> map) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XMLEncoder xmlEncoder = new XMLEncoder(bos);
		xmlEncoder.writeObject(map);
		xmlEncoder.flush();

		return bos.toString();
	}

	public static Map<String, String> deserializeStringMap(String serializedMap) {
		XMLDecoder xmlDecoder = new XMLDecoder(new ByteArrayInputStream(
				serializedMap.getBytes()));
		return (Map<String, String>) xmlDecoder.readObject();
	}
}
