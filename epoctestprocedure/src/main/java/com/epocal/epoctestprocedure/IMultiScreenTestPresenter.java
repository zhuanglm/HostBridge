package com.epocal.epoctestprocedure;

import android.content.Context;

import com.epocal.epoctest.uidriver.TestUIDriver;

public interface IMultiScreenTestPresenter {

    TestListAdapter getListViewAdapter();

    Context getViewContext();

    int getItemCount();
    TestUIDriver getItemByPosition(int position);

    void loadTests();
    void stopAllTests();
    void activateTest(TestUIDriver test);
}
