package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.testselection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.epocal.common.realmentities.Analyte;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.testsettingsfeature.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * HostSettingMenuAdapter is responsible for converting hostConfigData and userData
 * to the format to supply for ExpandableListAdapter
 * Created on 08/06/2017.
 */

class TestSelectionMenuAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<String> mParentMenuTitleList;
    private ArrayList<TestSelectionGroupItem> mChildMenuList;
    private TestSelectionFragment mTestSelectionFragment;
    private TestSelectionModel mTestSelectionModel;
    private static final int NO_ANALYTES_ENABLED_OR_SELECTED = 0;
    private static final int ALL_ANALYTES_ENABLED_AND_SELECTED = 1;
    private static final int ALL_ANALYTES_ENABLED_BUT_NOT_SELECTED = 2;
    private static final int SOME_ANALYTES_SELECTED_AND_SOME_ENABLED = 3;
    private static final int ALL_ANALYTES_ENABLED_AND_SOME_SELECTED = 4;
    private static final int SOME_ANALYTES_ENABLED_BUT_NONE_SELECTED = 5;

    TestSelectionMenuAdapter(Context context, TestSelectionModel testSelectionModel, TestSelectionFragment testSelectionFragment) {
        mContext = context;
        mTestSelectionModel = testSelectionModel;
        mParentMenuTitleList = createParentMenuList(testSelectionModel);
        mChildMenuList = createChildMenuList();
        mTestSelectionFragment = testSelectionFragment;
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
        TestSelectionGroupItem parent = mChildMenuList.get(groupPosition);
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
        TestSelectionGroupItem parent = (TestSelectionGroupItem) getGroup(groupPosition);
        return parent.getChildMenuList().get(childPosition);
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
        TestSelectionChildItem item = (TestSelectionChildItem) getChild(groupPosition, childPosition);
        return item.geAnalyteId();
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
     * @param view          the old view to reuse, if possible. You should check
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
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.test_selection_list_menugroup, parent, false);

        }

        final TextView analyteGroupName = (TextView) view.findViewById(R.id.menu_group_title);
        CheckBox enabledCheckbox = (CheckBox) view.findViewById(R.id.checkbox_enabled);
        CheckBox selectedCheckbox = (CheckBox) view.findViewById(R.id.checkbox_selected);

        // Bind data to view
        TestSelectionGroupItem groupHeader = (TestSelectionGroupItem) getGroup(groupPosition);
        final String analyteGroup = groupHeader.getTitle();
        analyteGroupName.setText(analyteGroup);

        int enablement = groupHeader.getEnablement();
        enabledCheckbox.setAlpha(1);
        selectedCheckbox.setAlpha(1);

        switch (enablement) {
            case ALL_ANALYTES_ENABLED_AND_SELECTED:
                enabledCheckbox.setChecked(true);
                selectedCheckbox.setChecked(true);
                break;
            case NO_ANALYTES_ENABLED_OR_SELECTED:
                enabledCheckbox.setChecked(false);
                selectedCheckbox.setChecked(false);
                break;
            case ALL_ANALYTES_ENABLED_AND_SOME_SELECTED:
                enabledCheckbox.setChecked(true);
                selectedCheckbox.setChecked(true);
                selectedCheckbox.setAlpha(.5f);
                break;
            case SOME_ANALYTES_SELECTED_AND_SOME_ENABLED:
                enabledCheckbox.setChecked(true);
                enabledCheckbox.setAlpha(.5f);
                selectedCheckbox.setChecked(true);
                selectedCheckbox.setAlpha(.5f);
                break;
            case ALL_ANALYTES_ENABLED_BUT_NOT_SELECTED:
                enabledCheckbox.setChecked(true);
                selectedCheckbox.setChecked(false);
                break;
            case SOME_ANALYTES_ENABLED_BUT_NONE_SELECTED:
                enabledCheckbox.setChecked(true);
                enabledCheckbox.setAlpha(.5f);
                selectedCheckbox.setChecked(false);
                break;
        }

        enabledCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    Log.d("-->", "Analyte Group: " + analyteGroup + (isChecked ? " enabled" : " disabled"));
                    mTestSelectionFragment.switchEnabledState(mTestSelectionModel.getAnalyteNamesFromGroup(analyteGroup), isChecked);
                }
            }
        });

        selectedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    Log.d("-->", "Analyte: " + analyteGroup + (isChecked ? " selected" : " unselected"));
                    mTestSelectionFragment.switchSelectedState(mTestSelectionModel.getAnalyteNamesFromGroup(analyteGroup), isChecked);
                }
            }
        });

        return view;
    }

    /**
     * Gets a View that displays the data for the given child within the given
     * group.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child (for which the View is
     *                      returned) within the group
     * @param isLastChild   Whether the child is the last child within the group
     * @param view          the old view to reuse, if possible. You should check
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.test_selection_list_menuitem, parent, false);
        }

        TextView title = (TextView) view.findViewById(R.id.menu_item_title);
        CheckBox enabledCheckbox = (CheckBox) view.findViewById(R.id.checkbox_enabled);
        CheckBox selectedCheckbox = (CheckBox) view.findViewById(R.id.checkbox_selected);

        // Bind data to view
        final TestSelectionChildItem analyte = (TestSelectionChildItem) getChild(groupPosition, childPosition);

        final int analyteId = analyte.geAnalyteId();
        int enablement = analyte.getEnablement();
        final AnalyteName analyteName = AnalyteName.fromInt(analyteId);

        title.setText(analyteName.name());
        enabledCheckbox.setEnabled(true);
        switch (enablement) {
            case 1:
                selectedCheckbox.setEnabled(false);
                enabledCheckbox.setChecked(false);
                selectedCheckbox.setChecked(false);
                break;
            case 2:
                selectedCheckbox.setEnabled(true);
                enabledCheckbox.setChecked(true);
                selectedCheckbox.setChecked(true);
                break;
            case 3:
                selectedCheckbox.setEnabled(true);
                enabledCheckbox.setChecked(true);
                selectedCheckbox.setChecked(false);
                break;
        }

        enabledCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    Log.d("-->", "Analyte: " + analyteName.name() + (isChecked ? " enabled" : " disabled"));
                    mTestSelectionFragment.switchEnabledState(analyteName, isChecked);
                }
            }
        });

        selectedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    Log.d("-->", "Analyte: " + analyteName + (isChecked ? " selected" : " unselected"));
                    mTestSelectionFragment.switchSelectedState(analyteName, isChecked);
                }
            }
        });

        return view;
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
        TestSelectionChildItem item = (TestSelectionChildItem) getChild(groupPosition, childPosition);
        return item.isEnabled();
    }

    private ArrayList<String> createParentMenuList(TestSelectionModel testSelection) {
        String[] headers = testSelection.getHeaders();
        return new ArrayList<>(Arrays.asList(headers));
    }

    /**
     * Create Menu that contains all entries (all parent menu and all child menu within parent menu)
     *
     * @return the menu
     */
    private ArrayList<TestSelectionGroupItem> createChildMenuList() {
        ArrayList<TestSelectionGroupItem> menu = new ArrayList<>();
        HashMap<String, ArrayList<Analyte>> listOfAnalytes = mTestSelectionModel.getAnalytes();

        int counter1 = mParentMenuTitleList.size();

        for (int i = 0; i < counter1; i++) {
            String parentItem = mParentMenuTitleList.get(i);
            ArrayList<Analyte> analytes = listOfAnalytes.get(parentItem);
            ArrayList<TestSelectionChildItem> testSelectionChildItems = new ArrayList<>();
            int[] groupEnablement = new int[analytes.size()];
            int counter2 = 0;

            for (Analyte analyte : analytes) {
                String analyteName = analyte.getAnalyteName().toString();
                int enablement = analyte.getOptionType().value;
                int analyteId = analyte.getAnalyteName().value;
                groupEnablement[counter2++] = enablement;

                TestSelectionChildItem item = new TestSelectionChildItem(i, analyteName, enablement, analyteId);
                testSelectionChildItems.add(item);
            }

            int value = groupEnablementValue(groupEnablement);

            menu.add(new TestSelectionGroupItem(parentItem, testSelectionChildItems, value));
        }
        return menu;
    }

    private int groupEnablementValue(int[] groupEnablement) {
        int disabled = 0;
        int enabledSelected = 0;
        int enabledUnselected = 0;
        int arraySize = groupEnablement.length;

        for (int i : groupEnablement) {
            if (i == EnabledSelectedOptionType.Disabled.value) {
                disabled++;
            }
            else if (i == EnabledSelectedOptionType.EnabledSelected.value) {
                enabledSelected++;
            }
            else if (i == EnabledSelectedOptionType.EnabledUnselected.value) {
                enabledUnselected++;
            }
        }

        if (arraySize == disabled) {
            return NO_ANALYTES_ENABLED_OR_SELECTED;
        } else if (arraySize == enabledSelected) {
            return ALL_ANALYTES_ENABLED_AND_SELECTED;
        } else if (arraySize == enabledUnselected) {
            return ALL_ANALYTES_ENABLED_BUT_NOT_SELECTED;
        } else if (arraySize == (enabledSelected + enabledUnselected)) {
            return ALL_ANALYTES_ENABLED_AND_SOME_SELECTED;
        } else if (arraySize == (enabledUnselected + disabled)) {
            return SOME_ANALYTES_ENABLED_BUT_NONE_SELECTED;
        } else if (arraySize == (enabledSelected + enabledUnselected + disabled)) {
            return SOME_ANALYTES_SELECTED_AND_SOME_ENABLED;
        }

        return 0;
    }
}