package com.epocal.epoctest.teststate.epoctest.patienttest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.TestActionResponse;
import com.epocal.reader.nextgen.message.request.test.ack.TstAckReqAck;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspBGETest;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspDevicePeripheralMonitor;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 7/25/2017.
 */

public class PerformTest extends TestState {

    public enum MeasurementState {NotRun, Pass, Fail}

    ;

    public enum CommandStatus {started, finished}

    public enum TestRunningStatus {
        NoTestRunning(0x00000000),
        BGETest(0x00000001),
        PeriphMonitor(0x00000002),
        CooxSampleMeasurement(0x00000004),          // UNUSED BY BRIDGE (NextGen only)
        CooxLedStability(0x00000008),               // UNUSED BY BRIDGE (NextGen only)
        CooxReferenceMeasurement(0x00000010),       // UNUSED BY BRIDGE (NextGen only)
        DarkMeasurement(0x00000020);

        public final int value;

        TestRunningStatus(int value) {
            this.value = Integer.valueOf(value);
        }

        public static TestRunningStatus convert(int value) {
            return TestRunningStatus.values()[value];
        }
    }

    private MeasurementState mBGEState;
    private CommandStatus mCommandStatus = CommandStatus.finished;
    private int mTestRunningStatus = 0;

    public MeasurementState getBGEState() {
        return mBGEState;
    }

    public void setBGEState(MeasurementState mBGEState) {
        this.mBGEState = mBGEState;
    }

    public CommandStatus getCommandStatus() {
        return mCommandStatus;
    }

    public void setCommandStatus(CommandStatus mCommandStatus) {
        this.mCommandStatus = mCommandStatus;
    }

    public int getTestRunningStatus() {
        return mTestRunningStatus;
    }

    public void setTestRunningStatus(int mTestRunningStatus) {
        this.mTestRunningStatus = mTestRunningStatus;
    }

