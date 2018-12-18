package com.epocal.testsettingsfeature.unitsandreportablerangesscreen.EPlus;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.epocal.common.realmentities.Analyte;
import com.epocal.testsettingsfeature.R;

import java.util.ArrayList;

/**
 * The View class for E+
 *
 * Created by Zeeshan A Zakaria on 8/2/2017.
 */

public class EPlusFragment extends Fragment {

    EPlusPresenter mEPlusPresenter;
    EPlusListAdapter mEPlusListAdapter;
    ArrayList<Analyte> mAnalytes;
    ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_e_settings_list, group, false);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mEPlusPresenter = new EPlusPresenter();

    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mAnalytes = mEPlusPresenter.getAnalytes();
        mListView = (ListView) view.findViewById(R.id.listview_e_analytes);

        mEPlusListAdapter = new EPlusListAdapter(getActivity(), mAnalytes, this);
        mListView.setAdapter(mEPlusListAdapter);
    }

    /**
     * When the data is changed in the adapter, re-read the analytes and update the adapter.
     */
    public void updateAnalytes() {
        mEPlusPresenter.setupAnalytes();
        mAnalytes = mEPlusPresenter.getAnalytes();
        mEPlusListAdapter.updateAnalytes(mAnalytes);
    }
}
