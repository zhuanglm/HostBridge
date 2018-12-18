package com.epocal.host4.FlowController;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;

import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.eventmessages.ExtraInfo;
import com.epocal.common.types.Permissions;
import com.epocal.common.types.UIScreens;
import com.epocal.datamanager.UserModel;
import com.epocal.diagnostics.DiagnosticsActivity;
import com.epocal.epoctestprocedure.QATestTypeMenuActivity;
import com.epocal.host4.AdminReceiver;
import com.epocal.host4.Common;
import com.epocal.host4.ExitAppService;
import com.epocal.host4.InactivityService;
import com.epocal.host4.ScreenReceiver;
import com.epocal.host4.aboutscreen.AboutActivity;
import com.epocal.host4.dmsettingscreen.DMSettingActivity;
import com.epocal.host4.homescreen.HomeScreenActivity;
import com.epocal.host4.homescreen.QATestMenuActivity;
import com.epocal.host4.homescreen.SettingsActivity;
import com.epocal.hostsettings.HostSettingsActivity;
import com.epocal.epoctestprocedure.MultiScreenTestActivity;
import com.epocal.readersettings.ReaderSettingsActivity;
import com.epocal.login.LoginActivity;
import com.epocal.epoctestprocedure.PatientTestActivity;
import com.epocal.readersettings.ReadersListActivity;
import com.epocal.testhistoryfeature.ui.TestHistoryMainActivity;
import com.epocal.testsettingsfeature.TestSettingsActivity;

import static com.epocal.common.CU.allowStatusBarExpansion;

/**
 * The application's main navigation menu
 *
 * Created by bmate on 3/27/2017.
 */

public class AppNavigationController {

    public static void handleNavigationEvent(EpocNavigationObject obj) {

        UIScreens targetScreen = obj.getTargetscreen();
        Context context = obj.getContext();
        ExtraInfo extraInfo1 = obj.getExtraInfo1();
        ExtraInfo extraInfo2 = obj.getExtraInfo2();

        switch (targetScreen) {

            case LoginScreen:
                Bundle parm = null;
                if (extraInfo1 != null) {
                    if (extraInfo1.getName() == "Handle_Inactivity") {
                        parm = new Bundle();
                        parm.putBoolean("Handle_Inactivity",(boolean) extraInfo1.getValue() );
                    }
                }
                if (extraInfo2 != null) {
                }
                launchLoginActivity(context, parm);
                break;
            case AboutScreen:
                launchDiagnosticsActivity(context); // About screen is supposed to show what was showed before in the Diagnostics screen, as per recent decisions made in November 2017. It is not final yet as per date of this typing, but is required regardless to match the SRS for QA.
//                launchAboutActivity(context);
                break;
            case HomeScreen:
                launchHomeScreenActivity(context);
                break;
            case HostSettingsScreen:
                launchHostSettingsActivity(context);
                break;
            case DMSettingScreen:
                launchDMSettingActivity(context);
                break;
            case ReaderSettingsScreen:
                launchReaderSettingsActivity(context);
                break;
            case TestSettingsScreen:
                launchTestSettingsActivity(context);
                break;
            case SettingsScreen:
                launchSettingsActivity(context);
                break;
            case DiagnosticsScreen:
                launchDiagnosticsActivity(context);
                break;
            case TestScreen:
                launchPatientTestScreen(context, extraInfo1, extraInfo2);
                break;
            case RunQATest:
                launchQATestActivity(context);
                break;
            case ENABLE_KIOSK_MODE:
                enableKioskMode(context);
                break;
            case DISABLE_KIOSK_MODE:
                disableKioskMode(context);
                break;
            case EXIT_HOST:
                exitHost(context);
                break;
            case START_INACTIVITY_SERVICE:
                launchInactivityService(context);
                break;
            case STOP_INACTIVITY_SERVICE:
                stopInactivityService(context);
                break;
            case READER_DISCOVERY:
                startReaderDiscovery(context, extraInfo1, extraInfo2);
                break;
            case MULTITEST_SCREEN:
                launchMultiTestScreenActivity(context);
                break;
            case TestHistoryScreen:
                launchTestHistory(context, extraInfo1);
                break;
            case QATestTypeMenuScreen:
                startQATestTypeMenuScreen(context);
                break;
            case QATestMenuScreen:
                startQATestMenuScreen(context);
                break;
            default:
                break;
        }
        if(obj.getFinishContext() && context instanceof Activity)
            ((Activity)context).finish();
    }

    public static void startQATestTypeMenuScreen(Context context) {
        Intent intent = new Intent(context, QATestTypeMenuActivity.class);
        context.startActivity(intent);
    }

    public static void startQATestMenuScreen(Context context) {
        Intent intent = new Intent(context, QATestMenuActivity.class);
        context.startActivity(intent);
    }

