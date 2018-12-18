package com.epocal.epoctestprocedure.stepper;

import com.epocal.epoctestprocedure.stepper.types.StepperValueType;

public class StringStepperValue implements IStepperValue {
    String value = "";

    public StringStepperValue(String value) {
        this.value = value;
    }

    @Override
    public StepperValueType getType() {
        return StepperValueType.VT_STRING;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
