package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.ConfigBlockFlag;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.configuration.LegacyReqConfig1_1;
import com.epocal.reader.legacy.message.response.LegacyRspAck;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 9 2018.
 */

public class WaitingForConfiguration1_1Ack extends LegacyTestState {
    public WaitingForConfiguration1_1Ack() {
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
            stateDataObject.StopTimer();

            LegacyRspAck resp = (LegacyRspAck) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            if (resp.getAckType() == LegacyRspAck.AckType.Config1_1) {
                return TestStateEventEnum.WaitingForConfiguration1_3Ack;

            }

        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value) {
            // the actual field code for this scenario
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.READERERROR);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    protected boolean sendMessageToReader(TestStateDataObject stateDataObject) {
        // no error. go ahead and exchange configuration information
        LegacyReqConfig1_1 config11Msg = new LegacyReqConfig1_1();
        config11Msg.setBlockFlag((byte) (ConfigBlockFlag.General.value | ConfigBlockFlag.MaintenanceTestRecordNumber.value));
        config11Msg.setNumMaintenanceRecords(stateDataObject.getTestDataProcessor().mNumMaintenanceRecordsToStore);

        stateDataObject.getTestDataProcessor().getTimers(config11Msg);

        return stateDataObject.sendMessage(config11Msg);
    }

}
