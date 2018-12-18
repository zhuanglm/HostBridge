package com.epocal.testsettingsfeature.unitsandreportablerangesscreen.MPlus;

import com.epocal.common.epocobjects.AnalyteGroups;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.datamanager.AnalyteModel;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * The presenter class for M+
 *
 * Created by Zeeshan A Zakaria on 8/2/2017.
 */

class MPlusPresenter implements IMPlusPresenter {

    private ArrayList<Analyte> mAnalytes;

    MPlusPresenter() {
        setupAnalytes();
    }

    /**
     * Insert the metabolite analytes into the ArrayList
     *
     */
    @Override
    public void setupAnalytes() {
        mAnalytes = new ArrayList<>();
        EnumSet<AnalyteName> metabolite = AnalyteGroups.METABOLITES;
        int index = 0;
        for (AnalyteName analyteName : metabolite) {
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