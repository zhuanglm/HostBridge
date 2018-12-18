package com.epocal.testsettingsfeature.dataentrysettings;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.epocal.testsettingsfeature.R;
import com.epocal.testsettingsfeature.TestSettingsActivity;

import static com.epocal.testsettingsfeature.BaseActivity.FIELD_SETTINGS;

/**
 * This class shows the Data Entry Settings screen
 *
 * Created by Zeeshan A Zakaria on 5/8/2017.
 */

public class DataEntrySettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.data_entry_settings, group, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        LinearLayout fieldViewSettings = (LinearLayout) view.findViewById(R.id.ll_field_settings);
        fieldViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestSettingsActivity)getActivity()).testSettingsNavigation(FIELD_SETTINGS);
            }
        });
    }
}