package com.epocal.epoctestprocedure.stepper;

import com.epocal.epoctestprocedure.stepper.types.StepperValueType;

public interface IStepperValue {
    StepperValueType getType();
    Object getValue();
}
