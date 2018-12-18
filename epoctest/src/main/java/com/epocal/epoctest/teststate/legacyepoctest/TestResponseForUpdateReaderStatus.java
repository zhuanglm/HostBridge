package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.command.LegacyReqHCM;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 7 2018.
 */

public class TestResponseForUpdateReaderStatus extends LegacyTestState {
    public TestResponseForUpdateReaderStatus() {
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

        if (message.getDescriptor().getType() == LegacyMessageType.CustomConfigurationResponse.value) {
            stateDataObject.StopTimer();

//            CustomConfigurationResponse resp = (CustomConfigurationResponse) message;
//            if (resp == null) {
//                TestEventInfo eventInfo = new TestEventInfo();
//                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
//                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
//                stateDataObject.postEvent(eventInfo);
//                return TestStateEventEnum.EndTestBeforeTestBegun;
//            }

            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.HCMERROR);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        return super.onEventPreHandle(stateDataObject);
    }

//    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {
//
//        //stateDataObject.sendMessage(new GetHCM());
//        LegacyReqHCM req = new LegacyReqHCM();
//        return stateDataObject.sendMessage(req);
//    }

}
