package com.epocal.host4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import static com.epocal.common.Consts.AC;
import static com.epocal.common.Consts.BATTERY;
import static com.epocal.common.Consts.DEVICE_STATE;
import static com.epocal.common.Consts.POWER_TYPE;

/**
 * This class checks if the AC power is connected or the device is on battery, and then update the
 * shared preferences accordingly.
 * <p>
 * Created by Zeeshan A Zakaria on 6/6/2017.
 */

public class PlugInControlReceiver extends BroadcastReceiver {

    SharedPreferences mPrefs;
    Editor mPrefsEditor;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        mPrefs = context.getSharedPreferences(DEVICE_STATE, Context.MODE_PRIVATE);
        mPrefsEditor = mPrefs.edit();

        if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
            mPrefsEditor.putString(POWER_TYPE, AC);
        }
        else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            mPrefsEditor.putString(POWER_TYPE, BATTERY);
        }

        mPrefsEditor.apply();
    }
}
