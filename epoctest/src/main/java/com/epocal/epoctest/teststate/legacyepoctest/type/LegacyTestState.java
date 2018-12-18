package com.epocal.epoctest.teststate.legacyepoctest.type;

import android.util.Log;

import com.epocal.common.epocobjects.TimerData;
import com.epocal.common.realmentities.EqcInfo;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.legacy.CustomConfigSubcode;
import com.epocal.reader.enumtype.legacy.CustomConfigTask;
import com.epocal.reader.enumtype.legacy.CustomConfigType;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.enumtype.legacy.TestStoppedReasons;
import com.epocal.reader.legacy.message.request.command.LegacyReqReaderStatus;
import com.epocal.reader.legacy.message.request.configuration.LegacyReqConfig1_2;
import com.epocal.reader.legacy.message.request.configuration.LegacyReqCustomConfig;
import com.epocal.reader.legacy.message.response.LegacyRspDataPacket;
import com.epocal.reader.legacy.message.response.LegacyRspDeviceID;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.statemachine.IEventEnum;
import com.epocal.statemachine.State;
import com.epocal.util.StringUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by rzhuang on July 30 2018.
 */

public class LegacyTestState extends State<TestStateDataObject> {
    private Observable mCountdownObservable;
    private Disposable mDisposable;

    public LegacyTestState() {
        super();
    }

    protected IMsgPayload message;

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        Log.d("State machine-entry:", this.getStateName());
    }

    @Override
    public void onExit(TestStateDataObject stateDataObject) {
        super.onExit(stateDataObject);
        Log.d("State machine-exit:", this.getStateName());
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.CancelConnection) {
            stateDataObject.getTestCommunication().disconnect();
            stateDataObject.closePreviousTest();
            Log.d("Received Cancel:", this.getStateName());
            return TestStateEventEnum.EndTestBeforeTestBegun;
        } else if (stateDataObject.getTestStateAction() == TestStateActionEnum.CommunicationFailed) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        } else if (stateDataObject.getTestStateAction() == TestStateActionEnum.RealTimeQCFailed) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.REALTIMEQCFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestAfterTestBegun;
        }

        if (stateDataObject.getIMsgPayload() != null) {
            message = stateDataObject.getIMsgPayload();
            if (message.getDescriptor().getType() == LegacyMessageType.DataPacket.value) {
                stateDataObject.getTestDataProcessor().setLastRecordedTime(
                        (float) Math.round(stateDataObject.getTestDataProcessor().getLastRecordedTime()
                                + stateDataObject.getTestDataProcessor().getTimeIncrement()));
                stateDataObject.getTestDataProcessor().parseDataPacket((LegacyRspDataPacket) message, stateDataObject, this);
            } else if (message.getDescriptor().getType() == LegacyMessageType.DeviceIdResponse.value) {
                stateDataObject.getTestDataProcessor().mARMfirmwareId = ((LegacyRspDeviceID) message)
                        .getLegacyInfo().getARMfirmwareId();
                stateDataObject.getTestDataProcessor().mDAMfirmwareId = ((LegacyRspDeviceID) message)
                        .getLegacyInfo().getDAMfirmwareId();
            }
        }

