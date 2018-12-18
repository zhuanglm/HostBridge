package com.epocal.hostsettings;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.epocobjects.IBaseActivity;
import com.epocal.common_ui.types.UIEditFieldType;
import com.epocal.common_ui.types.UIValueBucket;
import com.epocal.common_ui.types.UIValueBucketList;

import java.util.ArrayList;


/**
 * HostSettingMenuAdapter is responsible for converting hostConfigData and userData
 * to the format to supply for ExpandableListAdapter
 * Created on 08/06/2017.
 */

class GeneralSettingOptionsExpandableAdapter extends BaseExpandableListAdapter {

    public interface IChangedCallback {
        void onChanged(long key, int value);
    }

    private GeneralSettingOptionsExpandableAdapter.IChangedCallback mIChangedCallback;

    public void setIChangedCallback(IChangedCallback iChangedCallback) {
        this.mIChangedCallback = iChangedCallback;
    }

    private Context mContext;
    private ArrayList<UIValueBucketList> mMenuList;

    private boolean isReadOnly = false;
    private ItemValueSelectedListener mItemValueSelectedListener;

    public interface ItemValueSelectedListener {
        public void onValueSelectedListener(int itemId, String input);
    }

    GeneralSettingOptionsExpandableAdapter(Context context, ArrayList<UIValueBucketList> menuList) {
        mContext = context;
        if (context instanceof IBaseActivity) {
            isReadOnly = ((IBaseActivity) context).isReadOnly();
        }
        mMenuList = menuList;
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
        UIValueBucketList parent = mMenuList.get(groupPosition);
        return parent.getListItem().size();
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
        UIValueBucketList parent = (UIValueBucketList) getGroup(groupPosition);
        return parent.getListItem().get(childPosition);
    }

    public Object getChild(int itemid)
    {
        for(int i = 0; i < getGroupCount(); i++)
        {
            UIValueBucketList group = (UIValueBucketList)getGroup(i);
            for(int j = 0; j < group.getListItem().size(); j++)
            {
                if (group.getListItem().get(j).getID() == itemid)
                {
                    return group.getListItem().get(j);
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
        UIValueBucket item = (UIValueBucket) getChild(groupPosition, childPosition);
        return item.getID();
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
            convertView = infalInflater.inflate(R.layout.host_settings_list_menugroup, parent, false);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.menu_group_title);

        // Bind data to view
        UIValueBucketList headerMenu = (UIValueBucketList) getGroup(groupPosition);
        lblListHeader.setText(headerMenu.getTitle());
        lblListHeader.setTextColor(Color.BLACK);
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
     *                      {@link #getChildView(int, int, boolean, View, ViewGroup)}.
     * @param parent        the parent that this view will eventually be attached to
     * @return the View corresponding to the child at the specified position
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.general_setting_adapter_item, parent, false);
        }

        TextView view_edit_text = (TextView) convertView.findViewById(R.id.menuitem_edit_switch_title);
        SwitchCompat viewSwitchCompat = (SwitchCompat) convertView.findViewById(R.id.edit_switch_switcher);

        // Bind data to view
        UIValueBucket item = (UIValueBucket) getChild(groupPosition, childPosition);
        view_edit_text.setText(item.getTextString());
        viewSwitchCompat.setTag(R.id.edit_switch_switcher, item.getKey());
        if (item.getValueString() == "TRUE")
        {
            viewSwitchCompat.setChecked(true);
        }
        else {
            viewSwitchCompat.setChecked(false);
        }
        if(viewSwitchCompat != null) {
            viewSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    long key = (long)buttonView.getTag(R.id.edit_switch_switcher);
                    if (mIChangedCallback != null) {
                        if (!isChecked)
                        {
                            mIChangedCallback.onChanged(key, 0);
                        }
                        else
                        {
                            mIChangedCallback.onChanged(key, 1);
                        }
                    }
                }
            });
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
        UIValueBucket item = (UIValueBucket) getChild(groupPosition, childPosition);
        return item.isEnabled();
    }

    private ArrayList<UIValueBucketList> createMenu() {
        ArrayList<UIValueBucket> somethingList = new ArrayList<UIValueBucket>();
        UIValueBucket pair = new UIValueBucket();
        pair.setID(0);
        pair.setTextString("Print Range If High and 00000?");
        pair.setValueString("true");
        pair.settUIEditFieldType(UIEditFieldType.CheckBox);
        somethingList.add(pair);

        pair = new UIValueBucket();
        pair.setID(1);
        pair.setTextString("Print Range sfadf 11111?");
        pair.setValueString("true");
        pair.settUIEditFieldType(UIEditFieldType.CheckBox);
        somethingList.add(pair);

        pair = new UIValueBucket();
        pair.setID(2);
        pair.setTextString("Print Range sfadf 22222?");
        pair.setValueString("true");
        pair.settUIEditFieldType(UIEditFieldType.CheckBox);
        somethingList.add(pair);

        UIValueBucketList list = new UIValueBucketList();
        list.setListItem(somethingList);
        list.setTitle("test title");
        list.setExtendable(true);
        list.setID(0);
        ArrayList<UIValueBucketList> l = new ArrayList<UIValueBucketList>();
        l.add(list);
        return l;
    }

}
