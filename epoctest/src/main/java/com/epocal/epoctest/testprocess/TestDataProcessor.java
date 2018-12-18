package com.epocal.epoctest.testprocess;

import android.util.Log;

import com.epocal.common.am.FinalResult;
import com.epocal.common.am.HumidityStruct;
import com.epocal.common.am.Reading;
import com.epocal.common.am.RealTimeQC;
import com.epocal.common.am.SensorInfo;
import com.epocal.common.am.SensorReadings;
import com.epocal.common.androidutil.FileSystemUtil;
import com.epocal.common.androidutil.RxUtil;
import com.epocal.common.epocobjects.AnalyteOption;
import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.common.eventmessages.TestDataProcessorCallback;
import com.epocal.common.eventmessages.TestDataProcessorEventType;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.realmentities.CardLot;
import com.epocal.common.realmentities.CustomTestVariable;
import com.epocal.common.realmentities.EqcInfo;
import com.epocal.common.realmentities.EqcValue;
import com.epocal.common.realmentities.Host;
import com.epocal.common.realmentities.HostConfiguration;
import com.epocal.common.realmentities.QARangeValue;
import com.epocal.common.realmentities.Range;
import com.epocal.common.realmentities.RangeValue;
import com.epocal.common.realmentities.Reader;
import com.epocal.common.realmentities.RespiratoryDetail;
import com.epocal.common.realmentities.TestDetail;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.TestResult;
import com.epocal.common.types.AnalyteType;
import com.epocal.common.types.CardFactoryType;
import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.common.types.NotifyActionType;
import com.epocal.common.types.PatientIDEntryMethod;
import com.epocal.common.types.PatientIDLookupCode;
import com.epocal.common.types.PressureType;
import com.epocal.common.types.QAFluidType;
import com.epocal.common.types.RangeIgnoreInfo;
import com.epocal.common.types.ResultStatus;
import com.epocal.common.types.SyncState;
import com.epocal.common.types.TestErrorCode;
import com.epocal.common.types.TestStatus;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.AgeCategory;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.BubbleDetectMode;
import com.epocal.common.types.am.ChannelType;
import com.epocal.common.types.am.EGFRFormula;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.am.HumidityReturnCode;
import com.epocal.common.types.am.RealTimeHematocritQCReturnCode;
import com.epocal.common.types.am.RealTimeQCReturnCode;
import com.epocal.common.types.am.RealTimeQCType;
import com.epocal.common.types.am.ResultsCalcReturnCode;
import com.epocal.common.types.am.Sensors;
import com.epocal.common.types.am.Temperatures;
import com.epocal.common.types.am.TestMode;
import com.epocal.common.types.am.Units;
import com.epocal.datamanager.AnalyteModel;
import com.epocal.datamanager.HostConfigurationModel;
import com.epocal.datamanager.QASampleInfoModel;
import com.epocal.datamanager.RangeModel;
import com.epocal.datamanager.ReaderModel;
import com.epocal.datamanager.SelenaModel;
import com.epocal.datamanager.TestRecordModel;
import com.epocal.datamanager.UserModel;
import com.epocal.datamanager.WorkflowRepository;
import com.epocal.epoclog.LogFile;
import com.epocal.epoclog.LogServer;
import com.epocal.epoctest.TestConfigurationManager;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestManager;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.analyticalmanager.AnalyticalManager;
import com.epocal.epoctest.analyticalmanager.CheckForEarlyInjectionResponse;
import com.epocal.epoctest.analyticalmanager.ComputeCalculatedResultsResponse;
import com.epocal.epoctest.analyticalmanager.ComputeCorrectedResultsResponse;
import com.epocal.epoctest.analyticalmanager.PerformRealTimeQCResponse;
import com.epocal.epoctest.enumtype.BarcodeVerificationCode;
import com.epocal.epoctest.enumtype.QAScheduleLookoutReturnCode;
import com.epocal.epoctest.enumtype.ValidateAnalyteTestReturnCode;
import com.epocal.epoctest.enumtype.legacy.QCLockoutHandlerReturnCode;
import com.epocal.epoctest.testconfiguration.CardSetup;
import com.epocal.epoctest.testconfiguration.ChannelConfig;
import com.epocal.epoctest.testconfiguration.InsanityRange;
import com.epocal.epoctest.testconfiguration.ReportableRange;
import com.epocal.epoctest.testconfiguration.SequenceItem;
import com.epocal.epoctest.testconfiguration.TestConfiguration;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyVersionManager;
import com.epocal.epoctest.teststate.legacyepoctest.type.TestCardInsertedInfo;
import com.epocal.epoctest.type.BarcodeInformation;
import com.epocal.epoctest.type.ChannelTypeToSensorType;
import com.epocal.epoctest.type.DeviceIdInfoData;
import com.epocal.epoctest.type.TestDataFormatConvert;
import com.epocal.epoctest.type.TestRawResultCollection;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.common.EpocVersion;
import com.epocal.reader.enumtype.DAMStageType;
import com.epocal.reader.enumtype.ReaderStateType;
import com.epocal.reader.enumtype.legacy.DAMStages;
import com.epocal.reader.enumtype.legacy.DataPacketType;
import com.epocal.reader.enumtype.legacy.DryCardTransmissionMode;
import com.epocal.reader.enumtype.legacy.LegacyBubbleDetectMode;
import com.epocal.reader.enumtype.legacy.ReaderMode;
import com.epocal.reader.enumtype.legacy.TransmissionMode;
import com.epocal.reader.legacy.message.request.configuration.LegacyReqConfig1_1;
import com.epocal.reader.legacy.message.request.configuration.LegacyReqConfig1_2;
import com.epocal.reader.legacy.message.request.configuration.LegacyReqConfig1_3;
import com.epocal.reader.legacy.message.request.configuration.LegacyReqConfig1_4;
import com.epocal.reader.legacy.message.response.LegacyRspDataPacket;
import com.epocal.reader.legacy.message.response.LegacyRspDeviceID;
import com.epocal.reader.legacy.message.response.LegacyRspStatistics;
import com.epocal.reader.legacy.message.response.LegacyRspStatus;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspBGETest;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.reader.protocolcommontype.PMIIndicators;
import com.epocal.reader.protocolcommontype.Sequence;
import com.epocal.reader.type.DPMActionData;
import com.epocal.reader.type.DeviceStatusData;
import com.epocal.reader.type.Legacy.ReaderQCInfo;
import com.epocal.reader.type.Legacy.ReaderTQAInfo;
import com.epocal.reader.type.Legacy.SelfTestPacketInfo;
import com.epocal.reader.type.TestActionData;
import com.epocal.util.DateUtil;
import com.epocal.util.DecimalConversionUtil;
import com.epocal.util.RefWrappers;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;

//import com.epocal.common.types.HemodilutionPolicy;

/**
 * Created by dning on 6/23/2017.
 */
public class TestDataProcessor implements LegacyRspDataPacket.IDataPacketGetConfig {

    private ArrayList<AnalyteOption> mDefaultTestSelectionInclusions;
    private ArrayList<AnalyteName> mRuntimeTestSelectionInclusions;

    private DeviceStatusData mDeviceStatusData;

    private TestType mTestType;

    public TestType getTestType() {
        return mTestType;
    }

    public void setTestType(TestType testType) {
        this.mTestType = testType;
    }


    private TestRecord mTestRecord;

    public TestRecord getTestRecord() {
        return mTestRecord;
    }

    public void setTestRecord(TestRecord testRecord) {
        mTestRecord = testRecord;
    }

    public DeviceStatusData getDeviceStatusData() {
        return mDeviceStatusData;
    }

    private boolean mCalibrationFlag;

    public boolean isCalibrationFlag() {
        return mCalibrationFlag;
    }

    public void setCalibrationFlag(boolean mCalibrationFlag) {
        this.mCalibrationFlag = mCalibrationFlag;
    }

    public void setDeviceStatusData(DeviceStatusData mDeviceStatusData) {
        this.mDeviceStatusData = mDeviceStatusData;
    }

    private DeviceIdInfoData mDeviceIdInfoData;

    public DeviceIdInfoData getDeviceIdInfoData() {
        return mDeviceIdInfoData;
    }

    public void setDeviceIdInfoData(DeviceIdInfoData mDeviceIdInfoData) {
        this.mDeviceIdInfoData = mDeviceIdInfoData;
    }

    private double mShortestGoodInjection = DecimalConversionUtil.round(0.2, 2);
    private double mLongestGoodInjection = DecimalConversionUtil.round(3.4, 2);

    private FileSystemUtil mBGETestDataFile;
    private FileSystemUtil mDPMTestDataFile;
    private FileSystemUtil mTestPacketFile;

    private boolean mOneTimeFlag = false;

    private byte mAllFluidConductivityByte;

    private boolean mOldTestCalculated;
    private int mNumTestsRun;
    private int mNumSuccessfulTests;
    private int mBytesReceived;
    private int mNumConductivity;
    private int mHematocritReadings;
    private int mTopHeaterReadings;

    private short mBGETestPacketsReceived;
    private short mDPMTestPacketsReceived;

    private short mCalpackets;
    private short mBubblepackets;
    private short mSamplepackets;

    private short mDPMCalpackets;
    private short mDPMBubblepackets;
    private short mDPMSamplepackets;

    private short mCardType;
    private short mSensorLayout;

    private double mCalibrationTime;
    private double mBubbleDetect;
    private double mSampleDetect;
    private float mLastRecordedTime;
    private float mLastRecordedTimePeripheral;

    private float mTimeIncrement;
    private float mDPMTimeIncrement;

    private double mFirstFluid;
    private double mBubbleBegin;
    private double mBubbleEnd;
    private Calendar mTestTime;
    private Calendar mConnectionTime;

    private RealTimeHematocritQCReturnCode mRealTimehctRc;
    private String mBarcodeString = "";
    private BarcodeInformation mBarcodeInformation;
    private HostErrorCode mErrorCode;

    private float mAmbientTemperature;
    private float mBarometricPressureSensor;

    private List<Reading> mTopHeaterPid;
    private List<SensorReadings> mTestReadings;
    private List<Sequence> mTestSequence;
    private List<SensorInfo> mSensorDescriptors;
    private RealTimeQC mRealTimeQC;

    private Date mLastSelfTestTime;
    private TestConfiguration mTestConfiguration;
    private PerformTestCalculation mPerformTestCalculation;
    private boolean mTestFirstTimeSaved = true;
    private double mCthbLevel = Double.NaN;
    private boolean mShowBEb = true;
    private boolean mShowBEecf = true;
    private boolean mExistsFailedIQC = false;

    private String mTimeSpecificString;
    private String mDateSpecificString;

    public TestConfiguration getTestConfiguration() {
        return mTestConfiguration;
    }

    public Date getLastSelfTestTime() {
        return mLastSelfTestTime;
    }

    private void setLastSelfTestTime(Date mLastSelfTestTime) {
        this.mLastSelfTestTime = mLastSelfTestTime;
    }

    private TestStatus previousTestStatus;

    private TestStatus getPreviousTestStatus() {
        return previousTestStatus;
    }

    public void setPreviousTestStatus(TestStatus previousTestStatus) {
        this.previousTestStatus = previousTestStatus;
    }

    public ArrayList<AnalyteOption> getDefaultTestSelectionInclusions() {
        if (mDefaultTestSelectionInclusions == null) {
            mDefaultTestSelectionInclusions = new ArrayList<>();
        }
        return mDefaultTestSelectionInclusions;
    }

    public void setDefaultTestSelectionInclusions(ArrayList<AnalyteOption> mDefaultTestSelectionInclusions) {
        this.mDefaultTestSelectionInclusions = mDefaultTestSelectionInclusions;
    }

    public ArrayList<AnalyteName> getRuntimeTestSelectionInclusions() {
        if (mRuntimeTestSelectionInclusions == null) {
            mRuntimeTestSelectionInclusions = new ArrayList<>();

        }
        if (mRuntimeTestSelectionInclusions.size() == 0) {
            initRuntimeTestInclusions();
        }
        return mRuntimeTestSelectionInclusions;
    }

    public void initRuntimeTestInclusions() {
        if (mRuntimeTestSelectionInclusions.size() > 0) mRuntimeTestSelectionInclusions.clear();
        if (mDefaultTestSelectionInclusions.size() > 0) {
            for (AnalyteOption daop : mDefaultTestSelectionInclusions) {
                mRuntimeTestSelectionInclusions.add(daop.getAnalyteName());
            }
        }
    }

    public void setRuntimeTestSelectionInclusions(ArrayList<AnalyteName> mRuntimeTestSelectionInclusions) {
        this.mRuntimeTestSelectionInclusions = mRuntimeTestSelectionInclusions;

    }

    public boolean previousTestIncomplete() {
        return getPreviousTestStatus().equals(TestStatus.Incomplete);
    }

    public Calendar getConnectionTime() {
        return mConnectionTime;
    }

    public void setConnectionTime(Calendar mConnectionTime) {
        this.mConnectionTime = mConnectionTime;
    }

