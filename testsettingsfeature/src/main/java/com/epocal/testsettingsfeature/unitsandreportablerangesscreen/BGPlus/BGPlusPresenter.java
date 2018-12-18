package com.epocal.testsettingsfeature.unitsandreportablerangesscreen.BGPlus;

import com.epocal.common.epocobjects.AnalyteGroups;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.datamanager.AnalyteModel;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * The presenter class for BG+
 *
 * Created by Zeeshan A Zakaria on 8/2/2017.
 */

class BGPlusPresenter implements IBGPlusPresenter {

    private ArrayList<Analyte> mAnalytes;

    BGPlusPresenter() {
        setupAnalytes();
    }

    /**
     * Insert the gas analytes into the ArrayList
     *
     */
    @Override
    public void setupAnalytes() {
        mAnalytes = new ArrayList<>();
        EnumSet<AnalyteName> gases = AnalyteGroups.GASES;
        int index = 0;
        for (AnalyteName analyteName : gases) {
            AnalyteModel analyteModel = new AnalyteModel();
            Analyte analyte = analyteModel.getAnalyte(analyteName);
            mAnalytes.add(index++, analyte);
        }
    }

    @Override
    public ArrayList<Analyte> getAnalytes() {
        return mAnalytes;
    }
}