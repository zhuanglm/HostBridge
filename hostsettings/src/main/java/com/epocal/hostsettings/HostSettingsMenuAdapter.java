package com.epocal.hostsettings;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.LanguageTypeUtil;
import com.epocal.common.epocobjects.IBaseActivity;
import com.epocal.common.realmentities.User;
import com.epocal.common.types.LanguageType;
import com.epocal.common.types.am.Temperatures;
import com.epocal.common_ui.types.UIEditFieldType;
import com.epocal.datamanager.DeviceSettingsModel;
import com.epocal.datamanager.HostConfigurationModel;
import com.epocal.datamanager.UserModel;
import com.epocal.datamanager.WorkflowRepository;

import java.util.ArrayList;
import java.util.Arrays;

import static com.epocal.common.Consts.USER_ID_ANONYMOUS;


/**
 * HostSettingMenuAdapter is responsible for converting hostConfigData and userData
 * to the format to supply for ExpandableListAdapter
 * Created on 08/06/2017.
 */

class HostSettingsMenuAdapter extends BaseExpandableListAdapter {
    /**
     * List of constants to identify MENU ACTION in this adapter.
     * The value of long only needs to be unique within this file. (Does not need to be unique within app scope.)
     * The value of long does not need to be consecutive.
     */
    static final int MENU_ACTION_CHANGE_NAME = 101;
    static final int MENU_ACTION_CHANGE_HOSPITAL_NAME = 102;
    static final int MENU_ACTION_CHANGE_LOGIN_AUTH = 103;
    static final int MENU_ACTION_ENABLE_FIPS = 104;
    static final int MENU_ACTION_SET_INACTIVITY_TIMER = 105;
    static final int MENU_ACTION_ENABLE_INACTIVITY_LOGOUT = 106;
    static final int MENU_ACTION_SET_POWEROFF_ON_LOGOUT = 107;
    static final int MENU_ACTION_SET_TEMPERATURE_UNIT = 108;
    static final int MENU_ACTION_CHANGE_USERNAME = 109;
    static final int MENU_ACTION_CHANGE_PASSWORD = 110;
    static final int MENU_ACTION_SET_LANGUAGE = 111;
    static final int MENU_ACTION_ADD_USER = 112;
    static final int MENU_ACTION_EDIT_USER = 113;
    static final int MENU_ACTION_SET_PRINTER_FEATURES = 114;
    static final int MENU_ACTION_ADD_PRINTER = 115;
    static final int MENU_ACTION_EDIT_PRINTER = 116;
    static final int MENU_ACTION_BARCODE_CHANGE_USERID = 117;
    static final int MENU_ACTION_BARCODE_CHANGE_PASSWORD = 118;
    static final int MENU_ACTION_BARCODE_CHANGE_PATIENTID = 119;
    static final int MENU_ACTION_BARCOE_CHANGE_FLUIDLOT = 120;
    static final int MENU_ACTION_BARCOE_CHANGE_ID2 = 121;
    static final int MENU_ACTION_BARCOE_CHANGE_COMMENTS = 122;
    static final int MENU_ACTION_BARCOE_CHANGE_OTHER = 123;
    static final int MENU_ACTION_NO_UPGRADE = 124;
    static final int MENU_ACTION_SET_WORKFLOW = 125;

    private final String NAME_SETTINGS;
    private final String HOST_SETTINGS;
    private final String OPERATOR_NAME_SETTINGS;
    private final String LANGUAGE_SETTINGS;
    private final String USER_SETTINGS;
    private final String PRINTER_SETTINGS;
    private final String BARCODE_SETTINGS;
    private final String UPGRADE_SETTINGS;
    private final String WORKFLOW_SETTINGS;
    private Context mContext;
    private ArrayList<String> mParentMenuTitleList;
    private ArrayList<MenuGroup> mMenuList;

