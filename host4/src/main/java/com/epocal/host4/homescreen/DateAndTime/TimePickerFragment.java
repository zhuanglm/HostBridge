package com.epocal.host4.homescreen.DateAndTime;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    DialogFragmentDateAndTime mParent;

    public void setParent(DialogFragmentDateAndTime parent) {
        mParent = parent;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {

        final Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);

        return new TimePickerDialog(getActivity(), mParent, hourOfDay, minute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("", "Local onTimeSet");
    }
}