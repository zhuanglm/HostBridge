package com.epocal.hostsettings;

/**
 * Interface to create a helper class to convert
 * Realm entity value to MenuItem
 *
 */
public interface IMenuItemFromDataEntityHelper {
    String getValue();
    boolean isEnabled();
    boolean isChecked();
}
