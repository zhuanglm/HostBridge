package com.epocal.host4.homescreen.DateAndTime;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.epocal.host4.R;
import com.epocal.util.DateUtil;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import static com.epocal.common.CU.titleCase;

@SuppressLint("DefaultLocale")
public class DialogFragmentDateAndTime extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    View mDialogLayout;
    Button mBtnDate, mBtnTime, mBtnTimezone;
    TextView mTextViewDate, mTextViewTime;
    SharedPreferences mSharedPrefs;
    String mDate, mTime, mTimezone;
    private static boolean DO_IT;

    public static DialogFragmentDateAndTime newInstance() {
        DialogFragmentDateAndTime dialogFragment = new DialogFragmentDateAndTime();
        Bundle args = new Bundle();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        super.onCreate(bundle);
        final AlertDialog alertDialog;
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        alertDialog = alertDialog();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
            }
        });

        alertDialog.getWindow().setBackgroundDrawableResource(R.color.pastelGreenLight);
//        alertDialog.getWindow().setTitleColor(getResources().getColor(R.color.colorAlereWhite));

        return alertDialog;
    }

    /**
     * @return the alert dialog
     */
    private AlertDialog alertDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);

        mDialogLayout = factory.inflate(R.layout.dialog_set_date_time, null);
        mBtnDate = (Button) mDialogLayout.findViewById(R.id.btn_set_date);
        mBtnTime = (Button) mDialogLayout.findViewById(R.id.btn_set_time);
        mTextViewDate = (TextView) mDialogLayout.findViewById(R.id.text_view_date);
        mTextViewTime = (TextView) mDialogLayout.findViewById(R.id.text_view_time);
        mTextViewDate.setText(DateUtil.toString("yyyy-MM-dd"));
        mTextViewTime.setText(DateUtil.toString("HH:mm"));
        mBtnDate.setOnClickListener(mOnDateClickListener);
        mBtnTime.setOnClickListener(mOnTimeClickListener);

        alertDialog.setTitle(titleCase(getActivity().getString(R.string.set_date_time))).setView(mDialogLayout)
                .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Actions are taken in the onShow()
                    }
                });

        return alertDialog.create();
    }

    /**
     * Open the dialog box to edit date
     **/
    private View.OnClickListener mOnDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setParent(DialogFragmentDateAndTime.this);
            datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");
        }
    };

    /**
     * Open the dialog box to edit time
     **/
    private View.OnClickListener mOnTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setParent(DialogFragmentDateAndTime.this);
            timePickerFragment.show(getActivity().getFragmentManager(), "timePicker");
        }
    };

    /**
     * @param datePicker the widget
     * @param year       the year
     * @param month      the month, zero based
     * @param day        the day
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar selectedDate1 = DateUtil.now();
        selectedDate1.set(Calendar.DAY_OF_YEAR, year);
        selectedDate1.set(Calendar.DAY_OF_MONTH, month + 1);
        selectedDate1.set(Calendar.DATE, day);
        AlarmManager am = (AlarmManager) this.getContext().getSystemService(Context.ALARM_SERVICE);
        am.setTime(selectedDate1.getTimeInMillis());
        mBtnDate.setText(mDate);
//        Log.i(Constants.TAG, "new date is: " + year + ", " + (month + 1) + ", " + day);
//'        Log.i(Constants.TAG, "unixDate is: " + mTimestamp);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        mTimestamp = CU.stringToUnixtime(mDate, hourOfDay + ":" + minute, mClock24Hour);
//        setDateTime(mTimestamp);
//        mBtnTime.setText(mTime);
//        Log.i(Constants.TAG, "new hour is: " + hourOfDay);
//        Log.i(Constants.TAG, "new minute is: " + minute);
    }

    /**
     * Set up date and time when user updates it from the dialog
     *
     * @param timestamp the epoch time
     */
    private void setDateTime(String timestamp) {
        //CU.addToSharedPrefs(getActivity(), SyncStateContract.Constants.DATA, timestamp);
        //mReadableTime = CU.unixToTime(timestamp, 1, mClock24Hour);
        //String[] split = mReadableTime.split(", ");
        //mDate = split[0];
        //mTime = split[1];
    }
}