package com.epocal.common.realmentities;

import com.epocal.common.epocobjects.IVersioned;
import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.common.qaverfication.QATestStatus;
import com.epocal.common.types.ReaderType;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bmate on 5/29/2017.
 */

public class Reader extends RealmObject implements IVersioned {

    @PrimaryKey
    private long id =-1;
    private Boolean isActive;
    private String serialNumber = "";
    private String bluetoothAddress = "";
    private String alias = "";
    private String softwareVersion = "";
    private Integer readerQCContentVersion = 0;
    private String hardwareVersion = "";
    private String mechanicalVersion = "";
    private String pin = "1111";
    private boolean isDedicated;
    private Boolean syncedStatus;
    private Date created;
    private Date lastConnected;
    private Date lastUpdated;
    private Date lastEQCDateTime;
    private Date lastTQADateTime;
    private Date qcExpiryDateTime;
    private Date cvExpiryDateTime;
    private Date qcInfoUpdateDateTime;
    private Date cvInfoUpdateDateTime;
    private RealmList<ReaderQCFluidInfo> mReaderQCFluidInfos;
    private String mDeviceName;

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getMechanicalVersion() {
        return mechanicalVersion;
    }

    public void setMechanicalVersion(String mechanicalVersion) {
        this.mechanicalVersion = mechanicalVersion;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean getDedicated() {
        return isDedicated;
    }

    public void setDedicated(boolean dedicated) {
        isDedicated = dedicated;
    }

    public Date getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(Date lastConnected) {
        this.lastConnected = lastConnected;
    }

    public Integer getReaderQCContentVersion() {
        return readerQCContentVersion;
    }

    public void setReaderQCContentVersion(Integer readerQCContentVersion) {
        this.readerQCContentVersion = readerQCContentVersion;
    }

    public Boolean getSyncedStatus() {
        return syncedStatus;
    }

    public void setSyncedStatus(Boolean syncedStatus) {
        this.syncedStatus = syncedStatus;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getLastEQCDateTime() {
        return lastEQCDateTime;
    }

    public void setLastEQCDateTime(Date lastEQCDateTime) {
        this.lastEQCDateTime = lastEQCDateTime;
    }

    public Date getLastTQADateTime() {
        return lastTQADateTime;
    }

    public void setLastTQADateTime(Date lastTQADateTime) {
        this.lastTQADateTime = lastTQADateTime;
    }

    public Date getQcExpiryDateTime() {
        return qcExpiryDateTime;
    }

    public void setQcExpiryDateTime(Date qcExpiryDateTime) {
        this.qcExpiryDateTime = qcExpiryDateTime;
    }

    public Date getCvExpiryDateTime() {
        return cvExpiryDateTime;
    }

    public void setCvExpiryDateTime(Date cvExpiryDateTime) {
        this.cvExpiryDateTime = cvExpiryDateTime;
    }

    public Date getQcInfoUpdateDateTime() {
        return qcInfoUpdateDateTime;
    }

    public void setQcInfoUpdateDateTime(Date qcInfoUpdateDateTime) {
        this.qcInfoUpdateDateTime = qcInfoUpdateDateTime;
    }

    public Date getCvInfoUpdateDateTime() {
        return cvInfoUpdateDateTime;
    }

    public void setCvInfoUpdateDateTime(Date cvInfoUpdateDateTime) {
        this.cvInfoUpdateDateTime = cvInfoUpdateDateTime;
    }

    public RealmList<ReaderQCFluidInfo> getReaderQCFluidInfos() {
        return mReaderQCFluidInfos;
    }

    public void setReaderQCFluidInfos(RealmList<ReaderQCFluidInfo> readerQCFluidInfos) {
        mReaderQCFluidInfos = readerQCFluidInfos;
    }



    @Ignore
    private QATestStatus lastEQCPassFail;
    private Integer eqcPassFail=-1;

    public QATestStatus getLastEQCPassFail() {
        return QATestStatus.fromInt(getEqcPassFail());
    }

    public void setLastEQCPassFail(QATestStatus lastEQCPassFail) {
        setEqcPassFail(lastEQCPassFail.value);
    }

    private Integer getEqcPassFail() {
        return eqcPassFail;
    }

    private void setEqcPassFail(Integer eqcPassFail) {
        this.eqcPassFail = eqcPassFail;
    }

    @Ignore
    private QATestStatus lastTQAPassFail;
    private Integer tqaPassFail=-1;

    public QATestStatus getLastTQAPassFail() {
        return QATestStatus.fromInt(getTqaPassFail());
    }

    public void setLastTQAPassFail(QATestStatus lastTQAPassFail) {
        setTqaPassFail(lastTQAPassFail.value);
    }

    private Integer getTqaPassFail() {
        return tqaPassFail;
    }

    private void setTqaPassFail(Integer tqaPassFail) {
        this.tqaPassFail = tqaPassFail;
    }

    // methods
    public String getReaderVersion() {
        return String.format("%s-%s", getSoftwareVersion(), getHardwareVersion());
    }

    /**
     *  When changing a reader property value, it will check if the change requires a new version
     *
     * @return void
     **/
    @Override
    public boolean newVersionNeeded(String propertyName) {

        Set<String> values = new HashSet<String>(Arrays.asList(new String[]{"serialnumber", "alias", "softwareversion", "mechanicalversion", "hardwareversion"}));
        return values.contains(propertyName.toLowerCase());
    }

    public ReaderDevice toReaderDevice(){
        ReaderDevice readerDevice = new ReaderDevice();
        readerDevice.setDeviceAddress(this.bluetoothAddress);
        readerDevice.setDeviceName(this.serialNumber + this.alias);
        readerDevice.setDedicated(this.isDedicated);
        return readerDevice;
    }
}
