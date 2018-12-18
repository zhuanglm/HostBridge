package com.epocal.login.di;


import com.epocal.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * The Login DI
 *
 * Created by Zeeshan A Zakaria on 3/21/2017.
 */
@Singleton
@Component(modules = LoginScreenModule.class)
public interface LoginScreenComponent {
    void inject(LoginActivity activity);
}