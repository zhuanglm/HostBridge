package com.epocal.common.realmentities;

import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.AllensTest;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.DeliverySystem;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.am.RespiratoryMode;
import com.epocal.util.EnumSetUtil;

import java.util.Date;
import java.util.EnumSet;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 02/11/2017.
 */

public class TestAttributeValues extends RealmObject {
    private String patientIdValue = "";

    public String getPatientIdValue() {
        return patientIdValue;
    }

    public void setPatientIdValue(String patientIdValue) {
        this.patientIdValue = patientIdValue;
        presetAttributes.add(EpocTestFieldType.PATIENTID);
        setPresetAttributes(presetAttributes);
    }

    private String patientId2Value = "";

    public String getPatientId2Value() {
        return patientId2Value;
    }

    public void setPatientId2Value(String patientId2Value) {
        this.patientId2Value = patientId2Value;
        presetAttributes.add(EpocTestFieldType.PATIENTID2);
        setPresetAttributes(presetAttributes);

    }

    @Ignore
    private EnumSet<EpocTestFieldType> presetAttributes = EnumSet.noneOf(EpocTestFieldType.class);
    private long presetAttributesValue = EnumSetUtil.encode(presetAttributes);

    public EnumSet<EpocTestFieldType> getPresetAttributes() {
        return EnumSetUtil.toSet(getPresetAttributesValue(),EpocTestFieldType.class);
    }

    public void setPresetAttributes(EnumSet<EpocTestFieldType> presetAttributes) {
        setPresetAttributesValue(EnumSetUtil.encode(presetAttributes));
    }

    private long getPresetAttributesValue() {
        return presetAttributesValue;
    }

    private void setPresetAttributesValue(long presetAttributesValue) {
        this.presetAttributesValue = presetAttributesValue;
    }


    @Ignore
    private BloodSampleType sampleType;
    private int sampleTypeValue = BloodSampleType.Unspecified.value;

    public BloodSampleType getSampleType() {
        return BloodSampleType.fromInt(getSampleTypeValue());
    }

    public void setSampleType(BloodSampleType sampleType) {
        setSampleTypeValue(sampleType.value);
        presetAttributes.add(EpocTestFieldType.SAMPLETYPE);
        setPresetAttributes(presetAttributes);

    }

    private int getSampleTypeValue() {
        return sampleTypeValue;
    }

    private void setSampleTypeValue(int sampleTypeValue) {
        this.sampleTypeValue = sampleTypeValue;
    }
    private Double patientTemperature = null;

    public Double getPatientTemperature() {
        return patientTemperature;
    }

    public void setPatientTemperature(Double patientTemperature) {
        this.patientTemperature = patientTemperature;
        presetAttributes.add(EpocTestFieldType.PATIENTTEMPERATURE);
        setPresetAttributes(presetAttributes);
    }
    private Boolean hemodilution = null;

    public Boolean getHemodilution() {
        return hemodilution;
    }

    public void setHemodilution(Boolean hemodilution) {
        this.hemodilution = hemodilution;
        presetAttributes.add(EpocTestFieldType.HEMODILUTION);
        setPresetAttributes(presetAttributes);
    }
    private String comments ="";

    public String getComments() {
        return comments;

    }

    public void setComments(String comments) {
        this.comments = comments;
        presetAttributes.add(EpocTestFieldType.COMMENTS);
        setPresetAttributes(presetAttributes);

    }
    @Ignore
    private DrawSites drawSites = DrawSites.ENUM_UNINITIALIZED;
    private int drawSitesValue = DrawSites.ENUM_UNINITIALIZED.value;

    public DrawSites getDrawSites() {
        return DrawSites.fromInt(getDrawSitesValue());
    }

    public void setDrawSites(DrawSites drawSites) {
        setDrawSitesValue(drawSites.value);
        presetAttributes.add(EpocTestFieldType.DRAWSITE);
        setPresetAttributes(presetAttributes);

    }

    private int getDrawSitesValue() {
        return drawSitesValue;
    }

    private  void setDrawSitesValue(int drawSitesValue) {
        this.drawSitesValue = drawSitesValue;
    }

    @Ignore
    private AllensTest allensTest = AllensTest.ENUM_UNINITIALIZED;
    private int allensTestValue = AllensTest.ENUM_UNINITIALIZED.value;

    public AllensTest getAllensTest() {
        return AllensTest.fromInt(getAllensTestValue());
    }

    public void setAllensTest(AllensTest allensTest) {
        setAllensTestValue(allensTest.value);
        presetAttributes.add(EpocTestFieldType.ALLENSTEST);
        setPresetAttributes(presetAttributes);

    }

