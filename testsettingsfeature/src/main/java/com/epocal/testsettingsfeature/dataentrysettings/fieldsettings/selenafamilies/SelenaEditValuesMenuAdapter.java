package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.selenafamilies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.epocal.common.realmentities.Selena;
import com.epocal.common.types.SelenaFamilyType;
import com.epocal.datamanager.SelenaModel;
import com.epocal.testsettingsfeature.R;

import java.util.ArrayList;


/**
 * HostSettingMenuAdapter is responsible for converting hostConfigData and userData
 * to the format to supply for ExpandableListAdapter
 * Created by Zeeshan A Zakaria on 09/08/2017.
 */

class SelenaEditValuesMenuAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SelenaEditValuesItem> mMenuList;
    private SelenaEditValuesFragment mEditValuesFragment;
    private SelenaFamilyType mSelenaFamilyType;

    SelenaEditValuesMenuAdapter(Context context, SelenaFamilyType selenaFamilyType, SelenaEditValuesFragment editValuesFragment) {
        mContext = context;
        mSelenaFamilyType = selenaFamilyType;
        mMenuList = createMenuList(mSelenaFamilyType);
        mEditValuesFragment = editValuesFragment;
    }

    void updateFields() {
        mMenuList = createMenuList(mSelenaFamilyType);
        notifyDataSetChanged();
    }

    /**
     * Gets the number of groups.
     *
     * @return the number of groups
     */
    @Override
    public int getCount() {
        return mMenuList.size();
    }

    /**
     *
     * @param groupPosition the position of the group for which the ID is wanted
     * @return the ID associated with the group
     */
    @Override
    public long getItemId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Gets a View that displays the data for the given child within the given
     * group.
     *
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.selena_edit_values_list_menuitem, parent, false);
        }

        TextView title = (TextView) view.findViewById(R.id.menu_item_title);
        CheckBox enabledCheckbox = (CheckBox) view.findViewById(R.id.checkbox_enabled);
        CheckBox selectedCheckbox = (CheckBox) view.findViewById(R.id.checkbox_selected);

        // Bind data to view
        final SelenaEditValuesItem valuesItem = mMenuList.get(position);

        final SelenaFamilyType selenaFamilyType = valuesItem.getSelenafamilyType();
        final int analyteId = valuesItem.geAnalyteId();
        int enablement = valuesItem.getEnablement();
        final String itemName = valuesItem.getTitle();

        title.setText(itemName);
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
                    Log.d("-->", "Selena item: " + itemName + (isChecked ? " enabled" : " disabled"));
                    mEditValuesFragment.switchEnabledState(itemName, selenaFamilyType, isChecked);
                }
            }
        });

        selectedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    Log.d("-->", "Selena item: " + itemName + (isChecked ? " selected" : " unselected"));
                    mEditValuesFragment.switchSelectedState(itemName, selenaFamilyType, isChecked);
                }
            }
        });

        return view;
    }

    /**
     * Create Menu that contains all entries for the Selena family options
     *
     * @return the menu
     */
    private ArrayList<SelenaEditValuesItem> createMenuList(SelenaFamilyType selenaFamilyType) {
        ArrayList<SelenaEditValuesItem> menu = new ArrayList<>();
        ArrayList<Selena> selenaFamily = new ArrayList<>();

        if (selenaFamilyType == SelenaFamilyType.DELIVERYSYSTEM) selenaFamily = new SelenaModel().getSelenaFamily(SelenaFamilyType.DELIVERYSYSTEM);
        else if (selenaFamilyType == SelenaFamilyType.RESPIRATORYMODE) selenaFamily = new SelenaModel().getSelenaFamily(SelenaFamilyType.RESPIRATORYMODE);
        else if (selenaFamilyType == SelenaFamilyType.ALLENSTYPE) selenaFamily = new SelenaModel().getSelenaFamily(SelenaFamilyType.ALLENSTYPE);
        else if (selenaFamilyType == SelenaFamilyType.DRAWSITE) selenaFamily = new SelenaModel().getSelenaFamily(SelenaFamilyType.DRAWSITE);

        for (Selena selena : selenaFamily) {
            String itemName = selena.getName();
            int enablement = selena.getEnabledSelectedOptionType().value;
            SelenaEditValuesItem item = new SelenaEditValuesItem(itemName, enablement, selenaFamilyType);
            menu.add(item);
        }

        return menu;
    }
}