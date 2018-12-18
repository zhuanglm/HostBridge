package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by rzhuang on July 18 2018.
 */

public class ActionAfterInitialConnect extends LegacyTestState {
    public ActionAfterInitialConnect() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);

        stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.None);
    }


    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        //set reader address initialization was done in TestController
        //stateDataObject.initTestData();

        return TestStateEventEnum.GetReaderId;
    }

}