    private int getAllensTestValue() {
        return allensTestValue;
    }

    private void setAllensTestValue(int allensTestValue) {
        this.allensTestValue = allensTestValue;
    }
    @Ignore
    private DeliverySystem deliverySystem = DeliverySystem.ENUM_UNINITIALIZED;
    private int deliverySystemValue = DeliverySystem.ENUM_UNINITIALIZED.value;

    public DeliverySystem getDeliverySystem() {
        return DeliverySystem.fromInt(getDeliverySystemValue());
    }

    public void setDeliverySystem(DeliverySystem deliverySystem) {

        setDeliverySystemValue(deliverySystem.value);
        presetAttributes.add(EpocTestFieldType.DELIVERYSYSTEM);
        setPresetAttributes(presetAttributes);

    }

    private int getDeliverySystemValue() {
        return deliverySystemValue;
    }

    private void setDeliverySystemValue(int deliverySystemValue) {
        this.deliverySystemValue = deliverySystemValue;
    }
    @Ignore
    private RespiratoryMode respiratoryMode = RespiratoryMode.ENUM_UNINITIALIZED;
    private int respiratoryModeValue = RespiratoryMode.ENUM_UNINITIALIZED.value;

    public RespiratoryMode getRespiratoryMode() {
        return RespiratoryMode.fromInt(getRespiratoryModeValue());
    }

    public void setRespiratoryMode(RespiratoryMode respiratoryMode) {
        setRespiratoryModeValue(respiratoryMode.value);
        presetAttributes.add(EpocTestFieldType.MODE);
        setPresetAttributes(presetAttributes);

    }

    private int getRespiratoryModeValue() {
        return respiratoryModeValue;
    }

    private void setRespiratoryModeValue(int respiratoryModeValue) {
        this.respiratoryModeValue = respiratoryModeValue;
    }

    @Ignore
    private TestType testType = TestType.Unknown;
    private int testTypeValue = TestType.Unknown.value;

    public TestType getTestType() { return TestType.fromInt(getTestTypeValue()); }
    public void setQATestType(TestType testType) {
        setTestTypeValue(testType.value);
        presetAttributes.add(EpocTestFieldType.TESTTYPE);
        setPresetAttributes(presetAttributes);
    }

    private int getTestTypeValue() {
        return testTypeValue;
    }

    private  void setTestTypeValue(int testTypeValue) {
        this.testTypeValue = testTypeValue;
    }

