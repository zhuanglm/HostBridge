package com.epocal.testsettingsfeature.unitsandreportablerangesscreen.BGPlus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.types.am.Units;
import com.epocal.datamanager.AnalyteModel;
import com.epocal.testsettingsfeature.R;

import java.util.ArrayList;

/**
 * The list view which displays the analytes and allows to change their ranges and units.
 * <p>
 * Created by Zeeshan A Zakaria on 8/3/2017.
 */

class BGPlusListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Analyte> mAnalytes;
    private TextView mAnalyteName, mAnalyteUnit, mReportableHigh, mReportableLow;
    private BGPlusFragment mBGPlusFragment;

    BGPlusListAdapter(Context context, ArrayList<Analyte> analytes, BGPlusFragment bgPlusFragment) {
        super();
        mContext = context;
        mAnalytes = analytes;
        mBGPlusFragment = bgPlusFragment;
    }

    /**
     * This method is called to update the listview to reflect the updates in the adapter.
     *
     * @param updatedAnalytes the updated set of analytes
     */
    void updateAnalytes(ArrayList<Analyte> updatedAnalytes) {
        mAnalytes = updatedAnalytes;
        notifyDataSetChanged();
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
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.adapter_bgp_settings_list_item, parent, false);
        }

        initializeElements(view);
        setValues(position);
        setClickables(position);

        return view;
    }

    private void initializeElements(View view) {
        mAnalyteName = (TextView) view.findViewById(R.id.textView_analyte_name);
        mAnalyteUnit = (TextView) view.findViewById(R.id.textView_analyte_unit);
        mReportableHigh = (TextView) view.findViewById(R.id.textView_reportable_high);
        mReportableLow = (TextView) view.findViewById(R.id.textView_reportable_low);
    }

    private void setValues(int position) {
        Analyte analyte = mAnalytes.get(position);
        String analyteName = analyte.getAnalyteName().toString();
        String analyteUnit = analyte.getUnitType().toString();
        String reportableHigh = Double.toString(analyte.getReportableHigh());
        String reportableLow = Double.toString(analyte.getReportableLow());

        mAnalyteName.setText(analyteName);
        mAnalyteUnit.setText(analyteUnit);
        mReportableHigh.setText(reportableHigh);
        mReportableLow.setText(reportableLow);
    }

    private void setClickables(final int position) {
        final AnalyteModel analyteModel = new AnalyteModel();
        final Analyte analyte = mAnalytes.get(position);
        final ArrayList<Units> allUnits = analyteModel.getAllUnitTypes(analyte);

        final String analyteName = analyte.getAnalyteName().toString();
        final String reportableHigh = Double.toString(analyte.getReportableHigh());
        final String reportableLow = Double.toString(analyte.getReportableLow());

        mAnalyteUnit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int currentUnitPosition = allUnits.indexOf(analyte.getUnitType());

                new MaterialDialog.Builder(mContext)
                        .title(analyteName + " " + mContext.getString(R.string.units))
                        .items(allUnits)
                        .itemsCallbackSingleChoice(currentUnitPosition, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                Units newUnit = allUnits.get(which);
                                analyteModel.updateAnalyteUnitType(analyte.getAnalyteName(), newUnit);
                                mBGPlusFragment.updateAnalytes();
                                return true;
                            }
                        })
                        .positiveText(R.string.okay)
                        .negativeText(R.string.cancel)
                        .show();
            }
        });

        mReportableHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mContext)
                        .title(analyteName + " " + mContext.getString(R.string.reportable_high))
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .negativeText(R.string.cancel)
                        .input(null, reportableHigh, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                analyteModel.updateAnalyteReportableHigh(analyte.getAnalyteName(), Double.parseDouble(input.toString()));
                                mBGPlusFragment.updateAnalytes();
                            }
                        }).show();
            }
        });

        mReportableLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mContext)
                        .title(analyteName + " " + mContext.getString(R.string.reportable_low))
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .negativeText(R.string.cancel)
                        .input(null, reportableLow, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                analyteModel.updateAnalyteReportableLow(analyte.getAnalyteName(), Double.parseDouble(input.toString()));
                                mBGPlusFragment.updateAnalytes();
                            }
                        }).show();
            }
        });
    }
}
