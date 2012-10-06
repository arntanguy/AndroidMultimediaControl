package rc.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import rc.network.Network;
import rc.network.UDPLookup;
import tools.SerializationTool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import commands.Command;
import commands.CommandWord;

public class ConnectActivity extends Activity {
    public static final String TAG = "ConnectActivity";

    // Truncated list, according to text entry
    private ArrayList<IpItem> partialNames = new ArrayList<IpItem>();
    private ArrayList<IpItem> autoDetectedIps = new ArrayList<IpItem>();
    private ArrayList<IpItem> searchNames = null;
    private HashMap<String, String> ipTable = null;

    // Field where user enters his search criteria
    private EditText ipAdressT;
    private EditText portT;
    private Button connectB;
    private Button autoDetectedServerB;

    // List of names matching criteria are listed here
    private ListView ipList;
    private ArrayAdapter<IpItem> adapter;
    private ListView autoDetectedIpList;
    private ArrayAdapter<IpItem> adapterAutoDetectecIpList;

    // Get the app's shared preferences
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;

    protected static UDPLookup udpLookup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Global.network == null) {
            Global.network = new Network();
            Global.network.setPort(4242);
        }

        setContentView(R.layout.connect);

        ipAdressT = (EditText) findViewById(R.id.ipAdressTextEdit);
        ipAdressT.addTextChangedListener(textChangedWatcher);

        portT = (EditText) findViewById(R.id.portTextEdit);
        connectB = (Button) findViewById(R.id.connectButton);
        autoDetectedServerB = (Button) findViewById(R.id.autoDetectServerButton);

        ipList = (ListView) findViewById(R.id.ipListView);
        adapter = new ArrayAdapter<IpItem>(this,
                android.R.layout.simple_list_item_1, partialNames);
        ipList.setAdapter(adapter);
        ipList.setOnItemClickListener(new ItemClickListener(adapter));

        autoDetectedIpList = (ListView) findViewById(R.id.ipAutoListView);
        adapterAutoDetectecIpList = new ArrayAdapter<IpItem>(this,
                android.R.layout.simple_list_item_1, autoDetectedIps);
        autoDetectedIpList.setAdapter(adapterAutoDetectecIpList);
        autoDetectedIpList.setOnItemClickListener(new ItemClickListener(adapterAutoDetectecIpList));


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesEditor = preferences.edit();

        ipTable = new HashMap<String, String>();
        // XXX : fixme
         ipTable = (HashMap<String, String>) SerializationTool
         .stringToMap(preferences.getString("ip", "fail"));
        searchNames = new ArrayList<IpItem>(ipTable.size());
        for (String s : ipTable.keySet()) {
            searchNames.add(new IpItem(s, Integer.parseInt(ipTable.get(s))));
        }
        System.out.println(searchNames);

        alterAdapter();
        connectB.setOnClickListener(connectClickListener);
        autoDetectedServerB.setOnClickListener(autoConnectClickListener);

        Toast.makeText(this, "Toast it !!! Roast it !", Toast.LENGTH_SHORT)
                .show();

        udpLookup = new UDPLookup(this, 4243);

        if (!Global.network.isConnected())
            new ASyncUDPLookup().execute();

    }

    private OnClickListener connectClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            connect();
        }
    };

    private OnClickListener autoConnectClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            new ASyncUDPLookup().execute();
        }
    };

    private void connect() {
        connect(ipAdressT.getText().toString(),
                Integer.parseInt(portT.getText().toString()));
    }

    private void connect(String ip, int i) {
        Global.network.setIp(ip);
        Global.network.setPort(i);

        new ConnectNetwork().execute(ip);
    }

    /**
     * Establish a connection to the server in an asynchronous way. Thus, it
     * will not block the interface while connecting, and allow the use of
     * progress bar for instance. The UI is updated from the function
     * onProgressUpdate since it runs in the UI thread, as opposed of the rest
     * of the class, running in its own thread
     */
    private class ConnectNetwork extends AsyncTask<String, Integer, Void> {
        private ProgressDialog dialog = null;
        private String ip;
        private String port;

        /**
         * Called before the execution of doInBackground, used to set up dialogs
         * for instance
         */
        protected void onPreExecute() {
            dialog = new ProgressDialog(ConnectActivity.this);
            dialog.setMessage("Connecting...");
            dialog.show();
        }

        /**
         * Effectively handles the creation of the connection. This task may
         * take some time according to the network capabilities, thus the
         * threading
         */
        protected Void doInBackground(String... IP) {
            int nb = IP.length;
            String ip = (nb > 0) ? IP[0] : "";
            this.ip = ip;
            this.port = portT.getText().toString();
            Global.network.setIp(ip);
            Global.network.setPort(Integer.parseInt(port));
            Log.i(TAG, "Connecting to " + ip + ":" + port);
            try {
                Global.network.connect();
            } catch (SocketException e) {
                publishProgress(0);
                Log.e(TAG, "Socket exeption\n" + e.toString());
            } catch (UnknownHostException e) {
                publishProgress(1);
                Log.e(TAG, "Unknown Host\n" + e.toString());
            } catch (IOException e) {
                publishProgress(1);
                Log.e(TAG, "IO Exception\n" + e.toString());
            }
            return null;
        }

        /**
         * Update the UI on the status of the connection
         * 
         * @param progress
         */
        @SuppressWarnings("unused")
        protected void onProgressUpdate(Integer progress) {
            Log.i(TAG, "Progress");
            if (progress == 1) {
                Toast.makeText(ConnectActivity.this,
                        "Network connection failed!", Toast.LENGTH_SHORT)
                        .show();
            } else if (progress == 2) {
                Toast.makeText(ConnectActivity.this, "Unkown host!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Called once the doInBackground thread ends, manages the UI. On
         * success, shows the GridView used to manage the list of available
         * application, effectively giving the user access to the rest of the
         * application.
         */
        protected void onPostExecute(Void result) {
            Log.i(TAG, "Connection finished ");

            dialog.dismiss();
            if (Global.network.isConnected()) {
                Toast.makeText(ConnectActivity.this, "Connected",
                        Toast.LENGTH_SHORT).show();

                // Start the command parser thread
                Thread t = new Thread(Global.network.getCommandParser(),
                        "CommandParser Thread");
                t.start();

                ipTable.put(ip, port);
                preferencesEditor.putString("ip",
                        SerializationTool.mapToString(ipTable));
                // save preferences
                preferencesEditor.commit();
                alterAdapter();

                // Start application choice activity
                startApplicationSelectorActivity();

            } else {
                Toast.makeText(ConnectActivity.this,
                        "Network connection failed", Toast.LENGTH_SHORT).show();
            }

            Global.network.sendCommand(new Command(CommandWord.HELLO));
        }
    } // ConnectNetwork (AsyncTask)

    private class ASyncUDPLookup extends AsyncTask<String, Integer, Void> {
        private DatagramPacket foundServerIP = null;

        /**
         * Called before the execution of doInBackground, used to set up dialogs
         * for instance
         */
        protected void onPreExecute() {

            if (!ConnectActivity.udpLookup.isLooking()) {
                Toast.makeText(ConnectActivity.this, "Looking for server...",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ConnectActivity.this,
                        "A lookup is already in progress, please wait...",
                        Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * UDP lookup to search for server IP This works by broadcasting a
         * request (Ping) on all host on network on a certain port and waiting
         * for someone to respond. If you get a response, that means that the
         * server is up and running there ;)
         */
        protected Void doInBackground(String... strings) {
            UDPLookup lookup = ConnectActivity.udpLookup;
            if (lookup.isLooking())
                return null;
            try {
                foundServerIP = lookup.getServerIp();
                Log.i(TAG, "IP: " + foundServerIP.getAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Update the UI on the status of the connection
         * 
         * @param progress
         */
        @SuppressWarnings("unused")
        protected void onProgressUpdate(Integer progress) {
        }

        /**
         * Called once the doInBackground thread ends, manages the UI. On
         * success, shows the GridView used to manage the list of available
         * application, effectively giving the user access to the rest of the
         * application.
         */
        protected void onPostExecute(Void result) {
            if (foundServerIP != null) {
                final String IP = foundServerIP.getAddress().getHostAddress();
                final int port = foundServerIP.getPort();
                Log.i(TAG, "Found server IP : " + IP);
                Toast.makeText(ConnectActivity.this, "Server IP: " + IP,
                        Toast.LENGTH_SHORT).show();

                autoDetectedIps.add(new IpItem(IP, port - 1));
                adapterAutoDetectecIpList.notifyDataSetChanged();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ConnectActivity.this);

                // set title
                alertDialogBuilder.setTitle("Server Found");

                // set dialog message
                alertDialogBuilder
                        .setMessage(
                                "Connect to server " + IP + ":" + (port - 1)
                                        + "?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int id) {
                                        connect(IP, port - 1);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            } else {
                Toast.makeText(ConnectActivity.this, "Server not found",
                        Toast.LENGTH_SHORT).show();
            }
        }

    } // AsyncUDPLookup (AsyncTask)

    private TextWatcher textChangedWatcher = new TextWatcher() {

        // As the user types in the search field, the list is
        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                int arg3) {
            alterAdapter();
        }

        @Override
        public void afterTextChanged(Editable arg0) {
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                int arg3) {
        }
    };

    /**
     * We click on a server ip, start the application choice activity.
     */
    private class ItemClickListener implements OnItemClickListener {
       private ArrayAdapter<IpItem> adapter;
       
       public ItemClickListener(ArrayAdapter<IpItem> adapter) {
           this.adapter = adapter;
       }
       
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                long id) {
            IpItem ip = (IpItem) adapter.getItem(position);
            connect(ip.getIp(), ip.getPort());
            System.out.println("ip " + ip.toString());
        }
        
    }

    // Filters list of contacts based on user search criteria. If no information
    // is filled in, contact list will be fully shown
    private void alterAdapter() {
        Log.i(TAG, "alter " + ipAdressT.getText().toString());
        if ((ipAdressT.getText().toString()).length() == 0) {
            partialNames.clear();
            partialNames.addAll(searchNames);
        } else {
            partialNames.clear();
            for (int i = 0; i < searchNames.size(); i++) {
                if (searchNames.get(i).toString().toLowerCase()
                        .contains(ipAdressT.getText().toString().toLowerCase())) {
                    partialNames.add(searchNames.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
        Log.i(TAG, "partial " + partialNames);
    }

    private void startApplicationSelectorActivity() {
        Log.i(TAG, "startApplicationSelectorActivity()");
        ((TabWidgetActivity) this.getParent())
                .setTab(TabWidgetActivity.APPLICATIONTAB);
    }

}
