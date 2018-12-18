package com.epocal.testhistoryfeature.data.qadata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common_ui.list.IListItem;
import com.epocal.testhistoryfeature.R;

import java.util.List;

/**
 * This class is an list adapter for displaying the qa test data in Details Screen Tab 2 -
 * Entered data section.
 */
public class QATestInputDataAdapter extends RecyclerView.Adapter<QATestInputDataAdapter.ViewHolder> {
    private static final String TAG = QATestInputDataAdapter.class.getSimpleName();
    private static final int VIEWTYPE_SECTION_HEADER = R.layout.list_item_test_input_data_header;
    private static final int VIEWTYPE_ITEM           = R.layout.list_item_test_input_data;
    private List<IListItem<String>> mData;

    public QATestInputDataAdapter(Context context, TestRecord testRecord) {
        mData = new QATestInputDataList(context, testRecord).getList();
        Log.v(TAG, mData.toString());
    }

    @Override
    public QATestInputDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(viewType, parent, false);
        return new QATestInputDataAdapter.ViewHolder(view);
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
    public void onBindViewHolder(QATestInputDataAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mViewTitle;
        private TextView mViewValue;

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
