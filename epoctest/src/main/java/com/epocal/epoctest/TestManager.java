package com.epocal.epoctest;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.epocal.common.Consts;
import com.epocal.common.androidutil.RxUtil;
import com.epocal.common.epocexceptions.EpocTestDriverException;
import com.epocal.common.epocexceptions.TestManagementException;
import com.epocal.common.epocobjects.IApp;
import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.eventmessages.ExtraInfo;
import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.common.eventmessages.TestManagementCallback;
import com.epocal.common.eventmessages.TestManagementEventType;
import com.epocal.common.globaldi.GloabalObject;
import com.epocal.common.globaldi.GlobalAppModule;
import com.epocal.common.types.ReaderDiscoveryMode;
import com.epocal.common.types.TestType;
import com.epocal.common.types.UIChangeRequestReason;
import com.epocal.common.types.UIScreens;
import com.epocal.common.types.am.TestMode;
import com.epocal.datamanager.ReaderModel;
import com.epocal.epoctest.di.component.DaggerTestManagerComponent;
import com.epocal.epoctest.di.component.TestManagerComponent;
import com.epocal.epoctest.uidriver.PatientTestUIDriver;
import com.epocal.epoctest.uidriver.QATestUIDriver;
import com.epocal.epoctest.uidriver.TestUIDriver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by bmate on 7/12/2017.
 * Singleton class that will instantiate all objects for an epoctest
 * it manages parallel QA tests
 * it decides on navigation to UI test screens
 * it switches testcontext into test activity
 */
@SuppressWarnings("unchecked")
public class TestManager implements AutoCloseable {

    // factory
    @Inject
    Context mContext;
    @Inject
    IApp mApp;

    private SharedPreferences mPref;
    private static TestManager instance = null;
    private static final Object syncRoot = new Object();

    private TestManager() {
        // customize if needed
        mTestContainerEmitter = new RxUtil<TestContainerEvents>().create(null);
    }

    public static TestManager getInstance() {
        if (instance == null) {
            synchronized (syncRoot) {
                instance = new TestManager();
            }
        }
        return instance;
    }

    public void Initialize() {
        EventBus.getDefault().register(this);
        TestManagerComponent tmc = DaggerTestManagerComponent.builder()
                .globalAppModule(new GlobalAppModule())
                .build();
        tmc.inject(this);
        mPref = mContext.getSharedPreferences(Consts.DEVICE_STATE, Context.MODE_PRIVATE);
        setDeviceTestState(false); // Test is NOT in progress upon the app start-up.
        setDeviceTestCompletedState(false); // Upon app start-up, set TestCompleted state to false (NO test in progress)
    }

    // members
    private EpocNavigationObject mNavigationObject;
    private TestUIDriver mDisplayedTest;
    private ArrayList<TestUIDriver> mTestContainer;
    private volatile boolean mNavigationPostponed = false;

    private com.epocal.common.androidutil.RxUtil<TestContainerEvents> mTestContainerEmitter;

    public RxUtil<TestContainerEvents> getTestContainerEmitter() {
        return mTestContainerEmitter;
    }

    private ArrayList<TestUIDriver> getTestContainer() {
        synchronized (syncRoot) {
            if (mTestContainer == null) {
                mTestContainer = new ArrayList<>();

            }
            return mTestContainer;
        }
    }

    // public methods
    public TestUIDriver getTestDriver(String deviceAddress) {
        TestUIDriver retval = null;
        for (TestUIDriver tuid : getTestContainer()) {
            if (tuid.getReaderDevice().getDeviceAddress().equals(deviceAddress)) {
                retval = tuid;
                break;
            }

        }
        return retval;
    }


    /*
     * Starts a test on the selected reader
     */
    public void startSingleTest(Context context, TestMode testMode, TestType testType, ReaderDevice readerDevice) throws TestManagementException {
        setDeviceTestState(true);
        setupNavToScreen(readerDevice.getDeviceAddress(), readerDevice.getDeviceAlias(), context, UIScreens.TestScreen);
        startNewTest(readerDevice, testMode, testType);
    }

