package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.configuration.LegacyReqConfig1_2;
import com.epocal.reader.legacy.message.response.LegacyRspAck;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 1st 2018.
 */

public class WaitingForConfiguration1_2Ack extends LegacyTestState {
    public WaitingForConfiguration1_2Ack() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        // since we know the hcm is ok.. send self check configuration..
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

            if (resp.getAckType() == LegacyRspAck.AckType.Config1_2)
            {
                boolean prc ;

                // if a self test hasnt been performed yet.. then request one
                if (!stateDataObject.getTestDataProcessor().mSelfTestPerformed)
                {
                    prc = askForReaderStatus(stateDataObject, true, false);
                }
                else
                {
                    prc = askForReaderStatus(stateDataObject, false, false);
                }

                if (prc)
                {
                    return TestStateEventEnum.CheckReaderStatus;
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
        else if (message.getDescriptor().getType() == LegacyMessageType.Error.value)
        {
            // the actual field code for this scenario
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.READERERROR);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {
        LegacyReqConfig1_2 conMsg = new LegacyReqConfig1_2();
        stateDataObject.getTestDataProcessor().fillReqConfig1_2(conMsg);
        return stateDataObject.sendMessage(conMsg);
    }

}
