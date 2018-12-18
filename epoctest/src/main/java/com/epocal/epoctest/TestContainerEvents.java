package com.epocal.epoctest;

import com.epocal.epoctest.uidriver.TestUIDriver;

public class TestContainerEvents {
    public enum ChangeType {
        ADD, REMOVE, UPDATE
    }

    private ChangeType mChangeType;
    private TestUIDriver mTestDriver;

    public TestContainerEvents(ChangeType type, TestUIDriver driver)
    {
        this.mChangeType = type;
        this.mTestDriver = driver;
    }

    public ChangeType getChangeType() {
        return mChangeType;
    }

    public TestUIDriver getTestDriver() {
        return mTestDriver;
    }
}