    /*
     * This call is from the home screen
     * starts a patient test or a QA test if there is one dedicated reader,
     * else navigates to reader discovery screen
     * catch the exception in UI
     */
    public void startTest(Context context, TestMode testMode, TestType testType, Boolean finishContext) throws TestManagementException {
        switch (testMode) {
            case BloodTest: {
                ReaderDevice dedicatedReaderDevice = getSingleDedicatedReaderDevice();
                setDeviceTestState(true);
                if (dedicatedReaderDevice == null) {
                    navigateToReaderDiscovery(context, testMode, null, false);
                } else {
                    setupNavToScreen(dedicatedReaderDevice.getDeviceAddress(), dedicatedReaderDevice.getDeviceAlias(), context, UIScreens.TestScreen);
                    startNewTest(dedicatedReaderDevice, testMode, TestType.Blood);
                }
            }
            break;
            case QA: {
                if (testType == null) {
                    navigateToQATestTypeMenu(context, finishContext);
                } else {
                    navigateToQATestMenu(context, finishContext);
                }
            }
            break;
            default:
                //TODO: log error
                break;
        }
    }

    private void setupNavToScreen(String readerBTA, String readerAlias, Context context, UIScreens targetScreen) {
        mNavigationPostponed = true; // only navigate if test successfully started
        mNavigationObject = new EpocNavigationObject();
        mNavigationObject.setContext(context);
        if (targetScreen == UIScreens.HomeScreen || targetScreen == UIScreens.MULTITEST_SCREEN)
            mNavigationObject.setFinishContext(true);
        mNavigationObject.setTargetscreen(targetScreen);
        mNavigationObject.setExtraInfo1(new ExtraInfo("Reader_BTA", readerBTA));
        mNavigationObject.setExtraInfo2(new ExtraInfo("Reader_Alias", readerAlias));
    }

    private void setupNavToLoginScreen(boolean handleInactivity, Context context) {
        mNavigationPostponed = true; // only navigate if test successfully started
        mNavigationObject = new EpocNavigationObject();
        mNavigationObject.setContext(context);
        mNavigationObject.setTargetscreen(UIScreens.LoginScreen);
        mNavigationObject.setExtraInfo1(new ExtraInfo("Handle_Inactivity", handleInactivity));
    }

    //
//    private void navigate(Context context, UIScreens targetScreen) {
//        if (!mNavigationPostponed) {
//            mNavigationObject = new EpocNavigationObject();
//            mNavigationObject.setContext(context);
//            mNavigationObject.setTargetscreen(targetScreen);
//            EventBus.getDefault().post(mNavigationObject);
//        }
//    }
    private void navigateToQATestMenu(Context context, boolean finishContext) {
        if (!mNavigationPostponed) {
            EpocNavigationObject navigationObject = new EpocNavigationObject();
            navigationObject.setContext(context);
            navigationObject.setTargetscreen(UIScreens.QATestMenuScreen);
            navigationObject.setFinishContext(finishContext);
            EventBus.getDefault().post(navigationObject);
        }
    }

    private void navigateToQATestTypeMenu(Context context, boolean finishContext) {
        if (!mNavigationPostponed) {
            EpocNavigationObject navigationObject = new EpocNavigationObject();
            navigationObject.setContext(context);
            navigationObject.setTargetscreen(UIScreens.QATestTypeMenuScreen);
            navigationObject.setFinishContext(finishContext);
            EventBus.getDefault().post(navigationObject);
        }
    }

    public void navigateToReaderDiscovery(Context context, TestMode testMode, TestType testType, boolean finishContext) {
        if (!mNavigationPostponed) {
            EpocNavigationObject navigationObject = new EpocNavigationObject();
            navigationObject.setContext(context);
            int discoveryValue = getDiscoveryMode(testMode);
            navigationObject.setExtraInfo1(new ExtraInfo("SetDiscoveryMode", discoveryValue));
            if (testType != null)
                navigationObject.setExtraInfo2(new ExtraInfo("SetDiscoveryType", testType.value));
            navigationObject.setTargetscreen(UIScreens.READER_DISCOVERY);
            navigationObject.setFinishContext(finishContext);
            EventBus.getDefault().post(navigationObject);
        }
    }

