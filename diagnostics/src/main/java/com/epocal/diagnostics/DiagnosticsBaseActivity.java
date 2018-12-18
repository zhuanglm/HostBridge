package com.epocal.diagnostics;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.epocal.common_ui.BaseActivity;

/**
 * Created by akaiya on 05/06/2017.
 */
public class DiagnosticsBaseActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorActionBar));
    }

    /**
     * Broadcasts message to the InactivityService which is taking care of user inactivity timer.
     *
     * @param value the flag to convey whether to logout the user (true) or stop the timer (false)
     */
    private void inactivityBroadcast(boolean value) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.epocal.host4.inactivity");
        broadcastIntent.putExtra("inactivity", value);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onUserInteraction() {
        inactivityBroadcast(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        inactivityBroadcast(true);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}