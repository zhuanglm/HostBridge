package com.epocal.epoctestprocedure.stepper;

import com.epocal.epoctest.uidriver.TestUIDriver;
import com.epocal.epoctestprocedure.stepper.transformers.IStepperValueTransformer;
import com.epocal.epoctestprocedure.stepper.validators.IStepperValueValidator;

public interface IStepperItemBehavior {
    void init();
    String subtitle(TestUIDriver testUIDriver);
    IStepperValue load(TestUIDriver testUIDriver);
    void save(TestUIDriver testUIDriver, Object data);
    IStepperValueTransformer getTransformer();
    IStepperValueValidator getValidator();
}
