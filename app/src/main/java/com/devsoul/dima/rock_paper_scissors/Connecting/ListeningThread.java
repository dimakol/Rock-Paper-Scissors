package com.devsoul.dima.rock_paper_scissors.Connecting;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;
import com.devsoul.dima.rock_paper_scissors.R;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Dima on 8/5/2016.
 * Connecting as a server
 */
public class ListeningThread extends Thread
{
    private final BluetoothServerSocket bluetoothServerSocket;
    private Activity mContext;
    private Boolean isConnected = false;

    public ListeningThread(BluetoothAdapter bluetoothAdapter, final Activity activity, UUID uuid) {
        mContext = activity;

        BluetoothServerSocket temp = null;
        try {
            temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(mContext.getString(R.string.app_name), uuid);

        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothServerSocket = temp;
    }

    public void run() {
        BluetoothSocket bluetoothSocket;
        // This will block while listening until a BluetoothSocket is returned
        // or an exception occurs
        while (true) {
            try {
                bluetoothSocket = bluetoothServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection is accepted
            if (bluetoothSocket != null) {

                mContext.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mContext, "A connection has been accepted.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                this.isConnected = true;

                // Manage the connection in a separate thread
                ManageConnectThread manage = new ManageConnectThread(bluetoothSocket, mContext);
                manage.start();

                /**
                if (manage != null) {
                    String str = "Hi";
                    byte[] bytesToSend = str.getBytes();
                    try {
                        manage.sendData(bytesToSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                **/
            }
        }
    }

    // Cancel the listening socket and terminate the thread
    public void cancel() {
        try {
            bluetoothServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Return if the Listening thread have connection with Connecting thread
    public Boolean Connected()
    {
        return (this.isConnected);
    }
}
