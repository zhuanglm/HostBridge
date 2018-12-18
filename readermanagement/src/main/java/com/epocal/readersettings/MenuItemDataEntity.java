package com.epocal.readersettings;


/**
 * MenuItem whose data is backed by Realm data entity object.
 * - constructor will take helper class to map following 2 fields.
 *   dataEntity to menuItem.value
 *   dataEntity to menuItem.isEnabled
 * <p>
 * Created on 01/15/2017.
 * <p>
 * Based on class in hostsettings
 */

public class MenuItemDataEntity extends MenuItem {
    private IMenuItemFromDataEntityHelper mDataHelper;

    public MenuItemDataEntity(int id, String title, IMenuItemFromDataEntityHelper helper) {
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
