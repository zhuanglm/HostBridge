package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings;


/**
 * FieldSettingsMenuItem whose data is backed by Realm data entity object.
 * - constructor will take helper class to map following 2 fields.
 * dataEntity to menuItem.value
 * dataEntity to menuItem.isEnabled
 */

class FieldSettingsMenuItemDataEntity extends FieldSettingsMenuItem {
    private IFieldSettingsMenuItemFromDataEntityHelper mDataHelper;

    FieldSettingsMenuItemDataEntity(int id, String title, IFieldSettingsMenuItemFromDataEntityHelper helper) {
        super(id, title);
        mDataHelper = helper;
    }

    @Override
    public String getValue() {
        return mDataHelper.getValue();
    }

    @Override
    public boolean isEnabled() {
        return mDataHelper.isEnabled();
    }
}
