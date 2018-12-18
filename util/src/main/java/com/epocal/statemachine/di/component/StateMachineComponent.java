package com.epocal.statemachine.di.component;

import com.epocal.statemachine.StateMachineFactory;
import com.epocal.statemachine.di.module.StateMachineModule;

import dagger.Component;

/**
 * Created by dning on 5/24/2017.
 */

@Component(modules = {StateMachineModule.class})
public interface StateMachineComponent {
    StateMachineFactory provideStateMachineFactory();
}
