package com.epocal.epoctest.teststate.connection;

import com.epocal.epoctest.teststate.TestState;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 5/24/2017.
 */

public class Connected extends TestState
{
    public Connected(){}

    @Override
    public void onEntry(TestStateDataObject stateDataObject)
    {
        super.onEntry(stateDataObject);
        stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.NextStep);
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject)
    {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.ConnectionLost)
        {
            return TestStateEventEnum.Disconnected;
        }
        else if (stateDataObject.getTestStateAction() == TestStateActionEnum.CancelConnection)
        {
            stateDataObject.getTestCommunication().disconnect();
            return TestStateEventEnum.Disconnected;
        }
        else if (stateDataObject.getTestStateAction() == TestStateActionEnum.NextStep)
        {
            return TestStateEventEnum.TestStateBody;
        }
        return super.onEventPreHandle(stateDataObject);
    }
}