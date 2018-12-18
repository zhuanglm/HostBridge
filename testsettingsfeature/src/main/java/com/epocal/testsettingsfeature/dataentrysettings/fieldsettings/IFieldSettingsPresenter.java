package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings;

import com.epocal.common.realmentities.InputFieldConfig;
import com.epocal.common.types.SelenaFamilyType;

import java.util.HashMap;

/**
 *
 * Created by Zeeshan A Zakaria on 4/11/2017.
 */

interface IFieldSettingsPresenter {
    boolean itemClicked(int childId);
    boolean groupClicked(int groupId);
    void switchToggled(int childId, boolean itemValue);
    HashMap<String, String[]> getListItems();
    String[] getListHeaders();
    void setAllowCustomEntry(SelenaFamilyType selenaFamilyType, boolean newValue);
    void saveInputFieldConfig(InputFieldConfig inputFieldConfig);
}
