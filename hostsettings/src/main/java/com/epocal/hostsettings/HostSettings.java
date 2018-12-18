package com.epocal.hostsettings;

import com.epocal.datamanager.WorkflowRepository;

import java.util.HashMap;

/**
 * This file contains the list of items which are displayed on the Host Settings main screen.
 *
 * Created by Zeeshan A Zakaria on 4/11/2017.
 */

class HostSettings {

    private HashMap<String, String[]> listItems;

    String[] getHeaders() {
        return listDataHeader;
    }

    private String [] listDataHeader;

    HashMap<String, String[]> getListItems() {
        return listItems;
    }

    HostSettings() {
        getListData();
    }

    /*
     * Preparing the list data
     */
    private void getListData() {
        listDataHeader = new String[9];
        listItems = new HashMap<>();

        listDataHeader[0] = "Name settings";
        listDataHeader[1] = "General Host settings";
        listDataHeader[2] = "Operator name and password";
        listDataHeader[3] = "Set language";
        listDataHeader[4] = "Users";
        listDataHeader[5] = "Printers";
        listDataHeader[6] = "Barcode settings";
        listDataHeader[7] = "Upgrade";
        listDataHeader[8] = "Set Workflow";

        String [] nameSettings = new String[2];
        nameSettings[0] = "Host name";
        nameSettings[1] = "Hospital name";

        String [] operatorNameAndPassword = new String[2];
        operatorNameAndPassword[0] = "User name";
        operatorNameAndPassword[1] = "Password";

        String [] generalHostSettings = new String[6];
        generalHostSettings[0] = "Login authentication";
        generalHostSettings[1] = "Enable FIPS";
        generalHostSettings[2] = "Inactivity timer";
        generalHostSettings[3] = "Inactivity logout";
        generalHostSettings[4] = "Power off on logout";
        generalHostSettings[5] = "Temperature unit";

        String setLanguage [] = new String[1];
        setLanguage[0] = "English";

        String [] users = new String[2];
        users[0] = "Add user";
        users[1] = "Edit user";

        String [] printers = new String[3];
        printers[0] = "Print inclusions";
        printers[1] = "Add printer";
        printers[2] = "Edit printer";

        String [] barcodeSettings = new String[7];
        barcodeSettings[0] = "User ID";
        barcodeSettings[1] = "Password";
        barcodeSettings[2] = "Patient ID";
        barcodeSettings[3] = "Fluid Lot";
        barcodeSettings[4] = "ID2";
        barcodeSettings[5] = "Comments";
        barcodeSettings[6] = "Other";

        String [] upgrades = new String[1];
        upgrades[0] = "No upgrades available";

        String[] workflowNames = new WorkflowRepository().getAllWorkflowNames();

        listItems.put(listDataHeader[0], nameSettings);
        listItems.put(listDataHeader[1], generalHostSettings);
        listItems.put(listDataHeader[2], operatorNameAndPassword);
        listItems.put(listDataHeader[3], setLanguage);
        listItems.put(listDataHeader[4], users);
        listItems.put(listDataHeader[5], printers);
        listItems.put(listDataHeader[6], barcodeSettings);
        listItems.put(listDataHeader[7], upgrades);
        listItems.put(listDataHeader[8], workflowNames);

    }
}