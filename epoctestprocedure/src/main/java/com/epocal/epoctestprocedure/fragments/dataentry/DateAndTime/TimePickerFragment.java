package com.epocal.epoctestprocedure.fragments.dataentry.DateAndTime;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TimePicker;

import com.epocal.common.CU;
import com.epocal.epoctestprocedure.R;
import com.epocal.epoctestprocedure.fragments.dataentry.VerticalStepper;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = TimePickerFragment.class.getSimpleName();

    VerticalStepper mParent;
    View mView;
    Calendar mCalendar;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle mArgs = getArguments();
        if (mArgs == null) {
            mCalendar = new GregorianCalendar();
        } else {
            String timeAsString = mArgs.getString(CU.EPOC_TIME_STRING_KEY);
            try {
                mCalendar = CU.epocTimeStringToCalendar(timeAsString);
            } catch (Exception e) {
                Log.d(TAG, "Wrong time format in timeAsString="+timeAsString);
                e.printStackTrace();
                mCalendar = new GregorianCalendar();
            }
        }
    }

    public void setParent(VerticalStepper parent) {
        mParent = parent;
    }

    public void setView(View view) {
        mView = view;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        int hourOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        return new TimePickerDialog((new ContextThemeWrapper(getActivity(), R.style.CustomPickerTheme)), this, hourOfDay, minute, true);
    }

    @Override
    public void onResume() {
        bottomCenterPositionFragment();
        super.onResume();
    }

    private void bottomCenterPositionFragment() {
        // Note: Dialog's height is 850dp and the top covers over the divier line of MessagePanel.
        // However, trying any smaller than 850dp will result in the round time picker content
        // clipping happens on the bottom.
        final Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        lp.height = 850;
        lp.dimAmount = 0.2f;  // 20% dim
        dialogWindow.setAttributes(lp);
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mParent.onDialogDismissed(this);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        String formattedTime = CU.calendarToEpocTimeString(mCalendar);
        mParent.setDateTime(formattedTime, mView);
    }
}