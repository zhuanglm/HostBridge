package com.epocal.epoctestprocedure.fragments.dataentry;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class IMMResult extends ResultReceiver {
    public interface SoftKeyboardResultListener {
        void onSoftKeyboardResult(int result);
    }

    private SoftKeyboardResultListener mListener;
    public void setSoftKeyboardListener(SoftKeyboardResultListener listener) {
        mListener = listener;
    }

    public IMMResult() {
        super(null);
    }

    @Override
    public void onReceiveResult(int r, Bundle data) {
        if (mListener != null) {
            mListener.onSoftKeyboardResult(r)                                                 ;
        }
    }
}
