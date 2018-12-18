package com.epocal.common.eventmessages;

import com.epocal.common.epocobjects.EpocMessageBoxButtons;
import com.epocal.common.epocobjects.EpocUIMessageType;

import io.reactivex.functions.Action;

/**
 * Created by bmate on 8/14/2017.
 */

public class EpocMessageBoxInfo {
    private EpocMessageBoxButtons mEpocMessageBoxButtons;
    private Action mPositiveAction;
    private Action mMiddleAction;
    private Action mNegativeAction;
    private EpocUIMessageType mEpocUIMessageType;
    private int mMessageStringResId;
    private int mPositiveTextResId;
    private int mNegativeTextResId;
    private boolean mToRemove;

    public boolean isToRemove() {
        return mToRemove;
    }

    public void setToRemove(boolean mToRemove) {
        this.mToRemove = mToRemove;
    }

    public EpocMessageBoxButtons getEpocMessageBoxButtons() {
        return mEpocMessageBoxButtons;
    }

    public void setEpocMessageBoxButtons(EpocMessageBoxButtons epocMessageBoxButtons) {
        mEpocMessageBoxButtons = epocMessageBoxButtons;
    }

    public Action getMiddleAction() {
        return mMiddleAction;
    }

    public void setMiddleAction(Action middleAction) {
        mMiddleAction = middleAction;
    }

    public Action getPositiveAction() {
        return mPositiveAction;
    }

    public void setPositiveAction(Action positiveAction) {
        mPositiveAction = positiveAction;
    }

    public Action getNegativeAction() {
        return mNegativeAction;
    }

    public void setNegativeAction(Action negativeAction) {
        mNegativeAction = negativeAction;
    }

    public EpocUIMessageType getEpocUIMessageType() {
        return mEpocUIMessageType;
    }

    public void setEpocUIMessageType(EpocUIMessageType epocUIMessageType) {
        mEpocUIMessageType = epocUIMessageType;
    }

    public int getMessageStringResId() {
        return mMessageStringResId;
    }

    public void setMessageStringResId(int messageStringResId) {
        mMessageStringResId = messageStringResId;
    }

    public int getPositiveTextResId() {
        return mPositiveTextResId;
    }

    public void setPositiveTextResId(int positiveTextResId) {
        mPositiveTextResId = positiveTextResId;
    }

    public int getNegativeTextResId() {
        return mNegativeTextResId;
    }

    public void setNegativeTextResId(int negativeTextResId) {
        mNegativeTextResId = negativeTextResId;
    }
}
