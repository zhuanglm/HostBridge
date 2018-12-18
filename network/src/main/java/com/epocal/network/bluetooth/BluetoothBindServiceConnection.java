package com.epocal.network.bluetooth;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by dning on 4/28/2017.
 */

public class BluetoothBindServiceConnection implements ServiceConnection
{
    private Messenger mDiscoveryMessenger = null;
    private Messenger mCallbackMessenger;

    public void setCallbackMessenger(Messenger callbackMessenger)
    {
        mCallbackMessenger = callbackMessenger;
    }

    public BluetoothBindServiceConnection () {}

    public void onServiceConnected(ComponentName className, IBinder service)
    {
        mDiscoveryMessenger = new Messenger(service);
        Message msg = Message.obtain(null, BluetoothController.MSG_DISCOVERY_BEGIN, 0, 0);
        msg.replyTo = mCallbackMessenger;
        // Send the Message to the Service
        try
        {
            mDiscoveryMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onServiceDisconnected(ComponentName className)
    {
        mDiscoveryMessenger = null;
        mCallbackMessenger = null;
    }


}
