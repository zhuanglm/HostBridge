package com.epocal.testhistoryfeature.data.presetfilterdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.epocal.testhistoryfeature.R;

/**
 * This class represents the adapter for the list of pre set filters (search filter)
 * in the drop-down menu in Main Screen.
 *
 * @see com.epocal.testhistoryfeature.ui.TestHistoryMainActivity
 */
public class PresetFilterDataAdapter extends BaseAdapter {
    private Context mContext;
    private List<PresetFilterData> mItems;

    public PresetFilterDataAdapter(Context context, List<PresetFilterData> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public PresetFilterData getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.preset_filter_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bind(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        TextView  titleView;
        ImageView iconView;

        ViewHolder(View view) {
            titleView = view.findViewById(R.id.title);
            iconView = view.findViewById(R.id.icon);
        }

        void bind(PresetFilterData item) {
            titleView.setText(item.getTitle());
            if (!item.isSelected()) {
                iconView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
