package com.epocal.testsettingsfeature.unitsandreportablerangesscreen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.epocal.testsettingsfeature.R;
import com.epocal.testsettingsfeature.TestSettingsActivity;

import static com.epocal.testsettingsfeature.BaseActivity.BG_PLUS_SETTINGS;
import static com.epocal.testsettingsfeature.BaseActivity.E_PLUS_SETTINGS;
import static com.epocal.testsettingsfeature.BaseActivity.M_PLUS_SETTINGS;

/**
 * This class shows the Units and Reportable Ranges screen
 *
 * Created by Zeeshan A Zakaria on 5/8/2017.
 */

public class UnitsAndReportableRangesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.units_and_reportable_ranges, group, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        LinearLayout llBg = (LinearLayout) view.findViewById(R.id.ll_bg);
        LinearLayout llE = (LinearLayout) view.findViewById(R.id.ll_e);
        LinearLayout llM = (LinearLayout) view.findViewById(R.id.ll_m);

        llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestSettingsActivity)getActivity()).testSettingsNavigation(BG_PLUS_SETTINGS);
            }
        });

        llE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestSettingsActivity)getActivity()).testSettingsNavigation(E_PLUS_SETTINGS);
            }
        });

        llM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestSettingsActivity)getActivity()).testSettingsNavigation(M_PLUS_SETTINGS);
            }
        });
    }
}