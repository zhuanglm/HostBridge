package com.epocal.epoclog;

import com.epocal.common.realmentities.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.util.Date;

import io.realm.Realm;

/**
 * Created by bmate on 7/13/2017.
 * Saves logs into Realm
 */

public class RealmLogger implements AutoCloseable{

    private Object mSynchroot;
    RealmLogger(){
        EventBus.getDefault().register(this);
        mSynchroot = new Object();
    }

    @Subscribe
    public void logToDB(final Log msg){
        synchronized (mSynchroot) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Log managedLog = realm.createObject(Log.class);
                    managedLog.setAlert(msg.isAlert());
                    managedLog.setCreated(new Date());
                    managedLog.setLogContext(msg.getLogContext());
                    managedLog.setLogLevel(msg.getLogLevel());
                    managedLog.setMessage(msg.getMessage());
                    managedLog.setMessageExtra(msg.getMessageExtra());
                    managedLog.setSubContext(msg.getSubContext());

                }
            });
            realm.close();
        }

    }

    @Override
    public void close() throws Exception {
        EventBus.getDefault().unregister(this);
    }

}
