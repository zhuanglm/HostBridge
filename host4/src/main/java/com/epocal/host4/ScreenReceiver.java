package com.epocal.host4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            Log.v("SCREEN_RECEIVER", "ACTION_SCREEN_OFF");
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d("SCREEN_RECEIVER", "SET SCEREN OFF TIMEOUT to MAX");
            Settings.System.putString(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, String.valueOf(Integer.MAX_VALUE));
        }
    }
}

