package com.epocal.testhistoryfeature.data.patienttestdata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.data.type.SelectionType;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

import static com.epocal.testhistoryfeature.data.patienttestdata.THDataHelper.MAX_CHAR_LENGTH_SELECTABLETESTSUMMARYVIEWHOLDER;

/**
 * Data Adapter for RecyclerView on Test Selection screen to list Test Records.
 * Each row displays the same data as THDataAdapter with addition of CheckBox in each row.
 */
public class THMultiSelectDataAdapter extends RecyclerView.Adapter<SelectableTestSummaryViewHolder> {
    private Context mContext;
    private List<TestRecordListItem> mDataSource;
    private IMultiSelectStateChangeListener mSelectStateChangeListener;
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
     * @param initialSelectionId -- TestRecord id of initial row selection of the list. value can be
     *                          one of (TestRecord.id,  THDataHelper.SELECT_ALL, THDataHelper.SELECT_NONE)
     * @param selectStateChangeListener -- callback when the row selection state has changed.
     * @param partialStringMatch -- "partialString" (search criteria) for TestRecord search.
     *                           when partialString is null, partial search is not used.
     *
     */
    public THMultiSelectDataAdapter(Context context,
                                    RealmResults<TestRecord> dataSource,
                                    SelectionType selectionType,
                                    long initialSelectionId,
                                    IMultiSelectStateChangeListener selectStateChangeListener,
                                    String partialStringMatch)
    {
        this.mContext = context;
        if (selectionType == SelectionType.SELECT_ALL) {
            this.mDataSource = THDataHelper.createMultiSelectTestRecordListWithPartialStringMatch(dataSource, true, partialStringMatch);
        } else if (selectionType == SelectionType.SELECT_ONE_ITEM) {
            this.mDataSource = THDataHelper.createMultiSelectTestRecordListWithPartialStringMatch(dataSource, initialSelectionId, partialStringMatch);
        } else {
            this.mDataSource = THDataHelper.createMultiSelectTestRecordListWithPartialStringMatch(dataSource, false, partialStringMatch);
        }
        this.mSelectStateChangeListener = selectStateChangeListener;
        this.mPartialStringMatch = partialStringMatch;
    }

    @Override
    public SelectableTestSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_patient_test_checkbox, parent, false);
        return new SelectableTestSummaryViewHolder(view, mContext, mRowSelectCallback);
    }

    @Override
    public void onBindViewHolder(SelectableTestSummaryViewHolder holder, int position) {
        TestRecordListItem item = mDataSource.get(position);
        holder.bind(item, mPartialStringMatch, MAX_CHAR_LENGTH_SELECTABLETESTSUMMARYVIEWHOLDER);
    }

    public List<Long> getCurrentSelectedItemIdList() {
        List<Long> idList = new ArrayList<>();
        for (TestRecordListItem item : mDataSource) {
            if (item.isSelected()) {
                idList.add(item.getId());
            }
        }
        return idList;
    }

    private SelectableTestSummaryViewHolder.RowListener mRowSelectCallback = new SelectableTestSummaryViewHolder.RowListener() {
        @Override
        public void onRowSelected(int index, boolean isChecked) {
            mDataSource.get(index).setSelected(isChecked);
            if (mSelectStateChangeListener != null) {
                mSelectStateChangeListener.onSelectonStateChanged(getCurrentSelectedItemIdList());
            }
        }
    };

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public void selectAll(boolean shouldSelectAll) {
        for (TestRecordListItem item : mDataSource) {
            item.setSelected(shouldSelectAll);
        }
        notifyDataSetChanged();
        if (mSelectStateChangeListener != null) {
            mSelectStateChangeListener.onSelectonStateChanged(getCurrentSelectedItemIdList());
        }
    }
}
