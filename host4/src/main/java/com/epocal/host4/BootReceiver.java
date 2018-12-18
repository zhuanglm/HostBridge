package com.epocal.host4;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.epocal.host4.init.Entry;

/**
 * Receiver for booting at device boot up
 *
 * Created by Zeeshan A Zakaria on 5/24/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        // Enable the bluetooth
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        // Auto-Start the app
        // The user may have manually started the app before the app's auto-start takes place - as there is a delay between
        // the home screen display and this code actually runs to start the app.
        // Check this case and only start the app if not already running as a foreground app.
        if (!Common.isAppInForeground(context, Common.MYAPP_PACKAGE_NAME)) {
            Log.v(TAG, "Starting the app after the device (re)boot.");
            Intent myIntent = new Intent(context, Entry.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
    }
}