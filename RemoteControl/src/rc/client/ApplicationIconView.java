package rc.client;

import media.Applications;
import android.content.Context;
import android.widget.ImageView;

public class ApplicationIconView extends ImageView {
	private int enabledRessource;
	private int disabledRessource;
	private Applications application;
	
	public ApplicationIconView(Context context, Applications app, int enabledRessource, int disabledRessource) {
		super(context);
		this.enabledRessource = enabledRessource;
		this.disabledRessource = disabledRessource;
		this.application = app;
		this.setEnabled(false);
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(enabled) setImageResource(enabledRessource); 
		else setImageResource(disabledRessource);
	}
	
	public void setApplication(Applications app) {
		application = app;
	}
	
	public Applications getApplication() {
		return application;
	}

}
