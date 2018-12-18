package com.epocal.common;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.TimeZone;

/**
 * DeviceInfoUtil provides the methods to retrieve the following Device related data.
 * - Device's Manufacture and Model
 * - Serial Number
 * - Battery level (%)
 * - Available Memory (%)
 * - Timezone
 * - Language
 * Created on 06/06/2017.
 */
public class DeviceInfoUtil {
    public static final String TAG = DeviceInfoUtil.class.getSimpleName();

    public static String getDeviceID() {return Build.ID;}

    public static String getModel() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }

    public static String getSerialNumber() {
        return Build.SERIAL;
    }

    public static String getAndroidVersionNumber() {
        return Build.VERSION.RELEASE;
    }

    public static String getAndroidVersionCode() {
        return Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
    }

    public static int getBatteryLevelValue(@NonNull final Context context) {
        BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public static String getBatteryLevel(@NonNull final Context context) {
        int batteryLevel = getBatteryLevelValue(context);
        return String.valueOf(batteryLevel) + "%";
    }

    public static int getAvailableMemoryValue(@NonNull final Context context) {
        //https://developer.android.com/reference/android/app/ActivityManager.MemoryInfo.html
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;
        double availableMemoryRatio = mi.availMem / (double) mi.totalMem;
        return (int) Math.floor(availableMemoryRatio * 100);
    }

    public static String getAvailableMemory(@NonNull final Context context) {
        int availableMemory = getAvailableMemoryValue(context);
        return String.valueOf(availableMemory) + "%";
    }

    public static String getTimezone() {
        return TimeZone.getDefault().getID();
    }

    public static String getLanguage() {
        return Locale.getDefault().getDisplayLanguage();
    }

//    public static void test(@NonNull final Context context) {
//        Log.i(TAG, "availableMemory="+getAvailableMemory(context));
//        Log.i(TAG, "Timezone="+getTimezone());
//    }
    public static String getWifiMac(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();

        return wi.getMacAddress().trim();
    }

    public static String getBtAddress() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Field field = null;
        try {
            field = BluetoothAdapter.class.getDeclaredField("mService");
            field.setAccessible(true);
            Object bluetoothManagerService = field.get(bluetoothAdapter);
            if (bluetoothManagerService == null) {
                return null;
            }
            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
            if (method != null) {
                Object obj = method.invoke(bluetoothManagerService);
                if (obj != null) {
                    return obj.toString();
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    }