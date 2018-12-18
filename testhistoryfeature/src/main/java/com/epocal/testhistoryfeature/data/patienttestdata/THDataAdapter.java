package com.epocal.testhistoryfeature.data.patienttestdata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.testhistoryfeature.R;
import java.util.List;

import io.realm.RealmResults;

import static com.epocal.testhistoryfeature.data.patienttestdata.THDataHelper.MAX_CHAR_LENGTH_TESTSUMMARYVIEWHOLDER;

/**
 * Data Adapter for RecyclerView on Main screen to list Test Records.
 * It is displayed when SearchView on ActionBar is open.
 * Search term entered in SearchView will be highlighted (yellow) in each row.
 * It holds the list of TestRecord objects.
 *
 * Implementation Note:
 * Row 0 is the Header row to display the number of search results.
 * Row 1 - until the end of the list displays the TestRecords.
 */
public class THDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEWTYPE_TESTSUMMARY = R.layout.list_item_patient_test;
    private static final int VIEWTYPE_SEARCHRESULT_HEADER = R.layout.list_item_patient_test_searchresult_header;
    private Context mContext;
    private List<TestRecordListItem> mListDataSource;
    private ITestRecordListItemClickListener mListItemClickListener;
    private String mPartialStringMatch;

    /**
     * Use this constructor to highlight the field text in each row that
     * matches the partialString.
     * If partialStringMatch is not null, then the dataSource contains the item are searched by
     * partial string match to the given string in the following fields: Operator ID, Reader S/N
     * or Card Lot.
     *
     * @param context -- activity context
     * @param dataSource -- search results for searching for "partialString" in Operator ID, Reader S/N, or Card Lot
     *                   of TestRecord.
     * @param listItemClickListener -- callback when the row is clicked.
     * @param partialStringMatch -- "partialString" (search criteria) for TestRecord search.
     *                           when partialString is null, partial search is not used.
     */
    public THDataAdapter(Context context,
                         RealmResults<TestRecord> dataSource,
                         ITestRecordListItemClickListener listItemClickListener,
                         String partialStringMatch)
    {
        this.mContext = context;
        this.mListItemClickListener = listItemClickListener;
        this.mListDataSource = THDataHelper.createTestRecordListWithPartialStringMatch(dataSource, partialStringMatch);
        this.mPartialStringMatch = partialStringMatch;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(viewType, parent, false);

        if (viewType == VIEWTYPE_TESTSUMMARY) {
            return new THViewHolder(view, mContext);
        } else if (viewType == VIEWTYPE_SEARCHRESULT_HEADER) {
            return new SearchResultHeaderViewHolder(view, mContext);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            // Header Row
            ((SearchResultHeaderViewHolder)holder).bind(mListDataSource.size());
        } else {
            // Data Row
            ((THViewHolder)holder).bind(getItem(position).getTestRecord(), mPartialStringMatch, MAX_CHAR_LENGTH_TESTSUMMARYVIEWHOLDER);
        }
    }

    @Override
    public int getItemCount() {
        return mListDataSource.size() + 1;
    }

    /**
     * Returns the index to mListDataSource corresponds to row position.
     *
     * @param position - row index of listview
     * @return index to mListDataSource which is shifted by 1 (subtract 1) due to header row on
     * recyclerview. When position 0 is a header row and thus, there is no matching mListDataSource.
     * It returns -1 to indicate error.
     */
    private int getDataIndexForRowPosition(int position) {
        return (position == 0) ? -1 : position - 1;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    /**
     * Get Data Item corresponds to row position.
     * NOTE: Data item index is shift by one due to the header row added to this list.
     *
     * @param position -- row index
     * @return TestRecord (a data) corresponds to row index.
     */
    private TestRecordListItem getItem(int position) {
        return mListDataSource.get(getDataIndexForRowPosition(position));
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEWTYPE_SEARCHRESULT_HEADER : VIEWTYPE_TESTSUMMARY;
    }

    class THViewHolder extends TestSummaryViewHolder implements View.OnLongClickListener, View.OnClickListener {

        THViewHolder(View itemView, Context context) {
            super(itemView, context);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            if (mListItemClickListener != null) {
                mListItemClickListener.onClickListItem(getItem(index).getTestRecord());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int index = getAdapterPosition();
            if (mListItemClickListener != null) {
                mListItemClickListener.onLongClickListItem(getItem(index).getTestRecord());
            }
            return true;
        }
    }

    /**
     * ViewHolder to display the Header row in PatientTest ListView.
     */
    public static class SearchResultHeaderViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private TextView mTvTitle;

        SearchResultHeaderViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            mTvTitle = itemView.findViewById(R.id.tv_title);
        }

        void bind(int dataSize) {
            mTvTitle.setText(String.format(mContext.getResources().getString(R.string.search_result_header), dataSize));
        }
    }

}
