package com.epocal.epoctest;

import com.epocal.common.androidutil.RxUtil;
import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.TestMode;
import com.epocal.epoctest.di.component.DaggerTestStateComponent;
import com.epocal.epoctest.di.component.TestStateComponent;
import com.epocal.epoctest.di.module.TestStateModule;
import com.epocal.epoctest.testprocess.TestCommunication;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEnum;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.connection.Connected;
import com.epocal.epoctest.teststate.connection.Connecting;
import com.epocal.epoctest.teststate.connection.Disconnected;
import com.epocal.epoctest.teststate.epoctest.EndTestBeforeTestBegun;
import com.epocal.epoctest.teststate.epoctest.GetDeviceId;
import com.epocal.epoctest.teststate.epoctest.GetDeviceStatus;
import com.epocal.epoctest.teststate.epoctest.GetDeviceStatusBeforeTestStart;
import com.epocal.epoctest.teststate.epoctest.SendHostIdInfo;
import com.epocal.epoctest.teststate.epoctest.SetReaderInterface;
import com.epocal.epoctest.teststate.epoctest.TerminateTest;
import com.epocal.epoctest.teststate.epoctest.drycardcheck.PerformDryCardCheck;
import com.epocal.epoctest.teststate.epoctest.drycardcheck.PerformDryCardCheckEnd;
import com.epocal.epoctest.teststate.epoctest.drycardcheck.PerformDryCardCheckStart;
import com.epocal.epoctest.teststate.epoctest.patienttest.AcceptCard;
import com.epocal.epoctest.teststate.epoctest.patienttest.CancelTest;
import com.epocal.epoctest.teststate.epoctest.patienttest.CardInReader;
import com.epocal.epoctest.teststate.epoctest.patienttest.DisableTest;
import com.epocal.epoctest.teststate.epoctest.patienttest.EnablePatientTestSession;
import com.epocal.epoctest.teststate.epoctest.patienttest.EnableTest;
import com.epocal.epoctest.teststate.epoctest.patienttest.GetDeviceStatusAfterTestStart;
import com.epocal.epoctest.teststate.epoctest.patienttest.PerformFluidCalibration;
import com.epocal.epoctest.teststate.epoctest.patienttest.PerformSampleIntroduction;
import com.epocal.epoctest.teststate.epoctest.patienttest.PerformSampleProcessing;
import com.epocal.epoctest.teststate.epoctest.patienttest.PerformTestEnd;
import com.epocal.epoctest.teststate.epoctest.patienttest.PerformTestStart;
import com.epocal.epoctest.teststate.epoctest.patienttest.ReadyTest;
import com.epocal.epoctest.teststate.epoctest.patienttest.UsedCardInReader;
import com.epocal.epoctest.teststate.epoctest.readereqc.DisableReaderEQCSession;
import com.epocal.epoctest.teststate.epoctest.readereqc.EnableReaderEQCSession;
import com.epocal.epoctest.teststate.epoctest.readereqc.PerformReaderEQC;
import com.epocal.epoctest.teststate.epoctest.readereqc.PerformReaderEQCEnd;
import com.epocal.epoctest.teststate.epoctest.readereqc.PerformReaderEQCStart;
import com.epocal.epoctest.teststate.epoctest.readersetting.DisableReaderSettingsSession;
import com.epocal.epoctest.teststate.epoctest.readersetting.EnableReaderSettingsSession;
import com.epocal.epoctest.teststate.epoctest.readersetting.SetReaderDCCConfig;
import com.epocal.epoctest.teststate.epoctest.readersetting.SetReaderEQCConfig;
import com.epocal.epoctest.teststate.epoctest.readersetting.SetReaderGeneralConfig;
import com.epocal.epoctest.teststate.legacyepoctest.ActionAfterInitialConnect;
import com.epocal.epoctest.teststate.legacyepoctest.AfterTestResponseForUpdateReaderStatus;
import com.epocal.epoctest.teststate.legacyepoctest.CheckFirstReaderStatus;
import com.epocal.epoctest.teststate.legacyepoctest.CheckReaderStatus;
import com.epocal.epoctest.teststate.legacyepoctest.EndTestAfterTestBegun;
import com.epocal.epoctest.teststate.legacyepoctest.FluidicsCalibration;
import com.epocal.epoctest.teststate.legacyepoctest.GetReaderId;
import com.epocal.epoctest.teststate.legacyepoctest.LegacyCardInReader;
import com.epocal.epoctest.teststate.legacyepoctest.NewCardInReader;
import com.epocal.epoctest.teststate.legacyepoctest.OldCardInReader;
import com.epocal.epoctest.teststate.legacyepoctest.Ready;
import com.epocal.epoctest.teststate.legacyepoctest.SampleIntroduction;
import com.epocal.epoctest.teststate.legacyepoctest.SampleProcessing;
import com.epocal.epoctest.teststate.legacyepoctest.TestCompleted;
import com.epocal.epoctest.teststate.legacyepoctest.TestCompletedCardRemoved;
import com.epocal.epoctest.teststate.legacyepoctest.WaitingForConfiguration1_1Ack;
import com.epocal.epoctest.teststate.legacyepoctest.WaitingForConfiguration1_2Ack;
import com.epocal.epoctest.teststate.legacyepoctest.WaitingForConfiguration1_3Ack;
import com.epocal.epoctest.teststate.legacyepoctest.WaitingForConfiguration1_4Ack;
import com.epocal.epoctest.teststate.legacyepoctest.WaitingForDeviceEnableAck;
import com.epocal.epoctest.teststate.legacyepoctest.WaitingForHCMResponse;
import com.epocal.epoctest.teststate.legacyepoctest.WaitingForReaderStatusHeaterTemperatures;
import com.epocal.epoctest.teststate.legacyepoctest.WaitingForStatisticsResponse;
import com.epocal.epoctest.teststate.legacyepoctest.WaitingForTestGetReaderStatus;
import com.epocal.reader.IMsgPayload;
import com.epocal.statemachine.IState;
import com.epocal.statemachine.IStateEnum;
import com.epocal.statemachine.StateMachine;
import com.epocal.statemachine.StateMachineFactory;
import com.epocal.util.UUIDUtil;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.functions.Consumer;

