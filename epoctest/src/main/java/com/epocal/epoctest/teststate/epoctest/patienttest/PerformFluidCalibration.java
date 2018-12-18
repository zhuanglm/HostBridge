package com.epocal.epoctest.teststate.epoctest.patienttest;

import com.epocal.common.types.am.RealTimeHematocritQCReturnCode;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.TestNotificationResponse;
import com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCommandFinished;
import com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCommandStarted;
import com.epocal.reader.nextgen.message.response.test.notification.TstNtfRspTimingUpdate;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.reader.enumtype.DAMStageType;
import com.epocal.reader.enumtype.GenericNotificationResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.TestActionResponse;
import com.epocal.reader.nextgen.message.request.test.ack.TstAckReqAck;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspBGETest;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspDevicePeripheralMonitor;
import com.epocal.statemachine.IEventEnum;
import com.epocal.util.StringUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by dning on 7/24/2017.
 */

public class PerformFluidCalibration extends PerformTest {
    private Observable mCountdownObservable;
    private Disposable mDisposable;
    private TestStateDataObject mStateDataObject;

    public PerformFluidCalibration() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        stateDataObject.startTimer();
        this.mStateDataObject = stateDataObject;
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.CommunicationFailed) {
            StopCountDownFluidCalibration();
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }
        IMsgPayload message = stateDataObject.getIMsgPayload();
        if (message == null) {
            StopCountDownFluidCalibration();
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }
        if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.CommandStarted.value) {
            stateDataObject.StopTimer();
            GenNtfRspCommandStarted resp = (GenNtfRspCommandStarted)message;
            if(resp.getResponse() == GenNtfRspCommandStarted.MessageCodeType.Test)
            {
                setCommandStatus(CommandStatus.started);
                setTestRunningStatus(TestRunningStatus.NoTestRunning.value);

                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                eventInfo.setTestStatusType(TestStatusType.FLUIDICSCALIBRATION);
            }
            return TestStateEventEnum.Handled;
        } else if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.CommandFinished.value) {
            StopCountDownFluidCalibration();
            return TestStateEventEnum.Handled;
        }

        if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Test &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == TestNotificationResponse.TimingUpdate.value) {
            TstNtfRspTimingUpdate resp = (TstNtfRspTimingUpdate)message;
            if(resp.getDAMStageType() == DAMStageType.FluidicsCalibration) {
                StartCountDownFluidCalibration(Math.round(resp.getRemainingTime()));
            }
        }

        if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Test &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Action) {
            IEventEnum eret = handleBGEDataPacket(stateDataObject, message);
            if(eret == TestStateEventEnum.Handled)
            {
                return handleMeasurements(stateDataObject, message);
            }
            return eret;
        }

        return super.onEventPreHandle(stateDataObject);
    }

    private IEventEnum handleMeasurements(TestStateDataObject stateDataObject, IMsgPayload message)
    {
        if (message.getDescriptor().getMsgClass() == MessageClass.Test &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Action &&
                message.getDescriptor().getType() == TestActionResponse.BGETest.value) {
            if (message == null) {
                StopCountDownFluidCalibration();
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            TstActRspBGETest bge = (TstActRspBGETest) message;
            if (TstActRspBGETest.MessageCodeType.convert(bge.getMessageCode()) == TstActRspBGETest.MessageCodeType.Data) {
                if (bge.getData().getDAMStage() == DAMStageType.BubbleDetectFluid || bge.getData().getDAMStage() == DAMStageType.BubbleDetectAir) {
                    StopCountDownFluidCalibration();
                    stateDataObject.getTestDataProcessor().endFluidCalibration();
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                    eventInfo.setTestStatusType(TestStatusType.SAMPLEINTRODUCTION);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.PerformSampleIntroduction;
                }
            }
            if (!stateDataObject.getTestDataProcessor().checkEarlyInjection(stateDataObject, bge))
            {
                return TestStateEventEnum.CancelTest;
            }
            return TestStateEventEnum.Handled;
        }
        return TestStateEventEnum.Handled;
    }

    private void StartCountDownFluidCalibration(final int totalTimeInSeconds) {
        // Check to make sure previous one is disposed
        StopCountDownFluidCalibration();

        // Start new interval timer
        mCountdownObservable = Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return (totalTimeInSeconds - aLong) == 0;
                    }
                })
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        int remainingTime = (int) (totalTimeInSeconds - aLong);
                        return StringUtil.timeInSecondToTimeString(remainingTime);
                    }
                });

        mDisposable = mCountdownObservable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                eventInfo.setTestStatusType(TestStatusType.FLUIDICSCALIBRATION);
                eventInfo.setTestEventData(o);
                mStateDataObject.postEvent(eventInfo);
            }
        });
    }

    private void StopCountDownFluidCalibration() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            mCountdownObservable = null;
        }
    }
}