    private HostConfigurationModel mHostConfigModelData;
    private DeviceSettingsModel mDeviceSettingsModelData;
    private boolean isReadOnly = false;
    private ItemValueSelectedListener mItemValueSelectedListener;

    public interface ItemValueSelectedListener {
        boolean onValueSelectedListener(int itemId, String input);
    }

    HostSettingsMenuAdapter(Context context, HostConfigurationModel hostConfigModelData, DeviceSettingsModel deviceSettingsModel) {
        mContext = context;
        if (context instanceof IBaseActivity) {
            isReadOnly = ((IBaseActivity) context).isReadOnly();
        }
        mHostConfigModelData = hostConfigModelData;
        mDeviceSettingsModelData = deviceSettingsModel;

        NAME_SETTINGS = mContext.getString(R.string.name_settings);
        HOST_SETTINGS = mContext.getString(R.string.general_host_settings);
        OPERATOR_NAME_SETTINGS = mContext.getString(R.string.operator_name_and_password_settings);
        LANGUAGE_SETTINGS = mContext.getString(R.string.language_settings);
        USER_SETTINGS = mContext.getString(R.string.user_settings);
        PRINTER_SETTINGS = mContext.getString(R.string.printer_settings);
        BARCODE_SETTINGS = mContext.getString(R.string.barcode_settings);
        UPGRADE_SETTINGS = mContext.getString(R.string.upgrade_settings);
        WORKFLOW_SETTINGS = mContext.getString(R.string.workflow_settings);

        mParentMenuTitleList = createParentMenuList();
        mMenuList = createMenu();
    }

    /**
     * Gets the number of groups.
     *
     * @return the number of groups
     */
    @Override
    public int getGroupCount() {
        return mMenuList.size();
    }

    /**
     * Gets the number of children in a specified group.
     *
     * @param groupPosition the position of the group for which the children
     *                      count should be returned
     * @return the children count in the specified group
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        MenuGroup parent = mMenuList.get(groupPosition);
        return parent.getChildMenuList().size();
    }

    /**
     * Gets the data associated with the given group.
     *
     * @param groupPosition the position of the group
     * @return the data child for the specified group
     */
    @Override
    public Object getGroup(int groupPosition) {
        return mMenuList.get(groupPosition);
    }

