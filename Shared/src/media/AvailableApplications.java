package media;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AvailableApplications implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Applications> available;
	
	public AvailableApplications() {
		available = new ArrayList<Applications>();
	}
	
	public void add(Applications app) {
		available.add(app);
	}
	
	public List<Applications> getAvailable() {
		return available;
	}
	
}
