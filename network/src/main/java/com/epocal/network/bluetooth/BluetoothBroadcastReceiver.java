package com.epocal.network.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by dning on 4/28/2017.
 */

public class BluetoothBroadcastReceiver extends BroadcastReceiver
{
    private Messenger mCallbackMessenger;
    public void setCallbackMessenger(Messenger callbackMessenger)
    {
        mCallbackMessenger = callbackMessenger;
    }

    public BluetoothBroadcastReceiver () {}

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Discovery has found a device. Get the ReaderDevice
            // object and its info from the Intent.
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int deviceid = device.getBluetoothClass().getDeviceClass();
            if(mCallbackMessenger != null)
            {
                Bundle attached =  new Bundle();
                attached.putString(BluetoothController.DATA_DEVICE_NAME, device.getName());
                attached.putString(BluetoothController.DATA_DEVICE_ADDRESS, device.getAddress());
                Message replyMsg = Message.obtain(null, BluetoothController.MSG_DISCOVERY_NEW_DEVICE);
                replyMsg.setData(attached);
                try {
                    mCallbackMessenger.send(replyMsg);
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            if(mCallbackMessenger != null)
            {
                Message replyMsg = Message.obtain(null, BluetoothController.MSG_DISCOVERY_END);
                try {
                    mCallbackMessenger.send(replyMsg);
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            if(mCallbackMessenger != null)
            {
                Message replyMsg = Message.obtain(null, BluetoothController.MSG_DISCOVERY_BEGIN);
                try {
                    mCallbackMessenger.send(replyMsg);
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
