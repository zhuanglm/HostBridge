package com.epocal.hostsettings;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epocal.common.LanguageTypeUtil;
import com.epocal.common.androidutil.LocaleUtil;
import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.realmentities.BarcodeSetting;
import com.epocal.common.realmentities.Printer;
import com.epocal.common.realmentities.User;
import com.epocal.common.types.AuthorizationLogin;
import com.epocal.common.types.BarcodeSymbologiesType;
import com.epocal.common.types.ConnectionType;
import com.epocal.common.types.InputFieldType;
import com.epocal.common.types.LogoutPowerOffType;
import com.epocal.common.types.Permissions;
import com.epocal.common.types.PrinterType;
import com.epocal.common.types.UIScreens;
import com.epocal.common.types.am.Temperatures;
import com.epocal.common_ui.BaseActivity;
import com.epocal.common_ui.types.UIEditFieldType;
import com.epocal.common_ui.types.UIValueBucket;
import com.epocal.common_ui.types.UIValueBucketList;
import com.epocal.datamanager.BarcodeSettingModel;
import com.epocal.datamanager.DeviceSettingsModel;
import com.epocal.datamanager.HostConfigurationModel;
import com.epocal.datamanager.PrinterModel;
import com.epocal.datamanager.UserModel;
import com.epocal.datamanager.WorkflowRepository;
import com.epocal.util.DateUtil;
import com.epocal.util.IntegerUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import io.realm.RealmChangeListener;

import static com.epocal.common.CU.titleCase;
import static com.epocal.common.Consts.ADMINISTRATOR;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_ADD_PRINTER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_ADD_USER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCODE_CHANGE_PASSWORD;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCODE_CHANGE_PATIENTID;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCODE_CHANGE_USERID;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCOE_CHANGE_COMMENTS;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCOE_CHANGE_FLUIDLOT;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCOE_CHANGE_ID2;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCOE_CHANGE_OTHER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_CHANGE_HOSPITAL_NAME;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_CHANGE_LOGIN_AUTH;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_CHANGE_NAME;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_CHANGE_PASSWORD;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_CHANGE_USERNAME;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_EDIT_PRINTER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_EDIT_USER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_ENABLE_FIPS;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_ENABLE_INACTIVITY_LOGOUT;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_NO_UPGRADE;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_INACTIVITY_TIMER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_LANGUAGE;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_POWEROFF_ON_LOGOUT;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_PRINTER_FEATURES;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_TEMPERATURE_UNIT;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_WORKFLOW;

public class HostSettingsActivity extends BaseActivity implements IHostSettingsView {

    private static final String TAG = HostSettingsActivity.class.getSimpleName();

    ExpandableListView mExpListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HostSettingsPresenter presenter;
    HostConfigurationModel mHostConfigurationModel;
    DeviceSettingsModel mDeviceSettingsModel;
    BarcodeSettingModel mBarcodeSettingModel;

    Fragment mCurrentFragment = null;
    HostSettingsMenuAdapter newAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHomeBack();
        presenter = new HostSettingsPresenter(this);
        setContentView(R.layout.activity_host_settings);
        changeTitle(titleCase(getString(R.string.host_settings)));
        mExpListView = findViewById(R.id.lvExp);


        mHostConfigurationModel = new HostConfigurationModel();
        mHostConfigurationModel.openRealmInstance();

        mDeviceSettingsModel = new DeviceSettingsModel();
        mDeviceSettingsModel.openRealmInstance();

        mBarcodeSettingModel = new BarcodeSettingModel();
        mBarcodeSettingModel.openRealmInstance();

