package rc.client;

import java.lang.reflect.Field;

import media.Applications;
import media.AvailableApplications;
import media.MetaData;
import media.TrackList;
import player.Status;
import rc.network.NetworkDataListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import commands.Command;
import commands.CommandWord;
import commands.ErrorCommand;
import commands.ObjectCommand;

public class ApplicationSelectorActivity extends Activity {
    private static final String TAG = "ChoseApplicationActivity";

    private GridView gridview;
    private ImageAdapter gridviewAdapter;

    private NetworkDataListener networkHandler;
    private Handler uiHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Creating activity");
        
        uiHandler = new Handler();
        setContentView(R.layout.choose_application);

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(itemClickListener);

        networkHandler = new NetworkDataHandler();
        Global.network.addStatusListener(networkHandler);
        Global.network.sendCommand(new Command(
                CommandWord.GET_AVAILABLE_APPLICATIONS));

        /*
         * Shows an ImageView with the icon of an available application.
         */
        gridviewAdapter = (ImageAdapter) gridview.getAdapter();
        int resID = 0;
        int disabledResId = 0;
        for (Applications app : Applications.values()) {
            try {
                Class res = R.drawable.class;
                Field field = res.getField(app.getName().toLowerCase()
                        + "_launcher");
                resID = field.getInt(null);
                field = res.getField(app.getName().toLowerCase()
                        + "_launcher_disabled");
                disabledResId = field.getInt(null);
            } catch (Exception e) {
                resID = R.drawable.vlc_launcher;
                Log.e(TAG, "Failure to get drawable id.", e);
            }

            gridviewAdapter.addItem(new ApplicationIconView(this, app, resID,
                    disabledResId));
        }
    }

    /**
     * NetworkDataHandler is an observer for the Network class. It will recieve
     * new data when available, status update on the player state, and various
     * informations about the network.
     */
    private class NetworkDataHandler implements NetworkDataListener {
        @Override
        public void statusChanged(Status status) {
        }

        @Override
        public void timeChanged(Integer newTime) {
        }

        @Override
        public void metaDataChanged(MetaData metaData) {
        }

        @Override
        public void trackChanged() {
        }

        @Override
        public void trackListChanged(TrackList trackList) {
        }

        @Override
        public void availableApplicationsChanged(
                AvailableApplications availableApplications) {
            Log.i(TAG, "Available applications changed: "
                    + availableApplications.getAvailable());
            for (Applications app : availableApplications.getAvailable()) {
                gridviewAdapter.setItemEnabled(app, true);
            }
        }

        @Override
        public void error(ErrorCommand error) {
            // Show errors in UI thread
            uiHandler.post(new ErrorHandler(error));
        }
    }
    /**
     * Handles error commands, meant to be used in UI Thread
     * @author Arnaud TANGUY
     *
     */
    private class ErrorHandler implements Runnable {
        ErrorCommand error;
         
        public ErrorHandler(ErrorCommand e) {
            error = e;
        }
        @Override
        public void run() {
            switch (error.getCommand()) {
            case ERROR_APPLICATION_NOT_STARTED:
                Log.e(TAG, error.toString());
                Toast.makeText(ApplicationSelectorActivity.this,
                        "Failed to establish link with application", Toast.LENGTH_LONG)
                        .show();
            }
            
        }
    
    }

    /**
     * On click on an application icon, start the associated activity.
     */
    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                long id) {
            final ApplicationIconView app = (ApplicationIconView) gridviewAdapter
                    .getItem(position);
            if (app.isEnabled()) {
                Global.network.sendCommand(new ObjectCommand<String>(
                        CommandWord.SET_APPLICATION, app.getApplication()
                                .getName()));
                ((TabWidgetActivity) ApplicationSelectorActivity.this
                        .getParent()).setTab(TabWidgetActivity.PLAYTAB);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ApplicationSelectorActivity.this);
                builder.setMessage(
                        "The application "
                                + app.getApplication().getName()
                                + " is not started or not installed. Do you want to try to start it?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int id) {
                                        Log.i(TAG,
                                                "Trying to start application "
                                                        + app.getApplication()
                                                                .getName());
                                        Global.network
                                                .sendCommand(new ObjectCommand<String>(
                                                        CommandWord.START_APPLICATION,
                                                        app.getApplication()
                                                                .getName()));
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

                // Toast.makeText(ApplicationSelectorActivity.this,
                // "Application is not enabled",
                // Toast.LENGTH_SHORT).show();
            }
        }
    };
}
