package com.epocal.epoctestprocedure.stepper;

import android.content.Context;

import com.epocal.common.realmentities.CustomTestInputField;
import com.epocal.common.realmentities.TestInputField;
import com.epocal.common.realmentities.WorkflowItem;
import com.epocal.common.types.EpocTestFieldGroupType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Builds a map of stepper items from WorkFlow items.
 *
 * Relieves the VerticalStepper UI of the burden of determining how to
 * transforming Workflow items into UI elements.
 */
public class StepperItemBuilder {
    private Map<EpocTestFieldGroupType, List<StepperItem>> groups = new LinkedHashMap<>();

    public Map<EpocTestFieldGroupType, List<StepperItem>> getGroups() {
        return groups;
    }

    public void populateFromWorkflowItem(Context context, WorkflowItem workflowItem) {
        EpocTestFieldGroupType groupType = workflowItem.getFieldGroupType();

        groups.put(groupType, new ArrayList<StepperItem>());
        for (TestInputField testField : workflowItem.getFieldList()) {
            StepperItem vso = new StepperItem(context, testField);
            groups.get(groupType).add(vso);
        }
        for (CustomTestInputField customTestField : workflowItem.getCustomFieldList()) {
            StepperItem vso = new StepperItem(context, customTestField);
            groups.get(groupType).add(vso);
        }
    }
}