        setReadOnly(false);
        newAdapter = new HostSettingsMenuAdapter(this, mHostConfigurationModel, mDeviceSettingsModel);
        mExpListView.setAdapter(newAdapter);
        newAdapter.setItemValueSelectedListener(new HostSettingsMenuAdapter.ItemValueSelectedListener() {
            @Override
            public boolean onValueSelectedListener(int itemId, String input) {
                switch (itemId) {
                    case MENU_ACTION_CHANGE_NAME:
                        break;
                    case MENU_ACTION_CHANGE_HOSPITAL_NAME:
                        break;
                    case MENU_ACTION_CHANGE_LOGIN_AUTH:
                        break;
                    case MENU_ACTION_ENABLE_FIPS:
                        break;
                    case MENU_ACTION_SET_INACTIVITY_TIMER:
                        showDialogInactivityTimer(input.equals("0"));
                        break;
                    case MENU_ACTION_ENABLE_INACTIVITY_LOGOUT:
                        break;
                    case MENU_ACTION_SET_POWEROFF_ON_LOGOUT:
                        break;
                    case MENU_ACTION_SET_TEMPERATURE_UNIT:
                        break;
                    case MENU_ACTION_CHANGE_USERNAME:
                        new UserModel().setLoggedInUserName(input);
                        break;
                    case MENU_ACTION_CHANGE_PASSWORD:
                        break;
                    case MENU_ACTION_SET_LANGUAGE:
                        presenter.itemClicked(itemId);
                        break;
                    case MENU_ACTION_SET_WORKFLOW:
                        break;
                    case MENU_ACTION_ADD_USER:
                        break;
                    case MENU_ACTION_EDIT_USER:
                        break;
                    case MENU_ACTION_SET_PRINTER_FEATURES:
                        break;
                    case MENU_ACTION_ADD_PRINTER:
                        break;
                    case MENU_ACTION_EDIT_PRINTER:
                        break;
                    case MENU_ACTION_BARCODE_CHANGE_USERID:
                        break;
                    case MENU_ACTION_BARCODE_CHANGE_PASSWORD:
                        break;
                    case MENU_ACTION_BARCODE_CHANGE_PATIENTID:
                        break;
                    case MENU_ACTION_BARCOE_CHANGE_FLUIDLOT:
                        break;
                    case MENU_ACTION_BARCOE_CHANGE_ID2:
                        break;
                    case MENU_ACTION_BARCOE_CHANGE_COMMENTS:
                        break;
                    case MENU_ACTION_BARCOE_CHANGE_OTHER:
                        break;
                    case MENU_ACTION_NO_UPGRADE:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                boolean isClickHandled = presenter.itemClicked((int) id);
                if (!isClickHandled) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "You picked : menu=" + groupPosition + " submenu=" + childPosition, Snackbar.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        mHostConfigurationModel.addChangeListener(new RealmChangeListener() {
            /**
             * Called when a transaction is committed.
             *
             * @param element the object
             */
            @Override
            public void onChange(@NonNull Object element) {
                newAdapter.notifyDataSetChanged();
                Log.d(TAG, "Realm rHostConfiguration Data Changed.");
            }
        });

        mDeviceSettingsModel.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange(@NonNull Object element) {
                newAdapter.notifyDataSetChanged();
                Log.d(TAG, "Realm DeviceSetting Data Changed.");
            }
        });
    }

    @Override
    public void showDialogNameSettings() {
        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.EditTextBox);

        Bundle bundle = new Bundle();
        bundle.putString(GeneralSettingOptionsFragment.bundle_Value_Current, mDeviceSettingsModel.getDeviceName());
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {
                        mDeviceSettingsModel.setDeviceName(value, true);
                        closeFragment();
                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_name));
    }

    @Override
    public void showDialogHospitalSettings() {
        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.EditTextBox);

        Bundle bundle = new Bundle();
        bundle.putString(GeneralSettingOptionsFragment.bundle_Value_Current, mHostConfigurationModel.getHospitalName());
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {
                        mHostConfigurationModel.setHospitalName(value);
                        closeFragment();
                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.name_settings_hospital_name));
    }

    @Override
    public void showDialogLoginAuthentication() {
        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.RadioButton);

        Bundle bundle = new Bundle();
        ArrayList<UIValueBucket> somethingList = UIValueBucket.fromEnum(AuthorizationLogin.class);
        bundle.putParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List, somethingList);
        bundle.putInt(GeneralSettingOptionsFragment.bundle_Value_Current, mHostConfigurationModel.getAuthorizationLogin().value);
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {
                        mHostConfigurationModel.setAuthorizationLogin(AuthorizationLogin.fromInt(value));
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {

                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_login_authentications));
    }

    @Override
    public void showDialogEnableFIPS() {
        boolean isEnabled = mDeviceSettingsModel.getFips();
        // 0 = Yes, 1 = No
        int choice = isEnabled ? 1 : 0;
        new MaterialDialog.Builder(this)
                .title(R.string.general_host_settings_enable_fips)
                .items(R.array.yes_no)
                .itemsCallbackSingleChoice(choice, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        boolean isEnabled;
                        switch (which) {
                            case 0:
                                isEnabled = false;
                                break;
                            case 1:
                            default:
                                isEnabled = true;
                        }
                        mDeviceSettingsModel.setFips(isEnabled, true);
                        return true;
                    }
                })
                .positiveText(R.string.okay)
                .negativeText(R.string.cancel)
                .show();
    }

    @Override
    public void showDialogInactivityTimer(boolean disable) {

        if (disable) {
            mHostConfigurationModel.setEnableInactivity(false);
            EpocNavigationObject message = new EpocNavigationObject();
            message.setContext(HostSettingsActivity.this);
            message.setTargetscreen(UIScreens.STOP_INACTIVITY_SERVICE);
            EventBus.getDefault().post(message);
            return;
        } else {
            mHostConfigurationModel.setEnableInactivity(true);
        }
        int currentValue = mHostConfigurationModel.getInactivityTimer();

        final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(this)
                .minValue(1)
                .maxValue(60)
                .defaultValue(currentValue)
                .separatorColor(ContextCompat.getColor(this, R.color.colorDivider))
                .textColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .build();

        new AlertDialog.Builder(this)
                .setTitle(R.string.general_host_settings_inactivity_timer)
                .setView(numberPicker)
                .setNegativeButton(getString(android.R.string.cancel), null)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHostConfigurationModel.setInactivityTimer(numberPicker.getValue());

                        EpocNavigationObject stopMessage = new EpocNavigationObject();
                        stopMessage.setContext(HostSettingsActivity.this);
                        stopMessage.setTargetscreen(UIScreens.STOP_INACTIVITY_SERVICE);
                        EventBus.getDefault().post(stopMessage);

                        EpocNavigationObject startMessage = new EpocNavigationObject();
                        startMessage.setContext(HostSettingsActivity.this);
                        startMessage.setTargetscreen(UIScreens.START_INACTIVITY_SERVICE);
                        EventBus.getDefault().post(startMessage);
//                        Snackbar.make(getWindow().getDecorView().getRootView(), "You picked : " + numberPicker.getValue(), Snackbar.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    @Override
    public void showDialogInactivityLogout() {
        int currentInactivityLogout = mHostConfigurationModel.isLogoutWhenInactive() ? 1 : 0;
        new MaterialDialog.Builder(this)
                .title(R.string.general_host_settings_inactivity_sign_out)
                .items(R.array.yes_no)
                .itemsCallbackSingleChoice(currentInactivityLogout, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        boolean inactivityLogout;
                        switch (which) {
                            case 0:
                                inactivityLogout = false;
                                break;
                            case 1:
                            default:
                                inactivityLogout = true;
                        }
                        mHostConfigurationModel.setLogoutWhenInactive(inactivityLogout);
                        return true;
                    }
                })
                .positiveText(R.string.okay)
                .negativeText(R.string.cancel)
                .show();
    }

    // NOTE: 2017-12-05: Poweroff is replaced by Screenoff
    @Override
    public void showDialogPowerOffOnLogout() {
        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.RadioButton);

        Bundle bundle = new Bundle();
        ArrayList<UIValueBucket> somethingList = UIValueBucket.fromEnum(LogoutPowerOffType.class);
        bundle.putParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List, somethingList);
        bundle.putInt(GeneralSettingOptionsFragment.bundle_Value_Current, mHostConfigurationModel.getLogoutPowerOffType().value);
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {
                        mHostConfigurationModel.setLogoutPowerOffType(LogoutPowerOffType.fromInt(value));
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {

                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_screen_off_on_sign_out));
    }

    @Override
    public void showDialogTemperatureUnit() {
        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.RadioButton);

        Bundle bundle = new Bundle();
        ArrayList<UIValueBucket> somethingList = UIValueBucket.fromEnum(Temperatures.class);
        for (UIValueBucket e : somethingList) {
            if (e.getID() == Temperatures.ENUM_UNINITIALIZED.ordinal()) {
                somethingList.remove(e);
            }
        }
        bundle.putParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List, somethingList);
        bundle.putInt(GeneralSettingOptionsFragment.bundle_Value_Current, mHostConfigurationModel.getTemperatureUnit().value);
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {
                        mHostConfigurationModel.setTemperatureUnit(Temperatures.fromInt(value));
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {

                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }

                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_temperature_unit));
    }

    public void showDialogUsername() {
        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.EditTextBox);

        Bundle bundle = new Bundle();
        bundle.putString(GeneralSettingOptionsFragment.bundle_Value_Current, getLoggedInUser().getUserName());
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {
                        new UserModel().setLoggedInUserName(value);
                        closeFragment();
                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_user_name));
    }

    public void showDialogPassword() {
        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.EditTextBox);

        Bundle bundle = new Bundle();
        bundle.putString(GeneralSettingOptionsFragment.bundle_Value_Current, "####");
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {
                        if (getLoggedInUser().getUserId().equals(ADMINISTRATOR)) {
                            obj.showAlert("Changing Administrator's password is disabled on Development device.");
                        } else if (value.length() < 4) {
                            obj.showAlert(getString(R.string.password_less_than_min_length));
                        } else {
                            new UserModel().setLoggedInUserPassword(value);
                            closeFragment();
                        }
                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_user_name));
    }

    private User getLoggedInUser() {
        User user = new UserModel().getLoggedInUser();
        if (user == null)
            user = new UserModel().getAnonymousUser();
        return user;
    }

    public void showDialogLanguages() {
        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.RadioButton);

        Bundle bundle = new Bundle();
        ArrayList<UIValueBucket> somethingList = presenter.generateParametersForHostLanguage(this);
        bundle.putParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List, somethingList);
        bundle.putInt(GeneralSettingOptionsFragment.bundle_Value_Current, LanguageTypeUtil.getListIndexFromLanguageType(mDeviceSettingsModel.getLanguageType(), this.getApplicationContext()));
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {
                        presenter.handleHostLanguageUpdate(value);
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {

                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.language_settings));
    }

    public void showDialogWorkflows() {

        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.RadioButton);

        Bundle bundle = new Bundle();
        ArrayList<UIValueBucket> somethingList = presenter.generateParametersForWorkFlow(this);
        bundle.putParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List, somethingList);
        final String[] allWorkflows = new WorkflowRepository().getAllWorkflowNames();
        int currentlySelected = getIndexOfActive(allWorkflows);
        bundle.putInt(GeneralSettingOptionsFragment.bundle_Value_Current, currentlySelected);
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {
                        String newActiveWorkflowName = getSelectedWorkflowName(value, allWorkflows);
                        new WorkflowRepository().setActiveWorkflow(newActiveWorkflowName);
                        newAdapter.notifyDataSetInvalidated();
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {

                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.workflow_settings));
    }

    public void showDialogAddUser() {
        User user = new User();
        editaddUser(user);
    }

    private void editaddUser(final User user) {
        final long userDbId = user.getId();
        final String userid = user.getUserId();
        final String username = user.getUserName();
        final Boolean userEnabled = user.getEnabled();
        final Date userExpires = user.Expires();
        final Date userCreated = user.getCreated();
        final boolean creatingNewUser = (userDbId == 0);

        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.CustomLayout);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List, null);
        bundle.putInt(GeneralSettingOptionsFragment.bundle_Value_Current, 0);
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                        obj.customView(R.layout.host_settings_edit_user_dialog, true);

                        final Button positiveAction = obj.getActivity().findViewById(R.id.button_Positive);

                        final EditText userIdValue = obj.getActivity().findViewById(R.id.user_id_value);
                        final TextView userIdError = obj.getActivity().findViewById(R.id.user_id_error);

                        final EditText userNameValue = obj.getActivity().findViewById(R.id.user_name_value);
                        final TextView userNameError = obj.getActivity().findViewById(R.id.user_name_error);

                        final EditText userPasswordValue = obj.getActivity().findViewById(R.id.user_password_value);
                        final TextView userPasswordError = obj.getActivity().findViewById(R.id.user_password_error);

                        final Switch userStatusToggle = obj.getActivity().findViewById(R.id.user_status_toggle);
                        final Spinner userRoleSpinner = obj.getActivity().findViewById(R.id.user_role_spinner);

                        final EditText userExpiringValue = obj.getActivity().findViewById(R.id.user_expiration_value);
                        final TextView userExpirationError = obj.getActivity().findViewById(R.id.user_expiration_error);
                        final EditText userCreatedValue = obj.getActivity().findViewById(R.id.user_created_value);

                        userIdError.setVisibility(View.GONE);
                        userNameError.setVisibility(View.GONE);
                        userPasswordError.setVisibility(View.GONE);
                        userExpirationError.setVisibility(View.GONE);

                        final updateEditPositiveActionStateListener positiveActionStateListener = new updateEditPositiveActionStateListener() {
                            @Override
                            public void onUpdateEditPositiveActionState() {
                                String updatedUserId = userIdValue.getText().toString();
                                if (validateUserId(updatedUserId, userid) != FieldValidationCode.fieldValueIsValid) {
                                    positiveAction.setEnabled(false);
                                    return;
                                }

                                String updatedUsername = userNameValue.getText().toString();
                                if (validateUserName(updatedUsername, username) == FieldValidationCode.fieldValueIsInvalid) {
                                    positiveAction.setEnabled(false);
                                    return;
                                }

                                String updatedPassword = userPasswordValue.getText().toString();
                                boolean blankPasswordIsValid = !creatingNewUser;   // An blank password is only 'valid' for existing users; the password is not being modified
                                if (validateUserPassword(updatedPassword, blankPasswordIsValid) != FieldValidationCode.fieldValueIsValid) {
                                    positiveAction.setEnabled(false);
                                    return;
                                }

                                String updatedUserExpiration = userExpiringValue.getText().toString();
                                if (validateUserExpiration(updatedUserExpiration, "dd-MMM-yyyy") != FieldValidationCode.fieldValueIsValid) {
                                    positiveAction.setEnabled(false);
                                    return;
                                }

                                // All fields are valid - enable the OK button
                                positiveAction.setEnabled(true);
                            }
                        };

                        // positive button
                        positiveAction.setEnabled(false);
                        positiveAction.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((GeneralSettingOptionsFragment) mCurrentFragment).triggerPositive();
                            }
                        });

                        // User ID
                        if (userid != null)
                            userIdValue.setText(userid);
                        userIdValue.requestFocus();
                        if (creatingNewUser) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            }
                        }
                        userIdError.setVisibility(View.INVISIBLE);  // by default

                        userIdValue.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String newValue = s.toString();
                                switch (validateUserId(newValue, userid)) {
                                    case fieldValueIsValid:
                                        userIdError.setVisibility(View.INVISIBLE);
                                        break;

                                    case fieldValueAlreadyExists:
                                        userIdError.setText(R.string.host_settings_edit_user_id_error_exists);
                                        userIdError.setVisibility(View.VISIBLE);
                                        break;

                                    case fieldValueIsInvalid:
                                    default:
                                        userIdError.setText(R.string.host_settings_edit_user_id_error_invalid);
                                        userIdError.setVisibility(View.VISIBLE);
                                        break;
                                }
                                positiveActionStateListener.onUpdateEditPositiveActionState();
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        // User Name
                        if (username != null)
                            userNameValue.setText(username);

                        userNameError.setVisibility(View.INVISIBLE);    // by default

                        userNameValue.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String newValue = s.toString();

                                switch (validateUserName(newValue, username)) {
                                    case fieldValueIsInvalid:
                                        userNameError.setText(R.string.host_settings_edit_user_name_error_invalid);
                                        userNameError.setVisibility(View.VISIBLE);
                                        break;

                                    case fieldValueIsValid:
                                    case fieldValueAlreadyExists:
                                    default:
                                        userNameError.setVisibility(View.INVISIBLE);
                                        break;
                                }

                                positiveActionStateListener.onUpdateEditPositiveActionState();
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        // User Password
                        userPasswordValue.setText("");
                        if (creatingNewUser)
                            userPasswordValue.setHint(R.string.host_settings_edit_user_password_hint);
                        else
                            userPasswordValue.setHint(R.string.host_settings_edit_user_password_hint2);

                        userPasswordError.setVisibility(View.INVISIBLE);    // by default

                        userPasswordValue.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String newValue = s.toString();
                                boolean blankPasswordIsValid = !creatingNewUser;   // An blank password is only 'valid' for existing users; the password is not being modified
                                switch (validateUserPassword(newValue, blankPasswordIsValid)) {
                                    case fieldValueIsValid:
                                        userPasswordError.setVisibility(View.INVISIBLE);
                                        break;

                                    case fieldValueAlreadyExists:
                                    case fieldValueIsInvalid:
                                    default:
                                        userPasswordError.setText(R.string.host_settings_edit_user_password_error);
                                        userPasswordError.setVisibility(View.VISIBLE);
                                        break;
                                }

                                positiveActionStateListener.onUpdateEditPositiveActionState();
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        // User Status
                        if (userEnabled == null)
                            userStatusToggle.setChecked(true);
                        else
                            userStatusToggle.setChecked(userEnabled);

                        userStatusToggle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                positiveActionStateListener.onUpdateEditPositiveActionState();
                            }
                        });

                        // User Role
                        Permissions userPermission = user.getPermission();
                        if (userPermission == Permissions.NONE)
                            userPermission = Permissions.RUNPATIENTTESTS;
                        final int userPermissionSelectedIndex = userPermission.ordinal() - 1;
                        userRoleSpinner.setSelection(userPermissionSelectedIndex);

                        userRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                // onItemSelected() is called when the default selection is set via setSelection(),
                                // this effectively only updated the button's status when the selection makes a
                                // change from the default set value.
                                if (userPermissionSelectedIndex != i)
                                    positiveActionStateListener.onUpdateEditPositiveActionState();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });

                        // User Expiration Date
                        userExpiringValue.setHint(R.string.host_settings_edit_user_expiration_hint);
                        if (userExpires != null) {
                            userExpiringValue.setText(DisplayDate(userExpires, "dd-MMM-yyyy"));
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);

                            // Add a year to the date
                            calendar.set(year + 1, month, day);

                            userExpiringValue.setText(DisplayDate(calendar.getTime(), "dd-MMM-yyyy"));
                        }

                        userExpirationError.setVisibility(View.INVISIBLE);  // by default

                        final Calendar expirationCalendar = Calendar.getInstance();

                        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                                expirationCalendar.set(Calendar.YEAR, y);
                                expirationCalendar.set(Calendar.MONTH, m);
                                expirationCalendar.set(Calendar.DAY_OF_MONTH, d);
                                userExpiringValue.setText(DisplayDate(expirationCalendar.getTime(), "dd-MMM-yyyy"));
                            }
                        };

                        userExpiringValue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String expDateStr = userExpiringValue.getText().toString();
                                switch (validateUserExpiration(expDateStr, "dd-MMM-yyyy")) {
                                    case fieldValueIsValid:
                                        userExpirationError.setVisibility(View.INVISIBLE);
                                        break;

                                    case fieldValueAlreadyExists:
                                    case fieldValueIsInvalid:
                                    default:
                                        userExpirationError.setText(R.string.host_settings_edit_user_expiration_error);
                                        userExpirationError.setVisibility(View.VISIBLE);
                                        break;
                                }

                                positiveActionStateListener.onUpdateEditPositiveActionState();

                                Calendar expDate = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                try {
                                    expDate.setTime(sdf.parse(expDateStr));
                                } catch (ParseException pe) {
                                    // Use the current time (set by default)
                                }

                                new DatePickerDialog(HostSettingsActivity.this, dateListener, expDate.get(Calendar.YEAR), expDate.get(Calendar.MONTH), expDate.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        // User Created Date
                        userCreatedValue.setEnabled(false);

                        if (userCreated != null) {
                            userCreatedValue.setText(DisplayDate(userCreated, "dd-MMM-yyyy"));
                        } else {
                            userCreatedValue.setText(DisplayDate(new Date(), "dd-MMM-yyyy"));
                        }
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {

                        final EditText userIdValue = obj.getActivity().findViewById(R.id.user_id_value);

                        final EditText userNameValue = obj.getActivity().findViewById(R.id.user_name_value);

                        final EditText userPasswordValue = obj.getActivity().findViewById(R.id.user_password_value);

                        final Switch userStatusToggle = obj.getActivity().findViewById(R.id.user_status_toggle);
                        final Spinner userRoleSpinner = obj.getActivity().findViewById(R.id.user_role_spinner);

                        final EditText userExpiringValue = obj.getActivity().findViewById(R.id.user_expiration_value);
                        final EditText userCreatedValue = obj.getActivity().findViewById(R.id.user_created_value);

                        User updatedUser = new User(creatingNewUser ? null : user);

                        if (creatingNewUser) {
                            updatedUser.setActive(true);
                            updatedUser.setSpecial(false);
                        }

                        updatedUser.setUserId(userIdValue.getText().toString());
                        updatedUser.setUserName(userNameValue.getText().toString());

                        // Don't set the password if this is an existing user, and the field is left blank
                        String updatedPassword = userPasswordValue.getText().toString();
                        if (creatingNewUser || !updatedPassword.isEmpty()) {
                            updatedUser.setPassword(updatedPassword);
                        }

                        updatedUser.setEnabled(userStatusToggle.isChecked());
                        updatedUser.setPermission(Permissions.values()[userRoleSpinner.getSelectedItemPosition() + 1]);
                        updatedUser.setStartDate(DateUtil.CreateDate(userCreatedValue.getText().toString()));
                        updatedUser.setEndDate(DateUtil.CreateDate(userExpiringValue.getText().toString()));
                        updatedUser.setCreated(DateUtil.CreateDate(userCreatedValue.getText().toString()));
                        new UserModel().saveUser(updatedUser);
                        closeFragment();
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {

                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {

                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_user_edit));
    }

    public void showDialogEditUser() {
        ArrayList<User> allUsers = new UserModel().getAllUsers();
        ArrayList<UIValueBucket> somethingList = new ArrayList<>();
        for (User user : allUsers) {
            if (!user.getSpecial() && !user.getLoggedIn()) {
                UIValueBucket pair = new UIValueBucket();
                pair.setID((int) user.getId());
                pair.setTextString(user.getUserId());
                somethingList.add(pair);
            }
        }

        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.TextView);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List, somethingList);
        bundle.putInt(GeneralSettingOptionsFragment.bundle_Value_Current, 0);
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                        showDialogEditSelectedUser(value);
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {

                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {

                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_user_edit));

    }

    public void showDialogEditSelectedUser(int UserIdNum) {
        User user = new UserModel().findUserById(UserIdNum);

        editaddUser(user);
    }

    public interface updateEditPositiveActionStateListener {
        void onUpdateEditPositiveActionState();
    }

    private String DisplayDate(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(date);
    }

    private enum FieldValidationCode {
        fieldValueIsValid,
        fieldValueAlreadyExists,
        fieldValueIsInvalid
    }

    private FieldValidationCode validateUserId(String updatedUserId, String currentUserId) {
        FieldValidationCode rc = FieldValidationCode.fieldValueIsValid;

        int len = updatedUserId.length();
        if (len < 4) {
            rc = FieldValidationCode.fieldValueIsInvalid;
        } else if (!updatedUserId.equals(currentUserId)) {
            User matchedUser = new UserModel().findUser(updatedUserId);
            if (matchedUser != null && updatedUserId.toLowerCase().equals(matchedUser.getUserId().toLowerCase())) {
                rc = FieldValidationCode.fieldValueAlreadyExists;
            }
        }

        return rc;
    }

    private FieldValidationCode validateUserName(String updatedUserName, String currentUserName) {
        FieldValidationCode rc = FieldValidationCode.fieldValueIsValid;

        int len = updatedUserName.length();
        if (len < 4) {
            rc = FieldValidationCode.fieldValueIsInvalid;
        } else if (!updatedUserName.equals(currentUserName)) {
            User matchedUser = new UserModel().findUser(updatedUserName);
            if (matchedUser != null && updatedUserName.toLowerCase().equals(matchedUser.getUserId().toLowerCase())) {
                rc = FieldValidationCode.fieldValueAlreadyExists;
            }
        }

        return rc;
    }

    private FieldValidationCode validateUserPassword(String updatedPassword, boolean blankPasswordIsValid) {

        if (updatedPassword.isEmpty() && blankPasswordIsValid) {
            return FieldValidationCode.fieldValueIsValid;
        }

        if (updatedPassword.length() < 4) {
            return FieldValidationCode.fieldValueIsInvalid;
        }

        return FieldValidationCode.fieldValueIsValid;
    }

    private FieldValidationCode validateUserExpiration(String updatedUserExpiration, String dateFormat) {
        FieldValidationCode rc = FieldValidationCode.fieldValueIsValid;

        if (updatedUserExpiration != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            sdf.setLenient(false);

            try {
                sdf.parse(updatedUserExpiration);
            } catch (ParseException pe) {
                rc = FieldValidationCode.fieldValueIsInvalid;
            }
        } else {
            rc = FieldValidationCode.fieldValueIsInvalid;
        }
        return rc;
    }

    private FieldValidationCode validatePrinterName(String updatedValue, String currentValue) {
        FieldValidationCode rc = FieldValidationCode.fieldValueIsValid;

        int len = updatedValue.length();
        if (len == 0) {
            rc = FieldValidationCode.fieldValueIsInvalid;
        } else if (!updatedValue.equals(currentValue)) {
            Printer matchedUPrinter = new PrinterModel().findPrinterByName(updatedValue);
            if (matchedUPrinter != null && updatedValue.toLowerCase().equals(matchedUPrinter.getPrinterName().toLowerCase())) {
                rc = FieldValidationCode.fieldValueAlreadyExists;
            }
        }
        return rc;
    }

    private FieldValidationCode validatePrinterAddress(String updatedValue, String currentValue) {
        FieldValidationCode rc = FieldValidationCode.fieldValueIsValid;

        int len = updatedValue.length();
        if (len == 0) {
            rc = FieldValidationCode.fieldValueIsInvalid;
        }
        return rc;
    }

    public void showDialogAddPrinter() {
        Printer printer = new Printer();
        editaddPrinter(printer);
    }

    public void showDialogEditPrinter() {
        ArrayList<Printer> allPrinters = new PrinterModel().getAllPrinters();
        ArrayList<UIValueBucket> somethingList = new ArrayList<>();
        for (Printer printer : allPrinters) {
            UIValueBucket pair = new UIValueBucket();
            pair.setID((int) printer.getId());
            pair.setTextString(printer.getPrinterName());
            somethingList.add(pair);
        }

        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.TextView);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List, somethingList);
        bundle.putInt(GeneralSettingOptionsFragment.bundle_Value_Current, 0);
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                        showDialogEditSelectedPrinter(value);
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {

                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {

                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.printer_settings));
    }

    public void showDialogInactivityTimerSetting(String input) {
        if (IntegerUtil.isInteger(input)) {
            int val = Integer.parseInt(input);
            if (val == 0) {
                mHostConfigurationModel.setInactivityTimer(val);
                return;
            }
        }

        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.EditTextBox);

        Bundle bundle = new Bundle();
        bundle.putString(GeneralSettingOptionsFragment.bundle_Value_Current, Integer.toString(mHostConfigurationModel.getInactivityTimer()));
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {
                        try {
                            int val = Integer.parseInt(value);
                            if (val <= 0 || val > 60) {
                                obj.showAlert(getString(R.string.invalid_inactivity_timer));
                                return;
                            }
                            mHostConfigurationModel.setInactivityTimer(val);
                            closeFragment();
                        } catch (NumberFormatException e) {
                            obj.showAlert(getString(R.string.invalid_inactivity_timer));
                        }
                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_inactivity_timer));
    }

    public void showDialogEditSelectedPrinter(int UserIdNum) {
        Printer printer = new PrinterModel().findPrinterById(UserIdNum);
        editaddPrinter(printer);
    }

    private void editaddPrinter(final Printer printer) {
        final long printerDbId = printer.getId();
        final String printerName = printer.getPrinterName();
        final String printerAddress = printer.getPrinterAddress();
        final Boolean printCalculatedResult = printer.getIsPrintCalculatedResult();
        final Boolean printCorrectedResult = printer.getIsPrintCorrectedResult();
        final Boolean printTestInfo = printer.getIsPrintTestInfo();
        final boolean creatingNewPrinter = (printerDbId == 0);

        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.CustomLayout);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List, null);
        bundle.putInt(GeneralSettingOptionsFragment.bundle_Value_Current, 0);
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                        obj.customView(R.layout.host_settings_edit_printer_dialog, true);

                        final Button positiveAction = obj.getActivity().findViewById(R.id.button_Positive);

                        final EditText printerNameValue = obj.getActivity().findViewById(R.id.printer_name_value);
                        final TextView printerNameError = obj.getActivity().findViewById(R.id.printer_name_error);

                        final EditText printerAddressValue = obj.getActivity().findViewById(R.id.printer_address_value);
                        final TextView printerAddressError = obj.getActivity().findViewById(R.id.printer_address_error);

                        final android.support.v7.widget.AppCompatCheckBox checkBoxPrintCalculatedResult = obj.getActivity().findViewById(R.id.print_calculated_result);
                        final android.support.v7.widget.AppCompatCheckBox checkBoxPrintCorrectedResult = obj.getActivity().findViewById(R.id.print_corrected_result);
                        final android.support.v7.widget.AppCompatCheckBox checkBoxPrintTestInfo = obj.getActivity().findViewById(R.id.print_test_info);

                        final Spinner SpinnerPrinterType = obj.getActivity().findViewById(R.id.printer_type_value);
                        final Spinner SpinnerConnectionType = obj.getActivity().findViewById(R.id.connectin_type_value);

                        printerNameError.setVisibility(View.GONE);
                        printerAddressError.setVisibility(View.GONE);

                        final updateEditPositiveActionStateListener positiveActionStateListener = new updateEditPositiveActionStateListener() {
                            @Override
                            public void onUpdateEditPositiveActionState() {
                                String updatedValue = printerNameValue.getText().toString();
                                if (validatePrinterName(updatedValue, printerName) != FieldValidationCode.fieldValueIsValid) {
                                    positiveAction.setEnabled(false);
                                    return;
                                }
                                // All fields are valid - enable the OK button
                                positiveAction.setEnabled(true);
                            }
                        };

                        // positive button
                        positiveAction.setEnabled(false);
                        positiveAction.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((GeneralSettingOptionsFragment) mCurrentFragment).triggerPositive();
                            }
                        });

                        if (printerName != null)
                            printerNameValue.setText(printerName);
                        printerNameValue.requestFocus();
                        if (creatingNewPrinter) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            }
                        }
                        printerNameError.setVisibility(View.GONE);  // by default

                        printerNameValue.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String newValue = s.toString();
                                switch (validatePrinterName(newValue, printerName)) {
                                    case fieldValueIsValid:
                                        printerNameError.setVisibility(View.GONE);
                                        break;

                                    case fieldValueAlreadyExists:
                                        printerNameError.setText(R.string.host_settings_edit_printer_name_error_exists);
                                        printerNameError.setVisibility(View.VISIBLE);
                                        break;

                                    case fieldValueIsInvalid:
                                    default:
                                        printerNameError.setText(R.string.host_settings_edit_printer_name_error_invalid);
                                        printerNameError.setVisibility(View.VISIBLE);
                                        break;
                                }
                                positiveActionStateListener.onUpdateEditPositiveActionState();
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        if (printerAddress != null)
                            printerAddressValue.setText(printerAddress);

                        printerAddressError.setVisibility(View.GONE);    // by default

                        printerAddressValue.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String newValue = s.toString();

                                switch (validatePrinterAddress(newValue, printerName)) {
                                    case fieldValueIsInvalid:
                                        printerAddressError.setText(R.string.host_settings_edit_printer_address_error_invalid);
                                        printerAddressError.setVisibility(View.VISIBLE);
                                        break;

                                    case fieldValueIsValid:
                                    case fieldValueAlreadyExists:
                                    default:
                                        printerAddressError.setVisibility(View.GONE);
                                        break;
                                }

                                positiveActionStateListener.onUpdateEditPositiveActionState();
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        checkBoxPrintCalculatedResult.setChecked(printCalculatedResult);

                        checkBoxPrintCalculatedResult.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                positiveActionStateListener.onUpdateEditPositiveActionState();
                            }
                        });

                        checkBoxPrintCorrectedResult.setChecked(printCorrectedResult);

                        checkBoxPrintCorrectedResult.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                positiveActionStateListener.onUpdateEditPositiveActionState();
                            }
                        });

                        checkBoxPrintTestInfo.setChecked(printTestInfo);

                        checkBoxPrintTestInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                positiveActionStateListener.onUpdateEditPositiveActionState();
                            }
                        });

                        List<String> list = new ArrayList<>();

                        for (PrinterType e : PrinterType.values()) {
                            list.add(e.toString());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpinnerPrinterType.setAdapter(dataAdapter);
                        ((BaseAdapter) SpinnerPrinterType.getAdapter()).notifyDataSetChanged();
                        SpinnerPrinterType.invalidate();
                        PrinterType thePrinterType = printer.getThePrinterType();
                        if (thePrinterType == PrinterType.Unknown)
                            thePrinterType = PrinterType.Other;
                        final int printerTypeSelectedIndex = thePrinterType.ordinal();
                        SpinnerPrinterType.setSelection(printerTypeSelectedIndex);

                        SpinnerPrinterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                // onItemSelected() is called when the default selection is set via setSelection(),
                                // this effectively only updated the button's status when the selection makes a
                                // change from the default set value.
                                if (printerTypeSelectedIndex != i)
                                    positiveActionStateListener.onUpdateEditPositiveActionState();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });

                        list = new ArrayList<>();

                        for (ConnectionType e : ConnectionType.values()) {
                            list.add(e.toString());
                        }
                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, list);
                        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpinnerConnectionType.setAdapter(dataAdapter2);
                        ((BaseAdapter) SpinnerConnectionType.getAdapter()).notifyDataSetChanged();
                        SpinnerConnectionType.invalidate();

                        ConnectionType theConnectionType = printer.getTheConnectionType();
                        if (theConnectionType == ConnectionType.Unknown)
                            theConnectionType = ConnectionType.WiFi;
                        final int connectionTypeSelectedIndex = theConnectionType.ordinal();
                        SpinnerConnectionType.setSelection(connectionTypeSelectedIndex);

                        SpinnerConnectionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                // onItemSelected() is called when the default selection is set via setSelection(),
                                // this effectively only updated the button's status when the selection makes a
                                // change from the default set value.
                                if (connectionTypeSelectedIndex != i)
                                    positiveActionStateListener.onUpdateEditPositiveActionState();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {

                        final Button positiveAction = obj.getActivity().findViewById(R.id.button_Positive);

                        final EditText printerNameValue = obj.getActivity().findViewById(R.id.printer_name_value);
                        final TextView printerNameError = obj.getActivity().findViewById(R.id.printer_name_error);

                        final EditText printerAddressValue = obj.getActivity().findViewById(R.id.printer_address_value);
                        final TextView printerAddressError = obj.getActivity().findViewById(R.id.printer_address_error);

                        final android.support.v7.widget.AppCompatCheckBox checkBoxPrintCalculatedResult = obj.getActivity().findViewById(R.id.print_calculated_result);
                        final android.support.v7.widget.AppCompatCheckBox checkBoxPrintCorrectedResult = obj.getActivity().findViewById(R.id.print_corrected_result);
                        final android.support.v7.widget.AppCompatCheckBox checkBoxPrintTestInfo = obj.getActivity().findViewById(R.id.print_test_info);


                        final Spinner SpinnerPrinterType = obj.getActivity().findViewById(R.id.printer_type_value);
                        final Spinner SpinnerConnectionType = obj.getActivity().findViewById(R.id.connectin_type_value);


                        Printer updatedPrinter = new Printer(creatingNewPrinter ? null : printer);
                        updatedPrinter.setPrinterName(printerNameValue.getText().toString());
                        updatedPrinter.setPrinterAddress(printerAddressValue.getText().toString());

                        updatedPrinter.setIsPrintCalculatedResult(checkBoxPrintCalculatedResult.isChecked());
                        updatedPrinter.setIsPrintCorrectedResult(checkBoxPrintCorrectedResult.isChecked());
                        updatedPrinter.setIsPrintTestInfo(checkBoxPrintTestInfo.isChecked());

                        updatedPrinter.setThePrinterType(PrinterType.values()[SpinnerPrinterType.getSelectedItemPosition()]);
                        updatedPrinter.setTheConnectionType(ConnectionType.values()[SpinnerConnectionType.getSelectedItemPosition()]);

                        updatedPrinter.setLastUsed(DateUtil.now().getTime());
                        new PrinterModel().savePrinter(updatedPrinter);
                        closeFragment();
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {

                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {

                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_printer_edit));
    }

    public void showDialogSetPrinterFeature() {
        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new GeneralSettingOptionsFragment(this, UIEditFieldType.CheckBox);
        Bundle bundle = new Bundle();
        ArrayList<UIValueBucket> somethingList = presenter.generateParametersForPrinterFeature();
        UIValueBucket pair = new UIValueBucket();
        somethingList.get(0).setTextString(getString(R.string.general_host_settings_printer_print_range_if_highlow));
        somethingList.get(1).setTextString(getString(R.string.general_host_settings_printer_print_qa_range));
        somethingList.get(2).setTextString(getString(R.string.general_host_settings_printer_print_qa_info));
        bundle.putParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List, somethingList);
        bundle.putInt(GeneralSettingOptionsFragment.bundle_Value_Current, 0);
        mCurrentFragment.setArguments(bundle);

        ((GeneralSettingOptionsFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new GeneralSettingOptionsFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout) {

                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, int value) {
                        presenter.handlePrinterFeatureUpdate(value);
                    }

                    @Override
                    public void onPotiveAction(GeneralSettingOptionsFragment obj, String value) {

                    }

                    @Override
                    public void onNegativeAction(GeneralSettingOptionsFragment obj, int value) {
                        closeFragment();
                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(getString(R.string.general_host_settings_printer_Printer_inclusion));
    }

    public void showDialogBarcodeUserId() {
        showDialogBarcodeSetting(InputFieldType.USER_ID, InputFieldType.USER_ID.toString());
    }

    public void showDialogBarcodeUserPassword() {
        showDialogBarcodeSetting(InputFieldType.PASSWORD, InputFieldType.PASSWORD.toString());
    }

    public void showDialogBarcodePatientId() {
        showDialogBarcodeSetting(InputFieldType.PATIENT_ID, InputFieldType.PATIENT_ID.toString());
    }

    public void showDialogBarcodeFluidlot() {
        showDialogBarcodeSetting(InputFieldType.PATIENT_OR_LOT_ID, InputFieldType.PATIENT_OR_LOT_ID.toString());
    }

    public void showDialogBarcodeId2() {
        showDialogBarcodeSetting(InputFieldType.ID_2, InputFieldType.ID_2.toString());
    }

    public void showDialogBarcodeComments() {
        showDialogBarcodeSetting(InputFieldType.COMMENT, InputFieldType.COMMENT.toString());
    }

    public void showDialogBarcodeOther() {
        showDialogBarcodeSetting(InputFieldType.OTHER, InputFieldType.OTHER.toString());
    }

    public void showDialogBarcodeSetting(final InputFieldType bacodeInputType, String title) {
        BarcodeSetting barcodeSetting = mBarcodeSettingModel.getBarcodeSetting(bacodeInputType);

        final ArrayList<UIValueBucketList> l = new ArrayList<>();
        UIValueBucketList list = new UIValueBucketList();
        ArrayList<UIValueBucket> somethingList = new ArrayList<>();

        ArrayList<BarcodeSymbologiesType> list_1d = BarcodeSymbologiesType.get1DBardcodeList();
        for (int i = 0; i < list_1d.size(); i++) {
            UIValueBucket bucket = new UIValueBucket();
            bucket.setTextString(list_1d.get(i).toString());
            bucket.setKey(list_1d.get(i).value);
            if (BarcodeSymbologiesType.isEnabledSymbologies(barcodeSetting.getSymbologies(), list_1d.get(i))) {
                bucket.setValueString("TRUE");
            } else {
                bucket.setValueString("FALSE");
            }
            bucket.settUIEditFieldType(UIEditFieldType.CheckBox);
            somethingList.add(bucket);
        }

        list.setListItem(somethingList);
        list.setTitle(getString(R.string.host_settings_edit_barcode_1d_barcode));
        list.setExtendable(true);
        list.setID(0);
        l.add(list);

        //2D barcode
        list = new UIValueBucketList();
        somethingList = new ArrayList<>();

        list_1d = BarcodeSymbologiesType.get2DBardcodeList();
        for (int i = 0; i < list_1d.size(); i++) {
            UIValueBucket bucket = new UIValueBucket();
            bucket.setTextString(list_1d.get(i).toString());
            bucket.setKey(list_1d.get(i).value);
            if (BarcodeSymbologiesType.isEnabledSymbologies(barcodeSetting.getSymbologies(), list_1d.get(i))) {
                bucket.setValueString("TRUE");
            } else {
                bucket.setValueString("FALSE");
            }
            bucket.settUIEditFieldType(UIEditFieldType.CheckBox);
            somethingList.add(bucket);
        }

        list.setListItem(somethingList);
        list.setTitle(getString(R.string.host_settings_edit_barcode_2d_barcode));
        list.setExtendable(true);
        list.setID(1);
        l.add(list);

        FragmentTransaction mFt;
        FragmentManager mFm;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mCurrentFragment = new BarcodeListSettingFragment(this, l);
        ((BarcodeListSettingFragment) mCurrentFragment).setSymbologies(barcodeSetting.getSymbologies());
        ((BarcodeListSettingFragment) mCurrentFragment).setIGeneralSettingOptionsCallback(
                new BarcodeListSettingFragment.IGeneralSettingOptionsCallback() {
                    @Override
                    public void onUIInit(BarcodeListSettingFragment obj, LinearLayout mainLayout) {
                        BarcodeSetting barcodeSetting = mBarcodeSettingModel.getBarcodeSetting(bacodeInputType);
                        EditText cropBegin = mainLayout.findViewById(R.id.CropBegin);
                        EditText cropEnd = mainLayout.findViewById(R.id.CropEnd);
                        try {
                            cropBegin.setText(LocaleUtil.getIntegerValue(Integer.toString(barcodeSetting.getCropBegin())));
                        } catch (ParseException e) {
                            cropBegin.setText("");
                        }
                        try {
                            cropEnd.setText(LocaleUtil.getIntegerValue(Integer.toString(barcodeSetting.getCropEnd())));
                        } catch (ParseException e) {
                            cropEnd.setText("");
                        }
                    }

                    @Override
                    public void onPotiveAction(BarcodeListSettingFragment obj, int value) {
                        final EditText cropBegin = obj.getActivity().findViewById(R.id.CropBegin);
                        final EditText cropEnd = obj.getActivity().findViewById(R.id.CropEnd);

                        BarcodeSetting barcodeSetting = mBarcodeSettingModel.getBarcodeSetting(bacodeInputType);

                        try {
                            barcodeSetting.setCropBegin(Integer.parseInt(cropBegin.getText().toString()));
                        } catch (NumberFormatException e) {
                            barcodeSetting.setCropBegin(0);
                        }

                        try {
                            barcodeSetting.setCropEnd(Integer.parseInt(cropEnd.getText().toString()));
                        } catch (NumberFormatException e) {
                            barcodeSetting.setCropEnd(0);
                        }
                        barcodeSetting.setSymbologies(obj.getSymbologies());
                        mBarcodeSettingModel.updateBarcodeSetting(barcodeSetting);
                        closeFragment();
                    }

                    @Override
                    public void onNegativeAction(BarcodeListSettingFragment obj, int value) {

                    }
                }
        );
        mFt.replace(android.R.id.content, mCurrentFragment, "tagTest");
        mFt.addToBackStack("tagTest");
        mFt.commit();
        changeTitle(title);
    }

    private int getIndexOfActive(String[] allWorkflows) {
        int retval = 0;
        String activeWorkflowName = new WorkflowRepository().getActiveWorkflowName();
        for (String name : allWorkflows) {
            if (activeWorkflowName.equals(name)) {
                return retval;
            } else {
                retval++;
            }
        }
        return retval;
    }

    private String getSelectedWorkflowName(int index, String[] namelist) {
        String retval = null;
        int i = 0;
        for (String wfName : namelist) {
            if (i == index) {
                retval = wfName;
                break;
            }
            i++;
        }
        return retval;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDeviceSettingsModel.closeRealmInstance();
        mBarcodeSettingModel.closeRealmInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeFragment();
                break;
        }
        return true;
    }

    private void closeFragment() {
        if (mCurrentFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
                changeTitle(titleCase(getString(R.string.host_settings)));
                showHomeBack();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            changeTitle(titleCase(getString(R.string.host_settings)));
        }
        return super.onKeyDown(keyCode, event);
    }
}
