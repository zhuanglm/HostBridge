package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.realmentities.InputFieldConfig;
import com.epocal.common.types.InputFieldType;
import com.epocal.common.types.SelenaFamilyType;
import com.epocal.datamanager.HostConfigurationModel;
import com.epocal.datamanager.InputFieldConfigModel;
import com.epocal.datamanager.SelenaModel;
import com.epocal.testsettingsfeature.R;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * HostSettingMenuAdapter is responsible for converting hostConfigData and userData
 * to the format to supply for ExpandableListAdapter
 * Created on 08/06/2017.
 */

class FieldSettingsMenuAdapter extends BaseExpandableListAdapter {
    /**
     * List of constants to identify MENU ACTION in this adapter.
     * The value of long only needs to be unique within this file. (Does not need to be unique within app scope.)
     * The value of long does not need to be consecutive.
     */
    static final int MENU_ACTION_PATIENT_ID_MAXIMUM_LENGTH = 101;
    static final int MENU_ACTION_PATIENT_ID_MINIMUM_LENGTH = 102;
    static final int MENU_ACTION_PATIENT_ID_LOOKUP = 103;
    static final int MENU_ACTION_ID2_MAXIMUM_LENGTH = 104;
    static final int MENU_ACTION_ID2_MINIMUM_LENGTH = 105;
    static final int MENU_ACTION_DELIVERY_SYSTEM_ALLOW_USER_INPUT = 106;
    static final int MENU_ACTION_DELIVERY_SYSTEM_EDIT_VALUES = 107;
    static final int MENU_ACTION_DRAW_SITE_ALLOW_USER_INPUT = 108;
    static final int MENU_ACTION_DRAW_SITE_EDIT_VALUES = 109;
    static final int MENU_ACTION_RESPIRATORY_MODE_ALLOW_USER_INPUT = 110;
    static final int MENU_ACTION_RESPIRATORY_MODE_EDIT_VALUES = 111;
    static final int MENU_ACTION_ALLENS_TEST_ALLOW_USER_INPUT = 112;
    static final int MENU_ACTION_ALLENS_TEST_EDIT_VALUES = 113;
    static final int MENU_ACTION_HEMODILUTION = 114;
    static final int MENU_ACTION_TEST_SELECTION = 115;
    static final int MENU_ACTION_SAMPLE_TYPE = 116;
    private Context mContext;
    private ArrayList<String> mParentMenuTitleList;
    private ArrayList<FieldSettingsMenuGroup> mChildMenuList;
    private HostConfigurationModel mHostConfigurationModel;
    private FieldSettingsFragment mFieldSettingsFragment;

    FieldSettingsMenuAdapter(Context context, HostConfigurationModel hostConfigModelData, FieldSettingsFragment fieldSettingsFragment) {
        mContext = context;
        mHostConfigurationModel = hostConfigModelData;
        mParentMenuTitleList = createParentMenuList();
        mChildMenuList = createChildMenuList();
        mFieldSettingsFragment = fieldSettingsFragment;
    }

    void updateFields() {
        mChildMenuList = createChildMenuList();
        notifyDataSetChanged();
    }

    /**
     * Gets the number of groups.
     *
     * @return the number of groups
     */
    @Override
    public int getGroupCount() {
        return mChildMenuList.size();
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

        FieldSettingsMenuGroup group = (FieldSettingsMenuGroup) getGroup(groupPosition);
        int groupId = group.getGroupId();

        if (groupId == MENU_ACTION_TEST_SELECTION) { // Don't expand the group if it is the Test Selection item.
            return 0;
        } else {
            FieldSettingsMenuGroup parent = mChildMenuList.get(groupPosition);
            return parent.getChildMenuList().size();
        }
    }

