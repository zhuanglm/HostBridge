package com.epocal.datamanager;

import com.epocal.common.epocobjects.WorkflowField;
import com.epocal.common.realmentities.CustomTestInputField;
import com.epocal.common.realmentities.TestAttributeValues;
import com.epocal.common.realmentities.TestInputField;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.WorkFlow;
import com.epocal.common.realmentities.WorkflowItem;
import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldOptionType;
import com.epocal.common.types.EpocTestFieldType;
//import com.epocal.common.types.HemodilutionPolicy;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.AllensTest;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.DeliverySystem;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.am.RespiratoryMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashSet;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by bmate on 7/21/2017.
 */
/*
* returns an unmanaged workflow object
 */
public class WorkflowRepository {
    /*
        combines all custom and factory input fields into a string, preserved in each testrecord
        mandatoryItem_m,...mandatoryItem_n ; complianceItem_m,...complianceItem_n ; optionalItem_m,...optionalItem_n ; customItem_m,...customItem_n
     */

    public String getItemizedActiveWorkflow(){
        String retval = null;
        StringBuilder sbMandatory = new StringBuilder();
        StringBuilder sbCompliance = new StringBuilder();
        StringBuilder sbOptional = new StringBuilder();
        StringBuilder sbCustom = new StringBuilder();
        StringBuilder sbQA = new StringBuilder();
        for (WorkflowItem wfi:getActiveWorkflow().getWorkflowItems()) {
            switch (wfi.getFieldGroupType()){


                case MANDATORY:
                    for (TestInputField tif:wfi.getFieldList()) {
                        if (sbMandatory.length()==0){
                        sbMandatory.append(tif.getFieldType().value.toString());

                        } else {
                        sbMandatory.append("," + tif.getFieldType().value.toString());
                        }
                    }
                    for (CustomTestInputField ctif:wfi.getCustomFieldList()) {
                        if (sbMandatory.length()==0){
                        sbMandatory.append(ctif.getName());

                        } else {
                        sbMandatory.append("," + ctif.getName());
                        }
                    }
                    break;
                case COMPLIANCE:
                    for (TestInputField tif:wfi.getFieldList()) {
                        if (sbCompliance.length()==0){
                            sbCompliance.append(tif.getFieldType().value.toString());

                        } else {
                            sbCompliance.append("," + tif.getFieldType().value.toString());
                        }
                    }
                    for (CustomTestInputField ctif:wfi.getCustomFieldList()) {
                        if (sbCompliance.length()==0){
                            sbCompliance.append(ctif.getName());

                        } else {
                            sbCompliance.append("," + ctif.getName());
                        }
                    }
                    break;
                case PATIENTINFORMATION_OPTIONAL:
                case SAMPLEINFORMATION_OPTIONAL:
                case RESPIRATORY_OPTIONAL:
                case ADDITIONALDOCUMENTATION_OPTIONAL:
                case TESTSELECTION_OPTIONAL:
                case DOCUMENTRESULTS_OPTIONAL:
                case QA_OPTIONAL:
                    for (TestInputField tif:wfi.getFieldList()) {
                        if (sbOptional.length()==0){
                            sbOptional.append(tif.getFieldType().value.toString());

                        } else {
                            sbOptional.append("," + tif.getFieldType().value.toString());
                        }
                    }
                    for (CustomTestInputField ctif:wfi.getCustomFieldList()) {
                        if (sbOptional.length()==0){
                            sbOptional.append(ctif.getName());

                        } else {
                            sbOptional.append("," + ctif.getName());
                        }
                    }
                    break;
                case CUSTOMTESTVARIABLES_OPTIONAL:

                    for (CustomTestInputField ctif:wfi.getCustomFieldList()) {
                        if (sbCustom.length()==0){
                            sbCustom.append(ctif.getName());

                        } else {
                            sbCustom.append("," + ctif.getName());
                        }
                    }
                    break;

                default:
                    continue;
            }

        }
        retval = sbMandatory.toString() + ";"
                + sbCompliance.toString() + ";"
                + sbOptional.toString() + ";"
                + sbCustom.toString();

        return retval;
    }
    public String[] getAllWorkflowNames() {

        String [] retval = null;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<WorkFlow> allWF = realm.where(WorkFlow.class).not().in("mName", new String[] {"QA Workflow"}).findAll();
        if (allWF.size()>0){
            int i =0;
            //          retval = allWF.toArray(new String[allWF.size()]);
            retval = new String[allWF.size()];
            for (WorkFlow wf:allWF ) {
                retval[i] = wf.getName();
                i++;
            }
        }
        realm.close();
        return retval;

    }

