package com.epocal.testhistoryfeature.di;

import com.epocal.testhistoryfeature.ui.TestHistoryMainActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {TestHistoryModule.class})
public interface TestHistoryComponent {
    void inject(TestHistoryMainActivity mainActivity);
}
