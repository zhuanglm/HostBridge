package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.testselection;

import com.epocal.common.types.am.AnalyteName;

import java.util.ArrayList;

/**
 * The view interface
 *
 * Created by Zeeshan A Zakaria on 9/6/2017.
 */

interface ITestSelectionView {
    void switchEnabledState(AnalyteName analyteName, boolean isChecked);
    void switchEnabledState(ArrayList<AnalyteName> group, boolean isChecked);
    void switchSelectedState(AnalyteName analyteName, boolean isChecked);
    void switchSelectedState(ArrayList<AnalyteName> group, boolean isChecked);
}
