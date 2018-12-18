package com.epocal.network.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.epocal.network.bluetooth.di.component.BluetoothComponent;
import com.epocal.network.bluetooth.di.component.DaggerBluetoothComponent;
import com.epocal.network.bluetooth.di.module.BluetoothModule;
import com.epocal.network.bluetooth.di.module.InternalModule;

import javax.inject.Inject;

/**
 * Created by dning on 4/28/2017.
 */

public class BluetoothDiscovery extends Service
{
    public final String TAG = "bluetooth discovery";
    @Inject BluetoothBroadcastReceiver mBluetoothBroadcastReceiver;
    @Inject BluetoothAdapter mBluetoothAdapter;

    private Messenger mCallbackMessenger;
    final Messenger mMessenger = new Messenger(new mIncomingHandler());

    class mIncomingHandler extends Handler
    {
        @Override
        public void handleMessage( Message msg )
        {
            int msgType = msg.what;
            switch (msgType)
            {
                case BluetoothController.MSG_DISCOVERY_BEGIN:
                    if (mthread != null)
                    {
                        boolean r = true;

                        if(r) {
                            mCallbackMessenger = msg.replyTo;
                            IntentFilter filter = new IntentFilter();
                            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                            filter.addAction(BluetoothDevice.ACTION_FOUND);
                            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

                            mBluetoothBroadcastReceiver.setCallbackMessenger(mCallbackMessenger);
                            getApplicationContext().registerReceiver(mBluetoothBroadcastReceiver, filter);
                        }
                        if(mthread.getState() == Thread.State.NEW) {
                            mthread.start();
                        }
                        break;
                    }
                case BluetoothController.MSG_DISCOVERY_END:
                    getApplicationContext().unregisterReceiver(mBluetoothBroadcastReceiver);
                    stopSelf();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    public BluetoothDiscovery()
    {
        BluetoothComponent bc = DaggerBluetoothComponent.builder()
                .bluetoothModule(new BluetoothModule())
                .internalModule(new InternalModule())
                .build();
        bc.inject(this);
    }
    @Override public IBinder onBind(Intent intent)
    {
        return mMessenger.getBinder();
    }

    @Override public boolean onUnbind(Intent intent)
    {
        stopDiscovery();
        return super.onUnbind(intent);
    }

    private Thread mthread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            if(mBluetoothAdapter.isDiscovering())
            {
                mBluetoothAdapter.cancelDiscovery();
            }
            mBluetoothAdapter.startDiscovery();
        }
    });

    public void stopDiscovery()
    {
        Message msg = Message.obtain(null, BluetoothController.MSG_DISCOVERY_END, 0, 0);
        try {
            mMessenger.send(msg);
        }catch (RemoteException e){}
        mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    public void onDestroy() {
        mCallbackMessenger = null;
        if(mthread != null)
            mthread.currentThread().interrupt();
        super.onDestroy();
    }
}
