package com.epocal.common.types;

/**
 * Created by bmate on 3/23/2017.
 */

public enum AccountStatus{
    Unknown(0),
    Enabled(1) ,
    Disabled(2),
    Invalid (3),
    Expired(4) ,
    LockedOut(5);

    public final Integer value;
    AccountStatus(Integer value)
    {
        this.value = value;
    }
}
