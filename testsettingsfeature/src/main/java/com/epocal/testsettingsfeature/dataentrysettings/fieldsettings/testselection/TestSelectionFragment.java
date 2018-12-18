package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.testselection;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.epocal.common.types.am.AnalyteName;
import com.epocal.testsettingsfeature.R;

import java.util.ArrayList;

/**
 * The TestSelectionModel fragment class
 *
 * Created by Zeeshan A Zakaria on 8/28/2017.
 */

public class TestSelectionFragment extends Fragment implements ITestSelectionView {

    TestSelectionPresenter mTestSelectionPresenter;
    TestSelectionModel mTestSelection;
    ExpandableListView mExpListView;
    Runnable mRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_test_selection, group, false);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mTestSelectionPresenter = new TestSelectionPresenter();
        mTestSelection = new TestSelectionModel(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mExpListView = (ExpandableListView) view.findViewById(R.id.list_vew_expandable_test_selection);

        mExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.w("", "Group clicked: " + id);
                return false;
            }
        });

        mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "You picked : menu=" + groupPosition + " submenu=" + childPosition, Snackbar.LENGTH_SHORT).show();
//                boolean isClickHandled = mTestSelectionPresenter.itemClicked((int) id);
//                if (!isClickHandled) {
//                    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.llContent), "You picked : menu=" + groupPosition + " submenu=" + childPosition, Snackbar.LENGTH_SHORT).show();
//                }
                return true;
            }
        });

        final TestSelectionMenuAdapter newAdapter = new TestSelectionMenuAdapter(getActivity(), mTestSelection, this);
        mExpListView.setAdapter(newAdapter);
//        mExpListView.expandGroup(0);
//        mExpListView.expandGroup(1);
//        mExpListView.expandGroup(2);
//        mExpListView.expandGroup(3);

        mRunnable = new Runnable() {
            public void run() {
                newAdapter.updateFields();
            }
        };
    }

    @Override
    public void switchEnabledState(AnalyteName analyteName, boolean isChecked) {
        boolean valueUpdated = mTestSelectionPresenter.switchEnabledState(analyteName, isChecked);
        if (valueUpdated) {
            getActivity().runOnUiThread(mRunnable);
        }
    }

    @Override
    public void switchSelectedState(AnalyteName analyteName, boolean isChecked) {
        boolean valueUpdated = mTestSelectionPresenter.switchSelectedState(analyteName, isChecked);
        if (valueUpdated) {
            getActivity().runOnUiThread(mRunnable);
        }
    }

    @Override
    public void switchEnabledState(ArrayList<AnalyteName> group, boolean isChecked) {
        boolean valueUpdated = mTestSelectionPresenter.switchEnabledState(group, isChecked);
        if (valueUpdated) {
            getActivity().runOnUiThread(mRunnable);
        }
    }

    @Override
    public void switchSelectedState(ArrayList<AnalyteName> group, boolean isChecked) {
        boolean valueUpdated = mTestSelectionPresenter.switchSelectedState(group, isChecked);
        if (valueUpdated) {
            getActivity().runOnUiThread(mRunnable);
        }

    }
}