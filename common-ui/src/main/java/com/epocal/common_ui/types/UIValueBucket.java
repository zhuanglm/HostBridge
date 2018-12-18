package com.epocal.common_ui.types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.EnumSet;

public class UIValueBucket implements Parcelable {
    private int mId;
    private long mKey;
    private String mTextString;
    private String mValueString;
    private int mUIEditFieldType = UIEditFieldType.Unknown.value;
    private byte mIsEnabled;

    protected UIValueBucket(Parcel in) {
        mId = in.readInt();
        mKey = in.readLong();
        mTextString = in.readString();
        mValueString = in.readString();
        mUIEditFieldType = in.readInt();
        mIsEnabled = in.readByte();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeLong(mKey);
        dest.writeString(mTextString);
        dest.writeString(mValueString);
        dest.writeInt(mUIEditFieldType);
        dest.writeInt(mIsEnabled);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UIValueBucket> CREATOR = new Creator<UIValueBucket>() {
        @Override
        public UIValueBucket createFromParcel(Parcel in) {
            return new UIValueBucket(in);
        }

        @Override
        public UIValueBucket[] newArray(int size) {
            return new UIValueBucket[size];
        }
    };

    public UIEditFieldType getUIEditFieldType() { return UIEditFieldType.fromInt(mId); }
    public void settUIEditFieldType(UIEditFieldType uiEditFieldType){ mId = uiEditFieldType.value;}

    public String getTextString() { return mTextString; }
    public void setTextString(String textString){ mTextString = textString;}

    public String getValueString() { return mValueString; }
    public void setValueString(String valueString){ mValueString = valueString;}

    public int getID() { return mId; }
    public void setID(int id){ mId = id;}

    public long getKey() { return mKey; }
    public void setKey(long key){ mKey = key;}

    public boolean isEnabled() { return mIsEnabled != 0; }
    public void setEnabled(boolean extendable){ mIsEnabled = (byte)(extendable?1:0);}

    public UIValueBucket()
    {

    }
    public static <E extends Enum<E>> ArrayList<UIValueBucket> fromEnum(Class<E> type)
    {
        final ArrayList<UIValueBucket> enumList = new ArrayList<UIValueBucket>();
        E[] enums = type.getEnumConstants();
        EnumSet<E> enumSet = EnumSet.noneOf(type);
        for (E e: enums) {
            UIValueBucket v = new UIValueBucket();
            v.setID(e.ordinal());
            v.setTextString(e.toString());
            enumList.add( v );
        }
        return enumList;
    }
}

