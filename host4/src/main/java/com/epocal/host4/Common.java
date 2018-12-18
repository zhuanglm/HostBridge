package com.epocal.host4;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import static com.epocal.common.Consts.TAG;

/**
 *
 * Created by Zeeshan A Zakaria on 3/23/2017.
 
 */

public class Common {
    public static final String MYAPP_PACKAGE_NAME = "com.epocal.host4";

    static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static String getHomeActivity(Context c) {
        PackageManager packageManager = c.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ComponentName componentName = intent.resolveActivity(packageManager);
        if (componentName != null)
            return componentName.flattenToShortString();
        else
            return "none";
    }

    /**
     * Set our defined activity as the default home activity, i.e. the launcher activity
     *
     * @param context the application context
     */
    public static void becomeHomeActivity(Context context) {
        ComponentName deviceAdmin = new ComponentName(context, AdminReceiver.class);
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        if (!dpm.isAdminActive(deviceAdmin)) {
            showToast(context, "This app is not a device admin!");
            return;
        }
        if (!dpm.isDeviceOwnerApp(context.getPackageName())) {
            showToast(context, "This app is not the device owner!");
            return;
        }
//        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
//        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
//        intentFilter.addCategory(Intent.CATEGORY_HOME);
//        ComponentName activity = new ComponentName(context, LoginActivity.class);
//        dpm.addPersistentPreferredActivity(deviceAdmin, intentFilter, activity);
//        showToast(context, "Home activity: " + getHomeActivity(context));
    }

    public static void disableKioskMode() {

    }

    /**
     * Set the device in Kiiosk mode
     *
     * @param context   the application context
     */
    public static void enableKioskMode(Context context) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdmin = new ComponentName(context, AdminReceiver.class);
        if (!dpm.isAdminActive(deviceAdmin)) {
            Toast.makeText(context, "This app is not a device admin!", Toast.LENGTH_SHORT).show();
        }
        if (dpm.isDeviceOwnerApp(context.getPackageName())) {
            dpm.setLockTaskPackages(deviceAdmin, new String[]{context.getPackageName()});
        } else {
            Toast.makeText(context, "This app is not the device owner!", Toast.LENGTH_SHORT).show();
        }

        ComponentName componentName = new ComponentName(context, AdminReceiver.class);
        final String Battery_PLUGGED_ANY = Integer.toString(
                BatteryManager.BATTERY_PLUGGED_AC |
                        BatteryManager.BATTERY_PLUGGED_USB |
                        BatteryManager.BATTERY_PLUGGED_WIRELESS);

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
        intentFilter.addCategory(Intent.CATEGORY_HOME);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        try {
            if (dpm.isLockTaskPermitted(context.getPackageName())) {
                Activity activity = (Activity) context;
                activity.startLockTask();
                dpm.setGlobalSetting(componentName, Settings.Global.STAY_ON_WHILE_PLUGGED_IN, Battery_PLUGGED_ANY);
                dpm.addUserRestriction(deviceAdmin, UserManager.DISALLOW_ADJUST_VOLUME);
                dpm.addUserRestriction(deviceAdmin, UserManager.DISALLOW_FACTORY_RESET);
                dpm.addUserRestriction(deviceAdmin, UserManager.DISALLOW_ADD_USER);
                dpm.addUserRestriction(deviceAdmin, UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA);
                /* For APIs > 22
                dpm.addUserRestriction(deviceAdmin, UserManager.DISALLOW_SAFE_BOOT);
                dpm.setKeyguardDisabled(componentName, true);
                dpm.setStatusBarDisabled(componentName, true);
                */

                dpm.setLockTaskPackages(componentName, new String[]{context.getPackageName()});
                dpm.addPersistentPreferredActivity(
                        componentName, intentFilter, new ComponentName(
                                context.getPackageName(), "com.epocal.host4.init.Entry"));

                Common.becomeHomeActivity(context);
            } else {
                Toast.makeText(context, "Kiosk Mode not permitted", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }

    /**
     *
     * @param input the input String
     * @return      the title case String
     */
    public static String titleCase(String input) {
        String[] words = input.split(" ");
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
            for (int i = 1; i < words.length; i++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
            }
        }
        return sb.toString();
    }

    // Checks if the app identified by the package is currently in foreground. Called from app's Broadcast receiver to determine
    // if the app's activity has been started. The app's broadcast receiver cannot call "isAppRunning()" like in InactivityService
    // as this returns true when the broadcast receiver is running (process with app's package name?) even if the app's activity is not present.
    // NOTE: getRunningTasks() is deprecated but works on API22. In future, we should consider making the app as a main (custom) launcher
    // to remove the delay in auto-starting the app from boot_complete_event and this in turn prevents the user from launching the app manually.
    public static boolean isAppInForeground(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(1);
        ComponentName componentNameInfo = taskInfoList.get(0).topActivity;
        //Log.v(TAG, "Array size="+taskInfoList.size()+" componentNameInfo class="+componentNameInfo.getClassName()+ " package="+componentNameInfo.getPackageName());
        if (componentNameInfo.getPackageName().equals(packageName)) {
            return true;
        }
        return false;
    }
}
