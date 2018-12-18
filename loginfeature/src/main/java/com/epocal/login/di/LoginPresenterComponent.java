package com.epocal.login.di;

import com.epocal.common.globaldi.GlobalAppModule;
import com.epocal.login.LoginPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by bmate on 8/2/2017.
 */
@Singleton
@Component(modules = GlobalAppModule.class)
public interface LoginPresenterComponent {
    void inject(LoginPresenter loginPresenter);
}