    public static void launchTestHistory(Context context, ExtraInfo extraInfo1) {
        Intent intent = new Intent(context, TestHistoryMainActivity.class);
        if (extraInfo1 != null) {
            intent.putExtra(extraInfo1.getName(), (int)extraInfo1.getValue());
        }
        // Get current user's permissions
        Permissions permissions = new UserModel().getLoggedInUser().getPermission();
        if (permissions == Permissions.HOSTADMINISTRATOR) {
            intent.putExtra(Permissions.HOSTADMINISTRATOR.name(),true);
        }
        context.startActivity(intent);
    }

    public static void startReaderDiscovery(Context context, ExtraInfo extraInfo1, ExtraInfo extraInfo2){
        Intent intent = new Intent(context, ReadersListActivity.class);
        intent.putExtra(extraInfo1.getName(),(int)extraInfo1.getValue()); // ReaderDiscoveryMode    Enum
        if(extraInfo2 != null)
            intent.putExtra(extraInfo2.getName(),(int)extraInfo2.getValue());
        context.startActivity(intent);
    }
    public static void launchLoginActivity(Context context, Bundle parm) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // TODO: Testing this flag. It is equivalent to launchMode=singleTask. It was needed for the activity to be started from the Service which is taking care of user inactivity.
        if (parm != null) {
            intent.putExtras(parm);
        }
        context.startActivity(intent);
    }

    public static void launchInactivityService(Context context) {
        Intent intent = new Intent(context, InactivityService.class);
        context.startService(intent);
    }

    public static void stopInactivityService(Context context) {
        Intent intent = new Intent(context, InactivityService.class);
        context.stopService(intent);
    }

    public static void startScreenReceiver(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(new ScreenReceiver(), filter);
    }

    private static void launchAboutActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    private static void launchHostSettingsActivity(Context context) {
        Intent intent = new Intent(context, HostSettingsActivity.class);
        context.startActivity(intent);
    }

    private static void launchDMSettingActivity(Context context)
    {
        Intent intent = new Intent(context, DMSettingActivity.class);
        context.startActivity(intent);
    }

    private static void launchReaderSettingsActivity(Context context) {
        Intent intent = new Intent(context, ReaderSettingsActivity.class);
        context.startActivity(intent);
    }

    private static void launchTestSettingsActivity(Context context) {
        Intent intent = new Intent(context, TestSettingsActivity.class);
        context.startActivity(intent);
    }

    private static void launchHomeScreenActivity(Context context) {
        Intent intent = new Intent(context, HomeScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try{
            context.startActivity(intent);
        }catch(ActivityNotFoundException e){
            e.printStackTrace();

        }
    }

    private static void launchMultiTestScreenActivity(Context context) {
        Intent intent = new Intent(context, MultiScreenTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        try{
            context.startActivity(intent);
        }catch(ActivityNotFoundException e){
            e.printStackTrace();

        }
    }

    private static void launchSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    private static void launchQATestActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    private static void launchDiagnosticsActivity(Context context) {
        Intent intent = new Intent(context, DiagnosticsActivity.class);
        context.startActivity(intent);
    }

    private static void launchPatientTestScreen(Context context, ExtraInfo extraInfo1, ExtraInfo extraInfo2) {
        Intent intent = new Intent(context, PatientTestActivity.class);
        intent.putExtra(extraInfo1.getName(),(String)extraInfo1.getValue()); // reader bluetooth address (unique)
        intent.putExtra(extraInfo2.getName(),(String)extraInfo2.getValue()); // reader alias name
        context.startActivity(intent);
    }

    private static void enableKioskMode(Context context) {
        Common.enableKioskMode(context);
    }

    /**
     * This code is used to disable the application's Kiosk mode
     *
     * @param context the application context
     */
    private static void disableKioskMode(Context context) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdmin = new ComponentName(context, AdminReceiver.class);

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
        intentFilter.addCategory(Intent.CATEGORY_HOME);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        try {
            dpm.setGlobalSetting(deviceAdmin, Settings.Global.STAY_ON_WHILE_PLUGGED_IN, "0");
            dpm.clearPackagePersistentPreferredActivities(deviceAdmin, context.getPackageName());
            dpm.clearUserRestriction(deviceAdmin, UserManager.DISALLOW_ADJUST_VOLUME);
            dpm.clearUserRestriction(deviceAdmin, UserManager.DISALLOW_FACTORY_RESET);
            dpm.clearUserRestriction(deviceAdmin, UserManager.DISALLOW_ADD_USER);
            dpm.clearUserRestriction(deviceAdmin, UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA);
        } catch (Exception e) {
            Log.e(com.epocal.common.Consts.TAG, "Exception: " + e);
        }
    }

    private static void exitHost(Context context) {
        disableKioskMode(context);
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdmin = new ComponentName(context, AdminReceiver.class);
        dpm.clearPackagePersistentPreferredActivities(deviceAdmin, context.getPackageName());

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startService(new Intent(context, ExitAppService.class));
        context.startActivity(startMain);

        allowStatusBarExpansion(context);

    }
}
