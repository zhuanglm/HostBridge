package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.response.LegacyRspStatus;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on July 31 2018.
 */

public class CheckFirstReaderStatus extends LegacyTestState {
    public CheckFirstReaderStatus() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);

        stateDataObject.startTimer();

        stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.None);
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

        if (message.getDescriptor().getType() == LegacyMessageType.ReaderStatusResponse.value) {
            stateDataObject.StopTimer();

            LegacyRspStatus resp = (LegacyRspStatus) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            stateDataObject.getTestDataProcessor().mRsrMsg = resp;

            stateDataObject.getTestDataProcessor().fillTQAInfo();

            // check hcm
            if (stateDataObject.getTestDataProcessor().mRsrMsg.isHCMError()) {
                // get the hcm configuration and wait on a response
                return TestStateEventEnum.WaitingForHCMResponse;
            } else {
                return TestStateEventEnum.WaitingForConfiguration1_2Ack;
            }
        }
        return super.onEventPreHandle(stateDataObject);
    }

}
