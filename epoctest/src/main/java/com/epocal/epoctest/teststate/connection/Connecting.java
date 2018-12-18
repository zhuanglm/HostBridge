package com.epocal.epoctest.teststate.connection;

import com.epocal.epoctest.teststate.TestState;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.statemachine.IEventEnum;
/**
 * Created by dning on 5/24/2017.
 */

public class Connecting extends TestState
{
    public Connecting(){}

    @Override
    public void onEntry(TestStateDataObject stateDataObject)
    {
        super.onEntry(stateDataObject);
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject)
    {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.Connected)
        {
            return TestStateEventEnum.Connected;
        }
        else if (stateDataObject.getTestStateAction() == TestStateActionEnum.ConnectionFailed)
        {
            return TestStateEventEnum.Disconnected;
        }
        else if (stateDataObject.getTestStateAction() == TestStateActionEnum.CancelConnection)
        {
            stateDataObject.getTestCommunication().disconnect();
            return TestStateEventEnum.Disconnected;
        }
        return super.onEventPreHandle(stateDataObject);
    }
}
