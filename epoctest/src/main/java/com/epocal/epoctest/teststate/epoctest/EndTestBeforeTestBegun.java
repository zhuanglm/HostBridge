package com.epocal.epoctest.teststate.epoctest;

import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 6/21/2017.
 */

public class EndTestBeforeTestBegun extends TestState {
    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        stateDataObject.postEventToStateMachine(stateDataObject, stateDataObject.getTestStateAction());
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        return TestStateEventEnum.TerminateTest;
    }
}
