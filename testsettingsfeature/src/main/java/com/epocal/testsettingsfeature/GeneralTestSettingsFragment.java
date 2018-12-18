package com.epocal.testsettingsfeature;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.epocal.datamanager.HostConfigurationModel;

/**
 * This class shows the General Test Settings screen
 * <p>
 * Created by Zeeshan A Zakaria on 5/8/2017.
 */

public class GeneralTestSettingsFragment extends Fragment {

    HostConfigurationModel mHostConfigurationModel;

    SwitchCompat mSwitchAllowToRejectTest, mEnforceCriticalHandling, mRecallPatientId, mRecallSampleType;
    SwitchCompat mSaveRawData, mAllowDataRecall, mCloseCompletedTests, mAllowExpiredCards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_general_test_settings, group, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHostConfigurationModel = new HostConfigurationModel();
        mHostConfigurationModel.openRealmInstance();

        mSwitchAllowToRejectTest = (SwitchCompat) view.findViewById(R.id.switch_allow_reject_test);
        mEnforceCriticalHandling = (SwitchCompat) view.findViewById(R.id.switch_enforce_critical_handling);
        mRecallPatientId = (SwitchCompat) view.findViewById(R.id.switch_recall_patient_id);
        mRecallSampleType = (SwitchCompat) view.findViewById(R.id.switch_recall_sample_type);
        mSaveRawData = (SwitchCompat) view.findViewById(R.id.switch_save_raw_data);
        mAllowDataRecall = (SwitchCompat) view.findViewById(R.id.switch_allow_data_recall);
        mCloseCompletedTests = (SwitchCompat) view.findViewById(R.id.switch_close_completed_tests);
        mAllowExpiredCards = (SwitchCompat) view.findViewById(R.id.switch_allow_expired_cards);

        mSwitchAllowToRejectTest.setChecked(mHostConfigurationModel.isAllowRejectTests());
        mEnforceCriticalHandling.setChecked(mHostConfigurationModel.isEnforceCriticalHandling());
        mRecallPatientId.setChecked(mHostConfigurationModel.isRetainPatientId());
        mRecallSampleType.setChecked(mHostConfigurationModel.isRetainSampleType());
//        mSaveRawData.setChecked(mHostConfigurationModel.save());
        mAllowDataRecall.setChecked(mHostConfigurationModel.isAllowRecallData());
        mCloseCompletedTests.setChecked(mHostConfigurationModel.isCloseUnattendedTests());
        mAllowExpiredCards.setChecked(mHostConfigurationModel.isAllowExpiredCards());

        mSwitchAllowToRejectTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mHostConfigurationModel.setAllowRejectTests(isChecked);
            }
        });

        mEnforceCriticalHandling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mHostConfigurationModel.setEnforceCriticalHandling(isChecked);
            }
        });

        mRecallPatientId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mHostConfigurationModel.setRetainPatientId(isChecked);
            }
        });

        mRecallSampleType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mHostConfigurationModel.setRetainSampleType(isChecked);
            }
        });

        mSaveRawData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // TODO: fix this
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mHostConfigurationModel.setSaveRawData(isChecked);
            }
        });

        mAllowDataRecall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mHostConfigurationModel.setAllowRecallData(isChecked);
            }
        });

        mCloseCompletedTests.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // TODO: fix this
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mHostConfigurationModel.setCloseUnattendedTests(isChecked);
            }
        });

        mAllowExpiredCards.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mHostConfigurationModel.setAllowExpiredCards(isChecked);
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHostConfigurationModel.closeRealmInstance();
    }
}