    /**
     * Build a WorkFlow object from the string representation of workflow built by
     * above method getItemizedActiveWorkflow().
     *
     *
     * @param itemizedWorkflowString -- comma and semi-colon separated string to represent
     *                               the list of workitems in workflow
     *                               Format consists 4 sections separated by semi-colon.
     *
     * "mandatory fields; compliance fields; optional fields; custom fields"
     *
     * (e.g)
     * "1,6;Nurse_Pager;3,30,31,2,5,4,8,9,12,10,11,13,14,15,16,17,18,19,20,21,29,32,33,34,35,7,28,25,26,27,22,23,24,36,37,38,39,40,41;CustomField1"
     *
     * @return workflow object
     */
    public WorkFlow buildWorkFlowFromItemizedString(String itemizedWorkflowString){
        String GROUP_DELIMITER = ";";

        String[] workFlowItemsAsString = itemizedWorkflowString.split(GROUP_DELIMITER);

        WorkFlow wf = new WorkFlow();
        RealmList<WorkflowItem> workItemList = new RealmList<WorkflowItem>();

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // create mandatory group
        WorkflowItem mandatoryGroup = new WorkflowItem();
        mandatoryGroup.setFieldGroupType(EpocTestFieldGroupType.MANDATORY);
        mandatoryGroup.setDisplayOrder(1);
        parseFieldListItemsStringToFieldList(workFlowItemsAsString[0], mandatoryGroup);
        workItemList.add(mandatoryGroup);

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // create compliance group
        if (workFlowItemsAsString.length >= 2) {
            WorkflowItem complianceGroup = new WorkflowItem();
            complianceGroup.setFieldGroupType(EpocTestFieldGroupType.COMPLIANCE);
            complianceGroup.setDisplayOrder(2);
            parseFieldListItemsStringToFieldList(workFlowItemsAsString[1], complianceGroup);
            workItemList.add(complianceGroup);
        }

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // create optional group
        if (workFlowItemsAsString.length >= 3) {
            WorkflowItem optionalGroup = new WorkflowItem();
            optionalGroup.setFieldGroupType(EpocTestFieldGroupType.PATIENTINFORMATION_OPTIONAL);
            optionalGroup.setDisplayOrder(3);
            parseFieldListItemsStringToFieldList(workFlowItemsAsString[2], optionalGroup);
            workItemList.add(optionalGroup);
        }

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // create custom group
        if (workFlowItemsAsString.length >= 4) {
            WorkflowItem customGroup = new WorkflowItem();
            customGroup.setFieldGroupType(EpocTestFieldGroupType.CUSTOMTESTVARIABLES_OPTIONAL);
            customGroup.setDisplayOrder(4);
            parseFieldListItemsStringToFieldList(workFlowItemsAsString[3], customGroup);
            workItemList.add(customGroup);
        }

        wf.setWorkflowItems(workItemList);

        return wf;
    }

