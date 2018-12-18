package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.response.LegacyRspAck;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 28 2018.
 */

public class TestCompletedCardRemoved extends LegacyTestState {
    public TestCompletedCardRemoved() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);

    }


    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        message = stateDataObject.getIMsgPayload();
        TestEventInfo eventInfo = new TestEventInfo();

        if (message == null) {
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }

        if (message.getDescriptor().getType() == LegacyMessageType.Ack.value) {
            LegacyRspAck resp = (LegacyRspAck) message;
            if (resp == null) {
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            if (resp.getAckType() == LegacyRspAck.AckType.ResultsDestroyed) {
                stateDataObject.StopTimer();
                return TestStateEventEnum.Ready;
            }
        }
        return super.onEventPreHandle(stateDataObject);
    }

}
