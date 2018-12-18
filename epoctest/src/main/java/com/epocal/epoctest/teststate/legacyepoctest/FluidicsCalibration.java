package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.common.epocobjects.TimerData;
import com.epocal.common.types.am.RealTimeHematocritQCReturnCode;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestDataProcessor;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.reader.enumtype.legacy.DAMStages;
import com.epocal.reader.enumtype.legacy.DataPacketType;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.enumtype.legacy.ReaderMode;
import com.epocal.reader.legacy.message.request.command.LegacyReqStopTest;
import com.epocal.reader.legacy.message.response.LegacyRspDataPacket;
import com.epocal.reader.legacy.message.response.LegacyRspError;
import com.epocal.reader.legacy.message.response.LegacyRspStatusUpdate;
import com.epocal.reader.legacy.message.response.LegacyRspTestStopped;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.statemachine.IEventEnum;
import com.epocal.util.DecimalConversionUtil;
import com.epocal.util.StringUtil;

/**
 * Created by rzhuang on Aug 27 2018.
 */

public class FluidicsCalibration extends LegacyTestState {
    //private int nCountSeconds = 185;

    public FluidicsCalibration() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        stateDataObject.startTimer();
    }

    @Override
    public void onExit(TestStateDataObject stateDataObject) {
        stopTestTypeCountDown();
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        message = stateDataObject.getIMsgPayload();
        TestEventInfo eventInfo = new TestEventInfo();
        if (message == null) {
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestAfterTestBegun;
        }

        // we've sent out the config information and we're waiting for a response
        if (message.getDescriptor().getType() == LegacyMessageType.DataPacket.value) {
            // cancel the response timer for this packet, start it afterwards for the next packet
            stateDataObject.StopTimer();
            LegacyRspDataPacket tempPacket = (LegacyRspDataPacket) message;
            TestDataProcessor tdp = stateDataObject.getTestDataProcessor();

            if (tempPacket.getDAMMode() == DAMStages.FLUIDICS_CALIBRATION &&
                    stateDataObject.getTestDataProcessor().isCalibrationFlag()) {
                startTestTypeCountDown(TestStatusType.FLUIDICSCALIBRATION,
                        Math.round(tdp.mTimeEstimate), 0, 1, stateDataObject);
                stateDataObject.getTestDataProcessor().setCalibrationFlag(false);
            }

            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.FLUIDICSCALIBRATION);

            if (tempPacket.getReaderMode() == ReaderMode.eDeviceBubbleDetectedState) {
                // deactivate then activate the stage timer.. should reset the second counter to a full second
                //RaiseEvent(stateDataObject, epoc.common.types.TestStateMachineEventReason.EndFluidicsCalibration, null);
                stopTestTypeCountDown();
                eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                eventInfo.setTestStatusType(TestStatusType.SAMPLEINTRODUCTION);
                eventInfo.setTestEventData(new TimerData(StringUtil.timeInSecondToTimeString(
                        (int) tdp.mConfigMsg1_4.stagesTimers.getSampleIntroductionTimer()), 100));
                stateDataObject.postEvent(eventInfo);

                tdp.endFluidCalibration();

                // dont parse this message, go straight into sample introduction
                // state and let that method parse the packet
                stateDataObject.mRedistributed = true;
                return TestStateEventEnum.SampleIntroduction;
            }

            if (tempPacket.getPacketType() == DataPacketType.TestData) {
                // this is a fluidics packet.. set the new time and parse the packet
                tdp.setLastRecordedTime(DecimalConversionUtil.round(
                        tdp.getLastRecordedTime() + tdp.getTimeIncrement(), 1));
            }
            tdp.parseDataPacket(tempPacket, stateDataObject, this);

            if (tdp.getTestConfiguration().RealTimeQCSetting.Enabled) {
                // do we have our first fluid yet? save the value
                if (tdp.getmFirstFluid() == 0) {
                    if (tempPacket.getConductivityByte() != 0) {
                        tdp.setFirstFluid(tdp.getLastRecordedTime());
                    }
                }

                // we save the previous hematocrit return code so that we
                // can pass it in next time and the value gets taken into account
                // when determining if something went wrong.. save us cycles
                tdp.setEarlyInjection();

                // record the conductivity failure in the extra strings
                if (tdp.getHctRC() == RealTimeHematocritQCReturnCode.LowResistance) {
                    tdp.setTestReadingsHematocritExtraString("C");
                    LegacyReqStopTest legacyReqStopTest = new LegacyReqStopTest();
                    legacyReqStopTest.setTestID((short) stateDataObject.getTestID());
                    stateDataObject.sendMessage(legacyReqStopTest);
                    tdp.setErrorCode(HostErrorCode.HematocritLowResistance);

                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.HEMATOCRITLOWRESISTANCE);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.EndTestAfterTestBegun;
                } else if (tdp.getHctRC() == RealTimeHematocritQCReturnCode.EarlyInjection) {
                    tdp.setTestReadingsHematocritExtraString("I");
                    LegacyReqStopTest legacyReqStopTest = new LegacyReqStopTest();
                    legacyReqStopTest.setTestID((short) stateDataObject.getTestID());
                    stateDataObject.sendMessage(legacyReqStopTest);
                    tdp.setErrorCode(HostErrorCode.EarlyInjection);
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.EARLYINJECTION);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.EndTestAfterTestBegun;
                } else if (tdp.getHctRC() == RealTimeHematocritQCReturnCode.FailedQC) {
                    tdp.setTestReadingsHematocritExtraString("F");
                    LegacyReqStopTest legacyReqStopTest = new LegacyReqStopTest();
                    legacyReqStopTest.setTestID((short) stateDataObject.getTestID());
                    stateDataObject.sendMessage(legacyReqStopTest);
                    tdp.setErrorCode(HostErrorCode.FluidicsFailedQCDuringCalibration);

                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.REALTIMEQCFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.EndTestAfterTestBegun;
                }

                if (!tdp.doRealTimeQC(stateDataObject)) {
                    LegacyReqStopTest legacyReqStopTest = new LegacyReqStopTest();
                    legacyReqStopTest.setTestID((short) stateDataObject.getTestID());
                    stateDataObject.sendMessage(legacyReqStopTest);
                    return TestStateEventEnum.EndTestAfterTestBegun;
                }

            }
            stateDataObject.startTimer();
            return TestStateEventEnum.Handled;
        } else if (message.getDescriptor().getType() == LegacyMessageType.StatusUpdate.value) {
            LegacyRspStatusUpdate statusUpdate = (LegacyRspStatusUpdate) message;

            // make sure it's a legit message and all
            if (statusUpdate.getDamMode() == DAMStages.FLUIDICS_CALIBRATION) {
                // set the remaining seconds and the remaining packets on the progress bar to whatever
                // number of seconds is left
                //RaiseEvent(stateDataObject, epoc.common.types.TestStateMachineEventReason.UpdateFluidicsCalibration, (int)statusUpdate.RemainingTime);
                eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                eventInfo.setTestStatusType(TestStatusType.FLUIDICSCALIBRATION);
                eventInfo.setTestEventData(new TimerData(
                        StringUtil.timeInSecondToTimeString((int) statusUpdate.getRemainingTime()), 100));
                stateDataObject.postEvent(eventInfo);
                startTestTypeCountDown(TestStatusType.FLUIDICSCALIBRATION,
                        Math.round((int) statusUpdate.getRemainingTime()), 0, 1, stateDataObject);
                stateDataObject.getTestDataProcessor().mHasTimeStatusMessageBeenReceived = true;
            }

            return TestStateEventEnum.Handled;

        } else if (message.getDescriptor().getType() == LegacyMessageType.TestStopped.value) {
            stopTestTypeCountDown();
            stateDataObject.getTestDataProcessor().setErrorCode(
                    ConvertTestStoppedToErrorCode(((LegacyRspTestStopped) message).getReason(), stateDataObject));

            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(ErrorCodeToErrorEventType(stateDataObject.getTestDataProcessor().getErrorCode(),
                    TestStatusErrorType.ERROROCCURREDTESTSTOP));
            stateDataObject.postEvent(eventInfo);

            return TestStateEventEnum.EndTestAfterTestBegun;

        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value) {
            LegacyRspError error = (LegacyRspError) message;
            if (error.getErrorCode() != LegacyRspError.ErrorType.Undefined) {
                stateDataObject.getTestDataProcessor().setErrorCode(HostErrorCode.ErrorOccurredDuringFluidics);
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(ErrorCodeToErrorEventType(stateDataObject.getTestDataProcessor().getErrorCode(),
                        TestStatusErrorType.ERROROCCURREDTESTSTOP));
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestAfterTestBegun;
            }
        }

        if (stateDataObject.getTestStateAction() == TestStateActionEnum.CancelConnection) {
            stateDataObject.getTestDataProcessor().setErrorCode(HostErrorCode.UserStoppedTestDuringFluidics);
            stateDataObject.closePreviousTest();
        }
        return super.onEventPreHandle(stateDataObject);

    }

}