    /**
     * Parse workFlowFieldsString and build RealmList<TestInputField> and RealmList<CustomTestinputField>
     * @param workFlowFieldsString
     * @param wfItem
     */
    private void parseFieldListItemsStringToFieldList(String workFlowFieldsString, WorkflowItem wfItem) {
        String FIELD_DELIMITER = ",";

        String[] fieldListItemsAsString = workFlowFieldsString.split(FIELD_DELIMITER);
        RealmList<TestInputField> newTextFieldListItems = new RealmList<>();
        RealmList<CustomTestInputField> newCustomFieldListItems = new RealmList<>();
        for (String item : fieldListItemsAsString) {
            try {
                // Assume the item represents field type id (int).
                int typeId = Integer.parseInt(item);
                TestInputField newTextField = new TestInputField();
                newTextField.setFieldType(EpocTestFieldType.fromInt(typeId));
                newTextFieldListItems.add(newTextField);
            } catch (NumberFormatException formatEx) {
                // If not a number. Try string as a custom name
                CustomTestInputField newCustomField = new CustomTestInputField();
                newCustomField.setName(item);
                newCustomFieldListItems.add(newCustomField);
            }
        }
        wfItem.setFieldList(newTextFieldListItems);
        wfItem.setCustomFieldList(newCustomFieldListItems);
    }

