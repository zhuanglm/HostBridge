package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.command.LegacyReqEnableReader;
import com.epocal.reader.legacy.message.response.LegacyRspAck;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 1st 2018.
 */

public class WaitingForDeviceEnableAck extends LegacyTestState {
    public WaitingForDeviceEnableAck() {
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
        message = stateDataObject.getIMsgPayload();
        if (message == null) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }

        if (message.getDescriptor().getType() == LegacyMessageType.Ack.value) {

            LegacyRspAck resp = (LegacyRspAck) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            if (resp.getAckType() == LegacyRspAck.AckType.DeviceEnable)
            {
                stateDataObject.StopTimer();
                //stageTimer.Enabled = false;

                // request the reader status.. we must know if the hcm is corrupted before asking it for
                // a self-test
                if (askForReaderStatus(stateDataObject, false, true))
                {
                    // we are waiting for the reader status message.. no timer for now
                    return TestStateEventEnum.CheckFirstReaderStatus;
                }
                else
                {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.EndTestBeforeTestBegun;
                }
            }
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {
        LegacyReqEnableReader enableReader = new LegacyReqEnableReader();
        enableReader.setReaderMaintenanceDisabled(LegacyReqEnableReader.ReaderMaintenanceFlag.enabled);
        enableReader.setReaderHardwareDisabled(LegacyReqEnableReader.ReaderHardwareEnabledFlag.enabled);
        stateDataObject.sendMessage(enableReader);

        return stateDataObject.sendMessage(enableReader);

    }

}
