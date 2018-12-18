package com.epocal.testhistoryfeature;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

/**
 * Base class for all activities in epoc host app.
 * Inactivity broadcast is the main functionality that needs to be inherited by
 * all in the app. In addition, some utility methods common to all are provided here.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.primaryBlueNew)));
        }
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarBackgroundColor));
    }

    // TODO: Investigate why this method needed here?
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public void changeTitle(CharSequence title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
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
     */
    private void inactivityBroadcast() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.epocal.host4.inactivity");
        broadcastIntent.putExtra("inactivity", true);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onUserInteraction() {
        inactivityBroadcast();
    }

    @Override
    public void onResume() {
        super.onResume();
        inactivityBroadcast();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
