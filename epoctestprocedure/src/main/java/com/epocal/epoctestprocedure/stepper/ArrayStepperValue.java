package com.epocal.epoctestprocedure.stepper;

import com.epocal.epoctestprocedure.stepper.types.StepperValueType;

public class ArrayStepperValue implements IStepperValue {
    int value = -1;

    public ArrayStepperValue(int value) {
        this.value = value;
    }

    @Override
    public StepperValueType getType() {
        return StepperValueType.VT_ARRAY;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
