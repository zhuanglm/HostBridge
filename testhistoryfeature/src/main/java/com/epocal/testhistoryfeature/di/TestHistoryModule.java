package com.epocal.testhistoryfeature.di;

import com.epocal.testhistoryfeature.THContract;
import com.epocal.testhistoryfeature.THModel;
import com.epocal.testhistoryfeature.THPresenter;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class TestHistoryModule {
    private THContract.View mView;
    private Realm mRealm;

    public TestHistoryModule(THContract.View view, Realm realm) {
        this.mView = view;
        this.mRealm = realm;
    }

    @ActivityScope
    @Provides
    THContract.Model provideModel() {
        return new THModel(mRealm);
    }

    @ActivityScope
    @Provides
    THContract.View provideView() {
        return mView;
    }

    @ActivityScope
    @Provides
    THContract.Presenter providePresenter(THContract.Model model, THContract.View view) {
        return new THPresenter(model, view);
    }
}
