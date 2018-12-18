package com.epocal.common.realmentities;

import com.epocal.common.types.PressureType;
import com.epocal.common.types.am.Temperatures;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rzhuang on Aug 7 2018.
 */

public class EqcInfo extends RealmObject {

    public int MAX_EQC_VALUES = 7;

    @PrimaryKey
    private long id =-1;

    private Date mCreated;
    private Reader mReader;
    private Host mHost;
    private User mUser;
    private String mUserOther;
    private float mBatteryLevel;
    @Ignore
    public Temperatures mTemperatureType;
    @Ignore
    private PressureType mPressureType;
    private double mAmbientTemperature;
    private double mAmbientPressure;
    private boolean mHasPassed;
    private boolean mSelfTestResult;
    private byte mReturnCode;
    @Ignore
    public ArrayList<EqcValue> mValues;
    private Date mUploaded;

    public EqcInfo () {
        mValues = new ArrayList<>();
    }

    public long getId() { return id; }
    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedDate() { return mCreated; }
    public void setCreatedDate(Date date) { mCreated=date; }

    public Reader getReader() { return mReader; }
    public void setReader(Reader reader) { mReader=reader; }

    public double getAmbientPressure() { return mAmbientPressure; }
    public void setAmbientPressure(double ambientPressure) { mAmbientPressure=ambientPressure; }

    public double getAmbientTemperature() { return mAmbientTemperature; }
    public void setAmbientTemperature(double ambientTemperature) { mAmbientTemperature=ambientTemperature; }

    public PressureType getPressureType() { return mPressureType; }
    public void setPressureType(PressureType pressureType) { mPressureType=pressureType; }

    public float getBatteryLevel() { return mBatteryLevel; }
    public void setBatteryLevel(float batteryLevel) { mBatteryLevel=batteryLevel; }

    public Date getCreated() { return mCreated; }
    public void setCreated(Date created) { mCreated=created; }

    public boolean isHasPassed() { return mHasPassed; }
    public void setHasPassed(boolean hasPassed) { mHasPassed=hasPassed; }

    public Host getHost() { return mHost; }
    public void setHost(Host host) { mHost=host; }

    public byte getReturnCode() { return mReturnCode; }
    public void setReturnCode(byte reurnCode) { mReturnCode=reurnCode; }

    public User getUser() { return mUser; }
    public void setUser(User user) { mUser=user; }

    public String getUserOther() { return mUserOther; }
    public void setUserOther(String user) { mUserOther=user; }

    public boolean isSelfTestResult() { return mSelfTestResult; }
    public void setSelfTestResult(boolean selfTestResult) { mSelfTestResult=selfTestResult; }

}
