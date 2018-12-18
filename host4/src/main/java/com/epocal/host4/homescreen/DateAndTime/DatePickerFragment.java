package com.epocal.host4.homescreen.DateAndTime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DialogFragmentDateAndTime mParent;

    public void setParent(DialogFragmentDateAndTime parent) {
        mParent = parent;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {

        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        return new DatePickerDialog(getActivity(), mParent, year, month, day);

}

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
    }
}