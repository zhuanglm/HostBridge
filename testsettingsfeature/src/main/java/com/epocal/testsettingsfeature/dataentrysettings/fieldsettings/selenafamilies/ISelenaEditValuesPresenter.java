package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.selenafamilies;

import com.epocal.common.types.SelenaFamilyType;

/**
 * The Interface
 *
 * Created by Zeeshan A Zakaria on 9/08/2017.
 */

interface ISelenaEditValuesPresenter {
    boolean switchEnabledState(String itemName, SelenaFamilyType selenaFamilyType, boolean isChecked);
    boolean switchSelectedState(String itemName, SelenaFamilyType selenaFamilyType, boolean isChecked);

}
