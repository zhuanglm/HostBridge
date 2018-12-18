package com.epocal.common.epocobjects;

/**
 * Created by bmate on 6/5/2017.
 * Summary:
 * Supports cloning, which creates a new instance of a class with the same value
 * as an existing instance.
 */

public interface ICloneable {
    // Summary:
    //     Creates a new object that is a copy of the current instance.
    //
    // Returns:
    //     A new object that is a copy of this instance.
    Object Clone();
}
