package com.epocal.common.globaldi;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.epocal.common.epocobjects.IApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dning on 5/26/2017.
 * provides Application and Context singletons for entire Host4 application
 * also provides SharedPreferences, may be removed when cleaning up Host4
 */
@Module
public class GlobalAppModule {
    @Provides
    @Singleton
    IApp provideApplication(){ return GloabalObject.getApplication();}

    @Provides
    @Singleton
    Context provideContext(){ return GloabalObject.getApplication().getApplicationContext();}

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
            return GloabalObject.getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
    }
}