    public void setActiveWorkflow(String newActiveWorkflowName) {
        String activeName = getActiveWorkflowName();
        if (activeName.equals(newActiveWorkflowName)) return;
        Realm realm = Realm.getDefaultInstance();
        final WorkFlow active = realm.where(WorkFlow.class).equalTo("mIsActive",true).findFirst();
        final WorkFlow candidate = realm.where(WorkFlow.class).equalTo("mName", newActiveWorkflowName).findFirst();
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                active.setActive(false);
                candidate.setActive(true);
            }
        });
        realm.close();

    }
    public WorkFlow getWorkflow (String workflowName){
        WorkFlow retval = null;
        Realm realm = Realm.getDefaultInstance();
        WorkFlow managed = realm.where(WorkFlow.class).equalTo("mName", workflowName).findFirst();
        if (managed!= null){
            managed.getWorkflowItems().sort("mDisplayOrder", Sort.ASCENDING);
        retval = realm.copyFromRealm(managed);
        }
        realm.close();
        return retval;

    }

    public WorkFlow getActiveWorkflow (){
        WorkFlow retval = null;
        Realm realm = Realm.getDefaultInstance();
        WorkFlow managed = realm.where(WorkFlow.class).equalTo("mIsActive",true).findFirst();
        if (managed!= null){
            retval = realm.copyFromRealm(managed);
        }
        realm.close();


        // finaly, order workflowitems by displayorder, ascending

        Collections.sort(retval.getWorkflowItems(), new Comparator<WorkflowItem>() {
            @Override
            public int compare(WorkflowItem o1, WorkflowItem o2) {
                return o1.getDisplayOrder()-o2.getDisplayOrder();
            }
        });

        return retval;

    }

    public String getActiveWorkflowName(){
        String retval = "";
        Realm realm = Realm.getDefaultInstance();
        WorkFlow managed = realm.where(WorkFlow.class).equalTo("mIsActive",true).findFirst();
        if (managed!= null){
            retval = managed.getName();
        }

        realm.close();
        return retval;
    }

    private void createQAWorkflow(Realm realm) {
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                WorkFlow wf = realm.createObject(WorkFlow.class);
                TestAttributeValues tav4QAWorkflow = realm.createObject(TestAttributeValues.class);
                wf.setName("QA Workflow");
                wf.setActive(false);
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                // create mandatory group
                WorkflowItem mandatoryGroup = realm.createObject(WorkflowItem.class);
                mandatoryGroup.setFieldGroupType(EpocTestFieldGroupType.MANDATORY);
                mandatoryGroup.setDisplayOrder(1);

                // create mandatory workflowItem
                TestInputField lotNumber = createTestInputField(realm,EpocTestFieldType.LOTNUMBER,1);
                mandatoryGroup.getFieldList().add(lotNumber);
                wf.getWorkflowItems().add(mandatoryGroup);

                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                // create test type group
                WorkflowItem testTypeGroup = realm.createObject(WorkflowItem.class);
                testTypeGroup.setFieldGroupType(EpocTestFieldGroupType.QA_OPTIONAL);
                testTypeGroup.setDisplayOrder(2);
                TestInputField qaTestType = createTestInputField(realm,EpocTestFieldType.TESTTYPE,1);
                testTypeGroup.getFieldList().add(qaTestType);
                wf.getWorkflowItems().add(testTypeGroup);

                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                // create test type group
                WorkflowItem commentsGroup = realm.createObject(WorkflowItem.class);
                commentsGroup.setFieldGroupType(EpocTestFieldGroupType.QA_COMMENTS);
                commentsGroup.setDisplayOrder(3);
                TestInputField comments = createTestInputField(realm,EpocTestFieldType.COMMENTS,1);
                commentsGroup.getFieldList().add(comments);
                wf.getWorkflowItems().add(commentsGroup);

                // initialize testrecord attributes
                tav4QAWorkflow.setLotNumber("");
                tav4QAWorkflow.setFluidType("Default");
                tav4QAWorkflow.setQATestType(TestType.QualityControl);

                // add attribute values to workflow
                wf.setTestAttributeValues(tav4QAWorkflow);
            }
        });
    }

    private TestInputField createTestInputField (Realm realm, EpocTestFieldType fieldType, int displayOrder){
        TestInputField retval = realm.createObject(TestInputField.class);
        retval.setDisplayOrder(displayOrder);
        retval.setFieldType(fieldType);
        return retval;
    }
    private CustomTestInputField createCustomTestInputField (Realm realm,String name,EpocTestFieldGroupType epocTestFieldGroupType, EpocTestFieldOptionType epocTestFieldOptionType, int displayOrder, boolean editable){
        CustomTestInputField retval = realm.createObject(CustomTestInputField.class);
        retval.setName(name);
        retval.setDisplayOrder(displayOrder);
        retval.setEditable(editable);
        retval.setEpocTestFieldOptionType(epocTestFieldOptionType);
        retval.setFieldGroupType(epocTestFieldGroupType);
        return retval;
    }

    // this is specific to WorkFlow named Simple
    private void createSimpleWorkflow(Realm realm){

        final ArrayList<EpocTestFieldGroupType> simpleWorkflowGroups = new ArrayList<>();
        simpleWorkflowGroups.add(EpocTestFieldGroupType.MANDATORY);
        simpleWorkflowGroups.add(EpocTestFieldGroupType.ADDITIONALDOCUMENTATION_OPTIONAL);
        simpleWorkflowGroups.add(EpocTestFieldGroupType.DOCUMENTRESULTS_OPTIONAL);


        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                WorkFlow wf = realm.createObject(WorkFlow.class);
                TestAttributeValues tav4Simple = realm.createObject(TestAttributeValues.class);
                wf.setName("Simple workflow");
                wf.setActive(false);

                // add groups and fields
                for (int i = 0; i < simpleWorkflowGroups.size(); i++ ) {
                    EpocTestFieldGroupType groupType = simpleWorkflowGroups.get(i);
                    wf.getWorkflowItems().add(createSimpleWorkflowitem(realm,groupType,i+1,tav4Simple));
                };
                wf.setTestAttributeValues(tav4Simple);

            }
        });

    }
    // specific to Simple Workflow
    private WorkflowItem createSimpleWorkflowitem(Realm realm, EpocTestFieldGroupType groupType, int displayOrder, TestAttributeValues preset){

        WorkflowItem wfi = realm.createObject(WorkflowItem.class);
        wfi.setFieldGroupType(groupType);
        wfi.setDisplayOrder(displayOrder);
        switch(groupType){
            case MANDATORY:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTID,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.HEMODILUTION,2));

                preset.setHemodilution(null);
                break;

            case ADDITIONALDOCUMENTATION_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.COMMENTS,1));

                break;

            case DOCUMENTRESULTS_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYACTION,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYNAME,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYDATE,3));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYTIME,4));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.READBACK,5));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.REJECTTEST,6));

                break;

        }

        return wfi;
    }

    // this is specific to WorkFlow named Default
    private void createDefaultWorkflow(Realm realm){

        final ArrayList<EpocTestFieldGroupType> defaultWorkflowGroups = new ArrayList<>();
        defaultWorkflowGroups.add(EpocTestFieldGroupType.MANDATORY);
        defaultWorkflowGroups.add(EpocTestFieldGroupType.TESTSELECTION_OPTIONAL);
        defaultWorkflowGroups.add(EpocTestFieldGroupType.PATIENTINFORMATION_OPTIONAL);
        defaultWorkflowGroups.add(EpocTestFieldGroupType.SAMPLEINFORMATION_OPTIONAL);
        defaultWorkflowGroups.add(EpocTestFieldGroupType.RESPIRATORY_OPTIONAL);
        defaultWorkflowGroups.add(EpocTestFieldGroupType.ADDITIONALDOCUMENTATION_OPTIONAL);
        defaultWorkflowGroups.add(EpocTestFieldGroupType.DOCUMENTRESULTS_OPTIONAL);


        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                WorkFlow wf = realm.createObject(WorkFlow.class);
                TestAttributeValues tav4Default = realm.createObject(TestAttributeValues.class);
                wf.setName("Default workflow");
                wf.setActive(true);

                // add groups and fields
                for (int i = 0; i < defaultWorkflowGroups.size(); i++ ) {
                    EpocTestFieldGroupType groupType = defaultWorkflowGroups.get(i);
                    wf.getWorkflowItems().add(createDefaultWorkflowitem(realm,groupType,i+1,tav4Default));
                };
                wf.setTestAttributeValues(tav4Default);

            }
        });

    }
    // specific to Default Workflow
    private WorkflowItem createDefaultWorkflowitem(Realm realm, EpocTestFieldGroupType groupType, int displayOrder, TestAttributeValues preset){

        WorkflowItem wfi = realm.createObject(WorkflowItem.class);
        wfi.setFieldGroupType(groupType);
        wfi.setDisplayOrder(displayOrder);
        switch(groupType){
            case MANDATORY:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTID,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.HEMODILUTION,2));

                preset.setHemodilution(null);
                break;

            case PATIENTINFORMATION_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTID2,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTTEMPERATURE,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTGENDER,3));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTAGE,4));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTHEIGHT,5));

                break;
            case SAMPLEINFORMATION_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.SAMPLETYPE,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.DRAWSITE,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.ALLENSTEST,3));

                break;
            case RESPIRATORY_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.FIO2,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.DELIVERYSYSTEM,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.MODE,3));

                preset.setFIO2("23");
                preset.setRQ("0.88");
                break;

            case ADDITIONALDOCUMENTATION_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.COMMENTS,1));

                break;
            case TESTSELECTION_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.TESTSELECTION,1));
                break;

            case DOCUMENTRESULTS_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYACTION,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYNAME,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYDATE,3));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYTIME,4));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.READBACK,5));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.REJECTTEST,6));

                break;

        }

        return wfi;
    }

    // this is specific to WorkFlow named Extended
    private void createExtendedWorkflow(Realm realm){

        final ArrayList<EpocTestFieldGroupType> extendedWorkflowGroups = new ArrayList<>();
        extendedWorkflowGroups.add(EpocTestFieldGroupType.MANDATORY);
        extendedWorkflowGroups.add(EpocTestFieldGroupType.TESTSELECTION_OPTIONAL);
        extendedWorkflowGroups.add(EpocTestFieldGroupType.PATIENTINFORMATION_OPTIONAL);
        extendedWorkflowGroups.add(EpocTestFieldGroupType.SAMPLEINFORMATION_OPTIONAL);
        extendedWorkflowGroups.add(EpocTestFieldGroupType.RESPIRATORY_OPTIONAL);
        extendedWorkflowGroups.add(EpocTestFieldGroupType.ADDITIONALDOCUMENTATION_OPTIONAL);
        extendedWorkflowGroups.add(EpocTestFieldGroupType.DOCUMENTRESULTS_OPTIONAL);


        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                WorkFlow wf = realm.createObject(WorkFlow.class);
                TestAttributeValues tav4Extended = realm.createObject(TestAttributeValues.class);
                wf.setName("Extended workflow");
                wf.setActive(false);

                // add groups and fields
                for (int i = 0; i < extendedWorkflowGroups.size(); i++ ) {
                    EpocTestFieldGroupType groupType = extendedWorkflowGroups.get(i);
                    wf.getWorkflowItems().add(createExtendedWorkflowitem(realm,groupType,i+1,tav4Extended));
                };
                wf.setTestAttributeValues(tav4Extended);

            }
        });

    }
    // specific to Extended Workflow
    private WorkflowItem createExtendedWorkflowitem(Realm realm, EpocTestFieldGroupType groupType, int displayOrder, TestAttributeValues preset){

        WorkflowItem wfi = realm.createObject(WorkflowItem.class);
        wfi.setFieldGroupType(groupType);
        wfi.setDisplayOrder(displayOrder);
        switch(groupType){
            case MANDATORY:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTID,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.HEMODILUTION,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.SAMPLETYPE,3));

