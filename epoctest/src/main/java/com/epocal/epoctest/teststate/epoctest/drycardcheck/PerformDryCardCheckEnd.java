package com.epocal.epoctest.teststate.epoctest.drycardcheck;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.enumtype.TestProcessStateReturnCode;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.GenericCommandResponse;
import com.epocal.reader.enumtype.GenericNotificationResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.nextgen.message.request.generic.ack.GenAckReqAck;
import com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqPerformDryCardCheck;
import com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspPerformDryCardCheck;
import com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCard;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 7/19/2017.
 */

public class PerformDryCardCheckEnd extends TestState {
    public PerformDryCardCheckEnd() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        if (sendMessageToReader(stateDataObject)) {
            stateDataObject.startTimer();
        } else {
            stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.CommunicationFailed);
        }
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.CommunicationFailed) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }
        IMsgPayload message = stateDataObject.getIMsgPayload();
        if (message == null) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }
        if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Command &&
                message.getDescriptor().getType() == GenericCommandResponse.PerformDryCard.value) {
            stateDataObject.StopTimer();
            GenCmdRspPerformDryCardCheck resp = (GenCmdRspPerformDryCardCheck) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            if (resp.getMessageCode() == GenCmdRspPerformDryCardCheck.MessageCodeType.Ack.value) {
                return TestStateEventEnum.Handled;
            }
        }
        if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.Card.value) {
            stateDataObject.StopTimer();
            GenNtfRspCard resp = (GenNtfRspCard) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            if (resp.getMessageCode() == GenNtfRspCard.MessageCodeType.CardInformation.value) {
                if (!sendNotificationCardAckMessageToReader(stateDataObject)) {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.CancelTest;
                }
                stateDataObject.setCardInformation(resp.getCardInformation());
                if (resp.getCardInformation().getDryCardCheckResults()== 0)
                {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.FLUIDDETECTEDINCARD);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.UsedCardInReader;
                }
                if(!stateDataObject.validateEQCTest())
                {
                    //terminate test, disconnect communication, redo EQC
                    return TestStateEventEnum.CancelTest;
                }
                if(!stateDataObject.validateEQCTest())
                {
                    //terminate test, disconnect communication, redo EQC
                    return TestStateEventEnum.CancelTest;
                }
                if(!stateDataObject.validateTestCardType())
                {
                    return TestStateEventEnum.CardInReader;
                }
                TestProcessStateReturnCode retCode = stateDataObject.validateAvailableAnalytes();
                if(retCode == TestProcessStateReturnCode.RemoveCard)
                {
                    return TestStateEventEnum.CardInReader;
                }
                else if(retCode == TestProcessStateReturnCode.StopTest)
                {
                    return TestStateEventEnum.CancelTest;
                }
                return TestStateEventEnum.AcceptCard;
            }

            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {
        GenCmdReqPerformDryCardCheck req = new GenCmdReqPerformDryCardCheck(GenCmdReqPerformDryCardCheck.MessageCodeType.Stop);
        return stateDataObject.sendMessage(req);
    }

    private boolean sendNotificationCardAckMessageToReader(TestStateDataObject stateDataObject) {
        GenAckReqAck req = new GenAckReqAck();
        return stateDataObject.sendMessage(req);
    }
}
