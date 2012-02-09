package rc.client;

import java.util.ArrayList;

import rc.client.ImageObject;
import android.content.Context;
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
    private Context mContext;
    private ArrayList<ImageObject> apps;

    public ImageAdapter(Context c, ArrayList<ImageObject> apps) {
    	this.apps = apps;
    	this.mContext = c;
    }

    public ImageAdapter(Context c) {
    	this(c, new ArrayList<ImageObject>());
    }
    
    public int getCount() {
        return apps.size();
    }

    public Object getItem(int position) {
        return (position<apps.size()) ? apps.get(position) : null;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        for(ImageObject app: apps) {
        	imageView.setImageResource(app.getLauncherRessource());
        }
        return imageView;
    }
    
    
    public void addItems(ArrayList<ImageObject> apps) {
    	for(ImageObject app : apps) {
    		apps.add(app);
    	}
    }
    public void addItem(ImageObject app) {
    	apps.add(app);
    }
    @Override
	public long getItemId(int arg0) {
		return 0;
	}
}