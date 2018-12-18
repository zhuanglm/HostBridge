package com.epocal.epoctestprocedure;

import com.epocal.epoctest.uidriver.TestUIDriver;

/**
 * The interface of this MVP
 *
 * Created by Zeeshan A Zakaria on 12/5/2017.
 */

public interface IPatientTestPresenter {
    void processPatientTestUIDriver(TestUIDriver patientTestUIDriver);
    void updateActivityTitle(String text);

    void mandatoryStepsCompleted();
}
