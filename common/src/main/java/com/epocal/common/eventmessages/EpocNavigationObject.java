package com.epocal.common.eventmessages;

import android.content.Context;

import com.epocal.common.types.UIScreens;

/**
 * Created by bmate on 3/28/2017.
 */

public class EpocNavigationObject {
    private Context context;
    private UIScreens targetscreen;
    private ExtraInfo mExtraInfo1;
    private ExtraInfo mExtraInfo2;
    private boolean mFinishContext;

    public ExtraInfo getExtraInfo1() {
        return mExtraInfo1;
    }

    public void setExtraInfo1(ExtraInfo extraInfo1) {
        mExtraInfo1 = extraInfo1;
    }

    public ExtraInfo getExtraInfo2() {
        return mExtraInfo2;
    }

    public void setExtraInfo2(ExtraInfo extraInfo2) {
        mExtraInfo2 = extraInfo2;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public UIScreens getTargetscreen() {
        return targetscreen;
    }

    public void setTargetscreen(UIScreens targetscreen) {
        this.targetscreen = targetscreen;
    }

    public boolean getFinishContext() {
        return mFinishContext;
    }

    public void setFinishContext(boolean finish) {
        this.mFinishContext = finish;
    }
}
