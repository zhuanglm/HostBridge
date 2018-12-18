package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.common.epocobjects.AnalyteOption;
import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.TestMode;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.enumtype.BarcodeVerificationCode;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.epoctest.teststate.legacyepoctest.type.TestCardInsertedInfo;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.command.LegacyReqInvalidCard;
import com.epocal.reader.legacy.message.response.LegacyRspCardInserted;
import com.epocal.reader.legacy.message.response.LegacyRspDeviceID;
import com.epocal.reader.legacy.message.response.LegacyRspError;
import com.epocal.statemachine.IEventEnum;
import com.epocal.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rzhuang on Aug 10 2018.
 */
@SuppressWarnings("unchecked")
public class Ready extends LegacyTestState {
    public Ready() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {

        TestEventInfo eventInfo = new TestEventInfo();
        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
        eventInfo.setTestStatusType(TestStatusType.READYTOTEST);
        stateDataObject.postEvent(eventInfo);

        if (stateDataObject.getTestDataProcessor().mKeepWarningMessage) {
            stateDataObject.getTestDataProcessor().mKeepWarningMessage = false;
        }
        stateDataObject.getTestDataProcessor().mTestState = false;
        super.onEntry(stateDataObject);
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
            stateDataObject.getTestDataProcessor().legacyPrepareForNewTest();


            LegacyRspCardInserted insertMsg = (LegacyRspCardInserted) message;
//            int maxEQCPeriod = 0;
            int maxEQCPeriod = 2;   //set for debug
//        if (!epoc.host.maintenance.QCFeature.QCScheduleManager.Instance.CheckEQCFromQCSchedule(ref maxEQCPeriod))
//        {
//            maxEQCPeriod = stateDataObject.TestParameter.MaxNumHoursBetweenQC;
//        }

            Calendar cal = Calendar.getInstance();
            cal.setTime(stateDataObject.getTestDataProcessor().getLastSelfTestTime());
            if (DateUtil.daysBetween(cal, DateUtil.now()) >= maxEQCPeriod) {
                //if redo EQC, stateDataObject.TestDataProcessor.LastSelfTestTime need to be updated, Testrecord.LastEQCDateTime as well.
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.REDOEQC);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            if (hasCardBeenUsed(insertMsg.getmDryCheck())) {
                // dry card check failed somewhere
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.FLUIDDETECTEDINCARD);
                stateDataObject.postEvent(eventInfo);

                stateDataObject.sendMessage(new LegacyReqInvalidCard());
                return TestStateEventEnum.LegacyCardInReader;
            } else {
                // get the barcode string
                BarcodeVerificationCode barcodeReturnCode = stateDataObject.getTestDataProcessor().
                        getBarcodeInformation().decodeLegacy(insertMsg.getBarcode(), insertMsg.getBarcodeLength());

                if (barcodeReturnCode == BarcodeVerificationCode.Failure) {
                    stateDataObject.sendMessage(new LegacyReqInvalidCard());

                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.UNABLEREADCARDBARCODE);
                    stateDataObject.postEvent(eventInfo);

                    return TestStateEventEnum.LegacyCardInReader;
                } else if (barcodeReturnCode == BarcodeVerificationCode.Invalid) {
                    stateDataObject.sendMessage(new LegacyReqInvalidCard());

                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.INVALIDCARDBARCODE);
                    stateDataObject.postEvent(eventInfo);