//                preset.setPatientIdValue("ExtendedPatient");
                preset.setHemodilution(null);

                break;
            case COMPLIANCE:
                wfi.getCustomFieldList().add(createCustomTestInputField(realm,"Nurse_Pager",EpocTestFieldGroupType.COMPLIANCE,EpocTestFieldOptionType.REQUIRED,1,true));
                break;
            case PATIENTINFORMATION_OPTIONAL:

                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTID2,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTGENDER,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTAGE,3));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTTEMPERATURE,4));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTHEIGHT,5));

//                preset.setPatientId2Value("ExtendedID2");
//                preset.setPatientTemperature(37.8);
//                preset.setPatientGender(Gender.Female);
//                preset.setPatientAge(5);

                break;
            case SAMPLEINFORMATION_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.SAMPLETYPE,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.DRAWSITE,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.ALLENSTEST,3));

//                preset.setSampleType(BloodSampleType.CordArterial);
//                preset.setDrawSites(DrawSites.ArtLine);
//                preset.setAllensTest(AllensTest.Positive);

                break;
            case RESPIRATORY_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.FIO2,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.DELIVERYSYSTEM,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.MODE,3));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.VT,4));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.RR,5));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.TR,6));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PEEP,7));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PS,8));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.IT,9));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.ET,10));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PIP,11));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.MAP,12));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.FLOW,13));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.RQ,14));

                preset.setFIO2("23");
