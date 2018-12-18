package com.epocal.testhistoryfeature.data.patienttestdata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common_ui.list.IListItem;
import com.epocal.common_ui.list.ISelectableListItem;
import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.data.type.SelectionType;
import com.epocal.testhistoryfeature.data.type.THDateRange;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Data Adapter for RecyclerView on Test Selection screen to list Test Records.
 * Each row displays the same data as THGroupedDataAdapter with addition of CheckBox in each row.
 *
 * <h4>Important Note:</h4>
 * <p>
 * The change in the selection state of the header row affects the selection state of all child rows.
 * Each time the header row selection state toggles (by user action -- the code determines this case
 * by comparing Model object's selection state and the current CheckBox state. If they DO NOT match,
 * then concludes the user has changed it. Note that on the initial rendering of the list row,
 * each row's Model object's selection state == row's CheckBox state.
 * Thus when row's Model object selection state != header row's CheckBox state
 * -- then iterate all child rows to update its selection state (in both Model and CheckBox View)
 * to match with the header state.
 * </p>
 * <p>
 * Likewise, the change in the selection state of the child row can affects the selection state of
 * its header row.
 * <ul>
 *     <li><b>Case 1</b> - Initially no rows in the group were selected. Then the user selects
 *     one child row. --> The header row needs to update from "unselected" to "minus" icon
 *     to show partial group selection.</li>
 *     <li><b>Case 2</b> - Initially all child rows in the group were selected. Then the user
 *     unselects one child row. --> The header row needs to update from "checked" icon
 *     to "minus" icon to show partial group selection.</li>
 *     <li><b>Case 3 - Last child unselected</b> - Initially only one child row in the group was
 *     selected. Then the user unselect this last child. --> The header row needs to update from
 *     "minus" to "unselected" icon.</li>
 *     <li><b>Case 4 - Last child selected</b> - Initially in the group, all except one child
 *     was selected. Then the user select this last child (all children selected state)
 *     --> The header row needs to update from "minus" to "checked" icon.</li>
 * </ul>
 * </p>
 */
public class THMultiSelectGroupedDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEWTYPE_MULTISELECT_TESTSUMMARY = R.layout.list_item_patient_test_checkbox;
    private static final int VIEWTYPE_MULTISELECT_HEADER = R.layout.list_item_patient_test_header_checkbox;
    private Context mContext; // Use by inner class ViewHolder (TestSummaryViewHolder)
    private List<ISelectableListItem> mGroupedDataSource;
    private IMultiSelectStateChangeListener mSelectStateChangeListener;
    private List<Integer> mHeaderRowIndexes;

    public THMultiSelectGroupedDataAdapter(
            Context context,
            RealmResults<TestRecord> dataSource,
            SelectionType initialSelectionType,
            long initialSelectedId,
            THDateRange initialSelectedDateRange,
            IMultiSelectStateChangeListener listener)
    {
        this.mContext = context;
        mHeaderRowIndexes = new ArrayList<>();
        this.mGroupedDataSource = THDataHelper.groupByDate(mContext, dataSource, mHeaderRowIndexes, initialSelectionType, initialSelectedId, initialSelectedDateRange);
        this.mSelectStateChangeListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(viewType, parent, false);

        if (viewType == VIEWTYPE_MULTISELECT_TESTSUMMARY) {
            return new SelectableTestSummaryViewHolder(view, mContext, mRowSelectCallback);
        } else if (viewType == VIEWTYPE_MULTISELECT_HEADER) {
            return new SelectableHeaderVH(view, mHeaderRowSelectCallback);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IListItem item = mGroupedDataSource.get(position);
        if (holder instanceof SelectableHeaderVH) {
            SelectableHeaderVH selectableVH = (SelectableHeaderVH) holder;
            updateChildSelectionStateAtHeaderPosition(position);
            selectableVH.bind((TestRecordListItemHeader)item);
        } else {
            TestRecordListItem listItem = (TestRecordListItem) item;
            ((SelectableTestSummaryViewHolder) holder).bind(listItem);
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
            return VIEWTYPE_MULTISELECT_HEADER;
        } else {
            return VIEWTYPE_MULTISELECT_TESTSUMMARY;
        }
    }

    // Only count items and skip header
    public List<Long> getCurrentSelectedItemIdList() {
        List<Long> idList = new ArrayList<>();
        for (ISelectableListItem item : mGroupedDataSource) {
            if (item.isSectionHeader()) continue;
            if (item.isSelected()) {
                if (item instanceof TestRecordListItem) {
                    TestRecord testRecord = ((TestRecordListItem) item).getTestRecord();
                    idList.add(testRecord.getId());
                }
            }
        }
        return idList;
    }

    /**
     * Determine the ChildSelectionState of Header at index position.
     * if there is a state change, return true. else otherwise.
     * @param headerIndex -- adapter index position of HeaderItem
     */
    private boolean updateChildSelectionStateAtHeaderPosition(int headerIndex) {
        // Check for bound error.
        if ( (headerIndex < 0) || (headerIndex >= mGroupedDataSource.size()) ) return false;

        int selectedChildItemCount = 0;
        TestRecordListItemHeader headerItem = (TestRecordListItemHeader) mGroupedDataSource.get(headerIndex);
        for (int childIndex = 0; childIndex < headerItem.getNumberOfChildren(); childIndex++) {
            int itemIndex = (headerIndex+1) + childIndex;
            if (mGroupedDataSource.get(itemIndex).isSelected()) {
                selectedChildItemCount++;
            }
        }

        // NOTE: headerItem's selected state may or may not match
        // the newly determined HeaderRowState.
        // Update the selected state to match HeaderRowState.
        TestRecordListItemHeader.ChildSelectionState newState;
        if (selectedChildItemCount == 0) {
            newState = TestRecordListItemHeader.ChildSelectionState.NONE;
        } else if (selectedChildItemCount == headerItem.getNumberOfChildren()) {
            newState = TestRecordListItemHeader.ChildSelectionState.ALL;
        } else {
            newState = TestRecordListItemHeader.ChildSelectionState.PARTIAL;
        }

        if (headerItem.getChildSelectionState() != newState) {
            // state has changed.
            headerItem.setChildSelectionState(newState);
            headerItem.setSelected(newState == TestRecordListItemHeader.ChildSelectionState.ALL);
            return true; // header state was updated.
        }
        return false; // header state was not updated.
    }

    private int getHeaderRowIndex(int childRowIndex) {
        // return HeaderRow index matching given childRow index
        for (Integer headerIndex : mHeaderRowIndexes) {
            TestRecordListItemHeader headerItem = (TestRecordListItemHeader) mGroupedDataSource.get(headerIndex);
            if ((childRowIndex > headerIndex) && (childRowIndex <= headerIndex+1+headerItem.getNumberOfChildren()))
            {
                return headerIndex;
            }
        }
        return -1; // Error if you reach here. childRowIndex out of bounds.
    }

    private SelectableTestSummaryViewHolder.RowListener mRowSelectCallback = new SelectableTestSummaryViewHolder.RowListener() {
        @Override
        public void onRowSelected(int index, boolean isChecked) {
            TestRecordListItem testRecordListItem = (TestRecordListItem) mGroupedDataSource.get(index);
            // 1. - trigger the code only if the checkbox state is changed compared with model's state (toggled)
            if (testRecordListItem.isSelected() != isChecked) {
                mGroupedDataSource.get(index).setSelected(isChecked); // update childRow's selected state.
                // 2. - checkbox state change could affect the header row state (none, all or partial select).
                // 2.1. - find HeaderRow index for a given childRow
                int headerRowIndex = getHeaderRowIndex(index);
                if (headerRowIndex != -1) {
                    // 2.2. HeaderRow found. Now check if the HeaderRow state has been updated or not.
                    if (updateChildSelectionStateAtHeaderPosition(headerRowIndex)) {
                        // 2.3. HeaderRow updated. Trigger ListView refresh.
                        notifyDataSetChanged();
                    }
                }
                // 3. - callback
                if (mSelectStateChangeListener != null) {
                    mSelectStateChangeListener.onSelectonStateChanged(getCurrentSelectedItemIdList());
                }
            }
        }
    };

    private SelectableHeaderVH.HeaderRowListener mHeaderRowSelectCallback = new SelectableHeaderVH.HeaderRowListener() {
        @Override
        public void onHeaderRowSelected(int index, boolean isChecked) {
            // 1. - trigger the code only if the checkbox state is changed compared with model's state (toggled)
            TestRecordListItemHeader headerItem = (TestRecordListItemHeader) mGroupedDataSource.get(index); // Get data at position
            if (headerItem.isSelected() != isChecked) {
                // 2. - update the model data to match with checkbox state.
                headerItem.setSelected(isChecked);
                for (int i = index + 1; i < mGroupedDataSource.size(); i++) {
                    // 3. - update the model: set the state of all child items until the next header
                    ISelectableListItem nextItem = mGroupedDataSource.get(i);
                    if (nextItem.isSectionHeader()) break; // if nextItem is header, we are done!
                    nextItem.setSelected(isChecked); // set the state for all child items until the next header.
                }
                // 4. - When the header row updates, trigger ListView refresh to redraw child rows
                notifyDataSetChanged();

                // 5. - Callback the parent.
                if (mSelectStateChangeListener != null) {
                    mSelectStateChangeListener.onSelectonStateChanged(getCurrentSelectedItemIdList());
                }
            }
        }
    };

    public void selectAll(boolean shouldSelectAll) {
        for (ISelectableListItem item : mGroupedDataSource) {
            item.setSelected(shouldSelectAll);
        }
        notifyDataSetChanged();
        if (mSelectStateChangeListener != null) {
            mSelectStateChangeListener.onSelectonStateChanged(getCurrentSelectedItemIdList());
        }
    }

    /**
     * ViewHolder to display the Header row in Multi-Select PatientTest ListView.
     */
    public static class SelectableHeaderVH extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener
    {
        // NOTE: ALL / NONE / PARTIAL are the 3 states of Header Row child selection state.
        // The appearance of the Header Row changes depending on its state.
        //       - ALL = all child items are in selected state. (CheckBox shows checkmark.)
        //       - NONE = none of child items are in selected state. (CheckBox shows no checkmark.)
        //       - PARTIAL = one or more child items are in selected state. (ImageView shows Indeterminate state icon.)
        private TextView mTvTitle;
        private CheckBox mCheckBox;
        private ImageView mPartiallySelectedImage;
        private HeaderRowListener mCallback;

        public interface HeaderRowListener {
            void onHeaderRowSelected(int index, boolean isChecked);
        }

        SelectableHeaderVH(View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mCheckBox = itemView.findViewById(R.id.checkbox);
            mPartiallySelectedImage = itemView.findViewById(R.id.cb_indeterminate_image);
            mCheckBox.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);
        }

        SelectableHeaderVH(View itemView, HeaderRowListener listener) {
            this(itemView);
            mCallback = listener;
        }

        void bind(TestRecordListItemHeader headerItem) {
            mTvTitle.setText(headerItem.getTitle());
            mCheckBox.setChecked(headerItem.isSelected());
            if (headerItem.getChildSelectionState() == TestRecordListItemHeader.ChildSelectionState.PARTIAL) {
                mCheckBox.setVisibility(View.INVISIBLE);
                mPartiallySelectedImage.setVisibility(View.VISIBLE);
            } else {
                mCheckBox.setVisibility(View.VISIBLE);
                mPartiallySelectedImage.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int rowIndex = getAdapterPosition(); // get row index
            if (mCallback != null) {
                mCallback.onHeaderRowSelected(rowIndex, isChecked);
            }
        }

        // NOTE: On Row Click, Below is called only when CheckBox is INVISIBLE.
        // NOTE: When CheckBox is VISIBLE, it covers over the row and above onCheckedChanged() is call on Click.
        @Override
        public void onClick(View v) {
            ImageView imageView = itemView.findViewById(R.id.cb_indeterminate_image);
            imageView.setVisibility(View.INVISIBLE);
            CheckBox cb = v.findViewById(R.id.checkbox);
            cb.setVisibility(View.VISIBLE);
            cb.toggle(); // trigger onCheckedChanged method().
        }

    }

}
