package com.epocal.readersettings;

/**
 * Interface to create a helper class to convert
 * Realm entity value to MenuItem
 * <p>
 * Created on 01/15/2017.
 * <p>
 * Based on class in hostsettings
 */
public interface IMenuItemFromDataEntityHelper {
    String getValue();
    boolean isEnabled();
}
