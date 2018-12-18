package com.epocal.common.realmentities;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EVAD extends RealmObject {
    @PrimaryKey
    private long id =-1;
    private String type;
    private String version;
    private Date created;
    private int qaSamples;
    private double sensorConfig;
    private Date expiry;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getQaSamples() {
        return qaSamples;
    }

    public void setQaSamples(int qaSamples) {
        this.qaSamples = qaSamples;
    }

    public double getSensorConfig() {
        return sensorConfig;
    }

    public void setSensorConfig(double sensorConfig) {
        this.sensorConfig = sensorConfig;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }
}
