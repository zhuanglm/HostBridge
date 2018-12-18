package com.epocal.epoctest.di.component;

import com.epocal.common.globaldi.GlobalAppModule;
import com.epocal.epoctest.TestManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by bmate on 8/10/2017.
 */
@Singleton
@Component(modules = GlobalAppModule.class)
public interface TestManagerComponent {
    void inject(TestManager testManager);
}