/**
 * Created by dning on 5/24/2017.
 */

public class TestController {
    @Inject
    protected StateMachineFactory mStateMachineFactory;
    @Inject
    Provider<Disconnected> mDisconnected;
    @Inject
    Provider<Connecting> mConnecting;
    @Inject
    Provider<Connected> mConnected;
    @Inject
    Provider<GetDeviceId> mGetDeviceId;
    @Inject
    Provider<SendHostIdInfo> mSendHostIdInfo;
    @Inject
    Provider<GetDeviceStatus> mGetDeviceStatus;

    @Inject
    Provider<SetReaderInterface> mSetReaderInterface;

    @Inject
    Provider<EnableReaderSettingsSession> mEnableReaderSettingsSession;
    @Inject
    Provider<SetReaderGeneralConfig> mSetReaderGeneralConfig;
    @Inject
    Provider<SetReaderEQCConfig> mSetReaderEQCConfig;
    @Inject
    Provider<SetReaderDCCConfig> mSetReaderDCCConfig;
    @Inject
    Provider<DisableReaderSettingsSession> mDisableReaderSettingsSession;

    @Inject
    Provider<EnableReaderEQCSession> mEnableReaderEQCSession;
    @Inject
    Provider<PerformReaderEQCStart> mPerformReaderEQCStart;
    @Inject
    Provider<PerformReaderEQC> mPerformReaderEQC;
    @Inject
    Provider<PerformReaderEQCEnd> mPerformReaderEQCEnd;
    @Inject
    Provider<DisableReaderEQCSession> mDisableReaderEQCSession;

    @Inject
    Provider<GetDeviceStatusBeforeTestStart> mGetDeviceStatusBeforeTestStart;

    @Inject
    Provider<EnablePatientTestSession> mEnablePatientTestSession;
    @Inject
    Provider<ReadyTest> mReadyTest;
    @Inject
    Provider<EnableTest> mEnableTest;

    @Inject
    Provider<PerformDryCardCheckStart> mPerformDryCardCheckStart;
    @Inject
    Provider<PerformDryCardCheck> mPerformDryCardCheck;
    @Inject
    Provider<PerformDryCardCheckEnd> mPerformDryCardCheckEnd;

