package com.epocal.common.realmentities;

import com.epocal.common.types.NotifyActionType;
import com.epocal.common.types.PatientIDEntryMethod;
import com.epocal.common.types.PatientIDLookupCode;
import com.epocal.common.types.PressureType;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.Temperatures;


import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bmate on 5/29/2017.
 */

public class TestDetail extends RealmObject {

    @PrimaryKey
    private long id;
    private String customSampleType = "";
    private Boolean hemodilutionApplied;
    private Double ambientTemperature;
    private Double ambientPressure;
    private Double patientTemperature;
    private String notifyName; //i.e. who did perform the notification
    private String notifyTime;
    private Date notifyDate;
    private boolean isReadBack;
    private String comment;
    private Double duration;
    private String rangeName;
    private QASampleInfo mQaSampleInfo = null;
    private RealmList<CustomTestVariable> customTestVariables;
    private String orderingPhysician;
    private Date orderDate;
    private String orderTime="";
    private String collectedBy="";
    private Date collectionDate;
    private String collectionTime="";
    private String patientLocation="";
    private String patientName="";
    private Date patientDOB;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomSampleType() {
        return customSampleType;
    }

    public void setCustomSampleType(String customSampleType) {
        this.customSampleType = customSampleType;
    }

    public Boolean getHemodilutionApplied() {
        return hemodilutionApplied;
    }

    public void setHemodilutionApplied(Boolean hemodilutionApplied) {
        this.hemodilutionApplied = hemodilutionApplied;
    }

    public Double getAmbientTemperature() {
        return ambientTemperature;
    }

    public void setAmbientTemperature(Double ambientTemperature) {
        this.ambientTemperature = ambientTemperature;
    }

    public Double getAmbientPressure() {
        return ambientPressure;
    }

    public void setAmbientPressure(Double ambientPressure) {
        this.ambientPressure = ambientPressure;
    }

    public Double getPatientTemperature() {
        return patientTemperature;
    }

    public void setPatientTemperature(Double patientTemperature) {
        this.patientTemperature = patientTemperature;
    }


    public String getNotifyName() {
        return notifyName;
    }

    public void setNotifyName(String notifyName) {
        this.notifyName = notifyName;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Date getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }

    public boolean getReadBack() {
        return isReadBack;
    }

    public void setReadBack(boolean readBack) {
        isReadBack = readBack;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public QASampleInfo getQaSampleInfo() {
        return mQaSampleInfo;
    }

    public void setQaSampleInfo(QASampleInfo qaSampleInfo) {
        this.mQaSampleInfo = qaSampleInfo;
    }

    public RealmList<CustomTestVariable> getCustomTestVariables() {
        return customTestVariables;
    }

    public void setCustomTestVariables(RealmList<CustomTestVariable> customTestVariables) {
        this.customTestVariables = customTestVariables;
    }

    public String getOrderingPhysician() {
        return orderingPhysician;
    }

    public void setOrderingPhysician(String orderingPhysician) {
        this.orderingPhysician = orderingPhysician;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(String collectedBy) {
        this.collectedBy = collectedBy;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getPatientLocation() {
        return patientLocation;
    }

    public void setPatientLocation(String patientLocation) {
        this.patientLocation = patientLocation;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Date getPatientDOB() {
        return patientDOB;
    }

    public void setPatientDOB(Date patientDOB) {
        this.patientDOB = patientDOB;
    }

    @Ignore
    private PatientIDLookupCode patientIDLookupCode;
    private int patientIdLookupCode;

    public PatientIDLookupCode getPatientIDLookupCode() {
      return PatientIDLookupCode.fromInt(getPatientIdLookupCode());
    }
    public void setPatientIDLookupCode(PatientIDLookupCode patientIDLookupCode) {
        setPatientIdLookupCode(patientIDLookupCode.value);
    }
    private int getPatientIdLookupCode() {
        return patientIdLookupCode;
    }
    private void setPatientIdLookupCode(int patientIdLookupCode) {
        this.patientIdLookupCode = patientIdLookupCode;
    }

    @Ignore
    private PatientIDEntryMethod patientIDEntryMethod;
    private int patientIdEntryMethod;

    public PatientIDEntryMethod getPatientIDEntryMethod() {
       return PatientIDEntryMethod.fromInt(getPatientIdEntryMethod());
    }
    public void setPatientIDEntryMethod(PatientIDEntryMethod patientIDEntryMethod) {
        setPatientIdEntryMethod(patientIDEntryMethod.value);
    }
    private int getPatientIdEntryMethod() {
        return patientIdEntryMethod;
    }
    private void setPatientIdEntryMethod(int patientIdEntryMethod) {
        this.patientIdEntryMethod = patientIdEntryMethod;
    }

    @Ignore
    private BloodSampleType sampleType;
    private int bloodSampleType = BloodSampleType.Unknown.value;

    public BloodSampleType getSampleType() {
        return BloodSampleType.fromInt(getBloodSampleType());
    }
    public void setSampleType(BloodSampleType sampleType) {
        setBloodSampleType(sampleType.value);
    }
    private int getBloodSampleType() {
        return bloodSampleType;
    }
    private void setBloodSampleType(int bloodSampleType) {
        this.bloodSampleType = bloodSampleType;
    }
    @Ignore
    private Temperatures ambientTemperatureType;
    private int ambientTempType = Temperatures.ENUM_UNINITIALIZED.value;

    public Temperatures getAmbientTemperatureType() {
        return Temperatures.fromInt(getAmbientTempType());
    }
    public void setAmbientTemperatureType(Temperatures ambientTemperatureType) {
       setAmbientTempType(ambientTemperatureType.value);
    }
    private int getAmbientTempType() {
        return ambientTempType;
    }
    private void setAmbientTempType(int ambientTempType) {
        this.ambientTempType = ambientTempType;
    }

    @Ignore
    private Temperatures patientTemperatureType;
    private int patientTempType = Temperatures.ENUM_UNINITIALIZED.value;

    public Temperatures getPatientTemperatureType() {
        return Temperatures.fromInt(getAmbientTempType());
    }
    public void setPatientTemperatureType(Temperatures patientTemperatureType) {
        setPatientTempType(patientTemperatureType.value);
    }
    private int getPatientTempType() {
        return patientTempType;
    }
    private void setPatientTempType(int ambientTempType) {
        this.patientTempType = ambientTempType;
    }

    @Ignore
    private PressureType pressureType;
    private int ambienPressureType ;

    public PressureType getPressureType() {
        return PressureType.fromInt(getAmbienPressureType());
    }

    public void setPressureType(PressureType pressureType) {
        setAmbienPressureType(pressureType.value);
    }

    private int getAmbienPressureType() {
        return ambienPressureType;
    }

    private void setAmbienPressureType(int ambienPressureType) {
        this.ambienPressureType = ambienPressureType;
    }

    @Ignore
    private NotifyActionType notifyActionType;
    private int notifyActionTypeValue = NotifyActionType.None.value;

    public NotifyActionType getNotifyActionType() {
        return NotifyActionType.fromInt(getNotifyActionTypeValue());
    }

    public void setNotifyActionType(NotifyActionType notifyActionType) {
        setNotifyActionTypeValue(notifyActionType.value);
    }

    private int getNotifyActionTypeValue() {
        return notifyActionTypeValue;
    }

    private void setNotifyActionTypeValue(int notifyActionTypeValue) {
        this.notifyActionTypeValue = notifyActionTypeValue;
    }
}
