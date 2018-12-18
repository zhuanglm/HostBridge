package com.epocal.epoctest.uidriver;

import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.datamanager.WorkflowRepository;
import com.epocal.epoctest.LegacyTestController;
import com.epocal.epoctest.TestController;

/**
 * Created by bmate on 7/12/2017.
 */

public class QATestUIDriver extends TestUIDriver {
    public QATestUIDriver(ReaderDevice readerDevice, TestController testController) {
        super(readerDevice,testController);
    }

    public QATestUIDriver(ReaderDevice readerDevice, LegacyTestController testController) {
        super(readerDevice,testController);
    }

    @Override
    void derivedInit() {
        loadQATestWorkflow();
    }

    private void loadQATestWorkflow() {
        //TODO refactor
        WorkflowRepository repo = new WorkflowRepository();
        setTestFlow(repo.getQAWorkflow());
        setWorkflowFields(repo.getWorkflowFields(getTestFlow().getName()));
    }
}
