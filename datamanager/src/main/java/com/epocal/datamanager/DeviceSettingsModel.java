package com.epocal.datamanager;

import com.epocal.common.eventmessages.DeviceSettingsChangeNotification;
import com.epocal.common.realmentities.DeviceSetting;
import com.epocal.common.types.LanguageType;
import com.epocal.common.types.LogLevel;
import com.epocal.common.types.LogVerbosity;

import org.greenrobot.eventbus.EventBus;

import java.util.EnumSet;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

/**
 * Created by bmate on 6/15/2017.
 */

public class DeviceSettingsModel {

    Realm mRealm = null;

    public void openRealmInstance() {
        if (mRealm == null)
            mRealm = Realm.getDefaultInstance();
    }

    public void closeRealmInstance() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
    }

    /**
     * full update of deviceSetting object to the values of an input unmanaged deviceSettings object- most likely from a synchronization parser.
     * if any of updates failed, the entire update is canceled
     * @return  void
     **/
    public void updateDeviceSetting(final DeviceSetting input) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                DeviceSetting ds = realm.where(DeviceSetting.class)
                        .findFirst();
                // TODO: once the devicecontroller is implemented, get the serialnumber
                ds.setHostEdition(input.getHostEdition());
                ds.setDeviceName(input.getDeviceName());
                ds.setHostStatus(input.getHostStatus());
                ds.setCertificateValidation(input.getCertificateValidation());
                ds.setTimezoneType(input.getTimezoneType());
                ds.setFips(input.getFips());
                ds.setLanguageType(input.getLanguageType());
                ds.setWifiRoaming(input.getWifiRoaming());
                ds.setLogVerbosity(input.getLogVerbosity());
            }
        });

        if (mRealm == null)
            realm.close();

        postDeviceSettingsChange();
    }

    private void postDeviceSettingsChange() {
        EventBus.getDefault().post(new DeviceSettingsChangeNotification());
    }

    /**
     * clone DeviceSetting object
     *
     * @return DeviceSetting (unmanaged)
     **/
    public DeviceSetting Clone(DeviceSetting input) {
        DeviceSetting ds = new DeviceSetting();
        ds.setDeviceSerialNumber(input.getDeviceSerialNumber());
        ds.setHostEdition(input.getHostEdition());
        ds.setDeviceName(input.getDeviceName());
        ds.setHostStatus(input.getHostStatus());
        ds.setCertificateValidation(input.getCertificateValidation());
        ds.setTimezoneType(input.getTimezoneType());
        ds.setFips(input.getFips());
        ds.setLanguageType(input.getLanguageType());
        ds.setWifiRoaming(input.getWifiRoaming());
        ds.setLogVerbosity(input.getLogVerbosity());

        return ds;
    }

    /**
     * get DeviceSetting object (managed or unmanaged)
     *
     * @return DeviceSetting
     **/
    public DeviceSetting getDeviceSetting(boolean managed) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        DeviceSetting deviceSetting = getDeviceSetting(realm, managed);

        if (mRealm == null)
            realm.close();

        return deviceSetting;
    }

    private DeviceSetting getDeviceSetting(Realm realm, boolean managed) {
        DeviceSetting ds = realm.where(DeviceSetting.class).findFirst();
        if (managed) {
            return ds;
        } else {
            return Clone(ds);
        }
    }
    /**
     * reset DeviceSettings to factory default values
     *
     * @return void
     **/
    public void resetToFactoryDefault() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                DeviceSetting ds = realm.where(DeviceSetting.class)
                        .findFirst();
                ds.resetFactoryDefault();
            }
        });

        if (mRealm == null)
            realm.close();

    }

    public LogVerbosity getLogVerbosity() {
        LogVerbosity retval = LogVerbosity.Low;
        EnumSet<LogLevel> logLevels ;

        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        logLevels =  realm.where(DeviceSetting.class).findFirst().getLogVerbosity();
        if (logLevels.contains(LogLevel.Information)){
            retval = LogVerbosity.High;
        } else if (logLevels.contains(LogLevel.Information) && !logLevels.contains(LogLevel.Debug)){
            retval = LogVerbosity.Medium;
        }

        if (mRealm == null)
            realm.close();

        return retval;
    }

    public EnumSet<LogLevel> getLogLevels() {
        EnumSet<LogLevel> retval ;

        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        retval =  realm.where(DeviceSetting.class).findFirst().getLogVerbosity();

        if (mRealm == null)
            realm.close();

        return retval;
    }

    public void setLogLevel (LogVerbosity verbosity) {
        final EnumSet<LogLevel> logLevels= EnumSet.of(LogLevel.Error); // at minimum

        switch (verbosity){

            case None:
            case Low:
                // errors only
                break;
            case Medium:
                logLevels.add(LogLevel.Information);
                break;
            case High:
                logLevels.add(LogLevel.Information);
                logLevels.add(LogLevel.Debug);
                break;
        }

        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                DeviceSetting ds = realm.where(DeviceSetting.class)
                        .findFirst();
                ds.setLogVerbosity(logLevels);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public void setDeviceName(String deviceName, boolean managed) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        DeviceSetting deviceSetting = getDeviceSetting(realm, managed);

        realm.beginTransaction();
        deviceSetting.setDeviceName(deviceName);
        realm.commitTransaction();

        if (mRealm == null)
            realm.close();
    }

    public String getDeviceName() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        String deviceName = getDeviceSetting(realm, true).getDeviceName();

        if (mRealm == null)
            realm.close();

        return deviceName;
    }

    public void setFips(boolean isEnabled, boolean managed) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        DeviceSetting deviceSetting = getDeviceSetting(realm, managed);

        realm.beginTransaction();
        deviceSetting.setFips(isEnabled);
        realm.commitTransaction();

        if (mRealm == null)
            realm.close();
    }

    public boolean getFips() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean fipsEnabled = getDeviceSetting(realm, true).getFips();

        if (mRealm == null)
            realm.close();

        return fipsEnabled;
    }

    public void setLanguageType(LanguageType languageType, boolean managed) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        DeviceSetting deviceSetting = getDeviceSetting(realm, managed);

        realm.beginTransaction();
        deviceSetting.setLanguageType(languageType);
        realm.commitTransaction();

        if (mRealm == null)
            realm.close();
    }

    public final <E extends RealmModel> void addChangeListener(RealmChangeListener<E> realmChangeListener) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        getDeviceSetting(realm, true).addChangeListener(realmChangeListener);

        if (mRealm == null)
            realm.close();
    }

    public LanguageType getLanguageType() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        LanguageType languageType = getDeviceSetting(realm, true).getLanguageType();

        if (mRealm == null)
            realm.close();

        return languageType;
    }
}
