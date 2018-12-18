package com.epocal.common_ui.types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UIValueBucketList implements Parcelable {
    private int mID;
    private String mTitle;
    private byte mIsExtendable;
    private ArrayList<UIValueBucket> mItemList;

    // CREATOR is omitted
    private UIValueBucketList(Parcel in) { readFromParcel(in); }

    public static final Creator<UIValueBucketList> CREATOR = new Creator<UIValueBucketList>() {
        @Override
        public UIValueBucketList createFromParcel(Parcel in) {
            return new UIValueBucketList(in);
        }

        @Override
        public UIValueBucketList[] newArray(int size) {
            return new UIValueBucketList[size];
        }
    };

    private void readFromParcel(Parcel in) {
        mID = in.readInt();
        mTitle = in.readString();
        mIsExtendable = in.readByte();
        if (mItemList == null) {
            mItemList = new ArrayList<>();
        }
        in.readTypedList(mItemList, UIValueBucket.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mID);
        out.writeString(mTitle);
        out.writeByte(mIsExtendable);
        out.writeTypedList(mItemList);
    }

    public UIValueBucketList()
    {

    }

    public int getID() { return mID; }
    public void setID(int id){ mID = id;}

    public String getTitle() { return mTitle; }
    public void setTitle(String title){ mTitle = title;}

    public boolean isExtendable() { return mIsExtendable != 0; }
    public void setExtendable(boolean extendable){ mIsExtendable = (byte)(extendable?1:0);}

    public ArrayList<UIValueBucket> getListItem() { return mItemList; }
    public void setListItem(ArrayList<UIValueBucket> list) { mItemList = list; }
}