    public PerformTest() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        mBGEState = MeasurementState.NotRun;
        super.onEntry(stateDataObject);
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        return super.onEventPreHandle(stateDataObject);
    }

    public TestStateEventEnum detectSampling(TestStateDataObject stateDataObject, TstActRspBGETest bge) {
        boolean bubbleBeginSetThisPacket = stateDataObject.getTestDataProcessor().doBubbleDetectDuringSampleIntroduction(stateDataObject, bge);

        if (stateDataObject.getTestDataProcessor().getTestConfiguration().RealTimeQCSetting.Enabled)
        {
            // do we need to do realtime qc ? do it
            // ONLY do it if the bubble hasnt started yet
            if (stateDataObject.getTestDataProcessor().getBubbleDetect() == 0 && stateDataObject.getTestDataProcessor().getBubbleBegin() == 0) {
                if (!stateDataObject.getTestDataProcessor().doRealTimeQC(stateDataObject)) {
                    return TestStateEventEnum.CancelTest;
                }
            }
        }
        // once air has been found.. we need to look for fluid.. even
        // if its in the same byte.. only go into
        if (stateDataObject.getTestDataProcessor().doSamplingDetectDuringSampleIntroduction(stateDataObject, bge, bubbleBeginSetThisPacket)) {
            return TestStateEventEnum.PerformSampleProcessing;
        }
        return TestStateEventEnum.Handled;
    }

    private boolean sendTestAckMessageToReader(TestStateDataObject stateDataObject) {
        TstAckReqAck req = new TstAckReqAck();
        return stateDataObject.sendMessage(req);
    }

    private TestStateEventEnum evaluateTestStates(TestStateDataObject stateDataObject) {
        switch (getBGEState()) {
            case Fail: {
                //TODO: in theory BGE can fail and Coox pass.
                stateDataObject.getTestDataProcessor().setErrorCode(HostErrorCode.ErrorOccurredDuringFluidics);
                ////stateDataObject.postEvent(stateDataObject, TestInternalState.TestError, ErrorCodeToErrorEventType(stateDataObject, stateDataObject.TestDataProcessor.errorCode, epoc.common.types.TestStateErrrorType.ErrorOccurredTestStop));
                break;
            }
            case Pass: {
                return TestStateEventEnum.Handled;
            }
            default:
                return TestStateEventEnum.Handled;
        }
        return TestStateEventEnum.Handled;
    }

    public IEventEnum handleBGEDataPacket(TestStateDataObject stateDataObject, IMsgPayload message)
    {
        if (message.getDescriptor().getMsgClass() == MessageClass.Test &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Action &&
                message.getDescriptor().getType() == TestActionResponse.BGETest.value) {
            stateDataObject.StopTimer();
            TstActRspBGETest resp = (TstActRspBGETest) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            return handleBGEMeasurements(stateDataObject, resp);
        } else if (message.getDescriptor().getMsgClass() == MessageClass.Test &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Action &&
                message.getDescriptor().getType() == TestActionResponse.DevicePeripheralMonitor.value) {
            stateDataObject.StopTimer();
            TstActRspDevicePeripheralMonitor resp = (TstActRspDevicePeripheralMonitor) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            return handlePeriphMonitorMeasurements(stateDataObject, resp);
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private IEventEnum handleBGEMeasurements(TestStateDataObject stateDataObject, TstActRspBGETest bge) {
        switch ((TstActRspBGETest.MessageCodeType.convert(bge.getMessageCode()))) {
            case Error:
                setBGEState(MeasurementState.Fail);
                setTestRunningStatus((getTestRunningStatus() & (~TestRunningStatus.BGETest.value)));
                return evaluateTestStates(stateDataObject);
            case Ack:
                return TestStateEventEnum.Handled;
            case ActionStarted:
                ////RaiseEvent(stateDataObject, epoc.common.types.TestStateType.FluidicsCalibration, 180);
                setTestRunningStatus((getTestRunningStatus() | (TestRunningStatus.BGETest.value)));
                return TestStateEventEnum.Handled;
            case ActionCompleted:
                if (bge.getTestId()!= 65535)
                {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.READERERROR);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.TerminateTest;
                }
                setBGEState(MeasurementState.Pass);
                setTestRunningStatus((getTestRunningStatus() & (~TestRunningStatus.BGETest.value)));
                return evaluateTestStates(stateDataObject);
            case Data: {
                if (!sendTestAckMessageToReader(stateDataObject)) {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.CancelTest;
                }
                stateDataObject.getTestDataProcessor().parseDataPacket(stateDataObject, this, bge.getData());
                return TestStateEventEnum.Handled;
            }
        }
        // wait for next packet
        //stateDataObject.EnableResponseTimer();
        return TestStateEventEnum.Handled;
    }

    private IEventEnum handlePeriphMonitorMeasurements(TestStateDataObject stateDataObject, TstActRspDevicePeripheralMonitor bge) {
        switch ((TstActRspDevicePeripheralMonitor.MessageCodeType.convert(bge.getMessageCode()))) {
            case Error:
                setBGEState(MeasurementState.Fail);
                setTestRunningStatus((getTestRunningStatus() & (~TestRunningStatus.PeriphMonitor.value)));
                return evaluateTestStates(stateDataObject);
            case Ack:
                return TestStateEventEnum.Handled;
            case ActionStarted:
                ////RaiseEvent(stateDataObject, epoc.common.types.TestStateType.FluidicsCalibration, 180);
                setTestRunningStatus((getTestRunningStatus() | (TestRunningStatus.PeriphMonitor.value)));
                return TestStateEventEnum.Handled;
            case ActionCompleted:
                setBGEState(MeasurementState.Pass);
                setTestRunningStatus((getTestRunningStatus() & (~TestRunningStatus.PeriphMonitor.value)));
                return evaluateTestStates(stateDataObject);
            case Data: {
                if (!sendTestAckMessageToReader(stateDataObject)) {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.CancelTest;
                }
                stateDataObject.getTestDataProcessor().parseDataPacket(stateDataObject, this, bge.getData());
                return TestStateEventEnum.Handled;
            }
        }
        return TestStateEventEnum.Handled;
    }
}
