package com.epocal.epoctestprocedure.stepper.types;

/**
 * The type of data to be shown by the stepper. Provides direction in the UI on how
 * the workflow item should be represented on screen.
 *
 * Note: generally this enum determines how data is handled when it is changed by the UI
 *       for example, VS_RADIO will return integer indexes representing the value that was
 *       changed, while VS_COMBO will return strings. Please keep this in mind when writing
 *       IStepperValueTransformers.
 */
public enum StepperType {
    VS_STRING, VS_BARCODE, VS_INTEGER, VS_RADIO, VS_COMBO, VS_DATE, VS_TIME
}
