package com.epocal.common.realmentities;

//import com.epocal.common.types.HemodilutionPolicy;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by bmate on 7/21/2017.
 * Holds information of groups (workflowitems) of test input fields and custom test variables
 * Holds information of initial test attribute values
 */

public class WorkFlow extends RealmObject{
    private String mName;
    private RealmList<WorkflowItem> mWorkflowItems;
    private boolean mIsActive;
    private TestAttributeValues mTestAttributeValues;

    public TestAttributeValues getTestAttributeValues() {
        return mTestAttributeValues;
    }

    public void setTestAttributeValues(TestAttributeValues testAttributeValues) {
        mTestAttributeValues = testAttributeValues;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public RealmList<WorkflowItem> getWorkflowItems() {
        return mWorkflowItems;
    }

    public void setWorkflowItems(RealmList<WorkflowItem> workflowItems) {
        mWorkflowItems = workflowItems;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
    }
}
