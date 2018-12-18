package com.epocal.host4.init;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.multidex.MultiDexApplication;
import android.view.Gravity;
import android.view.WindowManager;

import com.epocal.common.DeviceInfoUtil;
import com.epocal.common.epocobjects.IApp;
import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.realmentities.Host;
import com.epocal.common.realmentities.Log;
import com.epocal.common.types.HostStatus;
import com.epocal.common.types.LogContext;
import com.epocal.common.types.LogLevel;
import com.epocal.common_ui.CustomViewGroup;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;
import com.epocal.datamanager.realmrepository.RepositoryManager;
import com.epocal.epoclog.LogServer;
import com.epocal.epoctest.TestManager;
import com.epocal.epoctest.testprocess.TestDataProcessor;
import com.epocal.host4.FlowController.AppNavigationController;
import com.epocal.host4.di.components.AppComponent;
import com.epocal.host4.di.components.DaggerAppComponent;
import com.epocal.host4.di.modules.AppModule;
import com.epocal.util.DateUtil;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import io.realm.Realm;

/**
 * The entry point of the application
 * <p>
 * Created by Bela Mate on 3/20/2017.
 */

public class App extends MultiDexApplication implements IApp {
    private AppComponent appComponent;
    private Host mHost;

    @Override
    public void onCreate() {
        super.onCreate();

        // subscribe to eventbus
        EventBus.getDefault().register(this);
        //init realm
        Realm.init(this);

//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .deleteRealmIfMigrationNeeded()
//                .schemaVersion(0)
//                .build();
//        Realm.setDefaultConfiguration(config);

        Realm realm = Realm.getDefaultInstance();

        PrimaryKeyFactory.getInstance().initialize(realm);
        if (realm.isEmpty()) {
            RepositoryManager.FactoryInitializeRealm(realm);
        }
        realm.close();

        // only for debugging, intitialize Stetho realm browser
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build()
        );

        //init epoc logging
        LogServer.getInstance().Initialize();

        // log app start
        Log epocLog = new Log();
        epocLog.setAlert(false);
        epocLog.setLogContext(LogContext.HOST);
        epocLog.setLogLevel(LogLevel.Information);
        epocLog.setMessage("Started HOST4 application, version 4.0.0");
        LogServer.getInstance().log(epocLog);

        //init dependency injector for  modules
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        com.epocal.common.globaldi.GloabalObject.setApplication(this);
        com.epocal.epoctest.TestConfigurationManager.getInstance().loadConfig();


        // init test manager
        TestManager.getInstance().Initialize();
        // create global catcher
//        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
//        {
//            @Override
//            public void uncaughtException (Thread thread, Throwable e)
//            {
//                handleUncaughtException (thread, e);
//            }
//        });
        // Create "AM" folder in file system where AnalyticalManager stores its input as *.buf files.
        //FileSystemUtil.createAMFolder();

        initHost();
        DisableNotificationBar();
    }

    public Host getHost() {
        return mHost;
    }

    private void initHost() {
        //added by rzhuang Aug 22 2018
        mHost = new Host();
        mHost.setCreated(new Date(System.currentTimeMillis()));
        mHost.setActive(true);
        mHost.setSoftwareVersion("4.0.0");
        mHost.setTestVersion("33.0");
        mHost.setDepartment("Default");
        mHost.setStatus(HostStatus.Normal);

        mHost.setSerialNumber(DeviceInfoUtil.getSerialNumber());
        mHost.setMacAddress(DeviceInfoUtil.getWifiMac(this));
        mHost.setAlias(DeviceInfoUtil.getDeviceID());
        mHost.setBluetoothAddress(DeviceInfoUtil.getBtAddress());

        TestDataProcessor.mHost = this.mHost;
    }

    private void handleUncaughtException(Thread thread, Throwable e) {

        LogServer.getInstance().devlog("UncaughtException : " + e.getMessage());

        Intent myIntent = new Intent(getApplicationContext(), Entry.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(myIntent);
        System.exit(1);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EpocNavigationObject navigationObject) {
        AppNavigationController.handleNavigationEvent(navigationObject);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EventBus.getDefault().unregister(this);
    }

    /*
     / date format yyyy-MM-dd HH:mm:ss
    */
    @Override
    public Date getSoftwareExpirationDate() {
        return DateUtil.CreateDate("2019-09-01 00:00:00");
    }

    private void DisableNotificationBar() {
        WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
// this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
// Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (50 * getResources().getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;
        CustomViewGroup view = new CustomViewGroup(this);
        manager.addView(view, localLayoutParams);
    }
}
