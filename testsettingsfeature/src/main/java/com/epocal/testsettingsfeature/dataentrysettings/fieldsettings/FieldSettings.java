package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings;

import android.content.Context;

import com.epocal.testsettingsfeature.R;

import java.util.HashMap;

/**
 * This file contains the list of items which are displayed on the Host Settings main screen.
 *
 * Created by Zeeshan A Zakaria on 4/11/2017.
 */

class FieldSettings {

    private HashMap<String, String[]> mListItems;
    String[] getHeaders() {
        return mListDataHeader;
    }
    private String [] mListDataHeader;
    HashMap<String, String[]> getListItems() {
        return mListItems;
    }
    private Context mContext;

    FieldSettings(Context context) {
        mContext = context;
        getListData();
    }

    /*
     * Preparing the list data
     */
    private void getListData() {
        mListDataHeader = new String[8];
        mListItems = new HashMap<>();

        mListDataHeader[0] = mContext.getString(R.string.patient_id);
        mListDataHeader[1] = mContext.getString(R.string.id2);
        mListDataHeader[2] = mContext.getString(R.string.test_selection);
        mListDataHeader[3] = mContext.getString(R.string.sample_type);
        mListDataHeader[4] = mContext.getString(R.string.delivery_system);
        mListDataHeader[5] = mContext.getString(R.string.draw_site);
        mListDataHeader[6] = mContext.getString(R.string.respiratory_mode);
        mListDataHeader[7] = mContext.getString(R.string.allens_test);


        String [] patientId = new String[3];
        patientId[0] = mContext.getString(R.string.maximum_length);
        patientId[1] = mContext.getString(R.string.minimum_length);
        patientId[2] = mContext.getString(R.string.patient_id_lookup);

        String [] id2 = new String[2];
        id2[0] = mContext.getString(R.string.maximum_length);
        id2[1] = mContext.getString(R.string.minimum_length);

        String [] testSelection = new String[0]; // TODO: This needs to be done
        testSelection[0] = "[PLACE HOLDER]";

        String[] sampleType  = new String[1];
        sampleType[0] = "[PLACE HOLDER]";



        String [] deliverySettings = new String[2];
        deliverySettings[0] = mContext.getString(R.string.allow_user_input);
        deliverySettings[1] = mContext.getString(R.string.edit_values);

        String [] drawSite = new String[3];
        drawSite[0] = mContext.getString(R.string.allow_user_input);
        drawSite[1] = mContext.getString(R.string.edit_values);

        String [] mode = new String[3];
        mode[0] = mContext.getString(R.string.allow_user_input);
        mode[1] = mContext.getString(R.string.edit_values);

        String [] allensTest = new String[3];
        allensTest[0] = mContext.getString(R.string.allow_user_input);
        allensTest[1] = mContext.getString(R.string.edit_values);

        mListItems.put(mListDataHeader[0], patientId);
        mListItems.put(mListDataHeader[1], id2);
        mListItems.put(mListDataHeader[2], testSelection);
        mListItems.put(mListDataHeader[3], sampleType);
        mListItems.put(mListDataHeader[5], deliverySettings);
        mListItems.put(mListDataHeader[6], drawSite);
        mListItems.put(mListDataHeader[7], mode);
        mListItems.put(mListDataHeader[8], allensTest);
    }
}