    @Inject
    Provider<AcceptCard> mAcceptCard;
    @Inject
    Provider<GetDeviceStatusAfterTestStart> mGetDeviceStatusAfterTestStart;

    @Inject
    Provider<PerformTestStart> mPerformTestStart;
    @Inject
    Provider<PerformFluidCalibration> mPerformFluidCalibration;
    @Inject
    Provider<PerformSampleIntroduction> mPerformSampleIntroduction;
    @Inject
    Provider<PerformSampleProcessing> mPerformSampleProcessing;
    @Inject
    Provider<PerformTestEnd> mPerformTestEnd;

    @Inject
    Provider<CancelTest> mCancelTest;
    @Inject
    Provider<DisableTest> mDisableTest;
    @Inject
    Provider<UsedCardInReader> mUsedCardInReader;

    @Inject
    Provider<CardInReader> mCardInReader;
    @Inject
    Provider<EndTestBeforeTestBegun> mEndTestBeforeTestBegun;
    @Inject
    Provider<TerminateTest> mTerminateTest;

    @Inject
    TestStateDataObject mTestStateDataObject;

    //Legacy state
    @Inject
    Provider<ActionAfterInitialConnect> mActionAfterInitialConnect;

    @Inject
    Provider<GetReaderId> mGetReaderId;

    @Inject
    Provider<WaitingForStatisticsResponse> mWaitingForStatisticsResponse;

    @Inject
    Provider<CheckFirstReaderStatus> mCheckFirstReaderStatus;

    @Inject
    Provider<WaitingForHCMResponse> mWaitingForHCMResponse;

    @Inject
    Provider<WaitingForConfiguration1_1Ack> mWaitingForConfiguration1_1Ack;

    @Inject
    Provider<WaitingForConfiguration1_2Ack> mWaitingForConfiguration1_2Ack;

    @Inject
    Provider<WaitingForConfiguration1_3Ack> mWaitingForConfiguration1_3Ack;

    @Inject
    Provider<WaitingForDeviceEnableAck> mWaitingForDeviceEnableAck;

    @Inject
    Provider<CheckReaderStatus> mCheckReaderStatus;

    @Inject
    Provider<AfterTestResponseForUpdateReaderStatus> mAfterTestResponseForUpdateReaderStatus;

    @Inject
    Provider<Ready> mReady;

    @Inject
    Provider<LegacyCardInReader> mLegacyCardInReader;

    @Inject
    Provider<WaitingForTestGetReaderStatus> mWaitingForTestGetReaderStatus;

    @Inject
    Provider<NewCardInReader> mNewCardInReader;

    @Inject
    Provider<WaitingForReaderStatusHeaterTemperatures> mWaitingForReaderStatusHeaterTemperatures;

    @Inject
    Provider<WaitingForConfiguration1_4Ack> mWaitingForConfiguration1_4Ack;

    @Inject
    Provider<FluidicsCalibration> mFluidicsCalibration;

    @Inject
    Provider<SampleIntroduction> mSampleIntroduction;

    @Inject
    Provider<SampleProcessing> mSampleProcessing;

    @Inject
    Provider<TestCompleted> mTestCompleted;

    @Inject
    Provider<TestCompletedCardRemoved> mTestCompletedCardRemoved;

    @Inject
    Provider<EndTestAfterTestBegun> mEndTestAfterTestBegun;

    @Inject
    Provider<OldCardInReader> mOldCardInReader;

    private StateMachine mStateMachine;
    private TestCommunication mTestCommunication;

    private UUID mControllerUUID;

    public UUID getControllerUUID() {
        return mControllerUUID;
    }

    protected RxUtil<TestEventInfo> mRxUtil;

    public RxUtil<TestEventInfo> getRxUtil() {
        return mRxUtil;
    }

    public void setRxUtil(RxUtil<TestEventInfo> mRxUtil) {
        this.mRxUtil = mRxUtil;
    }

    private String mAddress;

    public TestType getTestType() {
        return mTestStateDataObject.getTestDataProcessor().getTestType();
    }

    public void setTestType(TestType testType) {
        mTestStateDataObject.getTestDataProcessor().setTestType(testType);
    }

    public TestController() {
        TestStateComponent tc = DaggerTestStateComponent.builder()
                .testStateModule(new TestStateModule())
                .build();
        tc.inject(this);
        mRxUtil = new RxUtil().create(null);
    }

