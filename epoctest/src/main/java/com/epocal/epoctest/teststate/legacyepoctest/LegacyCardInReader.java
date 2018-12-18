package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 20 2018.
 */

public class LegacyCardInReader extends LegacyTestState {
    public LegacyCardInReader() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        stateDataObject.getTestDataProcessor().mTestState = false;
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        // used card is in reader.. only thing we want to handle is a card removed
        message = stateDataObject.getIMsgPayload();
        if (message == null) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }

        if (message.getDescriptor().getType() == LegacyMessageType.CardRemoved.value) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.CARDREMOVED);
            stateDataObject.postEvent(eventInfo);
            //keep this commented code
            //if (stateDataObject.TestParameter.readerRequiresMaintenance && epoc.host.configuration.GlobalConfiguration.Instance.HostConfiguration.Record.ReaderMaintenanceEnabled)
            //{
            //    RaiseEvent(stateDataObject, TestInternalState.TestError, epoc.common.types.TestStateErrrorType.ReaderNeedMaintenance);
            //}

            return TestStateEventEnum.Ready;
        }
        return super.onEventPreHandle(stateDataObject);
    }

}
