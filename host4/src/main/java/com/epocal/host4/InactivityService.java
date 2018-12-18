package com.epocal.host4;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.epocal.common.epocexceptions.EpocTestDriverException;
import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.eventmessages.ExtraInfo;
import com.epocal.common.types.UIScreens;
import com.epocal.datamanager.HostConfigurationModel;
import com.epocal.epoctest.TestManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.epocal.common.Consts.COMMUNICATING;
import static com.epocal.common.Consts.DEVICE_STATE;
import static com.epocal.common.Consts.LOGOUT_AND_SCREENOFF;
import static com.epocal.common.Consts.SYNCING;
import static com.epocal.common.Consts.TESTCOMPLETED;
import static com.epocal.common.Consts.TESTING;

/**
 * This class keeps track of the the user inactivity, and logs out the user if the user is not active
 * for a given number of minutes. These inactivity minutes come from the database. It was necessary
 * to use the Service instead of AlarmManager so that we could keep listening to the user interactions
 * and resetting the timer.
 *
 * Created by Zeeshan A Zakaria on 6/2/2017.
 * Updated by Y. Akita-Brunet on 11/29/2017
 *
 * In the Host App, inactivity-logout functionality is called "Inactivity Feature".
 * The user can Enable/Disable this feature by the following:
 * - To Enable the feature, the user sets "Host Settings > Global Host Settings > Inactivity Sign Out" to YES
 * - To Disable, set "Host Settings > Global Host Settings > Inactivity Sign Out" to NO.
 *
 * When it is Enabled, then inactivity timeout duration is set by the following:
 * - "Host Settings > Global Host Settings > Inactivity Timeout" -- enter numeric value between 1 - 30 min.
 *
 * The system is considered inactive when the following conditions met:
 *
 * - When no user input (tap/scroll) on the device screen for a timeout period.
 *   The "BaseActivity" of each screen will sends the message to this service each time the user
 *   touches the screen. When the message (with resetTimer=true) is received,
 *   the service reset the InactivityTimer to handle this condition.
 *   When the timer expires and the callback method is called, it means "inactivity time period" is elapsed.
 *
 * - The app Force-Logout the user when all of the following conditions are met:
 *
 * (1) <b>shouldLogoutWhenInactive</b> -- true/false indicating whether "Inactivity Feature" is Enabled/Disabled.
 *     Can logout when shouldLogoutWhenInactive==true
 *
 * (2) <b>isRunningReaderAdminTask</b> -- true/false indicating whether the Reader admin task is currently running.
 *     NOTE: This flag is mutually exclusive with test related boolean flags: isRunningTest, hasCompletedButNotClosedTest.
 *           When Reader Admin Task is running, then the user cannot run the tests.
 *     Can logout when isRunningReaderAdminTask==false
 *
 * (3) <b>isRunningTest</b> -- true/false indicating whether 1 or more Blood/QA tests are currently running.
 * (4) <b>hasCompletedButNotClosedTest</b> -- true/false indicating whether 1 or more Blood/QA tests are in "CompletedButNotClosed" state.
 *     NOTE: For a given test, isRunningTest and hasCompletedButnotClosedTest are mutually exclusive.
 *     Can logout when isRunningTest==false AND hasCompletedButNotClosedTest==false
 *     Can logout when hasCompletedButNotClosedTest==true AND shouldLogoutOnTestCompleted==true
 *
 *     No logout when isRunningTest==true
 *     No logout when isRunningTest==false AND (hasCompletedButNotClosedTest==true AND shouldLogoutOnTestCompleted==false)
 *
 *     Here's how flag value changes for one test.
 *     -                        EVENT: Initial value | TestManager starts test | test finishes calculation | TestManager ends test                           |
 *     -                isRunningTest: NO            | YES                     | NO                        | NO
 *     - hasCompletedButNotClosedTest: NO            | NO                      | YES                       | NO
 *
 *     TestManager starts test when the user taps on connecting to the reader.
 *     TestManager ends test when the user leaves the test panel, or it encounters error.
 *
 * (5) <b>isRunningSync</b>  -- true/false indicating whether the data sync is in progress.
 *     <b>isRunningUpdate</b> - true/false indicating whether the app upgrade is in progress.
 *     Can logout when isRunningSync==false
 *     Can logout when isRunningUpdate==false
 *
 *
 * NOTE: When the app wants to extends the InactivityTimer, send the following message to the service.
 * Example of extends (reset) the InactivityTimer from BaseActivity.java
 *
 * public void onResume() {
 *     super.onResume();
 *     inactivityBroadcast(true);
 * }
 *
 * private void inactivityBroadcast(boolean value) {
 *     Intent broadcastIntent = new Intent();
 *     broadcastIntent.setAction("com.epocal.host4.inactivity");
 *     broadcastIntent.putExtra("inactivity", value);
 *     sendBroadcast(broadcastIntent);
 * }
 */

