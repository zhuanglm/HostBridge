package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestDataProcessor;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.command.LegacyReqDestroyTest;
import com.epocal.reader.legacy.message.response.LegacyRspAck;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 28 2018.
 */

public class TestCompleted extends LegacyTestState {
    public TestCompleted() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        if (destroyTestResults(stateDataObject)) {
            stateDataObject.startTimer();
        } else {
            stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.CommunicationFailed);
        }
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

            if (resp.getAckType().value == LegacyRspAck.AckType.ResultsDestroyed.value) {
                stateDataObject.StopTimer();
                return TestStateEventEnum.LegacyCardInReader;
            }
        } else if (message.getDescriptor().getType() == LegacyMessageType.CardRemoved.value) {
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.CARDREMOVED);
            //stateDataObject.postEvent(eventInfo);

            return TestStateEventEnum.TestCompletedCardRemoved;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean destroyTestResults(TestStateDataObject stateDataObject) {

        TestDataProcessor tdp = stateDataObject.getTestDataProcessor();
        LegacyReqDestroyTest req = new LegacyReqDestroyTest((short) tdp.getTestRecord().getId()
                , (short) tdp.getErrorCode().value, (short) tdp.getErrorCode().value);
        return stateDataObject.sendMessage(req);
    }


}
