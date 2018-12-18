package com.epocal.epoctest.di.module;

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
import com.epocal.epoctest.teststate.epoctest.patienttest.GetDeviceStatusAfterTestStart;
import com.epocal.epoctest.teststate.epoctest.patienttest.EnableTest;
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
import com.epocal.statemachine.StateMachineFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dning on 5/24/2017.
 */

@Module
public class TestStateModule
{
    @Provides public StateMachineFactory provideStateMachineFactory() { return new StateMachineFactory();   }
    @Provides public TestStateDataObject provideTestStateDataObject() { return new TestStateDataObject();   }

    @Provides public Disconnected provideDisconnected() { return new Disconnected();   }
    @Provides public Connecting provideConnecting() { return new Connecting();   }
    @Provides public Connected provideConnected() { return new Connected();   }

    @Provides public GetDeviceId provideGetDeviceId() { return new GetDeviceId();   }
    @Provides public SendHostIdInfo provideSendHostIdInfo() { return new SendHostIdInfo();   }
    @Provides public GetDeviceStatus provideGetDeviceStatus(){return new GetDeviceStatus();}
    @Provides public SetReaderInterface provideSetReaderInterface(){return new SetReaderInterface();}

    @Provides public EnableReaderSettingsSession provideEnableReaderSettingsSession() { return new EnableReaderSettingsSession();   }
    @Provides public SetReaderGeneralConfig provideSetReaderGeneralConfig() { return new SetReaderGeneralConfig();   }
    @Provides public SetReaderEQCConfig provideSetReaderEQCConfig(){return new SetReaderEQCConfig();}
    @Provides public SetReaderDCCConfig provideSetReaderDCCConfig(){return new SetReaderDCCConfig();}
    @Provides public DisableReaderSettingsSession provideDisableReaderSettingsSession(){return new DisableReaderSettingsSession();}

    @Provides public EnableReaderEQCSession provideEnableReaderEQCSession() { return new EnableReaderEQCSession();   }
    @Provides public PerformReaderEQCStart providePerformReaderEQCStart() { return new PerformReaderEQCStart();   }
    @Provides public PerformReaderEQC providePerformReaderEQC(){return new PerformReaderEQC();}
    @Provides public PerformReaderEQCEnd providePerformReaderEQCEnd(){return new PerformReaderEQCEnd();}
    @Provides public DisableReaderEQCSession provideDisableReaderEQCSession(){return new DisableReaderEQCSession();}

    @Provides public GetDeviceStatusBeforeTestStart provideGetDeviceStatusBeforeTestStart(){return new GetDeviceStatusBeforeTestStart();}

    @Provides public EnablePatientTestSession provideEnablePatientTestSession(){return new EnablePatientTestSession();}
    @Provides public ReadyTest provideReadyTest(){return new ReadyTest();}
    @Provides public EnableTest provideEnableTest(){return new EnableTest();}

    @Provides public PerformDryCardCheckStart providePerformDryCardCheckStart(){return new PerformDryCardCheckStart();}
    @Provides public PerformDryCardCheck providePerformDryCardCheck(){return new PerformDryCardCheck();}
    @Provides public PerformDryCardCheckEnd providePerformDryCardCheckEnd(){return new PerformDryCardCheckEnd();}

    @Provides public AcceptCard provideAcceptCard(){return new AcceptCard();}
    @Provides public GetDeviceStatusAfterTestStart provideGetDeviceStatusAfterTestBegun(){return new GetDeviceStatusAfterTestStart();}

    @Provides public PerformTestStart providePerformTestStart(){return new PerformTestStart();}
    @Provides public PerformFluidCalibration providePerformFluidCalibration(){return new PerformFluidCalibration();}
    @Provides public PerformSampleIntroduction providePerformSampleIntroduction(){return new PerformSampleIntroduction();}
    @Provides public PerformSampleProcessing providePerformSampleProcessing(){return new PerformSampleProcessing();}
    @Provides public PerformTestEnd providePerformTestEnd(){return new PerformTestEnd();}

    @Provides public CancelTest provideCancelTest(){return new CancelTest();}
    @Provides public DisableTest provideDisableTest(){return new DisableTest();}
    @Provides public UsedCardInReader provideUsedCardInReader(){return new UsedCardInReader();}
    @Provides public CardInReader provideCardInReader(){return new CardInReader();}

    @Provides public EndTestBeforeTestBegun provideEndTestBeforeTestBegun() { return new EndTestBeforeTestBegun();   }

    @Provides public TerminateTest provideTerminateTest() { return new TerminateTest();   }

}
