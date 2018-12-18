package com.epocal.epoctest.teststate.epoctest.drycardcheck;

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
import com.epocal.reader.enumtype.TestActionResponse;
import com.epocal.reader.nextgen.message.request.generic.ack.GenAckReqAck;
import com.epocal.reader.nextgen.message.request.test.ack.TstAckReqAck;
import com.epocal.reader.nextgen.message.response.generic.action.GenActRspDryCardCheck;
import com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCard;
import com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCommandFinished;
import com.epocal.reader.nextgen.message.response.generic.notification.GenNtfRspCommandStarted;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspBGETest;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspDevicePeripheralMonitor;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 7/18/2017.
 */

public class PerformDryCardCheck extends TestState {
    public PerformDryCardCheck() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        stateDataObject.startTimer();
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
                if (!sendGenericAckMessageToReader(stateDataObject)) {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.CancelTest;
                }
                stateDataObject.setCardInformation(resp.getCardInformation());
                return super.onEventPreHandle(stateDataObject);
            }
            if (resp.getResponse() == GenNtfRspCard.MessageCodeType.Engaged) {
                if (!sendGenericAckMessageToReader(stateDataObject)) {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.EndTestBeforeTestBegun;
                }
                return super.onEventPreHandle(stateDataObject);
            }
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        } else if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.CommandStarted.value) {
            stateDataObject.StopTimer();
            GenNtfRspCommandStarted resp = (GenNtfRspCommandStarted) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            if (resp.getMessageCode() == GenNtfRspCommandStarted.MessageCodeType.DryCard.value) {
                return super.onEventPreHandle(stateDataObject);
            }
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        } else if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.CommandFinished.value) {
            stateDataObject.StopTimer();
            GenNtfRspCommandFinished resp = (GenNtfRspCommandFinished) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            if (resp.getMessageCode() == GenNtfRspCommandFinished.MessageCodeType.DryCard.value) {

                return TestStateEventEnum.PerformDryCardCheckEnd;
            }
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }

        if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Action &&
                message.getDescriptor().getType() == GenericActionResponse.DryCardCheck.value) {
            stateDataObject.StopTimer();
            GenActRspDryCardCheck resp = (GenActRspDryCardCheck) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            if (resp.getMessageCode() == GenActRspDryCardCheck.MessageCodeType.Ack.value) {
                return super.onEventPreHandle(stateDataObject);
            }
            else if (resp.getMessageCode() == GenActRspDryCardCheck.MessageCodeType.ActionStarted.value) {
                return super.onEventPreHandle(stateDataObject);
            } else if (resp.getMessageCode() == GenActRspDryCardCheck.MessageCodeType.ActionCompleted.value) {
                return super.onEventPreHandle(stateDataObject);
            } else if (resp.getMessageCode() == GenActRspDryCardCheck.MessageCodeType.Data.value) {
                if (!receiveDDCData(stateDataObject, resp)) {
                    return TestStateEventEnum.CancelTest;
                }
                if (!sendGenericAckMessageToReader(stateDataObject)) {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.CancelTest;
                }
            }
            return super.onEventPreHandle(stateDataObject);
        } else if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Test &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Action &&
                message.getDescriptor().getType() == TestActionResponse.DevicePeripheralMonitor.value) {
            stateDataObject.StopTimer();
            TstActRspDevicePeripheralMonitor resp = (TstActRspDevicePeripheralMonitor) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            if (resp.getMessageCode() == TstActRspDevicePeripheralMonitor.MessageCodeType.Ack.value) {
                return super.onEventPreHandle(stateDataObject);
            }
            else if (resp.getMessageCode() == TstActRspDevicePeripheralMonitor.MessageCodeType.ActionStarted.value) {
                return super.onEventPreHandle(stateDataObject);
            } else if (resp.getMessageCode() == TstActRspDevicePeripheralMonitor.MessageCodeType.ActionCompleted.value) {
                return super.onEventPreHandle(stateDataObject);
            } else if (resp.getMessageCode() == TstActRspDevicePeripheralMonitor.MessageCodeType.Data.value) {
                if (!receiveDPMData(stateDataObject, resp)) {
                    return TestStateEventEnum.CancelTest;
                }
                if (!sendGenericAckMessageToReader(stateDataObject)) {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.CancelTest;
                }
            }
            return super.onEventPreHandle(stateDataObject);
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean receiveDDCData(TestStateDataObject stateDataObject, GenActRspDryCardCheck data) {
        return true;
    }

    private boolean receiveDPMData(TestStateDataObject stateDataObject, TstActRspDevicePeripheralMonitor data) {
        return true;
    }

    private boolean sendGenericAckMessageToReader(TestStateDataObject stateDataObject) {
        GenAckReqAck req = new GenAckReqAck();
        return stateDataObject.sendMessage(req);
    }
}
