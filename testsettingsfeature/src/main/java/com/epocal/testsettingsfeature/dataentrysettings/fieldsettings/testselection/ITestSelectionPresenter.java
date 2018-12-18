package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.testselection;

import com.epocal.common.types.am.AnalyteName;

import java.util.ArrayList;

/**
 * The Interface
 *
 * Created by Zeeshan A Zakaria on 8/28/2017.
 */

interface ITestSelectionPresenter {
    boolean switchEnabledState(AnalyteName analyteName, boolean isChecked);
    boolean switchSelectedState(AnalyteName analyteName, boolean isChecked);
    boolean switchEnabledState(ArrayList<AnalyteName> group, boolean isChecked);
    boolean switchSelectedState(ArrayList<AnalyteName> group, boolean isChecked);

}
