package com.devsoul.dima.rock_paper_scissors.Connecting;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dima on 8/5/2016.
 * Managing a Connection
 */
public class ManageConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Activity mContext;

    public ManageConnectThread(BluetoothSocket socket, final Activity activity)
    {
        mmSocket = socket;
        mContext = activity;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try
        {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        }
        catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }


    public void run()
    {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes;

        // Keep listening to the InputStream until an exception occurs
        while (true)
        {
            try
            {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                String strReceived = new String(buffer, 0, bytes);
                final String msgReceived = String.valueOf(bytes) +
                        " bytes received:\n"
                        + strReceived;

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, String.valueOf(msgReceived),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch (IOException e)
            {
                final String msgConnectionLost = "Connection lost:\n"
                        + e.getMessage();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, String.valueOf(msgConnectionLost),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
        }
    }

    public void sendData(byte[] buffer) throws IOException {
        try
        {
            mmOutStream.write(buffer);
            mmOutStream.flush();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
