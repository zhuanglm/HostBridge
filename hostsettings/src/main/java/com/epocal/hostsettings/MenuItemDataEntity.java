package com.epocal.hostsettings;


import com.epocal.common_ui.types.UIEditFieldType;

/**
 * MenuItem whose data is backed by Realm data entity object.
 * - constructor will take helper class to map following 2 fields.
 *   dataEntity to menuItem.value
 *   dataEntity to menuItem.isEnabled
 *
 */

public class MenuItemDataEntity extends MenuItem {
    private IMenuItemFromDataEntityHelper mDataHelper;

    public MenuItemDataEntity(int id, String title, IMenuItemFromDataEntityHelper helper, UIEditFieldType uiEditFieldType) {
        super(id, title, uiEditFieldType);
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

    @Override
    public boolean isChecked() { return mDataHelper.isChecked(); }
}
