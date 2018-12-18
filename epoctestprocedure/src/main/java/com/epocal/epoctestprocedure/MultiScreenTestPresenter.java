package com.epocal.epoctestprocedure;

import android.content.Context;

import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.eventmessages.ExtraInfo;
import com.epocal.common.types.UIScreens;
import com.epocal.epoctest.TestContainerEvents;
import com.epocal.epoctest.TestManager;
import com.epocal.epoctest.uidriver.TestUIDriver;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MultiScreenTestPresenter implements IMultiScreenTestPresenter {
    MultiScreenTestActivity mView;
    TestListAdapter mAdapter;
    List<TestUIDriver> mTests;


    public MultiScreenTestPresenter(MultiScreenTestActivity view) {
        mView = view;
        mAdapter = new TestListAdapter(this);
        mTests = new ArrayList<>();
    }

    public TestListAdapter getListViewAdapter() {
        return mAdapter;
    }

    @Override
    public Context getViewContext() {
        return mView;
    }

    @Override
    public int getItemCount() {
        return mTests.size();
    }

    public TestUIDriver getItemByPosition(int position) {
        try {
            return mTests.get(position);
        } catch (Exception e) {
        }
        return null;
    }

    private void addTest(TestUIDriver testDriver) {
        mTests.add(testDriver);
        testDriver.setVisible(true);
        testDriver.getTestDriverEmmitter().subscribeOn(new Consumer<TestUIDriver>() {
            @Override
            public void accept(TestUIDriver testUIDriver) throws Exception {
                mAdapter.notifyDataSetChanged();
            }
        }, AndroidSchedulers.mainThread());
    }

    private void removeTest(TestUIDriver testUIDriver) {
        if (testUIDriver != null && testUIDriver.getTestDriverEmmitter() != null)
            testUIDriver.getTestDriverEmmitter().unsubscribe();
        mTests.remove(testUIDriver);
    }

    public void loadTests() {
        mTests.clear();
        TestManager.getInstance().getTestContainerEmitter().subscribeOn(new Consumer<TestContainerEvents>() {
            @Override
            public void accept(TestContainerEvents containerEvent) throws Exception {
                switch(containerEvent.getChangeType())
                {
                    case ADD:
                        addTest(containerEvent.getTestDriver());
                        break;
                    case REMOVE:
                        removeTest(containerEvent.getTestDriver());
                        break;
                    default:
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }
        }, AndroidSchedulers.mainThread());
        for(TestUIDriver testDriver : TestManager.getInstance().getQATests()) {
            addTest(testDriver);
        }
    }

    public void stopAllTests() {
        TestManager.getInstance().getTestContainerEmitter().unsubscribe();
        try {
            for (TestUIDriver testDriver : mTests) {
                testDriver.getTestDriverEmmitter().unsubscribe();
                TestManager.getInstance().stopTest(testDriver.getReaderDevice().getDeviceAddress());
            }
        } catch (Exception e) {

        }
        mTests.clear();
    }

    @Override
    public void activateTest(TestUIDriver test) {
        EpocNavigationObject navigationObject = new EpocNavigationObject();
        navigationObject.setContext(mView);
        navigationObject.setTargetscreen(UIScreens.TestScreen);
        navigationObject.setExtraInfo1(new ExtraInfo("Reader_BTA", test.getReaderDevice().getDeviceAddress()));
        navigationObject.setExtraInfo2(new ExtraInfo("Reader_Alias", test.getReaderDevice().getDeviceAlias()));
        EventBus.getDefault().post(navigationObject);
    }
}
