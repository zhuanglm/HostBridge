package com.epocal.testsettingsfeature.unitsandreportablerangesscreen.EPlus;

import com.epocal.common.epocobjects.AnalyteGroups;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.datamanager.AnalyteModel;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * The presenter class for E+
 *
 * Created by Zeeshan A Zakaria on 8/2/2017.
 */

class EPlusPresenter implements IEPlusPresenter {

    private ArrayList<Analyte> mAnalytes;

    EPlusPresenter() {
        setupAnalytes();
    }

    /**
     * Insert the chemical analytes into the ArrayList
     *
     */
    @Override
    public void setupAnalytes() {
        mAnalytes = new ArrayList<>();
        EnumSet<AnalyteName> chemicals = AnalyteGroups.CHEMICALS;
        int index = 0;
        for (AnalyteName analyteName : chemicals) {
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