    public HostErrorCode getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(HostErrorCode mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public double getBubbleDetect() {
        return mBubbleDetect;
    }

    public double getSampleDetect() {
        return mSampleDetect;
    }

    public void setSampleDetect(double sampleDetect) {
        mSampleDetect = sampleDetect;
    }

    public void setBubbleDetect(float mBubbleDetect) {
        this.mBubbleDetect = mBubbleDetect;
    }

    public double getBubbleBegin() {
        return mBubbleBegin;
    }

    public double getBubbleEnd() {
        return mBubbleEnd;
    }

    public float getLastRecordedTime() {
        return mLastRecordedTime;
    }

    public void setLastRecordedTime(float mLastRecordedTime) {
        this.mLastRecordedTime = mLastRecordedTime;
    }

    public float getTimeIncrement() {
        return mTimeIncrement;
    }

    private void setTimeIncrement(float mTimeIncrement) {
        this.mTimeIncrement = mTimeIncrement;
    }

    private void setNumConductivityPlus() {
        this.mNumConductivity++;
    }

    public boolean getOneTimeFlag() {
        return mOneTimeFlag;
    }

    public void setOneTimeFlag(boolean mOneTimeFlag) {
        this.mOneTimeFlag = mOneTimeFlag;
    }

    public List<SensorReadings> getTestReadings() {
        return mTestReadings;
    }

    public float getAmbientTemperature() {
        return mAmbientTemperature;
    }

    public float getBarometricPressureSensor() {
        return mBarometricPressureSensor;
    }

    public boolean isTestFirstTimeSaved() {
        return mTestFirstTimeSaved;
    }

    public List<Reading> getTopHeaterPid() {
        return mTopHeaterPid;
    }

    public short getSensorLayout() {
        return mSensorLayout;
    }

    public void setSensorLayout(short val) {
        mSensorLayout = val;
    }

    public BarcodeInformation getBarcodeInformation() {
        return mBarcodeInformation;
    }

    public boolean allowExpiredCards() {
        return new HostConfigurationModel().isAllowExpiredCards();
    }

    public Calendar getTestTime() {
        return mTestTime;
    }

    public TestDataProcessor() {
        mRealTimehctRc = RealTimeHematocritQCReturnCode.NotPerformed;
        mDeviceIdInfoData = new DeviceIdInfoData();

        mTestSequence = new ArrayList<>();
        mSensorDescriptors = new ArrayList<>();
        mTestReadings = new ArrayList<>();
        mTopHeaterPid = new ArrayList<>();

        mOldTestCalculated = false;
        mAllFluidConductivityByte = 0;
        mTimeIncrement = 0;
        mNumTestsRun = 0;
        mNumSuccessfulTests = 0;
        mConnectionTime = DateUtil.now();
        mTimeSpecificString = getTimeSpecificString();
        mDateSpecificString = getDateSpecificString();
        initializeTestDataProcessor();
        initializeLegacyTestData();
    }


    private void initializeTestDataProcessor() {
        if (mTestSequence != null) {
            mTestSequence.clear();
        }

        if (mSensorDescriptors != null) {
            mSensorDescriptors.clear();
        }
        mDefaultTestSelectionInclusions = new AnalyteModel().getDefaultTestInclusions();
    }

    public void initializeTestRecord(ReaderDevice readerDevice, TestMode testMode) {
        HostConfiguration hc = new HostConfigurationModel().getUnmanagedHostConfiguration();
        mTestRecord = new TestRecordModel().createUnmanagedTestRecord();
        if (getTestType() != null)
            mTestRecord.setType(getTestType());
        mTestRecord.setReader(findReader(readerDevice));
        mTestRecord.setTestMode(testMode);
        if (testMode.equals(TestMode.BloodTest)) {
            mTestRecord.setType(TestType.Blood);
            mTestRecord.getTestDetail().setSampleType(BloodSampleType.Unspecified);
        }
        mTestRecord.getTestDetail().setAmbientTemperatureType(hc.getTemperatureUnit());
        mTestRecord.getTestDetail().setPatientIDEntryMethod(PatientIDEntryMethod.None);
        if (hc.isEnablePatientIdLookup()) {
            mTestRecord.getTestDetail().setPatientIDLookupCode(PatientIDLookupCode.NotPerformed);

        } else {
            mTestRecord.getTestDetail().setPatientIDLookupCode(PatientIDLookupCode.NotEnabled);
        }
        mTestRecord.setSyncState(SyncState.Unknown);
        mTestRecord.setLastEqcDateTime(getLastSelfTestTime());
        WorkflowRepository wfr = new WorkflowRepository();
        mTestRecord.setWorkflowItems(wfr.getItemizedActiveWorkflow());
        wfr.setWorkflowValuesIntoTestRecord(mTestRecord);
        mTestRecord.setUser(new UserModel().getLoggedInUser());
        new SelenaModel().setupTestEnablement(mTestRecord);

        mTestRecord.setHost(mHost);        //added by rzhuang Aug 22
    }

    public static Host mHost;

    private CardLot findCardLot(String barcodeString, BarcodeInformation barcodeInformation, short cardType) {
        TestRecordModel trm = new TestRecordModel();
        CardLot cl = trm.findCardLot(barcodeInformation.getLotNumberStringWithDash());
        if (cl == null) {
            cl = new CardLot();
            cl.setLotNumber(barcodeInformation.getLotNumberStringWithDash());
            cl.setFactoryType(CardFactoryType.Ottawa1);
            trm.createCardLot(cl);

        }
        return cl;

    }

    // new reader will be saved to database only if connected to
    private Reader findReader(ReaderDevice readerDevice) {
        ReaderModel rm = new ReaderModel();
        Reader retval = rm.getReader(readerDevice.getDeviceAddress());
        if (retval == null) {
            // this new reader is still not in database until connection is established
            retval = readerDevice.toReader();
        }
        return retval;
    }

    //called from testUIDriver when test is started with a card
    public void resetTestRecord(boolean preserveData) {
        HostConfiguration hostConfig = new HostConfigurationModel().getUnmanagedHostConfiguration();
        initRuntimeTestInclusions();
        String patId = mTestRecord.getSubjectId();
        BloodSampleType bst = mTestRecord.getTestDetail().getSampleType();
        PatientIDEntryMethod patIdentry = mTestRecord.getTestDetail().getPatientIDEntryMethod();
        // mandatory keep
        // - workflowitems
        // - reader
        // - mode
        // -
        // -
        // mandatory reset
        mTestRecord.setId(-1L);

        if (mTestRecord.getTestResults() != null) {
            mTestRecord.getTestResults().clear();
        }
        mTestRecord.setTestErrorCode(TestErrorCode.NoError);
        mTestRecord.setStatus(TestStatus.Unknown);
        mTestRecord.setRejected(false);
        mTestRecord.setSyncState(SyncState.Unknown);
        mTestRecord.setTestDateTime(DateUtil.now().getTime());
        mTestRecord.setReportable(false);
        mTestRecord.getTestDetail().setPatientTemperatureType(hostConfig.getTemperatureUnit());
        mTestRecord.getTestDetail().setAmbientPressure(0d);
        mTestRecord.getTestDetail().setAmbientTemperature(0d);
        mTestRecord.getTestDetail().setDuration(null);
        mTestRecord.getTestDetail().setReadBack(false);
        mTestRecord.getTestDetail().setNotifyActionType(NotifyActionType.None);
        mTestRecord.getTestDetail().setNotifyDate(null);
        mTestRecord.getTestDetail().setNotifyName("");
        mTestRecord.getTestDetail().setNotifyTime("");
        mTestRecord.setHasCritical(false);
        // todo find out where the SensorConfigVersion is gonna come from
        mTestRecord.setSensorConfigVersion("");
        mTestRecord.setLastEqcDateTime(getLastSelfTestTime());
        // future: implement new realmentity: TestRecordReaderQCInfo and set here
        // mTestRecord.setTestRecordReaderQCInfo(null);

        if (!preserveData) {
            if (mTestRecord.getTestMode().equals(TestMode.BloodTest)) {
                mTestRecord.setType(TestType.Blood);
            }
            mTestRecord.setId2("");
            mTestRecord.setSubjectId("");
            mTestRecord.setPatientAge(-1);
            mTestRecord.setTestDetail(new TestDetail());
            mTestRecord.setRespiratoryDetail(new RespiratoryDetail());
            // todo sort the QASampleInfo issue out (future)
            mTestRecord.getTestDetail().setQaSampleInfo(new QASampleInfoModel().getUnmanagedSampleInfo(QAFluidType.None));
            mTestRecord.getTestDetail().setSampleType(BloodSampleType.Unspecified);
            mTestRecord.getTestDetail().setPatientIDEntryMethod(PatientIDEntryMethod.None);
            mTestRecord.setGender(Gender.ENUM_UNINITIALIZED);
            new SelenaModel().setupTestEnablement(mTestRecord);
            WorkflowRepository wfr = new WorkflowRepository();
            mTestRecord.setWorkflowItems(wfr.getItemizedActiveWorkflow());
            wfr.setWorkflowValuesIntoTestRecord(mTestRecord);
        }
        if (hostConfig.isRetainPatientId()) {
            mTestRecord.setSubjectId(patId);
            mTestRecord.getTestDetail().setPatientIDEntryMethod(patIdentry);
        }
        if (hostConfig.isRetainSampleType()) {
            mTestRecord.getTestDetail().setSampleType(bst);
        }
        //applyHemodilutionPolicy(preserveData, hostConfig.getHemodilutionPolicy());
    }

//    private void applyHemodilutionPolicy(boolean preserveData, HemodilutionPolicy hemodilutionPolicy) {
//        switch (hemodilutionPolicy) {
//            case Never:
//                mTestRecord.getTestDetail().setHemodilutionApplied(false);
//                break;
//            case Always:
//                mTestRecord.getTestDetail().setHemodilutionApplied(true);
//                break;
//            case ForceSelection:
//                mTestRecord.getTestDetail().setHemodilutionApplied(null);
//                break;
//            case ENUM_UNINITIALIZED:
//            default:
//                mTestRecord.getTestDetail().setHemodilutionApplied(false);
//                break;
//
//        }
//    }

    public void prepareForNewTest() {
        mConnectionTime = DateUtil.now();
        mExistsFailedIQC = false;
        mOldTestCalculated = false;
        mTestFirstTimeSaved = false;
        mTestTime = DateUtil.now();
        mTestRecord.setTestDateTime(mTestTime.getTime());
        mBubbleBegin = 0;
        mBubbleEnd = 0;
        mFirstFluid = 0;
        mCalibrationTime = 0;
        mErrorCode = HostErrorCode.NoError;
        mRealTimehctRc = RealTimeHematocritQCReturnCode.NotPerformed;
        mCalpackets = 0;
        mBubblepackets = 0;
        mSamplepackets = 0;
        mDPMCalpackets = 0;
        mDPMBubblepackets = 0;
        mDPMSamplepackets = 0;
        mBGETestPacketsReceived = 0;
        mDPMTestPacketsReceived = 0;
        mCardType = 0;
        mSensorLayout = 0;
        mBarcodeString = "";
        mBubbleDetect = 0;
        mSampleDetect = 0;
        mNumConductivity = 0;
        mLastRecordedTime = 0;
        mLastRecordedTimePeripheral = 0;
        mTimeIncrement = 0;
        mDPMTimeIncrement = 0;
        mBytesReceived = 0;
        if (mTopHeaterPid != null) {
            mTopHeaterPid.clear();
        } else {
            mTopHeaterPid = new ArrayList<>();
        }
        if (mBarcodeInformation != null) {
            mBarcodeInformation.reset();
        } else {
            mBarcodeInformation = new BarcodeInformation();
        }
        if (mNumTestsRun > 0) {
            int i;
            if (mTestReadings != null) {
                for (i = 0; i < mTestReadings.size(); i++) {
                    if (mTestReadings.get(i).readings != null) {
                        for (int j = 0; j < mTestReadings.get(i).readings.size(); j++) {
                            mTestReadings.get(i).readings.get(j).value = Float.NaN;
                        }
                        mTestReadings.get(i).readings.clear();
                        mTestReadings.get(i).result = Double.NaN;
                    }
                }
            }
        }

        mTimeSpecificString = getTimeSpecificString();
        mDateSpecificString = getDateSpecificString();
    }

    //called from PerformTestStart
    public void initializeForNewTest() {
        prepareRealTimeQCTest();
        prepareTestSequenceObject();
    }

    public void legacyInitializeForNewTest() {
        prepareRealTimeQCTest();
        legacyPrepareTestSequenceObject();
    }

    private void prepareRealTimeQCTest() {
        //just one time setup
        if (mRealTimeQC == null) {
            mRealTimeQC = new RealTimeQC();
            mRealTimeQC.continueIfFailed = true;
            mRealTimeQC.enabled = mTestConfiguration.RealTimeQCSetting.Enabled;
            mRealTimeQC.startTime = mTestConfiguration.RealTimeQCSetting.StartTime;
            mRealTimeQC.intervalTime = mTestConfiguration.RealTimeQCSetting.Interval;
            mRealTimeQC.type = RealTimeQCType.convert(mTestConfiguration.RealTimeQCSetting.RealTimeQCType);
            mRealTimeQC.numPoints = mTestConfiguration.RealTimeQCSetting.NumPoints;
            for (int j = 0; j < mTestConfiguration.RealTimeQCSetting.ExtraParameters.size(); j++) {
                if (mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterID == 1) {
                    mRealTimeQC.extra1 = mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterValue;
                } else if (mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterID == 2) {
                    mRealTimeQC.extra2 = mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterValue;
                } else if (mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterID == 3) {
                    mRealTimeQC.extra3 = mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterValue;
                } else if (mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterID == 4) {
                    mRealTimeQC.extra4 = mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterValue;
                } else if (mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterID == 5) {
                    mRealTimeQC.extra5 = mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterValue;
                } else if (mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterID == 6) {
                    mRealTimeQC.extra6 = mTestConfiguration.RealTimeQCSetting.ExtraParameters.get(j).extraParameterValue;
                }
            }
            mRealTimeQC.humidityUntil = mTestConfiguration.RealTimeQCSetting.HumidityDetectUntil;
            HumidityStruct humidityStruct = new HumidityStruct();
            humidityStruct.windowSize = (int) mTestConfiguration.HumidityDetects.get(0).WindowSize;
            humidityStruct.windowStart = (int) mTestConfiguration.HumidityDetects.get(0).StartTime;
            //humidityStruct.sensorType = Sensors.convert(mTestConfiguration.HumidityDetects.get(0).SensorType);
            humidityStruct.sensorType = Sensors.valueOf("Oxygen");
            humidityStruct.high = mTestConfiguration.HumidityDetects.get(0).HighLimit;
            humidityStruct.low = mTestConfiguration.HumidityDetects.get(0).LowLimit;
            humidityStruct.extra1 = mTestConfiguration.HumidityDetects.get(0).ExtraParameters.get(0).extraParameterValue;
            humidityStruct.extra2 = mTestConfiguration.HumidityDetects.get(0).ExtraParameters.get(1).extraParameterValue;
            humidityStruct.extra3 = mTestConfiguration.HumidityDetects.get(0).ExtraParameters.get(2).extraParameterValue;
            humidityStruct.extra4 = mTestConfiguration.HumidityDetects.get(0).ExtraParameters.get(3).extraParameterValue;
            humidityStruct.extra5 = mTestConfiguration.HumidityDetects.get(0).ExtraParameters.get(4).extraParameterValue;
            humidityStruct.extra6 = mTestConfiguration.HumidityDetects.get(0).ExtraParameters.get(5).extraParameterValue;
            humidityStruct.extra7 = mTestConfiguration.HumidityDetects.get(0).ExtraParameters.get(6).extraParameterValue;
            humidityStruct.extra8 = mTestConfiguration.HumidityDetects.get(0).ExtraParameters.get(7).extraParameterValue;
            humidityStruct.extra9 = mTestConfiguration.HumidityDetects.get(0).ExtraParameters.get(8).extraParameterValue;
            humidityStruct.extra10 = mTestConfiguration.HumidityDetects.get(0).ExtraParameters.get(9).extraParameterValue;
            mRealTimeQC.humidityConfig.add(humidityStruct);
        }
    }

    private void prepareTestSequenceObject() {
        if (mTestConfiguration.ReaderConfigSetting.SamplingInfo.SamplingFrequency != 0)
            mDPMTimeIncrement = mTimeIncrement = 1.0f / (float) mTestConfiguration.ReaderConfigSetting.SamplingInfo.SamplingFrequency;
        else
            mDPMTimeIncrement = mTimeIncrement = 0.2f;

        if (mTestReadings != null) {
            for (int jcount = 0; jcount < mTestReadings.size(); jcount++) {
                for (int icount = 0; icount < mTestReadings.get(jcount).readings.size(); icount++) {
                    mTestReadings.get(jcount).readings.set(icount, null);
                }
                mTestReadings.get(jcount).readings.clear();
                mTestReadings.get(jcount).readings = null;
                mTestReadings.set(jcount, null);
            }
            mTestReadings.clear();
            mTestReadings = null;
        }
        mTestReadings = new ArrayList<>();
        if (mTestSequence != null) {
            for (int icount = 0; icount < mTestSequence.size(); icount++) {
                mTestSequence.set(icount, null);
            }
            mTestSequence.clear();
            mTestSequence = null;
        }
        mTestSequence = new ArrayList<>();
        mTestConfiguration.getTestSequence(mSensorLayout, mTestSequence, mBarcodeInformation.getCardMade());

        mSensorDescriptors = new ArrayList<>();
        List channelMapping = new ArrayList();
        mTestConfiguration.getSensorInfo(mSensorLayout, mSensorDescriptors, channelMapping, mBarcodeInformation.getCardMade());

        List<AnalyteOption> testDefaults = getTestDefaultsForOneCardType();
        // take above list and cross section it with the defaultinclusions.
        updateDefaultTestInclusions(testDefaults);

        // copy test sequence
        for (int i = 0; i < mTestSequence.size(); i++) {
            ChannelType channelType = isConductivityChannel(mTestSequence.get(i).ChannelType);

            if (channelType == ChannelType.Conductivity) {
                mNumConductivity++;
            }

            int j;
            for (j = 0; j < mTestReadings.size(); j++) {
                // if its the same sensortype and descriptor number as another type of reading.. just add 1
                // to the num of this type
                if ((mTestReadings.get(j).channelType == channelType) &&
                        (mTestSequence.get(i).SensorType) != 0) {
                    mTestReadings.get(j).numThisTypeReading++;
                    mTestSequence.get(i).AddToWhichReading = j;
                    break;
                }
            }

            // if we hit the end and didnt find.. create a new sensorreading object
            if (j == mTestReadings.size()) {
                SensorReadings newReading = new SensorReadings();
                newReading.numThisTypeReading = 1;
                newReading.channelType = channelType;
                newReading.sensorType = Sensors.convert(mTestSequence.get(i).SensorType);
                newReading.analyte = TestDataFormatConvert.SensorToAnalyte(newReading.sensorType);
                newReading.analyteString = newReading.analyte.toString();
                newReading.sensorDescriptorNumber = mTestSequence.get(i).SensorDescriptorNumber;
                newReading.checkRealtimeQC = false;

                for (int k = 0; k < testDefaults.size(); k++) {
                    AnalyteName an = TestDataFormatConvert.AnalytesUreatoBUNInternalSwitch(testDefaults.get(k).getAnalyteName());
                    if ((newReading.analyte == an) &&
                            (testDefaults.get(k).getOptionType() == EnabledSelectedOptionType.EnabledSelected)) {
                        newReading.checkRealtimeQC = true;
                        break;
                    }
                }

                if ((channelType.value >= ChannelType.P1.value) &&
                        (channelType.value <= ChannelType.P7.value)) {
                    newReading.multiplicationFactor = 1000;
                } else if (((channelType.value >= ChannelType.A1.value) &&
                        (channelType.value <= ChannelType.A4.value)) ||
                        ((channelType.value >= ChannelType.A2_STIP.value) &&
                                (channelType.value <= ChannelType.A4_STIM.value)) ||
                        ((channelType.value >= ChannelType.A1ST0.value) &&
                                (channelType.value <= ChannelType.A4Sti.value)) ||
                        ((channelType.value >= ChannelType.CH_2_A2.value) &&
                                (channelType.value <= ChannelType.CH_2_A4.value))) {
                    newReading.multiplicationFactor = Math.pow(10, 9);
                }

                for (int k = 0; k < mSensorDescriptors.size(); k++) {
                    if ((newReading.sensorType == mSensorDescriptors.get(k).sensorType) &&
                            (newReading.sensorDescriptorNumber == mSensorDescriptors.get(k).sensorDescriptorNumber)) {
                        // save the sensor descriptor
                        newReading.sensorDescriptor = mSensorDescriptors.get(k);
                        break;
                    }
                }

                mTestReadings.add(newReading);

                // save the location of hematocrit and top heater readings
                if (newReading.sensorType == Sensors.Conductivity) {
                    mHematocritReadings = mTestReadings.size() - 1;
                } else if (newReading.sensorType == Sensors.HeaterTop) {
                    mTopHeaterReadings = mTestReadings.size() - 1;
                }
                mTestSequence.get(i).AddToWhichReading = j;
            }
        }

        // calculate what the conductivity will be if it was all fluid
        mAllFluidConductivityByte = (byte) ((1 << mNumConductivity) - 1);
    }

    // this method removes all analytes from defaultTestInclusions that are not supported by the card.
    // we might end up in the defaultTestInclusions with less analytes than supported on the card!
    // we might end up with no tests at all!

    private void updateDefaultTestInclusions(List<AnalyteOption> testDefaults) {
        ArrayList<AnalyteOption> crosssection = new ArrayList<>();
        for (AnalyteOption daop : mDefaultTestSelectionInclusions) {

            for (AnalyteOption aop : testDefaults) {
                if (daop.getAnalyteName().equals(aop.getAnalyteName())) {
                    crosssection.add(daop);
                    break;
                }
            }

        }
        mDefaultTestSelectionInclusions = crosssection;

        EventBus.getDefault().post(new TestDataProcessorCallback(TestDataProcessorEventType.TESTINCLUSIONS_UPDATED));
    }

    public void parseDataPacket(TestStateDataObject stateDataObject, TestState state, TestActionData tempPacket) {
        mBGETestPacketsReceived++;
        if (state.getStateID() == TestStateEnum.PerformFluidCalibration ||
                state.getStateID() == TestStateEnum.PerformSampleIntroduction ||
                state.getStateID() == TestStateEnum.PerformSampleProcessing) {
            if (mBGETestDataFile == null) {
                createBGETestDataFile();
            }
            mBGETestDataFile.write("[BGE]" + "\t" + mLastRecordedTime + "\t" + tempPacket.getSampleString() + "\r\n");
        }

        if (tempPacket.getDAMStage() == DAMStageType.FluidicsCalibration) {
            mCalpackets++;
            mLastRecordedTime = DecimalConversionUtil.round(mLastRecordedTime + mTimeIncrement, 1);
            //state.RaiseEvent(stateDataObject, TestStateMachineEventReason.CalibrationPacket, null);
        } else if (tempPacket.getDAMStage() == DAMStageType.BubbleDetectFluid || tempPacket.getDAMStage() == DAMStageType.BubbleDetectAir) {
            mBubblepackets++;
            mLastRecordedTime = DecimalConversionUtil.round(mLastRecordedTime + mTimeIncrement, 1);
            //if ((mBubblepackets % (configMsg.sampleFrequency * 10)) == 0)
            //{
            //state.RaiseEvent(stateDataObject, TestStateMachineEventReason.BubblePacket, configMsg.sampleFrequency);
            //}
        } else if (tempPacket.getDAMStage() == DAMStageType.SampleCollection) {
            mSamplepackets++;
            mLastRecordedTime = DecimalConversionUtil.round(mLastRecordedTime + mTimeIncrement, 1);
            //state.RaiseEvent(stateDataObject, TestStateMachineEventReason.SamplePacket, configMsg.sampleFrequency);
        }

        int numberOfConductivity = 0;

        // for every sample in the sequence
        for (int i = 0; i < mTestSequence.size(); i++) {
            // get the sensor readings object for this part of the sequence
            SensorReadings tempSR = mTestReadings.get((mTestSequence.get(i)).AddToWhichReading);
            float val = Float.MIN_VALUE;
            for (int j = 0; j < tempPacket.getSampledSignalsValues().getCount(); j++) {
                ChannelType channelType = isConductivityChannel(tempPacket.getSampledSignalsValues().getPairList().get(j).getChannelType());
                if (channelType.value == mTestSequence.get(i).ChannelType) {
                    val = tempPacket.getSampledSignalsValues().getPairList().get(j).getValue();
                    break;
                }
            }
            if (val == Float.MIN_VALUE) {
                continue;
            }
            if (tempSR.sensorType.value == Sensors.Conductivity.value) {
                numberOfConductivity++;

                // if just 1 conductivity, put it at the regular time
                if (tempSR.numThisTypeReading == 1) {
                    Reading newReading = new Reading();
                    newReading.time = mLastRecordedTime;
                    newReading.value = (val);
                    tempSR.readings.add(newReading);
                } else {
                    // more than 1 conductivity. go back 1 timeframe and space them out
                    Reading newReading = new Reading();
                    newReading.time = ((mLastRecordedTime - mTimeIncrement + ((mTimeIncrement / tempSR.numThisTypeReading) * numberOfConductivity)));
                    newReading.value = (val);
                    tempSR.readings.add(newReading);
                }
            } else {
                // if there are more than 1 of this kind of sensor reading.. and a reading has already been added
                // for this time.. then just add to it
                if ((tempSR.numThisTypeReading > 1) && (tempSR.readings.size() > 0) && ((tempSR.readings.get(tempSR.readings.size() - 1)).time == mLastRecordedTime)) {
                    // for now just add.. we'll divide later
                    (tempSR.readings.get(tempSR.readings.size() - 1)).value = ((tempSR.readings.get(tempSR.readings.size() - 1)).value + val);
                } else {
                    // otherwise.. its the first of many.. or its just 1
                    Reading newReading = new Reading();
                    newReading.time = (mLastRecordedTime);
                    newReading.value = (val);
                    tempSR.readings.add(newReading);
                }
            }
        }

        // if more than one type of this and its not conductivity, get average
        for (int i = 0; i < mTestReadings.size(); i++) {
            if (((mTestReadings.get(i)).numThisTypeReading > 1) && ((mTestReadings.get(i)).sensorType.value != Sensors.Conductivity.value)) {
                float temp = (((mTestReadings.get(i)).readings.get((mTestReadings.get(i)).readings.size() - 1))).value;
                (((mTestReadings.get(i)).readings.get((mTestReadings.get(i)).readings.size() - 1))).value = (temp / (mTestReadings.get(i)).numThisTypeReading);
            }
        }
    }

    public void parseDataPacket(TestStateDataObject stateDataObject, TestState state, DPMActionData tempPacket) {
        try {
            mDPMTestPacketsReceived++;
            // only log packets during the test
            if (state.getStateID() == TestStateEnum.PerformFluidCalibration ||
                    state.getStateID() == TestStateEnum.PerformSampleIntroduction ||
                    state.getStateID() == TestStateEnum.PerformSampleProcessing) {
                if (mDPMTestDataFile == null) {
                    createDPMTestDataFile();
                }
                mDPMTestDataFile.write("[DPM]" + "\t" + mLastRecordedTimePeripheral + "\t" + tempPacket.getSampleString() + "\r\n");
            }

            if (tempPacket.getReaderStateType() == ReaderStateType.Calibration) {
                mDPMCalpackets++;
                mLastRecordedTimePeripheral = DecimalConversionUtil.round(mLastRecordedTimePeripheral + mDPMTimeIncrement, 1);
                //state.RaiseEvent(stateDataObject, TestStateMachineEventReason.CalibrationPacket, null);
            } else if (tempPacket.getReaderStateType() == ReaderStateType.BubbleDetect) {
                mDPMBubblepackets++;
                mLastRecordedTimePeripheral = DecimalConversionUtil.round(mLastRecordedTimePeripheral + mDPMTimeIncrement, 1);
                //if ((mBubblepackets % (configMsg.sampleFrequency * 10)) == 0)
                //{
                //state.RaiseEvent(stateDataObject, TestStateMachineEventReason.BubblePacket, configMsg.sampleFrequency);
                //}
            } else if (tempPacket.getReaderStateType() == ReaderStateType.SampleCollection) {
                mDPMSamplepackets++;
                mLastRecordedTimePeripheral = DecimalConversionUtil.round(mLastRecordedTimePeripheral + mDPMTimeIncrement, 1);
                //state.RaiseEvent(stateDataObject, TestStateMachineEventReason.SamplePacket, configMsg.sampleFrequency);
            }

            float topHeaterT1 = Float.MIN_VALUE;
            float ambientTemp = Float.MIN_VALUE;
            float barometricPressure = Float.MIN_VALUE;
            for (int j = 0; j < tempPacket.getIndicatorValues().getCount(); j++) {
                if (tempPacket.getIndicatorValues().getPairList().get(j).getChannelType() == PMIIndicators.BGTopHeaterT1.value) {
                    topHeaterT1 = tempPacket.getIndicatorValues().getPairList().get(j).getValue();
                    break;
                }
            }
            for (int j = 0; j < tempPacket.getIndicatorValues().getCount(); j++) {
                if (tempPacket.getIndicatorValues().getPairList().get(j).getChannelType() == PMIIndicators.AmbientT1.value) {
                    ambientTemp = tempPacket.getIndicatorValues().getPairList().get(j).getValue();
                    break;
                }
            }
            for (int j = 0; j < tempPacket.getIndicatorValues().getCount(); j++) {
                if (tempPacket.getIndicatorValues().getPairList().get(j).getChannelType() == PMIIndicators.Pressure1.value) {
                    barometricPressure = tempPacket.getIndicatorValues().getPairList().get(j).getValue();
                    break;
                }
            }
            if (topHeaterT1 == Float.MIN_VALUE || ambientTemp == Float.MIN_VALUE || barometricPressure == Float.MIN_VALUE) {
                return;
            }

            // add the top heater pid
            Reading tempReading = new Reading();
            tempReading.time = (mLastRecordedTimePeripheral);
            tempReading.value = (topHeaterT1);
            mTopHeaterPid.add(tempReading);
            mAmbientTemperature = ambientTemp;
            mBarometricPressureSensor = barometricPressure;
        } catch (Exception ex) {
            if (Log.isLoggable("TAG", android.util.Log.DEBUG)) {
                Log.d("TAG", ex.getMessage());
            }
        }
    }

    public boolean doBubbleDetectDuringSampleIntroduction(TestStateDataObject stateDataObject, TstActRspBGETest bge) {
        boolean bubbleBeginSetThisPacket = false;

        // no bubble yet
        if (mBubbleDetect == 0) {
            if ((bge.getData().getDAMStage() == DAMStageType.BubbleDetectAir) || (bge.getData().getDAMStage() == DAMStageType.SampleCollection)) {
                mBubbleDetect = DecimalConversionUtil.round(mLastRecordedTime - mTimeIncrement, 1);

                if (bge.getData().getBDFlags() == mAllFluidConductivityByte) {
                    // this shouldnt happen, but somehow the DAM found a
                    // bubble, but none of the bits in the conductivity
                    // byte were air. so we will pretend the bubble began
                    // at the beginning of the packet
                    mBubbleBegin = mLastRecordedTime - mTimeIncrement;
                } else {
                    byte tempByte = bge.getData().getBDFlags();
                    int i;

                    for (i = 0; i < mNumConductivity; i++) {
                        // if we keep getting 0s, just keep overwriting the
                        // bubblebegin, becuase we want the last one
                        // unless we've found the end of the bubble, in
                        // which case dont overwrite the old bubble
                        if ((mBubbleBegin == 0) && ((tempByte & (0x01 << i)) == 0)) {
                            mBubbleBegin = mLastRecordedTime - mTimeIncrement + (mTimeIncrement / mNumConductivity) * (i + 1);
                            bubbleBeginSetThisPacket = true;
                        }

                        // if bubble found, Start looking for fluid again
                        if ((mBubbleEnd == 0) && (mBubbleBegin != 0) && ((tempByte & (0x01 << i)) != 0)) {
                            // after a 0, we found a 1 set the bubbleend too
                            mBubbleEnd = mLastRecordedTime - mTimeIncrement + (mTimeIncrement / mNumConductivity) * (i + 1);
                        }
                    }
                }
            }
        }
        return bubbleBeginSetThisPacket;
    }

    public boolean doSamplingDetectDuringSampleIntroduction(TestStateDataObject stateDataObject, TstActRspBGETest bge, boolean bubbleBeginSetThisPacket) {
        // once air has been found.. we need to look for fluid.. even
        // if its in the same byte.. only go into
        if ((!bubbleBeginSetThisPacket) && (mBubbleBegin != 0) && (mBubbleEnd == 0) && (bge.getData().getBDFlags() != 0)) {
            byte tempByte = bge.getData().getBDFlags();
            int i;

            // we Start at 1 because we want the leftmost 0. so we Start at
            // position 1. if its a 0,
            for (i = 0; i < mNumConductivity; i++) {
                // go until we find a 1 or we shift over 4 times
                if ((tempByte & (0x01 << i)) != 0) {
                    mBubbleEnd = mLastRecordedTime - mTimeIncrement + (mTimeIncrement / mNumConductivity) * (i + 1);
                    break;
                }
            }
        }

        boolean gotoProcessing = false;
        if (bge.getData().getDAMStage() == DAMStageType.SampleCollection) {
            if (mBubbleDetect == 0) {
                mBubbleDetect = DecimalConversionUtil.round(mLastRecordedTime - mTimeIncrement, 1);
            }

            if (mBubbleEnd == 0) {
                mBubbleEnd = mLastRecordedTime;
            }
            //RaiseEvent(stateDataObject, epoc.common.types.TestStateMachineEventReason.EndsampleIntroduction, null);

            mSampleDetect = DecimalConversionUtil.round(mLastRecordedTime + mTimeIncrement, 1);
            mOneTimeFlag = false;
            gotoProcessing = true;
            // this is the correct way to do this, because this is what the data manager will store
            //TabPage.timeInfo.Text = testTime.AddSeconds(Math.Round(sampleDetect, 0)).ToString("HH:mm:ss");
        }

        if ((mBubbleBegin != 0) && (mBubbleEnd != 0)) {
            if ((DecimalConversionUtil.round(mBubbleEnd - mBubbleBegin, 2)
                    < mShortestGoodInjection) || (DecimalConversionUtil.round(mBubbleEnd - mBubbleBegin, 2) > mLongestGoodInjection)) {
                {
                    if (DecimalConversionUtil.round(mBubbleEnd - mBubbleBegin, 2) < mShortestGoodInjection) {
                        setErrorCode(HostErrorCode.SampleInjectedTooFast);
                    } else {
                        setErrorCode(HostErrorCode.SampleInjectedTooSlowly);
                    }
                    mOneTimeFlag = false;
                    //stateDataObject.EnableResponseTimer();
                    gotoProcessing = true;
                }
            }
        }
        return gotoProcessing;
    }

    //calledn from PerformFluidCalibration when moving to SampleIndtroduction state
    public void endFluidCalibration() {
        mCalibrationTime = mLastRecordedTime;
    }

    private void createBGETestDataFile() {
        //mBGETestDataFile = new FileSystemUtil("BGE_P_" + mTimeSpecificString, FileSystemUtil.FileLocation.External, mDateSpecificString + "//" + this.mTestRecord.getReader().getSerialNumber());
        mBGETestDataFile = new FileSystemUtil("P_" + mTimeSpecificString, FileSystemUtil.FileLocation.External, mDateSpecificString + "//" + this.mTestRecord.getReader().getSerialNumber());
    }

    private void createDPMTestDataFile() {
        mDPMTestDataFile = new FileSystemUtil("DPM_P_" + mTimeSpecificString, FileSystemUtil.FileLocation.External, mDateSpecificString + "//" + this.mTestRecord.getReader().getSerialNumber());
    }

    private void createmTestPacketFile() {
        mTestPacketFile = new FileSystemUtil("TestPacket_" + mTimeSpecificString, FileSystemUtil.FileLocation.External, mDateSpecificString + "//" + this.mTestRecord.getReader().getSerialNumber());
    }

    private String getTimeSpecificString() {
        return
                Integer.toString(this.mConnectionTime.get(Calendar.YEAR)) +
                        (this.mConnectionTime.get(Calendar.MONTH) < 10 ? "0" + this.mConnectionTime.get(Calendar.MONTH) : this.mConnectionTime.get(Calendar.MONTH)) +
                        (this.mConnectionTime.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + this.mConnectionTime.get(Calendar.DAY_OF_MONTH) : this.mConnectionTime.get(Calendar.DAY_OF_MONTH)) + "_" +
                        (this.mConnectionTime.get(Calendar.HOUR) < 10 ? "0" + this.mConnectionTime.get(Calendar.HOUR) : this.mConnectionTime.get(Calendar.HOUR)) +
                        (this.mConnectionTime.get(Calendar.MINUTE) < 10 ? "0" + this.mConnectionTime.get(Calendar.MINUTE) : this.mConnectionTime.get(Calendar.MINUTE)) +
                        (this.mConnectionTime.get(Calendar.SECOND) < 10 ? "0" + this.mConnectionTime.get(Calendar.SECOND) : this.mConnectionTime.get(Calendar.SECOND));
    }

    private String getDateSpecificString() {
        return
                Integer.toString(this.mConnectionTime.get(Calendar.YEAR)) +
                        (this.mConnectionTime.get(Calendar.MONTH) < 10 ? "0" + this.mConnectionTime.get(Calendar.MONTH) : this.mConnectionTime.get(Calendar.MONTH)) +
                        (this.mConnectionTime.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + this.mConnectionTime.get(Calendar.DAY_OF_MONTH) : this.mConnectionTime.get(Calendar.DAY_OF_MONTH));
    }

    private ChannelType isConductivityChannel(byte channelType) {
        if (channelType == ChannelType.Conductivity.value || channelType == ChannelType.CONDUCTIVITY_SETTLING.value || channelType == ChannelType.CH_2_CONDUCTIVITY.value) {
            return ChannelType.Conductivity;
        }
        return ChannelType.convert(channelType);
    }

    public int getSamplingDelayDuration() {
        int delayTime;
        if ((mErrorCode == HostErrorCode.SampleInjectedTooSlowly) || (mErrorCode == HostErrorCode.SampleInjectedTooFast)) {
            delayTime = 2000;
        } else {
            double bubbleWidth = (mBubbleEnd - mBubbleBegin);
            if (bubbleWidth > 0.5) {
                delayTime = (int) (bubbleWidth * 0.35 * 1000);
            } else {
                delayTime = (int) (bubbleWidth * 0.1 * 1000);
            }
        }
        return delayTime;
    }

    public void checkSampleInjectError(TestStateDataObject stateDataObject) {
        if ((mErrorCode == HostErrorCode.SampleInjectedTooSlowly) || (mErrorCode == HostErrorCode.SampleInjectedTooFast)) {
            if (mErrorCode == HostErrorCode.SampleInjectedTooSlowly) {
                if (!checkConductivityFluidAfterFluidThreshold(stateDataObject)) {
                    //NONE of those points went over fluidafterfluid threshold (in config1_4) then report sample injected too quickly
                    mErrorCode = HostErrorCode.SampleInjectedTooFast;

                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.SAMPLEINJECTEDTOOFAST);
                    stateDataObject.postEvent(eventInfo);
                } else {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.SAMPLEINJECTEDTOOSLOWLY);
                    stateDataObject.postEvent(eventInfo);
                }
            } else {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.SAMPLEINJECTEDTOOFAST);
                stateDataObject.postEvent(eventInfo);
            }
        } else {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.SAMPLEPROCESSING);
            eventInfo.setTestEventData(100);
            stateDataObject.postEvent(eventInfo);
        }
    }

    private boolean checkConductivityFluidAfterFluidThreshold(TestStateDataObject stateDataObject) {
        SensorReadings tempSR = mTestReadings.get(mHematocritReadings);
        int maxNum = tempSR.readings.size() > 120 ? tempSR.readings.size() - 120 : 0;
        for (int icount = tempSR.readings.size() - 1; icount >= maxNum; icount--) {
            if (tempSR.readings.get(icount).value > mTestConfiguration.ReaderConfigSetting.FluidAfterFluidThreshold) {
                return true;
            }
        }
        return false;
    }

    // call only once, when cal fluid is broken
    // this will create the testrecord in Realm
    public void persistCreateData() {
        mTestRecord.setTestDateTime(new Date());
        if (mTestRecord.getId() == -1) {
            new TestRecordModel().createTestRecordFromUnmanaged(mTestRecord);
        }

    }

    public void saveTestData() {

        if (mTestRecord.getId() != -1) {
            new TestRecordModel().copyOrUpdate(mTestRecord);
        }
    }

    // eventually this will be only called from 'this', and will be privatized
    private void saveTestResults(RealmList<com.epocal.common.realmentities.TestResult> testResults) {

        mTestRecord.setTestResults(testResults);
        for (com.epocal.common.realmentities.TestResult tr : mTestRecord.getTestResults()) {
            tr.setTestRecordId(mTestRecord.getId());
        }
        // CUD testresults in Realm
        new TestRecordModel().persistTestResults(mTestRecord);

    }


    // insert or update custom test variable
    //
    public void saveCustomTestVariable(String customFieldName, String value) {
        // does it already exist?
        if (mTestRecord.getTestDetail().getCustomTestVariables() == null) {
            mTestRecord.getTestDetail().setCustomTestVariables(new RealmList<CustomTestVariable>());
        }
        if (!foundAndUpdatedCustomVariable(customFieldName, value)) {


            CustomTestVariable newVariable = new CustomTestVariable();
            // todo, get these, (or create the new object) from Workflow

//            newVariable.setDisplayOrder(customField.getDisplayOrder());
//            newVariable.setEpocTestFieldOptionType(customField.getEpocTestFieldOptionType());
//            newVariable.setFieldGroupType(customField.getFieldGroupType());
            newVariable.setName(customFieldName);
            newVariable.setTestRecordId(mTestRecord.getId());
            newVariable.setValue(value);
            mTestRecord.getTestDetail().getCustomTestVariables().add(newVariable);
        }
        // finally, if testrecord it's in realm, then insertOrUpdate there as well
        if (mTestRecord.getId() > 0) {
            new TestRecordModel().insertOrUpdateCustomVariable(mTestRecord, customFieldName);
        }

    }

    private boolean foundAndUpdatedCustomVariable(String name, final String value) {
        boolean retval = false;
        for (CustomTestVariable ctv : mTestRecord.getTestDetail().getCustomTestVariables()) {
            if (ctv.getName().equals(name)) {
                retval = true;
                ctv.setValue(value);
                break;
            }
        }
        return retval;
    }

    public void calculateResults(final TestStateDataObject testStateDataObject) {
        if (mPerformTestCalculation == null) {
            mPerformTestCalculation = new PerformTestCalculation(testStateDataObject);
            mPerformTestCalculation.getRxUtilStatus().subscribe(new Consumer<TestRecord>() {
                @Override
                public void accept(TestRecord testRecord) throws Exception {
                    fireEventCenter(testStateDataObject, testRecord);
                }
            });
        }
        mPerformTestCalculation.startCalculation();

    }

    private void fireEventCenter(TestStateDataObject testStateDataObject, TestRecord testRecord) {
        TestEventInfo eventInfo = new TestEventInfo();
        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
        eventInfo.setTestStatusType(TestStatusType.TESTCALCULATED);
        eventInfo.setTestEventData(testRecord);
        testStateDataObject.postEvent(eventInfo);
    }

    public void prepareDataBeforeCalculation() {
        // bm: this is already taken care of at this point
//        if (getRuntimeTestSelectionInclusions().size() == 0 && getDefaultTestSelectionInclusions().size() > 0) {
//            setRuntimeTestSelectionInclusions(mDefaultTestSelectionInclusions);
//        }
        addSensorCheck();
        getAllDefaultReportableRanges();
        if (!mTestFirstTimeSaved) {
            for (int i = 0; i < mTopHeaterPid.size(); i++) {
                // convert pid into %age power according to following equation from JH : (2.5 - PID)/.025=%pwr
                mTopHeaterPid.get(i).value = (float) (2.5 - (mTopHeaterPid.get(i).value) / 0.025);
            }
//            if (mTestRecord == null) {
//                mTestRecord = new TestRecord();
//            }
        }
    }

    public void prepareResultAfterCalculation(TestStateDataObject stateDataObject) {
        TestRawResultCollection testRawResultCollection = new TestRawResultCollection();
        RefWrappers<TestRawResultCollection> refW = new RefWrappers<>(testRawResultCollection);
        preHandleTestParameter();

        if (mErrorCode == HostErrorCode.SampleFailedQC) {
            try {
                saveTestResult(refW);
                mTestFirstTimeSaved = true;
            } catch (Exception ex) {
                LogServer.getInstance().devlog("TestDataProcessor.prepareResultAfterCalculation: " + ex.getMessage());
            }
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.SAMPLEDELIVERY);
            stateDataObject.postEvent(eventInfo);

            stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.TestFinishedWithFailed);
            return;
        } else if ((mErrorCode == HostErrorCode.NoError) ||
                (mErrorCode == HostErrorCode.ExpiredCard) ||
                (mErrorCode == HostErrorCode.NoLotNumber) ||
                (mErrorCode == HostErrorCode.NoPatientId) ||
                (mErrorCode == HostErrorCode.NoSampleType) ||
                (mErrorCode == HostErrorCode.NoApplyHemodilution) ||
                (mErrorCode == HostErrorCode.NoTestsSelected)) {
            if (!mTestFirstTimeSaved) {
                mNumSuccessfulTests++;
            }
            if (getRuntimeTestSelectionInclusions().size() == 0) {
                if ((mErrorCode == HostErrorCode.NoError) || (mErrorCode == HostErrorCode.ExpiredCard)) {
                    mErrorCode = HostErrorCode.NoTestsSelected;
                }
            }
            if ((mTestRecord.getTestMode() == TestMode.BloodTest) && (mTestRecord.getTestDetail().getSampleType() == BloodSampleType.Unspecified)) {
                if ((mErrorCode == HostErrorCode.NoError) || (mErrorCode == HostErrorCode.ExpiredCard)) {
                    mErrorCode = HostErrorCode.NoSampleType;
                }
            }
            // check hemodilution value
            if ((mTestRecord.getTestMode() == TestMode.BloodTest) && (mTestRecord.getTestDetail().getHemodilutionApplied() == null)) {
                if ((mErrorCode == HostErrorCode.NoError) || (mErrorCode == HostErrorCode.ExpiredCard)) {
                    // patient test mode.. there needs to be a hemodilution selection.. and yet both yes and
                    // no buttons were unchecked
                    mErrorCode = HostErrorCode.NoApplyHemodilution;

                }
            }

            //// TODO: 11/27/2017 hardcode mErrorCode = HostErrorCode.NoError for phase 1 SQA validation
            mErrorCode = HostErrorCode.NoError;

            mExistsFailedIQC = getTestResult(refW);
            if ((mErrorCode != HostErrorCode.SampleFailedQC) &&
                    (
                            (mErrorCode == HostErrorCode.NoError) || (mErrorCode == HostErrorCode.ExpiredCard) ||
                                    (mErrorCode == HostErrorCode.NoPatientId) || (mErrorCode == HostErrorCode.NoSampleType) ||
                                    (mErrorCode == HostErrorCode.NoApplyHemodilution) || (mErrorCode == HostErrorCode.NoTestsSelected)
                    )
                    ) {
                getReferenceRange(stateDataObject, refW);
                saveTestResult(refW);
            }

            // if this is the first results calculation, dont switch in the middle of data entry
            if (!mTestFirstTimeSaved) {
                //test result is calculated. can swtich UI to show result list.
                mTestFirstTimeSaved = true;
            }
        } else {
            // if we failed during the test and never got to the end.. don't bother doing the switchover.
            try {
                saveTestResult(refW);
                mTestFirstTimeSaved = true;
            } catch (Exception ex) {
                LogServer.getInstance().devlog("TestDataProcessor.prepareResultAfterCalculation2: " + ex.getMessage());
            }
        }
        mOldTestCalculated = true;
    }

    private void preHandleTestParameter() {
        if (mTestRecord.getTestDetail().getPatientTemperatureType() == Temperatures.C) {
            // c temperatures
            if ((mTestRecord.getTestDetail().getPatientTemperature() == null) || (mTestRecord.getTestDetail().getPatientTemperature() > 44) || (mTestRecord.getTestDetail().getPatientTemperature() < 10)) {
                mTestRecord.getTestDetail().setPatientTemperature(null);
            }
        } else {
            // f temperatures
            if ((mTestRecord.getTestDetail().getPatientTemperature() == null) || (mTestRecord.getTestDetail().getPatientTemperature() > 111) || (mTestRecord.getTestDetail().getPatientTemperature() < 50)) {
                mTestRecord.getTestDetail().setPatientTemperature(null);
            }
        }
        // check fio2
        double fio2Lev;
        try {
            fio2Lev = Double.parseDouble(mTestRecord.getRespiratoryDetail().getFiO2());
        } catch (Exception e) {
            fio2Lev = Double.NaN;
        }

        if ((fio2Lev < 21) || (fio2Lev > 100)) {
            fio2Lev = Double.NaN;
        }
        mTestRecord.getRespiratoryDetail().setFiO2(Double.toString(fio2Lev));
        // check RQ
        double rqVal;
        try {
            rqVal = Double.parseDouble(mTestRecord.getRespiratoryDetail().getRq());
        } catch (Exception e) {
            rqVal = Double.NaN;
        }

        if ((0.01 > rqVal) || (2.00 < rqVal)) {
            // RQ is not within acceptable range: 0.01-2.00
            rqVal = Double.NaN;
        }
        mTestRecord.getRespiratoryDetail().setRq(Double.toString(rqVal));
        // check flow
        double flowLevel;
        try {
            flowLevel = Double.parseDouble(mTestRecord.getRespiratoryDetail().getFlow());
        } catch (Exception e) {
            flowLevel = Double.NaN;
        }
        if ((flowLevel < 0) || (flowLevel > 15)) {
            flowLevel = Double.NaN;
        }
        mTestRecord.getRespiratoryDetail().setFlow(Double.toString(flowLevel));
    }

    private boolean saveTestResult(RefWrappers<TestRawResultCollection> testRawResultCollectionWrapper) {
        try {
            getTestInfo(testRawResultCollectionWrapper);
            try {
                if (!Double.isNaN(mLastRecordedTime)) {
                    mTestRecord.getTestDetail().setDuration(Double.parseDouble(Float.toString(mLastRecordedTime)));
                }

            } catch (Exception ex) {
                LogServer.getInstance().devlog("TestDataProcessor.saveTestResult: " + ex.getMessage());
            }
        } catch (Exception ex) {
            LogServer.getInstance().devlog("TestDataProcessor.saveTestResult: " + ex.getMessage());
        }
        return true;
    }

    // sets test info (ambient P, T, status, errorcode etc) into TestRecord
    private void getTestInfo(RefWrappers<TestRawResultCollection> testRawResultCollectionWrapper) {
        double respVal;
        try {
            respVal = Double.parseDouble(mTestRecord.getRespiratoryDetail().getFiO2());
            mTestRecord.getRespiratoryDetail().setFiO2(Double.toString(respVal));
        } catch (Exception e) {
            mTestRecord.getRespiratoryDetail().setFiO2("");
        }

        try {
            respVal = Double.parseDouble(mTestRecord.getRespiratoryDetail().getRq());
            mTestRecord.getRespiratoryDetail().setRq(Double.toString(respVal));
        } catch (Exception e) {
            mTestRecord.getRespiratoryDetail().setRq("");
        }

        double flowLevel;
        try {
            respVal = Double.parseDouble(mTestRecord.getRespiratoryDetail().getFlow());
            mTestRecord.getRespiratoryDetail().setFlow(Double.toString(respVal));
        } catch (Exception e) {
            mTestRecord.getRespiratoryDetail().setFlow("");
        }

        NumberFormat formatter = new DecimalFormat("#0.0");
        //modified by ray zhuang for Legacy calculate
        float ambientTemp1;
        float barometricPressureSensor1;
        if (mDeviceStatusData == null) {
            ambientTemp1 = mRsrMsg.hwStatus.getAmbientTemp1();
            barometricPressureSensor1 = mRsrMsg.hwStatus.getBarometricPressureSensor1();
        } else {
            ambientTemp1 = mDeviceStatusData.getHardwareStatus().getAmbientTemp1();
            barometricPressureSensor1 = mDeviceStatusData.getHardwareStatus().getBarometricPressureSensor1();
        }
        if (!Double.isNaN(ambientTemp1)) {
            mTestRecord.getTestDetail().setAmbientTemperature(Double.parseDouble(formatter.format(ambientTemp1)));
            mTestRecord.getTestDetail().setAmbientTemperatureType((mTestRecord.getTestDetail().getPatientTemperatureType() == Temperatures.C ? Temperatures.C : Temperatures.F));
        }

        if (!Double.isNaN(barometricPressureSensor1)) {
            AnalyteModel analyteModel = new AnalyteModel();
            Analyte analyte = analyteModel.getAnalyte(AnalyteName.pCO2);

            mTestRecord.getTestDetail().setAmbientPressure(Double.parseDouble(formatter.format(barometricPressureSensor1)));
            mTestRecord.getTestDetail().setPressureType((analyte.getUnitType() == Units.mmhg) ? PressureType.mmHg : PressureType.kPa);
        }
        if (mSampleDetect != 0) {
            mTestTime.add(Calendar.SECOND, Math.round(Float.parseFloat(Double.toString(mSampleDetect))));
        }
        mTestRecord.setTestDateTime(mTestTime.getTime());

        if (mErrorCode == HostErrorCode.NoError) {
            // no error just means we got all the way to results calculation. so lets see if there
            // were qc failures. if so then its iqc, otherwise its success
            if ((testRawResultCollectionWrapper.getRef() != null) &&
                    (testRawResultCollectionWrapper.getRef().isCNCBecauseFailedIQC() ||
                            testRawResultCollectionWrapper.getRef().isFailedIQC())) {
                mTestRecord.setStatus(TestStatus.iQC);
            } else {
                mTestRecord.setStatus(TestStatus.Success);
            }
        } else if (mErrorCode == HostErrorCode.ExpiredCard) {
            // and only expired card is an expired card if it didnt fail one of the other ways
            mTestRecord.setStatus(TestStatus.Expired);
        } else {
            // everything else other than expired card is an incomplete test
            mTestRecord.setStatus(TestStatus.Incomplete);
        }
        mTestRecord.setTestErrorCode(TestDataFormatConvert.convertToErrorCode(mErrorCode));
    }

    private boolean getTestResult(RefWrappers<TestRawResultCollection> testRawResultCollectionWrapper) {
        boolean cncBecauseFailedIQC = false;
        boolean failedIQC = false;

        ArrayList<FinalResult> measuredResults = new ArrayList<>();
        ArrayList<FinalResult> correctedResults = new ArrayList<>();
        ArrayList<FinalResult> calculatedResults = new ArrayList<>();

        int i;
        for (i = 0; i < mTestReadings.size(); i++) {
            // only save values for 'real' sensors.. not heater, conductivity etc.
            if (mTestConfiguration.isMeasuredAnalyte(TestDataFormatConvert.SensorToAnalyte(mTestReadings.get(i).sensorType))) {
                FinalResult currentResult = new FinalResult();
                currentResult.channelType = mTestReadings.get(i).channelType;
                currentResult.analyte = TestDataFormatConvert.SensorToAnalyte(mTestReadings.get(i).sensorType);
                currentResult.reading = mTestReadings.get(i).result;
                currentResult.returnCode = mTestReadings.get(i).returnCode;
                currentResult.requirementsFailedIQC = mTestReadings.get(i).requirementsFailedQC;
                measuredResults.add(currentResult);
            }
        }

        // so now everything is in the measured.. but the ones that aren't selected and were successful
        // have a return code of TestNotSelected
        double fio2Value;
        double rqValue;
        boolean applyCalculateAlveolar;
        BloodSampleType sampleType = mTestRecord.getTestDetail().getSampleType();
        if (mTestRecord.getTestMode() == TestMode.QA) {
            fio2Value = 21;
            rqValue = 0.86;
            applyCalculateAlveolar = true;
        } else {
            try {
                fio2Value = Double.parseDouble(mTestRecord.getRespiratoryDetail().getFiO2());
            } catch (Exception e) {
                fio2Value = Double.NaN;
            }
            try {
                rqValue = Double.parseDouble(mTestRecord.getRespiratoryDetail().getRq());
            } catch (Exception e) {
                rqValue = 0.86;
            }

            // Determine if we should calculate Alveolar
            // Note: These calculations require the sample type selection of Arterial or Capillary. If these
            //       sample types are not selected, these parameters will not be displayed.
            // Note: If FiO2 is not entered, these parameters will not be displayed.
            applyCalculateAlveolar = (((sampleType == BloodSampleType.Arterial) || (sampleType == BloodSampleType.Capillary)) && (!Double.isNaN(fio2Value)));
        }

        double temperature, ambientPressure;
        if (mTestRecord.getTestDetail().getPatientTemperature() != null) {
            temperature = Double.isNaN(mTestRecord.getTestDetail().getPatientTemperature()) ? Double.NaN : mTestRecord.getTestDetail().getPatientTemperature().doubleValue();
        } else {
            temperature = Double.NaN;
        }

        if (mTestRecord.getTestDetail().getAmbientPressure() != null) {
            ambientPressure = Double.isNaN(mTestRecord.getTestDetail().getAmbientPressure()) ? Double.NaN : mTestRecord.getTestDetail().getAmbientPressure().doubleValue();
        } else {
            ambientPressure = Double.NaN;
        }

        //// TODO: 11/27/2017 hardcode for phase 1 SQA validation
        fio2Value = 21;
        rqValue = 0.86;
        applyCalculateAlveolar = true;
        ambientPressure = 8;
        if (mTestRecord.getTestDetail().getPatientTemperatureType() == Temperatures.C) {
            temperature = 36.5;
        } else {
            temperature = 97.7;
        }

        // / so now everything is in the measured.. but the ones that aren't selected and were successful
        // have a return code of TestNotSelected
        ComputeCorrectedResultsResponse responseCorr;
        if (mTestRecord.getTestDetail().getPatientTemperatureType() == Temperatures.C) {
            responseCorr = AnalyticalManager.computeCorrectedResults(measuredResults, correctedResults, temperature, ambientPressure, fio2Value, rqValue, applyCalculateAlveolar);
        } else {
            // have to convert from F to C
            responseCorr = AnalyticalManager.computeCorrectedResults(measuredResults, correctedResults, TestDataFormatConvert.FToC(temperature), ambientPressure, fio2Value, rqValue, applyCalculateAlveolar);
        }

        if (!responseCorr.isSuccess()) {
            //critical error occurred
            Log.d("ComputeCorrectedResults", "critical error occurred");
        } else {
            correctedResults = (ArrayList<FinalResult>) responseCorr.getCorrectedResults();
        }

        double patientAge = 0;
        double patientHeight = 0;
        AgeCategory ageCategory = AgeCategory.None;

        if (mTestRecord.getTestMode() == TestMode.BloodTest) {
            patientAge = mTestRecord.getPatientAge();
        } else {
            if (false)//(PDASettingsManager.IsJapanese)
            {
                patientAge = 22;
                ageCategory = AgeCategory.Adult;
            } else {
                patientAge = 18;
            }
        }

        Gender patientGender = Gender.Unknown;

        if (mTestRecord.getTestMode() == TestMode.BloodTest) {
            patientGender = mTestRecord.getGender();
        } else {
            patientGender = Gender.Male;
        }

        EGFRFormula egfrFormula = EGFRFormula.None;

        if (false)//(PDASettingsManager.IsJapanese)
        {
            egfrFormula = EGFRFormula.Japanese;
        } else {
            egfrFormula = EGFRFormula.MDRD;
        }
        boolean applymTCO2 = false;

        //// TODO: 11/27/2017 hardcode for phase 1 SQA validation
        patientAge = 29;
        patientHeight = 45;
        patientGender = Gender.Female;
        ageCategory = AgeCategory.Adult;

        //add by rzhuang for Legacy compatibility
        float barometricPressureSensor1;
        if (mDeviceStatusData == null) {
            barometricPressureSensor1 = mRsrMsg.hwStatus.getBarometricPressureSensor1();
        } else {
            barometricPressureSensor1 = mDeviceStatusData.getHardwareStatus().getBarometricPressureSensor1();
        }

        ComputeCalculatedResultsResponse responseCal = AnalyticalManager.computeCalculatedResults(
                measuredResults,
                calculatedResults,
                mCthbLevel,
                fio2Value,
                temperature,
                barometricPressureSensor1,
                mTestRecord.getTestMode(),
                patientAge,
                patientGender,
                egfrFormula,
                patientHeight,
                ageCategory,
                rqValue,
                applyCalculateAlveolar,
                applymTCO2);

        if (!responseCal.isSuccess()) {
            Log.d("CalculatedResults", "critical error occurred");
        } else {
            calculatedResults = (ArrayList<FinalResult>) responseCal.getCalculatedResults();
        }

        // take out everything that wasn't selected in the measured results
        for (i = measuredResults.size() - 1; i >= 0; i--) {
            if (!isIncludeAnalyte(((measuredResults.get(i))).analyte, mRuntimeTestSelectionInclusions)) {
                measuredResults.remove(i);
            } else {
                if (((measuredResults.get(i))).requirementsFailedIQC) {
                    cncBecauseFailedIQC = true;
                }
                if (AnalyticalManager.failedIQC(((measuredResults.get(i))).returnCode)) {
                    failedIQC = true;
                }
            }
        }

        // remove calculated results for which the inclusions are not in
        for (i = calculatedResults.size() - 1; i >= 0; i--) {
            if (!isIncludeCalculatedResult((calculatedResults.get(i)).analyte, mRuntimeTestSelectionInclusions)) {
                calculatedResults.remove(i);
            } else {
                (calculatedResults.get(i)).channelType = ChannelType.None;

                if ((calculatedResults.get(i)).requirementsFailedIQC) {
                    cncBecauseFailedIQC = true;
                }
            }
        }

        // remove corrected results for which the inclusions are not in
        for (i = correctedResults.size() - 1; i >= 0; i--) {
            if (!isIncludeCorrectedResult((correctedResults.get(i)).analyte, mRuntimeTestSelectionInclusions)) {
                correctedResults.remove(i);
            } else {
                (correctedResults.get(i)).channelType = ChannelType.None;

                if ((correctedResults.get(i)).requirementsFailedIQC) {
                    cncBecauseFailedIQC = true;
                }
            }
        }

        //for (i = 0; i < correctedResults.Count; i++)
        //{
        //    ((FinalResult)correctedResults[i]).channelType = (byte)ChannelType.None;
        //}
        TestRawResultCollection testRawResultCollection = new TestRawResultCollection();
        testRawResultCollection.setMeasuredResults(measuredResults);
        testRawResultCollection.setCorrectedResults(correctedResults);
        testRawResultCollection.setCalculatedResults(calculatedResults);

        testRawResultCollection.setCNCBecauseFailedIQC(cncBecauseFailedIQC);
        testRawResultCollection.setFailedIQC(failedIQC);
        testRawResultCollectionWrapper.setRef(testRawResultCollection);
        return failedIQC;
    }

    private RangeValue getRangeValuesForBlood(AnalyteName analyteName, BloodSampleType bloodSampleType, String subSampleType) {
        RangeModel referenceRangeModel = new RangeModel();
        Range referenceRange = referenceRangeModel.getUnmanagedRange(bloodSampleType, subSampleType);
        RangeValue rangeValue = referenceRangeModel.getUnmanagedRangeValue(referenceRange, analyteName);
        if (rangeValue.getIgnoreInfo().contains(RangeIgnoreInfo.IgnoreReferenceLow)) {
            rangeValue.setReferenceLow(Double.MIN_VALUE);
        } else {
            if (rangeValue.getReferenceLow() == null || rangeValue.getReferenceLow() == Double.NaN)
                rangeValue.setReferenceLow(Double.MIN_VALUE);
        }
        if (rangeValue.getIgnoreInfo().contains(RangeIgnoreInfo.IgnoreReferenceHigh)) {
            rangeValue.setReferenceHigh(Double.MAX_VALUE);
        } else {
            if (rangeValue.getReferenceHigh() == null || rangeValue.getReferenceHigh() == Double.NaN)
                rangeValue.setReferenceHigh(Double.MAX_VALUE);
        }
        if (rangeValue.getIgnoreInfo().contains(RangeIgnoreInfo.IgnoreCriticalLow)) {
            rangeValue.setCriticalLow(Double.MIN_VALUE);
        } else {
            if (rangeValue.getCriticalLow() == null || rangeValue.getCriticalLow() == Double.NaN)
                rangeValue.setCriticalLow(Double.MIN_VALUE);
        }
        if (rangeValue.getIgnoreInfo().contains(RangeIgnoreInfo.IgnoreCriticalHigh)) {
            rangeValue.setCriticalHigh(Double.MAX_VALUE);
        } else {
            if (rangeValue.getCriticalHigh() == null || rangeValue.getCriticalHigh() == Double.NaN)
                rangeValue.setCriticalHigh(Double.MAX_VALUE);
        }
        return rangeValue;
    }

    private RangeValue getRangeValuesForQA(AnalyteType analyteType, AnalyteName analyteName) {
        RangeValue rangeValue = new RangeValue();
        rangeValue.setAnalyteName(analyteName);
        rangeValue.setReferenceLow(0.0);
        rangeValue.setReferenceHigh(0.0);
        rangeValue.setCriticalLow(Double.MIN_VALUE);
        rangeValue.setCriticalHigh(Double.MAX_VALUE);
        if (mTestRecord.getTestDetail().getQaSampleInfo() == null || mTestRecord.getTestDetail().getQaSampleInfo().getQaRange() == null) {
            rangeValue.setReferenceLow(0.0);
            rangeValue.setReferenceHigh(0.0);
        } else if (mTestRecord.getTestDetail().getQaSampleInfo().getCustom() && analyteType == AnalyteType.Calculated || analyteType == AnalyteType.Corrected) {
            rangeValue.setReferenceLow(0.0);
            rangeValue.setReferenceHigh(0.0);
        }
        for (QARangeValue qrv : mTestRecord.getTestDetail().getQaSampleInfo().getQaRange().getRangeValues()) {
            if (qrv.getAnalyteName().equals(analyteName)) {
                rangeValue.setReferenceLow(qrv.getReferenceLow());
                rangeValue.setReferenceHigh(qrv.getReferenceHigh());
                break;
            }
        }
        return rangeValue;
    }

    private void getReferenceRange(TestStateDataObject testStateDataObject, RefWrappers<TestRawResultCollection> testRawResultCollectionWrapper) {
        boolean eVADapplied = false;
        String retcode;
        RealmList<TestResult> testResults = new RealmList<>();

        //// TODO: 11/27/2017 hardcode mErrorCode = HostErrorCode.NoError for phase 1 SQA validation
        mErrorCode = HostErrorCode.NoError;
        if ((testRawResultCollectionWrapper.getRef() != null) && ((mErrorCode == HostErrorCode.NoError) || (mErrorCode == HostErrorCode.ExpiredCard))) {
            RangeModel referenceRangeModel = new RangeModel();
            Range referenceRange = referenceRangeModel.getUnmanagedRange(BloodSampleType.Unspecified, "default");

            for (int i = 0; i < testRawResultCollectionWrapper.getRef().getMeasuredResults().size(); i++) {
                ResultStatus reference;
                FinalResult currentResult = testRawResultCollectionWrapper.getRef().getMeasuredResults().get(i);
                RangeValue rangeValue;
                if (mTestRecord.getTestMode() == TestMode.BloodTest) {
                    rangeValue = getRangeValuesForBlood(currentResult.analyte, mTestRecord.getTestDetail().getSampleType(), "default");
                } else {
                    rangeValue = getRangeValuesForQA(AnalyteType.Measured, currentResult.analyte);
                }
                if (rangeValue == null) {
                    rangeValue = new RangeValue();
                    rangeValue.setAnalyteName(currentResult.analyte);
                    rangeValue.setReferenceLow(Double.MIN_VALUE);
                    rangeValue.setReferenceHigh(Double.MAX_VALUE);
                    rangeValue.setCriticalLow(Double.MIN_VALUE);
                    rangeValue.setCriticalHigh(Double.MAX_VALUE);

                }
                ReportableRange reportableRange = mTestConfiguration.getDefaultReportableRange(currentResult.analyte);

                // all the results are in the 'default' units.. if the selected units are not the
                // default ones, do the conversion
                double convertedResult = TestDataFormatConvert.upConvertAnalyteDataDefaultUnit(currentResult.analyte, currentResult.reading, reportableRange.DefaultUnit);

                // determine range indicator. compare the converted, rounded result against converted,
                // rounded ranges
                reference = TestDataFormatConvert.determineRangeIndicator(currentResult.returnCode, convertedResult,
                        rangeValue.getReferenceLow(), rangeValue.getReferenceHigh(),
                        rangeValue.getCriticalLow(), rangeValue.getCriticalHigh(),
                        reportableRange.Low, reportableRange.High);

                // we're going to check if it was outside the reportable range here, and if it was, we're going to
                // substitute the value in the reading with the reportable range.. that way it wont trigger
                // references or criticals and will get placed correctly in the file
                if (currentResult.returnCode == ResultsCalcReturnCode.OverReportableRange) {
                    convertedResult = reportableRange.High;
                } else if (currentResult.returnCode == ResultsCalcReturnCode.UnderReportableRange) {
                    convertedResult = reportableRange.Low;
                }

                // save the fact that it's an expired card if the test was successful
                if ((mErrorCode == HostErrorCode.ExpiredCard) &&
                        ((currentResult.returnCode == ResultsCalcReturnCode.Success) ||
                                (currentResult.returnCode == ResultsCalcReturnCode.UnderReportableRange) ||
                                (currentResult.returnCode == ResultsCalcReturnCode.OverReportableRange) ||
                                (currentResult.returnCode == ResultsCalcReturnCode.UncorrectedHematocrit))) {
                    currentResult.returnCode = ResultsCalcReturnCode.ExpiredCard;
                }

                // only do this if no errors
                if ((mErrorCode == HostErrorCode.NoError) || (mErrorCode == HostErrorCode.ExpiredCard)) {
                    TestResult result = null;
                    for (int j = 0; j < testResults.size(); j++) {
                        if (testResults.get(j).getAnalyteName() != null && testResults.get(j).getAnalyteName() == currentResult.analyte) {
                            result = testResults.get(j);
                        }
                    }
                    if (result == null) {
                        result = new TestResult();
                        testResults.add(result);
                    }


                    //QA test
                    /*
                    if (mTestRecord.getTestMode() == TestMode.QA && eVADapplied && qcFluidInfo != null)
                    {
                        QCTestedStatus testedStatus = QCTestedStatus.NotTested;
                        if (reference == Reference.Normal || reference == Reference.None)
                        {
                            testedStatus = QCTestedStatus.Passed;
                        }
                        else
                        {
                            testedStatus = QCTestedStatus.Failed;
                        }
                        if (currentResult.returnCode == ResultsCalcReturnCode.CannotCalculate || epoc.analyticalManager.AnalyticalManager.FailedIQC(currentResult.returnCode) || currentResult.requirementsFailedIQC)
                        {
                            testedStatus = QCTestedStatus.Failed;
                        }
                        UpdateFluidInfoByQATest((epoc.common.types.AnalyteName)currentResult.analyte, TestRecord.TestDateTime, testedStatus, ref qcFluidInfo);
                    }
                    */

                    //_result.Channel = _Legacy.ToChannelType(Strings.ChannelTypeStrings[(int)currentResult.channelType]);
                    result.setAnalyteName(currentResult.analyte);
                    result.setUnitType(TestDataFormatConvert.getUnit(currentResult.analyte));
                    result.setResultStatus(reference);

                    if (mErrorCode != HostErrorCode.ExpiredCard) {
                        double reportableLo = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), reportableRange.Low, result.getUnitType());
                        if ((!Double.isNaN(reportableLo)) && (reportableLo != Double.MIN_VALUE) && (reportableLo != Double.MAX_VALUE)) {
                            result.setReportableLow(reportableLo);
                        } else {
                            result.setReportableLow(Double.NaN);
                        }

                        double reportableHi = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), reportableRange.High, result.getUnitType());
                        if ((!Double.isNaN(reportableHi)) && (reportableHi != Double.MIN_VALUE) && (reportableHi != Double.MAX_VALUE)) {
                            result.setReportableHigh(reportableHi);
                        } else {
                            result.setReportableHigh(Double.NaN);
                        }
                    }

                    //result
                    double compareAgainst = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), convertedResult, result.getUnitType());
                    String valueString = TestDataFormatConvert.convertReadingValue(currentResult.returnCode, compareAgainst, result);
                    if (currentResult.returnCode == ResultsCalcReturnCode.UnderReportableRange) {
                        if (reference == ResultStatus.CriticalLow) {
                            result.setResultStatus(ResultStatus.CriticalBelowReportableRange);
                        } else if (reference == ResultStatus.ReferenceLow) {
                            result.setResultStatus(ResultStatus.ReferenceBelowReportableRange);
                        } else {
                            result.setResultStatus(ResultStatus.BelowReportableRange);
                        }
                    } else if (currentResult.returnCode == ResultsCalcReturnCode.OverReportableRange) {
                        if (reference == ResultStatus.CriticalHigh) {
                            result.setResultStatus(ResultStatus.CriticalAboveReportableRange);
                        } else if (reference == ResultStatus.ReferenceHigh) {
                            result.setResultStatus(ResultStatus.ReferenceAboveReportableRange);
                        } else {
                            result.setResultStatus(ResultStatus.AboveReportableRange);
                        }
                    }

                    //refence low
                    double valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getReferenceLow(), result.getUnitType());
                    if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                        result.setReferenceLow(valu);
                    } else {
                        result.setReferenceLow(Double.NaN);
                    }

                    //ref high
                    valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getReferenceHigh(), result.getUnitType());
                    if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                        result.setReferenceHigh(valu);
                    } else {
                        result.setReferenceHigh(Double.NaN);
                    }

                    //critical low
                    if (mTestRecord.getTestMode() == TestMode.BloodTest) {
                        valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getCriticalLow(), result.getUnitType());
                        if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                            result.setCriticalLow(valu);
                        } else {
                            result.setCriticalLow(Double.NaN);
                        }
                    } else {
                        result.setCriticalLow(Double.NaN);
                    }

                    //critical high
                    if (mTestRecord.getTestMode() == TestMode.BloodTest) {
                        valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getCriticalHigh(), result.getUnitType());
                        if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                            result.setCriticalHigh(valu);
                        } else {
                            result.setCriticalHigh(Double.NaN);
                        }
                    } else {
                        result.setCriticalHigh(Double.NaN);
                    }

                    //return code
                    result.setReturnCode(currentResult.returnCode.value);
                    if (reference == ResultStatus.CriticalHigh || reference == ResultStatus.CriticalLow) {
                        mTestRecord.setHasCritical(true);
                    }
                }
            }//measurement

            for (int i = 0; i < testRawResultCollectionWrapper.getRef().getCorrectedResults().size(); i++) {
                ResultStatus reference;
                FinalResult currentResult = testRawResultCollectionWrapper.getRef().getCorrectedResults().get(i);
                RangeValue rangeValue;

                if (mTestRecord.getTestMode() == TestMode.BloodTest) {
                    rangeValue = getRangeValuesForBlood(currentResult.analyte, mTestRecord.getTestDetail().getSampleType(), "default");
                } else {
                    rangeValue = getRangeValuesForQA(AnalyteType.Corrected, currentResult.analyte);
                }
                if (rangeValue == null) {
                    rangeValue = new RangeValue();
                    rangeValue.setReferenceLow(Double.MIN_VALUE);
                    rangeValue.setReferenceHigh(Double.MAX_VALUE);
                    rangeValue.setCriticalLow(Double.MIN_VALUE);
                    rangeValue.setCriticalHigh(Double.MAX_VALUE);

                }
                ReportableRange reportableRange = mTestConfiguration.getDefaultReportableRange(currentResult.analyte);

                // all the results are in the 'default' units.. if the selected units are not the
                // default ones, do the conversion
                double convertedResult = TestDataFormatConvert.upConvertAnalyteDataDefaultUnit(currentResult.analyte, currentResult.reading, reportableRange.DefaultUnit);

                // determine range indicator. compare the converted, rounded result against converted,
                // rounded ranges
                reference = TestDataFormatConvert.determineRangeIndicator(currentResult.returnCode, convertedResult,
                        rangeValue.getReferenceLow(), rangeValue.getReferenceHigh(),
                        rangeValue.getCriticalLow(), rangeValue.getCriticalHigh(),
                        reportableRange.Low, reportableRange.High);

                // we're going to check if it was outside the reportable range here, and if it was, we're going to
                // substitute the value in the reading with the reportable range.. that way it wont trigger
                // references or criticals and will get placed correctly in the file
                if (currentResult.returnCode == ResultsCalcReturnCode.OverReportableRange) {
                    convertedResult = reportableRange.High;
                } else if (currentResult.returnCode == ResultsCalcReturnCode.UnderReportableRange) {
                    convertedResult = reportableRange.Low;
                }

                // save the fact that it's an expired card if the test was successful
                if ((mErrorCode == HostErrorCode.ExpiredCard) &&
                        ((currentResult.returnCode == ResultsCalcReturnCode.Success) ||
                                (currentResult.returnCode == ResultsCalcReturnCode.UnderReportableRange) ||
                                (currentResult.returnCode == ResultsCalcReturnCode.OverReportableRange) ||
                                (currentResult.returnCode == ResultsCalcReturnCode.UncorrectedHematocrit))) {
                    currentResult.returnCode = ResultsCalcReturnCode.ExpiredCard;
                }

                // only do this if no errors
                if ((mErrorCode == HostErrorCode.NoError) || (mErrorCode == HostErrorCode.ExpiredCard)) {
                    TestResult result = null;
                    for (int j = 0; j < testResults.size(); j++) {
                        if (testResults.get(j).getAnalyteName() != null && testResults.get(j).getAnalyteName() == currentResult.analyte) {
                            result = testResults.get(j);
                        }
                    }
                    if (result == null) {
                        result = new TestResult();
                        testResults.add(result);
                    }

                    //_result.Channel = _Legacy.ToChannelType(Strings.ChannelTypeStrings[(int)currentResult.channelType]);
                    result.setAnalyteName(currentResult.analyte);
                    result.setUnitType(TestDataFormatConvert.getUnit(currentResult.analyte));
                    result.setResultStatus(reference);

                    if (mErrorCode != HostErrorCode.ExpiredCard) {
                        double reportableLo = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), reportableRange.Low, result.getUnitType());
                        if ((!Double.isNaN(reportableLo)) && (reportableLo != Double.MIN_VALUE) && (reportableLo != Double.MAX_VALUE)) {
                            result.setReportableLow(reportableLo);
                        } else {
                            result.setReportableLow(Double.NaN);
                        }

                        double reportableHi = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), reportableRange.High, result.getUnitType());
                        if ((!Double.isNaN(reportableHi)) && (reportableHi != Double.MIN_VALUE) && (reportableHi != Double.MAX_VALUE)) {
                            result.setReportableHigh(reportableHi);
                        } else {
                            result.setReportableHigh(Double.NaN);
                        }
                    }

                    //result
                    double compareAgainst = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), convertedResult, result.getUnitType());
                    String valueString = TestDataFormatConvert.convertReadingValue(currentResult.returnCode, compareAgainst, result);

                    if (currentResult.returnCode == ResultsCalcReturnCode.UnderReportableRange) {
                        if (reference == ResultStatus.CriticalLow) {
                            result.setResultStatus(ResultStatus.CriticalBelowReportableRange);
                        } else if (reference == ResultStatus.ReferenceLow) {
                            result.setResultStatus(ResultStatus.ReferenceBelowReportableRange);
                        } else {
                            result.setResultStatus(ResultStatus.BelowReportableRange);
                        }
                    } else if (currentResult.returnCode == ResultsCalcReturnCode.OverReportableRange) {
                        if (reference == ResultStatus.CriticalHigh) {
                            result.setResultStatus(ResultStatus.CriticalAboveReportableRange);
                        } else if (reference == ResultStatus.ReferenceHigh) {
                            result.setResultStatus(ResultStatus.ReferenceAboveReportableRange);
                        } else {
                            result.setResultStatus(ResultStatus.AboveReportableRange);
                        }
                    }

                    //refence low
                    double valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getReferenceLow(), result.getUnitType());
                    if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                        result.setReferenceLow(valu);
                    } else {
                        result.setReferenceLow(Double.NaN);
                    }

                    //ref high
                    valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getReferenceHigh(), result.getUnitType());
                    if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                        result.setReferenceHigh(valu);
                    } else {
                        result.setReferenceHigh(Double.NaN);
                    }

                    //critical low
                    if (mTestRecord.getTestMode() == TestMode.BloodTest) {
                        valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getCriticalLow(), result.getUnitType());
                        if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                            result.setCriticalLow(valu);
                        } else {
                            result.setCriticalLow(Double.NaN);
                        }
                    } else {
                        result.setCriticalLow(Double.NaN);
                    }
                    //critical high
                    if (mTestRecord.getTestMode() == TestMode.BloodTest) {
                        valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getCriticalHigh(), result.getUnitType());
                        if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                            result.setCriticalHigh(valu);
                        } else {
                            result.setCriticalHigh(Double.NaN);
                        }
                    } else {
                        result.setCriticalHigh(Double.NaN);
                    }
                    //return code
                    result.setReturnCode(currentResult.returnCode.value);
                    if (reference == ResultStatus.CriticalHigh || reference == ResultStatus.CriticalLow) {
                        mTestRecord.setHasCritical(true);
                    }
                }
            }//corrected result
            TestDataFormatConvert.applyReportableAndInsanityRangesToTestResults(testRawResultCollectionWrapper.getRef().getCalculatedResults(), mTestRecord.getTestMode(), mTestConfiguration);

            for (int i = 0; i < testRawResultCollectionWrapper.getRef().getCalculatedResults().size(); i++) {
                ResultStatus reference;
                FinalResult currentResult = testRawResultCollectionWrapper.getRef().getCalculatedResults().get(i);
                RangeValue rangeValue;

                if (mTestRecord.getTestMode() == TestMode.BloodTest) {
                    rangeValue = getRangeValuesForBlood(currentResult.analyte, mTestRecord.getTestDetail().getSampleType(), "default");
                } else {
                    rangeValue = getRangeValuesForQA(AnalyteType.Calculated, currentResult.analyte);
                }
                if (rangeValue == null) {
                    rangeValue = new RangeValue();
                    rangeValue.setReferenceLow(Double.MIN_VALUE);
                    rangeValue.setReferenceHigh(Double.MAX_VALUE);
                    rangeValue.setCriticalLow(Double.MIN_VALUE);
                    rangeValue.setCriticalHigh(Double.MAX_VALUE);

                }
                ReportableRange reportableRange = mTestConfiguration.getDefaultReportableRange(currentResult.analyte);

                // all the results are in the 'default' units.. if the selected units are not the
                // default ones, do the conversion
                double convertedResult = TestDataFormatConvert.upConvertAnalyteDataDefaultUnit(currentResult.analyte, currentResult.reading, reportableRange.DefaultUnit);

                // determine range indicator. compare the converted, rounded result against converted,
                // rounded ranges
                reference = TestDataFormatConvert.determineRangeIndicator(currentResult.returnCode, convertedResult,
                        rangeValue.getReferenceLow(), rangeValue.getReferenceHigh(),
                        rangeValue.getCriticalLow(), rangeValue.getCriticalHigh(),
                        reportableRange.Low, reportableRange.High);

                // we're going to check if it was outside the reportable range here, and if it was, we're going to
                // substitute the value in the reading with the reportable range.. that way it wont trigger
                // references or criticals and will get placed correctly in the file
                if (currentResult.returnCode == ResultsCalcReturnCode.OverReportableRange) {
                    convertedResult = reportableRange.High;
                } else if (currentResult.returnCode == ResultsCalcReturnCode.UnderReportableRange) {
                    convertedResult = reportableRange.Low;
                }

                // save the fact that it's an expired card if the test was successful
                if ((mErrorCode == HostErrorCode.ExpiredCard) &&
                        ((currentResult.returnCode == ResultsCalcReturnCode.Success) ||
                                (currentResult.returnCode == ResultsCalcReturnCode.UnderReportableRange) ||
                                (currentResult.returnCode == ResultsCalcReturnCode.OverReportableRange) ||
                                (currentResult.returnCode == ResultsCalcReturnCode.UncorrectedHematocrit))) {
                    currentResult.returnCode = ResultsCalcReturnCode.ExpiredCard;
                }

                // only do this if no errors
                if ((mErrorCode == HostErrorCode.NoError) || (mErrorCode == HostErrorCode.ExpiredCard)) {
                    TestResult result = null;
                    for (int j = 0; j < testResults.size(); j++) {
                        if (testResults.get(j).getAnalyteName() != null && testResults.get(j).getAnalyteName() == currentResult.analyte) {
                            result = testResults.get(j);
                        }
                    }
                    if (result == null) {
                        result = new TestResult();
                        testResults.add(result);
                    }

                    //_result.Channel = _Legacy.ToChannelType(Strings.ChannelTypeStrings[(int)currentResult.channelType]);
                    result.setAnalyteName(currentResult.analyte);
                    result.setUnitType(TestDataFormatConvert.getUnit(currentResult.analyte));
                    result.setResultStatus(reference);

                    if (mErrorCode != HostErrorCode.ExpiredCard) {
                        double reportableLo = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), reportableRange.Low, result.getUnitType());
                        if ((!Double.isNaN(reportableLo)) && (reportableLo != Double.MIN_VALUE) && (reportableLo != Double.MAX_VALUE)) {
                            result.setReportableLow(reportableLo);
                        } else {
                            result.setReportableLow(Double.NaN);
                        }

                        double reportableHi = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), reportableRange.High, result.getUnitType());
                        if ((!Double.isNaN(reportableHi)) && (reportableHi != Double.MIN_VALUE) && (reportableHi != Double.MAX_VALUE)) {
                            result.setReportableHigh(reportableHi);
                        } else {
                            result.setReportableHigh(Double.NaN);
                        }
                    }

                    //result
                    double compareAgainst = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), convertedResult, result.getUnitType());
                    String valueString = TestDataFormatConvert.convertReadingValue(currentResult.returnCode, compareAgainst, result);

                    if (currentResult.returnCode == ResultsCalcReturnCode.UnderReportableRange) {
                        if (reference == ResultStatus.CriticalLow) {
                            result.setResultStatus(ResultStatus.CriticalBelowReportableRange);
                        } else if (reference == ResultStatus.ReferenceLow) {
                            result.setResultStatus(ResultStatus.ReferenceBelowReportableRange);
                        } else {
                            result.setResultStatus(ResultStatus.BelowReportableRange);
                        }
                    } else if (currentResult.returnCode == ResultsCalcReturnCode.OverReportableRange) {
                        if (reference == ResultStatus.CriticalHigh) {
                            result.setResultStatus(ResultStatus.CriticalAboveReportableRange);
                        } else if (reference == ResultStatus.ReferenceHigh) {
                            result.setResultStatus(ResultStatus.ReferenceAboveReportableRange);
                        } else {
                            result.setResultStatus(ResultStatus.AboveReportableRange);
                        }
                    }

                    //refence low
                    double valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getReferenceLow(), result.getUnitType());
                    if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                        result.setReferenceLow(valu);
                    } else {
                        result.setReferenceLow(Double.NaN);
                    }
                    //ref high
                    valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getReferenceHigh(), result.getUnitType());
                    if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                        result.setReferenceHigh(valu);
                    } else {
                        result.setReferenceHigh(Double.NaN);
                    }

                    if (mTestRecord.getTestMode() == TestMode.QA && eVADapplied) {
                        result.setReferenceLow(Double.NaN);
                        result.setReferenceHigh(Double.NaN);
                        reference = ResultStatus.Normal;
                    }
                    //critical low
                    if (mTestRecord.getTestMode() == TestMode.BloodTest) {
                        valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getCriticalLow(), result.getUnitType());
                        if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                            result.setCriticalLow(valu);
                        } else {
                            result.setCriticalLow(Double.NaN);
                        }
                    } else {
                        result.setCriticalLow(Double.NaN);
                    }
                    //critical high
                    if (mTestRecord.getTestMode() == TestMode.BloodTest) {
                        valu = TestDataFormatConvert.getAnalyteToCorrectDecimalPlaces(result.getAnalyteName(), rangeValue.getCriticalHigh(), result.getUnitType());
                        if ((!Double.isNaN(valu)) && (valu != Double.MIN_VALUE) && (valu != Double.MAX_VALUE)) {
                            result.setCriticalHigh(valu);
                        } else {
                            result.setCriticalHigh(Double.NaN);
                        }
                    } else {
                        result.setCriticalHigh(Double.NaN);
                    }
                    //return code
                    result.setReturnCode(currentResult.returnCode.value);
                    if (reference == ResultStatus.CriticalHigh || reference == ResultStatus.CriticalLow) {
                        mTestRecord.setHasCritical(true);
                    }
                }
            }
        }
        saveTestResults(testResults);
        //  mTestRecord.setTestResults(testResults);
        //UpdateQCReaderTableWithFluidInfo
    }

    public boolean checkEQCTestValid() {
        return true;
    }

    public BarcodeVerificationCode validateBarcodeInformation(byte[] fakeBarcode, int length) {
        this.mCardType = 6;
        this.mSensorLayout = 0;

        return BarcodeVerificationCode.Success;
    }

    public boolean loadTestConfiguration() {
        mTestConfiguration = TestConfigurationManager.getInstance().getTestConfiguration(mCardType);
        return mTestConfiguration != null;
    }

    private boolean loadTestConfiguration(short cardType) {
        return TestConfigurationManager.getInstance().getTestConfiguration(cardType) != null;
    }

    public ValidateAnalyteTestReturnCode validateAvailableAnalytes(RefWrappers<List<Analyte>> availableByRef) {
        availableByRef.getRef().clear();
        AnalyteModel analyteModel = new AnalyteModel();
        List<Analyte> analytes = analyteModel.getAllAnalytes();
        for (int i = 0; i < analytes.size(); i++) {
            for (int j = 0; j < mTestConfiguration.HostTestResultConfigSetting.CardSetups.size(); j++) {
                CardSetup cardSetup = mTestConfiguration.HostTestResultConfigSetting.CardSetups.get(j);
                if (analytes.get(i).getAnalyteName().value == cardSetup.Analyte) {
                    if (analytes.get(i).getOptionType().value.equals(EnabledSelectedOptionType.EnabledUnselected.value)
                            || analytes.get(i).getOptionType().value.equals(EnabledSelectedOptionType.EnabledSelected.value)) {
                        availableByRef.getRef().add(analytes.get(i));
                        break;
                    }
                }
            }
        }
        return ValidateAnalyteTestReturnCode.AnalytesAvailable;
    }

    public QAScheduleLookoutReturnCode checkQAScheduleLookout(List<Analyte> availableAnalytes, RefWrappers<List<Analyte>> feedbackByRef) {
        return QAScheduleLookoutReturnCode.QAPassed;
    }

    public List<AnalyteOption> getTestDefaultsForOneCardType() {
        List<AnalyteOption> options = new ArrayList<>();
        AnalyteModel analyteModel = new AnalyteModel();
        List<Analyte> analytes = analyteModel.getAllAnalytes();
        for (int i = 0; i < mTestConfiguration.HostTestResultConfigSetting.CardSetups.size(); i++) {
            if (mTestConfiguration.HostTestResultConfigSetting.CardSetups.get(i).ResultType == AnalyteType.Measured.value) {
                CardSetup cardSetup = mTestConfiguration.HostTestResultConfigSetting.CardSetups.get(i);
                AnalyteOption option = new AnalyteOption();
                option.setAnalyteName(AnalyteName.convert(cardSetup.Analyte));
                Analyte analyte = null;
                for (int j = 0; j < analytes.size(); j++) {
                    if (analytes.get(j).getAnalyteName() == option.getAnalyteName()) {
                        analyte = analytes.get(j);
                        break;
                    }
                }
                if (analyte != null) {
                    option.setOptionType(analyte.getOptionType());
                    option.setDisplayOrder(analyte.getDisplayorder());
                } else {
                    option.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                    option.setDisplayOrder(99);
                }
                options.add(option);
            }
        }

        return options;
    }

    public boolean checkEarlyInjection(TestStateDataObject stateDataObject, TstActRspBGETest tempPacket) {

        if (mTestConfiguration.RealTimeQCSetting.Enabled) {
            // do we have our first fluid yet? save the value
            if (mFirstFluid == 0) {
                if (tempPacket.getData().getBDFlags() != 0) {
                    mFirstFluid = mLastRecordedTime;
                }
            }
            // we save the previous hematocrit return code so that we
            // can pass it in next time and the value gets taken into account
            // when determining if something went wrong.. save us cycles
            CheckForEarlyInjectionResponse response = AnalyticalManager.checkForEarlyInjection(mTestReadings.get(mHematocritReadings),
                    mTestReadings.get(mTopHeaterReadings),
                    mRealTimehctRc,
                    mTestConfiguration.ReaderConfigSetting.FluidAfterFluidThreshold,
                    mLastRecordedTime,
                    mFirstFluid);

            if (!response.isSuccess()) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.UNKNOWN);
                stateDataObject.postEvent(eventInfo);
                return false;
            }

            //SensorReadings sensorReadingsInResponse = response.getHematocritReadings();
            mRealTimehctRc = response.getAmReturnCode();

            if (mRealTimehctRc == RealTimeHematocritQCReturnCode.LowResistance) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.HEMATOCRITLOWRESISTANCE);
                stateDataObject.postEvent(eventInfo);
                mErrorCode = HostErrorCode.HematocritLowResistance;
                return false;
            } else if (mRealTimehctRc == RealTimeHematocritQCReturnCode.EarlyInjection) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.EARLYINJECTION);
                stateDataObject.postEvent(eventInfo);
                mErrorCode = HostErrorCode.EarlyInjection;
                return false;
            } else if (mRealTimehctRc == RealTimeHematocritQCReturnCode.FailedQC) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.EQCFAILED);
                stateDataObject.postEvent(eventInfo);
                if (getErrorCode() == HostErrorCode.NoError) {
                    mErrorCode = HostErrorCode.FluidicsFailedQCDuringCalibration;
                }
                return false;
            }

            return doRealTimeQC(stateDataObject);
        }

        return true;
    }

    public boolean doRealTimeQC(TestStateDataObject stateDataObject) {

        RealTimeQCReturnCode rc = performRealTimeQC();
        if ((rc != RealTimeQCReturnCode.Success) && (rc != RealTimeQCReturnCode.NotPerformed)) {
            // humidity gets its own check
            if (rc == RealTimeQCReturnCode.HumidityFailed) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.HUMIDITYCHECKFAILED);
                stateDataObject.postEvent(eventInfo);
            } else if (rc == RealTimeQCReturnCode.ReferenceBubble) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.REFERENCEBUBBLE);//ReferenceBubble
                stateDataObject.postEvent(eventInfo);
            } else {
                boolean fluidicsFailed = false;
                // find out if its a fluidics failed error code or realtime qc failed
                for (int i = 0; i < mTestReadings.size(); i++) {
                    if ((mTestReadings.get(i).sensorType == Sensors.Conductivity) &&
                            ((mTestReadings.get(i).realTimeQCPassed != RealTimeQCReturnCode.Success) &&
                                    (mTestReadings.get(i).realTimeQCPassed != RealTimeQCReturnCode.NotPerformed))) {
                        fluidicsFailed = true;
                        break;
                    }
                }
                if (fluidicsFailed) {
                    mErrorCode = HostErrorCode.FluidicsFailedQCDuringCalibration;
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.ERROROCCURREDTESTSTOP);
                    stateDataObject.postEvent(eventInfo);
                } else {
                    mErrorCode = HostErrorCode.RealtimeQCFailedDuringFluidics;
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.ERROROCCURREDTESTSTOP);
                    stateDataObject.postEvent(eventInfo);

                    for (int i = 0; i < mTestReadings.size(); i++) {
                        if ((mTestReadings.get(i).realTimeQCPassed == RealTimeQCReturnCode.FailedD2High)
                                || (mTestReadings.get(i).realTimeQCPassed == RealTimeQCReturnCode.FailedD2Low)) {
                            mErrorCode = HostErrorCode.ReferenceBubble;
                            break;
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    private RealTimeQCReturnCode performRealTimeQC() {
        boolean allImportantOnesPassed = true;
        if (!mTestConfiguration.RealTimeQCSetting.Enabled) {
            return RealTimeQCReturnCode.NotPerformed;
        }

        // only check hematocrit when
        PerformRealTimeQCResponse rcResp = AnalyticalManager.performRealTimeQC(mTestReadings,
                mRealTimeQC,
                mLastRecordedTime);

        if (!rcResp.isSuccess()) {
            return RealTimeQCReturnCode.Failed;
        }

        RealTimeQCReturnCode rc = rcResp.getAmReturnCode();
        if (rc == RealTimeQCReturnCode.NotPerformed) {
            return rc;
        } else if (rc != RealTimeQCReturnCode.Success) {
            // something failed.. Since the analytical manager is going to check ALL of them, we are
            // going to go through them and check that the important ones which are the sensors
            // and the conductivity
            for (int i = 0; i < mTestReadings.size(); i++) {
                // if it didnt pass, lets find out if it was important
                if (mTestReadings.get(i).realTimeQCPassed != RealTimeQCReturnCode.Success) {
                    mTestReadings.get(i).extraString += "R" + mLastRecordedTime + " ";

                    if (mTestReadings.get(i).checkRealtimeQC) {
                        // lets hope a realtime qc failure doesnt happen on a heater for now
                        allImportantOnesPassed = false;
                    }
                }

                // if it didnt pass, lets find out if it was important
                if (mTestReadings.get(i).humidityPassed == HumidityReturnCode.Failed) {
                    mTestReadings.get(i).extraString += "Humid" + mLastRecordedTime + " ";
                }
            }
        }

        if (!allImportantOnesPassed || (rc == RealTimeQCReturnCode.ReferenceBubble)) {
            return rc;
        } else {
            if (rc == RealTimeQCReturnCode.HumidityFailed) {
                return RealTimeQCReturnCode.HumidityFailed;
            } else {
                return RealTimeQCReturnCode.Success;
            }
        }
    }

    private void getAllDefaultReportableRanges() {
        for (int i = 0; i < mTestReadings.size(); i++) {
            AnalyteName analyte = TestDataFormatConvert.SensorToAnalyte(mTestReadings.get(i).sensorType);

            if (mTestConfiguration.isMeasuredAnalyte(analyte)) {
                ReportableRange range = mTestConfiguration.getDefaultReportableRange(analyte);
                InsanityRange insanityRange = mTestConfiguration.getInsanityRange(analyte);

                mTestReadings.get(i).reportableHigh = range.High;
                mTestReadings.get(i).reportableLow = range.Low;

                mTestReadings.get(i).insanityHigh = insanityRange.High;
                mTestReadings.get(i).insanityLow = insanityRange.Low;
                mTestReadings.get(i).insanityQAHigh = insanityRange.QAHigh;
                mTestReadings.get(i).insanityQALow = insanityRange.QALow;
            }
        }
    }

    private boolean isIncludeAnalyte(AnalyteName analyte, List<AnalyteName> inclusions) {
        for (int i = 0; i < inclusions.size(); i++) {
            if (analyte == inclusions.get(i))
                return true;
        }
        return false;
    }

    private boolean isIncludeCalculatedResult(AnalyteName calculatedAnalyte, List<AnalyteName> inclusions) {
        if (calculatedAnalyte == AnalyteName.BEb) {
            // be(b) is always in
            return (isIncludeAnalyte(AnalyteName.pH, inclusions) &&
                    isIncludeAnalyte(AnalyteName.pCO2, inclusions) && mShowBEb);
        } else if (calculatedAnalyte == AnalyteName.BEecf) {
            // be(ecf) is always in
            return (isIncludeAnalyte(AnalyteName.pH, inclusions) &&
                    isIncludeAnalyte(AnalyteName.pCO2, inclusions) && mShowBEecf);
        } else if (calculatedAnalyte == AnalyteName.cHgb) {
            // hemoglobin is in if hematocrit is in
            return isIncludeAnalyte(AnalyteName.Hct, inclusions);
        } else if (calculatedAnalyte == AnalyteName.HCO3act) {
            // hco3-act is in if ph and pco2 are in
            return (isIncludeAnalyte(AnalyteName.pH, inclusions) &&
                    isIncludeAnalyte(AnalyteName.pCO2, inclusions));
        } else if (calculatedAnalyte == AnalyteName.O2SAT) {
            // o2sat needs bicarb (ph, pco2) and oxygen
            return (isIncludeAnalyte(AnalyteName.pH, inclusions) &&
                    isIncludeAnalyte(AnalyteName.pCO2, inclusions) &&
                    isIncludeAnalyte(AnalyteName.pO2, inclusions));
        } else if (calculatedAnalyte == AnalyteName.cTCO2) {
            return (isIncludeAnalyte(AnalyteName.pH, inclusions) &&
                    isIncludeAnalyte(AnalyteName.pCO2, inclusions)
                    && (!isIncludeAnalyte(AnalyteName.TCO2, inclusions)));
        } else if (calculatedAnalyte == AnalyteName.AlveolarO2) {
            return (isIncludeAnalyte(AnalyteName.pCO2, inclusions));
        } else if (calculatedAnalyte == AnalyteName.ArtAlvOxDiff) {
            return (isIncludeAnalyte(AnalyteName.pCO2, inclusions)) && (isIncludeAnalyte(AnalyteName.pO2, inclusions));
        } else if (calculatedAnalyte == AnalyteName.ArtAlvOxRatio) {
            return (isIncludeAnalyte(AnalyteName.pCO2, inclusions)) && (isIncludeAnalyte(AnalyteName.pO2, inclusions));
        } else if (calculatedAnalyte == AnalyteName.AnionGap) {
            return (isIncludeAnalyte(AnalyteName.Na, inclusions) &&
                    isIncludeAnalyte(AnalyteName.Chloride, inclusions)) &&
                    ((isIncludeAnalyte(AnalyteName.pCO2, inclusions) && isIncludeAnalyte(AnalyteName.pH, inclusions) && new AnalyteModel().isEnabled(AnalyteName.HCO3act))
                            || isIncludeAnalyte(AnalyteName.TCO2, inclusions));
        } else if (calculatedAnalyte == AnalyteName.AnionGapK) {
            return (isIncludeAnalyte(AnalyteName.Na, inclusions) &&
                    isIncludeAnalyte(AnalyteName.K, inclusions) &&
                    isIncludeAnalyte(AnalyteName.Chloride, inclusions)) &&
                    ((isIncludeAnalyte(AnalyteName.pCO2, inclusions) && isIncludeAnalyte(AnalyteName.pH, inclusions) && new AnalyteModel().isEnabled(AnalyteName.HCO3act)) || (isIncludeAnalyte(AnalyteName.TCO2, inclusions)));
        } else if (calculatedAnalyte == AnalyteName.BUNCreaRatio) {
            return (isIncludeAnalyte(AnalyteName.BUN, inclusions) &&
                    isIncludeAnalyte(AnalyteName.Creatinine, inclusions));
        } else if (calculatedAnalyte == AnalyteName.UreaCreaRatio) {
            return (isIncludeAnalyte(AnalyteName.Urea, inclusions) &&
                    isIncludeAnalyte(AnalyteName.Creatinine, inclusions));
        } else if ((calculatedAnalyte == AnalyteName.eGFR) || (calculatedAnalyte == AnalyteName.eGFRa) ||
                (calculatedAnalyte == AnalyteName.eGFRj) ||
                (calculatedAnalyte == AnalyteName.GFRckd) || (calculatedAnalyte == AnalyteName.GFRckda) ||
                (calculatedAnalyte == AnalyteName.GFRswz)) {
            return isIncludeAnalyte(AnalyteName.Creatinine, inclusions);
        } else {
            // otherwise it's not in
            return false;
        }
    }

    private boolean isIncludeCorrectedResult(AnalyteName correctedAnalyte, List<AnalyteName> inclusions) {
        if (correctedAnalyte == AnalyteName.pCO2T) {
            // corrected pco2 is included if pco2 was included
            return isIncludeAnalyte(AnalyteName.pCO2, inclusions);
        } else if (correctedAnalyte == AnalyteName.pHT) {
            // corrected ph is included if ph was included
            return isIncludeAnalyte(AnalyteName.pH, inclusions);
        } else if (correctedAnalyte == AnalyteName.pO2T) {
            // corrected po2 is included if po2 was included
            return isIncludeAnalyte(AnalyteName.pO2, inclusions);
        } else if (correctedAnalyte == AnalyteName.cAlveolarO2) {
            // corrected po2 is included if po2 was included
            return isIncludeAnalyte(AnalyteName.pCO2, inclusions);
        } else if (correctedAnalyte == AnalyteName.cArtAlvOxDiff) {
            // corrected po2 is included if po2 was included
            return isIncludeAnalyte(AnalyteName.pCO2, inclusions) && isIncludeAnalyte(AnalyteName.pO2, inclusions);
        } else if (correctedAnalyte == AnalyteName.cArtAlvOxRatio) {
            // corrected po2 is included if po2 was included
            return isIncludeAnalyte(AnalyteName.pCO2, inclusions) && isIncludeAnalyte(AnalyteName.pO2, inclusions);
        } else {
            // otherwise its not in
            return false;
        }
    }

    public void logTestPacket(IMsgPayload mIMsgPayload, String beginString, String endString) {
        if (mIMsgPayload != null) {
            writeTestPacket(beginString + "     [" + (mIMsgPayload.getClass().getName() + "]     " + mIMsgPayload.toString()) + "   " + endString);
        }
    }

    private synchronized void writeTestPacket(String s) {
        try {
            //original for nexGen reader
//            if (mTestPacketFile == null) {
//                createmTestPacketFile();
//            }
            if (mLogFile.mLogZipFile == null) {
                mLogFile.createLogFile(mTimeSpecificString, mDateSpecificString,
                        mTestRecord.getReader().getSerialNumber());
            }
            Calendar now = Calendar.getInstance();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            //mTestPacketFile.write(format1.format(now.getTime()) + "     " + s + "\n");
            mRxUtilLogFile.onNext(format1.format(now.getTime()) + "     " + s + "\n");

        } catch (Exception e) {
            if (Log.isLoggable("writeTestPacket", Log.DEBUG)) {
                Log.d("writeTestPacket",e.getMessage());
            }
        }
    }

    public void closePreviousTest() {
        if (mBGETestDataFile != null) {
            mBGETestDataFile.close();
            mBGETestDataFile = null;
        }
        if (mDPMTestDataFile != null) {
            mDPMTestDataFile.close();
            mDPMTestDataFile = null;
        }
        if (mTestPacketFile != null) {
            mTestPacketFile.close();
            mTestPacketFile = null;
        }

        closePacketFile();
    }

    public void MockCalculation(TestStateDataObject stateDataObject) {
        //test BGE
        //mCardType = 6;
        //mSensorLayout = 0;
        //loadTestConfiguration();
        //TestRawResultCollection re = MockTestResult();
        //prepareResultAfterCalculation(stateDataObject);
    }

    public TestRawResultCollection MockTestResult() {
        TestRawResultCollection test = new TestRawResultCollection();

        ArrayList<FinalResult> measurement = new ArrayList<>();
        FinalResult finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.Hct;
        finalResult.channelType = ChannelType.Conductivity;
        finalResult.reading = Double.NaN;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.CannotCalculate;
        measurement.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.Chloride;
        finalResult.channelType = ChannelType.P1;
        finalResult.reading = 100;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        measurement.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.Na;
        finalResult.channelType = ChannelType.P3;
        finalResult.reading = 150;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        measurement.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.K;
        finalResult.channelType = ChannelType.P4;
        finalResult.reading = 5.0;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        measurement.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.Ca;
        finalResult.channelType = ChannelType.P5;
        finalResult.reading = 1.05;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        measurement.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.pH;
        finalResult.channelType = ChannelType.P6;
        finalResult.reading = 8;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        measurement.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.pCO2;
        finalResult.channelType = ChannelType.P7;
        finalResult.reading = 6;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        measurement.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.pO2;
        finalResult.channelType = ChannelType.A1;
        finalResult.reading = 200;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        measurement.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.Glucose;
        finalResult.channelType = ChannelType.A2;
        finalResult.reading = 80;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        measurement.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.Lactate;
        finalResult.channelType = ChannelType.A3;
        finalResult.reading = 2.07;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        measurement.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.Creatinine;
        finalResult.channelType = ChannelType.A4;
        finalResult.reading = 1.28;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        measurement.add(finalResult);

        test.setMeasuredResults(measurement);

        //corrected
        ArrayList<FinalResult> corrected = new ArrayList<>();
        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.pHT;
        finalResult.channelType = ChannelType.ENUM_UNINITIALIZED;
        finalResult.reading = 7.871;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        corrected.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.pCO2T;
        finalResult.channelType = ChannelType.ENUM_UNINITIALIZED;
        finalResult.reading = 5.9;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        corrected.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.pO2T;
        finalResult.channelType = ChannelType.P3;
        finalResult.reading = 189.2;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        corrected.add(finalResult);

        test.setCorrectedResults(corrected);

        //calcualted
        ArrayList<FinalResult> calculated = new ArrayList<>();
        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.cHgb;
        finalResult.channelType = ChannelType.ENUM_UNINITIALIZED;
        finalResult.reading = 10.8;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        calculated.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.AnionGap;
        finalResult.channelType = ChannelType.ENUM_UNINITIALIZED;
        finalResult.reading = 15.9;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        calculated.add(finalResult);

        finalResult = new FinalResult();
        finalResult.analyte = AnalyteName.O2SAT;
        finalResult.channelType = ChannelType.P3;
        finalResult.reading = 189.2;
        finalResult.requirementsFailedIQC = false;
        finalResult.returnCode = ResultsCalcReturnCode.Success;
        calculated.add(finalResult);

        test.setCalculatedResults(calculated);
        return test;
    }

    //added by rzhuang at July 30 2018 for Legacy
    public ArrayList<LegacyRspStatistics.IndividualStatistics> mStatisticsArrayList;
    private ArrayList<Sequence> mSelfCheckSequence;
    public ArrayList mDryCardSequence;
    public boolean mTestState;
    private boolean mTestRecordPersisted;
    public boolean mReaderRequiresMaintenance = false;
    public boolean mKeepWarningMessage = false;
    public boolean mCanRunThermalCheck = false;
    private int mNumFailedLimit = 4;
    private int mNumFailedOutOfHowMany = 10;
    public float mLowTemperatureLimit = 15;
    public float mHighTemperatureLimit = 30;
    public double mHighPressureLimit = 109.9725;
    public double mLowPressureLimit = 53.3;
    public int mNumMaintenanceRecordsToStore = 20;
    public LegacyRspStatus mRsrMsg;
    public LegacyRspDeviceID mDeviceIDResponse;
    public boolean mSelfTestPerformed;
    public boolean mUserClosedPage;
    public ReaderQCInfo mReaderQCInfo;
    private String mCurrentTimeZoneStandardName;
    private String mCurrentTimeZoneUTC;
    private short mTestPacketsReceived;
    private boolean mErrorsExist;
    public boolean mHasTimeStatusMessageBeenReceived = false;
    private int mSpeed;
    private byte[] mBarcode;
    public String mCardNumber;
    private int mLocalReaderInfoContentAdded = 0;
    public boolean mIsVet = false;
    public TestCardInsertedInfo mCardInfo;
    public LegacyReqConfig1_4 mConfigMsg1_4;
    private String[] readerVersionsNoExtraBytes = {"2.2.2.3"};
    public int mTimeEstimate;
    public String mDAMfirmwareId;
    public String mARMfirmwareId;
    public short mSensorLayoutNumber;   // keep sensor layout for legacy
    private RxUtil<String> mRxUtilLogFile;
    private LogFile mLogFile;

    private void initializeLegacyTestData() {
        mLogFile = new LogFile();
        mRxUtilLogFile = new RxUtil<String>().create(null);
        mRxUtilLogFile.subscribeOn(mLogFile, Schedulers.io());

        mCardInfo = new TestCardInsertedInfo();
        loadTestConfiguration();

        LegacyRspDataPacket.setInterface(this);
    }

    public void fillTQAInfo() {
        NumberFormat numFormat = NumberFormat.getNumberInstance();
        AnalyteModel analyteModel = new AnalyteModel();
        HostConfiguration hc = new HostConfigurationModel().getUnmanagedHostConfiguration();
        ReaderTQAInfo readerTQAInfo = new ReaderTQAInfo();

        readerTQAInfo.setTQADateTime(DateUtil.now());
        readerTQAInfo.setTopHeaterFloat(mRsrMsg.hwStatus.getBGETopHeaterTemp());
        readerTQAInfo.setBottomHeaterFloat(mRsrMsg.hwStatus.getBGEBottomHeaterTemp());
        readerTQAInfo.setAmbientFloat(mRsrMsg.hwStatus.getAmbientTemp1());

        readerTQAInfo.setReaderSerialNumber(mTestRecord.getReader().getSerialNumber());
        readerTQAInfo.setOperatorId(mTestRecord.getUser().getUserId());
        readerTQAInfo.setBatteryLevel(numFormat.format(mRsrMsg.hwStatus.getBatteryLevel()));

        //** Version = NULL readerTQAInfo.mSensorConfig = mTestConfiguration.ConfigVersion.Version.toString();
        readerTQAInfo.setHostSerialNumber(mHost.getSerialNumber());
        readerTQAInfo.setHostVersion(mHost.getTestVersion());
        readerTQAInfo.setReaderHardware(mDeviceIDResponse.getLegacyInfo().getHardwareId());
        readerTQAInfo.setReaderMechanical(mDeviceIDResponse.getLegacyInfo().getMechanicId());
        readerTQAInfo.setReaderSWVersion(mDeviceIDResponse.getLegacyInfo().getReaderFirmwareVersion());
        readerTQAInfo.setSIBVersion(mDeviceIDResponse.getLegacyInfo().getSibId());

        if (((Math.abs(readerTQAInfo.getTopHeaterFloat() - readerTQAInfo.getBottomHeaterFloat())) > 0.3) ||
                (Math.abs(readerTQAInfo.getAmbientFloat() -
                        ((readerTQAInfo.getBottomHeaterFloat() + readerTQAInfo.getTopHeaterFloat()) / 2)) > 1)) {
            readerTQAInfo.setPassed(false);
            readerTQAInfo.setPassFail("Pass");
        } else {
            readerTQAInfo.setPassed(true);
            readerTQAInfo.setPassFail("Fail");
        }

        readerTQAInfo.setAmbientPressure(numFormat.format(TestDataFormatConvert.upConvertAnalyteDataDefaultUnit(AnalyteName.pO2,
                mRsrMsg.hwStatus.getBarometricPressureSensor(), analyteModel.getAnalyte(AnalyteName.pO2).getUnitType().value)));

        readerTQAInfo.setAmbientTemperature((mTestRecord.getTestDetail().getAmbientTemperatureType() == Temperatures.C) ?
                numFormat.format(readerTQAInfo.getAmbientFloat()) + " " + mTestRecord.getTestDetail().getAmbientTemperatureType() :
                numFormat.format(TestDataFormatConvert.CToF(readerTQAInfo.getAmbientFloat())) + " "
                        + mTestRecord.getTestDetail().getAmbientTemperatureType());

        readerTQAInfo.setBottomHeater((hc.getTemperatureUnit() == Temperatures.C) ?
                numFormat.format(readerTQAInfo.getBottomHeaterFloat()) + " " + hc.getTemperatureUnit() :
                numFormat.format(TestDataFormatConvert.CToF(readerTQAInfo.getBottomHeaterFloat())) + " " + hc.getTemperatureUnit());

        readerTQAInfo.setTopHeater((hc.getTemperatureUnit() == Temperatures.C) ?
                numFormat.format(readerTQAInfo.getTopHeaterFloat()) + " " + hc.getTemperatureUnit() :
                numFormat.format(TestDataFormatConvert.CToF(readerTQAInfo.getTopHeaterFloat())) + " " + hc.getTemperatureUnit());

    }

    public QCLockoutHandlerReturnCode QCScheduleLockoutCheck(List<AnalyteOption> analyteOptions, String[] feedBackMessages) {
        List<AnalyteName> analytes = new ArrayList<>();
        for (int i = 0; i < analyteOptions.size(); i++) {
            analytes.add(analyteOptions.get(i).getAnalyteName());
        }
        return QCLockoutHandlerReturnCode.None;
    }

    public List<AnalyteOption> getAllEnabledMeasuredTest() {
        //ArrayList analytes = GlobalConfiguration.Instance.AnalyteConfiguration.RecordList.OrderBy((a)=> a.DisplayOrder).ToList();

        List<AnalyteOption> options = new ArrayList<>();
//        for (int i = 0; i < analytes.size(); i++)
//        {
//            if (analytes.get(i).getOptionType().value == AnalyteType.Measured.value &&
//                    analytes.get(i).getOptionType() != EnabledSelectedOptionType.Disabled)
//            {
//                AnalyteOption option = new AnalyteOption();
//                option.setAnalyteName(analytes.get(i).getAnalyteName());
//                option.setDisplayOrder(analytes.get(i).getDisplayorder());
//                option.setOptionType(analytes.get(i).getOptionType());
//                options.add(option);
//            }
//        }
        return options;
    }

    public boolean isPressureSensorsPassedQC(float sensor1, float sensor2) {
        // no 2nd pressure sensor
        if (Float.isNaN(sensor2)) {
            return true;
        } else {
            return !(Math.abs(sensor2 - sensor1) > (Math.min(sensor1, sensor2) * Math.sqrt(2) * 0.01));
        }
    }

    public boolean IsReaderCompatible() {
        return LegacyVersionManager.IsReaderCompatible(
                getDeviceIdInfoData().getLegacyDeviceInfo().getReaderFirmwareVersion());
    }

    public boolean CompareToReaderVersionWithStatistics() {
        EpocVersion readerVersion = new EpocVersion(getDeviceIdInfoData().getLegacyDeviceInfo().getReaderFirmwareVersion());

        return readerVersion.compareTo(EpocVersion.readerVersionWithStatistics);
    }

    public boolean DetermineWhetherReaderRequiresMaintenance(TestDataProcessor testProcessor) {
        return LegacyRspStatistics.determineReaderMaintenanceFailure(mNumFailedOutOfHowMany,
                testProcessor.mStatisticsArrayList, mNumFailedLimit);
    }

    public void fillReqConfig1_2(LegacyReqConfig1_2 conMsg) {
        mSelfCheckSequence = new ArrayList<>();

        conMsg.selfCheckVersion.major = (byte) mTestConfiguration.SelfCheckConfigSetting.SelfCheckVersion.Major;
        conMsg.selfCheckVersion.minor = (byte) mTestConfiguration.SelfCheckConfigSetting.SelfCheckVersion.Minor;
        conMsg.selfCheckVersion.revision = (byte) mTestConfiguration.SelfCheckConfigSetting.SelfCheckVersion.Revision;

        conMsg.qcLimits.setPotentiometricLow((float) mTestConfiguration.SelfCheckConfigSetting.QCLimitPotentiometricLow);
        conMsg.qcLimits.setPotentiometricHigh((float) mTestConfiguration.SelfCheckConfigSetting.QCLimitPotentiometricHigh);
        conMsg.qcLimits.setAmperometricLow((float) mTestConfiguration.SelfCheckConfigSetting.QCLimitAmperometricLow);
        conMsg.qcLimits.setAmperometricHigh((float) mTestConfiguration.SelfCheckConfigSetting.QCLimitAmperometricHigh);
        conMsg.qcLimits.setConductivityLow((float) mTestConfiguration.SelfCheckConfigSetting.QCLimitConductivityLow);
        conMsg.qcLimits.setConductivityHigh((float) mTestConfiguration.SelfCheckConfigSetting.QCLimitConductivityHigh);
        conMsg.qcLimits.setGroundLow((float) mTestConfiguration.SelfCheckConfigSetting.QCLimitGroundLow);
        conMsg.qcLimits.setGroundHigh((float) mTestConfiguration.SelfCheckConfigSetting.QCLimitGroundHigh);

        conMsg.bdMode = LegacyBubbleDetectMode.convert((byte) mTestConfiguration.SelfCheckConfigSetting.BubbleDetectSetting.BDMode);
        conMsg.setBdFrequency((int) mTestConfiguration.SelfCheckConfigSetting.BubbleDetectSetting.BDFrequency);

        conMsg.adcConfig.setFilterOrder((byte) mTestConfiguration.SelfCheckConfigSetting.ADCConfigSetting.DAMADCFilterOrder);
        conMsg.adcConfig.setInputBuffer((byte) mTestConfiguration.SelfCheckConfigSetting.ADCConfigSetting.DAMADCInputBuffer);
        conMsg.adcConfig.setPolarityMode((byte) mTestConfiguration.SelfCheckConfigSetting.ADCConfigSetting.DAMADCPolarityMode);
        conMsg.adcConfig.setVDACOffset((int) mTestConfiguration.SelfCheckConfigSetting.ADCConfigSetting.DAMADCVDACOffset);
        conMsg.adcConfig.setPGA((byte) mTestConfiguration.SelfCheckConfigSetting.ADCConfigSetting.DAMADCPGA);

        conMsg.setSelfCheckDuration((byte) mTestConfiguration.SelfCheckConfigSetting.Duration);
        conMsg.setSampleFrequency((float) mTestConfiguration.SelfCheckConfigSetting.SamplingInfoSetting.SamplingFrequency);
        conMsg.setAnalogClock((byte) mTestConfiguration.SelfCheckConfigSetting.SamplingInfoSetting.AnalogClock);
        conMsg.setNumSamplesPerChannel((byte) mTestConfiguration.SelfCheckConfigSetting.SamplingInfoSetting.SamplesPerChannel);
        conMsg.setSequenceLength((byte) mTestConfiguration.SelfCheckConfigSetting.SequenceLength);

        for (int i = 0; i < mTestConfiguration.MiscellaneousExtraInfoSet.size(); i++) {
            conMsg.extraInfo.add(mTestConfiguration.MiscellaneousExtraInfoSet.get(i));
        }

        conMsg.setNumLimitKeys((byte) mTestConfiguration.LimitKeySet.size());

        for (int i = 0; i < conMsg.getNumLimitKeys(); i++) {
            conMsg.limitKeys[i] = (float) (mTestConfiguration.LimitKeySet.get(i).LimitKeyValue);
        }

        conMsg.setNumChannelKeys((byte) mTestConfiguration.ChannelLimitSet.size());

        for (int i = 0; i < conMsg.getNumChannelKeys(); i++) {
            conMsg.channelLimits[i] = (byte) (mTestConfiguration.ChannelLimitSet.get(i).LimitKey);
        }

        conMsg.setNumBlocks((byte) mTestConfiguration.SelfCheckConfigSetting.SequenceBlocks.size());

        for (int i = 0; i < conMsg.getNumBlocks(); i++) {
            conMsg.blockSizes[i] = (byte) mTestConfiguration.SelfCheckConfigSetting.SequenceBlocks.get(i).BlockLength;
        }

        getSelfCheckSequence(mSelfCheckSequence);
        conMsg.setSequenceLength((byte) mSelfCheckSequence.size());

        // copy self check sequence
        for (int i = 0; i < mSelfCheckSequence.size(); i++) {
            conMsg.sampleSequence[i] = mSelfCheckSequence.get(i);
        }

    }

    public boolean sendQCInfo(TestDataProcessor dataProcessor, EqcInfo eqcInfo) {
        // if the only problem is that the card was in the reader. then act as if this self test never happened
        if (dataProcessor.mRsrMsg.isCardInReader()) {
            return false;
        }

        if (eqcInfo == null) {
            eqcInfo = new EqcInfo();
        }

        // save the date/time of the last selftest.. if the selftest passed.. its good for checking later
        // if the self test didnt pass, then we disconnected anyway and wont be able to get to the card
        // inserted in order to use this information
        setLastSelfTestTime(DateUtil.now().getTime());
        byte errorCodeFromRSR = dataProcessor.mRsrMsg.getErrorCode();
        // this should fix the problem of the self test failing but looking like it passed
        // rsrmsg.errorcode is readonly
        if (dataProcessor.mRsrMsg.hwStatus.getSelfCheckResults()[0] == 0) {
            errorCodeFromRSR |= LegacyRspStatus.ErrorCode120.SIBFailure.value;
        }

        StringBuilder selfTestResult = new StringBuilder();
        for (int i = 0; i < dataProcessor.mRsrMsg.hwStatus.getSelfCheckResults().length; i++) {
            selfTestResult.append(dataProcessor.mRsrMsg.hwStatus.getSelfCheckResults()[i] < 16 ?
                    "0" + mRsrMsg.hwStatus.getSelfCheckResults()[i] :
                    dataProcessor.mRsrMsg.hwStatus.getSelfCheckResults()[i]);
        }

        AnalyteModel analyteModel = new AnalyteModel();
        Analyte analyte = analyteModel.getAnalyte(AnalyteName.pCO2);

        NumberFormat formatter = new DecimalFormat("#0.0");
        eqcInfo.setAmbientPressure((Double.parseDouble(
                formatter.format(dataProcessor.mRsrMsg.hwStatus.getBarometricPressureSensor1()))));
        eqcInfo.setPressureType(((analyte.getUnitType() == Units.mmhg) ? PressureType.mmHg : PressureType.kPa));

        eqcInfo.setAmbientTemperature((getTestRecord().getTestDetail().getAmbientTemperatureType() == Temperatures.C) ?
                dataProcessor.mRsrMsg.hwStatus.getAmbientTemp1() :
                TestDataFormatConvert.CToF(dataProcessor.mRsrMsg.hwStatus.getAmbientTemp1()));

        eqcInfo.setBatteryLevel(dataProcessor.mRsrMsg.hwStatus.getBatteryLevel());
        eqcInfo.setCreated(getLastSelfTestTime());
        eqcInfo.setHasPassed(errorCodeFromRSR == 0);
        eqcInfo.setHost(getTestRecord().getHost());

        eqcInfo.setReader(getTestRecord().getReader());
        eqcInfo.setReturnCode(errorCodeFromRSR);
        eqcInfo.setUser(getTestRecord().getUser());
        eqcInfo.setUserOther(getTestRecord().getGuestName().isEmpty() ? ""
                : getTestRecord().getGuestName());
        eqcInfo.setSelfTestResult(selfTestResult.toString().equals("FF"));
        eqcInfo.mTemperatureType = getTestRecord().getTestDetail().getPatientTemperatureType();

        for (int i = 0; i < dataProcessor.mReaderQCInfo.selfTestValues.size(); i++) {
            final SelfTestPacketInfo packetInfo = dataProcessor.mReaderQCInfo.selfTestValues.get(i);
            StringBuilder tempValue = new StringBuilder();
            for (int j = 0; j < packetInfo.values.size(); j++) {
                tempValue.append(j).append("=").append(packetInfo.values.get(j)).append(";");
            }

            eqcInfo.mValues.add(new EqcValue(Integer.parseInt(packetInfo.getPacketNumber()), tempValue.toString()));
        }

        return true;
    }

    private Integer getTestSequenceSize() {
        return mTestSequence.size();
    }

    private List<Sequence> getTestSequence() {
        return mTestSequence;
    }

    private float mBatteryCriticalLevel = 5;

    public boolean checkReaderBatteryLevel() {
        return !(mRsrMsg.hwStatus.getBatteryLevel() < mBatteryCriticalLevel);
    }

    public void getTimers(LegacyReqConfig1_1 conMsg) {
        conMsg.generalConfig.setReadyTimer((byte) mTestConfiguration.ReaderConfigSetting.ReaderTimerSetting.ReadyTimer);
        conMsg.generalConfig.setHandleTurningTimer((byte) mTestConfiguration.ReaderConfigSetting.ReaderTimerSetting.HandleTurningTimer);
        conMsg.generalConfig.setCardRemovingTimer((byte) mTestConfiguration.ReaderConfigSetting.ReaderTimerSetting.CardRemovingTimer);
    }

    private boolean getSelfCheckSequence(ArrayList<Sequence> selfCheckSequence) {
        for (int j = 0; j < mTestConfiguration.SelfCheckConfigSetting.SequenceBlocks.size(); j++) {
            List<SequenceItem> selfConfigSeq = mTestConfiguration.SelfCheckConfigSetting.SequenceBlocks.get(j).Sequences;
            for (int i = 0; i < selfConfigSeq.size(); i++) {
                ChannelConfig configSetting = selfConfigSeq.get(i).ChannelConfigSetting;

                if (configSetting != null) {
                    Sequence newSample = new Sequence();

                    newSample.ChannelType = configSetting.ChannelType;
                    newSample.SensorType = (byte) 0;
                    newSample.SensorDescriptorNumber = (byte) 0;
                    newSample.Inputs = configSetting.Inputs;
                    newSample.MUXControl = configSetting.MuxControl;
                    newSample.ADCMUX = configSetting.ADCMUX;
                    newSample.VAPP1 = (short) configSetting.Vapp1;
                    newSample.VAPP2 = (short) configSetting.Vapp2;
                    newSample.VAPP3 = (short) configSetting.Vapp3;
                    newSample.VAPP4 = (short) configSetting.Vapp4;
                    newSample.Inputs2 = configSetting.Inputs2;
                    newSample.NumSamples = configSetting.NumSamples;

                    selfCheckSequence.add(newSample);
                } else {
                    // once we hit the first no-show, we leave
                    return false;
                }
            }
        }
        return true;
    }

    public void getDryCardCheckData(LegacyReqConfig1_3 theConfigMsg) {
        theConfigMsg.transMode = DryCardTransmissionMode.convert((byte) mTestConfiguration.DryCardCheckConfigSetting.TransmissionMode);
        theConfigMsg.qcLimits.setAmperometricLow((float) mTestConfiguration.DryCardCheckConfigSetting.QCLimitAmperometricLow);
        theConfigMsg.qcLimits.setThirtyKLow((float) mTestConfiguration.DryCardCheckConfigSetting.QCLimit30KLow);
        theConfigMsg.qcLimits.setAmperometricHigh((float) mTestConfiguration.DryCardCheckConfigSetting.QCLimitAmperometricHigh);
        theConfigMsg.qcLimits.setThirtyKHigh((float) mTestConfiguration.DryCardCheckConfigSetting.QСLimit30KHigh);

        theConfigMsg.bdMode = BubbleDetectMode.convert((byte) mTestConfiguration.DryCardCheckConfigSetting.BubbleDetectSetting.BDMode);
        theConfigMsg.setBdFrequency((int) mTestConfiguration.DryCardCheckConfigSetting.BubbleDetectSetting.BDFrequency);

        theConfigMsg.setDryCardAirThreshold((float) mTestConfiguration.DryCardCheckConfigSetting.DryCardAirThreshold);
        theConfigMsg.setDryCardFluidThreshold((float) mTestConfiguration.DryCardCheckConfigSetting.DryCardFluidThreshold);

        theConfigMsg.adcConfig.setFilterOrder((byte) mTestConfiguration.DryCardCheckConfigSetting.ADCConfigSetting.DAMADCFilterOrder);
        theConfigMsg.adcConfig.setInputBuffer((byte) mTestConfiguration.DryCardCheckConfigSetting.ADCConfigSetting.DAMADCInputBuffer);
        theConfigMsg.adcConfig.setPolarityMode((byte) mTestConfiguration.DryCardCheckConfigSetting.ADCConfigSetting.DAMADCPolarityMode);
        theConfigMsg.adcConfig.setVDACOffset((int) mTestConfiguration.DryCardCheckConfigSetting.ADCConfigSetting.DAMADCVDACOffset);
        theConfigMsg.adcConfig.setPGA((byte) mTestConfiguration.DryCardCheckConfigSetting.ADCConfigSetting.DAMADCPGA);

        theConfigMsg.setDryCardCheckDuration((byte) mTestConfiguration.DryCardCheckConfigSetting.Duration);

        theConfigMsg.setSampleFrequency((float) mTestConfiguration.DryCardCheckConfigSetting.SamplingInfoSetting.SamplingFrequency);
        theConfigMsg.setAnalogClock((byte) mTestConfiguration.DryCardCheckConfigSetting.SamplingInfoSetting.AnalogClock);
        theConfigMsg.setNumSamplesPerChannel((byte) mTestConfiguration.DryCardCheckConfigSetting.SamplingInfoSetting.SamplesPerChannel);

        theConfigMsg.setSequenceLength((byte) mTestConfiguration.DryCardCheckConfigSetting.SequenceLength);


        for (int i = 0; i < mTestConfiguration.MiscellaneousExtraInfoSet.size(); i++) {
            theConfigMsg.addExtraInfo(mTestConfiguration.MiscellaneousExtraInfoSet.get(i).name,
                    mTestConfiguration.MiscellaneousExtraInfoSet.get(i).type,
                    mTestConfiguration.MiscellaneousExtraInfoSet.get(i).value);
        }

        theConfigMsg.setNumBlocks((byte) mTestConfiguration.DryCardCheckConfigSetting.SequenceBlocks.size());
        theConfigMsg.blockSizes = new byte[theConfigMsg.getNumBlocks()];

        for (int i = 0; i < theConfigMsg.getNumBlocks(); i++) {
            theConfigMsg.blockSizes[i] = (byte) mTestConfiguration.DryCardCheckConfigSetting.SequenceBlocks.get(i).BlockLength;
        }

        theConfigMsg.dryCardCheckVersion.major = (byte) mTestConfiguration.DryCardCheckConfigSetting.DryCheckSequenceVersion.Major;
        theConfigMsg.dryCardCheckVersion.minor = (byte) mTestConfiguration.DryCardCheckConfigSetting.DryCheckSequenceVersion.Minor;
        theConfigMsg.dryCardCheckVersion.revision = (byte) mTestConfiguration.DryCardCheckConfigSetting.DryCheckSequenceVersion.Revision;

    }

    public void getDryCardCheckSequence(ArrayList dryCardSequence) {
        List<SequenceItem> selfConfigSeq = mTestConfiguration.DryCardCheckConfigSetting.SequenceBlocks.get(0).Sequences;
        for (int i = 0; i < selfConfigSeq.size(); i++) {
            ChannelConfig configSetting = selfConfigSeq.get(i).ChannelConfigSetting;

            if (configSetting != null) {
                Sequence newSample = new Sequence();

                newSample.ChannelType = configSetting.ChannelType;
                newSample.SensorType = (byte) 0;
                newSample.SensorDescriptorNumber = (byte) 0;
                newSample.Inputs = configSetting.Inputs;
                newSample.Inputs = 126;
                newSample.MUXControl = configSetting.MuxControl;
                newSample.ADCMUX = configSetting.ADCMUX;
                newSample.VAPP1 = (short) configSetting.Vapp1;
                newSample.VAPP2 = (short) configSetting.Vapp2;
                newSample.VAPP3 = (short) configSetting.Vapp3;
                newSample.VAPP4 = (short) configSetting.Vapp4;
                newSample.Inputs2 = configSetting.Inputs2;
                newSample.Inputs2 = 0;
                newSample.NumSamples = configSetting.NumSamples;

                dryCardSequence.add(newSample);
            } else
                return;
        }
    }

    public boolean getTestConfigData() {
        loadTestConfiguration();

        mConfigMsg1_4 = new LegacyReqConfig1_4();
        mConfigMsg1_4.setTransMode(TransmissionMode.convert((byte) mTestConfiguration.ReaderConfigSetting.TransmissionMode));
        mConfigMsg1_4.bdConfig.BDMode = BubbleDetectMode.convert(mTestConfiguration.ReaderConfigSetting.BubbleDetectSetting.BDMode);
        mConfigMsg1_4.bdConfig.setBDFrequency((int) mTestConfiguration.ReaderConfigSetting.BubbleDetectSetting.BDFrequency);
        mConfigMsg1_4.bdConfig.setCalInitTime((byte) mTestConfiguration.ReaderConfigSetting.ReaderTimerSetting.CalibrationInitTime);
        mConfigMsg1_4.bdConfig.setAirInitThreshold((float) mTestConfiguration.ReaderConfigSetting.AirInitThreshold);
        mConfigMsg1_4.bdConfig.setFluidInitThreshold((float) mTestConfiguration.ReaderConfigSetting.FluidInitThreshold);
        mConfigMsg1_4.bdConfig.setAirAfterFluidThreshold((float) mTestConfiguration.ReaderConfigSetting.AirAfterFluidThreshold);
        mConfigMsg1_4.bdConfig.setFluidAfterFluidThreshold((float) mTestConfiguration.ReaderConfigSetting.FluidAfterFluidThreshold);

        mConfigMsg1_4.adcConfig.setFilterOrder((byte) mTestConfiguration.ReaderConfigSetting.ADCConfigSetting.DAMADCFilterOrder);
        mConfigMsg1_4.adcConfig.setInputBuffer((byte) mTestConfiguration.ReaderConfigSetting.ADCConfigSetting.DAMADCInputBuffer);
        mConfigMsg1_4.adcConfig.setPolarityMode((byte) mTestConfiguration.ReaderConfigSetting.ADCConfigSetting.DAMADCPolarityMode);
        mConfigMsg1_4.adcConfig.setVDACOffset((int) mTestConfiguration.ReaderConfigSetting.ADCConfigSetting.DAMADCVDACOffset);
        mConfigMsg1_4.adcConfig.setPGA((byte) mTestConfiguration.ReaderConfigSetting.ADCConfigSetting.DAMADCPGA);

        mConfigMsg1_4.stagesTimers.setCalibrationExpiryTimer((short) mTestConfiguration.ReaderConfigSetting.ReaderTimerSetting.CalibrationExpiry);
        mConfigMsg1_4.stagesTimers.setSampleIntroductionTimer((short) mTestConfiguration.ReaderConfigSetting.ReaderTimerSetting.SampleIntroductionExpiry);

        if (mTestRecord.getTestMode() == TestMode.BloodTest) {
            mConfigMsg1_4.stagesTimers.setSampleCollectionTimer((short) mTestConfiguration
                    .ReaderConfigSetting.ReaderTimerSetting.SampleCollectionExpiry);
        } else {
            mConfigMsg1_4.stagesTimers.setSampleCollectionTimer((short) mTestConfiguration
                    .ReaderConfigSetting.ReaderTimerSetting.SampleCollectionExpiryAqueous != 0 ?
                    (short) mTestConfiguration.ReaderConfigSetting.ReaderTimerSetting.SampleCollectionExpiryAqueous
                    : (short) mTestConfiguration.ReaderConfigSetting.ReaderTimerSetting.SampleCollectionExpiry);
        }

        mConfigMsg1_4.topHeating = mTestConfiguration.ReaderConfigSetting.TopHeaterSetting;
        mConfigMsg1_4.bottomHeating = mTestConfiguration.ReaderConfigSetting.BottomHeaterSetting;
        mConfigMsg1_4.setSampleFrequency((float) mTestConfiguration.ReaderConfigSetting.SamplingInfo.SamplingFrequency);
        mConfigMsg1_4.setAnalogClock((byte) mTestConfiguration.ReaderConfigSetting.SamplingInfo.AnalogClock);
        mConfigMsg1_4.setNumSamplesPerChannel((byte) mTestConfiguration.ReaderConfigSetting.SamplingInfo.SamplesPerChannel);

//        byte seqLength = 0;
//        for (int i = 0; i < 100; i++) {
//            DataRow samplingRow = GlobalConfiguration.Instance.SensorConfiguration.Record.testSampling.Rows.Find(new object[]{cardType, sensorLayout, i});
//
//            if (samplingRow != null) {
//                seqLength++;
//            }
//        }
        mConfigMsg1_4.setSequenceLength((byte) mTestConfiguration.TestConfigSetting.SensorLayouts.size());

        boolean sendExtraBytes = true;

        for (String readerVersionsNoExtraByte : readerVersionsNoExtraBytes) {
            if (readerVersionsNoExtraByte.equals(mDeviceIDResponse.getLegacyInfo().getReaderFirmwareVersion())) {
                sendExtraBytes = false;
                break;
            }
        }

        if (sendExtraBytes) {
            for (int i = 0; i < mTestConfiguration.MiscellaneousExtraInfoSet.size(); i++) {
                mConfigMsg1_4.addExtraInfo(mTestConfiguration.MiscellaneousExtraInfoSet.get(i).name,
                        mTestConfiguration.MiscellaneousExtraInfoSet.get(i).type,
                        mTestConfiguration.MiscellaneousExtraInfoSet.get(i).value);
            }
        }

        mConfigMsg1_4.setNumBlocks((byte) mTestConfiguration.TestConfigSetting.SequenceBlocks.size());
        mConfigMsg1_4.blockSizes = new byte[mConfigMsg1_4.getNumBlocks()];

        for (int i = 0; i < mConfigMsg1_4.getNumBlocks(); i++) {
            mConfigMsg1_4.blockSizes[i] = (byte) mTestConfiguration.TestConfigSetting.SequenceBlocks.get(i).BlockLength;
        }

        mConfigMsg1_4.testSequenceVersion.major = (byte) mTestConfiguration.TestConfigSetting.testSequenceVersion.Major;
        mConfigMsg1_4.testSequenceVersion.minor = (byte) mTestConfiguration.TestConfigSetting.testSequenceVersion.Minor;
        mConfigMsg1_4.testSequenceVersion.revision = (byte) mTestConfiguration.TestConfigSetting.testSequenceVersion.Revision;
        return true;

    }

    private void loadTestSequence() {
        //loadTestConfiguration();
        LegacyRspDataPacket.setInterface(this);     //update testDataProcesser object in LegacyRspDataPacket
        mTestConfiguration.getTestSequence(mSensorLayout, mTestSequence, mBarcodeInformation.getCardMade());
    }

    private void getSensorInfo(ArrayList<ChannelTypeToSensorType> channelMapping) {
        mTestConfiguration.getSensorInfo(mSensorLayout, mSensorDescriptors, channelMapping
                , mBarcodeInformation.getCardMade());
    }


    public void closePacketFile() {
        if (mLogFile.mLogZipFile != null) {
            mRxUtilLogFile.unsubscribe();
            mLogFile.close();
        }
    }

    private void SetPreviousTestStatus() {
        if (mErrorCode == HostErrorCode.NoError)
            previousTestStatus = TestStatus.Success;
        else if (mErrorCode == HostErrorCode.ExpiredCard)
            previousTestStatus = TestStatus.Expired;
        else if (mErrorCode == HostErrorCode.RealtimeQCFailedDuringFluidics)
            previousTestStatus = TestStatus.iQC;
        else {
            previousTestStatus = TestStatus.Incomplete;
        }
    }

    private void resetAdditionalVariables() {
        mCurrentTimeZoneStandardName = "";
        mCurrentTimeZoneUTC = "";
    }

//    public void legacyResetTestRecord(boolean preservedata)
//    {
//        // this shouldn't be needed. todo, keep in mind
//        // if (TestRecord.Id != 0) FinalSaveTestRecord();
//        String patid = mTestRecord.getSubjectId();
//        BloodSampleType stype = mTestRecord.getTestDetail().getSampleType();
//        PatientIDEntryMethod patIdentryM = mTestRecord.getTestDetail().getPatientIDEntryMethod();
//        blockSavingTestRecord = true;
//        // mandatory
//        if (preservedata) getTestRecord().setId(0);
//        mTestRecord.getTestResults().clear();
//        mTestRecord.setTestErrorCode(TestErrorCode.NoError);
//        mTestRecord.setStatus(TestStatus.Unknown);
//        mTestRecord.setRejected(false);
//        mTestRecord.setSyncState(SyncState.Unknown);
//        mTestRecord.setTestDateTime(EpocTime.now());
//        //testRecord.ReaderConnected = true;
//        mTestRecord.setReportable(false);
//        //mTestRecord.setTestMode(mTestMode);
//        HostConfiguration hc = new HostConfigurationModel().getUnmanagedHostConfiguration();
//        mTestRecord.getTestDetail().setPatientTemperatureType(hc.getTemperatureUnit());
//        mTestRecord.getTestDetail().setAmbientPressure((double)0);
//        mTestRecord.getTestDetail().setAmbientTemperature((double)0);
//        mTestRecord.getTestDetail().setDuration((double)0);
//        //mTestRecord.setWorkflowItems(PrepareTestVariableListForObject(mTestRecord.getTestMode()));
//        WorkflowRepository wfr = new WorkflowRepository();
//        mTestRecord.setWorkflowItems(wfr.getItemizedActiveWorkflow());
//        wfr.setWorkflowValuesIntoTestRecord(mTestRecord);
//        mTestRecord.setSensorConfigVersion(hc.getSensorConfigVersion());
//        mTestRecord.setLastEqcDateTime(mLastSelfTestTime);
//        //copy from resetTestRecord
//        // future: implement new realmentity: TestRecordReaderQCInfo and set here
//        // mTestRecord.setTestRecordReaderQCInfo(null);
//
//        if (!preservedata)
//        {
//            // preserve qa test type regardless
//            if (mTestRecord.getTestMode() == TestMode.BloodTest)
//            {
//                mTestRecord.setType(TestType.Blood);
//            }
//            mTestRecord.setId2("");
//            mTestRecord.setSubjectId("");
//            mTestRecord.setPatientAge(-1);
//            mTestRecord.getTestDetail().setSampleType(BloodSampleType.Unspecified);
//            mTestRecord.setGender(Gender.ENUM_UNINITIALIZED);
//            mTestRecord.setTestDetail(new TestDetail());
//            mTestRecord.getTestDetail().setQaSampleInfo(new QASampleInfoModel().getUnmanagedSampleInfo(QAFluidType.None));
//            mTestRecord.setRespiratoryDetail(new RespiratoryDetail());
//            mTestRecord.getTestDetail().setPatientIDEntryMethod(PatientIDEntryMethod.None);
//            //defaultTestRecord(mTestRecord);
//            new SelenaModel().setupTestEnablement(mTestRecord);
//        }
//
//        if (hc.isRetainPatientId())
//        {
//            mTestRecord.setSubjectId(patid);
//            mTestRecord.getTestDetail().setPatientIDEntryMethod(patIdentryM);
//        }
//        if (hc.isRetainSampleType())
//            mTestRecord.getTestDetail().setSampleType(stype);
//        //   else : user decided to keep previous data, or this is the configuration setting.
//        applyHemodilutionPolicy(preservedata);
//        blockSavingTestRecord = false;
//    }

    private void legacyPrepareTestSequenceObject() {

        if (mConfigMsg1_4.getSampleFrequency() != 0)
            setTimeIncrement((float) (1.0 / mConfigMsg1_4.getSampleFrequency()));
        else
            setTimeIncrement((float) 0.2);

        //TabPage.MakeCalibrationProgressBar(configMsg.stagesTimers.calibrationExpiryTimer, configMsg.sampleFrequency);
        if (getTestReadings() != null) {
            for (int jcount = 0; jcount < getTestReadings().size(); jcount++) {
                for (int icount = 0; icount < getTestReadings().get(jcount).readings.size(); icount++) {
                    getTestReadings().get(jcount).readings.set(icount, null);
                }
                getTestReadings().get(jcount).readings.clear();
                getTestReadings().get(jcount).readings = null;
                getTestReadings().set(jcount, null);
            }
            getTestReadings().clear();
        }

        if (getTestSequence() != null) {
            for (int icount = 0; icount < getTestSequence().size(); icount++) {
                getTestSequence().set(icount, null);
            }
            getTestSequence().clear();
        }

        loadTestSequence();

        // get sensor descriptor info
        ArrayList<ChannelTypeToSensorType> channelMapping = new ArrayList<>();
        getSensorInfo(channelMapping);

        mConfigMsg1_4.setSequenceLength(getTestSequenceSize().byteValue());

        List<AnalyteOption> testDefaults = getTestDefaultsForOneCardType();

        // copy test sequence
        for (int i = 0; i < mTestSequence.size(); i++) {
            mConfigMsg1_4.sampleSequence[i] = mTestSequence.get(i);

            if (mTestSequence.get(i).ChannelType == ChannelType.Conductivity.value) {
                setNumConductivityPlus();
            }

            int j;
            for (j = 0; j < getTestReadings().size(); j++) {
                // if its the same sensortype and descriptor number as another type of reading.. just add 1
                // to the num of this type
                if ((getTestReadings().get(j).channelType.value == mTestSequence.get(i).ChannelType)
                        && (mTestSequence.get(i).SensorType != 0)) {
                    getTestReadings().get(j).numThisTypeReading++;
                    mTestSequence.get(i).AddToWhichReading = j;
                    break;
                }
            }

            // if we hit the end and didnt find.. create a new sensorreading object
            if (j == getTestReadings().size()) {
                SensorReadings newReading = new SensorReadings();
                newReading.numThisTypeReading = 1;
                newReading.channelType = ChannelType.convert((int) mTestSequence.get(i).ChannelType);
                newReading.sensorType = Sensors.convert((int) mTestSequence.get(i).SensorType);
                newReading.analyte = TestDataFormatConvert.SensorToAnalyte(newReading.sensorType);
                newReading.analyteString = newReading.analyte.toString();
                newReading.sensorDescriptorNumber = mTestSequence.get(i).SensorDescriptorNumber;
                newReading.checkRealtimeQC = false;

                for (int k = 0; k < testDefaults.size(); k++) {
                    AnalyteName an = TestDataFormatConvert.
                            AnalytesUreatoBUNInternalSwitch(testDefaults.get(k).getAnalyteName());
                    if ((newReading.analyte == an) &&
                            (testDefaults.get(k).getOptionType() == EnabledSelectedOptionType.EnabledSelected)) {
                        newReading.checkRealtimeQC = true;
                        break;
                    }
                }

                if (mTestSequence.get(i).ChannelType >= ChannelType.P1.value &&
                        mTestSequence.get(i).ChannelType <= ChannelType.P7.value) {
                    newReading.multiplicationFactor = 1000;
                } else if ((mTestSequence.get(i).ChannelType >= ChannelType.A1.value &&
                        mTestSequence.get(i).ChannelType <= ChannelType.A4.value) ||
                        (mTestSequence.get(i).ChannelType >= ChannelType.A2_STIP.value &&
                                mTestSequence.get(i).ChannelType <= ChannelType.A4_STIM.value) ||
                        (mTestSequence.get(i).ChannelType >= ChannelType.A1ST0.value &&
                                mTestSequence.get(i).ChannelType <= ChannelType.A4Sti.value)) {
                    newReading.multiplicationFactor = Math.pow(10, 9);
                }

                for (int k = 0; k < mSensorDescriptors.size(); k++) {
                    if ((newReading.sensorType == mSensorDescriptors.get(k).sensorType) &&
                            (newReading.sensorDescriptorNumber == mSensorDescriptors.get(k).sensorDescriptorNumber)) {
                        // save the sensor descriptor
                        newReading.sensorDescriptor = mSensorDescriptors.get(k);
                        break;
                    }
                }

                mTestReadings.add(newReading);

                // save the location of hematocrit and top heater readings
                if (newReading.sensorType == Sensors.Conductivity) {
                    mHematocritReadings = mTestReadings.size() - 1;
                } else if (newReading.sensorType == Sensors.HeaterTop) {
                    mTopHeaterReadings = mTestReadings.size() - 1;
                }
                mTestSequence.get(i).AddToWhichReading = j;
            }
        }

    }

    public void legacyPrepareForNewTest() {
        mUserClosedPage = false;
        mTestState = false;

        SetPreviousTestStatus();
        mTestRecordPersisted = false;
        mTestTime = DateUtil.now();
        getRuntimeTestSelectionInclusions();    //in order to init array
        resetTestRecord(true);
        resetAdditionalVariables();
        mOldTestCalculated = false;
        mTestFirstTimeSaved = false;
        mTestPacketsReceived = 0;
        TestManager.getInstance().setDeviceTestCompletedState(false);
        mErrorsExist = false;
        mExistsFailedIQC = false;
        mTopHeaterPid = new ArrayList<>();
        mBubbleBegin = 0;
        mBubbleEnd = 0;
        mFirstFluid = 0;
        mCalibrationTime = 0;
        mRealTimehctRc = RealTimeHematocritQCReturnCode.NotPerformed;
        mBarcodeString = "";
        mSpeed = 0;

        if (mBarcodeInformation != null) {
            mBarcodeInformation.reset();
        } else {
            mBarcodeInformation = new BarcodeInformation();
        }
        mErrorCode = HostErrorCode.NoError;
        mCalpackets = 0;
        mBubblepackets = 0;
        mSamplepackets = 0;
        mTestPacketsReceived = 0;
        mCardType = 0;
        mSensorLayout = 0;
        mHasTimeStatusMessageBeenReceived = false;
        mBarcode = null;
        mBubbleDetect = 0;
        mSampleDetect = 0;
        mLastRecordedTime = 0;
        mBytesReceived = 0;
        mNumConductivity = 0;
        mCardNumber = "";
        mLocalReaderInfoContentAdded = 0;

        // dont clear the tabpage on the first card.. clear it on every card after that
        try {
            if (mNumTestsRun > 0) {
                int i;

                if (mTestReadings != null) {
                    // this kluge cleans up the recorded readings for when there is a new test
                    for (i = 0; i < mTestReadings.size(); i++) {
                        if (mTestReadings.get(i).readings != null) {
                            mTestReadings.get(i).readings.clear();
                            mTestReadings.get(i).result = Double.NaN;
                        }
                    }
                }
            }
        } catch (Exception ex) {
//            LogServer.Instance.Log(new Log
//            {
//                Context = epoc.common.types.LogContext.Host,
//                        Level = epoc.common.types.LogLevel.Debug,
//                        IsAlert = false,
//                        Message = "TestDataProcessor.PrepareForNewTest: " + ex.ToString(),
//                        MessageExtra = ex.StackTrace
//            });
        }
    }

    public String getBarcodeString() {
        return mBarcodeString;
    }

    public void setBarcodeString(String str) {
        mBarcodeString = str;
    }

    public short getCardType() {
        return mCardType;
    }

    public void setCardType(int cardType) {
        mCardType = (short) cardType;
    }

    public double getmFirstFluid() {
        return mFirstFluid;
    }

    public void setFirstFluid(double v) {
        mFirstFluid = v;
    }


    public void getCardBarcode() {
        getTestRecord().setCardLot(findCardLot(mBarcodeString, mBarcodeInformation, mCardType));

    }

    public boolean isCardTypeSupported(short cardType) {
        return loadTestConfiguration(cardType);
    }

    public void parseDataPacket(LegacyRspDataPacket tempPacket, TestStateDataObject stateDataObject, LegacyTestState state) {
        try {
            int i;
            if (tempPacket.getPacketType() == DataPacketType.TestData) {
                mTestPacketsReceived++;

                // only log packets during the test
//                if (state.getStateID() == TestStateEnum.FluidicsCalibration ||
//                        state.getStateID() == TestStateEnum.SampleIntroduction ||
//                        state.getStateID() == TestStateEnum.SampleProcessing) {
//                    if (mBGETestDataFile == null) {
//                        createBGETestDataFile();
//                    }
//                    mBGETestDataFile.write(mLastRecordedTime + "\t" + tempPacket.getSampleString() + "\r\n");
//                }

                if (tempPacket.getReaderMode() == ReaderMode.eDeviceCalibrationState) {
                    mCalpackets++;
                    //state.RaiseEvent(stateDataObject, TestStateMachineEventReason.CalibrationPacket, null);
                } else if (tempPacket.getReaderMode() == ReaderMode.eDeviceBubbleDetectedState) {
                    mBubblepackets++;

                    //if ((mBubblepackets % (mConfigMsg1_4.mSampleFrequency * 10)) == 0) {
                    //state.RaiseEvent(stateDataObject, TestStateMachineEventReason.BubblePacket, configMsg.sampleFrequency);
                    //}

                } else if (tempPacket.getReaderMode() == ReaderMode.eDeviceSampleCollectionState) {
                    mSamplepackets++;
                    //state.RaiseEvent(stateDataObject, TestStateMachineEventReason.SamplePacket, configMsg.sampleFrequency);
                }

                int numberOfConductivity = 0;

                // for every sample in the sequence
                for (i = 0; i < mTestSequence.size(); i++) {
                    // get the sensor readings object for this part of the sequence
                    SensorReadings tempSR = mTestReadings.get((mTestSequence.get(i).AddToWhichReading));

                    if (tempSR.sensorType.value == (byte) Sensors.Conductivity.value) {
                        numberOfConductivity++;

                        // if just 1 conductivity, put it at the regular time
                        if (tempSR.numThisTypeReading == 1) {
                            Reading newReading = new Reading();
                            newReading.time = mLastRecordedTime;
                            newReading.value = tempPacket.getSamples()[i];
                            tempSR.readings.add(newReading);
                        } else {
                            // more than 1 conductivity. go back 1 timeframe and space them out
                            Reading newReading = new Reading();
                            newReading.time = (mLastRecordedTime - mTimeIncrement +
                                    ((mTimeIncrement / tempSR.numThisTypeReading) * numberOfConductivity));
                            newReading.value = tempPacket.getSamples()[i];
                            tempSR.readings.add(newReading);
                        }
                    } else {
                        // if there are more than 1 of this kind of sensor reading.. and a reading has already been added
                        // for this time.. then just add to it
                        if ((tempSR.numThisTypeReading > 1) && (tempSR.readings.size() > 0)
                                && ((tempSR.readings.get(tempSR.readings.size() - 1)).time == mLastRecordedTime)) {
                            // for now just add.. we'll divide later
                            (tempSR.readings.get(tempSR.readings.size() - 1)).value += tempPacket.getSamples()[i];
                        } else {
                            // otherwise.. its the first of many.. or its just 1
                            Reading newReading = new Reading();
                            newReading.time = mLastRecordedTime;
                            newReading.value = tempPacket.getSamples()[i];
                            tempSR.readings.add(newReading);
                        }
                    }
                }

                // if more than one type of this and its not conductivity
                for (i = 0; i < mTestReadings.size(); i++) {
                    if (((mTestReadings.get(i)).numThisTypeReading > 1) &&
                            ((mTestReadings.get(i)).sensorType.value != (byte) Sensors.Conductivity.value)) {
                        (((mTestReadings.get(i)).readings.get((mTestReadings.get(i)).readings.size()
                                - 1))).value /= (mTestReadings.get(i)).numThisTypeReading;
                    }
                }

                // add the top heater pid
                Reading tempReading = new Reading();
                tempReading.time = mLastRecordedTime;
                tempReading.value = tempPacket.getPid2();
                mTopHeaterPid.add(tempReading);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean exceedMaxCardInReaders()//how many tests we can run at the same time
    {
        return false;
    }

    public int getNumTestsRun() {
        return mNumTestsRun;
    }

    public void setNumTestsRun(int v) {
        mNumTestsRun = v;
    }

    public void setNumTestsRunPlus() {
        mNumTestsRun++;
    }

    public void setHctRC(RealTimeHematocritQCReturnCode v) {
        mRealTimehctRc = v;
    }

    public RealTimeHematocritQCReturnCode getHctRC() {
        return mRealTimehctRc;
    }

    public void setTestReadingsHematocritExtraString(String info) {
        mTestReadings.get(mHematocritReadings).extraString += info + mLastRecordedTime + " ";
    }

    public void setEarlyInjection() {
        mRealTimehctRc = AnalyticalManager.checkForEarlyInjection(mTestReadings.get(mHematocritReadings),
                mTestReadings.get(mTopHeaterReadings), mRealTimehctRc,
                mConfigMsg1_4.bdConfig.getFluidAfterFluidThreshold(), mLastRecordedTime, mFirstFluid).getAmReturnCode();
    }

    @Override
    public int getBlockSize(DataPacketType packetType, int blockNumber) {
        if (packetType == DataPacketType.TestData) {
            return getTestSequenceSize();
        } else if (packetType == DataPacketType.SelfCheck) {
            return mTestConfiguration.SelfCheckConfigSetting.SequenceBlocks.get(blockNumber).BlockLength;
        } else {
            return mTestConfiguration.DryCardCheckConfigSetting.SequenceBlocks.get(blockNumber).BlockLength;
        }
    }

    public boolean legacyDoBubbleDetectDuringSampleIntroduction(LegacyRspDataPacket dataPacket) {
        boolean bubbleBeginSetThisPacket = false;

        // no bubble yet
        if (mBubbleDetect == 0) {
            if ((dataPacket.getDAMMode() == DAMStages.BD_AIR) || (dataPacket.getDAMMode() == DAMStages.SAMPLE_COLLECTION)) {
                mBubbleDetect = DecimalConversionUtil.round(mLastRecordedTime - mTimeIncrement, 1);

                if (dataPacket.getConductivityByte() == mAllFluidConductivityByte) {
                    // this shouldnt happen, but somehow the DAM found a
                    // bubble, but none of the bits in the conductivity
                    // byte were air. so we will pretend the bubble began
                    // at the beginning of the packet
                    mBubbleBegin = mLastRecordedTime - mTimeIncrement;
                } else {
                    byte tempByte = dataPacket.getConductivityByte();
                    int i;

                    for (i = 0; i < mNumConductivity; i++) {
                        // if we keep getting 0s, just keep overwriting the
                        // bubblebegin, becuase we want the last one
                        // unless we've found the end of the bubble, in
                        // which case dont overwrite the old bubble
                        if ((mBubbleBegin == 0) && ((tempByte & (0x01 << i)) == 0)) {
                            mBubbleBegin = mLastRecordedTime - mTimeIncrement + (mTimeIncrement / mNumConductivity) * (i + 1);
                            bubbleBeginSetThisPacket = true;
                        }

                        // if bubble found, Start looking for fluid again
                        if ((mBubbleEnd == 0) && (mBubbleBegin != 0) && ((tempByte & (0x01 << i)) != 0)) {
                            // after a 0, we found a 1 set the bubbleend too
                            mBubbleEnd = mLastRecordedTime - mTimeIncrement + (mTimeIncrement / mNumConductivity) * (i + 1);
                        }
                    }
                }
            }

        }
        return bubbleBeginSetThisPacket;
    }

    public boolean legacyDoSamplingDetectDuringSampleIntroduction(LegacyRspDataPacket dataPacket,
                                                                  boolean bubbleBeginSetThisPacket) {
        // once air has been found.. we need to look for fluid.. even
        // if its in the same byte.. only go into
        if ((!bubbleBeginSetThisPacket) && (mBubbleBegin != 0) &&
                (mBubbleEnd == 0) && (dataPacket.getConductivityByte() != 0)) {
            byte tempByte = dataPacket.getConductivityByte();
            int i;

            // we Start at 1 because we want the leftmost 0. so we Start at
            // position 1. if its a 0,
            for (i = 0; i < mNumConductivity; i++) {
                // go until we find a 1 or we shift over 4 times
                if ((tempByte & (0x01 << i)) != 0) {
                    mBubbleEnd = mLastRecordedTime - mTimeIncrement + (mTimeIncrement / mNumConductivity) * (i + 1);
                    break;
                }
            }
        }

        boolean gotoProcessing = false;
        if (dataPacket.getDAMMode() == DAMStages.SAMPLE_COLLECTION) {
            if (mBubbleDetect == 0) {
                mBubbleDetect = DecimalConversionUtil.round(mLastRecordedTime - mTimeIncrement, 1);
            }

            if (mBubbleEnd == 0) {
                mBubbleEnd = mLastRecordedTime;
            }

            mSampleDetect = DecimalConversionUtil.round(mLastRecordedTime + mTimeIncrement, 1);
            mOneTimeFlag = false;
            gotoProcessing = true;
        }

        if ((mBubbleBegin != 0) && (mBubbleEnd != 0)) {
            double duration = Math.abs(mBubbleEnd - mBubbleBegin);
            if ((DecimalConversionUtil.round(duration, 1)
                    < mShortestGoodInjection) || (DecimalConversionUtil.round(duration, 1) > mLongestGoodInjection)) {
                {
                    if (DecimalConversionUtil.round(duration, 1) < mShortestGoodInjection) {
                        setErrorCode(HostErrorCode.SampleInjectedTooFast);
                    } else {
                        setErrorCode(HostErrorCode.SampleInjectedTooSlowly);
                    }
                    mOneTimeFlag = false;
                    gotoProcessing = true;
                }
            }
        }
        return gotoProcessing;
    }

    //check if TCO2 shown on the test selection panel when runing blood test. it means card type support TCO2, and TCO2 enable in card option 1
    public boolean ifTCO2SupportedByCardTypeAndEnabled() {
        List<AnalyteOption> testDefaults = getTestDefaultsForOneCardType();
        boolean mTCO2SupportedEnabled = false;
        for (int i = 0; i < testDefaults.size(); i++) {
            if ((testDefaults.get(i).getAnalyteName() == AnalyteName.TCO2) &&
                    ((testDefaults.get(i).getOptionType() == EnabledSelectedOptionType.EnabledSelected
                            || testDefaults.get(i).getOptionType() == EnabledSelectedOptionType.EnabledUnselected))) {
                mTCO2SupportedEnabled = true;
            }
        }
        return mTCO2SupportedEnabled;
    }

    public void saveTestRecordWhenError() {
        TestManager.getInstance().setDeviceTestCompletedState(true);
        getTestRecord().setTestErrorCode(TestErrorCode.fromInt(mErrorCode.value));
        // SaveTestRecord(TestRecord);
        finalSaveTestRecord();
    }

    private void finalSaveTestRecord() {
        if (getTestRecord() != null) {
            getTestRecord().setSyncState(SyncState.Unsent);
            enforceTestRecordCount();
        }
    }

    public void legacyCheckSampleInjectionError(final TestStateDataObject stateDataObject, int delayTime) {
        Observable.timer(delayTime, TimeUnit.MILLISECONDS).subscribe(new Consumer() {
            //timer is running in a new thread
            @Override
            public void accept(Object o) throws Exception{
                checkSampleInjectError(stateDataObject);
            }
        });

    }

    private void addSensorCheck() {
        for (int i = 0; i < mTestReadings.size(); i++) {
            if (getTestReadings().get(i).sensorType.value == Sensors.TCO2.value) {
                return; //sensor TCO2 already in the sensor reading list.
            }
        }
        for (int k = 0; k < mSensorDescriptors.size(); k++) {
            if (mSensorDescriptors.get(k).sensorType.value == Sensors.TCO2.value) {
                SensorReadings newReading = new SensorReadings();
                newReading.numThisTypeReading = 1;
                newReading.channelType = mSensorDescriptors.get(k).channelType;
                newReading.sensorType = mSensorDescriptors.get(k).sensorType;
                newReading.analyte = TestDataFormatConvert.SensorToAnalyte(newReading.sensorType);
                newReading.analyteString = newReading.analyte.toString();
                newReading.sensorDescriptorNumber = mSensorDescriptors.get(k).sensorDescriptorNumber;
                newReading.checkRealtimeQC = false;
                newReading.multiplicationFactor = 1;
                newReading.sensorDescriptor.slopeFactor = 1;
                newReading.sensorDescriptor = mSensorDescriptors.get(k);
                mTestReadings.add(newReading);
                break;
            }
        }
    }

    private void enforceTestRecordCount() {
//        var count = epoc.host.data.GlobalDataContext.Instance.DataAccess.Count<epoc.common.TestRecord>("id > 0", null);
//        if (count > epoc.host.configuration.ConstantConfiguration.maximumTestHistory)
//        {
//            var oldest = epoc.host.data.GlobalDataContext.Instance.DataAccess.Find<epoc.common.TestRecord>("id > 0 ORDER BY TestDateTime asc LIMIT 1", null).FirstOrDefault();
//            if (oldest != null)
//            {
//                epoc.host.data.GlobalDataContext.Instance.DataAccess.Delete<epoc.common.TestRecord>(oldest);
//            }
//        }
    }
}
