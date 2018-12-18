package com.epocal.common.realmentities;

import com.epocal.common.epocobjects.IVersioned;
import com.epocal.common.types.HostStatus;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bmate on 5/29/2017.
 */

public class Host extends RealmObject implements IVersioned {
    @PrimaryKey
    private long id =-1;
    private String serialNumber;
    private String macAddress;
    private String bluetoothAddress;
    private String department;
    private String alias;
    private String softwareVersion;
    private String testVersion;
    private Boolean isActive;
    private Date lastUpdated;
    private Date created;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getTestVersion() {
        return testVersion;
    }

    public void setTestVersion(String testVersion) {
        this.testVersion = testVersion;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Ignore
    private HostStatus status;
    private String hostStatus;

    public HostStatus getStatus() {
        return HostStatus.valueOf(getHostStatus());
    }

    public void setStatus(HostStatus status) {
        setHostStatus(status.toString());
    }

    private String getHostStatus() {
        return hostStatus;
    }

    private void setHostStatus(String hostStatus) {
        this.hostStatus = hostStatus;
    }

    @Override
    public boolean newVersionNeeded(String propertyName) {
        Set<String> values = new HashSet<String>(Arrays.asList(new String[]{"serialnumber", "alias", "softwareversion", "testversion", "department"}));
        return values.contains(propertyName.toLowerCase());
    }
    public void Merge (Host original)
    {

    }

}
