package com.epocal.diagnostics;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ExpandableListView;

import com.epocal.common.DeviceInfoUtil;
import com.epocal.common.types.LogVerbosity;
import com.epocal.datamanager.DeviceSettingsModel;
import com.epocal.epoctest.analyticalmanager.AnalyticalManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DiagnosticsActivity extends DiagnosticsBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.about);

        ArrayList<String> menuList = new ArrayList<>();
        HashMap<String, ArrayList<Pair<String, String>>> childMenuList = new HashMap<>();
        populateData(menuList, childMenuList);
        DiagnosticsMenuAdapter menuListAdapter = new DiagnosticsMenuAdapter(DiagnosticsActivity.this, menuList, childMenuList);
        ExpandableListView menuListView = findViewById(R.id.explv_diagnostics_menu);
        menuListView.setAdapter(menuListAdapter);
        showHomeBack();
    }

    private void populateData(ArrayList<String> parentMenu, HashMap<String, ArrayList<Pair<String, String>>> childMenu) {
        String[] menu = {
                "Device Information",
                "Software Information"
        };

//    private void populateData(ArrayList<String> parentMenu, HashMap<String, ArrayList<Pair<String, String>>> childMenu) {
//        String[] menu = {
//                "Device Information",
//                "Software Information",
//                "Software Configuration",
//                "Wifi Information",
//                "Bluetooth Information",
//                "Data Manager Information",
//                "Test Information",
//                "Test Configuration",
//                "Login Configuration",
//                "Reader Information",
//                "User Information",
//                "Printer Information",
//                "Printer Configuration"
//        };

        parentMenu.addAll(Arrays.asList(menu));

        // (1)
        ArrayList<Pair<String, String>> childMenu1 = new ArrayList<>();
        childMenu1.add(new Pair<>("Model #", DeviceInfoUtil.getModel()));
        childMenu1.add(new Pair<>("Serial #", DeviceInfoUtil.getSerialNumber()));
        childMenu1.add(new Pair<>("Battery Level", DeviceInfoUtil.getBatteryLevel(this)));
        childMenu1.add(new Pair<>("Free Memory", DeviceInfoUtil.getAvailableMemory(this)));
        childMenu1.add(new Pair<>("Department", "Default"));
        childMenu1.add(new Pair<>("Time Zone", DeviceInfoUtil.getTimezone()));
        childMenu.put(parentMenu.get(0), childMenu1);

        // (2)
        ArrayList<Pair<String, String>> childMenu2 = new ArrayList<>();
        childMenu2.add(new Pair<>("Host Version", "4.0.0"));
        childMenu2.add(new Pair<>("Host Expiry Date", "30-June-2019"));
        childMenu2.add(new Pair<>("Sensor Config", "33.0"));
        childMenu2.add(new Pair<>("Analytical Version", AnalyticalManager.version()));
        childMenu2.add(new Pair<>("eVAD Version", "N/A"));
        childMenu2.add(new Pair<>("Reader Upgrade Version", "N/A"));
        childMenu2.add(new Pair<>("Language", DeviceInfoUtil.getLanguage()));
        childMenu2.add(new Pair<>("Log Level", getLogLevel(new DeviceSettingsModel().getLogVerbosity())));
        childMenu2.add(new Pair<>("Host Mode", "Legacy 1A"));
        childMenu2.add(new Pair<>("Host Edition", "Bridge"));
        childMenu.put(parentMenu.get(1), childMenu2);
//
//        // (3)
//        ArrayList<Pair<String, String>> childMenu3 = new ArrayList<Pair<String, String>>();
//        childMenu3.add(new Pair<String, String>("Inactivity Timer", "Yes"));
//        childMenu3.add(new Pair<String, String>("Inactivity Timer Period", "10"));
//        childMenu3.add(new Pair<String, String>("Logout When Inactive", "Yes"));
//        childMenu3.add(new Pair<String, String>("PowerOff When Inactive", "Yes"));
//        childMenu3.add(new Pair<String, String>("Background Sync", "No"));
//        childMenu3.add(new Pair<String, String>("Scheduled Sync", "No"));
//        childMenu.put(parentMenu.get(2), childMenu3);
//
//        // (4) Wifi Info
//        ArrayList<Pair<String, String>> childMenu4 = new ArrayList<Pair<String, String>>();
//        childMenu4.add(new Pair<String, String>("SSID", ""));
//        childMenu4.add(new Pair<String, String>("IP Address", ""));
//        childMenu4.add(new Pair<String, String>("MAC Address", ""));
//        childMenu4.add(new Pair<String, String>("Signal Strength", "Disconnected"));
//        childMenu4.add(new Pair<String, String>("dBm", "0"));
//        childMenu4.add(new Pair<String, String>("Wifi Roaming", "False"));
//        childMenu4.add(new Pair<String, String>("FIPS Mode", "False"));
//        childMenu.put(parentMenu.get(3), childMenu4);
//
//        // (5) Bluetooth
//        ArrayList<Pair<String, String>> childMenu5 = new ArrayList<Pair<String, String>>();
//        childMenu5.add(new Pair<String, String>("Bluetooth Address", "00:00:40:ab"));
//        childMenu5.add(new Pair<String, String>("Radio Mode", "Connectable"));
//        childMenu.put(parentMenu.get(4), childMenu5);
//
//        // (6) DataManager
//        ArrayList<Pair<String, String>> childMenu6 = new ArrayList<Pair<String, String>>();
//        childMenu6.add(new Pair<String, String>("Enabled", "No"));
//        childMenu6.add(new Pair<String, String>("DM Address", "0.0.0.0"));
//        childMenu6.add(new Pair<String, String>("DM Port", "-1"));
//        childMenu6.add(new Pair<String, String>("Source Name", ""));
//        childMenu6.add(new Pair<String, String>("Use SSL", "No"));
//        childMenu6.add(new Pair<String, String>("Sync Protocol", "Unknown"));
//        childMenu6.add(new Pair<String, String>("Network Protocol", "Unknown"));
//        childMenu.put(parentMenu.get(5), childMenu6);
//
//        // (7) Test Info
//        ArrayList<Pair<String, String>> childMenu7 = new ArrayList<Pair<String, String>>();
//        childMenu7.add(new Pair<String, String>("All", "0"));
//        childMenu7.add(new Pair<String, String>("Patient", "0"));
//        childMenu7.add(new Pair<String, String>("QA", "0"));
//        childMenu7.add(new Pair<String, String>("Sent", "0"));
//        childMenu7.add(new Pair<String, String>("Unsent", "0"));
//        childMenu7.add(new Pair<String, String>("Not Accepted", "0"));
//        childMenu.put(parentMenu.get(6), childMenu7);
//
//        // (8) Test Configuration
//        ArrayList<Pair<String, String>> childMenu8 = new ArrayList<Pair<String, String>>();
//        childMenu8.add(new Pair<String, String>("Save Row Data", "OnFailure"));
//        childMenu8.add(new Pair<String, String>("Allow Expired Cards", "No"));
//        childMenu8.add(new Pair<String, String>("Allow Recall Data", "Yes"));
//        childMenu8.add(new Pair<String, String>("Patient ID Lookup", "No"));
//        childMenu8.add(new Pair<String, String>("Temperature Unit", "Celsius"));
//        childMenu.put(parentMenu.get(7), childMenu8);
//
//        // (9) Login Config
//        ArrayList<Pair<String, String>> childMenu9 = new ArrayList<Pair<String, String>>();
//        childMenu9.add(new Pair<String, String>("Authrization Mode", "UserIdAndPassword"));
//        childMenu9.add(new Pair<String, String>("Logout When Inactive", "Yes"));
//        childMenu.put(parentMenu.get(8), childMenu9);
//
//        // (10) Reader Info
//        ArrayList<Pair<String, String>> childMenu10 = new ArrayList<Pair<String, String>>();
//        childMenu.put(parentMenu.get(9), childMenu10);
//
//        // (11) User Info
//        ArrayList<Pair<String, String>> childMenu11 = new ArrayList<Pair<String, String>>();
//        childMenu11.add(new Pair<String, String>("User Name", "Administrator"));
//        childMenu11.add(new Pair<String, String>("Last Login", "04-Jun-17"));
//        childMenu11.add(new Pair<String, String>("User ID", "administrator"));
//        childMenu11.add(new Pair<String, String>("Role", "Administrator"));
//        childMenu11.add(new Pair<String, String>("Start Date", "01-Jan-05"));
//        childMenu11.add(new Pair<String, String>("End Date", "01-Jan-20"));
//        childMenu11.add(new Pair<String, String>("IsEnabled", "True"));
//        childMenu.put(parentMenu.get(10), childMenu11);
//
//        // (12) Printer Info
//        ArrayList<Pair<String, String>> childMenu12 = new ArrayList<Pair<String, String>>();
//        childMenu.put(parentMenu.get(11), childMenu12);
//
//        // (12) Printer Config
//        ArrayList<Pair<String, String>> childMenu13 = new ArrayList<Pair<String, String>>();
//        childMenu13.add(new Pair<String, String>("Reference Ranges", "Yes"));
//        childMenu13.add(new Pair<String, String>("Critical Ranges", "Yes"));
//        childMenu13.add(new Pair<String, String>("QA Ranges", "Yes"));
//        childMenu13.add(new Pair<String, String>("Result Documentation", "Yes"));
//        childMenu13.add(new Pair<String, String>("Test Information", "Yes"));
//        childMenu13.add(new Pair<String, String>("Operator Name", "Yes"));
//        childMenu13.add(new Pair<String, String>("Patient Name", "Yes"));
//        childMenu13.add(new Pair<String, String>("Patient Gender", "Yes"));
//        childMenu13.add(new Pair<String, String>("QC Info", "Yes"));
//  //      childMenu13.add(new Pair<String, String>("AM Test Calculate BGE", AnalyticalManager.testCalculateBGE()));
//
//        childMenu.put(parentMenu.get(12), childMenu13);

    }

    private String getLogLevel(LogVerbosity verbosity) {
        String retval = getString(R.string.low);
        switch (verbosity) {
            case None:
            case Low:
                break;
            case Medium:
                retval = getString(R.string.medium);
                break;
            case High:
                retval = getString(R.string.high);
                break;
        }
        return retval;
    }
}
