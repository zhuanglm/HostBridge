package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.common.epocobjects.TimerData;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.configuration.LegacyReqConfig1_4;
import com.epocal.reader.legacy.message.response.LegacyRspAck;
import com.epocal.reader.legacy.message.response.LegacyRspError;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.reader.type.HardwareStatus;
import com.epocal.statemachine.IEventEnum;
import com.epocal.util.StringUtil;

/**
 * Created by rzhuang on Aug 24 2018.
 */

public class WaitingForConfiguration1_4Ack extends LegacyTestState {
    public WaitingForConfiguration1_4Ack() {
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
        TestEventInfo eventInfo = new TestEventInfo();
        if (message == null) {
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }

        if (stateDataObject.getTestStateAction() == TestStateActionEnum.CommunicationFailed) {
            TestEventInfo eventInfo2 = new TestEventInfo();
            eventInfo2.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo2.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo2);
            return TestStateEventEnum.EndTestAfterTestBegun;
        }

        if (message.getDescriptor().getType() == LegacyMessageType.Ack.value) {
            stateDataObject.StopTimer();

            LegacyRspAck resp = (LegacyRspAck) message;
            if (resp == null) {
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            if (resp.getAckType() == LegacyRspAck.AckType.Config1_4)
            {
                stateDataObject.getTestDataProcessor().mCanRunThermalCheck = true;
                HardwareStatus hst = stateDataObject.getTestDataProcessor().mRsrMsg.hwStatus;
                //timeEstimate for calibaration moved from WaitingForReaderStatusHeaterTemperatures
                int timeEstimate;
                if (hst.getAmbientTemp1() < 20)
                {
                    timeEstimate = (int)(207 - (1.5 * hst.getBGEBottomHeaterTemp()));
                }
                else if (hst.getAmbientTemp1() > 28)
                {
                    timeEstimate = (int)(217 - (1.3 * hst.getBGEBottomHeaterTemp()));
                }
                else
                {
                    // between 20 and 28
                    timeEstimate = (int)(207 - (1.3 * hst.getBGEBottomHeaterTemp()));
                }

                if (timeEstimate < 0 || timeEstimate > 600)
                    timeEstimate = 190;

                stateDataObject.getTestDataProcessor().mTimeEstimate = timeEstimate;
                stateDataObject.getTestDataProcessor().setCalibrationFlag(true);
                eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                eventInfo.setTestStatusType(TestStatusType.FLUIDICSCALIBRATION);
                eventInfo.setTestEventData(new TimerData(StringUtil.timeInSecondToTimeString(timeEstimate),100));
                stateDataObject.postEvent(eventInfo);

                return TestStateEventEnum.FluidicsCalibration;
            }

            return TestStateEventEnum.Handled;
        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value)
        {
            LegacyRspError errMsg = (LegacyRspError)message;
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            if (errMsg.getMessageCode() == LegacyRspError.ErrorType.DetectionSwitchBounce.value)
            {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDINSERTEDNOTPROPERLY);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.LegacyCardInReader;
            }
            else
            {
                // for now
                stateDataObject.sendMessage(new LegacyReqConfig1_4());
                stateDataObject.getTestDataProcessor().setErrorCode(HostErrorCode.ErrorOccurredDuringFluidics);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.ERROROCCURREDTESTSTOP);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestAfterTestBegun;
            }

        }else if (message.getDescriptor().getType() == LegacyMessageType.CardRemoved.value)
        {
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.CARDREMOVED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.Ready;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {
        // when we get the card inserted, send config1_4
        return stateDataObject.sendMessage(stateDataObject.getTestDataProcessor().mConfigMsg1_4);
    }

}
