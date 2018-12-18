package com.epocal.epoctest.teststate.epoctest.drycardcheck;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.GenericCommandResponse;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.TestPerformMode;
import com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqPerformDryCardCheck;
import com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspPerformDryCardCheck;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 7/18/2017.
 */

public class PerformDryCardCheckStart extends TestState {
    public PerformDryCardCheckStart() {
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
                return TestStateEventEnum.PerformDryCardCheck;
            }
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {
        GenCmdReqPerformDryCardCheck req = new GenCmdReqPerformDryCardCheck(GenCmdReqPerformDryCardCheck.MessageCodeType.Start);
        req.setTestPerformMode(TestPerformMode.Auto);
        req.setDryCardId(stateDataObject.getDryCardCheckID());
        return stateDataObject.sendMessage(req);
    }
}

