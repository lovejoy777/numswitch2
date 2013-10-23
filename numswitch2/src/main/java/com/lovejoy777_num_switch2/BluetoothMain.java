package com.lovejoy777_num_switch2;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


/**
 * Created by lovejoy on 08/10/13.
 */
public class BluetoothMain extends Activity {
    protected static final int DISCOVERY_REQUEST = 1;

    /**
     * called when activity is first created
     */

    private BluetoothAdapter btAdaptor;

    public TextView statusUpdate;
    public Button connect;
    public Button disconnect;
    public ImageView btlogo;
    public String toastText = "";
    private BluetoothDevice remoteDevice;


    //Create a BroadcastReceiver to receive state changes
    BroadcastReceiver bluetoothState = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //  String prevStateExtra=BluetoothAdapter.EXTRA_PREVIOUS_STATE;
            String stateExtra = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(stateExtra, -1);
            // int previousState = intent.getIntExtra(prevStateExtra, -1);
            //String toastText="";
            switch (state) {
                case (BluetoothAdapter.STATE_TURNING_ON): {

                    break;
                }
                case (BluetoothAdapter.STATE_ON): {

                    setupUI();
                    break;

                }
                case (BluetoothAdapter.STATE_TURNING_OFF): {

                    break;
                }
                case (BluetoothAdapter.STATE_OFF): {

                    setupUI();
                    break;
                }
            }
        }


    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btmain);
        setupUI();

    } //end onCreate

    private void setupUI() {
        // get references
        final TextView statusUpdate = (TextView) findViewById(R.id.result);
        final Button connect = (Button) findViewById(R.id.connectBtn);
        final Button disconnect = (Button) findViewById(R.id.disconnectBtn);
        final ImageView btlogo = (ImageView) findViewById(R.id.btlogo);
        // set display view
        disconnect.setVisibility(View.GONE);
        btlogo.setVisibility(View.GONE);
        btAdaptor = BluetoothAdapter.getDefaultAdapter();
        if (btAdaptor.isEnabled()) {
            String address = btAdaptor.getAddress();
            String name = btAdaptor.getName();
            String statusText = name + ":" + address;
            statusUpdate.setText(statusText);
            disconnect.setVisibility(View.VISIBLE);
            btlogo.setVisibility(View.VISIBLE);
            connect.setVisibility(View.GONE);
        } else {
            connect.setVisibility(View.VISIBLE);
            statusUpdate.setText("Bluetooth is not on");
        }


        connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
                String actionRequestEnable = BluetoothAdapter.ACTION_REQUEST_ENABLE;
                IntentFilter filter = new IntentFilter(actionStateChanged);
                registerReceiver(bluetoothState, filter);
                startActivityForResult(new Intent(actionRequestEnable), 0);

                //register for discovery events
                //String scanModeChanged = BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
                //String beDiscoverable = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
                //IntentFilter filter = new IntentFilter(scanModeChanged);
                //registerReceiver(bluetoothState, filter);
                //startActivityForResult(new Intent(beDiscoverable), DISCOVERY_REQUEST);

            }
        });//end connect onClickListener

        disconnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btAdaptor.disable();
                disconnect.setVisibility(View.GONE);
                btlogo.setVisibility(View.GONE);
                connect.setVisibility(View.VISIBLE);
                statusUpdate.setText("Bluetooth Off");
            }
        });//end disconnect onClickListener
    }//end setupUI

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DISCOVERY_REQUEST) {
            Toast.makeText(BluetoothMain.this, "Discovery in progress", Toast.LENGTH_SHORT).show();
            setupUI();
            findDevices();

        }
    }

    private void findDevices() {
        String lastUsedRemoteDevice = getLastUsedRemoteDevice();
        if (lastUsedRemoteDevice != null) {
            toastText = "Checking for known paired devices, namely: " + lastUsedRemoteDevice;
            Toast.makeText(BluetoothMain.this, toastText, Toast.LENGTH_SHORT).show();
            //see if this device is in a list of currently visible (?), paired devices
            Set<BluetoothDevice> pairedDevices = btAdaptor.getBondedDevices();
            for (BluetoothDevice pairedDevice : pairedDevices) {
                if (pairedDevice.getAddress().equals(lastUsedRemoteDevice)) {
                    toastText = "Found device: " + pairedDevice.getName() + "@" + lastUsedRemoteDevice;
                    Toast.makeText(BluetoothMain.this, toastText, Toast.LENGTH_SHORT).show();
                    remoteDevice = pairedDevice;
                }
            }
        }//end if


        if (remoteDevice == null) {
            toastText = "Starting discovery for remote devices....";
            Toast.makeText(BluetoothMain.this, toastText, Toast.LENGTH_SHORT).show();
            //start discovery
            if (btAdaptor.startDiscovery()) {
                toastText = "Discovery thread started...Scanning for Devices";
                Toast.makeText(BluetoothMain.this, toastText, Toast.LENGTH_SHORT).show();
                registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
        }
    }//end find devices

    // Create a BroadcastReceiver to receive device discovery
    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice;
            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            toastText = "Discovered: " + remoteDeviceName;
            Toast.makeText(BluetoothMain.this, toastText, Toast.LENGTH_SHORT).show();
            //statusUpdate.setText(statusText);

        }
    };


    private String getLastUsedRemoteDevice() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String result = prefs.getString("LAST_REMOTE_DEVICE_ADDRESS", null);
        return result;
    }


}//end BluetoothMain