//        message = stateDataObject.getIMsgPayload();
//        if (message == null) {
//            TestEventInfo eventInfo = new TestEventInfo();
//            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
//            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
//            stateDataObject.postEvent(eventInfo);
//            return TestStateEventEnum.EndTestBeforeTestBegun;
//        }

        return super.onEventPreHandle(stateDataObject);
    }

    protected boolean askForReaderStatus(TestStateDataObject stateDataObject, boolean performSelfTest, boolean getTemperatures) {
        LegacyReqReaderStatus getReaderStatus = new LegacyReqReaderStatus();
        getReaderStatus.setPerformSelfCheck(performSelfTest);
        LegacyReqConfig1_2 config12Msg = new LegacyReqConfig1_2();
        //config12Msg.TransmitRawData = true;
        stateDataObject.getTestDataProcessor().fillReqConfig1_2(config12Msg);

        // if there is to be a self test, we want the countdown bar and the whole shebang
        if (performSelfTest) {
            //code from C# 2008
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.CONFIGURATION);
            eventInfo.setTestEventData(config12Msg.getSelfCheckDuration() + 7);
            stateDataObject.postEvent(eventInfo);
        }

        getReaderStatus.setTemperatures(getTemperatures);
        return stateDataObject.sendMessage(getReaderStatus);
    }

    protected boolean sendQCInfo(TestStateDataObject stateDataObject, EqcInfo eqcInfo) {
        return stateDataObject.getTestDataProcessor().sendQCInfo(stateDataObject.getTestDataProcessor(), eqcInfo);
    }

    protected LegacyReqCustomConfig reqCustomConfigMsg(TestStateDataObject stateDataObject,
                                                       byte[] data,
                                                       CustomConfigSubcode subcode,
                                                       CustomConfigTask task,
                                                       CustomConfigType type,
                                                       int formatVersion,
                                                       short contentVersion,
                                                       int position) {
        LegacyReqCustomConfig messageCustomConfig = new LegacyReqCustomConfig();

        messageCustomConfig.setCustomConfigSubcode(subcode);
        messageCustomConfig.setCustomConfigTask(task);
        messageCustomConfig.setCustomConfigType(type);

        messageCustomConfig.setFormatVersion((byte) formatVersion);
        messageCustomConfig.setContentVersion(contentVersion);
        messageCustomConfig.setPosition(position);
        messageCustomConfig.setHostID(stateDataObject.getTestDataProcessor().getTestRecord().getHost().getSerialNumber());

        if (data != null) {
            messageCustomConfig.mDataBlock = data;
        }

        byte lut[] = {0x10, 0x3B, (byte) 0xEE, 0x15};
        byte tl[] = {0x0a, 0x07, 0, 0};
        messageCustomConfig.setLastUpdateTimeBySeconds(LegacyReqCustomConfig.getInt(lut, 0));
        messageCustomConfig.setTableLength(LegacyReqCustomConfig.getInt(tl, 0));
        messageCustomConfig.setTableCRC((byte) 0x51);

        return messageCustomConfig;

    }

    protected HostErrorCode ConvertTestStoppedToErrorCode(TestStoppedReasons reason, TestStateDataObject stateDataObject) {
        if (stateDataObject.getTestDataProcessor().mUserClosedPage) {
            if (getStateID() == TestStateEventEnum.FluidicsCalibration) {
                return HostErrorCode.UserStoppedTestDuringFluidics;
            } else if (getStateID() == TestStateEventEnum.SampleIntroduction) {
                return HostErrorCode.UserStoppedTestDuringSampleIntro;
            } else {
                return HostErrorCode.UserStoppedTestDuringSampling;
            }
        } else if (reason == TestStoppedReasons.MultipleQCFailed) {
            if (getStateID() == TestStateEventEnum.FluidicsCalibration) {
                return HostErrorCode.RealtimeQCFailedDuringFluidics;
            } else if (getStateID() == TestStateEventEnum.SampleIntroduction) {
                return HostErrorCode.RealtimeQCFailedDuringSampleIntro;
            } else {
                return HostErrorCode.RealtimeQCFailedDuringSampling;
            }
        } else if ((reason == TestStoppedReasons.CalDetectionTimerExpired) ||
                (reason == TestStoppedReasons.CalFluidNotDetected)) {
            return HostErrorCode.CalNotDetected;
        } else if (reason == TestStoppedReasons.SampleIntroductionTimerExpired) {
            return HostErrorCode.SampleNotDetected;
        } else if (reason == TestStoppedReasons.SamplingTimerExpired) {
            return HostErrorCode.NoError;
        } else if (reason == TestStoppedReasons.HandleTurningTimerExpired) {
            return HostErrorCode.HandleNotTurned;
        } else if ((reason == TestStoppedReasons.HandleSwitchOff) ||
                (reason == TestStoppedReasons.CardRemoved)) {
            if (getStateID() == TestStateEventEnum.FluidicsCalibration) {
                return HostErrorCode.CardRemovedDuringFluidics;
            } else if (getStateID() == TestStateEventEnum.SampleIntroduction) {
                return HostErrorCode.CardRemovedDuringSampleIntro;
            } else {
                return HostErrorCode.CardRemovedDuringSampling;
            }
        } else if (reason == TestStoppedReasons.ThermalQCFailed) {
            if (getStateID() == TestStateEventEnum.FluidicsCalibration) {
                return HostErrorCode.ThermalQCFailedDuringFluidics;
            } else if (getStateID() == TestStateEventEnum.SampleIntroduction) {
                return HostErrorCode.ThermalQCFailedDuringSampleIntro;
            } else {
                return HostErrorCode.ThermalQCFailedDuringSampling;
            }
        } else if (reason == TestStoppedReasons.DetectionSwitchBounce) {
            if (getStateID() == TestStateEventEnum.FluidicsCalibration) {
                // reserved, cp stop, etc
                return HostErrorCode.CardRemovedDuringFluidics;
            } else if (getStateID() == TestStateEventEnum.SampleIntroduction) {
                // reserved, cp stop, etc
                return HostErrorCode.CardRemovedDuringSampleIntro;
            } else {
                // reserved, cp stop, etc
                return HostErrorCode.CardRemovedDuringSampling;
            }
        } else {
            if (getStateID() == TestStateEventEnum.FluidicsCalibration) {
                // reserved, cp stop, etc
                return HostErrorCode.ReaderStoppedTestDuringFluidics;
            } else if (getStateID() == TestStateEventEnum.SampleIntroduction) {
                // reserved, cp stop, etc
                return HostErrorCode.ReaderStoppedTestDuringSampleIntro;
            } else {
                // reserved, cp stop, etc
                return HostErrorCode.ReaderStoppedTestDuringSampling;
            }
        }
    }

    protected TestStatusErrorType ErrorCodeToErrorEventType(HostErrorCode errorCode,
                                                            TestStatusErrorType defaultErrorType) {
        if (errorCode == HostErrorCode.HematocritLowResistance) {
            return TestStatusErrorType.HEMATOCRITLOWRESISTANCE;
        } else if (errorCode == HostErrorCode.EarlyInjection) {
            return TestStatusErrorType.EARLYINJECTION;
        } else if (errorCode == HostErrorCode.FluidicsFailedQCDuringCalibration
                || errorCode == HostErrorCode.FluidicsFailedQCDuringSampleIntro) {
            return TestStatusErrorType.FLUIDICSCHECKFAILED;
        } else if (errorCode == HostErrorCode.HumidityCheckFailed) {
            return TestStatusErrorType.HUMIDITYCHECKFAILED;
        } else if (errorCode == HostErrorCode.ErrorOccurredDuringFluidics
                || errorCode == HostErrorCode.ErrorOccurredDuringSampleIntro
                || errorCode == HostErrorCode.ErrorOccurredDuringSampling) {
            return TestStatusErrorType.ERROROCCURREDTESTSTOP;
        } else if (errorCode == HostErrorCode.SampleInjectedTooFast) {
            return TestStatusErrorType.SAMPLEINJECTEDTOOFAST;
        } else if (errorCode == HostErrorCode.SampleInjectedTooSlowly) {
            return TestStatusErrorType.SAMPLEINJECTEDTOOSLOWLY;
        } else if (errorCode == HostErrorCode.SampleFailedQC) {
            return TestStatusErrorType.SAMPLEDELIVERY;
        } else if (errorCode == HostErrorCode.CalNotDetected) {
            return TestStatusErrorType.CALIBRATIONFLUIDICSNOTDETECTED;
        } else if (errorCode == HostErrorCode.SampleNotDetected) {
            return TestStatusErrorType.SAMPLINGTIMEOUT;
        } else if (errorCode == HostErrorCode.ThermalQCFailedDuringFluidics
                || errorCode == HostErrorCode.ThermalQCFailedDuringSampleIntro
                || errorCode == HostErrorCode.ThermalQCFailedDuringSampling) {
            return TestStatusErrorType.THERMALQCFAILED;
        } else if (errorCode == HostErrorCode.CardRemovedDuringFluidics
                || errorCode == HostErrorCode.CardRemovedDuringSampleIntro
                || errorCode == HostErrorCode.CardRemovedDuringSampling) {
            return TestStatusErrorType.CARDREMOVEDDURINGTEST;
        } else if (errorCode == HostErrorCode.ReaderStoppedRespondingDuringFluidics
                || errorCode == HostErrorCode.ReaderStoppedRespondingDuringSampleIntro
                || errorCode == HostErrorCode.ReaderStoppedRespondingDuringSampling) {
            return TestStatusErrorType.ERROROCCURREDTESTSTOP;
        } else if (errorCode == HostErrorCode.RealtimeQCFailedDuringFluidics
                || errorCode == HostErrorCode.RealtimeQCFailedDuringSampleIntro
                || errorCode == HostErrorCode.RealtimeQCFailedDuringSampling) {
            return TestStatusErrorType.REALTIMEQCFAILED;
        }
        return defaultErrorType;
    }

    protected void startTestTypeCountDown(final TestStatusType testStatusType, final int totalTimeInSeconds,
                                          int delay, int period, final TestStateDataObject stateDataObject) {
        // Check to make sure previous one is disposed
        stopTestTypeCountDown();

        // Start new interval timer
        mCountdownObservable = Observable.interval(delay, period, TimeUnit.SECONDS)
                .takeUntil(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) {
                        return (totalTimeInSeconds - aLong) == 0;
                    }
                })
                .map(new Function<Long, TimerData>() {
                    @Override
                    public TimerData apply(Long aLong) {
                        int remainingTime = (int) (totalTimeInSeconds - aLong);
                        double pct = (((double) totalTimeInSeconds - aLong) / totalTimeInSeconds) * 100.0;
                        return new TimerData(StringUtil.timeInSecondToTimeString(remainingTime), (int) pct);
                    }
                });

        mDisposable = mCountdownObservable.subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                eventInfo.setTestStatusType(testStatusType);
                eventInfo.setTestEventData(o);
                if (stateDataObject != null)
                    stateDataObject.postEvent(eventInfo);
            }
        });
    }

    protected void stopTestTypeCountDown() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            mCountdownObservable = null;
        }
    }
}
