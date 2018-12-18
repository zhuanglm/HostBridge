package com.epocal.epoctest.di.module;

import com.epocal.epoctest.testprocess.BGETestCalculation;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dning on 10/17/2017.
 */
@Module
public class TestProcessModule {
    @Provides
    public BGETestCalculation provideBGETestCalculation() { return new BGETestCalculation();   }
}
