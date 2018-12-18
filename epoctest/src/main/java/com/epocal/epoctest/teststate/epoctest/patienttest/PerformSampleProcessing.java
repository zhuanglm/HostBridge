package com.epocal.epoctest.teststate.epoctest.patienttest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.GenericNotificationResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.TestActionResponse;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspBGETest;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspDevicePeripheralMonitor;
import com.epocal.statemachine.IEventEnum;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dning on 7/24/2017.
 */

public class PerformSampleProcessing extends PerformTest {
    public PerformSampleProcessing(){}

    private TestStateDataObject mOneTimeStateDataObject;
    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        stateDataObject.getTestDataProcessor().setOneTimeFlag(false);
        stateDataObject.startTimer();
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        if(stateDataObject.getTestStateAction() == TestStateActionEnum.CommunicationFailed)
        {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }
        IMsgPayload message = stateDataObject.getIMsgPayload();
        if (message == null)
        {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }
        if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.CommandStarted.value)
        {
            stateDataObject.StopTimer();
            setCommandStatus(CommandStatus.started);
            setTestRunningStatus(TestRunningStatus.NoTestRunning.value);
            return TestStateEventEnum.Handled;
        }
        else if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.CommandFinished.value)
        {
            stateDataObject.StopTimer();
            setCommandStatus(CommandStatus.finished);
            return TestStateEventEnum.PerformTestEnd;
        }

        if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Test &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Action )
        {
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
            TstActRspBGETest bge = (TstActRspBGETest) message;
            if (bge == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            if (TstActRspBGETest.MessageCodeType.convert(bge.getMessageCode()) == TstActRspBGETest.MessageCodeType.Data) {
                if (!stateDataObject.getTestDataProcessor().getOneTimeFlag()) {
                    stateDataObject.getTestDataProcessor().setOneTimeFlag(true);
                    int delayTime = stateDataObject.getTestDataProcessor().getSamplingDelayDuration();
                    mOneTimeStateDataObject = stateDataObject;
                    mOneTimeStateDataObject.getTestDataProcessor().checkSampleInjectError(mOneTimeStateDataObject);
                }
            }
        }
        // wait for next packet
        //stateDataObject.EnableResponseTimer();
        return TestStateEventEnum.Handled;
    }
}