package com.epocal.epoctest.testprocess;

import android.os.AsyncTask;

import com.epocal.common.androidutil.RxUtil;
import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.types.am.TestMode;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.enumtype.BarcodeVerificationCode;
import com.epocal.epoctest.enumtype.QAScheduleLookoutReturnCode;
import com.epocal.epoctest.enumtype.TestProcessStateReturnCode;
import com.epocal.epoctest.enumtype.ValidateAnalyteTestReturnCode;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.type.CardInformation;
import com.epocal.statemachine.StateMachine;
import com.epocal.util.RefWrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dning on 5/24/2017.
 */

public class TestStateDataObject {
    // 60sec timeout
    final static long TRY_CONNECTION_TIMEOUT_PERIOD_MILLISECOND = 60000L;

    private com.epocal.common.androidutil.RxUtil<TestEventInfo> mRxUtil;

    public com.epocal.common.androidutil.RxUtil<TestEventInfo> getRxUtil() {
        return mRxUtil;
    }

    public void setRxUtil(com.epocal.common.androidutil.RxUtil<TestEventInfo> mRxUtil) {
        this.mRxUtil = mRxUtil;
    }

    private TestStateActionEnum mTestStateAction;

    public void setTestStateAction(TestStateActionEnum testStateAction) {
        mTestStateAction = testStateAction;
        if (mTestStateAction == TestStateActionEnum.Connecting) {
            // Start connection try timer
            // When timer expires, mTestCommunication.disconnet()
            if (mTryConnectionTimeoutTimer == null) {
                mTryConnectionTimeoutTimer = new Timer();
                TimerTask timerTaskObj = new TimerTask() {
                    public void run() {
                        //perform your action here
                        mTestCommunication.disconnect();
                        TestEventInfo eventInfo = new TestEventInfo();
                        eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                        eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                        postEvent(eventInfo);
                        mTryConnectionTimeoutTimer.cancel();
                        mTryConnectionTimeoutTimer.purge();
                    }
                };
                // 60sec Timeout on trying connection.
                mTryConnectionTimeoutTimer.schedule(timerTaskObj, TRY_CONNECTION_TIMEOUT_PERIOD_MILLISECOND, TRY_CONNECTION_TIMEOUT_PERIOD_MILLISECOND);
            }
        } else if (mTestStateAction == TestStateActionEnum.Connected) {
            // Stop connection try timer if it has been started.
            if (mTryConnectionTimeoutTimer != null) {
                mTryConnectionTimeoutTimer.cancel();
                mTryConnectionTimeoutTimer.purge();
            }
        }
    }

    //*****added by rzhuang for legacy
    public boolean mRedistributed;

    //*********************************

    private Timer mTryConnectionTimeoutTimer;

    public TestStateActionEnum getTestStateAction() {
        return mTestStateAction;
    }

    private IMsgPayload mIMsgPayload;

    public IMsgPayload getIMsgPayload() {
        return mIMsgPayload;
    }

    public void setIMsgPayload(IMsgPayload mIMsgPayload) {

        mTestDataProcessor.logTestPacket(mIMsgPayload, "[Received]", "");
        this.mIMsgPayload = mIMsgPayload;
    }

    private String mAddress; // this is the reader bluetooth address

    public String getAddress() {
        return mAddress;
    }

    private int mTestID = 1;

    public int getTestID() {
        return mTestID;
    }

    public void setTestID(int mTestID) {
        this.mTestID = mTestID;
    }

    private int mDryCardCheckID = 1;

    public int getDryCardCheckID() {
        return mDryCardCheckID;
    }

    public void setDryCardCheckID(int mDryCardCheckID) {
        this.mDryCardCheckID = mDryCardCheckID;
    }

    private CardInformation mCardInformation;

    public CardInformation getCardInformation() {
        return mCardInformation;
    }

