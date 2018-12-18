package com.epocal.host4.aboutscreen.di;

import com.epocal.host4.aboutscreen.AboutScreenContract;
import com.epocal.host4.di.scopes.CustomScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bmate on 3/21/2017.
 */
@Module
public class AboutScreenModule {
    // provides the aboutview into the presenter
    private  final AboutScreenContract.View aboutView;
    public AboutScreenModule(AboutScreenContract.View mView)
    {
        this.aboutView = mView;
    }
    @Provides
    @CustomScope
    AboutScreenContract.View providesAboutScreenView()
    {
        return aboutView;
    }

}
