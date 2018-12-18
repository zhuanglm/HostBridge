package com.epocal.epoctest.di.component;

import com.epocal.epoctest.di.module.TestProcessModule;
import com.epocal.epoctest.testprocess.PerformTestCalculation;
import com.epocal.epoctest.testprocess.BGETestCalculation;

import dagger.Component;

/**
 * Created by dning on 10/17/2017.
 */

@Component(modules = {TestProcessModule.class})
public interface TestProcessComponent {
    void inject(PerformTestCalculation injected);

    BGETestCalculation provideBGETestCalculation();
}
