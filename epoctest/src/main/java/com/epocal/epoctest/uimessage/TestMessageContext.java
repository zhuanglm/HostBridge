package com.epocal.epoctest.uimessage;

import com.epocal.common.androidutil.StringResourceUtil;
import com.epocal.common.epocobjects.EpocUIMessageType;
import com.epocal.common.epocobjects.TimerData;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.teststate.legacyepoctest.type.TestCardInsertedInfo;

import java.util.Arrays;

import io.reactivex.functions.Action;

/**
 * Created by bmate on 7/12/2017.
 * Holds all test  state related context information (messages, colors, click-action)
 * emitted by the UITestController to UI consumer to display real-time test info
 */

public class TestMessageContext {

    private Action mClickHandler;
    private EpocUIMessageType mEpocUIMessageType;
    private EpocTestUIMessage[] mEpocTestUIMessages;

    public Action getClickHandler() {
        return mClickHandler;
    }
    public void setClickHandler(Action clickHandler) {
        mClickHandler = clickHandler;
    }
    public int mProgress = 0;
    public float mBatteryLevel = 0;
    public boolean mVibrate = false;

    public EpocTestUIMessage[] getEpocTestUIMessages() {
        return mEpocTestUIMessages;
    }

    public EpocUIMessageType getEpocUIMessageType() {
        return mEpocUIMessageType;
    }

    public void setEpocUIMessageType(EpocUIMessageType epocUIMessageType) {
        mEpocUIMessageType = epocUIMessageType;
    }
    public void cleanUpp(){
        mClickHandler = null;
      if (mEpocTestUIMessages!= null){
          Arrays.fill(mEpocTestUIMessages,null);
      }
      mEpocUIMessageType = EpocUIMessageType.REGULAR_MESSAGE;

    }

    public void createTestStateChangeMessages(TestStatusType testStatusType) {
        this.createTestStateChangeMessages(testStatusType, null);
    }

     public void createTestStateChangeMessages(TestStatusType testStatusType, Object testEventDataObj){
        mVibrate = false;
        mEpocUIMessageType = EpocUIMessageType.WARNING;
        switch (testStatusType){

            case NEWTESTSTARTING:
                mEpocUIMessageType = EpocUIMessageType.REGULAR_MESSAGE;
                // create the message array from resource file (common.R), add textcolor info
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("NewTestStarting"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("NewTestStarting"),ForeColorType.PRIMARY_FORECOLOR )
                };

                break;
            case CONNECTING:
                mEpocUIMessageType = EpocUIMessageType.PROGRESS_MESSAGE;
                mProgress = -1;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("Connecting"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("Connecting"),ForeColorType.PRIMARY_FORECOLOR )
                };

