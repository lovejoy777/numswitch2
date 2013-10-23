package com.lovejoy777_num_switch2;


// imports

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by lovejoy on 13/10/13.
 */

public class BT2Main extends Activity {
    private static final String TAG = "num i";

    Switch switch1;
    Switch switch2;
    Switch switch3;
    Switch switch4;
    TextView txtArduino;
    Handler h;

    final int RECEIVE_MESSAGE = 1;        // Status  for Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // Default Mac Address 00:15:FF:F2:19:5F


    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // MAC-address of Bluetooth module (you must edit this line)
    private static String address = "00:15:FF:F2:19:5F";

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.bt2main1);


        switch1 = (Switch) findViewById(R.id.switch1);         // switch Relay 1 toggle
        switch2 = (Switch) findViewById(R.id.switch2);         // switch Relay 2 toggle
        switch3 = (Switch) findViewById(R.id.switch3);           // switch Relay 3 toggle
        switch4 = (Switch) findViewById(R.id.switch4);           // switch Relay 4 toggle

        txtArduino = (TextView) findViewById(R.id.txtArduino); // for display the received data from the Arduino

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECEIVE_MESSAGE:                                        // if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);      // create string from bytes array
                        sb.append(strIncom);                                     // append string
                        int endOfLineIndex = sb.indexOf("\r\n");                 // determine the end-of-line
                        if (endOfLineIndex > 0) {                                // if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);    // extract string
                            sb.delete(0, sb.length());                           // and clear
                            txtArduino.setText("Data from Arduino: " + sbprint); // update TextView
                            mConnectedThread.write("0");
                        }

                        break;
                } // ends switch

            } // ends public void handleMessage

        }; // ends h = new Handler

        btAdapter = BluetoothAdapter.getDefaultAdapter();        // get Bluetooth adapter
        checkBTState();

        // ends public void onCreate


        // switch 1

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (switch1.getId()) {
                    case R.id.switch1:
                }
                if (switch1.isChecked()) {
                    mConnectedThread.write("1");    // Send "1" via Bluetooth
                    Toast.makeText(getBaseContext(), "Turning on Relay One", Toast.LENGTH_SHORT).show();

                } else if (!switch1.isChecked()) {
                    mConnectedThread.write("0");    // Send "0" via Bluetooth
                    Toast.makeText(getBaseContext(), "Turning off Relay One", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //switch 2
        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (switch2.getId()) {
                    case R.id.switch2:
                }
                if (switch2.isChecked()) {
                    mConnectedThread.write("3");    // Send "3" via Bluetooth
                    Toast.makeText(getBaseContext(), "Turning on Relay Two", Toast.LENGTH_SHORT).show();

                } else if (!switch2.isChecked()) {
                    mConnectedThread.write("2");    // Send "2" via Bluetooth
                    Toast.makeText(getBaseContext(), "Turning off Relay Two", Toast.LENGTH_SHORT).show();

                }
            }

        });

        switch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (switch3.getId()) {
                    case R.id.switch3:
                }
                if (switch3.isChecked()) {
                    mConnectedThread.write("5");    // Send "5" via Bluetooth
                    Toast.makeText(getBaseContext(), "Turning on Relay Three", Toast.LENGTH_SHORT).show();

                } else if (!switch3.isChecked()) {
                    mConnectedThread.write("4");    // Send "4" via Bluetooth
                    Toast.makeText(getBaseContext(), "Turning off Relay Three", Toast.LENGTH_SHORT).show();
                }

            }
        });

        switch4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (switch4.getId()) {
                    case R.id.switch4:
                }
                if (switch4.isChecked()) {
                    mConnectedThread.write("7");    // Send "7" via Bluetooth
                    Toast.makeText(getBaseContext(), "Turning on Relay Four", Toast.LENGTH_SHORT).show();

                } else if (!switch4.isChecked()) {
                    mConnectedThread.write("6");    // Send "6" via Bluetooth
                    Toast.makeText(getBaseContext(), "Turning off Relay Four", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection", e);
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);

    } // ends private BluetoothSocket

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

    /*try {
      btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
    } catch (IOException e) {
      errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
    }*/

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "....Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

    } // ends public void onResume

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    } // ends public void onPause

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }

        } // ends else

    } // ends private void checkBTState

    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    } // ends errorExit

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        // ends public ConnectedThread

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECEIVE_MESSAGE, bytes, -1, buffer).sendToTarget();        // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }

            } //ends while

        } // ends public void run

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }

        } //ends public void write

    } //ends private class ConnectedThread


} //ends public class BT2Main
