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
import com.epocal.reader.legacy.message.request.command.LegacyReqInvalidCard;
import com.epocal.reader.legacy.message.response.LegacyRspError;
import com.epocal.reader.legacy.message.response.LegacyRspStatus;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 22 2018.
 */

public class WaitingForReaderStatusHeaterTemperatures extends LegacyTestState
{
    public WaitingForReaderStatusHeaterTemperatures(){}

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);

        if (stateDataObject.mRedistributed)
        {
            stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.None);
        }
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject)
    {
        message = stateDataObject.getIMsgPayload();
        if (message == null) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }

        if(message.getDescriptor().getType() == LegacyMessageType.ReaderStatusResponse.value ||
                message.getDescriptor().getType() == LegacyMessageType.HandleStatus.value) {
            TestDataProcessor tdp = stateDataObject.getTestDataProcessor();
            if (message.getDescriptor().getType() == LegacyMessageType.ReaderStatusResponse.value)
            {
                tdp.mRsrMsg = (LegacyRspStatus)message;
            }
            //rsrMsg.hwStatus.bottomHeaterTemperature = 22;

            // when we get the card inserted, send config1_4
            boolean testConfigRC = tdp.getTestConfigData();

            // we checked for this already, but doesn't hurt to check again
            if (!testConfigRC)
            {
                stateDataObject.sendMessage(new LegacyReqInvalidCard());
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDTYPENOTSUPPORTED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.LegacyCardInReader;
            }

            tdp.legacyInitializeForNewTest();
            return TestStateEventEnum.WaitingForConfiguration1_4Ack;

        } else if (message.getDescriptor().getType() == LegacyMessageType.CardRemoved.value)
        {
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
        else if (message.getDescriptor().getType() == LegacyMessageType.Error.value)
        {
            LegacyRspError errMsg = (LegacyRspError)message;
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);

            if (errMsg.getErrorCode() == LegacyRspError.ErrorType.DetectionSwitchBounce)
            {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDINSERTEDNOTPROPERLY);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.LegacyCardInReader;
            }
            else
            {
//                LogServer.Instance.Log(new Log
//                {
//                    Context = epoc.common.types.LogContext.Host,
//                            Level = epoc.common.types.LogLevel.Debug,
//                            IsAlert = false,
//                            Message = "WaitingForTestGetReaderStatus: Reader " + stateDataObject.TestDataProcessor.TestRecord.Reader.SerialNumber +
//                                    ": Error message returned for pretest reader status"
//                });
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERERROR);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
        }

        return super.onEventPreHandle(stateDataObject);
    }

}
