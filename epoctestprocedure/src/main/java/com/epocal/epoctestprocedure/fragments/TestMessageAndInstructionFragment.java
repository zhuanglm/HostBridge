package com.epocal.epoctestprocedure.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.epocal.common.epocobjects.EpocUIMessageType;
import com.epocal.epoctest.uimessage.EpocTestUIMessage;
import com.epocal.epoctest.uimessage.TestMessageContext;
import com.epocal.epoctestprocedure.PatientTestActivity;
import com.epocal.epoctestprocedure.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;


public class TestMessageAndInstructionFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = TestMessageAndInstructionFragment.class.getSimpleName();
    private List<TextView> mTvMessages;
    private TextView mTvReaderName;
    private TextView mTvBatteryLevel;
    private ImageView mIvBatteryLevel;
    private ProgressBar mProgressBar;
    private ConstraintLayout mMessagePanel;
    private OnMessagePanelSelectedListener mCallback;
    private float mDefaultScale = 1f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test_message_and_instruction, container, false);
        mTvMessages = new ArrayList<>(4);
        mMessagePanel = rootView.findViewById(R.id.message_panel);
        mTvMessages.add((TextView) rootView.findViewById(R.id.message_0));
        mTvMessages.add((TextView) rootView.findViewById(R.id.message_1));
        mTvMessages.add((TextView) rootView.findViewById(R.id.message_2));
        mTvMessages.add((TextView) rootView.findViewById(R.id.message_3));
        mTvReaderName = rootView.findViewById(R.id.text_view_reader_id);
        mTvBatteryLevel = rootView.findViewById(R.id.text_view_reader_battery);
        mIvBatteryLevel = rootView.findViewById(R.id.image_view_reader_battery);
        mProgressBar = rootView.findViewById(R.id.message_progress_bar);
        mDefaultScale =  mProgressBar.getScaleY();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mProgressBar.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(getContext(), R.color.primaryBlueNew),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            mProgressBar.getIndeterminateDrawable().setColorFilter(
                    getContext().getResources().getColor(R.color.primaryBlueNew),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach()");
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        if (context instanceof OnMessagePanelSelectedListener) {
            mCallback = (OnMessagePanelSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnTestResultSelectedListener");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.message_panel) {
            // future execute action if not null
        }
    }

    public void setMessages(final TestMessageContext testMessageContext) {
        EpocTestUIMessage[] messages = testMessageContext.getEpocTestUIMessages();
        // messages array has length of 5.
        // index 0 - 4 are used for patient test
        // index 5 is used for QA test (multi test view)
        if (messages == null) return;
        for (int i = 0; i < mTvMessages.size(); i++) {
            TextView tv = mTvMessages.get(i);
            // When resId = 0, it means no messages to display in this message array slot.
            int resId = messages[i].getStringResourceID();
            Object[] params = messages[i].getOptionalStringParameters();
            try {
                String message = (resId == 0) ? " " : ((params == null) ? getString(resId) : getString(resId, params));
                tv.setText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int progress = (testMessageContext.mProgress - 100) * -1; //reverse the progress bar
        if(testMessageContext.getEpocUIMessageType() == EpocUIMessageType.PROGRESS_MESSAGE) {
            mProgressBar.setVisibility(View.VISIBLE);
            if(testMessageContext.mProgress == -1) {
                mProgressBar.setScaleY(3f);
                mProgressBar.setIndeterminate(true);
            } else {
                mProgressBar.setScaleY(mDefaultScale);
                mProgressBar.setIndeterminate(false);
                mProgressBar.setProgress(progress);
            }
            mTvMessages.get(2).setVisibility(View.GONE);
            mTvMessages.get(3).setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mTvMessages.get(2).setVisibility(View.VISIBLE);
            mTvMessages.get(3).setVisibility(View.VISIBLE);
        }
        if (testMessageContext.mVibrate)
            shakeIt(mProgressBar.getContext());
        mTvBatteryLevel.setText(getString(R.string.reader_battery_level,(int)testMessageContext.mBatteryLevel));
        if ((int)testMessageContext.mBatteryLevel == 100) {
            mIvBatteryLevel.setImageResource(R.mipmap.ic_battery_full);
        } else if ((int)testMessageContext.mBatteryLevel >= 75) {
            mIvBatteryLevel.setImageResource(R.mipmap.ic_battery_threequarters);
        } else if ((int)testMessageContext.mBatteryLevel >= 50) {
            mIvBatteryLevel.setImageResource(R.mipmap.ic_battery_half);
        } else if ((int)testMessageContext.mBatteryLevel >= 25) {
            mIvBatteryLevel.setImageResource(R.mipmap.ic_battery_quarter);
        } else {
            mIvBatteryLevel.setImageResource(R.mipmap.ic_battery_empty);
        }
        if (testMessageContext.getClickHandler() != null) {
            mMessagePanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        testMessageContext.getClickHandler().run();
                    } catch (Exception e) {
                        Log.d(TAG, "Exception: " + e.getMessage());
                    }
                }
            });
        } else {
            mMessagePanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        PatientTestActivity activity = (PatientTestActivity)getActivity();
                        activity.onMessagePanelSelected();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void shakeIt(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onAttach()");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        PatientTestActivity patientTestActivity = (PatientTestActivity) getActivity();
        setMessages(patientTestActivity.mTestUIDriver.getTestMessageContext()); // It is important to refresh the latest message when the fragment is switched back from the test results fragment.
        mTvReaderName.setText(patientTestActivity.mTestUIDriver.getReaderDevice().getDeviceAlias());
    }

    // Container Activity must implement this interface
    public interface OnMessagePanelSelectedListener {
        void onMessagePanelSelected();
        void closeActivity();
        void openDocumentResultsStep();
    }
}
