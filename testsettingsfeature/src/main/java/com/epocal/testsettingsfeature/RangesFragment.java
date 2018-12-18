package com.epocal.testsettingsfeature;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This class shows the Units and Reportable Ranges screen
 *
 * Created by Zeeshan A Zakaria on 5/8/2017.
 */

public class RangesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.ranges, group, false);
    }
}