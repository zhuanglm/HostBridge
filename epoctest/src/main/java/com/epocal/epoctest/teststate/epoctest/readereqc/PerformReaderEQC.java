package com.epocal.epoctest.teststate.epoctest.readereqc;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.GenericActionResponse;
import com.epocal.reader.enumtype.GenericNotificationResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.TestNotificationResponse;
import com.epocal.reader.nextgen.message.response.generic.action.GenActRspEQC;
import com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCommandStarted;
import com.epocal.reader.nextgen.message.response.test.notification.TstNtfRspTimingUpdate;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 7/4/2017.
 */

public class PerformReaderEQC extends TestState {
    public PerformReaderEQC(){}

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
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
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        IMsgPayload message = stateDataObject.getIMsgPayload();
        if (message == null)
        {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        if ( message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.CommandStarted.value)
        {
            GenNtfRspCommandStarted resp = (GenNtfRspCommandStarted)message;
            if (resp == null)
            {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            if (resp.getMessageCode() == GenNtfRspCommandStarted.MessageCodeType.eQC.value)
            {
                stateDataObject.StopTimer();
                return super.onEventPreHandle(stateDataObject);
            }
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        if ( message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Test &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == TestNotificationResponse.TimingUpdate.value)
        {
            stateDataObject.StopTimer();
            TstNtfRspTimingUpdate resp = (TstNtfRspTimingUpdate)message;
            if (resp == null)
            {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            float duration = resp.getRemainingTime();
            return super.onEventPreHandle(stateDataObject);
        }

        if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Action &&
                message.getDescriptor().getType() == GenericActionResponse.eQC.value)
        {
            stateDataObject.StopTimer();
            GenActRspEQC resp = (GenActRspEQC)message;
            if (resp == null)
            {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            if (resp.getMessageCode() == GenActRspEQC.MessageCodeType.ActionStarted.value)
            {
                return super.onEventPreHandle(stateDataObject);
            }
            else if (resp.getMessageCode() == GenActRspEQC.MessageCodeType.ActionCompleted.value)
            {
                return super.onEventPreHandle(stateDataObject);
            }
            else if (resp.getMessageCode() == GenActRspEQC.MessageCodeType.Data.value)
            {
                if(!receiveEQCData(stateDataObject, resp))
                {
                    return TestStateEventEnum.EndTestBeforeTestBegun;
                }
            }
            return super.onEventPreHandle(stateDataObject);
        }

        if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.CommandFinished.value)
        {
            return TestStateEventEnum.PerformReaderEQCEnd;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean receiveEQCData(TestStateDataObject stateDataObject, GenActRspEQC data)
    {
        switch (GenActRspEQC.MessageCodeType.convert(data.getMessageCode()))
        {
            case Error:
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.EQCFAILED);
                stateDataObject.postEvent(eventInfo);
                return false;
            case Ack:
            case ActionStarted:
                break;
            case Data:
                //TODO: do something with it
                break;
            case ActionCompleted:
                break;
        }
        return true;
    }
}
