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
import com.epocal.reader.legacy.message.request.command.LegacyReqStopTest;
import com.epocal.reader.legacy.message.response.LegacyRspDataPacket;
import com.epocal.reader.legacy.message.response.LegacyRspError;
import com.epocal.reader.legacy.message.response.LegacyRspTestStopped;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.statemachine.IEventEnum;
import com.epocal.util.DecimalConversionUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by rzhuang on Aug 28 2018.
 */

public class SampleIntroduction extends LegacyTestState {

    private ExecutorService mThreadPool;
    private TestDataProcessor mTdp;
    private int mCpuNum;
    private ArrayBlockingQueue mArrayBlockingQueue;
    private boolean mRTQC;

    public SampleIntroduction() {
        mCpuNum = Runtime.getRuntime().availableProcessors();
        mArrayBlockingQueue = new ArrayBlockingQueue(1);
        mRTQC = true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);

        if (stateDataObject.mRedistributed) {
            stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.None);
        }
        mTdp = stateDataObject.getTestDataProcessor();
        startTestTypeCountDown(TestStatusType.SAMPLEINTRODUCTION, Math.round((int) stateDataObject
                .getTestDataProcessor().mConfigMsg1_4.stagesTimers.getSampleIntroductionTimer()), 0, 1, stateDataObject);

        if (mThreadPool == null || mThreadPool.isShutdown())
            mThreadPool = new ThreadPoolExecutor(mCpuNum, mCpuNum, 1L, TimeUnit.MILLISECONDS, mArrayBlockingQueue);
    }

    @Override
    public void onExit(TestStateDataObject stateDataObject) {
        stopTestTypeCountDown();
        mThreadPool.shutdownNow();
    }

    @Override
    public IEventEnum onEventPreHandle(final TestStateDataObject stateDataObject) {
        message = stateDataObject.getIMsgPayload();
        TestEventInfo eventInfo = new TestEventInfo();

        if (message == null) {
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }

        // we're waiting for the sample
        if (message.getDescriptor().getType() == LegacyMessageType.DataPacket.value) {
            boolean bubbleBeginSetThisPacket;

            stateDataObject.StopTimer();

            // store bubble detect data
            LegacyRspDataPacket tempPacket = (LegacyRspDataPacket) message;

            mTdp.setLastRecordedTime(DecimalConversionUtil.round(
                    mTdp.getLastRecordedTime() + mTdp.getTimeIncrement(), 1));
            mTdp.parseDataPacket(tempPacket, stateDataObject, this);

            bubbleBeginSetThisPacket = mTdp.legacyDoBubbleDetectDuringSampleIntroduction(tempPacket);

            if (mTdp.getTestConfiguration().RealTimeQCSetting.Enabled) {
                // do we need to do realtime qc ? do it
                // ONLY do it if the bubble hasnt started yet
                if (mTdp.getBubbleDetect() == 0 && stateDataObject.getTestDataProcessor().getBubbleBegin() == 0) {
                    try {
                        mThreadPool.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (!mTdp.doRealTimeQC(stateDataObject)) {
                                    LegacyReqStopTest legacyReqStopTest = new LegacyReqStopTest();
                                    legacyReqStopTest.setTestID((short) stateDataObject.getTestID());
                                    stateDataObject.sendMessage(legacyReqStopTest);
                                    Log.d("PerformRealTimeQC", "Stop test!");
                                    mRTQC = false;
                                } else {
                                    mRTQC = true;
                                }
                            }
                        });
                    } catch (RejectedExecutionException e) {
                        Log.d("PerformRealTimeQC", "Too many tasks");
                    }

                    if (!mRTQC) {
                        eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                        eventInfo.setTestStatusErrorType(TestStatusErrorType.REALTIMEQCFAILED);
                        stateDataObject.postEvent(eventInfo);

                        return TestStateEventEnum.EndTestAfterTestBegun;
                    }

                }
            }
            boolean gotoProcessing = mTdp.legacyDoSamplingDetectDuringSampleIntroduction
                    (tempPacket, bubbleBeginSetThisPacket);

            if (gotoProcessing) {

                return TestStateEventEnum.SampleProcessing;
            }
            // wait for next packet
            stateDataObject.startTimer();
            return TestStateEventEnum.Handled;

        } else if (message.getDescriptor().getType() == LegacyMessageType.TestStopped.value) {
            // user opened handle while in this state.. must write test results and exit
            mTdp.setErrorCode(ConvertTestStoppedToErrorCode(((LegacyRspTestStopped) message).getReason(), stateDataObject));

            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(ErrorCodeToErrorEventType(mTdp.getErrorCode(), TestStatusErrorType.ERROROCCURREDTESTSTOP));
            stateDataObject.postEvent(eventInfo);

            return TestStateEventEnum.EndTestAfterTestBegun;
        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value) {
            LegacyRspError error = (LegacyRspError) message;
            if (error.getErrorCode() != LegacyRspError.ErrorType.Undefined) {
                stateDataObject.getTestDataProcessor().setErrorCode(HostErrorCode.ErrorOccurredDuringFluidics);
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(ErrorCodeToErrorEventType(stateDataObject.getTestDataProcessor().getErrorCode(),
                        TestStatusErrorType.ERROROCCURREDTESTSTOP));

                return TestStateEventEnum.EndTestAfterTestBegun;
            }
        }

        if (stateDataObject.getTestStateAction() == TestStateActionEnum.CancelConnection) {
            mTdp.setErrorCode(HostErrorCode.UserStoppedTestDuringSampleIntro);
            stateDataObject.closePreviousTest();

            mThreadPool.shutdownNow();
        }

        return super.onEventPreHandle(stateDataObject);

    }

}
