package com.epocal.host4.aboutscreen.di;

import com.epocal.host4.aboutscreen.AboutActivity;
import com.epocal.host4.di.components.AppComponent;
import com.epocal.host4.di.scopes.CustomScope;

import dagger.Component;

/**
 *
 * Created by bmate on 3/21/2017.
 */
@CustomScope
@Component(dependencies = AppComponent.class, modules = AboutScreenModule.class)
public interface AboutScreenComponent {
   void inject (AboutActivity activity);
}