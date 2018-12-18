package com.epocal.testsettingsfeature.unitsandreportablerangesscreen.MPlus;

import com.epocal.common.realmentities.Analyte;

import java.util.ArrayList;

/**
 * The presenter interface for M+
 *
 * Created by Zeeshan A Zakaria on 8/9/2017.
 */

interface IMPlusPresenter {
    ArrayList<Analyte> getAnalytes();
    void setupAnalytes();
}
