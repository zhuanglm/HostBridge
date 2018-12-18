package com.epocal.epoctestprocedure.fragments.dataentry;

import android.view.View;

/**
 * The class which is responsible to deal with error messages in the stepper
 *
 * Created by Zeeshan A Zakaria on 11/9/2017.
 */

class ErrorObject {

    private String errorMessage;
    private View view;

    ErrorObject(String errorMessage, View view) {
        this.errorMessage = errorMessage;
        this.view = view;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public View getView() {
        return view;
    }
}
