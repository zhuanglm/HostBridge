package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.common.EpocTime;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.command.LegacyReqEnableTest;
import com.epocal.reader.legacy.message.response.LegacyRspAck;
import com.epocal.reader.legacy.message.response.LegacyRspError;
import com.epocal.statemachine.IEventEnum;

import java.util.Calendar;

/**
 * Created by rzhuang on Aug 22 2018.
 */

public class NewCardInReader extends LegacyTestState {
    public NewCardInReader() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);

        if (sendMessageToReader(stateDataObject)) {
            Calendar expireDay = stateDataObject.getTestDataProcessor().getBarcodeInformation().mCardExpiry;
            expireDay.add(Calendar.DAY_OF_YEAR, 1);
            if (expireDay.getTime().compareTo(EpocTime.now()) < 0) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDEXPIRED);
                stateDataObject.postEvent(eventInfo);
            }
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

        // only thing we're interested in is card removed
        if (message.getDescriptor().getType() == LegacyMessageType.CardRemoved.value) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.CARDREMOVED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.Ready;

        } else if (message.getDescriptor().getType() == LegacyMessageType.Ack.value ||
                message.getDescriptor().getType() == LegacyMessageType.HandleStatus.value) {
            LegacyRspAck resp = (LegacyRspAck) message;
            if (resp.getAckType() == LegacyRspAck.AckType.ResultsDestroyed)
                return TestStateEventEnum.Handled;

            LegacyRspAck ackMsg = null;

            if (message.getDescriptor().getType() == LegacyMessageType.Ack.value) {
                ackMsg = (LegacyRspAck) message;
            }

            stateDataObject.getTestDataProcessor().setNumTestsRunPlus();

            if (ackMsg != null && message.getDescriptor().getType() == LegacyMessageType.Ack.value &&
                    ackMsg.getAckType() == LegacyRspAck.AckType.EnableTestAck) {
                // we want the temperatures for the time estimate
                askForReaderStatus(stateDataObject, false, true);
                stateDataObject.mRedistributed = false;
                return TestStateEventEnum.WaitingForReaderStatusHeaterTemperatures;
            } else {
                stateDataObject.mRedistributed = true;
                return TestStateEventEnum.WaitingForReaderStatusHeaterTemperatures;
            }

        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value) {
            // this message may occur at exactly the same time as the reader status, so we
            // will get this AFTER the reader status instead of before.. so we need to discard
            // it here otherwise it looks like some sort of hardware failure
            LegacyRspError resp = (LegacyRspError) message;
            if (resp.getMessageCode() == LegacyRspError.ErrorType.CardInsertedWhileMotorMoving.value) {
                return TestStateEventEnum.Handled;
            }
            if (resp.getMessageCode() == LegacyRspError.ErrorType.DetectionSwitchBounce.value) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDINSERTEDNOTPROPERLY);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.LegacyCardInReader;
            }
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {

        // and send out the start message
        TestRecord tr = stateDataObject.getTestDataProcessor().getTestRecord();
        tr.setTestDateTime(EpocTime.now());

        LegacyReqEnableTest stMsg = new LegacyReqEnableTest();
        String gn = tr.getGuestName();
        stMsg.setOperatorId((tr.getUser() == null ?
                ((gn == null || gn.isEmpty()) ? "" : tr.getGuestName()) :
                (tr.getUser().getUserId() == null || tr.getUser().getUserId().isEmpty())
                        ? "" : tr.getUser().getUserId()));
        stMsg.setPatientId(tr.getSubjectId());
        stMsg.setTestId((short) stateDataObject.getTestDataProcessor().getTestRecord().getId());
        stMsg.setHostId(String.valueOf(tr.getHost().getId()));
        stMsg.setTestTime(tr.getTestDateTime());

        return stateDataObject.sendMessage(stMsg);
    }

}

