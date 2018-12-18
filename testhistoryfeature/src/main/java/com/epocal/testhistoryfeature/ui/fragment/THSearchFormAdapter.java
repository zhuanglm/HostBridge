package com.epocal.testhistoryfeature.ui.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.epocal.common.CU;
import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.search.THSearchFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This adapter provide the data for the advanced search form.
 *
 * @see THSearchFormDialogFragment
 */
public class THSearchFormAdapter extends RecyclerView.Adapter<THSearchFormAdapter.ViewHolder> {

    private Context mContext;
    private List<FilterItem> mFilters = new ArrayList<>();

    THSearchFormAdapter(Map<String, String> searchFilters, Context context) {
        mContext = context;
        List<String> keys = THSearchFilters.getValidFilterKeys();
        Map<String, String> keyToTitle = createSearchFilterKeyTitle(mContext);
        for (String key : keys) {
            String value = "";
            boolean isChecked = false;
            if (searchFilters.get(key) != null) {
                value = searchFilters.get(key);
                isChecked = true;
            }
            mFilters.add(new FilterItem(isChecked, key, keyToTitle.get(key), value));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_filter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mFilters.size();
    }

    /**
     * Map<String, String> represents a search criteria. First string is a colunm name and second is the value.
     * @return - Returns Map of filter (filter_key, value) pair.
     */
    Map<String, String> getFilters() {
        ArrayMap<String, String> dbFilters = new ArrayMap<>();
        for (FilterItem item : mFilters) {
            if (item.mIsChecked) {
                dbFilters.put(item.mKey, item.mValue);
            }
        }
        return dbFilters;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox mCb;
        private TextView mTitleView;
        private EditText mValueView;

        ViewHolder(View itemView) {
            super(itemView);
            mCb = itemView.findViewById(R.id.checkbox_filter);
            mTitleView = itemView.findViewById(R.id.filter_title);
            mValueView = itemView.findViewById(R.id.filter_value);
        }

        void bind(int position) {
            final FilterItem item = mFilters.get(position);
            mCb.setChecked(item.mIsChecked);
            mTitleView.setText(item.mTitle);
            mValueView.setText(item.mValue);

            mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.mIsChecked = isChecked;
                }
            });

            if (item.mKey.equals(THSearchFilters.TESTDATE_FROM)) {

                mValueView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            showDatePicker(item, mValueView);
                        }
                    }
                });
                mValueView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker(item, mValueView);
                    }
                });
            } else if (item.mKey.equals(THSearchFilters.TESTDATE_TO)) {

                mValueView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            showDatePicker(item, mValueView);
                        }
                    }
                });
                mValueView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker(item, mValueView);
                    }
                });
            } else {
                mValueView.setFocusable(true);
                mValueView.setOnClickListener(null); // cleanup previously binded callback.
                mValueView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            item.mValue = mValueView.getText().toString();
                        }
                    }
                });
            }
        }
    }

    private void showDatePicker(final FilterItem item, final EditText editText) {
        String formattedDateString = item.mValue;

        DatePickerDialogFragment datePickerFragment = new DatePickerDialogFragment();
        // Set the current date if exists
        if (!formattedDateString.isEmpty()) {
            Bundle args = new Bundle();
            args.putString(CU.EPOC_DATE_STRING_KEY, formattedDateString);
            datePickerFragment.setArguments(args);
        }
        datePickerFragment.setCallback(new DatePickerDialogFragment.IDatePickerDialogFragmentCallback() {
            @Override
            public void onDatePickerDialogDismissed(DialogFragment dialog) {
                // Remove the focus
                editText.setFocusable(false);
                editText.setFocusable(true);
            }

            @Override
            public void setDate(String formattedDateString) {
                item.mValue = formattedDateString;
                editText.setText(formattedDateString);
            }
        });
        datePickerFragment.show(((Activity) mContext).getFragmentManager(), "datePicker");
    }

    class FilterItem {
        boolean mIsChecked;
        String  mKey;
        String  mTitle;
        String  mValue;

        FilterItem(boolean isChecked, String colKey, String title, String value) {
            this.mIsChecked = isChecked;
            this.mKey   = colKey;
            this.mTitle = title;
            this.mValue = value;
        }
    }

    ////////////////////
    // Utility methods
    ////////////////////

    /**
     * Create a Map of the database search filter key (in Realm TestRecord column name) to search criteria value.
     * Map<String, String> represents a search criteria. First string is a colunm name and second is the value.
     * @return - Returns Map of default filter (filter_key, value) pair.
     */
    public static Map<String, String> createDefaultSearchFilters() {
        Map<String, String> defaultFilters = new ArrayMap<>();
        List<String> keys = THSearchFilters.getValidFilterKeys();
        for (String key : keys) {
            defaultFilters.put(key, "");
        }
        return defaultFilters;
    }

    /**
     * Create a Map of the database search filter key (in Realm TestRecord column name) to the displayable title string.
     * (e.g) THPatientTestSearchFilter.READERSN "reader.serialNumber" ==> "Reader SN"
     * One exception is TESTDATE_FROM and TESTDATE_TO keys which represent the date range.
     *
     * @param context -- activity context
     * @return Map<String, String> where 1st string is the filter key (in Realm TestRecord column name)
     *         and the 2nd string is matching title text to display to the user.
     */
    static Map<String, String> createSearchFilterKeyTitle(Context context) {
        ArrayMap<String, String> filterKeysToTitles = new ArrayMap<>();

        Resources res = context.getResources();

        // PATIENTID, CARDLOT, READERSN, TESTDATE_FROM, TESTDATE_TO
        filterKeysToTitles.put(THSearchFilters.PATIENTID, res.getString(R.string.patientId));
        filterKeysToTitles.put(THSearchFilters.CARDLOT, res.getString(R.string.cardlot));
        filterKeysToTitles.put(THSearchFilters.READERSN, res.getString(R.string.reader_sn));
        filterKeysToTitles.put(THSearchFilters.TESTDATE_RANGE, res.getString(R.string.test_date_range));
        filterKeysToTitles.put(THSearchFilters.TESTDATE_FROM, res.getString(R.string.test_date_from));
        filterKeysToTitles.put(THSearchFilters.TESTDATE_TO, res.getString(R.string.test_date_to));

        return filterKeysToTitles;
    }
}
