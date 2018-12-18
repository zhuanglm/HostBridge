package com.epocal.epoctest.di.module;


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

import dagger.Module;
import dagger.Provides;

/**
 * Created by rzhuang on July 18 2018
 */

@Module
public class LegacyTestStateModule
{
    @Provides public ActionAfterInitialConnect provideActionAfterInitialConnect() { return new ActionAfterInitialConnect();   }
    @Provides public GetReaderId provideGetReaderId() { return new GetReaderId();}
    @Provides public WaitingForStatisticsResponse provideWaitingForStatisticsResponse() { return new WaitingForStatisticsResponse();}
    @Provides public CheckFirstReaderStatus provideCheckFirstReaderStatus() {return new CheckFirstReaderStatus();}
    @Provides public WaitingForHCMResponse provideWaitingForHCMResponse() {return new WaitingForHCMResponse();}
    @Provides public WaitingForConfiguration1_2Ack provideWaitingForConfiguration1_2Ack() {return new WaitingForConfiguration1_2Ack();}
    @Provides public WaitingForDeviceEnableAck provideWaitingForDeviceEnableAck() {return new WaitingForDeviceEnableAck();}
    @Provides public CheckReaderStatus provideCheckReaderStatus() {return new CheckReaderStatus();}
    @Provides public AfterTestResponseForUpdateReaderStatus provideAfterTestResponseForUpdateReaderStatus(){
        return new AfterTestResponseForUpdateReaderStatus();
    }
    @Provides public OldCardInReader provideOldCardInReader() {return  new OldCardInReader();}
    @Provides public WaitingForConfiguration1_1Ack provideWaitingForConfiguration1_1Ack() {return new WaitingForConfiguration1_1Ack();}
    @Provides public WaitingForConfiguration1_3Ack provideWaitingForConfiguration1_3Ack() {return new WaitingForConfiguration1_3Ack();}
    @Provides public Ready provideReady() {return new Ready();}
    @Provides public LegacyCardInReader provideLegacyCardInReader() {return new LegacyCardInReader();}
    @Provides public WaitingForTestGetReaderStatus provideWaitingForTestGetReaderStatus() {return new WaitingForTestGetReaderStatus();}
    @Provides public NewCardInReader provideNewCardInReader() {return new NewCardInReader();}
    @Provides public WaitingForReaderStatusHeaterTemperatures provideWaitingForReaderStatusHeaterTemperatures() {
        return new WaitingForReaderStatusHeaterTemperatures();
    }
    @Provides public WaitingForConfiguration1_4Ack provideWaitingForConfiguration1_4Ack() {return new WaitingForConfiguration1_4Ack();}
    @Provides public FluidicsCalibration provideFluidicsCalibration() {return new FluidicsCalibration();}
    @Provides public SampleIntroduction provideSampleIntroduction() {return new SampleIntroduction();}
    @Provides public SampleProcessing provideSampleProcessing() {return new SampleProcessing();}
    @Provides public TestCompleted provideTestCompleted() {return new TestCompleted();}
    @Provides public TestCompletedCardRemoved provideTestCompletedCardRemoved() {return new TestCompletedCardRemoved();}
    @Provides public EndTestAfterTestBegun provideEndTestAfterTestBegun() { return new EndTestAfterTestBegun();   }
}
