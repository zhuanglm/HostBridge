package com.epocal.testhistoryfeature.data.patienttestdata;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.epocal.testhistoryfeature.R;

/**
 * This class extends TestSummaryViewHolder with additional row select attribute to help
 * THMultiSelectDataAdapter and THMultiSelectGroupedDataAdapter in Test Selection screen.
 */
public class SelectableTestSummaryViewHolder extends TestSummaryViewHolder implements CompoundButton.OnCheckedChangeListener {
    private CheckBox mCheckBox;
    private RowListener mCallback;

    public interface RowListener {
        void onRowSelected(int index, boolean isChecked);
    }

    private SelectableTestSummaryViewHolder(View itemView, Context context) {
        super(itemView, context);
        mCheckBox = itemView.findViewById(R.id.checkbox);
        mCheckBox.setOnCheckedChangeListener(this);
    }

    SelectableTestSummaryViewHolder(View itemView, Context context, RowListener listener) {
        this(itemView, context);
        mCallback = listener;
    }

    public void bind(TestRecordListItem item) {
        super.bind(item.getTestRecord());
        mCheckBox.setChecked(item.isSelected());
    }

    public void bind(TestRecordListItem item, String stringToMatch, int maxCharLength) {
        super.bind(item.getTestRecord(), stringToMatch, maxCharLength);
        mCheckBox.setChecked(item.isSelected());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int rowIndex = getAdapterPosition(); // get row index
        if (mCallback != null) {
            mCallback.onRowSelected(rowIndex, isChecked);
        }
    }
}