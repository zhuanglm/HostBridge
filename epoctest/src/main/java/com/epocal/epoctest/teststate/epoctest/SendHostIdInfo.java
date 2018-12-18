package com.epocal.epoctest.teststate.epoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.common.EpocVersion;
import com.epocal.reader.enumtype.GenericCommandResponse;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqHostId;
import com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspHostId;
import com.epocal.reader.type.HostIdInfo;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 6/15/2017.
 */

public class SendHostIdInfo extends TestState {
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
                message.getDescriptor().getType() == GenericCommandResponse.HostIdInfo.value) {
            stateDataObject.StopTimer();
            GenCmdRspHostId resp = (GenCmdRspHostId) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            if (resp.getMessageCode() == GenCmdRspHostId.MessageCodeType.Error.value || resp.getMessageCode() == GenCmdRspHostId.MessageCodeType.NotDefined.value) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            return TestStateEventEnum.GetDeviceStatus;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {
        HostIdInfo hostIdInfo = new HostIdInfo();
        hostIdInfo.setHostId(new EpocVersion(1, 0, 0, 0));             //***new ReaderProtocolLibrary.Common.EpocVersion(stateDataObject.TestDataProcessor.TestRecord.Host.SoftwareVersion);
        hostIdInfo.setSoftwareVersion(new EpocVersion(1, 0, 0, 0));    //***new ReaderProtocolLibrary.Common.EpocVersion(stateDataObject.TestDataProcessor.TestRecord.Host.SoftwareVersion);
        hostIdInfo.setType((byte) 0);
        hostIdInfo.setCompileType((byte) 0);
        GenCmdReqHostId req = new GenCmdReqHostId();
        return stateDataObject.sendMessage(req);
    }
}
