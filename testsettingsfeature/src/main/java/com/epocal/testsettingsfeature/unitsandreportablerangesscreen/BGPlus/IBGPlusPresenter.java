package com.epocal.testsettingsfeature.unitsandreportablerangesscreen.BGPlus;

import com.epocal.common.realmentities.Analyte;

import java.util.ArrayList;

/**
 * The presenter interface for BG+
 *
 * Created by Zeeshan A Zakaria on 8/2/2017.
 */

interface IBGPlusPresenter {
    ArrayList<Analyte> getAnalytes();
    void setupAnalytes();
}
