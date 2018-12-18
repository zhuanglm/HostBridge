package com.epocal.common.realmentities;

import com.epocal.common.types.HostEdition;
import com.epocal.common.types.HostStatus;
import com.epocal.common.types.LanguageType;
import com.epocal.common.types.LogLevel;
import com.epocal.common.types.RangeIgnoreInfo;
import com.epocal.common.types.TimezoneType;
import com.epocal.util.EnumSetUtil;

import java.util.EnumSet;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 6/15/2017.
 */

public class DeviceSetting extends RealmObject{
    private Boolean wifiRoaming;
    private Boolean fips;
    private Boolean certificateValidation;
    private String deviceSerialNumber;
    private String deviceName;

    public Boolean getWifiRoaming() {
        return wifiRoaming;
    }

    public void setWifiRoaming(Boolean wifiRoaming) {
        this.wifiRoaming = wifiRoaming;
    }

    public Boolean getFips() {
        return fips;
    }

    public void setFips(Boolean fips) {
        this.fips = fips;
    }

    public Boolean getCertificateValidation() {
        return certificateValidation;
    }

    public void setCertificateValidation(Boolean certificateValidation) {
        this.certificateValidation = certificateValidation;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Ignore
    private HostStatus mHostStatus;
    private Integer hostAppStatus;

    public HostStatus getHostStatus() {
        return HostStatus.fromInt(getHostAppStatus());
    }
    public void setHostStatus(HostStatus hostStatus) {
        setHostAppStatus(hostStatus.value);
    }
    private Integer getHostAppStatus() {
        return hostAppStatus;
    }
    private void setHostAppStatus(Integer hostAppStatus) {
        this.hostAppStatus = hostAppStatus;
    }

    @Ignore
    private TimezoneType mTimezoneType;
    private Integer timeZone;

    public TimezoneType getTimezoneType() {
        return TimezoneType.fromInt(getTimeZone());
    }

    public void setTimezoneType(TimezoneType timezoneType) {
        setTimeZone(timezoneType.value);
    }

    private Integer getTimeZone() {
        return timeZone;
    }

    private void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }

    @Ignore
    private HostEdition mHostEdition;
    private Integer edition;
    public HostEdition getHostEdition() {
        return HostEdition.fromInt(getEdition());
    }
    public void setHostEdition(HostEdition hostEdition) {
        setEdition(hostEdition.value);
    }
    private Integer getEdition() {
        return edition;
    }
    private void setEdition(Integer edition) {
        this.edition = edition;
    }

    @Ignore
    private LanguageType mLanguageType;
    private Integer language;

    public LanguageType getLanguageType() {
        return LanguageType.fromInt(getLanguage());
    }
    public void setLanguageType(LanguageType languageType) {
        setLanguage(languageType.value);
    }
    private Integer getLanguage() {
        return language;
    }
    private void setLanguage(Integer language) {
        this.language = language;
    }

    @Ignore
    private EnumSet<LogLevel> logVerbosity= EnumSet.of(LogLevel.Information,LogLevel.Error);
    private long logLevel;

    public EnumSet<LogLevel> getLogVerbosity() {
        return EnumSetUtil.toSet(getLogLevel(),LogLevel.class);
    }

    public void setLogVerbosity(EnumSet<LogLevel> logVerbosity) {
        setLogLevel(EnumSetUtil.encode(logVerbosity));
    }

    private long getLogLevel() {
        return logLevel;
    }

    private void setLogLevel(long logLevel) {
        this.logLevel = logLevel;
    }

    public void resetFactoryDefault() {
        setLanguageType(LanguageType.English); // 1
        setLogVerbosity(EnumSet.of(LogLevel.Information,LogLevel.Error)); // 6
        setWifiRoaming(true);
        setFips(false);
        setCertificateValidation(false);
        setTimezoneType(TimezoneType.EASTERNTIME_USACAN);
        setHostStatus(HostStatus.Normal);
        setDeviceName("Epoc Host");
        setHostEdition(HostEdition.Professional);
        setDeviceSerialNumber("");
    }


}
