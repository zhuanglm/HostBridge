package com.epocal.host4;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.epocal.common.realmentities.User;
import com.epocal.datamanager.UserModel;

import io.realm.Realm;

/**
 * This Service kills the application using its PID
 *
 * Created by Zeeshan A Zakaria on 7/19/2017.
 */

public class ExitAppService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Realm realm = Realm.getDefaultInstance();
        User currentUser = new UserModel().getLoggedInUser();

        realm.beginTransaction();
        currentUser.setLoggedIn(false);
        realm.commitTransaction();
//        System.exit(0);
//        int process = android.os.Process.myPid();
//        Log.w("KILL SWITCH", "Killing process: " + process);
//        android.os.Process.killProcess(android.os.Process.myPid());
        stopSelf();
    }
}
