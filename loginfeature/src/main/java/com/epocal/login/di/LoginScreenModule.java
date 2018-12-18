package com.epocal.login.di;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.epocal.common.androidutil.RxUtil;
import com.epocal.common.globaldi.GlobalAppModule;
import com.epocal.hardware.EMDKScanner;
import com.epocal.login.LoginPresenter;
import com.epocal.login.model.LoginLogic;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = GlobalAppModule.class)
public class LoginScreenModule {
    private final LoginScreenContract.View mView; // provides the loginview into the presenter

    public LoginScreenModule(LoginScreenContract.View view) {
        mView = view;
    }

    @Provides
    @Singleton
    LoginScreenContract.Presenter provideLoginPresenter(LoginScreenContract.Logic loginLogic, EMDKScanner emdkScanner) {
        return new LoginPresenter(mView, loginLogic, emdkScanner);
    }

    @Provides
    @Singleton
    LoginScreenContract.View provideLoginScreenView() {
        return mView;
    }

    @Provides
    @Singleton
    LoginScreenContract.Logic provideLoginLogic(){
        return new LoginLogic();
    }

    @Provides
    @Singleton
    EMDKScanner provideEMDKScanner(Context context) {
        return new EMDKScanner(context);
    }

}