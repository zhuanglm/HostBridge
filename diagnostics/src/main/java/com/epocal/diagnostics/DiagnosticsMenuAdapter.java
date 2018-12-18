package com.epocal.diagnostics;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class DiagnosticsMenuAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> mParentMenuList;
    private HashMap<String, List> mChildMenuMap;
    private Pair<String, String> childEntry;

    public DiagnosticsMenuAdapter(Context context, List menuList, HashMap childMenuMap) {
        mContext = context;
        mParentMenuList = (List<String>)menuList;
        mChildMenuMap = (HashMap<String, List>)childMenuMap;
    }

    @Override
    public int getGroupCount() {
        return mParentMenuList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            String parentMenu = mParentMenuList.get(groupPosition);
            List childMenuList = mChildMenuMap.get(parentMenu);
            return (childMenuList == null) ? 0 : childMenuList.size();
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mParentMenuList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildMenuMap.get(getGroup(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (infalInflater != null)
                convertView = infalInflater.inflate(R.layout.diagnostics_list_group, null);
            else
                return null;
        }
        String headerTitle = (String) getGroup(groupPosition);
        TextView headerTextView = convertView.findViewById(R.id.tv_diagnostics_menu_title);
        headerTextView.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (infalInflater != null)
                convertView = infalInflater.inflate(R.layout.diagnostics_list_item, null);
            else
                return null;
        }

        Pair<String, String> childEntry = (Pair<String, String>) getChild(groupPosition, childPosition);

        TextView titleTextView = convertView.findViewById(R.id.tv_diagnostics_submenu_title);
        TextView valueTextView = convertView.findViewById(R.id.tv_diagnostics_value);

        titleTextView.setText(childEntry.first);
        valueTextView.setText(childEntry.second);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
