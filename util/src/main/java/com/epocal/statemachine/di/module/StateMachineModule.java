package com.epocal.statemachine.di.module;

import com.epocal.statemachine.StateMachineFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dning on 5/24/2017.
 */

@Module
public class StateMachineModule {
    @Provides
    public StateMachineFactory provideStateMachineFactory() {
        return new StateMachineFactory();
    }
}
