package com.epocal.epoctest.di.module;

import com.epocal.epoctest.TestController;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dning on 6/1/2017.
 */

@Module
public class TestControllerModule
{
    @Provides public TestController provideTestController() { return new TestController();   }
}
