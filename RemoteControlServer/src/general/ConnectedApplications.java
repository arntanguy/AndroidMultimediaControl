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
				System.out.println("Application "+app.getName()+" not connected!");
				continue;
			}
			if(ac.isConnected()) {
				available.add(app);
			}
			
		}
		return available;
	}
}
