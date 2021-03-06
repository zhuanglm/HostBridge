package com.epocal.epoctest.teststate.epoctest;

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
import com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqDeviceStatus;
import com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspDeviceStatus;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 7/4/2017.
 */

public class GetDeviceStatusBeforeTestStart extends TestState {
    public GetDeviceStatusBeforeTestStart() {
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
                message.getDescriptor().getMsgGroup() == MessageGroup.Command &&
                message.getDescriptor().getType() == GenericCommandResponse.DeviceStatusResponse.value) {
            stateDataObject.StopTimer();
            GenCmdRspDeviceStatus resp = (GenCmdRspDeviceStatus) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            if (resp.getMessageCode() == GenCmdRspDeviceStatus.MessageCodeType.Error.value || resp.getMessageCode() == GenCmdRspDeviceStatus.MessageCodeType.NotDefined.value) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            stateDataObject.getTestDataProcessor().setDeviceStatusData(resp.getDeviceStatusData());
            return TestStateEventEnum.PatientTest;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {
        GenCmdReqDeviceStatus req = new GenCmdReqDeviceStatus(GenCmdReqDeviceStatus.MessageCodeType.NotDefined);
        return stateDataObject.sendMessage(req);
    }
}