    /**
     * Gets the data associated with the given child within the given group.
     *
     * @param groupPosition the position of the group that the child resides in
     * @param childPosition the position of the child with respect to other
     *                      children in the group
     * @return the data of the child
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        MenuGroup parent = (MenuGroup) getGroup(groupPosition);
        return parent.getChildMenuList().get(childPosition);
    }

    private Object getChild(int itemid) {
        for (int i = 0; i < getGroupCount(); i++) {
            MenuGroup group = (MenuGroup) getGroup(i);
            for (int j = 0; j < group.getChildMenuList().size(); j++) {
                if (group.getChildMenuList().get(j).getItemId() == itemid) {
                    return group.getChildMenuList().get(j);
                }
            }
        }
        return null;
    }

    /**
     * Gets the ID for the group at the given position. This group ID must be
     * unique across groups. The combined ID (see
     * {@link #getCombinedGroupId(long)}) must be unique across ALL items
     * (groups and all children).
     *
     * @param groupPosition the position of the group for which the ID is wanted
     * @return the ID associated with the group
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Gets the ID for the given child within the given group. This ID must be
     * unique across all children within the group. The combined ID (see
     * {@link #getCombinedChildId(long, long)}) must be unique across ALL items
     * (groups and all children).
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child within the group for which
     *                      the ID is wanted
     * @return the ID associated with the child
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        MenuItem item = (MenuItem) getChild(groupPosition, childPosition);
        return item.getItemId();
    }

    /**
     * Indicates whether the child and group IDs are stable across changes to the
     * underlying data.
     *
     * @return whether or not the same ID always refers to the same object
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Gets a View that displays the given group. This View is only for the
     * group--the Views for the group's children will be fetched using
     * {@link #getChildView(int, int, boolean, View, ViewGroup)}.
     *
     * @param groupPosition the position of the group for which the View is
     *                      returned
     * @param isExpanded    whether the group is expanded or collapsed
     * @param convertView   the old view to reuse, if possible. You should check
     *                      that this view is non-null and of an appropriate type before
     *                      using. If it is not possible to convert this view to display
     *                      the correct data, this method can create a new view. It is not
     *                      guaranteed that the convertView will have been previously
     *                      created by
     *                      {#getGroupView(int, boolean, View, ViewGroup)}.
     * @param parent        the parent that this view will eventually be attached to
     * @return the View corresponding to the group at the specified position
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(infalInflater != null) {
                convertView = infalInflater.inflate(R.layout.host_settings_list_menugroup, parent, false);
            }
            else
            {
                return null;
            }
        }
        TextView lblListHeader = convertView.findViewById(R.id.menu_group_title);

        // Bind data to view
        MenuGroup headerMenu = (MenuGroup) getGroup(groupPosition);
        lblListHeader.setText(headerMenu.getTitle());

        return convertView;
    }

    /**
     * Gets a View that displays the data for the given child within the given
     * group.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child (for which the View is
     *                      returned) within the group
     * @param isLastChild   Whether the child is the last child within the group
     * @param convertView   the old view to reuse, if possible. You should check
     *                      that this view is non-null and of an appropriate type before
     *                      using. If it is not possible to convert this view to display
     *                      the correct data, this method can create a new view. It is not
     *                      guaranteed that the convertView will have been previously
     *                      created by
     * @param parent        the parent that this view will eventually be attached to
     * @return the View corresponding to the child at the specified position
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (infalInflater != null) {
                convertView = infalInflater.inflate(R.layout.host_settings_list_menuitem, parent, false);
            }
            else
            {
                return null;
            }
        }

        LinearLayout view_null_value = convertView.findViewById(R.id.menuitem_null_value);
        LinearLayout view_edit_switch = convertView.findViewById(R.id.menuitem_edit_switch);
        view_edit_switch.setVisibility(View.GONE);
        view_null_value.setVisibility(View.GONE);
        // Bind data to view
        MenuItem item = (MenuItem) getChild(groupPosition, childPosition);

        View visibleView;
        int valueView_id, titleView_id, editValue_id;
        if (item.getValue() != null && item.getUIEditFieldType().value == UIEditFieldType.EditPlusSwitch.value) {
            view_null_value.setVisibility(View.GONE);
            view_edit_switch.setVisibility(View.VISIBLE);
            visibleView = view_edit_switch;

            TextView titleView = convertView.findViewById(R.id.menuitem_edit_switch_title);
            TextView valueView = convertView.findViewById(R.id.menuitem_edit_switch_value);
            SwitchCompat switchSetting = convertView.findViewById(R.id.edit_switch_switcher);

            valueView.setTag(R.layout.host_settings_list_menuitem, (int) item.getItemId());
            titleView.setTag(R.layout.host_settings_list_menuitem, (int) item.getItemId());
            switchSetting.setTag(R.layout.host_settings_list_menuitem, (int) item.getItemId());

            titleView.setText(item.getTitle());
            valueView.setText(item.getValue());

            MenuItem item2 = (MenuItem) getChild((int)item.getItemId());

            switchSetting.setOnCheckedChangeListener(null);
            switchSetting.setChecked(item2.isChecked());
            if (switchSetting.isChecked()) {
                if (item.getEditable()) {
                    if (valueView.getVisibility() != View.GONE)
                        valueView.setVisibility(View.GONE);
                } else {
                    if (valueView.getVisibility() != View.VISIBLE)
                        valueView.setVisibility(View.VISIBLE);
                }
            } else {
                valueView.setVisibility(View.GONE);
            }

        }
        else{
            view_edit_switch.setVisibility(View.GONE);
            view_null_value.setVisibility(View.VISIBLE);
            visibleView = view_null_value;

            TextView titleView_null_title = convertView.findViewById(R.id.menuitem_null_value_title);
            TextView valueView_null_value = convertView.findViewById(R.id.menuitem_null_value_value);
            titleView_null_title.setTag(R.layout.host_settings_list_menuitem, (int) item.getItemId());
            valueView_null_value.setTag(R.layout.host_settings_list_menuitem, (int) item.getItemId());
            titleView_null_title.setText(item.getTitle());
            valueView_null_value.setText(item.getValue() == null ? "" : item.getValue());
        }

        if (item.isEnabled()) {
            visibleView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite));
        } else {
            visibleView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray50));
        }

        if (item.getUIEditFieldType().value == UIEditFieldType.EditPlusSwitch.value) {
            titleView_id = R.id.menuitem_edit_switch_title;
            valueView_id = R.id.menuitem_edit_switch_value;

            final TextView valueView = convertView.findViewById(valueView_id);
            TextView titleView = convertView.findViewById(titleView_id);
            SwitchCompat switchSetting = convertView.findViewById(R.id.edit_switch_switcher);

            titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int valueView_id = R.id.menuitem_edit_switch_value;
                    TextView valueView = ((ViewGroup) view.getParent()).findViewById(valueView_id);
                    if (valueView.getVisibility() == View.GONE) {
                        return;
                    }
                    int para = (int) view.getTag(R.layout.host_settings_list_menuitem);
                    MenuItem item = (MenuItem) getChild(para);
                    if (item != null && mItemValueSelectedListener != null) {
                        mItemValueSelectedListener.onValueSelectedListener((int) item.getItemId(), valueView.getText().toString());
                    }
                }
            });

            valueView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int para = (int) view.getTag(R.layout.host_settings_list_menuitem);
                    MenuItem item = (MenuItem) getChild(para);
                    if (item != null && mItemValueSelectedListener != null) {
                        mItemValueSelectedListener.onValueSelectedListener((int) item.getItemId(), valueView.getText().toString());
                    }
                }
            });

            if (switchSetting != null) {
                titleView.setOnClickListener(null);
                switchSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int para = (int) buttonView.getTag(R.layout.host_settings_list_menuitem);

                        MenuItem item = (MenuItem) getChild(para);
                        if(item == null) return;
                        int valueView_id = R.id.menuitem_edit_switch_value;
                        TextView valueView = ((ViewGroup) buttonView.getParent()).findViewById(valueView_id);

                        if (valueView != null) {
                            if (!isChecked) {
                                valueView.setVisibility(View.GONE);
                                if (mItemValueSelectedListener != null) {
                                    mItemValueSelectedListener.onValueSelectedListener((int) item.getItemId(), "0");
                                }
                            } else {
                                valueView.setVisibility(View.VISIBLE);
                                if (mItemValueSelectedListener != null) {
                                    mItemValueSelectedListener.onValueSelectedListener((int) item.getItemId(), item.getValue());
                                }
                            }
                        }
                    }
                });
            }
        }
        return convertView;
    }

    /**
     * Whether the child at the specified position is selectable.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child within the group
     * @return whether the child is selectable.
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        MenuItem item = (MenuItem) getChild(groupPosition, childPosition);
        return item.isEnabled();
    }

    private ArrayList<String> createParentMenuList() {
        return new ArrayList<>(
                Arrays.asList(
                        NAME_SETTINGS,
                        HOST_SETTINGS,
                        OPERATOR_NAME_SETTINGS,
                        LANGUAGE_SETTINGS,
                        USER_SETTINGS,
                        PRINTER_SETTINGS,
                        BARCODE_SETTINGS,
                        UPGRADE_SETTINGS,
                        WORKFLOW_SETTINGS)
        );
    }

    private ArrayList<MenuItem> createChildMenuForNameSettings() {
        ArrayList<MenuItem> childMenu = new ArrayList<>();
        //childMenu.add(new MenuItem("Name", ""));
        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_CHANGE_NAME,
                mContext.getString(R.string.general_host_settings_name),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return mDeviceSettingsModelData.getDeviceName();
                    }

                    @Override
                    public boolean isEnabled() {
                        return !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.TextView));

        //childMenu.add(new MenuItem("Hospital", mHostConfigModelData.getHospitalName()));
        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_CHANGE_HOSPITAL_NAME,
                mContext.getString(R.string.general_host_settings_hospital),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return mHostConfigModelData.getHospitalName();
                    }

                    @Override
                    public boolean isEnabled() {
                        return !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.TextView));

        return childMenu;
    }

    private ArrayList<MenuItem> createChildMenuForGeneralHostSettings() {
        ArrayList<MenuItem> childMenu = new ArrayList<>();
        //childMenu.add(new MenuItem("Login Authentication", mHostConfigModelData.getAuthorizationLogin().toString()));
        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_CHANGE_LOGIN_AUTH,
                mContext.getString(R.string.general_host_settings_login_authentications),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        String[] loginAuthStringArray = mContext.getResources().getStringArray(R.array.login_authentication);
                        return loginAuthStringArray[mHostConfigModelData.getAuthorizationLogin().ordinal()];
                    }

                    @Override
                    public boolean isEnabled() {
                        return !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.TextView));
        //childMenu.add(new MenuItem("Inactivity Timer", String.valueOf(mHostConfigModelData.getInactivityTimer())));
        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_SET_INACTIVITY_TIMER,
                mContext.getString(R.string.general_host_settings_inactivity_timer),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        if (mHostConfigModelData.getInactivityTimer() <= 0 || mHostConfigModelData.getInactivityTimer() > 60) {
                            return "";
                        }
                        return String.valueOf(mHostConfigModelData.getInactivityTimer());
                    }

                    @Override
                    public boolean isEnabled() {
                        return !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return mHostConfigModelData.isEnableInactivityTimer();
                    }
                }, UIEditFieldType.EditPlusSwitch));
        //childMenu.add(new MenuItem("Power off on logout", mHostConfigModelData.getLogoutPowerOffType().toString()));
        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_SET_POWEROFF_ON_LOGOUT,
                mContext.getString(R.string.general_host_settings_screen_off_on_sign_out),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        String[] powerModeStringArray = mContext.getResources().getStringArray(R.array.power_off_on_logout);
                        return powerModeStringArray[mHostConfigModelData.getLogoutPowerOffType().ordinal()];
                    }

                    @Override
                    public boolean isEnabled() {
                        // TODO: Currently 60 min is set to disable this menu to test dynamic enable/disable menu based on Data.
                        return mHostConfigModelData.getInactivityTimer() != 60 && !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.RadioButton));
        //childMenu.add(new MenuItem("Temperature Unit", mHostConfigModelData.getTemperatureUnit().toString()));
        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_SET_TEMPERATURE_UNIT,
                mContext.getString(R.string.general_host_settings_temperature_unit),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        String[] tempUnits = mContext.getResources().getStringArray(R.array.temperature_unit);
                        String tempUnitString;
                        Temperatures currentTempType = mHostConfigModelData.getTemperatureUnit();
                        if (currentTempType == Temperatures.F) {
                            tempUnitString = tempUnits[0];
                        } else {
                            tempUnitString = tempUnits[1];
                        }
                        return tempUnitString;
                    }

                    @Override
                    public boolean isEnabled() {
                        return !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.RadioButton));
        return childMenu;
    }

    private ArrayList<MenuItem> createChildMenuForOperatorSettings() {
        ArrayList<MenuItem> childMenu = new ArrayList<>();
        //childMenu.add(new MenuItem("User name", mUserData.getUserName()));
        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_CHANGE_USERNAME,
                mContext.getString(R.string.general_host_settings_user_name),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return getLoggedInUser().getUserName();
                    }

                    @Override
                    public boolean isEnabled() {
                        return !getLoggedInUser().getUserId().equals(USER_ID_ANONYMOUS);
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.TextView));
        //childMenu.add(new MenuItem("Password", mUserData.getPassword()));
        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_CHANGE_PASSWORD,
                mContext.getString(R.string.general_host_settings_password),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        String hiddenPassword = "###########################";
                        User loggedInUser = getLoggedInUser();
                        int len = (loggedInUser.getPassword().length() < 16) ? loggedInUser.getPassword().length() : 16;
                        return hiddenPassword.substring(0, len - 1);
                    }

                    @Override
                    public boolean isEnabled() {
                        return !getLoggedInUser().getUserId().equals(USER_ID_ANONYMOUS) && !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.TextView));
        return childMenu;
    }

    private ArrayList<MenuItem> createChildMenuForLanguage() {
        ArrayList<MenuItem> childMenu = new ArrayList<>();
        //childMenu.add(new MenuItem("Language", "English"));
        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_SET_LANGUAGE,
                mContext.getString(R.string.general_host_settings_language),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        LanguageType langType = mDeviceSettingsModelData.getLanguageType();
                        return LanguageTypeUtil.fromLanguageTypeToLanguageString(mContext, langType);
                    }

                    @Override
                    public boolean isEnabled() {
                        return !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.Unknown));
        return childMenu;
    }

    private ArrayList<MenuItem> createChildMenuForUsers() {
        ArrayList<MenuItem> childMenu = new ArrayList<>();

        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_ADD_USER,
                mContext.getString(R.string.general_host_settings_user_add),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return null;
                    }

                    @Override
                    public boolean isEnabled() {
                        return !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.CustomLayout
        ));

        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_EDIT_USER,
                mContext.getString(R.string.general_host_settings_user_edit),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return null;
                    }

                    @Override
                    public boolean isEnabled() {
                        return !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.CustomLayout
        ));

        return childMenu;
    }

    private ArrayList<MenuItem> createChildMenuForWorkflows() {
        ArrayList<MenuItem> childMenu = new ArrayList<>();
        childMenu.add(new MenuItemDataEntity(MENU_ACTION_SET_WORKFLOW, mContext.getString(R.string.select_workflow), new IMenuItemFromDataEntityHelper() {
            @Override
            public String getValue() {
                return new WorkflowRepository().getActiveWorkflowName();
            }

            @Override
            public boolean isEnabled() {
                return !isReadOnly;
            }

            @Override
            public boolean isChecked() {
                return false;
            }
        }, UIEditFieldType.Unknown));

        return childMenu;
    }

    private ArrayList<MenuItem> createChildMenuForPrinters() {
        ArrayList<MenuItem> childMenu = new ArrayList<>();
        childMenu.add(new MenuItem(MENU_ACTION_SET_PRINTER_FEATURES, mContext.getString(R.string.general_host_settings_printer_inclusions), UIEditFieldType.Unknown));

        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_ADD_PRINTER,
                mContext.getString(R.string.general_host_settings_printer_add),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return null;
                    }

                    @Override
                    public boolean isEnabled() {
                        return !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.CustomLayout
        ));

        childMenu.add(new MenuItemDataEntity(
                MENU_ACTION_EDIT_PRINTER,
                mContext.getString(R.string.general_host_settings_printer_edit),
                new IMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return null;
                    }

                    @Override
                    public boolean isEnabled() {
                        return !isReadOnly;
                    }

                    @Override
                    public boolean isChecked() {
                        return false;
                    }
                }, UIEditFieldType.CustomLayout
        ));
        return childMenu;
    }

    private ArrayList<MenuItem> createChildMenuForBarcode() {
        ArrayList<MenuItem> childMenu = new ArrayList<>();
        childMenu.add(new MenuItem(MENU_ACTION_BARCODE_CHANGE_USERID, mContext.getString(R.string.general_host_settings_barcode_userID), "", UIEditFieldType.TextView));
        childMenu.add(new MenuItem(MENU_ACTION_BARCODE_CHANGE_PASSWORD, mContext.getString(R.string.general_host_settings_password), "", UIEditFieldType.TextView));
        childMenu.add(new MenuItem(MENU_ACTION_BARCODE_CHANGE_PATIENTID, mContext.getString(R.string.general_host_settings_barcode_patientID), "", UIEditFieldType.TextView));
        childMenu.add(new MenuItem(MENU_ACTION_BARCOE_CHANGE_FLUIDLOT, mContext.getString(R.string.general_host_settings_barcode_fluid_lot), "", UIEditFieldType.TextView));
        childMenu.add(new MenuItem(MENU_ACTION_BARCOE_CHANGE_ID2, mContext.getString(R.string.general_host_settings_barcode_ID2), "", UIEditFieldType.TextView));
        childMenu.add(new MenuItem(MENU_ACTION_BARCOE_CHANGE_COMMENTS, mContext.getString(R.string.general_host_settings_barcode_comments), "", UIEditFieldType.TextView));
        childMenu.add(new MenuItem(MENU_ACTION_BARCOE_CHANGE_OTHER, mContext.getString(R.string.general_host_settings_barcode_other), "", UIEditFieldType.TextView));
        return childMenu;
    }

    private ArrayList<MenuItem> createChildMenuForUpgrade() {
        ArrayList<MenuItem> childMenu = new ArrayList<>();
        MenuItem noUpgrade = new MenuItem(MENU_ACTION_NO_UPGRADE, mContext.getString(R.string.no_upgrade), UIEditFieldType.Unknown);
        noUpgrade.setEnabled(false);
        childMenu.add(noUpgrade);
        return childMenu;
    }

    private ArrayList<MenuGroup> createMenu() {
        if (getLoggedInUser().getUserId().equals(USER_ID_ANONYMOUS)) {
            return createLanguageMenu();
        }
        return createAdminMenu();
    }

    private User getLoggedInUser() {
        return new UserModel().getLoggedInUser();
    }

    /**
     * Create Menu that contains all entries (all parent menu and all child menu within parent menu)
     *
     * @return ArrayList<MenuGroup>
     */
    private ArrayList<MenuGroup> createAdminMenu() {
        ArrayList<MenuGroup> menu = new ArrayList<>();
        menu.add(new MenuGroup(mParentMenuTitleList.get(0), createChildMenuForNameSettings()));
        menu.add(new MenuGroup(mParentMenuTitleList.get(1), createChildMenuForGeneralHostSettings()));
        menu.add(new MenuGroup(mParentMenuTitleList.get(2), createChildMenuForOperatorSettings()));
        menu.add(new MenuGroup(mParentMenuTitleList.get(3), createChildMenuForLanguage()));
        menu.add(new MenuGroup(mParentMenuTitleList.get(4), createChildMenuForUsers()));
        menu.add(new MenuGroup(mParentMenuTitleList.get(5), createChildMenuForPrinters()));
        menu.add(new MenuGroup(mParentMenuTitleList.get(6), createChildMenuForBarcode()));
        menu.add(new MenuGroup(mParentMenuTitleList.get(7), createChildMenuForUpgrade()));
        menu.add(new MenuGroup(mParentMenuTitleList.get(8), createChildMenuForWorkflows()));
        return menu;
    }

    private ArrayList<MenuGroup> createLanguageMenu() {
        ArrayList<MenuGroup> menu = new ArrayList<>();
        menu.add(new MenuGroup(mParentMenuTitleList.get(3), createChildMenuForLanguage()));
        return menu;
    }

    public void setItemValueSelectedListener(ItemValueSelectedListener listener) {
        this.mItemValueSelectedListener = listener;
    }
}
