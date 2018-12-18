package com.epocal.epoctest.teststate.connection;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 5/24/2017.
 */

public class Disconnected extends TestState
{
    public Disconnected(){}

    @Override
    public void onEntry(TestStateDataObject stateDataObject)
    {
        super.onEntry(stateDataObject);
        TestEventInfo eventInfo = new TestEventInfo();
        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
        eventInfo.setTestStatusType(TestStatusType.DISCONNECTED);
        stateDataObject.postEvent(eventInfo);
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject)
    {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.Connecting)
        {
            stateDataObject.getTestCommunication().connecting();
            return TestStateEventEnum.Connecting;
        }
        else if (stateDataObject.getTestStateAction() == TestStateActionEnum.CancelConnection
                || stateDataObject.getTestStateAction() == TestStateActionEnum.Connected)
        {
            stateDataObject.getTestCommunication().disconnect();
        }
        return super.onEventPreHandle(stateDataObject);
    }
}