                break;
            case MISSINGDATAREQUIRED:
                mEpocUIMessageType = EpocUIMessageType.WARNING;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("MissingDataRequired"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("MissingDataRequired"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case NOTESTRUNNING:
                break;
            case TESTRECALCULATED:
                break;
            case CALCULATEDANDUNEDITABLE:
                break;
            case READINGCARD:
                break;
            case QCLOCKOUT:
                break;
            case QCEXPIRINGWARNING:
                break;
            case QCEXPIREDWARNING:
                break;
            case QCEXPIREDWARNINGCARDINREADER:
                break;
            case QCGRACEPERIODWARNING:
                break;
            case CONNECTED:
                mEpocUIMessageType = EpocUIMessageType.REGULAR_MESSAGE;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("Connected"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("Connected"),ForeColorType.PRIMARY_FORECOLOR )
                };

                break;
            case DISCONNECTED:
                mEpocUIMessageType = EpocUIMessageType.WARNING;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ConnectionLost"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ConnectionLost"),ForeColorType.PRIMARY_FORECOLOR )
                };

                break;
            case CONFIGURATION:
                mEpocUIMessageType = EpocUIMessageType.WARNING;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("Configuring"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("MayEnterData"),ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("Configuring"),ForeColorType.PRIMARY_FORECOLOR )
                };

                break;
            case FLUIDICSCALIBRATION:
                //testEventDataObj is an Integer object with remaining time in seconds
                EpocTestUIMessage remainingTimeMsg = new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemainingTime"),ForeColorType.PRIMARY_FORECOLOR );
                EpocTestUIMessage calibratingRemainingMsg = new EpocTestUIMessage(StringResourceUtil.getStringIDByName("Calibrating_Remaining"),ForeColorType.PRIMARY_FORECOLOR);

                if ((testEventDataObj != null) && (testEventDataObj instanceof TimerData)) {
                    TimerData timerdata = (TimerData)testEventDataObj;
                    Object[] parms = { timerdata.string_params };
                    remainingTimeMsg.setOptionalStringParameters(parms);
                    calibratingRemainingMsg.setOptionalStringParameters(parms);
                    mProgress = timerdata.percentage_remaining;
                }
                mEpocUIMessageType = EpocUIMessageType.PROGRESS_MESSAGE;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CalibratingDoNotInject"),ForeColorType.PRIMARY_FORECOLOR ),
                        remainingTimeMsg,
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        calibratingRemainingMsg,
                };
                break;
            case READYTOTEST:
                mEpocUIMessageType = EpocUIMessageType.WARNING;
                mVibrate = true;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("InsertCard"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("InsertCard"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case DEVICEINFO:
                break;
            case CARDINSERTED:
                EpocTestUIMessage cardLotMsg = new EpocTestUIMessage(StringResourceUtil.getStringIDByName("MockLot"),ForeColorType.PRIMARY_FORECOLOR );
                if (testEventDataObj != null && testEventDataObj instanceof TestCardInsertedInfo) {
                    Object[] parms = { ((TestCardInsertedInfo)testEventDataObj).getLotInfo() };
                    cardLotMsg.setOptionalStringParameters(parms);
                }
                mEpocUIMessageType = EpocUIMessageType.REGULAR_MESSAGE;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardInserted"),ForeColorType.PRIMARY_FORECOLOR ),
                        cardLotMsg,
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case SAMPLEINTRODUCTION:
                mEpocUIMessageType = EpocUIMessageType.PROGRESS_MESSAGE;
                EpocTestUIMessage siRemainingTimeMsg = new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemainingTime"),ForeColorType.PRIMARY_FORECOLOR );
                if ((testEventDataObj != null) && (testEventDataObj instanceof TimerData)) {
                    TimerData timerdata = (TimerData)testEventDataObj;
                    Object[] parms = { timerdata.string_params };
                    siRemainingTimeMsg.setOptionalStringParameters(parms);
                    mProgress = timerdata.percentage_remaining;
                    if (mProgress == 100)
                        mVibrate = true;
                } else {
                    mProgress = -1;
                }
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("SampleIntroduction"),ForeColorType.PRIMARY_FORECOLOR ),
                        siRemainingTimeMsg,
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case SAMPLEPROCESSING:
                mEpocUIMessageType = EpocUIMessageType.PROGRESS_MESSAGE;
                EpocTestUIMessage spRemainingTimeMsg = new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemainingTime"),ForeColorType.PRIMARY_FORECOLOR );
                if ((testEventDataObj != null) && (testEventDataObj instanceof TimerData)) {
                    TimerData timerdata = (TimerData)testEventDataObj;
                    Object[] parms = { timerdata.string_params };
                    spRemainingTimeMsg.setOptionalStringParameters(parms);
                    mProgress = timerdata.percentage_remaining;
                } else {
                    mProgress = -1;
                }
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("SampleProcessing"),ForeColorType.PRIMARY_FORECOLOR ),
                        spRemainingTimeMsg,
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case TESTCALCULATING:
                mEpocUIMessageType = EpocUIMessageType.PROGRESS_MESSAGE;
                mProgress = -1;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TestCalculating"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case TESTCALCULATED:
                mEpocUIMessageType = EpocUIMessageType.WARNING;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TestCalculated"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case UNKNOWN:
            default:
                mEpocUIMessageType = EpocUIMessageType.ERROR;
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("UnknownStatus"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
        }

    }
    public void createTestStateErrorMessages(TestStatusErrorType testStatusErrorType){
        mEpocUIMessageType = EpocUIMessageType.ERROR;

        switch (testStatusErrorType) {
            case UNKNOWN:
            case HCMERROR:

                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("UnknownError"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("UnknownError"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case COMMUNICATIONFAILED:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CommunicationFailed"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("AttemptingToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CommunicationFailed"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case READERNOCOMPATIBLE:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderNotCompatible"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("UseAnotherReader"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderNotCompatible"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case READERERROR:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderGeneralError"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderGeneralError"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case READERBATTERYLOW:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderBatteryLow"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderBatteryLow"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case EQCFAILED:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("EQCFailed_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("EQCFailed_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case LOWTEMPERATURELIMIT:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("LowTempLimit_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("LowTempLimit_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case HIGHTEMPERATURELIMIT:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("HighTempLimit_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("HighTempLimit_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR)
                };
                break;
            case HIGHPRESSURELIMIT:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("HighPressureLimit_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("HighPressureLimit_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case LOWPRESSURELIMIT:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("LowPressureLimit_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("LowPressureLimit_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR)
                };
                break;
            case AMBIENTPRESSURESENSORFAILEDQC:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("AmbientPressureIQCFailed_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("AmbientPressureIQCFailed_ConnectClosed"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case READERNEEDMAINTENANCE:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderMaintenanceRequiered"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("UseAnotherReader"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderMaintenanceRequiered"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case REDOEQC:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("E13ConnectionClosed"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("E13ConnectionClosed"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case FLUIDDETECTEDINCARD:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("FluidDetectedInCard"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("FluidDetectedInCard"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case UNABLEREADCARDBARCODE:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("UnableToReadBarcode"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertAgain"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("UnableToReadBarcode"), ForeColorType.PRIMARY_FORECOLOR)
                };
                break;
            case INVALIDCARDBARCODE:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("InvalidBarcode"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertAgain"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("InvalidBarcode"), ForeColorType.PRIMARY_FORECOLOR)
                };
                break;
            case INVALIDCARDMANUFACTUREDATE:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("InvalidCardManufactureDate"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CheckHostDate"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("InvalidCardManufactureDate"), ForeColorType.PRIMARY_FORECOLOR)
                };
                break;
            case CARDEXPIRED:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardExpired"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardExpired"), ForeColorType.PRIMARY_FORECOLOR)
                };
                break;
            case CARDTYPENOTSUPPORTED:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardTypeNotSupported"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardTypeNotSupported"), ForeColorType.PRIMARY_FORECOLOR)
                };
                break;
            case READERREQUIRESUPGRADE:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderUpgradeRequired"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("Connection_Closed"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderUpgradeRequired"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case NOTESTENABLEFORCARDTYPE:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("NoTestEnabledOnCard"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("NoTestEnabledOnCard"), ForeColorType.PRIMARY_FORECOLOR)
                };
                break;
            case CARDINSERTEDWHENMOTORMOVING:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardInsertedEarly"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertAgain"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardInsertedEarly"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case CARDINSERTEDNOTPROPERLY:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardNotInsertedProperly"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertAgain"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardNotInsertedProperly"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case EXCEEDMAXTEST:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("Connection_Closed"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TapToReconnect"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("Connection_Closed"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case OLDCARDINREADER:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("OldCardInReader"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveCard"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("OldCardInReader"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case EARLYINJECTION:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("EarlyInjectionError"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("EarlyInjectionError"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            // todo implement for Host version 4.3.1
            case ERROROCCURREDTESTSTOP:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderGeneralError"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderGeneralError"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;
            case HEMATOCRITLOWRESISTANCE:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderGeneralError"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("ReaderGeneralError"), ForeColorType.PRIMARY_FORECOLOR)
                };

                break;

            case HUMIDITYCHECKFAILED:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCHumidityCheck"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCHumidityCheck"), ForeColorType.PRIMARY_FORECOLOR)
                };
                break;
            case REALTIMEQCFAILED:
                mEpocTestUIMessages = new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCSensorCheck"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"), ForeColorType.SECONDARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"), ForeColorType.PRIMARY_FORECOLOR),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCSensorCheck"), ForeColorType.PRIMARY_FORECOLOR)
                };
                break;
            case FLUIDICSCHECKFAILED:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCFluidicsCheck"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCFluidicsCheck"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case SAMPLEINJECTEDTOOFAST:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCFastSampleInjection"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCFastSampleInjection"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case SAMPLEINJECTEDTOOSLOWLY:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCInsufficientSample"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCInsufficientSample"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case SAMPLINGTIMEOUT:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("SampleNotIntroducedInTime"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("SampleNotIntroducedInTime"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case CALIBRATIONFLUIDICSNOTDETECTED:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCCalibrationFluidError"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCCalibrationFluidError"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case THERMALQCFAILED:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCThermalQCFailed"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCThermalQCFailed"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case CARDREMOVEDDURINGTEST:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardRemovedFromReader"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CardRemovedFromReader"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case UNABLETOCONNECT:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CommunicationFailed"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("AttemptingToReconnect"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("CommunicationFailed"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case SAMPLEDELIVERY:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCSampleDelivery"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("IQCSampleDelivery"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            case ENDTESTAFTERTESTBEGUN:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TestStop"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("RemoveInsertNewCard"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("TestStop"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
            default:
                mEpocTestUIMessages =new EpocTestUIMessage[]{
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("UnknownError"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.SECONDARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR ),
                        new EpocTestUIMessage(StringResourceUtil.getStringIDByName("empty_string"),ForeColorType.PRIMARY_FORECOLOR )
                };
                break;
        }

    }
}
