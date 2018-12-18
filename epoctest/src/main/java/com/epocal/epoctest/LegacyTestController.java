package com.epocal.epoctest;

import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.common.types.am.TestMode;
import com.epocal.epoctest.testprocess.TestCommunication;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEnum;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.reader.IMsgPayload;
import com.epocal.statemachine.IState;
import com.epocal.statemachine.IStateEnum;
import com.epocal.statemachine.StateMachine;
import com.epocal.util.UUIDUtil;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.reactivex.functions.Consumer;

/**
 * Created by rzhuang on July/17/2018.
 */

public class LegacyTestController extends TestController {

    private StateMachine<TestStateDataObject> mStateMachine;
    private TestCommunication mTestCommunication;

    private UUID mControllerUUID;

    public UUID getControllerUUID() {
        return mControllerUUID;
    }

    LegacyTestController() {
        super();
    }

    public void initializeController(ReaderDevice readerDevice, TestMode testMode) {
        mControllerUUID = UUIDUtil.generateUUID();

        //setup StateMachine
        mStateMachine = new StateMachine<>();
        mStateMachine.setStateList(GetStateList());
        mStateMachineFactory.StateMachineInstance
                (getStateMachineXML(), mStateMachine, EnumSet.allOf(TestStateEnum.class), EnumSet.allOf(TestStateEventEnum.class));

        //setup Communication
        mTestCommunication = new TestCommunication(readerDevice.getDeviceAddress());
        mTestCommunication.setReaderSerialNumber(readerDevice.getDeviceId());
        mTestCommunication.getRxUtilStatus().subscribe(new Consumer<TestEventInfo>() {
            @Override
            public void accept(TestEventInfo testEventInfo) {
                fireEventCenter(testEventInfo);
            }
        });
        mTestCommunication.getRxUtilMessage().subscribe(new Consumer<IMsgPayload>() {
            @Override
            public void accept(IMsgPayload iMsgPayload) {
                mTestStateDataObject.setTestStateAction(TestStateActionEnum.None);
                mTestStateDataObject.setIMsgPayload(iMsgPayload);
                postEventToStateMachiane(mTestStateDataObject);
            }
        });

        //setup State object
        mTestStateDataObject.initTestData(readerDevice, testMode); // need the testRecord object right away, even if test not started yet
        mTestStateDataObject.setAddress(readerDevice.getDeviceAddress());
        mTestStateDataObject.setStateMachine(mStateMachine);
        mTestStateDataObject.setTestCommunication(mTestCommunication);
        mTestStateDataObject.getRxUtil().subscribe(new Consumer<TestEventInfo>() {
            @Override
            public void accept(TestEventInfo testEventInfo) {
                fireEventCenter(testEventInfo);
            }
        });
    }

    /***
     * post event into state machine which events are coming from connection layer or communication layer.
     * to notify state machine the connection status or to send message to states.
     */
    protected void postEventToStateMachiane(TestStateDataObject testStateDataObject) {
        if (mStateMachine != null) {
            mStateMachine.postEvent(testStateDataObject);
        }
    }

    public void start() {
        if (mStateMachine == null) return;
        mStateMachine.start(mTestStateDataObject);

        mTestStateDataObject.setTestStateAction(TestStateActionEnum.Connecting);
        postEventToStateMachiane(mTestStateDataObject);
    }

    public void stop() {
        if (mStateMachine == null) return;

        mTestStateDataObject.setTestStateAction(TestStateActionEnum.CancelConnection);
        postEventToStateMachiane(mTestStateDataObject);
        mTestCommunication.disconnect();
        mStateMachine.stop();
        mTestStateDataObject.unSubscribeRx();
    }

