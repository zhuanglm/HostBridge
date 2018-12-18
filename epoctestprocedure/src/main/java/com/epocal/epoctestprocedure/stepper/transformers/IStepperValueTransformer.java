package com.epocal.epoctestprocedure.stepper.transformers;

/**
 * This interface determines how data is transformed from the UI into
 * the actual type in the test record
 */
public interface IStepperValueTransformer {
    Object transform(Object data);
}