    public void setCardInformation(CardInformation mCardInformation) {
        this.mCardInformation = mCardInformation;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    private StateMachine mStateMachine;

    public StateMachine getStateMachine() {
        return mStateMachine;
    }

    public void setStateMachine(StateMachine mStateMachine) {
        this.mStateMachine = mStateMachine;
    }

    private TestCommunication mTestCommunication;

    public TestCommunication getTestCommunication() {
        return mTestCommunication;
    }

    public void setTestCommunication(TestCommunication mTestCommunication) {
        this.mTestCommunication = mTestCommunication;
    }

    private TestDataProcessor mTestDataProcessor;

    public TestDataProcessor getTestDataProcessor() {
        return mTestDataProcessor;
    }

    public void setTestDataProcessor(TestDataProcessor mTestDataProcessor) {
        this.mTestDataProcessor = mTestDataProcessor;
    }

    private long mTimeout = 30000;
    private Timer mTimer = new Timer();

    public TestStateDataObject() {
        mRxUtil = new RxUtil().create(null);
        mTestDataProcessor = new TestDataProcessor();

    }

    public void initTestData(ReaderDevice readerDevice, TestMode testMode) {
        initializeTestRecord(readerDevice, testMode);
        // maybe here add the reader and the Host to testrecord as well
    }

    public void startTimer() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                this.cancel();
                timeout();
            }
        }, mTimeout, mTimeout);

    }

    private void timeout() {
        postEventToStateMachine(this, TestStateActionEnum.Timeout);
    }

    public void StopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    public void unSubscribeRx() {
        mRxUtil.unsubscribe();
    }

    /***
     * post event from state to test controller and then from test controller to UI layer
     * @param testEventInfo
     */
    public void postEvent(TestEventInfo testEventInfo) {
        if (mRxUtil != null) {
            mRxUtil.onNext(testEventInfo);
        }
    }

    public void postEventToStateMachine(final TestStateDataObject testStateDataObject, final TestStateActionEnum testStateActionEnum) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                testStateDataObject.setTestStateAction(testStateActionEnum);
                mStateMachine.postEvent(testStateDataObject);
            }
        });
    }

    //execute when card inserted
    public void prepareForNewTest() {
        closePreviousTest();
        mTestDataProcessor.prepareForNewTest();
        //mTestDataProcessor.MockCalculation(this);
    }

    //execute when card accepted and before calibration
    public void initializeForNewTest() {
        mTestDataProcessor.initializeForNewTest();
    }

    //called from testStateDataObject creates a new testRecord
    public void initializeTestRecord(ReaderDevice readerDevice, TestMode testMode) {
        mTestDataProcessor.initializeTestRecord(readerDevice, testMode);
    }

    //execute only once when calibration started (cal-fluid was broken), called from uiTestdriver
    public void persistTestData() {
        mTestDataProcessor.persistCreateData();
    }

    public boolean validateEQCTest() {
        if (!mTestDataProcessor.checkEQCTestValid()) {
            //if redo EQC, stateDataObject.TestDataProcessor.LastSelfTestTime need to be updated, Testrecord.LastEQCDateTime as well.
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.REDOEQC);
            postEvent(eventInfo);
            return false;
        }
        return true;
    }

    public boolean validateTestCardType() {
        byte[] fakeBarcode = new byte[3];
        fakeBarcode[0] = 42;
        fakeBarcode[1] = 57;
        fakeBarcode[2] = 84;

        BarcodeVerificationCode retCode = mTestDataProcessor.validateBarcodeInformation(fakeBarcode, fakeBarcode.length);
        if (retCode == BarcodeVerificationCode.Invalid) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.INVALIDCARDBARCODE);
            postEvent(eventInfo);
            return false;//Barcode is invalid
        } else if (retCode == BarcodeVerificationCode.ExpiryInTheFuture) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.INVALIDCARDMANUFACTUREDATE);
            postEvent(eventInfo);
            return false;
        } else if (retCode == BarcodeVerificationCode.Expired) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDEXPIRED);
            postEvent(eventInfo);
            return false;
        }
        if (!mTestDataProcessor.loadTestConfiguration()) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.CARDTYPENOTSUPPORTED);
            postEvent(eventInfo);
            return false;//Test card type is Not supported by test configuration
        }
        return true;
    }

    public TestProcessStateReturnCode validateAvailableAnalytes() {
        TestProcessStateReturnCode retCode = TestProcessStateReturnCode.Continue;
        List<Analyte> availableAnalytes = new ArrayList<Analyte>();
        RefWrappers<List<Analyte>> availableByRef = new RefWrappers<List<Analyte>>(availableAnalytes);
        ValidateAnalyteTestReturnCode retAnalyte = mTestDataProcessor.validateAvailableAnalytes(availableByRef);
        if (retAnalyte == ValidateAnalyteTestReturnCode.AnalyteNoCompatibleForReader) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.READERREQUIRESUPGRADE);
            postEvent(eventInfo);
            retCode = TestProcessStateReturnCode.RemoveCard;//Reader need tobe upgrade to run some particular analyte test
        } else if (retAnalyte == ValidateAnalyteTestReturnCode.NoAnalytesAvailable) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.NOTESTENABLEFORCARDTYPE);
            postEvent(eventInfo);
            retCode = TestProcessStateReturnCode.RemoveCard;//Reader need tobe upgrade to run some particular analyte test
        }

        if (mTestDataProcessor.getTestRecord().getTestMode() == TestMode.BloodTest) {
            List<Analyte> unavailableAnalytes = new ArrayList<Analyte>();
            RefWrappers<List<Analyte>> feedbackByRef = new RefWrappers<List<Analyte>>(unavailableAnalytes);
            QAScheduleLookoutReturnCode retQACode = mTestDataProcessor.checkQAScheduleLookout(availableByRef.getRef(), feedbackByRef);
            if (retQACode != QAScheduleLookoutReturnCode.QAPassed) {
                switch (retQACode) {
                    case QALookout: {
                        TestEventInfo eventInfo = new TestEventInfo();
                        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                        eventInfo.setTestStatusType(TestStatusType.QCLOCKOUT);
                        postEvent(eventInfo);
                        retCode = TestProcessStateReturnCode.StopTest;
                        break;
                    }
                    case ExpiringSoon: {
                        TestEventInfo eventInfo = new TestEventInfo();
                        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                        eventInfo.setTestStatusType(TestStatusType.QCEXPIREDWARNING);
                        postEvent(eventInfo);
                        retCode = TestProcessStateReturnCode.Continue;
                        break;
                    }
                    case ExpiredWarning: {
                        TestEventInfo eventInfo = new TestEventInfo();
                        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                        eventInfo.setTestStatusType(TestStatusType.QCEXPIREDWARNING);
                        postEvent(eventInfo);
                        retCode = TestProcessStateReturnCode.Continue;
                        break;
                    }
                    case RemoveCard: {
                        TestEventInfo eventInfo = new TestEventInfo();
                        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                        eventInfo.setTestStatusType(TestStatusType.QCEXPIREDWARNINGCARDINREADER);
                        postEvent(eventInfo);
                        retCode = TestProcessStateReturnCode.RemoveCard;
                        break;
                    }
                    case GraceWarning: {
                        TestEventInfo eventInfo = new TestEventInfo();
                        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
                        eventInfo.setTestStatusType(TestStatusType.QCGRACEPERIODWARNING);
                        postEvent(eventInfo);
                        retCode = TestProcessStateReturnCode.Continue;
                        break;
                    }
                }
            }
            if (unavailableAnalytes.size() > 0) {
                for (Analyte analyte : unavailableAnalytes) {
                    for (Analyte existigAnalyte : availableByRef.getRef()) {
                        if (analyte.getAnalyteName() == existigAnalyte.getAnalyteName()) {
                            availableByRef.getRef().remove(existigAnalyte);
                        }
                    }
                }
            }
        }

        TestEventInfo eventInfo = new TestEventInfo();
        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
        eventInfo.setTestStatusType(TestStatusType.CARDINSERTED);
        eventInfo.setTestEventData(availableByRef.getRef());
        postEvent(eventInfo);
        return retCode;
    }

    public void calculateResults() {
        TestEventInfo eventInfo = new TestEventInfo();
        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
        eventInfo.setTestStatusType(TestStatusType.TESTCALCULATING);
        postEvent(eventInfo);
        mTestDataProcessor.calculateResults(this);
    }

    public boolean sendMessage(IMsgPayload mIMsgPayload) {
        mTestDataProcessor.logTestPacket(mIMsgPayload, "[Sent]", "");
        return mTestCommunication.sendMessage(mIMsgPayload);
    }

    public void closePreviousTest() {
        getTestDataProcessor().closePreviousTest();
    }

    public void disposeObject() {
    }
}