    private Map<IStateEnum, IState> GetStateList() {
        Map<IStateEnum, IState> stateList = new HashMap<>();
        putInTheList(stateList, TestStateEnum.Disconnected, mDisconnected.get());
        putInTheList(stateList, TestStateEnum.Connecting, mConnecting.get());
        putInTheList(stateList, TestStateEnum.Connected, mConnected.get());

        putInTheList(stateList, TestStateEnum.ActionAfterInitialConnect, mActionAfterInitialConnect.get());
        putInTheList(stateList, TestStateEnum.GetReaderId, mGetReaderId.get());
        putInTheList(stateList, TestStateEnum.WaitingForStatisticsResponse, mWaitingForStatisticsResponse.get());
        putInTheList(stateList, TestStateEnum.CheckFirstReaderStatus, mCheckFirstReaderStatus.get());
        putInTheList(stateList, TestStateEnum.WaitingForHCMResponse, mWaitingForHCMResponse.get());
        putInTheList(stateList, TestStateEnum.WaitingForConfiguration1_2Ack, mWaitingForConfiguration1_2Ack.get());
        putInTheList(stateList, TestStateEnum.WaitingForDeviceEnableAck, mWaitingForDeviceEnableAck.get());
        putInTheList(stateList, TestStateEnum.CheckReaderStatus, mCheckReaderStatus.get());
        putInTheList(stateList, TestStateEnum.AfterTestResponseForUpdateReaderStatus, mAfterTestResponseForUpdateReaderStatus.get());
        putInTheList(stateList, TestStateEnum.OldCardInReader, mOldCardInReader.get());
        putInTheList(stateList, TestStateEnum.WaitingForConfiguration1_1Ack, mWaitingForConfiguration1_1Ack.get());
        putInTheList(stateList, TestStateEnum.WaitingForConfiguration1_3Ack, mWaitingForConfiguration1_3Ack.get());
        putInTheList(stateList, TestStateEnum.Ready, mReady.get());
        putInTheList(stateList, TestStateEnum.LegacyCardInReader, mLegacyCardInReader.get());
        putInTheList(stateList, TestStateEnum.WaitingForTestGetReaderStatus, mWaitingForTestGetReaderStatus.get());
        putInTheList(stateList, TestStateEnum.NewCardInReader, mNewCardInReader.get());
        putInTheList(stateList, TestStateEnum.WaitingForReaderStatusHeaterTemperatures, mWaitingForReaderStatusHeaterTemperatures.get());
        putInTheList(stateList, TestStateEnum.WaitingForConfiguration1_4Ack, mWaitingForConfiguration1_4Ack.get());
        putInTheList(stateList, TestStateEnum.FluidicsCalibration, mFluidicsCalibration.get());
        putInTheList(stateList, TestStateEnum.SampleIntroduction, mSampleIntroduction.get());
        putInTheList(stateList, TestStateEnum.SampleProcessing, mSampleProcessing.get());
        putInTheList(stateList, TestStateEnum.TestCompleted, mTestCompleted.get());
        putInTheList(stateList, TestStateEnum.TestCompletedCardRemoved, mTestCompletedCardRemoved.get());
        putInTheList(stateList, TestStateEnum.EndTestAfterTestBegun, mEndTestAfterTestBegun.get());
        putInTheList(stateList, TestStateEnum.EndTestBeforeTestBegun, mEndTestBeforeTestBegun.get());
        putInTheList(stateList, TestStateEnum.TerminateTest, mTerminateTest.get());

        return stateList;
    }

