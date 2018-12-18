package com.epocal.common.realmentities;

import com.epocal.common.types.am.AGAPType;
import com.epocal.common.types.AllowCustomEntryType;
import com.epocal.common.types.AuthorizationLogin;
//import com.epocal.common.types.HemodilutionPolicy;
import com.epocal.common.types.LogoutPowerOffType;
import com.epocal.common.types.ReaderType;
import com.epocal.common.types.SaveRawData;
import com.epocal.common.types.am.Temperatures;
import com.epocal.util.EnumSetUtil;

import java.util.EnumSet;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 4/4/2017.
 */

public class HostConfiguration extends RealmObject {
    // SaveRawData enum mapping starts

    @Ignore
    private SaveRawData mSaveRawDataMode;
    private Integer mSaveRawDataModeValue;

    public SaveRawData getSaveRawDataMode() {
        return SaveRawData.fromInt(getSaveRawDataModeValue());
    }

    public void setSaveRawDataMode(SaveRawData saveRawDataMode) {
        setSaveRawDataModeValue(saveRawDataMode.value);
    }

    private Integer getSaveRawDataModeValue() {
        return mSaveRawDataModeValue;
    }

    private void setSaveRawDataModeValue(Integer saveRawDataModeValue) {
        mSaveRawDataModeValue = saveRawDataModeValue;
    }
// enum mapping ends

    //AuthorizationLogin enum mapping starts

    @Ignore
    private AuthorizationLogin mAuthorizationLogin;
    private Integer mAuthorizationLoginValue;

    public AuthorizationLogin getAuthorizationLogin() {
        return AuthorizationLogin.fromInt(getAuthorizationLoginValue());
    }

    public void setAuthorizationLogin(AuthorizationLogin authorizationLogin) {
        setAuthorizationLoginValue(authorizationLogin.value);
    }

    private Integer getAuthorizationLoginValue() {
        return mAuthorizationLoginValue;
    }

    private void setAuthorizationLoginValue(Integer authorizationLoginValue) {
        mAuthorizationLoginValue = authorizationLoginValue;
    }
    //AuthoriztationLogin enum mapping ends


//    // HemodilutionPolicy enum mapping starts here
//
//    @Ignore
//    private HemodilutionPolicy mHemodilutionPolicy;
//    private Integer mHemodilutionPolicyValue = HemodilutionPolicy.Never.value;
//
//    public HemodilutionPolicy getHemodilutionPolicy() {
//        return HemodilutionPolicy.fromInt(getHemodilutionPolicyValue());
//    }
//
//    public void setHemodilutionPolicy(HemodilutionPolicy hemodilutionPolicy) {
//        setHemodilutionPolicyValue(hemodilutionPolicy.value);
//    }
//
//    private Integer getHemodilutionPolicyValue() {
//        return mHemodilutionPolicyValue;
//    }
//
//    private void setHemodilutionPolicyValue(Integer hemodilutionPolicyValue) {
//        mHemodilutionPolicyValue = hemodilutionPolicyValue;
//    }
//    // HemodilutionPolicy enum mapping ends here


    // PowerOffWhenInactive enum mapping start


    @Ignore
    private LogoutPowerOffType mLogoutPowerOffType;
    private Integer mLogoutPowerOffTypeValue;

    public LogoutPowerOffType getLogoutPowerOffType() {
        return LogoutPowerOffType.fromInt(getLogoutPowerOffTypeValue());
    }

    public void setLogoutPowerOffType(LogoutPowerOffType logoutPowerOffType){
        setLogoutPowerOffTypeValue(logoutPowerOffType.value);
    }

    private Integer getLogoutPowerOffTypeValue() {
        return mLogoutPowerOffTypeValue;
    }

    private void setLogoutPowerOffTypeValue(Integer logoutPowerOffTypeValue) {
        mLogoutPowerOffTypeValue = logoutPowerOffTypeValue;
    }
    // PowerOffWhenInactive enum mapping stop

    // TemperatureUnit enum mapping start
    private Integer TemperatureUnittype;
    @Ignore
    private Temperatures TemperatureUnit;
    private Integer getTemperatureUnittype() {
        return TemperatureUnittype;
    }
    private void setTemperatureUnittype(Integer temperatureUnittype) {
        TemperatureUnittype = temperatureUnittype;
    }
    public Temperatures getTemperatureUnit() {
        return Temperatures.fromInt(getTemperatureUnittype());
    }
    public void setTemperatureUnit(Temperatures temperatureUnit) {
        setTemperatureUnittype(temperatureUnit.value);
    }
    // TemperatureUnit enum mapping stop


    //AGAPType enum mapping starts here

