package rc.client;

import java.util.ArrayList;

import media.Applications;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Manage image grids, inspired by the android tutorial
 * http://developer.android.com/guide/tutorials/views/hello-gridview.html
 */
public class ImageAdapter extends BaseAdapter {
	private ArrayList<ApplicationIconView> apps;
	private Handler uiHandler;

	public ImageAdapter(Context c, ArrayList<ApplicationIconView> apps) {
		super();
		this.apps = apps;
		uiHandler = new Handler();
	}

	public ImageAdapter(Context c) {
		this(c, new ArrayList<ApplicationIconView>());
	}

	public int getCount() {
		return apps.size();
	}

	public Object getItem(int position) {
		return (position < apps.size()) ? apps.get(position) : null;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = apps.get(position);
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}
		return imageView;
	}

	public void addItems(ArrayList<ApplicationIconView> apps) {
		apps.addAll(apps);
	}

	public void addItem(ApplicationIconView app) {
		apps.add(app);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void setItemEnabled(Applications app, boolean enabled) {
		for (ApplicationIconView appView : apps) {
			if (appView.getApplication() == app) {
				uiHandler.post(new ChangeRessourceRunner(appView, enabled));
				//appView.setEnabled(enabled);
			}
		}
	}

	// Create runnable for updating ui according to the new state
	private class ChangeRessourceRunner implements Runnable {
		private ApplicationIconView appView;
		private boolean enabled;
		
		public ChangeRessourceRunner(ApplicationIconView view, boolean enabled) {
			this.enabled = enabled;
			this.appView = view;
		}
		
		public void run() {		
			appView.setEnabled(true);
		}
	};

}