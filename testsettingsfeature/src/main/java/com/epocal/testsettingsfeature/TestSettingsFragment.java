package com.epocal.testsettingsfeature;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static com.epocal.testsettingsfeature.BaseActivity.DATA_ENTRY_SETTINGS;
import static com.epocal.testsettingsfeature.BaseActivity.GENERAL_TEST_SETTINGS;
import static com.epocal.testsettingsfeature.BaseActivity.RANGES;
import static com.epocal.testsettingsfeature.BaseActivity.UNITS_AND_REPORTABLE_RANGES;

/**
 *
 * Created by Zeeshan A Zakaria on 5/8/2017.
 */

public class TestSettingsFragment extends Fragment {

    LinearLayout mLLGeneralTestSettings,  mLLDataEntrySettings, mLLUnitsAndReportableRanges, mLLRanges;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_settings, group, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLLGeneralTestSettings = (LinearLayout) view.findViewById(R.id.ll_general_test_settings);
        mLLDataEntrySettings = (LinearLayout) view.findViewById(R.id.ll_data_entry_settings);
        mLLUnitsAndReportableRanges = (LinearLayout) view.findViewById(R.id.ll_units_and_reportable_ranges);
        mLLRanges = (LinearLayout) view.findViewById(R.id.ll_ranges);

        mLLGeneralTestSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestSettingsActivity)getActivity()).testSettingsNavigation(GENERAL_TEST_SETTINGS);
            }
        });

        mLLDataEntrySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestSettingsActivity)getActivity()).testSettingsNavigation(DATA_ENTRY_SETTINGS);
            }
        });

        mLLUnitsAndReportableRanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestSettingsActivity)getActivity()).testSettingsNavigation(UNITS_AND_REPORTABLE_RANGES);
            }
        });

        mLLRanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestSettingsActivity)getActivity()).testSettingsNavigation(RANGES);
            }
        });
    }

}