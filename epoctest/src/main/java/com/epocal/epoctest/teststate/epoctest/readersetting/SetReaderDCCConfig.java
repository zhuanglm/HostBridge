package com.epocal.epoctest.teststate.epoctest.readersetting;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.GenericConfigurationResponse;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.nextgen.message.request.generic.configuration.GenCfgReqDryCardCheck;
import com.epocal.reader.nextgen.message.response.generic.configuration.GenCfgRspDryCardCheck;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 6/29/2017.
 */

public class SetReaderDCCConfig extends TestState {
    public SetReaderDCCConfig() {
    }

    private boolean mConfigNeedToUpdateSet = false;
    private boolean mConfigNeedToUpdateGet = false;

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        // TODO: 6/28/2017 check configuration version to see if configuration need to be updated
        // (from reader to host or from host to reader)
        if (mConfigNeedToUpdateSet) {
            if (sendMessageToReaderSet(stateDataObject)) {
                stateDataObject.startTimer();
            } else {
                stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.CommunicationFailed);
            }
        } else if (mConfigNeedToUpdateGet) {
            if (sendMessageToReaderGet(stateDataObject)) {
                stateDataObject.startTimer();
            } else {
                stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.CommunicationFailed);
            }
        } else {
            stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.NextStep);
        }
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.NextStep) {
            return TestStateEventEnum.DisableReaderSettingsSession;
        }
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
                message.getDescriptor().getMsgGroup() == MessageGroup.Configuration &&
                message.getDescriptor().getType() == GenericConfigurationResponse.DryCardCheck.value) {
            stateDataObject.StopTimer();
            GenCfgRspDryCardCheck resp = (GenCfgRspDryCardCheck) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            if (resp.getMessageCode() == GenCfgRspDryCardCheck.MessageCodeType.Error.value || resp.getMessageCode() == GenCfgRspDryCardCheck.MessageCodeType.NotDefined.value) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            } else if (resp.getMessageCode() == GenCfgRspDryCardCheck.MessageCodeType.Ack.value) {
                return TestStateEventEnum.DisableReaderSettingsSession;
            } else //configuration received
            {
                //// TODO: 6/28/2017 update configuration which received from reader
            }
            return TestStateEventEnum.DisableReaderSettingsSession;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReaderSet(TestStateDataObject stateDataObject) {
        GenCfgReqDryCardCheck req = new GenCfgReqDryCardCheck();
        req.setMessageCode(GenCfgReqDryCardCheck.MessageCodeType.Set.value);
        return stateDataObject.sendMessage(req);
    }

    private boolean sendMessageToReaderGet(TestStateDataObject stateDataObject) {
        GenCfgReqDryCardCheck req = new GenCfgReqDryCardCheck();
        req.setMessageCode(GenCfgReqDryCardCheck.MessageCodeType.Get.value);
        return stateDataObject.sendMessage(req);
    }
}