package com.epocal.epoctestprocedure;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.epocal.epoctest.uidriver.TestUIDriver;

import java.util.ArrayList;

public class TestListAdapter  extends BaseAdapter {

    private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();

    IMultiScreenTestPresenter mPresenter;
    public TestListAdapter(IMultiScreenTestPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public int getCount() {
        return mPresenter.getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return mPresenter.getItemByPosition(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mPresenter.getViewContext()); //.from(mContext);
            view = layoutInflater.inflate(R.layout.test_list_item, parent, false);
        }
        final TestUIDriver test = mPresenter.getItemByPosition(position); //mTests.get(position);

        TextView tvTestName = (TextView) view.findViewById(R.id.text_view_test_name);
        TextView tvTestStatus = view.findViewById(R.id.text_view_test_status);

        String readerName = test.getReaderDevice().getDeviceName();
        String readerStatus = "";
        if (test.getStatusErrorType() != null) {
            readerStatus = test.getStatusErrorType().toString();
        } else {
        if (test.getStatusEventType() != null)
            readerStatus = test.getStatusEventType().toString();
        }
        tvTestName.setText(readerName);
        tvTestStatus.setText(readerStatus);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.activateTest(test);
            }
        });

        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyDataSetChanged(){
        for (DataSetObserver observer: observers) {
            observer.onChanged();
        }
    }
}
