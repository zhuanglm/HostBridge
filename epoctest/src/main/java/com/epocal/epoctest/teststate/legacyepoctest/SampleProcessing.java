package com.epocal.epoctest.teststate.legacyepoctest;

import android.util.Log;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestDataProcessor;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.enumtype.legacy.TestStoppedReasons;
import com.epocal.reader.legacy.message.response.LegacyRspDataPacket;
import com.epocal.reader.legacy.message.response.LegacyRspError;
import com.epocal.reader.legacy.message.response.LegacyRspTestStopped;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.statemachine.IEventEnum;
import com.epocal.util.DecimalConversionUtil;

import java.util.Calendar;

/**
 * Created by rzhuang on Aug 28 2018.
 */

public class SampleProcessing extends LegacyTestState {
    public SampleProcessing() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        stateDataObject.startTimer();

    }

    @Override
    public void onExit(TestStateDataObject stateDataObject) {
        stopTestTypeCountDown();
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        message = stateDataObject.getIMsgPayload();
        TestEventInfo eventInfo = new TestEventInfo();
        TestDataProcessor tdp = stateDataObject.getTestDataProcessor();
        if (message == null) {
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }

        //Timer oneTimeProcessTimer;
        // we are processing the sample
        if (message.getDescriptor().getType() == LegacyMessageType.DataPacket.value) {
            stateDataObject.StopTimer();

            if (!tdp.getOneTimeFlag()) {
                tdp.setOneTimeFlag(true);
                int delayTime = tdp.getSamplingDelayDuration();
                startTestTypeCountDown(TestStatusType.SAMPLEPROCESSING,
                        Math.round((int) tdp.mConfigMsg1_4.stagesTimers.getSampleCollectionTimer()),
                        0, 1, stateDataObject);

                tdp.legacyCheckSampleInjectionError(stateDataObject, delayTime);
            }
            //stateDataObject.StopTimer();
            tdp.setLastRecordedTime(DecimalConversionUtil.round(
                    tdp.getLastRecordedTime() + tdp.getTimeIncrement(), 1));

            // store data packet
            LegacyRspDataPacket tempPacket = (LegacyRspDataPacket) message;
            tdp.parseDataPacket(tempPacket, stateDataObject, this);
            // wait for next packet
            stateDataObject.startTimer();

            return TestStateEventEnum.Handled;
        } else if (message.getDescriptor().getType() == LegacyMessageType.TestStopped.value) {
            stateDataObject.StopTimer();
            Log.d(getStateName(), "get Teststopped in SampleProcessing");

            LegacyRspTestStopped tempMsg = (LegacyRspTestStopped) message;

            if (tempMsg.getReason() == TestStoppedReasons.SamplingTimerExpired) {
                // test finished normally... go to the state where we wait on the ack,
                // but can handle the card remove.. but dont send out the destroytest
                // until we've calculated the results

                //stateDataObject.StopTimer();
                //bm: do not close packet file here, we are still connected to the reader. File will be recreated anyway (teststate.cs)resulting in two files per test.
                //stateDataObject.TestDataProcessor.ClosePacketFile("Test ended normally");
                tdp.setConnectionTime(Calendar.getInstance());
                // Since we made it to the end, have the form up the number of successful tests.
                stateDataObject.calculateResults();
                return TestStateEventEnum.TestCompleted;
            } else {
                Log.d(getStateName(), "error reason in SampleProcessing is: " +
                        ((LegacyRspTestStopped) message).getReason().toString());
                tdp.setErrorCode(ConvertTestStoppedToErrorCode(((LegacyRspTestStopped) message).getReason(), stateDataObject));

                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(ErrorCodeToErrorEventType(tdp.getErrorCode(), TestStatusErrorType.ERROROCCURREDTESTSTOP));
                stateDataObject.postEvent(eventInfo);

                return TestStateEventEnum.EndTestAfterTestBegun;
            }
        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value) {
//            if (oneTimeProcessTimer != null)
//            {
//                oneTimeProcessTimer.Change(System.Threading.Timeout.Infinite, System.Threading.Timeout.Infinite);
//            }
            stateDataObject.StopTimer();
            LegacyRspError error = (LegacyRspError) message;
            if (error.getErrorCode() != LegacyRspError.ErrorType.Undefined) {
                if (tdp.getErrorCode() == HostErrorCode.NoError) {
                    tdp.setErrorCode(HostErrorCode.ErrorOccurredDuringSampling);
                }

                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(ErrorCodeToErrorEventType(tdp.getErrorCode(), TestStatusErrorType.ERROROCCURREDTESTSTOP));
                return TestStateEventEnum.EndTestAfterTestBegun;
            }
        }

        if (stateDataObject.getTestStateAction() == TestStateActionEnum.CancelConnection) {
            tdp.setErrorCode(HostErrorCode.UserStoppedTestDuringSampleIntro);
            stateDataObject.closePreviousTest();
        }
        return super.onEventPreHandle(stateDataObject);
    }

}
