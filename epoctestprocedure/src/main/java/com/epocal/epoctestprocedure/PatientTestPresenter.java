package com.epocal.epoctestprocedure;

import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.uidriver.TestUIDriver;
import com.epocal.epoctest.uimessage.TestMessageContext;

/**
 * The presenter for the view
 * <p>
 * Created by Zeeshan A Zakaria on 12/5/2017.
 */

class PatientTestPresenter implements IPatientTestPresenter {

    private IPatientTestView mView;

    PatientTestPresenter(IPatientTestView view) {
        mView = view;
    }

    @Override
    public void updateActivityTitle(String text) {
        mView.updateActivityTitle(text);
    }

    @Override
    public void mandatoryStepsCompleted() {
        if (mView.canShowResults()) {
            mView.hideProgressDialog();
            mView.enableDocumentResults();
            mView.displayTestResultFragment();
        }
    }

    @Override
    public void processPatientTestUIDriver(TestUIDriver patientTestUIDriver) {
        TestMessageContext tmc = patientTestUIDriver.getTestMessageContext();
        mView.handleMessages(tmc);
        TestStatusType testStatusType = patientTestUIDriver.getStatusEventType();
        TestStatusErrorType testStatusErrorType = patientTestUIDriver.getStatusErrorType();

        if (testStatusErrorType != null) {
            mView.displayTestMessageAndInstructionFragment();
            switch (testStatusErrorType) {
                case READERNOCOMPATIBLE: {
                    break;
                }
                case UNKNOWN:
                    break;
                case COMMUNICATIONFAILED: {
                    break;
                }
                case HCMERROR:
                    break;
                case READERERROR:
                    break;
                case READERBATTERYLOW:
                    break;
                case EQCFAILED:
                    break;
                case LOWTEMPERATURELIMIT:
                    break;
                case HIGHTEMPERATURELIMIT:
                    break;
                case HIGHPRESSURELIMIT:
                    break;
                case LOWPRESSURELIMIT:
                    break;
                case AMBIENTPRESSURESENSORFAILEDQC:
                    break;
                case READERNEEDMAINTENANCE:
                    break;
                case REDOEQC:
                    break;
                case FLUIDDETECTEDINCARD:
                    break;
                case UNABLEREADCARDBARCODE:
                    break;
                case INVALIDCARDBARCODE:
                    break;
                case INVALIDCARDMANUFACTUREDATE:
                    break;
                case CARDEXPIRED:
                    break;
                case CARDTYPENOTSUPPORTED:
                    break;
                case READERREQUIRESUPGRADE:
                    break;
                case NOTESTENABLEFORCARDTYPE:
                    break;
                case CARDINSERTEDWHENMOTORMOVING:
                    break;
                case CARDINSERTEDNOTPROPERLY:
                    break;
                case EXCEEDMAXTEST:
                    break;
                case OLDCARDINREADER:
                    break;
                case ERROROCCURREDTESTSTOP:
                    break;
                case HEMATOCRITLOWRESISTANCE:
                    break;
                case EARLYINJECTION:
                    break;
                case HUMIDITYCHECKFAILED:
                    break;
                case REALTIMEQCFAILED:
                    break;
                case FLUIDICSCHECKFAILED:
                    break;
                case SAMPLEINJECTEDTOOFAST:
                    break;
                case SAMPLEINJECTEDTOOSLOWLY:
                    break;
                case SAMPLINGTIMEOUT:
                    break;
                case CALIBRATIONFLUIDICSNOTDETECTED:
                    break;
                case THERMALQCFAILED:
                    break;
                case CARDREMOVEDDURINGTEST:
                    break;
                case SAMPLEDELIVERY:
                    break;
                case ENDTESTAFTERTESTBEGUN:
                    break;
                case UNABLETOCONNECT:
                    break;
            }
        }

        if (testStatusType != null) {
            switch (testStatusType) {
                case UNKNOWN:
                    break;
                case NEWTESTSTARTING:
					// Reset the view to initial state - is done by CARDINSERTED_NEWTEST_DONTKEEPDATA, CARDINSERTED_NEWTEST_KEEPDATA events
                    break;
                case CONNECTING:
                    break;
                case CONFIGURATION:
                    // DEV
//                    mView.refreshDataEntries();
//                    mView.mockTestPanels();
                    //
                    break;
                case READYTOTEST:
                    break;
                case FLUIDICSCALIBRATION:
                    break;
                case SAMPLEINTRODUCTION:
                    break;
                case SAMPLEPROCESSING:
                    break;
                case TESTCALCULATING:
//                    mView.hideProgressDialog();
                    break;
                case TESTRECALCULATED:
                case TESTCALCULATED: {
                    mView.updateTestRecord();
                    mView.hideProgressDialog();
                    if (mView.canShowResults()) {
                        mView.enableDocumentResults();
                        mView.displayTestResultFragment();
                    } else {
                        mView.showLogcatMessage("Calculation not done");
                    }
                    break;
                }
                case MISSINGDATAREQUIRED:
                    break;
                case NOTESTRUNNING:
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
                    // DEV
//                    mView.refreshDataEntries();
//                    mView.mockTestPanels();
                    //
                    break;
                case DISCONNECTED:
                    break;
                case DEVICEINFO:
                    break;
                case CARDINSERTED:
                    break;
            }
        }



    }
}