    private String FIO2 = "";
    private String VT = "";
    private String RR = "";
    private String TR = "";
    private String PEEP = "";
    private String PS = "";
    private String IT = "";
    private String ET = "";
    private String PIP = "";
    private String MAP = "";
    private String orderingPhysician = "";
    private Date orderDate = null;
    private String orderTime = "";
    private String collectedBy = "";
    private Date collectionDate = null;
    private String collectionTime = "";
    private String patientLocation = "";
    private String flow = "";
    private Integer patientAge = null;
    private String hertz = "";
    private String deltaP = "";
    private String NOPPM = "";
    private String RQ = "";
    private String lotNumber = "";
    private String fluidType = "";

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
        presetAttributes.add(EpocTestFieldType.LOTNUMBER);
        setPresetAttributes(presetAttributes);

    }

    public String getFluidType() {
        return fluidType;
    }

    public void setFluidType(String fluidType) {
        this.fluidType = fluidType;
        presetAttributes.add(EpocTestFieldType.FLUIDTYPE);
        setPresetAttributes(presetAttributes);

    }

    public String getFIO2() {
        return FIO2;
    }

    public void setFIO2(String FIO2) {
        this.FIO2 = FIO2;
        presetAttributes.add(EpocTestFieldType.FIO2);
        setPresetAttributes(presetAttributes);

    }

    public String getVT() {
        return VT;
    }

    public void setVT(String VT) {
        this.VT = VT;
        presetAttributes.add(EpocTestFieldType.VT);
        setPresetAttributes(presetAttributes);

    }

    public String getRR() {
        return RR;
    }

    public void setRR(String RR) {
        this.RR = RR;
        presetAttributes.add(EpocTestFieldType.RR);
        setPresetAttributes(presetAttributes);

    }

    public String getTR() {
        return TR;
    }

    public void setTR(String TR) {
        this.TR = TR;
        presetAttributes.add(EpocTestFieldType.TR);
        setPresetAttributes(presetAttributes);

    }

    public String getPEEP() {
        return PEEP;
    }

    public void setPEEP(String PEEP) {
        this.PEEP = PEEP;
        presetAttributes.add(EpocTestFieldType.PEEP);
        setPresetAttributes(presetAttributes);
    }

    public String getPS() {
        return PS;
    }

    public void setPS(String PS) {
        this.PS = PS;
        presetAttributes.add(EpocTestFieldType.PS);
        setPresetAttributes(presetAttributes);

    }

    public String getIT() {
        return IT;
    }

    public void setIT(String IT) {
        this.IT = IT;
        presetAttributes.add(EpocTestFieldType.IT);
        setPresetAttributes(presetAttributes);

    }

    public String getET() {
        return ET;
    }

    public void setET(String ET) {
        this.ET = ET;
        presetAttributes.add(EpocTestFieldType.ET);
        setPresetAttributes(presetAttributes);

    }

    public String getPIP() {
        return PIP;
    }

    public void setPIP(String PIP) {
        this.PIP = PIP;
        presetAttributes.add(EpocTestFieldType.PIP);
        setPresetAttributes(presetAttributes);

    }

    public String getMAP() {
        return MAP;
    }

    public void setMAP(String MAP) {
        this.MAP = MAP;
        presetAttributes.add(EpocTestFieldType.MAP);
        setPresetAttributes(presetAttributes);

    }

    public String getOrderingPhysician() {
        return orderingPhysician;
    }

    public void setOrderingPhysician(String orderingPhysician) {
        this.orderingPhysician = orderingPhysician;
        presetAttributes.add(EpocTestFieldType.ORDERINGPHYSICIAN);
        setPresetAttributes(presetAttributes);
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
        presetAttributes.add(EpocTestFieldType.ORDERDATE);
        setPresetAttributes(presetAttributes);

    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
        presetAttributes.add(EpocTestFieldType.ORDERTIME);
        setPresetAttributes(presetAttributes);
    }

    public String getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(String collectedBy) {
        this.collectedBy = collectedBy;
        presetAttributes.add(EpocTestFieldType.COLLECTEDBY);
        setPresetAttributes(presetAttributes);

    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
        presetAttributes.add(EpocTestFieldType.COLLECTIONDATE);
        setPresetAttributes(presetAttributes);

    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
        presetAttributes.add(EpocTestFieldType.COLLECTIONTIME);
        setPresetAttributes(presetAttributes);

    }

    public String getPatientLocation() {
        return patientLocation;
    }

    public void setPatientLocation(String patientLocation) {
        this.patientLocation = patientLocation;
        presetAttributes.add(EpocTestFieldType.PATIENTLOCATION);
        setPresetAttributes(presetAttributes);

    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
        presetAttributes.add(EpocTestFieldType.FLOW);
        setPresetAttributes(presetAttributes);
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
        presetAttributes.add(EpocTestFieldType.PATIENTAGE);
        setPresetAttributes(presetAttributes);
    }

    public String getHertz() {
        return hertz;
    }

    public void setHertz(String hertz) {
        this.hertz = hertz;
        presetAttributes.add(EpocTestFieldType.HERTZ);
        setPresetAttributes(presetAttributes);

    }

    public String getDeltaP() {
        return deltaP;
    }

    public void setDeltaP(String deltaP) {
        this.deltaP = deltaP;
        presetAttributes.add(EpocTestFieldType.DELTAP);
        setPresetAttributes(presetAttributes);

    }

    public String getNOPPM() {
        return NOPPM;
    }

    public void setNOPPM(String NOPPM) {
        this.NOPPM = NOPPM;
        presetAttributes.add(EpocTestFieldType.NOPPM);
        setPresetAttributes(presetAttributes);

    }

    public String getRQ() {
        return RQ;
    }

    public void setRQ(String RQ) {
        this.RQ = RQ;
        presetAttributes.add(EpocTestFieldType.RQ);
        setPresetAttributes(presetAttributes);

    }
    @Ignore
    private Gender patientGender = Gender.ENUM_UNINITIALIZED;
    private int patientGenderValue = Gender.ENUM_UNINITIALIZED.value;

    public Gender getPatientGender() {
        return Gender.fromInt(getPatientGenderValue());
    }

    public void setPatientGender(Gender patientGender) {
        setPatientGenderValue(patientGender.value);
        presetAttributes.add(EpocTestFieldType.PATIENTGENDER);
        setPresetAttributes(presetAttributes);

    }

    private int getPatientGenderValue() {
        return patientGenderValue;
    }

    private void setPatientGenderValue(int patientGenderValue) {
        this.patientGenderValue = patientGenderValue;
    }
}
