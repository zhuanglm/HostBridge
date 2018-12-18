package com.epocal.common.androidutil;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by dning on 10/17/2017.
 */

public class BindServiceConnectorUtil implements ServiceConnection {

    public static final int MSG_SERVICE_BEGIN = 1;
    public static final int MSG_SERVICE_END = 2;

    private Messenger mInternalServiceMessenger = null;
    private Messenger mCallbackMessenger;

    public void setCallbackMessenger(Messenger callbackMessenger)
    {
        mCallbackMessenger = callbackMessenger;
    }

    public BindServiceConnectorUtil () {}

    public void onServiceConnected(ComponentName className, IBinder service)
    {
        mInternalServiceMessenger = new Messenger(service);
        Message msg = Message.obtain(null, MSG_SERVICE_BEGIN, 0, 0);
        msg.replyTo = mCallbackMessenger;
        // Send the Message to the Service
        try
        {
            mInternalServiceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onServiceDisconnected(ComponentName className)
    {
        mInternalServiceMessenger = null;
        mCallbackMessenger = null;
    }
}