//                preset.setDeliverySystem(DeliverySystem.NeoVent);
//                preset.setRespiratoryMode(RespiratoryMode.PRVC);
//                preset.setVT("extVT");
//                preset.setRR("extRR");
//                preset.setTR("extTR");
//                preset.setPEEP("extPEEP");
//                preset.setPS("extPS");
//                preset.setIT("extIT");
//                preset.setET("extET");
//                preset.setPIP("extPIP");
//                preset.setMAP("extMAP");
//                preset.setFlow("extFlow");
//                preset.setHertz("extHertz");
//                preset.setDeltaP("extDeltaP");
//                preset.setNOPPM("extNOPPM");
                preset.setRQ("0.88");
                break;

            case ADDITIONALDOCUMENTATION_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.COMMENTS,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.COLLECTEDBY,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.COLLECTIONDATE,3));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.COLLECTIONTIME,4));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.ORDERINGPHYSICIAN,5));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.ORDERDATE,6));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.ORDERTIME,7));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.PATIENTLOCATION,8));
//
//                preset.setComments("extComm");
//                preset.setPatientLocation("extPatLoc");
//                preset.setCollectedBy("extColBy");
//                preset.setCollectionDate(DateUtil.CreateDate("2017-12-12 00:00:00"));
//                preset.setCollectionTime("5 AM");
//                preset.setOrderingPhysician("extOP");
//                preset.setOrderDate(DateUtil.CreateDate("2017-12-12 00:00:00"));
//                preset.setOrderTime("4 AM");

                break;
            case TESTSELECTION_OPTIONAL:
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.TESTSELECTION,1));
                break;
            case CUSTOMTESTVARIABLES_OPTIONAL:
                wfi.getCustomFieldList().add(createCustomTestInputField(realm,"CustomField1",EpocTestFieldGroupType.CUSTOMTESTVARIABLES_OPTIONAL,EpocTestFieldOptionType.OPTIONAL,1,true));
                break;
            case DOCUMENTRESULTS_OPTIONAL:


                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYACTION,1));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYNAME,2));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYDATE,3));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.NOTIFYTIME,4));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.READBACK,5));
                wfi.getFieldList().add(createTestInputField(realm,EpocTestFieldType.REJECTTEST,6));

                break;

        }

        return wfi;
    }

