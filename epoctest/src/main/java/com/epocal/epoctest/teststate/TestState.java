package com.epocal.epoctest.teststate;

import android.util.Log;

import com.epocal.epoclog.LogServer;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.statemachine.IEventEnum;
import com.epocal.statemachine.State;

/**
 * Created by dning on 5/24/2017.
 */

public class TestState extends State<TestStateDataObject> {
    public TestState() {
        super();
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        Log.d("State machine-entry:", this.getStateName());
    }

    @Override
    public void onExit(TestStateDataObject stateDataObject) {
        super.onExit(stateDataObject);
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.CancelConnection) {
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        return super.onEventPreHandle(stateDataObject);
    }
}
