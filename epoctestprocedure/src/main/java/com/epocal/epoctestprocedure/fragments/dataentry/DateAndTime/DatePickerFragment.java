package com.epocal.epoctestprocedure.fragments.dataentry.DateAndTime;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import com.epocal.common.CU;
import com.epocal.epoctestprocedure.R;
import com.epocal.epoctestprocedure.fragments.dataentry.VerticalStepper;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private static final String TAG = DatePickerFragment.class.getSimpleName();

    VerticalStepper mParent;
    View mView;
    Calendar mCalendar;
    DatePicker mDatePicker;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle mArgs = getArguments();
        if (mArgs == null) {
            mCalendar = new GregorianCalendar();
        } else {
            String dateAsString = mArgs.getString(CU.EPOC_DATE_STRING_KEY);
            try {
                mCalendar = CU.epocDateStringToCalendar(dateAsString);
            } catch (Exception e) {
                Log.d(TAG, "Wrong date format in dateAsString="+dateAsString);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_date_picker, container, false);

        Calendar calendar = new GregorianCalendar();
        mDatePicker = (DatePicker) v.findViewById(R.id.date_picker);
        Button cancelButton = (Button) v.findViewById(R.id.button_1);
        Button okButton = (Button) v.findViewById(R.id.button_2);

        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);
        mDatePicker.init(year, month, day, null);
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        bottomCenterPositionFragment();
        super.onResume();
    }


    private void bottomCenterPositionFragment() {
        final Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        lp.width = 640;
        lp.height = 856;
        lp.dimAmount = 0.2f;  // 20% dim
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mParent.onDialogDismissed(this);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(year, month, dayOfMonth);
        String formattedDate = CU.calendarToEpocDateString(mCalendar);
        mParent.setDateTime(formattedDate, mView);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_1) {
            // Cancel - callback
        } else if (id == R.id.button_2) {
            // OK - callback
            int year = mDatePicker.getYear();
            int month = mDatePicker.getMonth();
            int dayOfMonth = mDatePicker.getDayOfMonth();
            onDateSet(mDatePicker, year, month, dayOfMonth);
        }
        dismiss();
    }
}