// todo: remove from release
    public void createTestWorkflows(Realm realm) {

        createSimpleWorkflow(realm);
        createDefaultWorkflow(realm);
        createExtendedWorkflow(realm);
        createQAWorkflow(realm);

    }

    public void setWorkflowValuesIntoTestRecord(TestRecord testRecord) {
        TestAttributeValues tav = getActiveWorkflow().getTestAttributeValues();
        if (tav!=null){
            for (EpocTestFieldType etft:tav.getPresetAttributes()) {
                switch (etft){
                    case CUSTOM:
                    case UNKNOWN:
                    case TESTSELECTION:
                        break;
                    case PATIENTID:
                        testRecord.setSubjectId(tav.getPatientIdValue());
                        break;
                    case PATIENTID2:
                        testRecord.setId2(tav.getPatientId2Value());
                        break;
                    case SAMPLETYPE:
                        testRecord.getTestDetail().setSampleType(tav.getSampleType());
                        break;
                    case PATIENTTEMPERATURE:
                        testRecord.getTestDetail().setPatientTemperature(tav.getPatientTemperature());
                        break;
                    case HEMODILUTION:
                        testRecord.getTestDetail().setHemodilutionApplied(tav.getHemodilution());
                        break;
                    case COMMENTS:
                        testRecord.getTestDetail().setComment(tav.getComments());
                        break;
                    case DRAWSITE:
                        testRecord.getRespiratoryDetail().setDrawSite(tav.getDrawSites());
                        break;
                    case ALLENSTEST:
                        testRecord.getRespiratoryDetail().setRespAllensType(tav.getAllensTest());
                        break;
                    case DELIVERYSYSTEM:
                        testRecord.getRespiratoryDetail().setDeliverySystem(tav.getDeliverySystem());
                        break;
                    case MODE:
                        testRecord.getRespiratoryDetail().setRespiratoryMode(tav.getRespiratoryMode());
                        break;
                    case FIO2:
                        testRecord.getRespiratoryDetail().setFiO2(tav.getFIO2());
                        break;
                    case VT:
                        testRecord.getRespiratoryDetail().setTidalVolume(tav.getVT());
                        break;
                    case RR:
                        testRecord.getRespiratoryDetail().setRespiratoryRate(tav.getRR());
                        break;
                    case TR:
                        testRecord.getRespiratoryDetail().setTotalRespiratoryRate(tav.getTR());
                        break;
                    case PEEP:
                        testRecord.getRespiratoryDetail().setPeep(tav.getPEEP());
                        break;
                    case PS:
                        testRecord.getRespiratoryDetail().setPs(tav.getPS());
                        break;
                    case IT:
                        testRecord.getRespiratoryDetail().setInspiratoryTime(tav.getIT());
                        break;
                    case ET:
                        testRecord.getRespiratoryDetail().setExpiratoryTime(tav.getET());
                        break;
                    case PIP:
                        testRecord.getRespiratoryDetail().setPeakInspiratoryPressure(tav.getPIP());
                        break;
                    case MAP:
                        testRecord.getRespiratoryDetail().setMeanAirWayPressure(tav.getMAP());
                        break;
                    case ORDERINGPHYSICIAN:
                        testRecord.getTestDetail().setOrderingPhysician(tav.getOrderingPhysician());
                        break;
                    case ORDERDATE:
                        testRecord.getTestDetail().setOrderDate(tav.getOrderDate());
                        break;
                    case ORDERTIME:
                        testRecord.getTestDetail().setOrderTime(tav.getOrderTime());
                        break;
                    case COLLECTEDBY:
                        testRecord.getTestDetail().setCollectedBy(tav.getCollectedBy());
                        break;
                    case COLLECTIONDATE:
                        testRecord.getTestDetail().setCollectionDate(tav.getCollectionDate());
                        break;
                    case COLLECTIONTIME:
                        testRecord.getTestDetail().setCollectionTime(tav.getCollectionTime());
                        break;
                    case PATIENTLOCATION:
                        testRecord.getTestDetail().setPatientLocation(tav.getPatientLocation());
                        break;
                    case FLOW:
                        testRecord.getRespiratoryDetail().setFlow(tav.getFlow());
                        break;
                    case PATIENTGENDER:
                        testRecord.setGender(tav.getPatientGender());
                        break;
                    case PATIENTAGE:
                        testRecord.setPatientAge(tav.getPatientAge());
                        break;
                    case HERTZ:
                        testRecord.getRespiratoryDetail().setHertz(tav.getHertz());
                        break;
                    case DELTAP:
                        testRecord.getRespiratoryDetail().setDeltaP(tav.getDeltaP());
                        break;
                    case NOPPM:
                        testRecord.getRespiratoryDetail().setNoPPM(tav.getNOPPM());
                        break;
                    case RQ:
                        testRecord.getRespiratoryDetail().setRq(tav.getRQ());
                        break;
                    case LOTNUMBER:
                        testRecord.getCardLot().setLotNumber(tav.getLotNumber());
                        break;
                    case FLUIDTYPE:
                        testRecord.getTestDetail().getQaSampleInfo().setName("Default");
                    case TESTTYPE:
                        testRecord.setType(tav.getTestType());
                        break;
                }

            }
        }
    }
