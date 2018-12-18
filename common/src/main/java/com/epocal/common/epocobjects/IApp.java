package com.epocal.common.epocobjects;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by bmate on 8/2/2017.
 */

public interface IApp {

    Date getSoftwareExpirationDate();
    Context getApplicationContext();
    SharedPreferences getSharedPreferences(String name, int mode);
}
