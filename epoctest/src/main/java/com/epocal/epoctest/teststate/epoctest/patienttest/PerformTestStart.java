package com.epocal.epoctest.teststate.epoctest.patienttest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.GenericCommandResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.TestPerformMode;
import com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqPerformTest;
import com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspPerformTest;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 7/24/2017.
 */

public class PerformTestStart extends TestState {
    boolean doing = false;
    public PerformTestStart() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        doing = false;
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
        if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Command &&
                message.getDescriptor().getType() == GenericCommandResponse.PerformTest.value) {
            stateDataObject.StopTimer();
            GenCmdRspPerformTest resp = (GenCmdRspPerformTest) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            if (resp.getMessageCode() == GenCmdRspPerformTest.MessageCodeType.Ack.value) {
                if(!doing) {
                    doing = true;
                    stateDataObject.initializeForNewTest();
                    return TestStateEventEnum.PerformFluidCalibration;
                }
            }
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {
        GenCmdReqPerformTest req = new GenCmdReqPerformTest(GenCmdReqPerformTest.MessageCodeType.Start);
        req.setTestPerformMode(TestPerformMode.Auto);
        req.setTestId(stateDataObject.getTestID());
        return stateDataObject.sendMessage(req);
    }
}
