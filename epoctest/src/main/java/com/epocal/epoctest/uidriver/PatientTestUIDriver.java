package com.epocal.epoctest.uidriver;

import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.datamanager.WorkflowRepository;
import com.epocal.epoctest.LegacyTestController;
import com.epocal.epoctest.TestController;

/**
 * The Patient Test UI Driver
 *
 * Created by bmate on 7/12/2017.
 */

public class PatientTestUIDriver<T extends TestController> extends TestUIDriver {
    public PatientTestUIDriver(ReaderDevice readerDevice, T testController) {
        super(readerDevice, testController);
    }

    @Override
    void derivedInit() {
        loadPatientTestWorkflow();
    }

    private void loadPatientTestWorkflow() {
      setWorkflowFields(new WorkflowRepository().getActiveWorkflowFields());
    }
}
