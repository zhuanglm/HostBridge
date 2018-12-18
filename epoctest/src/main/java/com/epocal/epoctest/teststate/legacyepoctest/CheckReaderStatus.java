package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.common.realmentities.EqcInfo;
import com.epocal.datamanager.EqcInfoModel;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.DataPacketType;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.enumtype.legacy.TestStoppedReasons;
import com.epocal.reader.legacy.message.response.LegacyRspDataPacket;
import com.epocal.reader.legacy.message.response.LegacyRspError;
import com.epocal.reader.legacy.message.response.LegacyRspStatus;
import com.epocal.reader.legacy.message.response.LegacyRspTestStopped;
import com.epocal.reader.type.Legacy.ReaderQCInfo;
import com.epocal.reader.type.Legacy.SelfTestPacketInfo;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on Aug 2 2018.
 */
@SuppressWarnings("unchecked")
public class CheckReaderStatus extends LegacyTestState {
    public CheckReaderStatus() {
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

            // save the reader status response
            stateDataObject.getTestDataProcessor().mRsrMsg = resp;
            LegacyRspStatus.InterfaceVersion = stateDataObject.getTestDataProcessor()
                    .mDeviceIDResponse.getLegacyInfo().getHostReaderInterface();
            stateDataObject.getTestDataProcessor().mRsrMsg.AdjustErrorCode();

            // if there were no self test packets, we need to create this class and fill it up
            if (stateDataObject.getTestDataProcessor().mReaderQCInfo == null) {
                stateDataObject.getTestDataProcessor().mReaderQCInfo = new ReaderQCInfo();
            }

            // send the qc info to the datamanager
            EqcInfo eqcInfo = new EqcInfo();
            if (sendQCInfo(stateDataObject, eqcInfo)) {
                EqcInfoModel eim = new EqcInfoModel();
                eim.saveEqcInfo(eqcInfo);
            }

            stateDataObject.mRedistributed = true;
            return TestStateEventEnum.AfterTestResponseForUpdateReaderStatus;
        } else if (message.getDescriptor().getType() == LegacyMessageType.CardRemoved.value) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.CONFIGURATION);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.Handled;
        } else if (message.getDescriptor().getType() == LegacyMessageType.DataPacket.value) {
            LegacyRspDataPacket selfTestPacket = (LegacyRspDataPacket) message;

            // add this packets info to the current self test results
            if (selfTestPacket.getPacketType() == DataPacketType.SelfCheck) {
                if (stateDataObject.getTestDataProcessor().mReaderQCInfo == null) {
                    stateDataObject.getTestDataProcessor().mReaderQCInfo = new ReaderQCInfo();
                }

                SelfTestPacketInfo thisPacketsInfo = new SelfTestPacketInfo();
                thisPacketsInfo.setPacketNumber(String.valueOf(selfTestPacket.getBlockNumber()));

                for (int i = 0; i < selfTestPacket.getSamples().length; i++) {
                    thisPacketsInfo.values.add(String.valueOf(selfTestPacket.getSamples()[i]));
                }

                stateDataObject.getTestDataProcessor().mReaderQCInfo.selfTestValues.add(thisPacketsInfo);
            }
        } else if (message.getDescriptor().getType() == LegacyMessageType.TestStopped.value) {
            LegacyRspTestStopped testStopped = (LegacyRspTestStopped) message;

            if (testStopped.getReason() == TestStoppedReasons.SelfTestEnded) {
                return TestStateEventEnum.Handled;
            }
        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value) {
            LegacyRspError errMsg = (LegacyRspError) message;

            if (errMsg.getErrorCode() == LegacyRspError.ErrorType.CardInsertedWhileMotorMoving) {
                return TestStateEventEnum.Handled;
            }
        }
        return super.onEventPreHandle(stateDataObject);
    }

}
