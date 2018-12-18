package com.epocal.epoctestprocedure;

import com.epocal.epoctest.uidriver.TestUIDriver;
import com.epocal.epoctest.uimessage.TestMessageContext;

/**
 * The interface for the view, i.e. PatientTestActivity
 *
 * Created by Zeeshan A Zakaria on 12/5/2017.
 */

interface IPatientTestView {
    void handleTestEvent(TestUIDriver patientTestUIDriver);
    void enableDocumentResults();
    void setTestPanels();
    void handleMessages(TestMessageContext tmc);
    void updateTestRecord();
    void displayTestResultFragment();
    void displayTestMessageAndInstructionFragment();
    void showLogcatMessage(String message);
    void updateActivityTitle(String message);
    void refreshDataEntries();
    void showProgressDialog();
    void hideProgressDialog();
    void mockTestPanels();

    boolean canShowResults();
}
