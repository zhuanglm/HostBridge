package com.epocal.common.eventmessages;

import com.epocal.common.realmentities.Reader;

import io.reactivex.internal.operators.maybe.MaybeDefer;

/**
 * Reader device model object
 *
 * Created by dning on 5/2/2017.
 */

public class ReaderDevice {
    private String mDeviceName;
    private String mDeviceAddress;
    private boolean isBonded;
    private boolean mDedicated;

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public String getDeviceAddress() {
        return mDeviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        mDeviceAddress = deviceAddress;
    }

    public boolean getisBonded() {
        return isBonded;
    }

    public void setisBonded(boolean bonded) {
        isBonded = bonded;
    }

    public String getDeviceId() {
        return mDeviceName.substring(0, 6);
    }

    public String getDeviceAlias() {
        return mDeviceName.substring(6, mDeviceName.length());
    }

    public boolean getDedicated() {
        return mDedicated;
    }

    public void setDedicated(boolean dedicated) {
        mDedicated = dedicated;
    }

    public Reader toReader() {
        Reader reader = new Reader();
        reader.setBluetoothAddress(mDeviceAddress);
        reader.setDeviceName(mDeviceName);
        reader.setAlias(getDeviceAlias());
        reader.setSerialNumber(getDeviceId());
        reader.setDedicated(getDedicated());
        return reader;
    }
}
