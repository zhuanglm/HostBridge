package com.epocal.epoclog;

import com.epocal.common.eventmessages.DeviceSettingsChangeNotification;
import com.epocal.common.realmentities.DeviceSetting;
import com.epocal.common.realmentities.Log;
import com.epocal.common.types.LogContext;
import com.epocal.common.types.LogLevel;
import com.epocal.datamanager.DeviceSettingsModel;
import com.epocal.util.EnumSetUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.EnumSet;

/**
 * Created by bmate on 7/13/2017.
 * saves logs according to verbosity settings
 */

public class LogServer implements AutoCloseable{
    private static LogServer instance = null;
    private static Object synchroot = new Object();

    private RealmLogger mRealmLogger;
    private LogServer() {
        // customize if needed

    }
    public static LogServer getInstance() {
        synchronized (synchroot){
            if (instance == null) {
                instance = new LogServer();
            }
            return instance ;
        }

    }
    private EnumSet<LogLevel> mLogVerbosity = EnumSet.allOf(LogLevel.class);
    /*
    * initialize logger objects
     */
    public void Initialize(){
        EventBus.getDefault().register(this);
        mLogVerbosity = new DeviceSettingsModel().getLogLevels();
        mRealmLogger = new RealmLogger();

    }
    @Subscribe
    public void updateLogVerbosity(DeviceSettingsChangeNotification notification){
        mLogVerbosity = new DeviceSettingsModel().getLogLevels();
    }
    /*
    * produces a Log object to consumer (save to DB)
    * if Log object is not well formed, no broadcast occurs
     */
    public void log (Log epocLog){
        if (mLogVerbosity.contains(epocLog.getLogLevel())){
            EventBus.getDefault().post(epocLog);
        }
    }
    public void devlog (String message){
        Log newLog = new Log();
        newLog.setMessage(message);
        newLog.setMessageExtra("dev only");
        newLog.setLogLevel(LogLevel.Debug);
        newLog.setLogContext(LogContext.HOST);
        EventBus.getDefault().post(newLog);
    }
    @Override
    public void close() throws Exception {

        EventBus.getDefault().unregister(this);
        mRealmLogger.close();
    }
}