    @Ignore
    private AGAPType mAgapType;
    private Integer mAgapTypeValue;

    public AGAPType getAgapType() {
        return AGAPType.fromInt(getAgapTypeValue());
    }

    public void setAgapType(AGAPType agapType) {
        setAgapTypeValue(agapType.value);
    }

    private Integer getAgapTypeValue() {
        return mAgapTypeValue;
    }

    private void setAgapTypeValue(Integer agapTypeValue) {
        mAgapTypeValue = agapTypeValue;
    }
    //AGAPType enum mapping stops here

    // ReaderType enum mapping starts

    @Ignore
    private ReaderType mReaderType;
    private Integer mReaderTypeValue;

    public ReaderType getReaderType() {
        return ReaderType.fromInt(getReaderTypeValue());
    }

    public void setReaderType(ReaderType readerType) {
        setReaderTypeValue(readerType.value);
    }

    private Integer getReaderTypeValue() {
        return mReaderTypeValue;
    }

    private void setReaderTypeValue(Integer readerTypeValue) {
        mReaderTypeValue = readerTypeValue;
    }
    // ReaderType enum mapping stop

    // AllowCustomEntry enumSet mapping starts

    @Ignore
    private EnumSet<AllowCustomEntryType> mAllowCustomEntryTypes;
    private long mAllowCustomEntryValue;

    public EnumSet<AllowCustomEntryType> getAllowCustomEntryTypes() {
        return EnumSetUtil.toSet(getAllowCustomEntryValue(),AllowCustomEntryType.class);
    }

    public void setAllowCustomEntryTypes(EnumSet<AllowCustomEntryType> allowCustomEntryTypes) {
       setAllowCustomEntryValue(EnumSetUtil.encode(allowCustomEntryTypes));
    }

    private long getAllowCustomEntryValue() {
        return mAllowCustomEntryValue;
    }

    private void setAllowCustomEntryValue(long allowCustomEntryValue) {
        mAllowCustomEntryValue = allowCustomEntryValue;
    }

    private boolean AllowExpiredCards;
    private boolean AllowIndividualTestSelection;
    private boolean CloseUnattendedTests;
    private boolean EnableInactivityTimer;
    private int InactivityTimer;
    private String Department;
    private String HospitalName;
    private boolean LogoutWhenInactive;
    private boolean BackgroundsyncEnabled;
    private boolean ScheduledsyncEnabled;
    private boolean AllowRecallData;
    private boolean RetainPatientId;
    private boolean RetainSampleType;
    private boolean ReaderMaintenanceEnabled;
    private boolean AllowRejectTests;
    private boolean EnforceCriticalHandling;
    private boolean EnablePatientIdLookup;
    private boolean BUNEnabled;
    private String SensorConfigVersion;
    private boolean DisplayBEb;
    private boolean DisplayBEecf;
    private boolean PrintRangeIfHighLow;
    private boolean PrintQARange;
    private boolean PrintQAInfo;

    public boolean isDisplayBEb() {
        return DisplayBEb;
    }

    public void setDisplayBEb(boolean displayBEb) {
        DisplayBEb = displayBEb;
    }

    public boolean isDisplayBEecf() {
        return DisplayBEecf;
    }

    public void setDisplayBEecf(boolean displayBEecf) {
        DisplayBEecf = displayBEecf;
    }

    public String getSensorConfigVersion() {
        return SensorConfigVersion;
    }

    public void setSensorConfigVersion(String sensorConfigVersion) {
        SensorConfigVersion = sensorConfigVersion;
    }

    public boolean isAllowExpiredCards() {
        return AllowExpiredCards;
    }

    public void setAllowExpiredCards(boolean allowExpiredCards) {
        AllowExpiredCards = allowExpiredCards;
    }

    public boolean isAllowIndividualTestSelection() {
        return AllowIndividualTestSelection;
    }

    public void setAllowIndividualTestSelection(boolean allowIndividualTestSelection) {
        AllowIndividualTestSelection = allowIndividualTestSelection;
    }

    public boolean isCloseUnattendedTests() {
        return CloseUnattendedTests;
    }

    public void setCloseUnattendedTests(boolean closeUnattendedTests) {
        CloseUnattendedTests = closeUnattendedTests;
    }

    public boolean isEnableInactivityTimer() {
        return EnableInactivityTimer;
    }

    public void setEnableInactivityTimer(boolean enableInactivityTimer) {
        EnableInactivityTimer = enableInactivityTimer;
    }

    public int getInactivityTimer() {
        return InactivityTimer;
    }

