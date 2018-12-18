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
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 21 2018.
 */

public class WaitingForTestGetReaderStatus extends LegacyTestState {
    public WaitingForTestGetReaderStatus() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);

        if (stateDataObject.mRedistributed) {
            stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.None);
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

        if (message.getDescriptor().getType() == LegacyMessageType.CardInserted.value) {
            //stateDataObject.StopTimer();

            // we got the reader status response.. save it so we'll have the ambient
            // temperature. the old rsrMsg will disappear. go garbage collector
            //RaiseEvent(stateDataObject, epoc.common.types.TestStateType.ReaderBattery, (int)stateDataObject.TestDataProcessor.rsrMsg.HwStatus.batteryLevel);

            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);

            TestDataProcessor tdp = stateDataObject.getTestDataProcessor();
            if (tdp.mRsrMsg.hwStatus.getAmbientTemp1() < tdp.mLowTemperatureLimit) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.LOWTEMPERATURELIMIT);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            } else if (tdp.mRsrMsg.hwStatus.getAmbientTemp1() > tdp.mHighTemperatureLimit) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.HIGHTEMPERATURELIMIT);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            } else if (tdp.mRsrMsg.hwStatus.getBarometricPressureSensor1() < tdp.mLowPressureLimit) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.LOWPRESSURELIMIT);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            } else if (tdp.mRsrMsg.hwStatus.getBarometricPressureSensor1() > tdp.mHighPressureLimit) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.HIGHPRESSURELIMIT);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            } else if (!tdp.isPressureSensorsPassedQC(tdp.mRsrMsg.hwStatus.getBarometricPressureSensor1(),
                    tdp.mRsrMsg.hwStatus.getBarometricPressureSensor2())) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.AMBIENTPRESSURESENSORFAILEDQC);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            if (!tdp.checkReaderBatteryLevel()) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERBATTERYLOW);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            if (tdp.exceedMaxCardInReaders()) {
                stateDataObject.sendMessage(new LegacyReqInvalidCard());
                eventInfo.setTestStatusErrorType(TestStatusErrorType.EXCEEDMAXTEST);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.LegacyCardInReader;
            }

            return TestStateEventEnum.NewCardInReader;

        } else if (message.getDescriptor().getType() == LegacyMessageType.CardRemoved.value) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.CARDREMOVED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.Ready;
        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value) {
            LegacyRspError errMsg = (LegacyRspError) message;
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);

            if (errMsg.getErrorCode() == LegacyRspError.ErrorType.DetectionSwitchBounce) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDINSERTEDNOTPROPERLY);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.LegacyCardInReader;
            } else {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERERROR);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
        }

        return super.onEventPreHandle(stateDataObject);
    }

}
