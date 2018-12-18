package com.epocal.common.realmentities;

import com.epocal.common.types.am.Gender;
import com.epocal.common.types.SyncState;
import com.epocal.common.types.TestErrorCode;
import com.epocal.common.types.am.TestMode;
import com.epocal.common.types.TestStatus;
import com.epocal.common.types.TestType;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bmate on 4/3/2017.
 */

public class TestRecord extends RealmObject {
    @PrimaryKey
    private long id =-1;
    private RealmList<TestResult> testResults;
    private String barcode = "";
    private Date testDateTime;
    private Date uploaded;
    private CardLot cardLot = new CardLot();
    private Reader reader;
    private Host mHost;
    private User user;
    private String guestName = "";
    private int internalErrorCode;
    private boolean hasCritical;
    private String subjectId = "";
    private String id2 = "";
    private TestDetail testDetail = null;
    private RespiratoryDetail respiratoryDetail;
    private Integer patientAge= -1;
    private String workflowItems = "";
    private boolean isRejected;
    private Date lastEqcDateTime;
    private String sensorConfigVersion;
    private Boolean isReportable;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RealmList<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(RealmList<TestResult> testResults) {
        this.testResults = testResults;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Date getTestDateTime() {
        return testDateTime;
    }

    public void setTestDateTime(Date testDateTime) {
        this.testDateTime = testDateTime;
    }

    public Date getUploaded() {
        return uploaded;
    }

    public void setUploaded(Date uploaded) {
        this.uploaded = uploaded;
    }

    public CardLot getCardLot() {
        return cardLot;
    }

    public void setCardLot(CardLot cardLot) {
        this.cardLot = cardLot;
    }

    public Host getHost() {
        return mHost;
    }

    public void setHost(Host host) {
        mHost = host;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public int getInternalErrorCode() {
        return internalErrorCode;
    }

    public void setInternalErrorCode(int internalErrorCode) {
        this.internalErrorCode = internalErrorCode;
    }

    public boolean isHasCritical() {
        return hasCritical;
    }

    public void setHasCritical(boolean hasCritical) {
        this.hasCritical = hasCritical;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public TestDetail getTestDetail() {
        return testDetail;
    }

    public void setTestDetail(TestDetail testDetail) {

        this.testDetail = testDetail;
    }

    public RespiratoryDetail getRespiratoryDetail() {
        return respiratoryDetail;
    }

    public void setRespiratoryDetail(RespiratoryDetail respiratoryDetail) {
        this.respiratoryDetail = respiratoryDetail;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getWorkflowItems() {
        return workflowItems;
    }

    public void setWorkflowItems(String testVariableList) {
        this.workflowItems = testVariableList;
    }

    public boolean getRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public Date getLastEqcDateTime() {
        return lastEqcDateTime;
    }

    public void setLastEqcDateTime(Date lastEqcDateTime) {
        this.lastEqcDateTime = lastEqcDateTime;
    }

    public String getSensorConfigVersion() {
        return sensorConfigVersion;
    }

    public void setSensorConfigVersion(String sensorConfigVersion) {
        this.sensorConfigVersion = sensorConfigVersion;
    }

    public Boolean getReportable() {
        return isReportable;
    }

    public void setReportable(Boolean reportable) {
        isReportable = reportable;
    }

    @Ignore
    private TestStatus mStatus;
    private int testStatus;

    public TestStatus getStatus() {
        return TestStatus.fromInt(getTestStatus());
    }

    public void setStatus(TestStatus status) {
       setTestStatus(status.value);
    }

    private int getTestStatus() {
        return testStatus;
    }

    private void setTestStatus(int testStatus) {
        this.testStatus = testStatus;
    }

    @Ignore
    private TestErrorCode testErrorCode;
    private int errorCode;

    public TestErrorCode getTestErrorCode() {
        return TestErrorCode.fromInt(getErrorCode());
    }

    public void setTestErrorCode(TestErrorCode testErrorCode) {
        setErrorCode(testErrorCode.value);
    }

    private int getErrorCode() {
        return errorCode;
    }

    private void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Ignore
    private TestType type;
    private int testType;

    public TestType getType() {
        return TestType.fromInt(getTestType());
    }

    public void setType(TestType type) {
        setTestType(type.value);
    }

    private int getTestType() {
        return testType;
    }

    private void setTestType(int testType) {
        this.testType = testType;
    }

    @Ignore
    private TestMode mTestMode;
    private int testModeValue = TestMode.ENUM_UNINITIALIZED.value;

    public TestMode getTestMode() {
        return TestMode.fromInt(getTestModeValue());
    }

    public void setTestMode(TestMode testMode) {
        setTestModeValue(testMode.value);
    }

    private int getTestModeValue() {
        return testModeValue;
    }

    private void setTestModeValue(int testModeValue) {
        this.testModeValue = testModeValue;
    }

    @Ignore
    private Gender gender;
    private int patientGender = Gender.ENUM_UNINITIALIZED.value;

    public Gender getGender() {
        return Gender.fromInt(getPatientGender());
    }

    public void setGender(Gender gender) {
        setPatientGender(gender.value);
    }

    private int getPatientGender() {
        return patientGender;
    }

    private void setPatientGender(int patientGender) {
        this.patientGender = patientGender;
    }


    @Ignore
    private SyncState syncState;
    private int synchronizationState = 0;

    public SyncState getSyncState() {
        return SyncState.fromInt(getSynchronizationState());
    }

    public void setSyncState(SyncState syncState) {
        setSynchronizationState(syncState.value);
    }

    private int getSynchronizationState() {
        return synchronizationState;
    }

    private void setSynchronizationState(int synchronizationState) {
        this.synchronizationState = synchronizationState;
    }

    public void updateFrom(TestRecord input){
        if (input!= null) {
            this.setBarcode(input.getBarcode());
            this.setGender(input.getGender());
            this.setGuestName(input.getGuestName());
            this.setHasCritical(input.isHasCritical());
            this.setId2(input.getId2());
            this.setInternalErrorCode(input.getInternalErrorCode());
            this.setLastEqcDateTime(input.getLastEqcDateTime());
            this.setPatientAge(input.getPatientAge());
            this.setRejected(input.getRejected());
            this.setReportable(input.getReportable());
            this.getRespiratoryDetail().updateFrom(input.getRespiratoryDetail());
            this.setSensorConfigVersion(input.getSensorConfigVersion());
            this.setStatus(input.getStatus());
            this.setSubjectId(input.getSubjectId());
            this.setSyncState(input.getSyncState());
            this.setTestDateTime(input.getTestDateTime());
            //this.setTestDetail(input.getTestDetail());
            this.setTestErrorCode(input.getTestErrorCode());
            this.setTestMode(input.getTestMode());
            //this.setTestResults(input.getTestResults());
            this.setWorkflowItems(input.getWorkflowItems());
            this.setType(input.getType());
            this.setUploaded(input.getUploaded());

        }

    }


}
