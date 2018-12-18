package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.configuration.LegacyReqConfig1_3;
import com.epocal.reader.legacy.message.response.LegacyRspAck;
import com.epocal.reader.protocolcommontype.Sequence;
import com.epocal.statemachine.IEventEnum;

import java.util.ArrayList;

/**
 * Created by rzhuang on Aug 9 2018.
 */

public class WaitingForConfiguration1_3Ack extends LegacyTestState {
    public WaitingForConfiguration1_3Ack() {
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

            if (resp.getAckType() == LegacyRspAck.AckType.Config1_1 || resp.getAckType() == LegacyRspAck.AckType.Config1_3)
            {
                stateDataObject.getTestDataProcessor().mCanRunThermalCheck = true;
                return TestStateEventEnum.Ready;
            }

            return TestStateEventEnum.Handled;
        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value)
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

        int i;

        // send out configuration1_3
        LegacyReqConfig1_3 conMsg = new LegacyReqConfig1_3();
        stateDataObject.getTestDataProcessor().mDryCardSequence = new ArrayList();
        //load config into LegacyReqConfig1_3
        stateDataObject.getTestDataProcessor().getDryCardCheckData(conMsg);
        stateDataObject.getTestDataProcessor().getDryCardCheckSequence(
                stateDataObject.getTestDataProcessor().mDryCardSequence);
        conMsg.setSequenceLength((byte)stateDataObject.getTestDataProcessor().mDryCardSequence.size());

        conMsg.setContinueOnWetCard(false);

        for (i = 0; i < stateDataObject.getTestDataProcessor().mDryCardSequence.size(); i++)
        {
            conMsg.sampleSequence[i] = (Sequence) stateDataObject.getTestDataProcessor().mDryCardSequence.get(i);
        }
        return stateDataObject.sendMessage(conMsg);
    }

}
