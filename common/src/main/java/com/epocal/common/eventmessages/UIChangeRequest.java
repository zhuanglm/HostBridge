package com.epocal.common.eventmessages;

import com.epocal.common.types.UIChangeRequestReason;

/**
 * Created by bmate on 18/12/2017.
 */

public class UIChangeRequest {
    UIChangeRequestReason mReason = UIChangeRequestReason.Unknown;

    public UIChangeRequestReason getReason() {
        return mReason;
    }

    public void setReason(UIChangeRequestReason reason) {
        mReason = reason;
    }
    public UIChangeRequest(UIChangeRequestReason reason){
        mReason = reason;
    }


}