    public void initializeController(ReaderDevice readerDevice, TestMode testMode) {
        mControllerUUID = UUIDUtil.generateUUID();

        //setup StateMachine
        mStateMachine = new StateMachine<TestStateDataObject>();
        mStateMachine.setStateList(GetStateList());
        mStateMachineFactory.<TestStateDataObject, TestStateEnum, TestStateEventEnum>StateMachineInstance
                (getStateMachineXML(), mStateMachine, EnumSet.allOf(TestStateEnum.class), EnumSet.allOf(TestStateEventEnum.class));

        //setup Communication
        mTestCommunication = new TestCommunication(readerDevice.getDeviceAddress());
        mTestCommunication.setReaderSerialNumber(readerDevice.getDeviceId());
        mTestCommunication.getRxUtilStatus().subscribe(new Consumer<TestEventInfo>() {
            @Override
            public void accept(TestEventInfo testEventInfo) throws Exception {
                fireEventCenter(testEventInfo);
            }
        });
        mTestCommunication.getRxUtilMessage().subscribe(new Consumer<IMsgPayload>() {
            @Override
            public void accept(IMsgPayload iMsgPayload) throws Exception {
                mTestStateDataObject.setTestStateAction(TestStateActionEnum.None);
                mTestStateDataObject.setIMsgPayload(iMsgPayload);
                postEventToStateMachiane(mTestStateDataObject);
            }
        });

        //setup State object
        mTestStateDataObject.initTestData(readerDevice, testMode); // need the testRecord object right away, even if test not started yet
        mTestStateDataObject.setAddress(readerDevice.getDeviceAddress());
        mTestStateDataObject.setStateMachine(mStateMachine);
        mTestStateDataObject.setTestCommunication(mTestCommunication);
        mTestStateDataObject.getRxUtil().subscribe(new Consumer<TestEventInfo>() {
            @Override
            public void accept(TestEventInfo testEventInfo) throws Exception {
                fireEventCenter(testEventInfo);
            }
        });
    }

    public TestStateDataObject getTestStateDataObject() {
        return mTestStateDataObject;
    }

    /***
     * post event into state machine which events are coming from connection layer or communication layer.
     * to notify state machine the connection status or to send message to states.
     * @param testStateDataObject
     */
    protected void postEventToStateMachiane(TestStateDataObject testStateDataObject) {
        if (mStateMachine != null) {
            mStateMachine.postEvent(testStateDataObject);
        }
    }

    /***
     * fire event to subscriber which usually is coming from UI
     * @param testEventInfo
     */
    protected void fireEventCenter(TestEventInfo testEventInfo) {
        mRxUtil.onNext(testEventInfo);
        if (testEventInfo.getTestEventInfoType() == TestEventInfoType.STATUS_INFO && testEventInfo.getTestStatusType() == TestStatusType.CONNECTED) {
            mTestStateDataObject.setTestStateAction(TestStateActionEnum.Connected);
            postEventToStateMachiane(mTestStateDataObject);
        }
    }

    public void start() {
        if (mStateMachine == null) return;
        mStateMachine.start(mTestStateDataObject);

        mTestStateDataObject.setTestStateAction(TestStateActionEnum.Connecting);
        postEventToStateMachiane(mTestStateDataObject);
    }

    public void stop() {
        if (mStateMachine == null) return;

        mTestStateDataObject.setTestStateAction(TestStateActionEnum.CancelConnection);
        postEventToStateMachiane(mTestStateDataObject);
        mTestCommunication.disconnect();
    }

    public void disposeObject() {
        mTestStateDataObject.disposeObject();
    }

