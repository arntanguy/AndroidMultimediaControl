package general;

import media.Applications;
import media.AvailableApplications;

public class ConnectedApplications {
	
	public static AvailableApplications getAvailable() {
		AvailableApplications available = new AvailableApplications();
		for(Applications app: Applications.values()) {
			ApplicationControlInterface ac = Factory.getApplicationControl(app);
			try {
				ac.connect();
			} catch (Exception e) {
			}
			if(ac.isConnected()) {
				available.add(app);
			}
		}
		return available;
	}
}
