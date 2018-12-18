package com.epocal.testsettingsfeature.unitsandreportablerangesscreen.BGPlus;

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
 * The View class for BG+
 *
 * Created by Zeeshan A Zakaria on 8/2/2017.
 */

public class BGPlusFragment extends Fragment {

    BGPlusPresenter mBGPlusPresenter;
    BGPlusListAdapter mBGPlusListAdapter;
    ArrayList<Analyte> mAnalytes;
    ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_bgp_settings_list, group, false);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mBGPlusPresenter = new BGPlusPresenter();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mAnalytes = mBGPlusPresenter.getAnalytes();
        mListView = (ListView) view.findViewById(R.id.listview_bgp_analytes);

        mBGPlusListAdapter = new BGPlusListAdapter(getActivity(), mAnalytes, this);
        mListView.setAdapter(mBGPlusListAdapter);
    }

    /**
     * When the data is changed in the adapter, re-read the analytes and update the adapter.
     */
    public void updateAnalytes() {
        mBGPlusPresenter.setupAnalytes();
        mAnalytes = mBGPlusPresenter.getAnalytes();
        mBGPlusListAdapter.updateAnalytes(mAnalytes);
    }
}
