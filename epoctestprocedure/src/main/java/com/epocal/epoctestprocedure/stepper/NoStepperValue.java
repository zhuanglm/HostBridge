package com.epocal.epoctestprocedure.stepper;

import com.epocal.epoctestprocedure.stepper.types.StepperValueType;

import static com.epocal.epoctestprocedure.stepper.types.StepperValueType.VT_STRING;

/**
 * null object pattern instead of dealing with nulls
 */
public class NoStepperValue implements IStepperValue{
    @Override
    public StepperValueType getType() {
        return VT_STRING;
    }

    @Override
    public Object getValue() {
        return "";
    }
}
