package com.epocal.epoctest.type;

import com.epocal.reader.type.AdditionalIdInfo;
import com.epocal.reader.type.Legacy.LegacyInfo;
import com.epocal.reader.type.MainIdInfo;

/**
 * Created by dning on 7/20/2017.
 */

public class DeviceIdInfoData {
    private AdditionalIdInfo mDeviceIdAdditionalIdInfo;

    public AdditionalIdInfo getDeviceIdAdditionalIdInfo() {
        return mDeviceIdAdditionalIdInfo;
    }

    public void setDeviceIdAdditionalIdInfo(AdditionalIdInfo mDeviceIdAdditionalIdInfo) {
        this.mDeviceIdAdditionalIdInfo = mDeviceIdAdditionalIdInfo;
    }

    private MainIdInfo mDeviceIdMainIdInfo;

    public MainIdInfo getDeviceIdMainIdInfo() {
        return mDeviceIdMainIdInfo;
    }

    public void setDeviceIdMainIdInfo(MainIdInfo mDeviceIdMainIdInfo) {
        this.mDeviceIdMainIdInfo = mDeviceIdMainIdInfo;
    }

    //added by rzhuang at July 27 2018 for Legacy
    private LegacyInfo  mLegacyDeviceInfo;

    public void setLegacyDeviceInfo(LegacyInfo legacyInfo) {
        this.mLegacyDeviceInfo = legacyInfo;
    }

    public LegacyInfo getLegacyDeviceInfo() {
        return mLegacyDeviceInfo;
    }

    public DeviceIdInfoData() {
    }
}