                    return TestStateEventEnum.LegacyCardInReader;
                }
                // check if we have info for this card type.. or whether the card usage is 0 (human) and we're a vet
                // or vice versa.. cardusage is 1 (vet) and we're 0 (human).. then the card type is not supported
                // card usage flag is checked for barcodes NOT of length 4..
                // if a barcode is of length 4, then we allow it as long as its before the cutoff date for
                // vets to use 4 digit barcodes
                else if (!stateDataObject.getTestDataProcessor().isCardTypeSupported(
                        stateDataObject.getTestDataProcessor().getCardType())) {
                    stateDataObject.sendMessage(new LegacyReqInvalidCard());

                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDTYPENOTSUPPORTED);
                    stateDataObject.postEvent(eventInfo);

                    return TestStateEventEnum.LegacyCardInReader;
                }
                // always give the expiry in the future message for customer
                else if (barcodeReturnCode == BarcodeVerificationCode.ExpiryInTheFuture) {
                    {
                        stateDataObject.sendMessage(new LegacyReqInvalidCard());

                        TestEventInfo eventInfo = new TestEventInfo();
                        eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                        eventInfo.setTestStatusErrorType(TestStatusErrorType.INVALIDCARDMANUFACTUREDATE);
                        stateDataObject.postEvent(eventInfo);

                        return TestStateEventEnum.LegacyCardInReader;
                    }
//                } else if (barcodeReturnCode == BarcodeVerificationCode.Expired) {
//                    if (!stateDataObject.getTestDataProcessor().allowExpiredCards()) {
//                        stateDataObject.sendMessage(new LegacyReqInvalidCard());
//
//                        TestEventInfo eventInfo = new TestEventInfo();
//                        eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
//                        eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDEXPIRED);
//                        stateDataObject.postEvent(eventInfo);
//
//                        return TestStateEventEnum.LegacyCardInReader;
//                    }
                }

                String barcodeString = stateDataObject.getTestDataProcessor().getBarcodeInformation().
                        getBarcodeString();
                stateDataObject.getTestDataProcessor().setBarcodeString(barcodeString);
                stateDataObject.getTestDataProcessor().mCardNumber = barcodeString;

                stateDataObject.getTestDataProcessor().setCardType(stateDataObject.getTestDataProcessor()
                        .getBarcodeInformation().getCardType());

                if (stateDataObject.getTestDataProcessor().getCardType() == 0) {
                    stateDataObject.getTestDataProcessor().setCardType(111);
                }

                //TestConfiguration:getTestSequence all the item's sensorlayout=0
                stateDataObject.getTestDataProcessor().mSensorLayoutNumber = (short) stateDataObject.
                        getTestDataProcessor().getBarcodeInformation().mLineNumber;
                stateDataObject.getTestDataProcessor().setSensorLayout((short) 0);


                List<AnalyteOption> bgeTestDefaults =
                        stateDataObject.getTestDataProcessor().getTestDefaultsForOneCardType();

                int numEnabledTests = 0;
                int numSelectedTests = 0;

                for (int i = 0; i < bgeTestDefaults.size(); i++) {
                    if (bgeTestDefaults.get(i).getOptionType() == EnabledSelectedOptionType.EnabledSelected
                            || bgeTestDefaults.get(i).getOptionType() == EnabledSelectedOptionType.EnabledUnselected) {
                        numEnabledTests++;

                        if (bgeTestDefaults.get(i).getOptionType() == EnabledSelectedOptionType.EnabledSelected) {
                            numSelectedTests++;
                        }
                    }
                }

                // no enabled tests?
                //boolean noEnabledTestDueToCardOption = false;
                if ((numEnabledTests == 0) || (stateDataObject.getTestDataProcessor().mIsVet && (numSelectedTests == 0))) {
                    //noEnabledTestDueToCardOption = true;

                    stateDataObject.sendMessage(new LegacyReqInvalidCard());

                    TestEventInfo eventInfo = new TestEventInfo();
                    eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                    eventInfo.setTestStatusErrorType(TestStatusErrorType.NOTESTENABLEFORCARDTYPE);
                    stateDataObject.postEvent(eventInfo);

                    return TestStateEventEnum.LegacyCardInReader;
                }

                // set the card type string
                stateDataObject.getTestDataProcessor().mCanRunThermalCheck = false;

                TestCardInsertedInfo cardinfo = new TestCardInsertedInfo();
                cardinfo.setLotInfo(stateDataObject.getTestDataProcessor().getBarcodeInformation()
                        .getLotNumberStringWithDash());

                ArrayList<AnalyteOption> testSelectionArrayList = new ArrayList<>();
                List<AnalyteName> tempAnalytes = new ArrayList<>();
                List<AnalyteName> tempExpiredAnalytes = new ArrayList<>();

                // create the test selection List.. only put in tests that are enabled by default
                for (int i = 0; i < bgeTestDefaults.size(); i++) {
                    if ((stateDataObject.getTestDataProcessor().getTestConfiguration()
                            .isMeasuredAnalyte(bgeTestDefaults.get(i).getAnalyteName())) &&
                            ((bgeTestDefaults.get(i).getOptionType() == EnabledSelectedOptionType.EnabledSelected
                                    || bgeTestDefaults.get(i).getOptionType() == EnabledSelectedOptionType.EnabledUnselected))) {
                        cardinfo.getAnalyteOptions().add(bgeTestDefaults.get(i));
                        tempAnalytes.add(bgeTestDefaults.get(i).getAnalyteName());
                        testSelectionArrayList.add(bgeTestDefaults.get(i));
                    }
                }
                LegacyRspDeviceID deviceIdResponse = stateDataObject.getTestDataProcessor().mDeviceIDResponse;

                if (stateDataObject.getTestDataProcessor().getTestRecord().getTestMode() == TestMode.BloodTest) {
                    if (deviceIdResponse != null) {
                        String[] feedBackMessages = null;
//                    var retCode = stateDataObject.getTestDataProcessor().QCScheduleLockoutCheckAfterCardInserted(tempExpiredAnalytes, tempAnalytes, testSelectionArrayList, ref feedBackMessages);
//                    switch (retCode)
//                    {
//                        case epoc.common.types.QCLockoutHandlerReturnCode.Disconnected:
//                            RaiseEvent(stateDataObject, epoc.common.types.TestStateType.QCLockout, feedBackMessages);
//                            return TestEventID.EndTestBeforeTestBegun.ToString();
//                        break;
//                        case epoc.common.types.QCLockoutHandlerReturnCode.ExpiringSoon:
//                            RaiseEvent(stateDataObject, epoc.common.types.TestStateType.QCExpiringWarning, feedBackMessages);
//                            break;
//                        case epoc.common.types.QCLockoutHandlerReturnCode.WarningExpired:
//                            RaiseEvent(stateDataObject, epoc.common.types.TestStateType.QCExpiredWarning, feedBackMessages);
//                            break;
//                        case epoc.common.types.QCLockoutHandlerReturnCode.RemoveCard:
//                            RaiseEvent(stateDataObject, epoc.common.types.TestStateType.QCExpiredWarningCardInReader, feedBackMessages);
//                            epoc.host.communication.Message.InvalidCard invalidCard = new epoc.host.communication.Message.InvalidCard();
//                            stateDataObject.Communication.SendMessage(invalidCard);
//                            return TestEventID.LegacyCardInReader.ToString();
//                        break;
//                        case epoc.common.types.QCLockoutHandlerReturnCode.GraceMessage:
//                            RaiseEvent(stateDataObject, epoc.common.types.TestStateType.QCGracePeriodWarning, feedBackMessages);
//                            if (tempExpiredAnalytes.Count > 0)
//                            {
//                                for (int m = 0; m < tempExpiredAnalytes.Count; m++)
//                                {
//                                    for (int n = 0; n < cardinfo.AnalyteOptions.Count; n++)
//                                    {
//                                        if (cardinfo.AnalyteOptions[n].AnalyteName == tempExpiredAnalytes[m])
//                                        {
//                                            cardinfo.AnalyteOptions.RemoveAt(n);
//                                            break;
//                                        }
//                                    }
//                                    continue;
//                                }
//                            }
//                            return TestEventID.AfterReadyForQCLockout.ToString();
//                        break;
//                    }
                    }
                }

                if (tempExpiredAnalytes.size() > 0) {
                    for (int m = 0; m < tempExpiredAnalytes.size(); m++) {
                        for (int n = 0; n < cardinfo.getAnalyteOptions().size(); n++) {
                            if (cardinfo.getAnalyteOptions().get(n).getAnalyteName() == tempExpiredAnalytes.get(m)) {
                                cardinfo.getAnalyteOptions().remove(n);
                                break;
                            }
                        }
                    }
                }
                stateDataObject.getTestDataProcessor().mCardInfo = cardinfo;
                stateDataObject.getTestDataProcessor().getCardBarcode();
                // send the test list into the TabPage along with each tests' flags

                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                eventInfo.setTestStatusType(TestStatusType.CARDINSERTED);
                eventInfo.setTestEventData(stateDataObject.getTestDataProcessor().mCardInfo);
                stateDataObject.postEvent(eventInfo);
            }

            stateDataObject.mRedistributed = true;
            return TestStateEventEnum.WaitingForTestGetReaderStatus;

        } else if (message.getDescriptor().getType() == LegacyMessageType.CardDetected.value) {
            askForReaderStatus(stateDataObject, false, false);
        } else if (message.getDescriptor().getType() == LegacyMessageType.Error.value) {
            // this condition may happen when the host sends starttest, but
            // the reader has already sent cardremoved.. we will go to the right
            // state of ready, but then we've got to handle the error message
            // with type invalid request that will show up shotly thereafter
            LegacyRspError errMsg = (LegacyRspError) message;

            if ((errMsg.getMessageCode() == LegacyRspError.ErrorType.UnableToProcessRequest.value) ||
                    (errMsg.getErrorCode() == LegacyRspError.ErrorType.InvalidRequest)) {
                return TestStateEventEnum.Handled;
            } else if (errMsg.getErrorCode() == LegacyRspError.ErrorType.CardInsertedWhileMotorMoving) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDINSERTEDWHENMOTORMOVING);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.LegacyCardInReader;
            } else if (errMsg.getErrorCode() == LegacyRspError.ErrorType.DetectionSwitchBounce) {

                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDINSERTEDNOTPROPERLY);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.LegacyCardInReader;
            }
        } else if (message.getDescriptor().getType() == LegacyMessageType.CardRemoved.value) {
            return TestStateEventEnum.Ready;
        }
        return super.onEventPreHandle(stateDataObject);
    }

    private boolean hasCardBeenUsed(byte[] dryCardCheckResults) {
        for (byte dryCardCheckResult : dryCardCheckResults)
            if ((dryCardCheckResult & 0xFF) != 0xFF) {
                return false;//true;
            }

        return false;
    }

}
