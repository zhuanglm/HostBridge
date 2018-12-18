package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.selenafamilies;

import com.epocal.common.types.SelenaFamilyType;

/**
 * The view interface
 *
 * Created by Zeeshan A Zakaria on 9/8/2017.
 */

interface ISelenaEditValuesView {
    void switchEnabledState(String itemName, SelenaFamilyType selenaFamilyType, boolean isChecked);
    void switchSelectedState(String itemName, SelenaFamilyType selenaFamilyType, boolean isChecked);
}
