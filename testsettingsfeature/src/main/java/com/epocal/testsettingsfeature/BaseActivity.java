package com.epocal.testsettingsfeature;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 *
 * Created by Zeeshan A Zakaria on 3/21/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public static final int TEST_SETTINGS = 100;
    public static final int GENERAL_TEST_SETTINGS = 101;
    public static final int DATA_ENTRY_SETTINGS = 102;
    public static final int UNITS_AND_REPORTABLE_RANGES = 103;
    public static final int RANGES = 104;
    public static final int FIELD_SETTINGS = 105;
    public static final int CUSTOM_FIELDS = 106;
    public static final int BG_PLUS_SETTINGS = 107;
    public static final int E_PLUS_SETTINGS = 108;
    public static final int M_PLUS_SETTINGS = 109;
    public static final int TEST_SELECTION = 110;
    public static final int DELIVERY_SYSTEM_EDIT_VALUES = 111;
    public static final int DRAW_SITE_EDIT_VALUES = 112;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void changeTitle(CharSequence title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    /**
     * Go to immersive mode
     */
    public void goFullscreen() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("", "Turning immersive mode mode off. ");
        } else {
            Log.i("", "Turning immersive mode mode on.");
        }

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().
                getDecorView().
                setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
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