    public void navigateToMultiTestActivity(Context context, Boolean finishContext) {
        EpocNavigationObject navigationObject = new EpocNavigationObject();
        navigationObject.setContext(context);
        navigationObject.setTargetscreen(UIScreens.MULTITEST_SCREEN);
        navigationObject.setFinishContext(finishContext);
        EventBus.getDefault().post(navigationObject);
    }

    private int getDiscoveryMode(TestMode testMode) {
        int retval = 0;
        switch (testMode) {

            case BloodTest:
                retval = ReaderDiscoveryMode.BLOOD_TEST.value;
                break;
            case QA:
                retval = ReaderDiscoveryMode.QA_TEST.value;
                break;
            case ENUM_UNINITIALIZED:
                break;
        }
        return retval;
    }
    // handle call from inactivity service client
    // will close all tests and navigate to Logout page

    public void closeTestsAndLogout() throws EpocTestDriverException {
        // prepare navigation
        setupNavToLoginScreen(true, this.mContext);
        try {
            for (int i = 0; i < getTestContainer().size(); i++) {
                getTestContainer().get(i).close();
            }
            EventBus.getDefault().post(new TestManagementCallback(TestManagementEventType.All_TESTS_STOPPED, true));
        } catch (EpocTestDriverException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new TestManagementCallback(TestManagementEventType.CANNOT_STOP_MULTIPLE_TESTS, true));
            throw e;

        }
    }

    /*
     * called from testuidriver.
     * public for testuidriver and testmanager are in different packages.
     */
    public void navigate(UIScreens targetScreen, String readerBTA, String readerAlias) {
        EpocNavigationObject navigationObject = new EpocNavigationObject();
        navigationObject.setContext(mContext);
        navigationObject.setTargetscreen(targetScreen);
        navigationObject.setExtraInfo1(new ExtraInfo("Reader_BTA", readerBTA));
        navigationObject.setExtraInfo2(new ExtraInfo("Reader_Alias", readerAlias));
        EventBus.getDefault().post(navigationObject);

    }

    private void doPostponedNavigation() {
        if (mNavigationPostponed && mNavigationObject != null) {
            EventBus.getDefault().post(mNavigationObject);
            mNavigationObject = null;
            mNavigationPostponed = false;
        }
    }

    private void clearPostponedNavigation() {
        mNavigationPostponed = false;
        mNavigationObject = null;
    }

    @Subscribe
    public void onEvent(TestManagementCallback callback) throws TestManagementException {
        switch (callback.getTestManagementEventType()) {
            case FOREGROUND_TEST_RESTARTED:
                break;
            case FOREGROUND_TEST_STARTED:
                doPostponedNavigation();
                break;
            case BACKGROUND_TEST_STARTED:
                break;
            case BACKGROUND_TEST_RESTARTED:
                break;
            case TEST_STOPPED:
                if (getTestContainer().size() == 0) {
                    setDeviceTestState(false);
                }
                if (getTestDriver(callback.getReaderBTA()).isCalculationDone()) {
                    setDeviceTestCompletedState(false);
                }
                getTestContainer().remove(getTestDriver(callback.getReaderBTA()));
                mTestContainerEmitter.onNext(new TestContainerEvents(TestContainerEvents.ChangeType.REMOVE, getTestDriver(callback.getReaderBTA())));
                doPostponedNavigation();
                break;

            case CANNOT_STOP_MULTIPLE_TESTS:
                throw new TestManagementException("Multiple tests couldn't be stopped");

            case CANNOT_START_FOREGROUND_TEST:
            case CANNOT_START_BACKGROUND_TEST:

                getTestContainer().remove(getTestDriver(callback.getReaderBTA()));
                mTestContainerEmitter.onNext(new TestContainerEvents(TestContainerEvents.ChangeType.REMOVE, getTestDriver(callback.getReaderBTA())));
                throw new TestManagementException("test on Reader: " + callback.getReaderBTA() + " couldn't be started");

            case CANNOT_STOP_TEST:
                throw new TestManagementException("Reader: " + callback.getReaderBTA() + " couldn't be stopped");


            case CANNOT_RESTART_FOREGROUND_TEST:
                break;
            case CANNOT_RESTART_BACKGROUND_TEST:
                break;
            case All_TESTS_STOPPED:
                getTestContainer().clear();
                setDeviceTestState(false);
                setDeviceTestCompletedState(false);
                doPostponedNavigation();
                break;
        }
        clearPostponedNavigation();
    }

    public int getActiveTestCount(final String btAddress) {
        int count = 0;
        for (TestUIDriver uitd : getTestContainer()) {

            if (!uitd.getReaderDevice().getDeviceAddress().equals(btAddress) && uitd.isActive()) {
                count++;
            }
        }
        return count;
    }

    /*
     * this method creates a single test and displays it right away if there are no other tests running.
     * otherwise (and only if QA test), the new test will be created in the background
     */
    private void startNewTest(final ReaderDevice readerDevice, final TestMode testMode, final TestType testType) throws TestManagementException {

        final String btAddress = readerDevice.getDeviceAddress();
        // exit condition: a patient test is already in container
        if (testMode.compareTo(TestMode.BloodTest) == 0 && getTestContainer().size() > 0) {
            throw new TestManagementException("A patient test is still processed. Cannot start a second patient test.");
        }
        if (isReaderInUse(btAddress)) {
            throw new TestManagementException("Cannot start test. Reader is in use");
        }
        int activeTests = getActiveTestCount(btAddress);
        if (activeTests == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    createForegroundTest(readerDevice, testMode, testType);
                }
            }, readerDevice.getDeviceAlias()).start();

        } else if (activeTests > 0 && testMode.compareTo(TestMode.QA) == 0) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    createBackgroundTest(readerDevice, testMode, testType);
                }
            }, readerDevice.getDeviceAlias()).start();
        }
    }

    public ReaderDevice getSingleDedicatedReaderDevice() {
        ReaderDevice singleDedicatedReaderDevice = new ReaderModel().getSingleDedicatedReaderDevice();
        return singleDedicatedReaderDevice;
    }

    public boolean isReaderInUse(String btAddress) {
        for (TestUIDriver uitd : getTestContainer()) {
            if (uitd.getReaderDevice().getDeviceAddress().equals(btAddress) && uitd.isActive()) {
                return true;
            }

        }
        return false;
    }

    /*
     * this method will render to display a test that was running in background
     */
    public void showTest(UUID guid) {
        setTestVisible(guid);
        mDisplayedTest.show();
    }

    public void restartTest(final TestUIDriver uiTestController) {
        if (mDisplayedTest != null && mDisplayedTest.getTestController().getControllerUUID().equals(uiTestController.getTestController().getControllerUUID())) {
            mDisplayedTest = null;

        }
        // stop testcontroller
        uiTestController.getTestController().stop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                restartEpocTest(uiTestController);
            }
        }, uiTestController.getReaderDevice().getDeviceAlias()).start();
    }

    public void startMultipleTests(Context context, List<ReaderDevice> readers, final TestMode testMode, final TestType testType, Boolean finishContext) {
        for (final ReaderDevice rdrdevice : readers) {
            if (!isReaderInUse(rdrdevice.getDeviceAddress())) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        createBackgroundTest(rdrdevice, testMode, testType);
                    }
                }, rdrdevice.getDeviceAlias()).start();
            }

        }
        navigateToMultiTestActivity(context, finishContext);
    }

    public void stopAllTests() {

    }

    /*
     * simply stop the test and stay on same screen
     */
    public void stopTest(String readerBTA) throws EpocTestDriverException {
        setDeviceTestState(false);
        setDeviceTestCompletedState(false);
        try {
            TestUIDriver tuid = getTestDriver(readerBTA);
            tuid.close();
            getTestContainer().remove(tuid);
            mTestContainerEmitter.onNext(new TestContainerEvents(TestContainerEvents.ChangeType.REMOVE, tuid));
        } catch (EpocTestDriverException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*
     *  navigate to target screen only if the test successfully stopped
     */
    public void stopTestAndNavigate(String readerBTA, UIScreens targetScreen, Context navigationContext) throws EpocTestDriverException {
        // prepare navigation
        setupNavToScreen(readerBTA, null, navigationContext, targetScreen);
        setDeviceTestState(false);
        setDeviceTestCompletedState(false);
        try {
            TestUIDriver tuid = getTestDriver(readerBTA);
            tuid.close();
            EventBus.getDefault().post(new TestManagementCallback(TestManagementEventType.TEST_STOPPED, readerBTA));
        } catch (EpocTestDriverException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new TestManagementCallback(TestManagementEventType.CANNOT_STOP_TEST, readerBTA));
            throw e;

        }
    }

    public void continueTestAndNavigate(Context navigationContext) throws EpocTestDriverException {
        EpocNavigationObject navigationObject = new EpocNavigationObject();
        navigationObject.setContext(navigationContext);
        navigationObject.setTargetscreen(UIScreens.MULTITEST_SCREEN);
        navigationObject.setFinishContext(true);
        EventBus.getDefault().post(navigationObject);
    }

    // private methods

    private void restartEpocTest(TestUIDriver uiTestController) {
        final String deviceAddress = uiTestController.getReaderDevice().getDeviceAddress();
        TestType prevTestType = uiTestController.getTestController().getTestType();
        uiTestController.setTestController(null);
        LegacyTestController tc = new LegacyTestController();
        tc.setTestType(prevTestType);
        tc.initializeController(uiTestController.getReaderDevice(), uiTestController.getOperationMode());
        uiTestController.setTestController(tc);
        try {
            uiTestController.start();
            uiTestController.setActive(true);
            tc.start();
            setDeviceTestState(true);
            EventBus.getDefault().post(new TestManagementCallback(TestManagementEventType.FOREGROUND_TEST_RESTARTED, deviceAddress));
        } catch (Exception exp) {
            setDeviceTestState(false);
            exp.printStackTrace();
            EventBus.getDefault().post(new TestManagementCallback(TestManagementEventType.CANNOT_RESTART_FOREGROUND_TEST, deviceAddress));
        }
    }

    private UUID createTest(ReaderDevice readerDevice, TestMode testmode, TestType testtype, boolean background) {
        TestManagementEventType testEvtStartType = background ? TestManagementEventType.BACKGROUND_TEST_STARTED : TestManagementEventType.FOREGROUND_TEST_STARTED;
        TestManagementEventType testEvtFailType = background ? TestManagementEventType.CANNOT_START_BACKGROUND_TEST : TestManagementEventType.CANNOT_START_FOREGROUND_TEST;
        //TestController tc = new TestController();
        //***********************************************
        //this row was added at July 16th 2018 for HostBridge
        //Lagacy... for 1A reader , keep TestController for NexGen reader
        LegacyTestController tc = new LegacyTestController();
        //*********************************************
        tc.setTestType(testtype);
        tc.initializeController(readerDevice, testmode);

        UUID retval = tc.getControllerUUID();
        TestUIDriver uitd = null;
        switch (testmode) {
            case BloodTest:
                uitd = new PatientTestUIDriver(readerDevice, tc);
                uitd.setOperationMode(TestMode.BloodTest);
                break;
            case QA:
                uitd = new QATestUIDriver(readerDevice, tc);
                uitd.setOperationMode(TestMode.QA);
                break;
        }

        getTestContainer().add(uitd);
        mTestContainerEmitter.onNext(new TestContainerEvents(TestContainerEvents.ChangeType.ADD, uitd));

        // start gears
        if (background)
            uitd.hide();
        else
            setTestVisible(tc.getControllerUUID());

        try {
            uitd.start();
            if (!background)
                uitd.resetUI(UIChangeRequestReason.Unknown);
            tc.start();
            uitd.setActive(true);
            EventBus.getDefault().post(new TestManagementCallback(testEvtStartType, readerDevice.getDeviceAddress()));
        } catch (Exception exp) {
            exp.printStackTrace();
            EventBus.getDefault().post(new TestManagementCallback(testEvtFailType, readerDevice.getDeviceAddress()));
        }
        return retval;
    }

    private UUID createForegroundTest(ReaderDevice readerDevice, TestMode testmode, TestType testtype) {
        return createTest(readerDevice, testmode, testtype, false);
    }

    private UUID createBackgroundTest(ReaderDevice readerDevice, TestMode testmode, TestType testtype) {
        return createTest(readerDevice, testmode, testtype, true);

    }

    public void setDeviceTestState(boolean testing) {
        SharedPreferences.Editor editor = mPref.edit();
        if (testing) {
            editor.putBoolean(Consts.TESTING, true);
            editor.putBoolean(Consts.TESTCOMPLETED, false);
        } else {
            editor.putBoolean(Consts.TESTING, false);
        }
        editor.apply();
    }

    /**
     * Broadcasts message to the InactivityService which is taking care of user inactivity timer.
     *
     * @param value the flag to convey whether to reset the timer(true) or stop the timer (false)
     */
    private void inactivityBroadcast(boolean value) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.epocal.host4.inactivity");
        broadcastIntent.putExtra("inactivity", value);
        GloabalObject.getApplication().getApplicationContext().sendBroadcast(broadcastIntent);
    }

    public void setDeviceTestCompletedState(boolean testCompletedState) {
        SharedPreferences.Editor editor = mPref.edit();
        if (testCompletedState) {
            editor.putBoolean(Consts.TESTING, false);
            editor.putBoolean(Consts.TESTCOMPLETED, true);
            inactivityBroadcast(true);
        } else {
            editor.putBoolean(Consts.TESTCOMPLETED, false);
        }
        editor.apply();
    }

    private void setTestVisible(UUID guid) {

        if (mDisplayedTest != null) {
            // already visible
            if (mDisplayedTest.getTestController().getControllerUUID().equals(guid)) {
                return;
            } else {
                mDisplayedTest.setVisible(false);

            }
        }
        for (TestUIDriver uitc : getTestContainer()) {
            if (uitc.getTestController().getControllerUUID().equals(guid)) {
                mDisplayedTest = uitc;
                break;
            }
        }
        mDisplayedTest.setVisible(true);
    }

    public boolean areMultipleActiveTests() {
        int nrTests = 0;
        for (TestUIDriver tuid : getTestContainer()) {
            if (tuid.isActive()) {
                nrTests++;
            }
        }
        return (nrTests > 1);
    }

    @Override
    public void close() throws Exception {
        EventBus.getDefault().unregister(this);
    }

    public int getQATestCount() {
        //todo can we upgrade to streams?
        int i = 0;
        for (TestUIDriver driver : getTestContainer()) {
            if (driver instanceof QATestUIDriver)
                i++;
        }
        return i;
    }

    public List<TestUIDriver> getQATests() {
        List<TestUIDriver> tests = new ArrayList<>();
        for (TestUIDriver driver : getTestContainer()) {
            if (driver instanceof QATestUIDriver)
                tests.add(driver);
        }
        return tests;
    }

    public void navigateToQATestHistory(Context context, TestMode testMode, boolean finishContext) {
        if (!mNavigationPostponed) {
            EpocNavigationObject navigationObject = new EpocNavigationObject();
            navigationObject.setContext(context);
            // Pack int TestMode.value to send to TestHistory
            navigationObject.setExtraInfo1(new ExtraInfo(Consts.KEY_TESTMODE, testMode.value));
            navigationObject.setTargetscreen(UIScreens.TestHistoryScreen);
            navigationObject.setFinishContext(finishContext);
            EventBus.getDefault().post(navigationObject);
        }
    }
}