public class InactivityService extends Service {

    long mDisconnectTimeout;
    public static final String mBroadcastInactivityAction = "com.epocal.host4.inactivity";
    IntentFilter mIntentFilter;
    SharedPreferences mPrefs;

    private static Handler mDisconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    /**
     * The Broadcast receiver to listen to the messages, and decide whether to reset or stop the timer.
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("<><><>", "Broadcast received");
            if (intent.getAction().equals(mBroadcastInactivityAction) && mDisconnectTimeout > 0) {
                boolean resetTimer = intent.getBooleanExtra("inactivity", false);
                if (resetTimer) resetDisconnectTimer();
                else stopDisconnectTimer();
            }
        }
    };

    /**
     * When timeout value is reached, this block of code is executed. It checks if the user should be
     * logged out. If yes, it send message on the event bus to take the user back to the login screen.
     */
    public Runnable mDisconnectCallback = new Runnable() {
        @Override
        public void run() {
            Log.v("<><><>","ENTER DISCONNECT CALLBACK");
            // There are a set of actions that can be done when the conditions are met
            boolean actionCloseTest = false;
            boolean actionScreenOff = false;
            HostConfigurationModel hcm = new HostConfigurationModel();

            setTimerDuration();

            // Read Config settings from DB
            // Check if the Inactivity Feature is Disabled. If so, reset and return.
            boolean shouldLogoutWhenInactive = hcm.getUnmanagedHostConfiguration().isLogoutWhenInactive();
            Log.v("<><><>","shouldLogoutWhenInactive: " + shouldLogoutWhenInactive);
            if (!shouldLogoutWhenInactive) {
                Log.v("<><><>","resetDisconnectTimer()");
                resetDisconnectTimer();
                return;
            }

            // Inactivity Feature is Enabled. Check other conditions to logout.
            boolean isRunningSync = mPrefs.getBoolean(SYNCING, false);
            boolean isRunningReaderAdminTask = mPrefs.getBoolean(COMMUNICATING, false);
            boolean isRunningTest = mPrefs.getBoolean(TESTING, false);
            boolean hasCompletedButNotClosedTest = mPrefs.getBoolean(TESTCOMPLETED, false);
            boolean shouldLogoutOnTestCompleted = hcm.getUnmanagedHostConfiguration().isCloseUnattendedTests();
            Log.v("<><><>","isRunningSync: " + isRunningSync + " isRunningReaderAdminTask:" + isRunningReaderAdminTask + " isRunningTest: " + isRunningTest + " hasCompletedButNotClosedTest:" + hasCompletedButNotClosedTest + " shouldLogoutOnTestCompleted: " + shouldLogoutOnTestCompleted);
            // TODO: Add isRunningReaderAdminTask in if statement when the flag is implemented
            if (isRunningSync || isRunningReaderAdminTask || isRunningTest || (hasCompletedButNotClosedTest && !shouldLogoutOnTestCompleted)) {
                Log.v("<><><>","resetDisconnectTimer()");
                resetDisconnectTimer();
                return;
            } else if (hasCompletedButNotClosedTest && shouldLogoutOnTestCompleted) {
                actionCloseTest = true;
            }


            // If we reach here, the logout conditions are met!




            // Determine the action.
            try {
                Log.v("<><><>","logout(actionCloseTest)");
                logout(actionCloseTest);
            } catch (EpocTestDriverException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Log out the user
     * this is never used.
     */
    private void logout() {
        // Auto log out
        System.out.println("<><><><><><><><><><><><>     USER TIMED OUT     <><><><><><><><><><><><>");
//            new UserModel().getLoggedInUser().setLoggedIn(false);
        EpocNavigationObject message = new EpocNavigationObject();
        message.setContext(getApplicationContext());
        message.setTargetscreen(UIScreens.LoginScreen);
        EventBus.getDefault().post(message);
    }
    private void logout_x() throws EpocTestDriverException {
        try {
            TestManager.getInstance().closeTestsAndLogout();
        } catch (EpocTestDriverException e) {
            e.printStackTrace();
        }
    }
    /**
     * Log out the user with the option of closing test and turn off screen
     */
    private void logout(boolean shouldCloseTest) throws EpocTestDriverException {
        // Auto log out
        System.out.println("<><><><><><><><><><><><>     device TIMED OUT     <><><><><><><><><><><><>");
        if (shouldCloseTest) {
        logout_x();

        } else {
            EpocNavigationObject message = new EpocNavigationObject();
            message.setContext(getApplicationContext());
            message.setTargetscreen(UIScreens.LoginScreen);
            message.setExtraInfo1(new ExtraInfo("Handle_Inactivity",true));
            EventBus.getDefault().post(message);
        }

    }

    /**
     * Log out the user then turn the screen off
     */
    private void logoutAndScreenOff() {
        System.out.println("<><><><><><><><><><><><>     USER TIMED OUT     <><><><><><><><><><><><>");
        EpocNavigationObject message = new EpocNavigationObject();
        message.setContext(getApplicationContext());
        message.setTargetscreen(UIScreens.LoginScreen);
        message.setExtraInfo1(new ExtraInfo(LOGOUT_AND_SCREENOFF, "true"));
        EventBus.getDefault().post(message);
    }

    /**
     * Log out the user and power off the device. Need the device to be rooted
     */
    private void logoutAndPowerOff() {
        logout();
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", true);
        startActivity(intent);
    }


    private void setTimerDuration() {
        mDisconnectTimeout = new HostConfigurationModel().getUnmanagedHostConfiguration().getInactivityTimer();
        mDisconnectTimeout *= 60000; // Converting to milliseconds
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        
        setTimerDuration();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastInactivityAction);
        registerReceiver(mReceiver, mIntentFilter);
        mPrefs = getSharedPreferences(DEVICE_STATE, Context.MODE_PRIVATE);
        Log.v("<><><>", "Inactivity timer service started, timer for minutes: " + mDisconnectTimeout);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // So that the system restarts the Service if it gets killed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("<><><>", "Timer destroyed");
        stopDisconnectTimer();
        unregisterReceiver(mReceiver);

        if (isAppRunning(InactivityService.this, "com.epocal.host4")) {
            startService(new Intent(this, InactivityService.class)); // Restating the Service if it is destroyed
        }
    }

    public void onTaskRemoved(Intent rootIntent) {
        stopDisconnectTimer(); // If the app that started the service goes away, stop the timer.
    }

    public void resetDisconnectTimer() {
        mDisconnectHandler.removeCallbacks(mDisconnectCallback);
        if (new HostConfigurationModel().getUnmanagedHostConfiguration().isEnableInactivityTimer()) {
            System.out.println("<><><><><><><><><><><><>     TIMER RESET     <><><><><><><><><><><><>");
            mDisconnectHandler.postDelayed(mDisconnectCallback, mDisconnectTimeout);
        } else {
            Log.v("<><><>","Inactivity timer disabled");
        }
    }

    /**
     * Stop the timer. This is required when the time out value is set to 0, or when the Host device is in its cradle.
     */
    public void stopDisconnectTimer() {
        mDisconnectHandler.removeCallbacks(mDisconnectCallback);
    }

    private boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}