package com.epocal.hostsettings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common_ui.types.UIValueBucketList;

import java.util.ArrayList;

public class BarcodeListSettingFragment extends Fragment {

    public interface IGeneralSettingOptionsCallback {
        void onUIInit(BarcodeListSettingFragment obj, LinearLayout mainLayout);
        void onPotiveAction(BarcodeListSettingFragment obj, int value);
        void onNegativeAction(BarcodeListSettingFragment obj, int value);
    }

    private BarcodeListSettingFragment.IGeneralSettingOptionsCallback mIGeneralSettingOptionsCallback;

    public void setIGeneralSettingOptionsCallback(BarcodeListSettingFragment.IGeneralSettingOptionsCallback iGeneralSettingOptionsCallback) {
        this.mIGeneralSettingOptionsCallback = iGeneralSettingOptionsCallback;
    }

    private Context mContext;
    private ArrayList<UIValueBucketList> mList;
    private BarcodeListSettingFragment instance;
    private BarcodeListAdapter mAdapter;
    private long mSymbologies;

    public long getSymbologies()
    {
        return mAdapter.getSymbologies();
    }

    public void setSymbologies(long symbologies)
    {
        mSymbologies = symbologies;
    }

    public BarcodeListSettingFragment()
    {
        super();
    }

    @SuppressLint("ValidFragment")
    public BarcodeListSettingFragment(Context context, ArrayList<UIValueBucketList> list)
    {
        super();
        mContext = context;
        mList = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return inflater.inflate(R.layout.fragment_barcode_list_setting, group, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void createView()
    {
        instance = this;
        LinearLayout mainLayout = this.getView().findViewById(R.id.mainLayout);

        if(mIGeneralSettingOptionsCallback != null)
        {
            mIGeneralSettingOptionsCallback.onUIInit(this, mainLayout);
        }

        final Button positiveAction = (Button) mainLayout.findViewById(R.id.button_Positive);

        positiveAction.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(mIGeneralSettingOptionsCallback != null)
                {
                    mIGeneralSettingOptionsCallback.onPotiveAction(instance, 0);
                }
            }
        });

        EditText cropBegin = (EditText) mainLayout.findViewById(R.id.CropBegin);
        EditText cropEnd = (EditText) mainLayout.findViewById(R.id.CropEnd);

        final TextView cropBeginError = (TextView) mainLayout.findViewById(R.id.cropBegin_error);
        final TextView cropEndError = (TextView) mainLayout.findViewById(R.id.cropEnd_error);

        cropBeginError.setVisibility(View.GONE);
        cropEndError.setVisibility(View.GONE);

        cropBegin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newValue = s.toString();
                if (!validateCropNumber(newValue)) {
                    cropBeginError.setVisibility(View.VISIBLE);
                    positiveAction.setEnabled(false);
                    return;
                }
                cropBeginError.setVisibility(View.GONE);
                onUpdateEditPositiveActionState();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cropEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newValue = s.toString();
                if (!validateCropNumber(newValue)) {
                    cropEndError.setVisibility(View.VISIBLE);
                    positiveAction.setEnabled(false);
                    return;
                }
                cropEndError.setVisibility(View.GONE);
                onUpdateEditPositiveActionState();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ArrayList<UIValueBucketList> listExp = new ArrayList<UIValueBucketList>();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isExtendable()) {
                listExp.add(mList.get(i));
            }
        }

        ExpandableListView expandableListView = (ExpandableListView) mainLayout.findViewById(R.id.expBarcodeListView);
        mAdapter = new BarcodeListAdapter(this.getContext(), listExp);
        mAdapter.setSymbologies(mSymbologies);
        expandableListView.setAdapter(mAdapter);
        expandableListView.setVisibility(View.VISIBLE);
    }

    private boolean validateCropNumber(String cropNumber) {
        try {
            Integer.parseInt(cropNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void onUpdateEditPositiveActionState() {

        LinearLayout mainLayout = this.getView().findViewById(R.id.mainLayout);

        Button positiveAction = (Button) mainLayout.findViewById(R.id.button_Positive);
        EditText cropBegin = (EditText) mainLayout.findViewById(R.id.CropBegin);
        EditText cropEnd = (EditText) mainLayout.findViewById(R.id.CropEnd);

        String cropBeginValue = cropBegin.getText().toString();
        String cropEndValue = cropEnd.getText().toString();
        if (!validateCropNumber(cropBeginValue)) {
            positiveAction.setEnabled(false);
            return;
        }
        if (!validateCropNumber(cropEndValue)) {
            positiveAction.setEnabled(false);
            return;
        }
        positiveAction.setEnabled(true);
    }
}
