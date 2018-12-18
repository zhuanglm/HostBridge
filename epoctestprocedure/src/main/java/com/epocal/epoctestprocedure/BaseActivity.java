package com.epocal.epoctestprocedure;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * TODO: Add a file header comment !
 * Created on 23/06/2017.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.primaryBlueNew)));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_window_close24);
            showHideCancelTestIcon(false);
        }
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryBlueNew));
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

    public void showHideCancelTestIcon(boolean show) {
        if (getSupportActionBar() != null) {
            if (show) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }
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

    public void changeTitle(CharSequence title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }
}