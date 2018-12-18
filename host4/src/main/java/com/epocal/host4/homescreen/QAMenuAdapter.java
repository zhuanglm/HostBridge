package com.epocal.host4.homescreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epocal.host4.R;

import java.util.ArrayList;

public class QAMenuAdapter extends ArrayAdapter<QAMenuModel> {
    private final Context context;
    private final ArrayList<QAMenuModel> modelsArrayList;

    public QAMenuAdapter(Context context, ArrayList<QAMenuModel> modelsArrayList) {

        super(context, R.layout.activity_qatest_menu_item, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
        if(!modelsArrayList.get(position).isGroupHeader()){
            rowView = inflater.inflate(R.layout.activity_qatest_menu_item, parent, false);

            // 3. Get icon,title & counter views from the rowView
            ImageView imgView = rowView.findViewById(R.id.item_icon);
            TextView titleView = rowView.findViewById(R.id.item_title);

            // 4. Set the text for textView
            imgView.setImageResource(modelsArrayList.get(position).getIcon());
            titleView.setText(modelsArrayList.get(position).getTitle());
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QAMenuModel.MenuListener listener = modelsArrayList.get(position).getActionListener();
                    if (listener != null)
                        listener.onMenuClicked();
                }
            });
        }
        else{
            rowView = inflater.inflate(R.layout.activity_qatest_menu_headeritem, parent, false);
            TextView titleView = rowView.findViewById(R.id.header);
            titleView.setText(modelsArrayList.get(position).getTitle());
            titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QAMenuModel.MenuListener listener = modelsArrayList.get(position).getActionListener();
                    if (listener != null)
                        listener.onMenuClicked();
                }
            });
        }

        // 5. return rowView
        return rowView;
    }
}
