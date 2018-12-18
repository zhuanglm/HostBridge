package com.epocal.login.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.epocal.login.di.LoginScreenContract;

/**
 * The login model
 * Created by Zeeshan A Zakaria on 6/13/2017.
 */

public class LoginLogic implements LoginScreenContract.Logic {

    @Override
    public int getTestConfigExpiry(long testConfigExpiryDate) {
        long currentTime = System.currentTimeMillis() / 1000;  // in seconds
        long remainingTime = ((testConfigExpiryDate /1000) - currentTime) / (60 * 60 * 24); // In days
        return (int) remainingTime;
    }
}