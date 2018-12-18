package com.epocal.common.globaldi;

import android.app.Application;

import com.epocal.common.epocobjects.IApp;

/**
 * Created by dning on 5/26/2017.
 */

public class GloabalObject {
    private static IApp mApp;
    public static void setApplication(IApp app)
    {
        mApp = app;
    }
    public static IApp getApplication()
    {
        return mApp;
    }
}
