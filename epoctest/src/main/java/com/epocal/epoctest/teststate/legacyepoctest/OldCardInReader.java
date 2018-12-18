package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.response.LegacyRspAck;
import com.epocal.reader.legacy.message.response.LegacyRspError;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 9 2018.
 */

public class OldCardInReader extends LegacyTestState {
    public OldCardInReader() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
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

        // only thing we're interested in is card removed
        if (message.getDescriptor().getType() == LegacyMessageType.CardRemoved.value) {
            // once card is removed, get the teststatus again and continue
            // as there may be other errors.

            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.CONFIGURATION);
            stateDataObject.postEvent(eventInfo);

            boolean prc;
            prc = askForReaderStatus(stateDataObject, true, false);
            if (prc) {
                return TestStateEventEnum.CheckReaderStatus;
            } else {
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
        } else if (message.getDescriptor().getType() == LegacyMessageType.Ack.value) {
            LegacyRspAck resp = (LegacyRspAck) message;
            if (resp.getAckType() == LegacyRspAck.AckType.ResultsDestroyed)
                return TestStateEventEnum.Handled;
        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value) {
            // this message may occur at exactly the same time as the reader status, so we
            // will get this AFTER the reader status instead of before.. so we need to discard
            // it here otherwise it looks like some sort of hardware failure
            LegacyRspError resp = (LegacyRspError) message;
            if (resp.getMessageCode() == LegacyRspError.ErrorType.CardInsertedWhileMotorMoving.value) {
                return TestStateEventEnum.Handled;
            }
        }
        return super.onEventPreHandle(stateDataObject);
    }

}