    private String getStateMachineXML() {
        return new StringBuilder()
                .append("<state id='Root'>")
                .append("<initial>")
                .append("<transition target='Connection'/>")
                .append("</initial>")

                .append("<state id='Connection'>")
                .append("<initial>")
                .append("<transition target='Disconnected'/>")
                .append("</initial>")
                .append("<state id='Disconnected'>")
                .append("<transition event='connecting' target='connecting' type='normal'/>")
                .append("</state>")
                .append("<state id='connecting'>")
                .append("<transition event='Connected' target='Connected' type='normal'/>")
                .append("<transition event='Disconnected' target='Disconnected' type='normal'/>")
                .append("</state>")
                .append("<final id='Connected'>")
                .append("<transition event='Disconnected' target='Disconnected' type='normal'/>")
                .append("</final>")
                .append("<transition event='TestStateBody' target='TestStateBody' type='normal'/>")
                .append("</state>")//end of Connection

                .append("<state id='TestStateBody'>")
                .append("<initial>")
                .append("<transition target='ActionAfterInitialConnect'/>")
                .append("</initial>")

                .append("<state id='ActionAfterInitialConnect'>")
                .append("<transition event='GetReaderId' target='GetReaderId' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='GetReaderId'>")
                .append("<transition event='WaitingForStatisticsResponse' target='WaitingForStatisticsResponse' type='normal'/>")
                .append("<transition event='CheckFirstReaderStatus' target='CheckFirstReaderStatus' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='WaitingForStatisticsResponse'>")
                .append("<transition event='WaitingForDeviceEnableAck' target='WaitingForDeviceEnableAck' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='WaitingForDeviceEnableAck'>")
                .append("<transition event='CheckFirstReaderStatus' target='CheckFirstReaderStatus' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='CheckFirstReaderStatus'>")
                .append("<transition event='WaitingForHCMResponse' target='WaitingForHCMResponse' type='normal'/>")
                .append("<transition event='WaitingForConfiguration1_2Ack' target='WaitingForConfiguration1_2Ack' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='WaitingForHCMResponse'>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='WaitingForConfiguration1_2Ack'>")
                .append("<transition event='CheckReaderStatus' target='CheckReaderStatus' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='CheckReaderStatus'>")
                .append("<transition event='OldCardInReader' target='OldCardInReader' type='normal'/>")
                .append("<transition event='WaitingForHCMResponse' target='WaitingForHCMResponse' type='normal'/>")
                .append("<transition event='WaitingForConfiguration1_1Ack' target='WaitingForConfiguration1_1Ack' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("<transition event='TestResponseForUpdateReaderStatus' target='TestResponseForUpdateReaderStatus' type='normal'/>")
                .append("<transition event='AfterTestResponseForUpdateReaderStatus' target='AfterTestResponseForUpdateReaderStatus' type='normal'/>")
                .append("</state>")

                .append("<state id='TestResponseForUpdateReaderStatus'>")
                .append("<transition event='AfterTestResponseForUpdateReaderStatus' target='AfterTestResponseForUpdateReaderStatus' type='normal'/>")
                .append("</state>")

                .append("<state id='AfterTestResponseForUpdateReaderStatus'>")
                .append("<transition event='OldCardInReader' target='OldCardInReader' type='normal'/>")
                .append("<transition event='WaitingForHCMResponse' target='WaitingForHCMResponse' type='normal'/>")
                .append("<transition event='WaitingForConfiguration1_1Ack' target='WaitingForConfiguration1_1Ack' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='OldCardInReader'>")
                .append("<transition event='CheckReaderStatus' target='CheckReaderStatus' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='WaitingForConfiguration1_1Ack'>")
                .append("<transition event='WaitingForConfiguration1_3Ack' target='WaitingForConfiguration1_3Ack' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='WaitingForConfiguration1_3Ack'>")
                .append("<transition event='Ready' target='Ready' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='Ready'>")
                .append("<transition event='LegacyCardInReader' target='LegacyCardInReader' type='normal'/>")
                .append("<transition event='WaitingForTestGetReaderStatus' target='WaitingForTestGetReaderStatus' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("<transition event='AfterReadyForQCLockout' target='AfterReadyForQCLockout' type='normal'/>")
                .append("</state>")

                .append("<state id='AfterReadyForQCLockout'>")
                .append("<transition event='Ready' target='Ready' type='normal'/>")
                .append("<transition event='LegacyCardInReader' target='LegacyCardInReader' type='normal'/>")
                .append("<transition event='WaitingForTestGetReaderStatus' target='WaitingForTestGetReaderStatus' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='LegacyCardInReader'>")
                .append("<transition event='Ready' target='Ready' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='WaitingForTestGetReaderStatus'>")
                .append("<transition event='LegacyCardInReader' target='LegacyCardInReader' type='normal'/>")
                .append("<transition event='NewCardInReader' target='NewCardInReader' type='normal'/>")
                .append("<transition event='Ready' target='Ready' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='NewCardInReader'>")
                .append("<transition event='WaitingForReaderStatusHeaterTemperatures' target='WaitingForReaderStatusHeaterTemperatures' type='normal'/>")
                .append("<transition event='Ready' target='Ready' type='normal'/>")
                .append("<transition event='LegacyCardInReader' target='LegacyCardInReader' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='WaitingForReaderStatusHeaterTemperatures'>")
                .append("<transition event='LegacyCardInReader' target='LegacyCardInReader' type='normal'/>")
                .append("<transition event='WaitingForConfiguration1_4Ack' target='WaitingForConfiguration1_4Ack' type='normal'/>")
                .append("<transition event='Ready' target='Ready' type='normal'/>")
                .append("<transition event='EndTestBeforeTestBegun' target='EndTestBeforeTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='WaitingForConfiguration1_4Ack'>")
                .append("<transition event='FluidicsCalibration' target='FluidicsCalibration' type='normal'/>")
                .append("<transition event='LegacyCardInReader' target='LegacyCardInReader' type='normal'/>")
                .append("<transition event='Ready' target='Ready' type='normal'/>")
                .append("<transition event='EndTestAfterTestBegun' target='EndTestAfterTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='FluidicsCalibration'>")
                .append("<transition event='SampleIntroduction' target='SampleIntroduction' type='normal'/>")
                .append("<transition event='EndTestAfterTestBegun' target='EndTestAfterTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='SampleIntroduction'>")
                .append("<transition event='SampleProcessing' target='SampleProcessing' type='normal'/>")
                .append("<transition event='EndTestAfterTestBegun' target='EndTestAfterTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='SampleProcessing'>")
                .append("<transition event='TestCompleted' target='TestCompleted' type='normal'/>")
                .append("<transition event='LegacyCardInReader' target='LegacyCardInReader' type='normal'/>")
                .append("<transition event='EndTestAfterTestBegun' target='EndTestAfterTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='TestCompleted'>")
                .append("<transition event='LegacyCardInReader' target='LegacyCardInReader' type='normal'/>")
                .append("<transition event='TestCompletedCardRemoved' target='TestCompletedCardRemoved' type='normal'/>")
                .append("<transition event='EndTestAfterTestBegun' target='EndTestAfterTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='TestCompletedCardRemoved'>")
                .append("<transition event='Ready' target='Ready' type='normal'/>")
                .append("<transition event='EndTestAfterTestBegun' target='EndTestAfterTestBegun' type='normal'/>")
                .append("</state>")

                .append("<state id='EndTestBeforeTestBegun'>")
                .append("<transition event='TerminateTest' target='TerminateTest' type='normal'/>")
                .append("</state>")

                .append("<state id='EndTestAfterTestBegun'>")
                .append("<transition event='LegacyCardInReader' target='LegacyCardInReader' type='normal'/>")
                .append("</state>")

                .append("<state id='TerminateTest'>")
                .append("</state>")

                .append("</state>") //end of TestStateBody

                .append("<final id='Done'/>")
                .append("</state>").toString(); //end of Root
    }

}
