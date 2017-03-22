package com.devsoul.dima.rock_paper_scissors.Connecting;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Dima on 8/5/2016.
 * Connecting as a client
 */
public class ConnectingThread extends Thread
{
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private final BluetoothAdapter bluetoothAdapter;
    private Activity mContext;

    public ManageConnectThread myManageThreadfromClient;

    public ConnectingThread(BluetoothAdapter adapter, final Activity activity, UUID uuid, BluetoothDevice device) {

        BluetoothSocket temp = null;
        bluetoothDevice = device;
        bluetoothAdapter = adapter;
        mContext = activity;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            temp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothSocket = temp;
    }

    public void run() {
        // Cancel any discovery as it will slow down the connection
        bluetoothAdapter.cancelDiscovery();

        try {
            // This will block until it succeeds in connecting to the device
            // through the bluetoothSocket or throws an exception
            bluetoothSocket.connect();
        } catch (IOException connectException) {
            connectException.printStackTrace();
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
        }

        // If a connection is accepted
        if (bluetoothSocket != null) {
            mContext.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(mContext, "A connection has been accepted.",
                            Toast.LENGTH_SHORT).show();
                }
            });

            // Code to manage the connection in a separate thread
            myManageThreadfromClient = new ManageConnectThread(bluetoothSocket, mContext);
            myManageThreadfromClient.start();

        }
        /**
        if (myManageThreadfromClient != null) {
            String str = "Hello";
            byte[] bytesToSend = str.getBytes();
            try {
                myManageThreadfromClient.sendData(bytesToSend);
            } catch (IOException e) {
                e.printStackTrace();
            }

            str = "How are you?";
            bytesToSend = str.getBytes();
            try {
                myManageThreadfromClient.sendData(bytesToSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         **/
    }

    // Cancel an open connection and terminate the thread
    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
