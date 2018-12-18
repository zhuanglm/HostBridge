package com.epocal.login.rx;

import io.reactivex.Scheduler;

/**
 * The Rx implementation
 * Created by Zeeshan A Zakaria on 28/06/2017.
 */

public interface RxSchedulers {


    Scheduler runOnBackground();

    Scheduler io();

    Scheduler compute();

    Scheduler androidThread();

    Scheduler internet();

}