    private Map<IStateEnum, IState> GetStateList() {
        Map<IStateEnum, IState> stateList = new HashMap<IStateEnum, IState>();
        putInTheList(stateList, TestStateEnum.Disconnected, mDisconnected.get());
        putInTheList(stateList, TestStateEnum.Connecting, mConnecting.get());
        putInTheList(stateList, TestStateEnum.Connected, mConnected.get());
        putInTheList(stateList, TestStateEnum.GetDeviceId, mGetDeviceId.get());
        putInTheList(stateList, TestStateEnum.SendHostIdInfo, mSendHostIdInfo.get());
        putInTheList(stateList, TestStateEnum.GetDeviceStatus, mGetDeviceStatus.get());

        putInTheList(stateList, TestStateEnum.SetReaderInterface, mSetReaderInterface.get());

        putInTheList(stateList, TestStateEnum.EnableReaderSettingsSession, mEnableReaderSettingsSession.get());
        putInTheList(stateList, TestStateEnum.SetReaderGeneralConfig, mSetReaderGeneralConfig.get());
        putInTheList(stateList, TestStateEnum.SetReadereQCConfig, mSetReaderEQCConfig.get());
        putInTheList(stateList, TestStateEnum.SetReaderDCCConfig, mSetReaderDCCConfig.get());
        putInTheList(stateList, TestStateEnum.DisableReaderSettingsSession, mDisableReaderSettingsSession.get());

        putInTheList(stateList, TestStateEnum.EnableReaderEQCSession, mEnableReaderEQCSession.get());
        putInTheList(stateList, TestStateEnum.PerformReaderEQCStart, mPerformReaderEQCStart.get());
        putInTheList(stateList, TestStateEnum.PerformReaderEQC, mPerformReaderEQC.get());
        putInTheList(stateList, TestStateEnum.PerformReaderEQCEnd, mPerformReaderEQCEnd.get());
        putInTheList(stateList, TestStateEnum.DisableReaderEQCSession, mDisableReaderEQCSession.get());

        putInTheList(stateList, TestStateEnum.GetDeviceStatusBeforeTestStart, mGetDeviceStatusBeforeTestStart.get());

        putInTheList(stateList, TestStateEnum.EnablePatientTestSession, mEnablePatientTestSession.get());
        putInTheList(stateList, TestStateEnum.ReadyTest, mReadyTest.get());
        putInTheList(stateList, TestStateEnum.EnableTest, mEnableTest.get());

        putInTheList(stateList, TestStateEnum.PerformDryCardCheckStart, mPerformDryCardCheckStart.get());
        putInTheList(stateList, TestStateEnum.PerformDryCardCheck, mPerformDryCardCheck.get());
        putInTheList(stateList, TestStateEnum.PerformDryCardCheckEnd, mPerformDryCardCheckEnd.get());

        putInTheList(stateList, TestStateEnum.AcceptCard, mAcceptCard.get());
        putInTheList(stateList, TestStateEnum.GetDeviceStatusAfterTestStart, mGetDeviceStatusAfterTestStart.get());

        putInTheList(stateList, TestStateEnum.PerformTestStart, mPerformTestStart.get());
        putInTheList(stateList, TestStateEnum.PerformFluidCalibration, mPerformFluidCalibration.get());
        putInTheList(stateList, TestStateEnum.PerformSampleIntroduction, mPerformSampleIntroduction.get());
        putInTheList(stateList, TestStateEnum.PerformSampleProcessing, mPerformSampleProcessing.get());
        putInTheList(stateList, TestStateEnum.PerformTestEnd, mPerformTestEnd.get());

        putInTheList(stateList, TestStateEnum.CancelTest, mCancelTest.get());
        putInTheList(stateList, TestStateEnum.DisableTest, mDisableTest.get());
        putInTheList(stateList, TestStateEnum.UsedCardInReader, mUsedCardInReader.get());
        putInTheList(stateList, TestStateEnum.CardInReader, mCardInReader.get());

        putInTheList(stateList, TestStateEnum.EndTestBeforeTestBegun, mEndTestBeforeTestBegun.get());
        putInTheList(stateList, TestStateEnum.TerminateTest, mTerminateTest.get());

        return stateList;
    }

    protected void putInTheList(Map<IStateEnum, IState> theStateList, IStateEnum theStateEnum, IState theState)
    {
        theState.setStateName(theStateEnum.toString());
        theStateList.put(theStateEnum, theState);
    }

