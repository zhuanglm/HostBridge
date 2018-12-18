package com.epocal.testhistoryfeature.data.patientdata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.WorkFlow;
import com.epocal.common_ui.list.IListItem;
import com.epocal.testhistoryfeature.R;

import java.util.List;

/**
 * This class is an list adapter for displaying the patient data in Details Screen Tab 2 -
 * Entered data section.
 */
public class PatientTestInputDataAdapter extends RecyclerView.Adapter<PatientTestInputDataAdapter.ViewHolder> {
    private static final String TAG = PatientTestInputDataAdapter.class.getSimpleName();
    private static final int VIEWTYPE_SECTION_HEADER = R.layout.list_item_test_input_data_header;
    private static final int VIEWTYPE_ITEM           = R.layout.list_item_test_input_data;
    private final List<IListItem<String>> mData;

    public PatientTestInputDataAdapter(Context context, TestRecord testRecord, WorkFlow workFlow) {
        mData = new PatientTestInputDataList(context, testRecord, workFlow).getList();
        Log.v(TAG, mData.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        IListItem item = mData.get(position);
        if (item.isSectionHeader()) {
            return VIEWTYPE_SECTION_HEADER;
        } else {
            return VIEWTYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mViewTitle;
        private final TextView mViewValue;

        ViewHolder(View itemView) {
            super(itemView);
            mViewTitle = itemView.findViewById(R.id.tv_title);
            mViewValue = itemView.findViewById(R.id.tv_value);
        }

        void bind(int position) {
            IListItem<String> item = mData.get(position);

            mViewTitle.setText(item.getTitle());

            if (!item.isSectionHeader()) {
                mViewValue.setText(item.getValue());
            }
        }
    }
}
