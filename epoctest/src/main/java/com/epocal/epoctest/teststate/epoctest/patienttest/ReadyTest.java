package com.epocal.epoctest.teststate.epoctest.patienttest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.GenericNotificationResponse;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.nextgen.message.request.generic.ack.GenAckReqAck;
import com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCard;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 7/5/2017.
 */

public class ReadyTest extends TestState {
    public ReadyTest() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        TestEventInfo eventInfo = new TestEventInfo();
        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
        eventInfo.setTestStatusType(TestStatusType.READYTOTEST);
        stateDataObject.postEvent(eventInfo);
        stateDataObject.StopTimer();
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.CommunicationFailed) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        IMsgPayload message = stateDataObject.getIMsgPayload();
        if (message == null) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.Card.value) {
            GenNtfRspCard resp = (GenNtfRspCard) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            if (resp.getResponse() == GenNtfRspCard.MessageCodeType.Detected) {
                //send ACK to reader
                if (!sendAckMessageToReader(stateDataObject)) {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.EndTestBeforeTestBegun;
                }
                return TestStateEventEnum.EnableTest; //detect the card and need to enable the test
            } else if (resp.getResponse() == GenNtfRspCard.MessageCodeType.Inserted) {
                if (!sendAckMessageToReader(stateDataObject)) {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.EndTestBeforeTestBegun;
                }
                return super.onEventPreHandle(stateDataObject);
            }
            if (resp.getResponse() == GenNtfRspCard.MessageCodeType.Removed) {
                if (!sendAckMessageToReader(stateDataObject)) {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.EndTestBeforeTestBegun;
                }
                //card removed from reader
                return super.onEventPreHandle(stateDataObject);
            }
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendAckMessageToReader(TestStateDataObject stateDataObject) {
        GenAckReqAck req = new GenAckReqAck();
        return stateDataObject.sendMessage(req);
    }
}