// Patient test workflows are extracted from database
    public ArrayList<WorkflowField> getActiveWorkflowFields() {
        ArrayList<WorkflowField> retval = new ArrayList<>();
        for (WorkflowItem wfi:getActiveWorkflow().getWorkflowItems()) {
            for (TestInputField tif : wfi.getFieldList()) {
                WorkflowField wff = new WorkflowField();
                wff.setEpocTestFieldGroupType(wfi.getFieldGroupType());
                wff.setEpocTestFieldType(tif.getFieldType());
                retval.add(wff);
            }
        }

        return retval;
    }

    // TODO: QA workflow is in-memory, factory hard-coded below
    public WorkFlow getQAWorkflow()
    {
        // todo change this: create here the QA workflow
        return getWorkflow("QA Workflow");
    }

    public ArrayList<WorkflowField> getWorkflowFields(String workflowName) {

        ArrayList<WorkflowField> retval = new ArrayList<>();

        for (WorkflowItem wfi:getWorkflow(workflowName).getWorkflowItems()) {
            for (TestInputField tif : wfi.getFieldList()) {
                WorkflowField wff = new WorkflowField();
                wff.setEpocTestFieldGroupType(wfi.getFieldGroupType());
                wff.setEpocTestFieldType(tif.getFieldType());
                retval.add(wff);
            }
        }

        return retval;

    }
}
