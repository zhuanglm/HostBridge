package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.common.epocobjects.AnalyteOption;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.enumtype.legacy.QCLockoutHandlerReturnCode;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.statemachine.IEventEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rzhuang on Aug 7 2018.
 */

public class AfterTestResponseForUpdateReaderStatus extends LegacyTestState {
    public AfterTestResponseForUpdateReaderStatus() {
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
        stateDataObject.mRedistributed = false;

        // display battery level
        //RaiseEvent(stateDataObject, epoc.common.types.TestStateType.ReaderBattery, (int)stateDataObject.TestDataProcessor.rsrMsg.HwStatus.batteryLevel);
        //rsrMsg.hwStatus.batteryLevel = 0;

        // check if the battery level is enough to run a test
        if (!stateDataObject.getTestDataProcessor().checkReaderBatteryLevel()) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.READERBATTERYLOW);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }

        boolean selfCheckPassed = stateDataObject.getTestDataProcessor().mRsrMsg.isSelfCheckPassed();

        // first.. look at the self check results
        if (!selfCheckPassed || stateDataObject.getTestDataProcessor().mRsrMsg.isMotorInUnknownPosition()) {
            // if the self test failed and there is no card in the reader
            // then the self test simply failed and we're done
            if (stateDataObject.getTestDataProcessor().mRsrMsg.isCardInReader() && !selfCheckPassed) {
                // however.. if the self-test failed and theres a card in the
                // reader, ask the user to remove the card, then when they do,
                // go back and do the self-test again, just in case the
                // test failed because of a wet card
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.OLDCARDINREADER);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.OldCardInReader;
                //cardRemovalTimer.Enabled = true;
            } else if (stateDataObject.getTestDataProcessor().mRsrMsg.isMotorNotReset() &&
                    !stateDataObject.getTestDataProcessor().mRsrMsg.isMotorInUnknownPosition()) {
                // no card. but motor not reset.. do another self test
                // we will only come here if we got a card removed inside check reader status
                // send the reader status again.. and wait another 10 seconds.
                if (askForReaderStatus(stateDataObject, true, false)) {
                    return TestStateEventEnum.CheckReaderStatus;
                } else {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.EndTestBeforeTestBegun;
                }
            } else {
                // this will come here if motor in unknown position... or if self test failed without
                // card in reader
                stateDataObject.getTestDataProcessor().mSelfTestPerformed = true;
                {
                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.EQCFAILED);
                    stateDataObject.postEvent(eventInfo);
                    return TestStateEventEnum.EndTestBeforeTestBegun;
                }
            }

        } else {
            // otherwise self test passed, so flag that we've done the self test
            stateDataObject.getTestDataProcessor().mSelfTestPerformed = true;
        }

        // there is no running test.. determine if there is an old test to be retrieved
        if (stateDataObject.getTestDataProcessor().mRsrMsg.isHCMError()) {
            // get the hcm configuration and wait on a response
            return TestStateEventEnum.WaitingForHCMResponse;
        } else if (stateDataObject.getTestDataProcessor().mRsrMsg.isCardInReader()) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.OLDCARDINREADER);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.OldCardInReader;
        } else if (stateDataObject.getTestDataProcessor().mRsrMsg.isMotorNotReset()) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.EQCFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        } else //if (rsrMsg.ErrorCode == ErrorCode.NoError)
        {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);

            if (stateDataObject.getTestDataProcessor().mRsrMsg.hwStatus.getAmbientTemp1() <
                    stateDataObject.getTestDataProcessor().mLowTemperatureLimit) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.LOWTEMPERATURELIMIT);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            } else if (stateDataObject.getTestDataProcessor().mRsrMsg.hwStatus.getAmbientTemp1() >
                    stateDataObject.getTestDataProcessor().mHighTemperatureLimit) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.HIGHTEMPERATURELIMIT);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            } else if (stateDataObject.getTestDataProcessor().mRsrMsg.hwStatus.getBarometricPressureSensor() <
                    stateDataObject.getTestDataProcessor().mLowPressureLimit) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.LOWPRESSURELIMIT);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            } else if (stateDataObject.getTestDataProcessor().mRsrMsg.hwStatus.getBarometricPressureSensor() >
                    stateDataObject.getTestDataProcessor().mHighPressureLimit) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.HIGHPRESSURELIMIT);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            } else if (!stateDataObject.getTestDataProcessor().isPressureSensorsPassedQC(
                    stateDataObject.getTestDataProcessor().mRsrMsg.hwStatus.getBarometricPressureSensor1(),
                    stateDataObject.getTestDataProcessor().mRsrMsg.hwStatus.getBarometricPressureSensor2())) {
                eventInfo.setTestStatusErrorType(TestStatusErrorType.AMBIENTPRESSURESENSORFAILEDQC);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

        }

        //List<AnalyteOption> bgeTestDefaults = stateDataObject.getTestDataProcessor().getAllEnabledMeasuredTest();
        List<AnalyteOption> bgeTestDefaults = new ArrayList<>();
        String[] feedBackMessages = new String[]{};
        stateDataObject.getTestDataProcessor().mKeepWarningMessage = false;

        QCLockoutHandlerReturnCode qrc = stateDataObject.getTestDataProcessor().
                QCScheduleLockoutCheck(bgeTestDefaults, feedBackMessages);
        TestEventInfo eventInfo = new TestEventInfo();
        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
        eventInfo.setTestEventData(feedBackMessages);
        switch (qrc) {
            case Disconnected:
                stateDataObject.getTestDataProcessor().mKeepWarningMessage = true;
                eventInfo.setTestStatusType(TestStatusType.QCLOCKOUT);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;

            case ExpiringSoon:
                stateDataObject.getTestDataProcessor().mKeepWarningMessage = true;
                eventInfo.setTestStatusType(TestStatusType.QCEXPIRINGWARNING);
                stateDataObject.postEvent(eventInfo);
                break;

            case WarningExpired:
                stateDataObject.getTestDataProcessor().mKeepWarningMessage = true;
                eventInfo.setTestStatusType(TestStatusType.QCEXPIREDWARNING);
                stateDataObject.postEvent(eventInfo);
                break;

        }
        //stateDataObject.postEvent(eventInfo);
        return TestStateEventEnum.WaitingForConfiguration1_1Ack;
    }

}
