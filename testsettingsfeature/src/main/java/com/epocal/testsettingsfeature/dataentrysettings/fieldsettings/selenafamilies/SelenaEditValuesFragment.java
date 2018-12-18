package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.selenafamilies;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.epocal.common.types.SelenaFamilyType;
import com.epocal.testsettingsfeature.R;

/**
 * The SelenaEditValuesModel fragment class
 * <p>
 * Created by Zeeshan A Zakaria on 9/08/2017.
 */

public class SelenaEditValuesFragment extends Fragment implements ISelenaEditValuesView {

    SelenaEditValuesPresenter mEditValuesPresenter;
    ListView mListView;
    Runnable mRunnable;
    SelenaFamilyType mSelenaFamilyType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_selena_edit_values, group, false);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        int value = 1;
        bundle = getArguments();
        if (bundle != null) {
            value = bundle.getInt("selenaFamilyTypeValue", 1);
        }


        mSelenaFamilyType = SelenaFamilyType.fromInt(value);
        mEditValuesPresenter = new SelenaEditValuesPresenter();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mListView = (ListView) view.findViewById(R.id.list_vew);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "You picked : menu=" + groupPosition + " submenu=" + childPosition, Snackbar.LENGTH_SHORT).show();
//                boolean isClickHandled = mEditValuesPresenter.itemClicked((int) id);
//                if (!isClickHandled) {
//                    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "You picked : menu=" + groupPosition + " submenu=" + childPosition, Snackbar.LENGTH_SHORT).show();
//                }
            }
        });

        final SelenaEditValuesMenuAdapter selenaEditValuesMenuAdapter = new SelenaEditValuesMenuAdapter(getActivity(), mSelenaFamilyType, this);
        mListView.setAdapter(selenaEditValuesMenuAdapter);

        mRunnable = new Runnable() {
            public void run() {
                selenaEditValuesMenuAdapter.updateFields();
            }
        };
    }

    @Override
    public void switchEnabledState(String itemName, SelenaFamilyType selenaFamilyType, boolean isChecked) {
        boolean valueUpdated = mEditValuesPresenter.switchEnabledState(itemName, selenaFamilyType, isChecked);
        if (valueUpdated) {
            getActivity().runOnUiThread(mRunnable);
        }
    }

    @Override
    public void switchSelectedState(String itemName, SelenaFamilyType selenaFamilyType, boolean isChecked) {
        boolean valueUpdated = mEditValuesPresenter.switchSelectedState(itemName, selenaFamilyType, isChecked);
        if (valueUpdated) {
            getActivity().runOnUiThread(mRunnable);
        }
    }
}