package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestDataProcessor;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.legacy.message.request.command.LegacyReqDestroyTest;
import com.epocal.reader.legacy.message.response.LegacyRspStatistics;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.statemachine.IEventEnum;

import java.util.Calendar;

/**
 * Created by rzhuang on Sep 20 2018.
 */

public class EndTestAfterTestBegun extends LegacyTestState {
    public EndTestAfterTestBegun() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);

        stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.None);
    }


    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        stateDataObject.StopTimer();

        if (stateDataObject.getTestCommunication().isConnected()) {
            TestDataProcessor tdp = stateDataObject.getTestDataProcessor();
            LegacyReqDestroyTest req = new LegacyReqDestroyTest((short) tdp.getTestRecord().getId()
                    , (short) tdp.getErrorCode().value, (short) tdp.getErrorCode().value);
            stateDataObject.sendMessage(req);

            if (tdp.mStatisticsArrayList != null) {
                // test just finished, so add it to the stats arraylist so that it counts in the future
                tdp.mStatisticsArrayList.add(0, new LegacyRspStatistics.IndividualStatistics(
                        (short) tdp.getTestRecord().getId()
                        , (short) tdp.getErrorCode().value, (short) tdp.getErrorCode().value));

                tdp.mReaderRequiresMaintenance = tdp.DetermineWhetherReaderRequiresMaintenance(tdp);
            }

            tdp.saveTestRecordWhenError();


            if (tdp.getErrorCode() == HostErrorCode.NoError) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.ENDTESTAFTERTESTBEGUN);
                stateDataObject.postEvent(eventInfo);
            }

            // for all error scenarios that force a test to end early, this will close
            // the old packet and test data file and cause new files to be opened
            //tdp.closeTestDataFile(HostErrorCode[(int)stateDataObject.TestDataProcessor.errorCode], stateDataObject.TestParameter);
            //tdp.closePacketFile("Test Ended abnormally with cause `" + epoc.host.utilities.Strings.HostErrorCodeStrings[(int)stateDataObject.TestDataProcessor.errorCode] + "`");
            tdp.closePacketFile();
            tdp.setConnectionTime(Calendar.getInstance());

            if (tdp.mReaderRequiresMaintenance /*&& tdp.getHostConfiguration().ReaderMaintenanceEnabled*/) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNEEDMAINTENANCE);
                stateDataObject.postEvent(eventInfo);
            } else if (tdp.getErrorCode() == HostErrorCode.NoError) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.OLDCARDINREADER);
                stateDataObject.postEvent(eventInfo);
            }
        }

        return TestStateEventEnum.LegacyCardInReader;
    }

}
