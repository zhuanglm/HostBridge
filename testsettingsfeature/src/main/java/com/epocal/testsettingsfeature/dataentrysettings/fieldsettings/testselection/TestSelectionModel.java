package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.testselection;

import android.content.Context;

import com.epocal.common.epocobjects.AnalyteGroups;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.datamanager.AnalyteModel;
import com.epocal.testsettingsfeature.R;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * This file contains the list of items which are displayed on the Host Settings main screen.
 *
 * Created by Zeeshan A Zakaria on 4/11/2017.
 */

class TestSelectionModel {

    private HashMap<String, ArrayList<Analyte>> mListOfAnalytes;
    private String [] mListDataHeader;
    private Context mContext;

    TestSelectionModel(Context context) {
        mContext = context;
        setupListData();
    }

    /*
     * Preparing the list data
     */
    private void setupListData() {
        mListDataHeader = new String[3];
        mListOfAnalytes = new HashMap<>();

        mListDataHeader[0] = mContext.getString(R.string.gases_plus);
        mListDataHeader[1] = mContext.getString(R.string.chem_plus);
        mListDataHeader[2] = mContext.getString(R.string.meta_plus);

        ArrayList<Analyte> gasesPlus = setupAnalytes(AnalyteGroups.GASES);
        ArrayList<Analyte> chemicalsPlus = setupAnalytes(AnalyteGroups.CHEMICALS);
        ArrayList<Analyte> metabolitesPlus = setupAnalytes(AnalyteGroups.METABOLITES);

        mListOfAnalytes.put(mListDataHeader[0], gasesPlus);
        mListOfAnalytes.put(mListDataHeader[1], chemicalsPlus);
        mListOfAnalytes.put(mListDataHeader[2], metabolitesPlus);
    }

    String[] getHeaders() {
        return mListDataHeader;
    }

    HashMap<String, ArrayList<Analyte>> getAnalytes() {
        setupListData();
        return mListOfAnalytes;
    }

    ArrayList<AnalyteName> getAnalyteNamesFromGroup(String group) {
        ArrayList<AnalyteName> analyteNames = new ArrayList<>();
        ArrayList<Analyte> analytes = mListOfAnalytes.get(group);
        for (Analyte analyte : analytes) {
            analyteNames.add(analyte.getAnalyteName());
        }
        return analyteNames;
    }

    /**
     *
     * @param name  the analyte group name
     * @return      the analytes in the analyte group
     */
    private ArrayList<Analyte> setupAnalytes(EnumSet<AnalyteName> name) {
        ArrayList<Analyte> analytes = new ArrayList<>();
        int index = 0;
        for (AnalyteName analyteName : name) {
            AnalyteModel analyteModel = new AnalyteModel();
            Analyte analyte = analyteModel.getAnalyte(analyteName);
            analytes.add(index++, analyte);
        }

        return analytes;
    }
}