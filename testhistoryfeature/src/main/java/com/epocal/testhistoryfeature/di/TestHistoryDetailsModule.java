package com.epocal.testhistoryfeature.di;

import com.epocal.testhistoryfeature.THDetailsContract;
import com.epocal.testhistoryfeature.THDetailsModel;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class TestHistoryDetailsModule {
    private Realm mRealm;

    public TestHistoryDetailsModule(Realm realm) {
        this.mRealm = realm;
    }

    @ActivityScope
    @Provides
    THDetailsContract.Model provideDetailsModel() {
        return new THDetailsModel(mRealm);
    }
}
