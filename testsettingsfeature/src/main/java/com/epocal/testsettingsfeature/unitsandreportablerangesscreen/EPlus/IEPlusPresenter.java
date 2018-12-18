package com.epocal.testsettingsfeature.unitsandreportablerangesscreen.EPlus;

import com.epocal.common.realmentities.Analyte;

import java.util.ArrayList;

/**
 * The presenter interface for E+
 *
 * Created by Zeeshan A Zakaria on 8/9/2017.
 */

interface IEPlusPresenter {
    ArrayList<Analyte> getAnalytes();
    void setupAnalytes();
}
