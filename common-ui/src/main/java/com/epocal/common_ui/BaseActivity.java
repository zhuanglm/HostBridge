package com.epocal.common_ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;

import com.epocal.common.epocobjects.IBaseActivity;

public class BaseActivity extends AppCompatActivity implements IBaseActivity {

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
//    public static final int DELIVERY_SYSTEM_EDIT_VALUES = 111;
//    public static final int DRAW_SITE_EDIT_VALUES = 112;
    private boolean mIsReadOnly;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void changeTitle(CharSequence title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    public void showHomeBack() {
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish(); // Close the settings activity
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.d("Focus debug", "Focus changed !");

        if (!hasFocus) {
            Log.d("Focus debug", "Lost focus !");

            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
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

    public void killActivity() {
        finish();
    }

    @Override
    public boolean isReadOnly() {
        return mIsReadOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        mIsReadOnly = readOnly;
    }
}
