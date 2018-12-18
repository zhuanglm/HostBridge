package com.epocal.common_ui.qaresults;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epocal.common_ui.R;

import java.util.List;

public class SimpleTestGridViewAdapter extends BaseAdapter {
    Context mContext;
    List<SimpleTest> mDataList;

    public SimpleTestGridViewAdapter(Context context, List<SimpleTest> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Check the overall status of the test results.
     *
     * @return
     *   true -- only if all test results are in passed state.
     *   false - if at least one test result is in failed state.
     *   null -- if dataList is null or its size is 0.
     */
    public Boolean hasFailedTest() {
        Boolean hasFailedTest = null;
        if ((mDataList != null) && (mDataList.size()>0)) {
            for (SimpleTest result : mDataList) {
                if (!result.passed()) {
                    hasFailedTest = true;
                    break;
                }
            }
            hasFailedTest = false;
        }
        return hasFailedTest;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.grid_item_qa_test_result, parent, false);
        ImageView imageView = view.findViewById(R.id.status_icon);
        TextView textView = view.findViewById(R.id.tv_title);

        SimpleTest simpleTest = mDataList.get(position);
        if (simpleTest.passed()) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.colorForestGreen));
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_white_24dp, null));
        } else {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.colorDarkRed));
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_clear_white_24dp, null));
        }
        textView.setText(simpleTest.getName());
        return view;
    }
}