    public void setInactivityTimer(int inactivityTimer) {
        InactivityTimer = inactivityTimer;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public boolean isLogoutWhenInactive() {
        return LogoutWhenInactive;
    }

    public void setLogoutWhenInactive(boolean logoutWhenInactive) {
        LogoutWhenInactive = logoutWhenInactive;
    }

    public boolean isBackgroundsyncEnabled() {
        return BackgroundsyncEnabled;
    }

    public void setBackgroundsyncEnabled(boolean backgroundsyncEnabled) {
        BackgroundsyncEnabled = backgroundsyncEnabled;
    }

    public boolean isScheduledsyncEnabled() {
        return ScheduledsyncEnabled;
    }

    public void setScheduledsyncEnabled(boolean scheduledsyncEnabled) {
        ScheduledsyncEnabled = scheduledsyncEnabled;
    }

    public boolean isAllowRecallData() {
        return AllowRecallData;
    }

    public void setAllowRecallData(boolean allowRecallData) {
        AllowRecallData = allowRecallData;
    }

    public boolean isRetainPatientId() {
        return RetainPatientId;
    }

    public void setRetainPatientId(boolean retainPatientId) {
        RetainPatientId = retainPatientId;
    }

    public boolean isRetainSampleType() {
        return RetainSampleType;
    }

    public void setRetainSampleType(boolean retainSampleType) {
        RetainSampleType = retainSampleType;
    }

    public boolean isReaderMaintenanceEnabled() {
        return ReaderMaintenanceEnabled;
    }

    public void setReaderMaintenanceEnabled(boolean readerMaintenanceEnabled) {
        ReaderMaintenanceEnabled = readerMaintenanceEnabled;
    }

    public boolean isAllowRejectTests() {
        return AllowRejectTests;
    }

    public void setAllowRejectTests(boolean allowRejectTests) {
        AllowRejectTests = allowRejectTests;
    }

    public boolean isEnforceCriticalHandling() {
        return EnforceCriticalHandling;
    }

    public void setEnforceCriticalHandling(boolean enforceCriticalHandling) {
        EnforceCriticalHandling = enforceCriticalHandling;
    }

    public boolean isEnablePatientIdLookup() {
        return EnablePatientIdLookup;
    }

    public void setEnablePatientIdLookup(boolean enablePatientIdLookup) {
        EnablePatientIdLookup = enablePatientIdLookup;
    }

    public boolean isBUNEnabled() {
        return BUNEnabled;
    }

    public void setBUNEnabled(boolean BUNEnabled) {
        this.BUNEnabled = BUNEnabled;
    }

    public boolean getPrintRangeIfHighLow() {
        return PrintRangeIfHighLow;
    }

    public void setPrintRangeIfHighLow(boolean printRangeIfHighLow) {
        this.PrintRangeIfHighLow = printRangeIfHighLow;
    }

    public boolean getPrintQARange() {
        return PrintQARange;
    }

    public void setPrintQARange(boolean printQARange) {
        this.PrintQARange = printQARange;
    }

    public boolean getPrintQAInfo() {
        return PrintQAInfo;
    }

    public void setPrintQAInfo(boolean printQAInfo) {
        this.PrintQAInfo = printQAInfo;
    }

    public void resetFactoryDefault(boolean isNextGenMode) {
        setSaveRawDataMode(SaveRawData.OnFailure);
        setAuthorizationLogin(AuthorizationLogin.None);
      //  setHemodilutionPolicy(HemodilutionPolicy.ForceSelection);
        setLogoutPowerOffType(LogoutPowerOffType.ON_BATTERY);
        setTemperatureUnit(Temperatures.C);
        setAgapType(AGAPType.AGAPK);
        if (isNextGenMode) {
            setReaderType(ReaderType.NEXTGEN_READER);
        } else {
            setReaderType(ReaderType.BGE_READER);
        }
        setAllowCustomEntryTypes(EnumSet.of(AllowCustomEntryType.NoneAllowed));
        AllowExpiredCards = false;
        AllowIndividualTestSelection = true;
        CloseUnattendedTests = true;
        EnableInactivityTimer = true;
        InactivityTimer = 10; // 10 minutes
        Department = "Default";
        HospitalName = "";
        LogoutWhenInactive = true;
        BackgroundsyncEnabled = false;
        ScheduledsyncEnabled = false;
        AllowRecallData = true;
        RetainPatientId = false;
        RetainSampleType = false;
        ReaderMaintenanceEnabled = false;
        AllowRejectTests = true;
        EnforceCriticalHandling = true;
        EnablePatientIdLookup = false;
        BUNEnabled = true;
        SensorConfigVersion = "";
        DisplayBEb = true;
        DisplayBEecf = true;
        PrintRangeIfHighLow = false;
        PrintQARange = false;
        PrintQAInfo = false;
    }
}
