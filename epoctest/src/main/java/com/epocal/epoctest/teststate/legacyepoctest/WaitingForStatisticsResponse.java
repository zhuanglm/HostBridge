package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.command.LegacyReqStatistics;
import com.epocal.reader.legacy.message.response.LegacyRspStatistics;
import com.epocal.statemachine.IEventEnum;

import java.util.Collections;

/**
 * Created by rzhuang on July 31 2018.
 */

public class WaitingForStatisticsResponse extends LegacyTestState {
    public WaitingForStatisticsResponse() {
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

        if (message.getDescriptor().getType() == LegacyMessageType.StatisticsResponse.value) {
            stateDataObject.StopTimer();

            LegacyRspStatistics resp = (LegacyRspStatistics) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            if (stateDataObject.getTestDataProcessor().mStatisticsArrayList != null)
            {
                stateDataObject.getTestDataProcessor().mStatisticsArrayList.clear();
                stateDataObject.getTestDataProcessor().mStatisticsArrayList = null;
            }

            stateDataObject.getTestDataProcessor().mStatisticsArrayList = resp.statisticsList;
            Collections.sort(stateDataObject.getTestDataProcessor().mStatisticsArrayList);

            stateDataObject.getTestDataProcessor().mReaderRequiresMaintenance = stateDataObject
                 .getTestDataProcessor()
                 .DetermineWhetherReaderRequiresMaintenance(stateDataObject.getTestDataProcessor());

            return TestStateEventEnum.WaitingForDeviceEnableAck;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {
        LegacyReqStatistics getStats = new LegacyReqStatistics();
        getStats.setStatisticsFlag(LegacyReqStatistics.StatisticsType.maintenance);
        return stateDataObject.sendMessage(getStats);

    }
}
