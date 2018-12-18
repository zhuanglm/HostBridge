package com.epocal.hostsettings;

import android.test.ActivityUnitTestCase;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.epocal.common.realmentities.User;
import com.epocal.hostsettings.R;

import java.util.ArrayList;

/**
 * Created by michde on 1/30/2018.
 */

public class UserListAdapter extends ArrayAdapter<User> {
    private final Activity mContext;
    private final ArrayList<User> mUsers;

    public UserListAdapter(Activity context, ArrayList<User> users) {
        super(context, R.layout.host_settings_user_list_menu_item, users);
        mContext = context;
        mUsers = users;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.host_settings_user_list_menu_item, null, true);
        TextView tv = (TextView)rowView.findViewById(R.id.host_settings_user_list_item);

        if (position < mUsers.size())
            tv.setText(mUsers.get(position).getUserName());

        return rowView;
    }
}
