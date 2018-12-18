package com.epocal.epoctest.di.component;

import com.epocal.epoctest.TestController;
import com.epocal.epoctest.di.module.LegacyTestStateModule;
import com.epocal.epoctest.di.module.TestStateModule;
import com.epocal.epoctest.testprocess.TestStateDataObject;
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
import com.epocal.statemachine.StateMachineFactory;

import dagger.Component;

/**
 * Created by dning on 5/24/2017.
 */

@Component(modules = {TestStateModule.class, LegacyTestStateModule.class})
public interface TestStateComponent {
    void inject(TestController injected);

    void inject(TestStateDataObject injected);
    StateMachineFactory provideStateMachineFactory();
    TestStateDataObject provideTestStateDataObject();

    Disconnected provideDisconnected();
    Connecting provideConnecting();
    Connected provideConnected();

    GetDeviceId provideGetDeviceId();
    SendHostIdInfo provideSendHostIdInfo();
    GetDeviceStatus provideGetDeviceStatus();
    SetReaderInterface provideSetReaderInterface();

    EnableReaderSettingsSession provideEnableReaderSettingsSession();
    SetReaderGeneralConfig provideSetReaderGeneralConfig();
    SetReaderEQCConfig provideSetReaderEQCConfig();
    SetReaderDCCConfig provideSetReaderDCCConfig();
    DisableReaderSettingsSession provideDisableReaderSettingsSession();

    EnableReaderEQCSession provideEnableReaderEQCSession();
    PerformReaderEQCStart providePerformReaderEQCStart();
    PerformReaderEQC providePerformReaderEQC();
    PerformReaderEQCEnd providePerformReaderEQCEnd();
    DisableReaderEQCSession provideDisableReaderEQCSession();

    GetDeviceStatusBeforeTestStart provideGetDeviceStatusBeforeTestStart();

    EnablePatientTestSession provideEnablePatientTestSession();
    ReadyTest provideReadyTest();
    EnableTest provideEnableTest();

    PerformDryCardCheckStart providePerformDryCardCheckStart();
    PerformDryCardCheck providePerformDryCardCheck();
    PerformDryCardCheckEnd providePerformDryCardCheckEnd();

    AcceptCard provideAcceptCard();
    GetDeviceStatusAfterTestStart provideGetDeviceStatusAfterTestStart();

    PerformTestStart providePerformTestStart();
    PerformFluidCalibration providePerformFluidCalibration();
    PerformSampleIntroduction providePerformSampleIntroduction();
    PerformSampleProcessing providePerformSampleProcessing();
    PerformTestEnd providePerformTestEnd();

    CancelTest provideCancelTest();
    DisableTest provideDisableTest();
    UsedCardInReader provideUsedCardInReader();
    CardInReader provideCardInReader();

    EndTestBeforeTestBegun provideEndTestBeforeTestBegun();
    EndTestAfterTestBegun provideEndTestAfterTestBegun();
    TerminateTest provideTerminateTest();

    // added for Legacy reader
    ActionAfterInitialConnect provideActionAfterInitialConnect();
    GetReaderId provideGetReaderId();
    WaitingForStatisticsResponse provideWaitingForStatisticsResponse();
    CheckFirstReaderStatus provideCheckFirstReaderStatus();
    WaitingForHCMResponse provideWaitingForHCMResponse();
    WaitingForConfiguration1_2Ack provideWaitingForConfiguration1_2Ack();
    WaitingForDeviceEnableAck provideWaitingForDeviceEnableAck();
    CheckReaderStatus provideCheckReaderStatus();
    AfterTestResponseForUpdateReaderStatus provideAfterTestResponseForUpdateReaderStatus();
    OldCardInReader provedeOldCardInReader();
    WaitingForConfiguration1_1Ack provideWaitingForConfiguration1_1Ack();
    WaitingForConfiguration1_3Ack provideWaitingForConfiguration1_3Ack();
    Ready provideReady();
    LegacyCardInReader provideLegacyCardInReader();
    WaitingForTestGetReaderStatus provideWaitingForTestGetReaderStatus();
    NewCardInReader provideNewCardInReader();
    WaitingForReaderStatusHeaterTemperatures provideWaitingForReaderStatusHeaterTemperatures();
    WaitingForConfiguration1_4Ack provideWaitingForConfiguration1_4Ack();
    FluidicsCalibration provideFluidicsCalibration();
    SampleIntroduction provideSampleIntroduction();
    SampleProcessing provideSampleProcessing();
    TestCompleted provideTestCompleted();
    TestCompletedCardRemoved provideTestCompletedCardRemoved();

}
