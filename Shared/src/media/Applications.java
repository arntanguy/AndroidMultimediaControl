package media;

import java.util.HashMap;
import java.util.Map;

public enum Applications {
	VLC("vlc"), QUODLIBET("quodlibet");

	private final String name;
	// Reverse-lookup map for getting a day from an abbreviation
	private static final Map<String, Applications> lookup = new HashMap<String, Applications>();
	static {
		for (Applications d : Applications.values())
			lookup.put(d.getName(), d);
	}

	private Applications(String abbreviation) {
		this.name = abbreviation;
	}

	public String getName() {
		return name;
	}

	public static Applications get(String abbreviation) {
		return lookup.get(abbreviation);
	}
}
