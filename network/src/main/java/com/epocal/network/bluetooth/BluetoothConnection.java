package com.epocal.network.bluetooth;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dning on 5/5/2017.
 */

public class BluetoothConnection
{
    private BluetoothSocket mSocket = null;
    public BluetoothSocket getSocket() {
        return mSocket;
    }
    public void setSocket(BluetoothSocket socket) {
        this.mSocket = socket;
    }

    private String mAddress = "";
    public void setAddress(String address) {
        this.mAddress = address;
    }
    public String getAddress() {
        return mAddress;
    }

    private IDataReceivedCallback mIDataReceivedCallback;
    public void setIDataReceivedCallback(IDataReceivedCallback mIDataReceivedCallback) {
        this.mIDataReceivedCallback = mIDataReceivedCallback;
    }

    private boolean mListenProcessing;
    private Thread mBackgroundListener;

    InputStream mInputData = null;
    OutputStream mOutputData = null;

    public BluetoothConnection(){}

    public boolean initialConnection() throws IOException
    {
        if((mSocket == null) ||(!mSocket.isConnected()))
        {
            return false;
        }
        mInputData = mSocket.getInputStream();
        mOutputData = mSocket.getOutputStream();
        mListenProcessing = true;
        mBackgroundListener = new Thread(new BackgroundRunnable());
        mBackgroundListener.start();
        return true;
    }

    class BackgroundRunnable implements Runnable {
        @Override
        public void run()
        {
            byte[] buffer = new byte[50000];
            int received = 0;

            while(mListenProcessing)
            {
                if (!isConnected())
                {
                    received = -1;
                } else {
                    try {
                        received = mInputData.read(buffer);
                    } catch (IOException e) {
                    }
                }
                if(received > 0)
                {
                    if(mIDataReceivedCallback != null)
                    {
                        mIDataReceivedCallback.dataReceived(buffer, received);
                    }
                }
                else {
                    if(mIDataReceivedCallback != null)
                    {
                        mIDataReceivedCallback.dataReceived(null, received);
                        mListenProcessing = false;
                    }
                }
            }
        }
    }

    public boolean sendData(byte[] theData, int size)
    {
        if (!isConnected())
        {
            return false;
        }
        try
        {
            mOutputData.write(theData, 0, size);
            return true;
        }
        catch(Exception ex)
        {
        }
        return false;
    }

    public boolean isConnected()
    {
        if(mSocket != null)
        {
            return mSocket.isConnected();
        }
        return false;
    }

    public boolean disconnect() throws java.io.IOException
    {
        //mListenProcessing = false;
        if (mSocket != null) {
            mSocket.close();
            mSocket = null;
        }
        return true;
    }
}
