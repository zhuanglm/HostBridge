package com.epocal.epoctestprocedure.fragments.dataentry;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.epocal.common.epocobjects.AnalyteOption;
import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.epoctestprocedure.R;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * The adapter
 * <p>
 * Created by Zeeshan A Zakaria on 1/8/2018.
 */

class CustomTestSelectionAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<AnalyteOption> mAnalytes;
    private boolean mIsCalculationDone;
    EnumSet<AnalyteName> mEnumSet;

    CustomTestSelectionAdapter(Context context, ArrayList<AnalyteOption> analytes, boolean customPanelOnly) {
        Log.v("HOST4", "Mi");
        mContext = context;
        mAnalytes = analytes;
        mEnumSet = EnumSet.noneOf(AnalyteName.class);
        populateEnumSet();
        mIsCalculationDone = customPanelOnly;
    }

    @Override
    public int getCount() {
        return mAnalytes.size();
    }

    @Override
    public Object getItem(int position) {
        return mAnalytes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.dialog_fragment_custom_test_selection_item, parent, false);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        AnalyteOption analyteOption = mAnalytes.get(position);
        holder.tvAnalyteName = (TextView) view.findViewById(R.id.text_view_analyte_name);
        holder.cbAnalyte = (CheckBox) view.findViewById(R.id.checkbox_analyte);
        final AnalyteName analyteNameOriginal = analyteOption.getAnalyteName();
        String analyteName = analyteNameOriginal.name();
        holder.tvAnalyteName.setText(analyteName);

        holder.cbAnalyte.setOnClickListener(new View.OnClickListener() {
            // Callback when CheckBox is tapped
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                boolean isChecked = cb.isChecked();
                if (isChecked) {
                    mEnumSet.add(analyteNameOriginal);
                } else {
                    mEnumSet.remove(analyteNameOriginal);
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            // Callback when the row is tapped.
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                CheckBox cb = holder.cbAnalyte;
                boolean isChecked = cb.isChecked();
                // toggle checkbox and update mEnumSet.
                if (isChecked) {
                    cb.setChecked(false);
                    mEnumSet.remove(analyteNameOriginal);
                } else {
                    cb.setChecked(true);
                    mEnumSet.add(analyteNameOriginal);
                }
            }
        });

        // Set the row/checkbox to be clickable by default.
        enableRow(view, holder);

        // mEnumSet - represent a current selection set.
        // EnabledSelected / UnabledUnselected - is the last DB saved selection state of analyte from the business logic
        if (mEnumSet.contains(analyteNameOriginal)) {
            holder.cbAnalyte.setChecked(true);

            // Check the exception case to disable row-click.
            // When an analyte is previously EnabledSelected and calculation is done,
            // then the user cannot select this analyte. DISABLE row in this case.
            EnabledSelectedOptionType type = analyteOption.getOptionType();
            if ((type == EnabledSelectedOptionType.EnabledSelected) && mIsCalculationDone) {
                disableRow(view, holder);
            }

        } else {
            // EnabledUnselected case
            holder.cbAnalyte.setChecked(false);
        }

        return view;
    }

    /**
     * Populate the enum set with the analyte names which are marked as selected
     */
    private void populateEnumSet() {
        for (AnalyteOption analyteOption : mAnalytes) {
            AnalyteName analyteName = analyteOption.getAnalyteName();
            boolean selected = (analyteOption.getOptionType() == EnabledSelectedOptionType.EnabledSelected);
            if (selected) {
                mEnumSet.add(analyteName);
            }
        }
    }

    // Disable "tap" on row and checkbox, and set their appearance to disabled state.
    private void disableRow(View rowView, ViewHolder holderView) {
        rowView.setBackgroundColor(mContext.getResources().getColor(R.color.colorAlereLightGray));
        rowView.setClickable(false);
        holderView.disableCheckBox(mContext);
    }

    // Enable "tap" on row and checkbox, and set thier appearance to enabled state.
    private void enableRow(View rowView, ViewHolder holderView) {
        rowView.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
        rowView.setClickable(true);
        holderView.enableCheckBox(mContext);
    }

    private static class ViewHolder {
        private TextView tvAnalyteName;
        private CheckBox cbAnalyte;

        public void disableCheckBox(Context context) {
            cbAnalyte.setEnabled(false);
            cbAnalyte.setClickable(false);
            cbAnalyte.setButtonTintList(ColorStateList.valueOf((context.getResources().getColor(R.color.colorAlereDarkGray))));
        }

        public void enableCheckBox(Context context) {
            cbAnalyte.setEnabled(true);
            cbAnalyte.setClickable(true);
            cbAnalyte.setButtonTintList(ColorStateList.valueOf((context.getResources().getColor(R.color.primaryBlueNew))));
        }
    }
}