package com.epocal.host4.init;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.epocal.host4.FlowController.AppNavigationController;

import static com.epocal.common.CU.preventStatusBarExpansion;

public class Entry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do whatever it is needed.
        // e.g. if there was an error in the boot procedure ('app' execution),
        // than direct to a global error screen

        // TODO: Below two lines are to implement SCREEN_OFF upon inactivity timeout.
        // TODO: Currently commented out due to existing issue with manual screen OFF -> ON.
        // TODO: When screen is OFF manually after inactivity service request to launch LoginScreen,
        // TODO: then turn screen back ON, Login screen is not displayed. Instead, previous (to Login Screen)
        // TODO: is displayed.
        //checkSystemSettingsWritePermission();
        //AppNavigationController.startScreenReceiver(this);
        // TODO: END_TODO

        AppNavigationController.launchLoginActivity(this, null);
        AppNavigationController.launchInactivityService(this);
    }

    private void checkSystemSettingsWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                // Do stuff here
                Log.d("LoginActivity", "WRITE_SETTINGS_PERMISSION success");
            } else{
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}