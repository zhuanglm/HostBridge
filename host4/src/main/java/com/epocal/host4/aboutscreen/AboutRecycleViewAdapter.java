package com.epocal.host4.aboutscreen;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epocal.common.realmentities.User;
import com.epocal.host4.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


/**
 * Created by bmate on 3/28/2017.
 */

public class AboutRecycleViewAdapter extends RealmRecyclerViewAdapter<User,AboutRecycleViewAdapter.MyViewHolder>{
    private final AboutActivity aboutActivity;

    public AboutRecycleViewAdapter(AboutActivity activity, OrderedRealmCollection<User> data){
        super(data,true);
        this.aboutActivity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_about_item,null);

        // create ViewHolder

        MyViewHolder myViewHolder = new AboutRecycleViewAdapter.MyViewHolder(itemLayoutView, aboutActivity);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User displayedItem = getData().get(position);
        holder.textViewItem.setText(displayedItem.getUserId());
        holder.textViewPersonName.setText(displayedItem.getUserName());
        holder.ownerId = displayedItem.getId();
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private long ownerId;

        public AboutActivity aboutActivity;
        public TextView textViewItem;
        public TextView textViewPersonName;
        public View viewParent;

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(mainActivity, OwnerManagementActivity.class);
//            intent.putExtra("userId", ownerId);
//            mainActivity.startActivityForResult(intent, 1);

        }
        public MyViewHolder(View itemLayoutView, AboutActivity aboutActivity) {
            super(itemLayoutView);
            this.aboutActivity = aboutActivity ;
            textViewItem = (TextView) itemLayoutView.findViewById(R.id.activity_about_item_userID);
            textViewPersonName = (TextView) itemLayoutView.findViewById(R.id.activity_about_item_user_name);
            viewParent = itemLayoutView.findViewById(R.id.activity_about_item_parent);
            viewParent.setOnClickListener(this);

        }

        public void setUserId(long userId) {
            this.ownerId = ownerId;
        }
    }
}
