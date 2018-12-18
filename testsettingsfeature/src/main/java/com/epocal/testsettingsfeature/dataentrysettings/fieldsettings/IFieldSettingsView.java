package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings;

import com.epocal.common.realmentities.InputFieldConfig;
import com.epocal.common.types.SelenaFamilyType;

/**
 *
 * Created by Zeeshan A Zakaria on 4/11/2017.
 */

interface IFieldSettingsView {
    void showDialogAllowUserInput(SelenaFamilyType selenaFamilyType, boolean allowed);
    void showDialogMaxMinLength(InputFieldConfig inputFieldConfig, String value, boolean maximum);
    void receiveSwitchToggle(long itemId, boolean itemValue);
    void gotoTestSelection();
    void gotoSelenaEditValues(SelenaFamilyType selenaFamilyType);
}
