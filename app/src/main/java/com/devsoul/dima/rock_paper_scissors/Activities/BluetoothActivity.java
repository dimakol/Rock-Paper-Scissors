package com.devsoul.dima.rock_paper_scissors.Activities;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.devsoul.dima.rock_paper_scissors.Connecting.ConnectingThread;
import com.devsoul.dima.rock_paper_scissors.Connecting.ListeningThread;
import com.devsoul.dima.rock_paper_scissors.R;

import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends Activity {
    private static final int ENABLE_BT_REQUEST_CODE = 1;
    private static final int DISCOVERABLE_BT_REQUEST_CODE = 2;

    protected static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static final int DISCOVERABLE_DURATION = 60;
    public final static UUID uuid = UUID.fromString("fc5ffc49-00e3-4c8b-9cf1-6b72aad1001a");
    private final static String EXTRA_ADDRESS = "Device_MAC_Address";

    Button btn_on, btn_off, btn_list , btn_visible, btn_connect;
    private TextView text;
    private String toastText;

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private ArrayAdapter adapter, adapter_list;

    public ListeningThread Server_Thread;
    public ConnectingThread Client_Thread;

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                 // Add the name and address of the device to an array adapter to show in a ListView
                adapter.add(device.getName() + "\n" + device.getAddress());
                adapter.notifyDataSetChanged();
            }

            // Show connect button
            if (!adapter.isEmpty())
                btn_connect.setVisibility(View.VISIBLE);
            else // Disappear connect button
                btn_connect.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplayer);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
        {
            toastText = "Device does not support Bluetooth";
            Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
            // Back to main menu
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);
        }
        else
        // Set the UI
        {
            btn_on = (Button) findViewById(R.id.button_on);
            btn_off = (Button) findViewById(R.id.button_off);
            btn_visible = (Button) findViewById(R.id.button_visible);
            btn_list = (Button) findViewById(R.id.button_list);
            btn_connect = (Button) findViewById(R.id.button_connect);

            text = (TextView) findViewById(R.id.textView_devices);

            lv = (ListView) findViewById(R.id.listView);

            // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
            // and the array that contains the data
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                    text.setTextColor(Color.BLACK);
                    return view;
                }
            };

            adapter_list = new ArrayAdapter(this, android.R.layout.simple_list_item_1) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                    text.setTextColor(Color.BLACK);
                    return view;
                }
            };
        }
    }

    /**
     * Enabling BluetoothActivity
      */
    public void on(View v){
        if (!bluetoothAdapter.isEnabled())
        {
            // A dialog will appear requesting user permission to enable Bluetooth
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, ENABLE_BT_REQUEST_CODE);
        }
        else
        {
            toastText = "Bluetooth already on";
            Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Disabling BluetoothActivity
     */
    public void off(View v){
        bluetoothAdapter.disable();
        adapter.clear();
        toastText = "Bluetooth turned off";
        Toast.makeText(getApplicationContext(), toastText ,Toast.LENGTH_LONG).show();
        // Disappear connect button
        btn_connect.setVisibility(View.INVISIBLE);
    }

    /**
     * Making BluetoothActivity Discoverable,
     * By default, the device will become discoverable for 120 seconds
     * but we declare him to become discoverable for 60 seconds
      */
    public void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        getVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
        startActivityForResult(getVisible, DISCOVERABLE_BT_REQUEST_CODE);

        // Waits for connection request from client
        Server_Thread = new ListeningThread(bluetoothAdapter, this, uuid);
        Server_Thread.start();
    }

    public void list(View v){
        // Here, you set the data in your ListView
        lv.setAdapter(adapter_list);
        adapter_list.clear();
        pairedDevices = bluetoothAdapter.getBondedDevices();

        for(BluetoothDevice bt : pairedDevices) {
            adapter_list.add(bt.getName() + "\n" + bt.getAddress());
        }
        toastText = "Showing Paired Devices";
        Toast.makeText(getApplicationContext(), toastText ,Toast.LENGTH_SHORT).show();
        text.setText("Paired Devices:");
        adapter_list.notifyDataSetChanged();

        btn_connect.setVisibility(View.INVISIBLE);
    }

    public void discover(View v)
    {
        // Here, you set the data in your ListView
        lv.setAdapter(adapter);
        adapter.clear();
        text.setText("Discovered Devices:");

        // Marshmallow Permissions for API version 23 and above.
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        // To scan for remote Bluetooth devices
        if (bluetoothAdapter.startDiscovery())
        {
            toastText = "Discovering other bluetooth devices...";
            Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
        }
        else
        {
            toastText = "Discovery failed to start.";
            Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            // Bluetooth successfully enabled!
            case ENABLE_BT_REQUEST_CODE:
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    toastText = "Bluetooth turned on";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                }
                else // RESULT_CANCELED as user refuse or failed
                {
                    toastText = "Bluetooth isn't turned on";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                }
                break;
            }
            case DISCOVERABLE_BT_REQUEST_CODE:
            {
                if (resultCode == DISCOVERABLE_DURATION)
                {
                    toastText = "Your device is now discoverable by other devices for " + DISCOVERABLE_DURATION + " seconds";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
                }
                else // RESULT_CANCELED as user refuse or failed
                {
                    toastText = "Fail to enable discoverability on your device.";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void back(View v)
    {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
    }

    public void connect (View v)
    {
        int pos = lv.getCheckedItemPosition();

        // User selected a device to connect with
        if (pos > -1)
        {
            String itemValue = (String) lv.getItemAtPosition(pos);
            // Get the MAC Address of selected device
            String MAC = itemValue.substring(itemValue.length() - 17);
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC);

            // Initiate a connection request in a separate thread
            Client_Thread = new ConnectingThread(bluetoothAdapter, this, uuid, bluetoothDevice);
            Client_Thread.start();

            //toastText = "Connection Established";
            //Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
        }
        else
        {
            toastText = "Please select a device";
            Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
        }
    }

/**
 * The Handler that gets information back from the ManageConnectThread
 */
    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int)msg.arg1;
            int end = (int)msg.arg2;
            switch(msg.what)
            {
                case 1:
                    String writeMessage = new String(writeBuf);
                    writeMessage = writeMessage.substring(begin, end);
                    Toast.makeText(getApplicationContext(), writeMessage, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // Register the BroadcastReceiver for ACTION_FOUND
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }
}