    /**
     * Gets the data associated with the given group.
     *
     * @param groupPosition the position of the group
     * @return the data child for the specified group
     */
    @Override
    public Object getGroup(int groupPosition) {
        return mChildMenuList.get(groupPosition);
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
        FieldSettingsMenuGroup group = (FieldSettingsMenuGroup) getGroup(groupPosition);
        return group.getChildMenuList().get(childPosition);
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
        FieldSettingsMenuGroup group = (FieldSettingsMenuGroup) getGroup(groupPosition);
        int groupId = group.getGroupId();
        return (groupId > 0 ? groupId : groupPosition);
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
        FieldSettingsMenuItem item = (FieldSettingsMenuItem) getChild(groupPosition, childPosition);
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
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null)
                convertView = layoutInflater.inflate(R.layout.field_settings_list_menugroup, parent, false);
            else
                return null;
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.menugroup_title);

        // Bind data to view
        FieldSettingsMenuGroup headerMenu = (FieldSettingsMenuGroup) getGroup(groupPosition);
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
     *                      {#getChildView(int, int, boolean, View, ViewGroup)}.
     * @param parent        the parent that this view will eventually be attached to
     * @return the View corresponding to the child at the specified position
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null)
                convertView = layoutInflater.inflate(R.layout.field_settings_list_menuitem, parent, false);
            else
                return null;
        }

        LinearLayout view1 = convertView.findViewById(R.id.menuitem_1);
        LinearLayout view2 = convertView.findViewById(R.id.menuitem_2);

        TextView titleView = convertView.findViewById(R.id.menuitem_title);
        TextView valueView = convertView.findViewById(R.id.menuitem_value);
        TextView actionTitleView = convertView.findViewById(R.id.menuitem_action_title);
        SwitchCompat switchCompat = convertView.findViewById(R.id.switch_yes_no);

        // Bind data to view
        FieldSettingsMenuItem item = (FieldSettingsMenuItem) getChild(groupPosition, childPosition);

        final long itemId = item.getItemId();
        final String itemValue = item.getValue();
        String itemTitle = item.getTitle();

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFieldSettingsFragment.receiveSwitchToggle(itemId, isChecked);
            }
        });

        View visibleView;
        if (item.getValue() == null) {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            visibleView = view2;
        } else {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            visibleView = view1;
        }
        if (item.isEnabled()) {
            visibleView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAlereLightGray));
        } else {
            visibleView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray50));
        }

        if (
                itemId == MENU_ACTION_PATIENT_ID_LOOKUP ||
                        itemId == MENU_ACTION_DELIVERY_SYSTEM_ALLOW_USER_INPUT ||
                        itemId == MENU_ACTION_DRAW_SITE_ALLOW_USER_INPUT ||
                        itemId == MENU_ACTION_RESPIRATORY_MODE_ALLOW_USER_INPUT ||
                        itemId == MENU_ACTION_ALLENS_TEST_ALLOW_USER_INPUT
                ) {

            if (itemValue != null && itemValue.equals(mContext.getString(R.string.no))) {
                switchCompat.setChecked(false);
            } else {
                switchCompat.setChecked(true);
            }

            valueView.setVisibility(View.GONE);
            switchCompat.setVisibility(View.VISIBLE);
        } else {
            valueView.setVisibility(View.VISIBLE);
            switchCompat.setVisibility(View.GONE);
        }

        titleView.setText(itemTitle);
        valueView.setText(itemValue);
        actionTitleView.setText(itemTitle);
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
        FieldSettingsMenuItem item = (FieldSettingsMenuItem) getChild(groupPosition, childPosition);
        return item.isEnabled();
    }

    private ArrayList<String> createParentMenuList() {
        return new ArrayList<>(
                Arrays.asList(
                        mContext.getString(R.string.patient_id),
                        mContext.getString(R.string.id2),
                        mContext.getString(R.string.test_selection),
                        mContext.getString(R.string.sample_type),
                        mContext.getString(R.string.delivery_system),
                        mContext.getString(R.string.draw_site),
                        mContext.getString(R.string.respiratory_mode),
                        mContext.getString(R.string.allens_test)
                )
        );
    }

    /**
     * Create Menu that contains all entries (all parent menu and all child menu within parent menu)
     *
     * @return the menu
     */
    private ArrayList<FieldSettingsMenuGroup> createChildMenuList() {
        ArrayList<FieldSettingsMenuGroup> menu = new ArrayList<>();
        menu.add(new FieldSettingsMenuGroup(mParentMenuTitleList.get(0), createChildMenuPatientId()));
        menu.add(new FieldSettingsMenuGroup(mParentMenuTitleList.get(1), createChildMenuForID2()));
        menu.add(new FieldSettingsMenuGroup(mParentMenuTitleList.get(2), createChildMenuForTestSelection(), MENU_ACTION_TEST_SELECTION));
        menu.add(new FieldSettingsMenuGroup(mParentMenuTitleList.get(3), createChildMenuForSampleType()));
        menu.add(new FieldSettingsMenuGroup(mParentMenuTitleList.get(4), createChildMenuForDeliverySystem()));
        menu.add(new FieldSettingsMenuGroup(mParentMenuTitleList.get(5), createChildMenuForDrawSite()));
        menu.add(new FieldSettingsMenuGroup(mParentMenuTitleList.get(6), createChildMenuForRepositoryMode()));
        menu.add(new FieldSettingsMenuGroup(mParentMenuTitleList.get(7), createChildMenuForAllensTest()));
        return menu;
    }

    private String booleanToYesNo(boolean boolVal) {
        return boolVal ? mContext.getResources().getString(R.string.yes) : mContext.getResources().getString(R.string.no);
    }

    /**
     * The patient id menu
     *
     * @return the menu items
     */
    private ArrayList<FieldSettingsMenuItem> createChildMenuPatientId() {
        InputFieldConfigModel inputFieldConfigModel = new InputFieldConfigModel();
        final InputFieldConfig inputFieldConfig = inputFieldConfigModel.getInputFieldConfig(InputFieldType.PATIENT_ID);
        ArrayList<FieldSettingsMenuItem> childMenu = new ArrayList<>();

        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_PATIENT_ID_MAXIMUM_LENGTH,
                mContext.getString(R.string.maximum_length),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        Integer value = inputFieldConfig.getMaximumLength();
                        return (value != null ? Integer.toString(value) : "null");
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));

        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_PATIENT_ID_MINIMUM_LENGTH,
                mContext.getString(R.string.minimum_length),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        Integer value = inputFieldConfig.getMinimumLength();
                        return (value != null ? Integer.toString(value) : "null");
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));

        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_PATIENT_ID_LOOKUP,
                mContext.getString(R.string.patient_id_lookup),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        boolean value = mHostConfigurationModel.isEnablePatientIdLookup();
                        return (value ? mContext.getString(R.string.yes) : mContext.getString(R.string.no));
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));

        return childMenu;
    }

    /**
     * The ID2 menu
     *
     * @return the menu items
     */
    private ArrayList<FieldSettingsMenuItem> createChildMenuForID2() {
        InputFieldConfigModel inputFieldConfigModel = new InputFieldConfigModel();
        final InputFieldConfig inputFieldConfig = inputFieldConfigModel.getInputFieldConfig(InputFieldType.ID_2);

        ArrayList<FieldSettingsMenuItem> childMenu = new ArrayList<>();
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_ID2_MAXIMUM_LENGTH,
                mContext.getString(R.string.maximum_length),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        Integer value = inputFieldConfig.getMaximumLength();
                        return (value != null ? Integer.toString(value) : "null");
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));

        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_ID2_MINIMUM_LENGTH,
                mContext.getString(R.string.minimum_length),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        Integer value = inputFieldConfig.getMinimumLength();
                        return (value != null ? Integer.toString(value) : "null");
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));

        return childMenu;
    }

    /**
     * The Test Selection menu
     *
     * @return the menu items
     */
    private ArrayList<FieldSettingsMenuItem> createChildMenuForTestSelection() {
        ArrayList<FieldSettingsMenuItem> childMenu = new ArrayList<>();
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_PATIENT_ID_MAXIMUM_LENGTH,
                mContext.getString(R.string.UnknownStatus),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return "in progress";
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));
        return childMenu;
    }

    /**
     * The Sample Type menu
     *
     * @return the menu items
     */
    private ArrayList<FieldSettingsMenuItem> createChildMenuForSampleType() {
        ArrayList<FieldSettingsMenuItem> childMenu = new ArrayList<>();
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_PATIENT_ID_MAXIMUM_LENGTH,
                mContext.getString(R.string.UnknownStatus),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return "in progress";
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));
        return childMenu;
    }

    /**
     * The Delivery System Settings menu
     *
     * @return the menu items
     */
    private ArrayList<FieldSettingsMenuItem> createChildMenuForDeliverySystem() {
        final boolean value = new SelenaModel().isCustomEntryAllowed(SelenaFamilyType.DELIVERYSYSTEM);

        ArrayList<FieldSettingsMenuItem> childMenu = new ArrayList<>();
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_DELIVERY_SYSTEM_ALLOW_USER_INPUT,
                mContext.getString(R.string.allow_user_input),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return (value ? mContext.getString(R.string.yes) : mContext.getString(R.string.no));
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_DELIVERY_SYSTEM_EDIT_VALUES,
                mContext.getString(R.string.edit_values),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return "";
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));
        return childMenu;
    }

    /**
     * The Draw Site menu
     *
     * @return the menu items
     */
    private ArrayList<FieldSettingsMenuItem> createChildMenuForDrawSite() {
        final boolean value = new SelenaModel().isCustomEntryAllowed(SelenaFamilyType.DRAWSITE);

        ArrayList<FieldSettingsMenuItem> childMenu = new ArrayList<>();
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_DRAW_SITE_ALLOW_USER_INPUT,
                mContext.getString(R.string.allow_user_input),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return (value ? mContext.getString(R.string.yes) : mContext.getString(R.string.no));
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_DRAW_SITE_EDIT_VALUES,
                mContext.getString(R.string.edit_values),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return "";
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));
        return childMenu;
    }

    /**
     * The Repository Mode menu
     *
     * @return the menu items
     */
    private ArrayList<FieldSettingsMenuItem> createChildMenuForRepositoryMode() {
        final boolean value = new SelenaModel().isCustomEntryAllowed(SelenaFamilyType.RESPIRATORYMODE);
        ArrayList<FieldSettingsMenuItem> childMenu = new ArrayList<>();
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_RESPIRATORY_MODE_ALLOW_USER_INPUT,
                mContext.getString(R.string.allow_user_input),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return (value ? mContext.getString(R.string.yes) : mContext.getString(R.string.no));
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_RESPIRATORY_MODE_EDIT_VALUES,
                mContext.getString(R.string.edit_values),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return "";
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));
        return childMenu;
    }

    /**
     * The Allen's Test menu
     *
     * @return the menu items
     */
    private ArrayList<FieldSettingsMenuItem> createChildMenuForAllensTest() {
        final boolean value = new SelenaModel().isCustomEntryAllowed(SelenaFamilyType.ALLENSTYPE);

        ArrayList<FieldSettingsMenuItem> childMenu = new ArrayList<>();
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_ALLENS_TEST_ALLOW_USER_INPUT,
                mContext.getString(R.string.allow_user_input),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return (value ? mContext.getString(R.string.yes) : mContext.getString(R.string.no));
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));
        childMenu.add(new FieldSettingsMenuItemDataEntity(
                MENU_ACTION_ALLENS_TEST_EDIT_VALUES,
                mContext.getString(R.string.edit_values),
                new IFieldSettingsMenuItemFromDataEntityHelper() {
                    @Override
                    public String getValue() {
                        return "";
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                }));
        return childMenu;
    }


}
