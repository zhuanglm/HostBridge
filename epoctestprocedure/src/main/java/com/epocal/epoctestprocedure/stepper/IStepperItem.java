package com.epocal.epoctestprocedure.stepper;

import com.epocal.epoctest.uidriver.TestUIDriver;
import com.epocal.epoctestprocedure.stepper.types.StepperType;

public interface IStepperItem {
    Boolean isValidated(TestUIDriver testUIDriver);
    Boolean validate(Object data);
    Boolean validateAndSave(TestUIDriver testUIDriver, Object data);
    StepperType getDataType();
    IStepperValue getValue(TestUIDriver testUIDriver);
    String getSubtitleValue(TestUIDriver testUIDriver);
}
