package com.epocal.testhistoryfeature.di;

import com.epocal.testhistoryfeature.ui.TestHistoryDetailsActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {TestHistoryDetailsModule.class})
public interface TestHistoryDetailsComponent {
    void inject(TestHistoryDetailsActivity detailsActivity);
}