    private String getStateMachineXML() {
        String xmlString = new StringBuilder()
                .append("<state id='Root'>")
                .append("<initial>")
                .append("<transition target='Connection'/>")
                .append("</initial>")

                .append("<state id='Connection'>")
                .append("<initial>")
                .append("<transition target='Disconnected'/>")
                .append("</initial>")
                .append("<state id='Disconnected'>")
                .append("<transition event='connecting' target='connecting' type='normal'/>")
                .append("</state>")
                .append("<state id='connecting'>")
                .append("<transition event='Connected' target='Connected' type='normal'/>")
                .append("<transition event='Disconnected' target='Disconnected' type='normal'/>")
                .append("</state>")
                .append("<final id='Connected'>")
                .append("<transition event='Disconnected' target='Disconnected' type='normal'/>")
                .append("</final>")
                .append("<transition event='TestStateBody' target='TestStateBody' type='normal'/>")
                .append("</state>")//end of Connection

                .append("<state id='TestStateBody'>")
                .append("<initial>")
                .append("<transition target='ReaderInitialize'/>")
                .append("</initial>")

                .append("<state id='ReaderInitialize'>")
                .append("<initial>")
                .append("<transition target='GetDeviceId'/>")
                .append("</initial>")
                .append("<state id='GetDeviceId'>")
                .append("<transition event='GetDeviceStatus' target='GetDeviceStatus' type='normal'/>")
                .append("</state>")
                .append("<state id='SendHostIdInfo'>")
                .append("<transition event='GetDeviceStatus' target='GetDeviceStatus' type='normal'/>")
                .append("</state>")
                .append("<state id='GetDeviceStatus'>")
                .append("<transition event='SetReaderInterface' target='SetReaderInterface' type='normal'/>")
                .append("</state>")
                .append("<final id='SetReaderInterface'>")
                .append("</final>")
                .append("<transition event='ReaderSettings' target='ReaderSettings' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='error'/>")
                .append("</state>") //end of ReaderInitialize

                .append("<state id='ReaderSettings'>")
                .append("<initial>")
                .append("<transition target='EnableReaderSettingsSession'/>")
                .append("</initial>")
                .append("<state id='EnableReaderSettingsSession'>")
                .append("<transition event='SetReaderGeneralConfig' target='SetReaderGeneralConfig' type='normal'/>")
                .append("</state>")
                .append("<state id='SetReaderGeneralConfig'>")
                .append("<transition event='SetReadereQCConfig' target='SetReadereQCConfig' type='normal'/>")
                .append("</state>")
                .append("<state id='SetReadereQCConfig'>")
                .append("<transition event='SetReaderDCCConfig' target='SetReaderDCCConfig' type='normal'/>")
                .append("</state>")
                .append("<state id='SetReaderDCCConfig'>")
                .append("<transition event='DisableReaderSettingsSession' target='DisableReaderSettingsSession' type='normal'/>")
                .append("</state>")
                .append("<final id='DisableReaderSettingsSession'>")
                .append("</final>")
                .append("<transition event='ReaderEQC' target='ReaderEQC' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='error'/>")
                .append("</state>") //end of ReaderSettings

                //eQC section
                .append("<state id='ReaderEQC'>")
                .append("<initial>")
                .append("<transition target='EnableReaderEQCSession'/>")
                .append("</initial>")
                .append("<state id='EnableReaderEQCSession'>")
                .append("<transition event='PerformReaderEQCStart' target='PerformReaderEQCStart' type='normal'/>")
                .append("</state>")
                .append("<state id='PerformReaderEQCStart'>")
                .append("<transition event='PerformReaderEQC' target='PerformReaderEQC' type='normal'/>")
                .append("</state>")
                .append("<state id='PerformReaderEQC'>")
                .append("<transition event='PerformReaderEQCEnd' target='PerformReaderEQCEnd' type='normal'/>")
                .append("</state>")
                .append("<state id='PerformReaderEQCEnd'>")
                .append("<transition event='DisableReaderEQCSession' target='DisableReaderEQCSession' type='normal'/>")
                .append("</state>")
                .append("<final id='DisableReaderEQCSession'>")
                .append("</final>")
                .append("<transition event='GetDeviceStatusBeforeTestStart' target='GetDeviceStatusBeforeTestStart' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='error'/>")
                .append("</state>")
                //END of eQC section

                .append("<state id='GetDeviceStatusBeforeTestStart'>")
                .append("<transition event='PatientTest' target='PatientTest' type='normal'/>")
                .append("</state>")

                //Start PatientTest
                .append("<state id='PatientTest'>")
                .append("<initial>")
                .append("<transition target='EnablePatientTestSession'/>")
                .append("</initial>")
                .append("<state id='EnablePatientTestSession'>")
                .append("<transition event='ReadyTest' target='ReadyTest' type='normal'/>")
                .append("</state>")
                .append("<state id='ReadyTest'>")
                .append("<transition event='EnableTest' target='EnableTest' type='normal'/>")
                .append("</state>")
                .append("<state id='EnableTest'>")
                .append("<transition event='DryCardCheck' target='DryCardCheck' type='normal'/>")
                .append("</state>")

                //Dry card check section
                .append("<state id='DryCardCheck'>")
                .append("<initial>")
                .append("<transition target='PerformDryCardCheckStart'/>")
                .append("</initial>")
                .append("<state id='PerformDryCardCheckStart'>")
                .append("<transition event='PerformDryCardCheck' target='PerformDryCardCheck' type='normal'/>")
                .append("</state>")
                .append("<state id='PerformDryCardCheck'>")
                .append("<transition event='PerformDryCardCheckEnd' target='PerformDryCardCheckEnd' type='normal'/>")
                .append("</state>")
                .append("<final id='PerformDryCardCheckEnd'>")
                .append("</final>")
                .append("<transition event='UsedCardInReader' target='UsedCardInReader' type='normal'/>")
                .append("<transition event='AcceptCard' target='AcceptCard' type='normal'/>")
                .append("<transition event='DisableTest' target='DisableTest' type='error'/>")
                .append("<transition event='CancelTest' target='CancelTest' type='error'/>")
                .append("</state>")
                //END of Dry card check section

                .append("<state id='AcceptCard'>")
                .append("<transition event='GetDeviceStatusAfterTestStart' target='GetDeviceStatusAfterTestStart' type='normal'/>")
                .append("</state>")

                .append("<state id='GetDeviceStatusAfterTestStart'>")
                .append("<transition event='DoTest' target='DoTest' type='normal'/>")
                .append("</state>")

                //do Test
                .append("<state id='DoTest'>")
                .append("<initial>")
                .append("<transition target='PerformTestStart'/>")
                .append("</initial>")
                .append("<state id='PerformTestStart'>")
                .append("<transition event='PerformFluidCalibration' target='PerformFluidCalibration' type='normal'/>")
                .append("</state>")
                .append("<state id='PerformFluidCalibration'>")
                .append("<transition event='PerformSampleIntroduction' target='PerformSampleIntroduction' type='normal'/>")
                .append("</state>")
                .append("<state id='PerformSampleIntroduction'>")
                .append("<transition event='PerformSampleProcessing' target='PerformSampleProcessing' type='normal'/>")
                .append("</state>")
                .append("<state id='PerformSampleProcessing'>")
                .append("<transition event='PerformTestEnd' target='PerformTestEnd' type='normal'/>")
                .append("</state>")
                .append("<final id='PerformTestEnd'>")
                .append("</final>")
                .append("<transition event='DisableTest' target='DisableTest' type='normal'/>")
                .append("<transition event='CancelTest' target='CancelTest' type='error'/>")
                .append("</state>")
                //END of do Test

                .append("<state id='CancelTest'>")
                .append("<transition event='UsedCardInReader' target='UsedCardInReader' type='normal'/>")
                .append("</state>")

                .append("<state id='DisableTest'>")
                .append("<transition event='UsedCardInReader' target='UsedCardInReader' type='normal'/>")
                .append("</state>")

                .append("<state id='CardInReader'>")
                .append("<transition event='ReadyTest' target='ReadyTest' type='normal'/>")
                .append("</state>")

                .append("<state id='UsedCardInReader'>")
                .append("<transition event='ReadyTest' target='ReadyTest' type='normal'/>")
                .append("</state>")

                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='error'/>")
                .append("</state>") //end of PatientTest


                .append("<state id='EndTestBeforeTestBegun'>")
                .append("</state>")

                .append("<transition event='Done' target='Done' type='normal'/>")
                .append("<transition event='TerminateTest' target='TerminateTest' type='error'/>")

                .append("</state>")//end of TestStateBody

                .append("<state id='TerminateTest'>")
                .append("<transition event='Disconnected' target='Connection' type='normal'/>")
                .append("</state>")

                .append("<final id='Done'/>")
                .append("</state>").toString(); //end of Root

        return xmlString;
    }
}
