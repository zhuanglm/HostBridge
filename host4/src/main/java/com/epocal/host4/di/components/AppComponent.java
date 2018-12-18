package com.epocal.host4.di.components;

import com.epocal.host4.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;


/**
 *
 * Created by bmate on 3/20/2017.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    // downstream components need these exposed with the return type
    // method name does not really matter

    // removed retrofit
}
