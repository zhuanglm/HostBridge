package com.epocal.testhistoryfeature.data.patienttestdata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common_ui.list.IListItem;
import com.epocal.testhistoryfeature.R;

import java.util.List;

import io.realm.RealmResults;


/**
 * Data Adapter for RecyclerView on Main screen to list Test Records grouped by date range.
 * The row display is the same as THDataAdapter with addition of grouping the rows by date.
 * The header row indicates the date range (e.g) Today, Yesterday ...
 */
public class THGroupedDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEWTYPE_TESTSUMMARY = R.layout.list_item_patient_test;
    private static final int VIEWTYPE_HEADER      = R.layout.list_item_patient_test_header;
    private Context mContext; // Use by inner class ViewHolder (TestSummaryViewHolder)
    private List<IListItem<String>> mGroupedDataSource;
    private ITestRecordListItemClickListener mListItemClickListener;
    private ITestRecordListItemHeaderClickListener mListItemGroupHeaderClickListener;

    public THGroupedDataAdapter(Context context,
                                RealmResults<TestRecord> dataSource,
                                ITestRecordListItemClickListener listItemClickListener,
                                ITestRecordListItemHeaderClickListener headerClickListener) {
        this.mContext = context;
        this.mGroupedDataSource = THDataHelper.groupByDate(mContext, dataSource);
        this.mListItemClickListener = listItemClickListener;
        this.mListItemGroupHeaderClickListener = headerClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(viewType, parent, false);

        if (viewType == VIEWTYPE_TESTSUMMARY) {
            return new THViewHolder(view, mContext);
        } else if (viewType == VIEWTYPE_HEADER) {
            return new HeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IListItem<String> item = mGroupedDataSource.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind(item.getTitle());
        } else {
            TestRecord record = ((TestRecordListItem) item).getTestRecord();
            ((TestSummaryViewHolder) holder).bind(record);
        }
    }

    @Override
    public int getItemCount() {
        return mGroupedDataSource.size();
    }

    @Override
    public int getItemViewType(int position) {
        IListItem item = mGroupedDataSource.get(position);
        if (item.isSectionHeader()) {
            return VIEWTYPE_HEADER;
        } else {
            return VIEWTYPE_TESTSUMMARY;
        }
    }

    class THViewHolder extends TestSummaryViewHolder implements View.OnLongClickListener, View.OnClickListener {

        THViewHolder(View itemView, Context context) {
            super(itemView, context);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            IListItem item = mGroupedDataSource.get(getAdapterPosition());
            if (item instanceof TestRecordListItem) {
                TestRecord record = ((TestRecordListItem) item).getTestRecord();
                if (mListItemClickListener != null) {
                    mListItemClickListener.onClickListItem(record);
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mListItemClickListener != null) {
                IListItem item = mGroupedDataSource.get(getAdapterPosition());
                if (item instanceof TestRecordListItem) {
                    TestRecord record = ((TestRecordListItem) item).getTestRecord();
                    mListItemClickListener.onLongClickListItem(record);
                }
            }
            return true;
        }
    }

    /**
     * ViewHolder to display the Header row in PatientTest ListView.
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView mTvTitle;

        HeaderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            mTvTitle = itemView.findViewById(R.id.tv_title);
        }

        void bind(String title) {
            mTvTitle.setText(title);
        }

        @Override
        public boolean onLongClick(View v) {
            if (mListItemGroupHeaderClickListener != null) {
                IListItem item = mGroupedDataSource.get(getAdapterPosition());
                if (item instanceof TestRecordListItemHeader) {
                    int dateRangeValue = ((TestRecordListItemHeader) item).getDateRangeValue();
                    mListItemGroupHeaderClickListener.onLongClickHeaderListItem(dateRangeValue);
                }
            }
            return true;
        }
    }
}
