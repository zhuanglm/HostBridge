package com.epocal.epoctest.teststate.epoctest;

import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 6/21/2017.
 */

public class TerminateTest extends TestState {
    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        stateDataObject.postEventToStateMachine(stateDataObject, stateDataObject.getTestStateAction());
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.ConnectionLost) {
            //stateDataObject.TestDataProcessor.ClosePacketFile("Connection lost");
            //stateDataObject.TestDataProcessor.CloseTestDataFile("Connection lost", stateDataObject.TestParameter);
        } else {
            //stateDataObject.TestDataProcessor.ClosePacketFile("Disconnection");
            //stateDataObject.TestDataProcessor.CloseTestDataFile("Disconnection", stateDataObject.TestParameter);
        }
        //stateDataObject.TestDataProcessor.SaveTestRecordWhenError();
        stateDataObject.getTestCommunication().disconnect();
        stateDataObject.closePreviousTest();
        //return TestStateEventEnum.Disconnected;
        return super.onEventPreHandle(stateDataObject);
    }
}
