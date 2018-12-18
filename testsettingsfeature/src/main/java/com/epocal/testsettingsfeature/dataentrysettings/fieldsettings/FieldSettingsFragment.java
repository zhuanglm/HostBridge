package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epocal.common.realmentities.InputFieldConfig;
import com.epocal.common.types.SelenaFamilyType;
import com.epocal.datamanager.HostConfigurationModel;
import com.epocal.testsettingsfeature.R;
import com.epocal.testsettingsfeature.TestSettingsActivity;

import static com.epocal.testsettingsfeature.BaseActivity.DELIVERY_SYSTEM_EDIT_VALUES;
import static com.epocal.testsettingsfeature.BaseActivity.DRAW_SITE_EDIT_VALUES;
import static com.epocal.testsettingsfeature.BaseActivity.TEST_SELECTION;


public class FieldSettingsFragment extends Fragment implements IFieldSettingsView {

    ExpandableListView mExpListView;
    FieldSettingsPresenter mPresenter;
    HostConfigurationModel mHostConfigurationModel;
    Runnable mRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_field_settings, group, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mExpListView = (ExpandableListView) view.findViewById(R.id.list_vew_expandable_field_settings);

        mExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                mPresenter.groupClicked((int) id);
                return false;
            }
        });

        mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                boolean isClickHandled = mPresenter.itemClicked((int) id);
                if (!isClickHandled) {
                    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "You picked : menu=" + groupPosition + " submenu=" + childPosition, Snackbar.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        final FieldSettingsMenuAdapter newAdapter = new FieldSettingsMenuAdapter(getActivity(), mHostConfigurationModel, this);
        mExpListView.setAdapter(newAdapter);

        mRunnable = new Runnable() {
            public void run() {
                newAdapter.updateFields();
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FieldSettingsPresenter(this);
        mHostConfigurationModel = new HostConfigurationModel();
        mHostConfigurationModel.openRealmInstance();
    }

    @Override
    public void showDialogAllowUserInput(final SelenaFamilyType selenaFamilyType, boolean allowed) {
        int currentValue = (allowed ? 0 : 1); // 0 = Yes, 1 = No
        new MaterialDialog.Builder(getActivity())
                .title(R.string.allow_custom_entry)
                .items(R.array.yes_no)
                .itemsCallbackSingleChoice(currentValue, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        boolean newValue;
                        switch (which) {
                            case 0:
                                newValue = true;
                                break;
                            case 1:
                            default:
                                newValue = false;
                        }
                        mPresenter.setAllowCustomEntry(selenaFamilyType, newValue);
                        getActivity().runOnUiThread(mRunnable);
                        return true;
                    }
                })
                .positiveText(R.string.okay)
                .negativeText(R.string.cancel)
                .show();
    }

    @Override
    public void showDialogMaxMinLength(final InputFieldConfig inputFieldConfig, String value, final boolean maximum) {
        new MaterialDialog.Builder(getActivity())
                .title(getActivity().getString(R.string.maximum_length))
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .negativeText(R.string.cancel)
                .input(null, value, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (input.toString().equals("")) {
                            input = "0";
                        }
                        int newValue = Integer.parseInt(input.toString());
                        if (maximum) {
                            inputFieldConfig.setMaximumLength(newValue);
                        } else {
                            inputFieldConfig.setMinimumLength(newValue);
                        }
                        mPresenter.saveInputFieldConfig(inputFieldConfig);
                        getActivity().runOnUiThread(mRunnable);
                    }
                }).show();
    }

    @Override
    public void gotoTestSelection() {
        ((TestSettingsActivity)getActivity()).testSettingsNavigation(TEST_SELECTION);
    }

    @Override
    public void gotoSelenaEditValues(SelenaFamilyType selenaFamilyType) {
        ((TestSettingsActivity)getActivity()).testSettingsNavigation(selenaFamilyType.value);
    }

    @Override
    public void receiveSwitchToggle(long itemId, boolean itemValue) {
        mPresenter.switchToggled((int) itemId, itemValue);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHostConfigurationModel.closeRealmInstance();
    }
}
