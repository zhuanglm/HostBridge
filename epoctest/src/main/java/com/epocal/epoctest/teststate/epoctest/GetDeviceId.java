package com.epocal.epoctest.teststate.epoctest;

import com.epocal.common.androidutil.FileSystemUtil;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusType;
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
import com.epocal.reader.nextgen.message.request.generic.command.GenCmdReqDeviceId;
import com.epocal.reader.nextgen.message.response.generic.command.GenCmdRspDeviceId;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 6/14/2017.
 */

public class GetDeviceId extends TestState {
    public GetDeviceId() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        TestEventInfo eventInfo = new TestEventInfo();
        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
        eventInfo.setTestStatusType(TestStatusType.CONFIGURATION);
        stateDataObject.postEvent(eventInfo);
        if (SendMessageToReader(stateDataObject)) {
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

        if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Command &&
                message.getDescriptor().getType() == GenericCommandResponse.DeviceIdResponse.value) {
            stateDataObject.StopTimer();
            GenCmdRspDeviceId resp = (GenCmdRspDeviceId) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            if (resp.getMessageCode() == GenCmdRspDeviceId.MessageCodeType.Error.value || resp.getMessageCode() == GenCmdRspDeviceId.MessageCodeType.NotDefined.value) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            stateDataObject.getTestDataProcessor().getDeviceIdInfoData().setDeviceIdAdditionalIdInfo(resp.getAdditionalIdInfo());
            stateDataObject.getTestDataProcessor().getDeviceIdInfoData().setDeviceIdMainIdInfo(resp.getMainIdInfo());
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.DEVICEINFO);
            //eventInfo.setTestEventData(stateDataObject.getReaderDevice());
            //stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.GetDeviceStatus;
            //return TestStateEventEnum.SendHostIdInfo;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean SendMessageToReader(TestStateDataObject stateDataObject) {
        GenCmdReqDeviceId req = new GenCmdReqDeviceId(GenCmdReqDeviceId.MessageCodeType.MainId);
        return stateDataObject.sendMessage(req);
    }
}
