package com.epocal.epoctest.uidriver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.epocal.common.androidutil.RxUtil;
import com.epocal.common.androidutil.StringResourceUtil;
import com.epocal.common.epocexceptions.EpocTestDriverException;
import com.epocal.common.epocobjects.AnalyteOption;
import com.epocal.common.epocobjects.EpocUIMessageType;
import com.epocal.common.epocobjects.UITestPanel;
import com.epocal.common.epocobjects.WorkflowField;
import com.epocal.common.eventmessages.EpocMessageBoxInfo;
import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.common.eventmessages.TestDataProcessorCallback;
import com.epocal.common.eventmessages.UIChangeRequest;
import com.epocal.common.globaldi.GloabalObject;
import com.epocal.common.realmentities.CustomTestInputField;
import com.epocal.common.realmentities.CustomTestVariable;
import com.epocal.common.realmentities.Reader;
import com.epocal.common.realmentities.TestInputField;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.TestResult;
import com.epocal.common.realmentities.WorkFlow;
import com.epocal.common.realmentities.WorkflowItem;
import com.epocal.common.types.ActionTypeUponTestFieldSaved;
import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.EpocTestPanelType;
import com.epocal.common.types.NotifyActionType;
import com.epocal.common.types.QAFluidType;
import com.epocal.common.types.TestType;
import com.epocal.common.types.UIChangeRequestReason;
import com.epocal.common.types.UIScreens;
import com.epocal.common.types.am.AllensTest;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.DeliverySystem;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.am.RespiratoryMode;
import com.epocal.common.types.am.TestMode;
import com.epocal.datamanager.HostConfigurationModel;
import com.epocal.datamanager.ReaderModel;
import com.epocal.datamanager.TestPanelModel;
import com.epocal.datamanager.TestRecordModel;
import com.epocal.datamanager.WorkflowRepository;
import com.epocal.epoctest.TestController;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestManager;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.uimessage.TestMessageContext;
import com.epocal.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.realm.RealmList;

/**
 * prepare and emit test context to UI subscriber
 * prepare and emit statusmessages to UI subscriber
 *
 * Created by bmate on 7/12/2017.
 */

public abstract class TestUIDriver<T extends TestController> implements IUITestDriver, AutoCloseable {
    private static final String TAG = TestUIDriver.class.getSimpleName();

    // no Realm objects!
    private boolean mIsCalBroken;
    private String devMessage;
    private boolean mIsActive;
    private boolean mIsVisible;
    private boolean mResetUI;
    private ReaderDevice mReaderDevice;
    private boolean isReadyToTest;
    private boolean isCardInserted;
    private boolean isAnimationRunning;
    private boolean mIsEditable;
    private boolean mCalculationDone;
    private TestMode operationMode;
    private TestMessageContext mTestMessageContext;
    private boolean testCompleted;
    private T mTestController;
    private boolean actionPostponed;
    private ArrayList<UITestPanel> mUITestPanels;
    private TestStatusType mStatusEventType;
    private TestStatusErrorType mStatusErrorType;
    private ArrayList<WorkflowField> mWorkflowFields;



   // private TestRecord mTestRecord;
    private Boolean mCriticalsHandled;
    private WorkFlow mTestFlow;
    private com.epocal.common.androidutil.RxUtil<TestUIDriver> mTestDriverEmmitter;
    private com.epocal.common.androidutil.RxUtil<EpocMessageBoxInfo> mMessageBoxRequestor;


    TestUIDriver(ReaderDevice readerDevice, T testController) {
        this.mReaderDevice = readerDevice;
        this.mTestController = testController;
        initialize();
    }

    // setters


    public void setCalBroken(boolean calBroken) {
        mIsCalBroken = calBroken;
    }

    // getters
    public String getDevMessage(){return devMessage;}
    public boolean isCalculationDone() {
        return mCalculationDone;
    }

    public TestMessageContext getTestMessageContext() {
        return mTestMessageContext;
    }

    public boolean isTestCompleted() {
        return testCompleted;
    }

    public TestStatusType getStatusEventType() {
        return mStatusEventType;
    }

    public TestStatusErrorType getStatusErrorType() {
        return mStatusErrorType;
    }

    public void setWorkflowFields(ArrayList<WorkflowField> workflowFields) {
        mWorkflowFields = workflowFields;
    }

    // testdata relay
    public void saveTestData (EnumSet<EpocTestFieldType> fieldTypes){
        ActionTypeUponTestFieldSaved actionType = actionUponSaveData(fieldTypes);
        mTestController.getTestStateDataObject().getTestDataProcessor().saveTestData();
        switch(actionType){

            case NO_ACTION:
                break;
            case RESTRICT_EDITABILITY:
                EventBus.getDefault().post(new UIChangeRequest(UIChangeRequestReason.EDITABILITY_CHANGED));
                break;
            case REQUEST_RECALCULATION:
                Recalculate();
                break;
        }

    }
    private void Recalculate(){
        if (!mCalculationDone){
            return;
        } else {

            getTestController().getTestStateDataObject().calculateResults();
            EventBus.getDefault().post(new UIChangeRequest(UIChangeRequestReason.RECALCULATION_STARTED));
        }

    }
    private ActionTypeUponTestFieldSaved actionUponSaveData(EnumSet<EpocTestFieldType> fieldTypes) {

        ActionTypeUponTestFieldSaved retval = ActionTypeUponTestFieldSaved.NO_ACTION;
        TestRecord updatedTestRecord = mTestController.getTestStateDataObject().getTestDataProcessor().getTestRecord();
        TestRecord persistedTestRecord = new TestRecordModel().getTestRecordById(updatedTestRecord.getId());
        // exit criteria : no test saved yet, or calculation was not performed yet:
        if (persistedTestRecord==null || !mCalculationDone){
            return ActionTypeUponTestFieldSaved.NO_ACTION;
        }
        for (EpocTestFieldType etft:fieldTypes ) {
            switch (etft){

                case TESTTYPE:
                    if (mCalculationDone &&(!updatedTestRecord.getType().equals(persistedTestRecord.getType()))){
                        return ActionTypeUponTestFieldSaved.REQUEST_RECALCULATION;
                    }
                    break;
                case FLUIDTYPE:
                    if (mCalculationDone &&(!updatedTestRecord.getTestDetail().getQaSampleInfo().getQaFluidType().equals(persistedTestRecord.getTestDetail().getQaSampleInfo().getQaFluidType()))){
                        return ActionTypeUponTestFieldSaved.REQUEST_RECALCULATION;
                    }
                    break;
                case SAMPLETYPE:
                    if (mCalculationDone &&(!updatedTestRecord.getTestDetail().getSampleType().equals(persistedTestRecord.getTestDetail().getSampleType()))){
                        return ActionTypeUponTestFieldSaved.REQUEST_RECALCULATION;
                    }
                    break;
                case PATIENTTEMPERATURE:
                    if (mCalculationDone && updatedTestRecord.getTestDetail().getPatientTemperature() != null) {
                        if(!updatedTestRecord.getTestDetail().getPatientTemperature().equals(persistedTestRecord.getTestDetail().getPatientTemperature())) {
                            if (AnyOfTestsEnabled(EnumSet.of(AnalyteName.pH,AnalyteName.pCO2,AnalyteName.pO2,AnalyteName.AlveolarO2,AnalyteName.ArtAlvOxDiff,AnalyteName.ArtAlvOxRatio)))
                            return ActionTypeUponTestFieldSaved.REQUEST_RECALCULATION;
                        }
                    }
                    break;
                case HEMODILUTION:
                    if (mCalculationDone && updatedTestRecord.getTestDetail().getHemodilutionApplied() != null) {
                        if (!updatedTestRecord.getTestDetail().getHemodilutionApplied().equals(persistedTestRecord.getTestDetail().getHemodilutionApplied())){
                            return ActionTypeUponTestFieldSaved.REQUEST_RECALCULATION;
                        }
                    }
                    break;
                case FIO2:
                    if (mCalculationDone &&
                            (!StringUtil.empty(updatedTestRecord.getRespiratoryDetail().getFiO2())) &&
                            (!updatedTestRecord.getRespiratoryDetail().getFiO2().equals(persistedTestRecord.getRespiratoryDetail().getFiO2())) &&
                            AnyOfTestsEnabled(EnumSet.of(AnalyteName.AlveolarO2,AnalyteName.ArtAlvOxDiff,AnalyteName.ArtAlvOxRatio))){

                            return ActionTypeUponTestFieldSaved.REQUEST_RECALCULATION;
                    }
                    break;
                case RQ:
                    if (mCalculationDone &&
                            (!StringUtil.empty(updatedTestRecord.getRespiratoryDetail().getFiO2())) &&
                            (!updatedTestRecord.getRespiratoryDetail().getRq().equals(persistedTestRecord.getRespiratoryDetail().getRq())) &&
                            AnyOfTestsEnabled(EnumSet.of(AnalyteName.AlveolarO2,AnalyteName.ArtAlvOxDiff,AnalyteName.ArtAlvOxRatio))){

                        return ActionTypeUponTestFieldSaved.REQUEST_RECALCULATION;
                    }
                    break;
                case PATIENTGENDER:
                    if (mCalculationDone &&
                            (updatedTestRecord.getPatientAge()>0) &&
                            (!updatedTestRecord.getGender().equals(persistedTestRecord.getGender())) &&
                            AnyOfTestsEnabled(EnumSet.of(AnalyteName.Creatinine))){

                        return ActionTypeUponTestFieldSaved.REQUEST_RECALCULATION;
                    }
                    break;
                case PATIENTAGE:
                    if (mCalculationDone &&
                            (updatedTestRecord.getPatientAge()>0) &&
                            (!updatedTestRecord.getGender().equals(persistedTestRecord.getGender())) &&
                            AnyOfTestsEnabled(EnumSet.of(AnalyteName.Creatinine))){

                        return ActionTypeUponTestFieldSaved.REQUEST_RECALCULATION;
                    }
                    break;
                case NOTIFYACTION:
                case NOTIFYNAME:
                case READBACK:
                case REJECTTEST:
                    mCriticalsHandled = true;
                    return ActionTypeUponTestFieldSaved.RESTRICT_EDITABILITY;
                case LOTNUMBER:
                    return ActionTypeUponTestFieldSaved.RESTRICT_EDITABILITY;
                default:
                    break;
            }
        }

        return retval;
    }

    private boolean AnyOfTestsEnabled(EnumSet<AnalyteName> tests) {
        boolean retval = false;
        ArrayList<AnalyteName> testInclusions = getRuntimeTestInclusions();
        for (AnalyteName test : testInclusions  ) {
            if (tests.contains(test)){
                retval = true;
                break;
            }
        }
        return retval;
    }

    public TestRecord getTestData() {
        return mTestController.getTestStateDataObject().getTestDataProcessor().getTestRecord();
    }
    // get / set


    public ReaderDevice getReaderDevice() {
        return mReaderDevice;
    }

    public void setReaderDevice(ReaderDevice readerDevice) {
        mReaderDevice = readerDevice;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public void setVisible(boolean visible) {
        mIsVisible = visible;
    }

    public boolean isReadyToTest() {
        return isReadyToTest;
    }

    public void setReadyToTest(boolean readyToTest) {
        isReadyToTest = readyToTest;
    }

    public boolean isCardInserted() {
        return isCardInserted;
    }

    public void setCardInserted(boolean cardInserted) {
        isCardInserted = cardInserted;
    }

    public boolean isAnimationRunning() {
        return isAnimationRunning;
    }

    public void setAnimationRunning(boolean animationRunning) {
        isAnimationRunning = animationRunning;
    }

    public TestMode getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(TestMode operationMode) {
        this.operationMode = operationMode;
    }


    public void setCalculationDone(boolean calculationDone) {
        this.mCalculationDone = calculationDone;
    }

    public void setTestMessageContext(TestMessageContext testMessageContext) {
        mTestMessageContext = testMessageContext;
    }

    public void setTestCompleted(boolean testCompleted) {
        this.testCompleted = testCompleted;
    }

    public T getTestController() {
        return mTestController;
    }

    public void setTestController(T testController) {
        mTestController = testController;
    }

    public boolean isEditable() {
        return mIsEditable;
    }

    public void setEditable(boolean editable) {
        mIsEditable = editable;
    }

    public Boolean getCriticalsHandled() {
        return mCriticalsHandled;
    }

    public void setCriticalsHandled(Boolean criticalsHandled) {
        mCriticalsHandled = criticalsHandled;
    }

    public WorkFlow getTestFlow() {
        return mTestFlow;
    }

    public void setTestFlow(WorkFlow testFlow) {
        mTestFlow = testFlow;
    }

    public RxUtil<TestUIDriver> getTestDriverEmmitter() {
        return mTestDriverEmmitter;
    }

    public void setTestDriverEmmitter(RxUtil<TestUIDriver> testDriverEmmitter) {
        mTestDriverEmmitter = testDriverEmmitter;
    }

    public RxUtil<EpocMessageBoxInfo> getMessageBoxRequestor() {
        return mMessageBoxRequestor;
    }

    public void setMessageBoxRequestor(RxUtil<EpocMessageBoxInfo> messageBoxRequestor) {
        mMessageBoxRequestor = messageBoxRequestor;
    }
    // methods

    @Override
    public void show() {
        mIsVisible = true;
        mTestDriverEmmitter.onNext(this);
    }


    @Override
    public void hide() {
        mIsVisible = false;
    }

    @Override
    public void start() {

        //wire up relay
        // subscribe to incoming event
        mTestController.getRxUtil().subscribe(new Consumer<TestEventInfo>() {
            @Override
            public void accept(TestEventInfo testEventInfo) throws Exception {
                handleTestEvent(testEventInfo);
            }
        });
        // emit uitestdriver to UI
        if (mIsVisible) {
            mTestDriverEmmitter.onNext(this);
        }
        // set testrecord hascritical to false

    }

    @Override
    public void restart() {
        // renew the testcontroller ?
        reinitializeDriver();
    }

    /*
    returns all fields of the current workflow that are not editable
     */
    public ArrayList<WorkflowField> getNonEditableFieldsMock() {
        ArrayList<WorkflowField> retval = new ArrayList<>();
        for (WorkflowField wf : mWorkflowFields) {
            if (wf.getEpocTestFieldType().equals(EpocTestFieldType.PATIENTID) ||
                    wf.getEpocTestFieldType().equals(EpocTestFieldType.PATIENTID) ||
                    wf.getEpocTestFieldType().equals(EpocTestFieldType.PATIENTAGE) ||
                    wf.getEpocTestFieldType().equals(EpocTestFieldType.PATIENTGENDER) ||
                    wf.getEpocTestFieldType().equals(EpocTestFieldType.PATIENTTEMPERATURE) ||
                    wf.getEpocTestFieldType().equals(EpocTestFieldType.LOTNUMBER) ||
                    wf.getEpocTestFieldType().equals(EpocTestFieldType.RQ)) {
                retval.add(wf);
            }
        }


        return retval;
    }

    /*
    returns all fields of the current workflow that are not editable
     */
    public ArrayList<WorkflowField> getNonEditableFields(){

        ArrayList<WorkflowField> retval = new ArrayList<>();

        // apply the rules. add the workflowfield to retval only if it is in the actual active workflow.
        //1. after calculation done, patient ID is not editable
        if (mCalculationDone ){
            for ( WorkflowField wf:mWorkflowFields ) {
                if (wf.getEpocTestFieldType().equals(EpocTestFieldType.PATIENTID) && !StringUtil.empty(getTestData().getSubjectId())){
                    retval.add(wf);
                }
                else if (wf.getEpocTestFieldType().equals(EpocTestFieldType.LOTNUMBER) && !StringUtil.empty(getTestData().getSubjectId())){
                    retval.add(wf);
                }
            }

        }
        boolean resultsDocumented = areResultsDocumented();

        //2. if results were documented, Hemodilution and Sample type are not editable - always.
        if (resultsDocumented){
            for ( WorkflowField wf:mWorkflowFields ) {
                if (wf.getEpocTestFieldType().equals(EpocTestFieldType.HEMODILUTION)
                     || wf.getEpocTestFieldType().equals(EpocTestFieldType.SAMPLETYPE)
                     || wf.getEpocTestFieldType().equals(EpocTestFieldType.TESTSELECTION)
                        ){
                    retval.add(wf);
                    break;
                }
            }
        }
        //3. if results were documented, and any of pH(T), pCO2(T), pO2(T), A(T), A-a(T), a/A(T) are in the results
        //    then patient temperature is not editable
        if (resultsDocumented && temperatureCorrectedResultsPresent()){
            for ( WorkflowField wf:mWorkflowFields ) {
                if (wf.getEpocTestFieldType().equals(EpocTestFieldType.PATIENTTEMPERATURE)){
                    retval.add(wf);
                    break;
                }
            }
        }
        //4. if results were documented, and any of A, A-a, a/A are in results, then FiO2 and RQ are not editable
        // NOTE: RQ doesn't necessarily has to be in the workflow
        if (resultsDocumented && alveolarResultsPresent()){
            for ( WorkflowField wf:mWorkflowFields ) {
                if (wf.getEpocTestFieldType().equals(EpocTestFieldType.RQ) ||
                        wf.getEpocTestFieldType().equals(EpocTestFieldType.FIO2)){
                    retval.add(wf);
                    break;
                }
            }
        }
        //5. if results were documented and eGFR or eGFR-a are in results, then Gender and Age are not editable

        if (resultsDocumented && eGFRResultsPresent()){
            for ( WorkflowField wf:mWorkflowFields ) {
                if (wf.getEpocTestFieldType().equals(EpocTestFieldType.PATIENTGENDER) ||
                        wf.getEpocTestFieldType().equals(EpocTestFieldType.PATIENTAGE)){
                    retval.add(wf);
                    break;
                }
            }
        }

        return retval;
    }

    private boolean temperatureCorrectedResultsPresent() {
        boolean retval = false;
        RealmList<TestResult> testResults = getTestData().getTestResults();
        if (testResults !=null && testResults.size()>0 ) {
            for (TestResult tr :testResults ) {
               if (tr.getAnalyteName().equals(AnalyteName.pHT) ||
                       tr.getAnalyteName().equals(AnalyteName.pCO2T) ||
                       tr.getAnalyteName().equals(AnalyteName.pO2T) ||
                       tr.getAnalyteName().equals(AnalyteName.cAlveolarO2) ||
                       tr.getAnalyteName().equals(AnalyteName.cArtAlvOxDiff) ||
                       tr.getAnalyteName().equals(AnalyteName.cArtAlvOxRatio) ){
                   retval= true;
                   break;
               }
            }
        }

        return retval;
    }
    private boolean alveolarResultsPresent() {
        boolean retval = false;
        RealmList<TestResult> testResults = getTestData().getTestResults();
        if (testResults !=null && testResults.size()>0 ) {
            for (TestResult tr :testResults ) {
                if (tr.getAnalyteName().equals(AnalyteName.AlveolarO2) ||
                        tr.getAnalyteName().equals(AnalyteName.ArtAlvOxDiff) ||
                        tr.getAnalyteName().equals(AnalyteName.ArtAlvOxRatio) ){
                    retval= true;
                    break;
                }
            }
        }

        return retval;
    }
    private boolean eGFRResultsPresent() {
        boolean retval = false;
        RealmList<TestResult> testResults = getTestData().getTestResults();
        if (testResults !=null && testResults.size()>0 ) {
            for (TestResult tr :testResults ) {
                if (tr.getAnalyteName().equals(AnalyteName.eGFR) ||
                    tr.getAnalyteName().equals(AnalyteName.eGFRa) ||
                    tr.getAnalyteName().equals(AnalyteName.eGFRj) ||
                    tr.getAnalyteName().equals(AnalyteName.GFRckd) ||
                    tr.getAnalyteName().equals(AnalyteName.GFRckda) ||
                    tr.getAnalyteName().equals(AnalyteName.GFRswz)) {
                    retval= true;
                    break;
                }
            }
        }

        return retval;
    }
    private boolean areResultsDocumented(){
        boolean retval = false;
        TestRecord trval = getTestData();
        boolean testval = getTestData().getTestDetail().getReadBack();
        if (!getTestData().getTestDetail().getNotifyActionType().equals(NotifyActionType.None) ||
            !StringUtil.empty(getTestData().getTestDetail().getNotifyName()) ||
              getTestData().getTestDetail().getReadBack() ||
                getTestData().getRejected() ){
            retval = true;
        }
        int i = 1;
        return retval;
    }
    private boolean isContainedInWorkflow(EpocTestFieldType testFieldType) {
        boolean retval = false;
        for ( WorkflowField wf:mWorkflowFields ) {
            if (wf.getEpocTestFieldType().equals(testFieldType)){
                retval=true;
                break;
            }
        }
        return retval;
    }

    @Override
    public void fullReset() throws EpocTestDriverException {
        mResetUI = false;
        isCardInserted = false;
        isReadyToTest = false;
        hide();
        close();
    }

    @Override
    public void resetUI(UIChangeRequestReason reason) {

        EventBus.getDefault().post(new UIChangeRequest(reason));
    }

    @Override
    public void terminateWithOption(final Context context) {
        if(isEditable() && operationMode.equals(TestMode.BloodTest) ){
            // check if compliance fullfilled
            final boolean complianceFullfilled = checkCompliance();
            // check if enforce criticals and there are critical results
            boolean enforceCriticals = new HostConfigurationModel().getUnmanagedHostConfiguration().isEnforceCriticalHandling();
            if (mCalculationDone) {
                if (enforceCriticals && getTestData().isHasCritical() && !mCriticalsHandled) {
                    requestEpocMessageBox(
                            StringResourceUtil.getStringIDByName("criticals_not_documented"), //"Critical results not documented. Continue?",
                            StringResourceUtil.getStringIDByName("yes"),
                            StringResourceUtil.getStringIDByName("no"),
                            EpocUIMessageType.WARNING,
                            new Action() {
                                @Override
                                public void run() throws Exception {
                                    if (!complianceFullfilled) {
                                        requestEpocMessageBox(
                                                StringResourceUtil.getStringIDByName("compliance_data_missing"), //"Some required fields not entered. Continue?"
                                                StringResourceUtil.getStringIDByName("yes"),
                                                StringResourceUtil.getStringIDByName("no"),
                                                EpocUIMessageType.WARNING,
                                                 new Action() {
                                                    @Override
                                                    public void run() throws Exception {
                                                        TestManager.getInstance().stopTestAndNavigate(mReaderDevice.getDeviceAddress(), UIScreens.HomeScreen, context);
                                                    }
                                                },
                                                null,
                                                null
                                        );

                                    } else {
                                        TestManager.getInstance().stopTestAndNavigate(mReaderDevice.getDeviceAddress(), UIScreens.HomeScreen, context);
                                    }
                                }
                            },
                            null,
                            null
                    );
                } else if (!complianceFullfilled) {
                    requestEpocMessageBox(
                            StringResourceUtil.getStringIDByName("compliance_data_missing"), //"Some required fields not entered. Continue?"
                            StringResourceUtil.getStringIDByName("yes"),
                            StringResourceUtil.getStringIDByName("no"),
                            EpocUIMessageType.WARNING,
                            new Action() {
                                @Override
                                public void run() throws Exception {
                                    TestManager.getInstance().stopTestAndNavigate(mReaderDevice.getDeviceAddress(), UIScreens.HomeScreen, context);
                                }
                            },
                            null,
                            null
                    );
                } else {
                    requestEpocMessageBox(
                            StringResourceUtil.getStringIDByName("close_test_and_exit"),
                            StringResourceUtil.getStringIDByName("close_test"),
                            StringResourceUtil.getStringIDByName("dont_close"),
                            EpocUIMessageType.WARNING,
                            new Action() {
                                @Override
                                public void run() throws Exception {
                                    TestManager.getInstance().stopTestAndNavigate(mReaderDevice.getDeviceAddress(), UIScreens.HomeScreen, context);
                                }
                            },
                            null,
                            null
                    );
                }
            } else{
                requestEpocMessageBox(
                        StringResourceUtil.getStringIDByName("cancel_test_and_exit"),
                        StringResourceUtil.getStringIDByName("cancel_test"),
                        StringResourceUtil.getStringIDByName("dont_cancel"),
                        EpocUIMessageType.WARNING,
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                TestManager.getInstance().stopTestAndNavigate(mReaderDevice.getDeviceAddress(), UIScreens.HomeScreen, context);
                            }
                        },
                        null,
                        null
                );
            }

        }else if(operationMode.equals(TestMode.QA)){
            final boolean multipleTestsRunning = TestManager.getInstance().areMultipleActiveTests();
            int messageId = mCalculationDone? StringResourceUtil.getStringIDByName("close_test_and_exit"): StringResourceUtil.getStringIDByName("cancel_test_and_exit");
            requestEpocMessageBox(
                    messageId,
                    StringResourceUtil.getStringIDByName(!mCalculationDone?"cancel_test":"close_test_and_exit"),
                    StringResourceUtil.getStringIDByName(!mCalculationDone?"dont_cancel":"dont_close"),
                    EpocUIMessageType.WARNING,
                    new Action() {
                        @Override
                        public void run() throws Exception {
                            TestManager.getInstance().stopTestAndNavigate(mReaderDevice.getDeviceAddress(), multipleTestsRunning?UIScreens.MULTITEST_SCREEN:UIScreens.HomeScreen, context);
                        }
                    },
                    null,
                    new Action() {
                        @Override
                        public void run() throws Exception {
                            //enable for multi test
                            /*
                            setVisible(false);
                            TestManager.getInstance().continueTestAndNavigate(context);
                            */
                        }
                    }
            );

        }else{
            requestEpocMessageBox(
                    StringResourceUtil.getStringIDByName("close_test_and_exit"),
                    StringResourceUtil.getStringIDByName("close_test"),
                    StringResourceUtil.getStringIDByName("dont_close"),
                    EpocUIMessageType.WARNING,
                    new Action() {
                        @Override
                        public void run() throws Exception {
                            TestManager.getInstance().stopTestAndNavigate(mReaderDevice.getDeviceAddress(), UIScreens.HomeScreen, context);
                        }
                    },
                    null,
                    null
            );
        }
    }



    private void createData() {
        // cal fluid was broken
        mTestController.getTestStateDataObject().persistTestData();
    }




    @Override
    public void showCommunicationBoard() {

    }

    @Override
    public void loadInputScreen(int testVariableUIFieldType) {

    }

    @Override
    public void loadCustomVariableScreen(String variablename) {

    }

    @Override
    // comes from Custom Test Selection
    public void setCustomTestInclusions(EnumSet<AnalyteName> analytes) {
        setSelectedPanel(EpocTestPanelType.CUSTOM);

        if (CustomTestSelectionChangesDetected(analytes)) {
            // update testdataprocessor's runtime testinclusions
            TestPanelModel tpm = new TestPanelModel();
            mTestController.getTestStateDataObject().getTestDataProcessor().setRuntimeTestSelectionInclusions(tpm.setRuntimeTestInclusions(analytes));
            Recalculate();
        }
    //    int i = 1;
    }

    private boolean CustomTestSelectionChangesDetected(EnumSet<AnalyteName> changedAnalyteSet) {
        boolean retval = true;
        // runtime set is never null
        ArrayList<AnalyteName> rtset= mTestController.getTestStateDataObject().getTestDataProcessor().getRuntimeTestSelectionInclusions();
        // exit: no action if input is null
        if (changedAnalyteSet==null) return false;
        // assess only if both sets are of same length
        if (rtset.size()== changedAnalyteSet.size()){
            boolean foundMatch = false;
            for (AnalyteName chan:changedAnalyteSet) {
                foundMatch =false;
                for (AnalyteName rtan:rtset) {
                   if(chan.equals(rtan)){
                       foundMatch = true;
                       break;
                   }
                }
                if(!foundMatch){
                    break;
                }
            }
            retval = !foundMatch;
        }

        return retval;
    }

    @Override
    // comes from Test Selection, for a custom test panel (future)
    public void setTestInclusions(String testPanelName) {
        // update testdataprocessor's runtime testinclusions
        int i = 1;
    }
    @Override
    // comes from Test Selection, for a factory test panel
    public void setTestInclusions(EpocTestPanelType panelType) {

        if (!getSelectedPanel().equals(EpocTestPanelType.UNKNOWN) && getSelectedPanel().equals(panelType)){
            return;
        }
        // update testdataprocessor's runtime testinclusions
        if (panelType.equals(EpocTestPanelType.CUSTOM) || panelType.equals(EpocTestPanelType.UNKNOWN)) return;
        setSelectedPanel(panelType);
        if (panelType.equals(EpocTestPanelType.ALL)){
            resetRuntimeTestInclusionsToAll();
        } else {
            TestPanelModel tpm = new TestPanelModel();
            mTestController.getTestStateDataObject().getTestDataProcessor().setRuntimeTestSelectionInclusions(tpm.setRuntimeTestInclusions(panelType));
        }
        // if here, then there was a new panel selected, do recalc
        Recalculate();
     //   int i = 1;
    }
    private void resetRuntimeTestInclusionsToAll(){
        mTestController.getTestStateDataObject().getTestDataProcessor().initRuntimeTestInclusions();

    }
    private void setUITestPanels(){
        TestPanelModel tpm = new TestPanelModel();
        mUITestPanels = tpm.getTestPanels(mTestController.getTestStateDataObject().getTestDataProcessor().getDefaultTestSelectionInclusions());
        Collections.sort(mUITestPanels, new Comparator<UITestPanel>() {
            @Override
            public int compare(UITestPanel o1, UITestPanel o2) {
                if (o1.getDisplayOrder() > o2.getDisplayOrder()) return 1;
                if (o1.getDisplayOrder() < o2.getDisplayOrder()) return -1;
                return 0;
            }
        });
    }
    public ArrayList<EpocTestPanelType> getTestPanels(){
        ArrayList<EpocTestPanelType> retval = null;
        if (mUITestPanels != null && mUITestPanels.size()>0) {
            retval = new ArrayList<>();
            for (UITestPanel utp : mUITestPanels) {
                retval.add(utp.getTestPanelType());
            }
        }
        return retval;
    }
    private void setSelectedPanel(EpocTestPanelType panelType){
        if (mUITestPanels != null && mUITestPanels.size()>0) {

            for (UITestPanel utp : mUITestPanels) {
                if (utp.getTestPanelType().equals(panelType)){
                    utp.setSelected(true);

                }
                else {
                    utp.setSelected(false);
                }
            }
        }
    }
    /*
    / if no panels , then returns Unknown : UI displays card needed.
    / if there are panels but none selected, UI displays "All"
    / else UI displays selected panel.
    / future: if selected panel is 'Custom', then UI displays Custom Analyte selection view (fragment)
     */
    public EpocTestPanelType getSelectedPanel(){
        EpocTestPanelType retval = EpocTestPanelType.UNKNOWN;
        if ((mUITestPanels == null) || (mUITestPanels!=null && mUITestPanels.size()==0)){
            return EpocTestPanelType.UNKNOWN;
        }
        if (mUITestPanels != null && mUITestPanels.size()>0) {
            retval = EpocTestPanelType.ALL;
            for (UITestPanel utp : mUITestPanels) {
                if (utp.isSelected()){
                    retval = utp.getTestPanelType();
                    break;
                }

            }
        }
        return retval;
    }
    @Override
    public ArrayList<AnalyteName> getRuntimeTestInclusions() {
        // get the runtime testinclusions  from testdataprocessor
        // if runtime test list is empty, we'll get the default tests
        //
        return mTestController.getTestStateDataObject().getTestDataProcessor().getRuntimeTestSelectionInclusions();
    }

    @Override
    public ArrayList<AnalyteOption> getCustomSelectedTests (){

       ArrayList<AnalyteOption> retval = new ArrayList<>();
       ArrayList<AnalyteOption> allSupportedTests = mTestController.getTestStateDataObject().getTestDataProcessor().getDefaultTestSelectionInclusions();
       ArrayList<AnalyteName> runtimeSelectedTests = mTestController.getTestStateDataObject().getTestDataProcessor().getRuntimeTestSelectionInclusions();

       for (AnalyteOption an:allSupportedTests  ) {

           AnalyteOption aop = new AnalyteOption();
           aop.setDisplayOrder(an.getDisplayOrder());
           aop.setAnalyteName(an.getAnalyteName());
           aop.setOptionType(EnabledSelectedOptionType.EnabledUnselected);
           for (AnalyteName ran: runtimeSelectedTests  ) {
               if (an.getAnalyteName().equals(ran)) {
                   aop.setOptionType(EnabledSelectedOptionType.EnabledSelected);
               }
           }
           retval.add(aop);
       }

       return retval;
    }

    @Override
    public boolean showCustomTestSelectionOnly() {
        return mCalculationDone;
    }

    @Override
    public void updateCommunicationBoard(TestMessageContext messageContext) {

    }

    @Override
    public void close() throws EpocTestDriverException {
        try {
            mTestController.getRxUtil().unsubscribe();
            mTestController.stop();
            mTestFlow = null;
            mTestMessageContext = null;
            EventBus.getDefault().unregister(this);
        }catch (Exception exp){
            throw new EpocTestDriverException("Couldn't stop test on Reader : " + mReaderDevice.getDeviceAlias() + " Exception : " + exp.getStackTrace().toString());
        }
    }

    private void handleTestEvent(TestEventInfo e) {
        // handle message change
        // apply test logic
        TestEventInfoType testEventInfoType = e.getTestEventInfoType();
        switch (testEventInfoType){

            case UNKNOWN:
                return;
            case ERROR_INFO:
                mStatusErrorType = e.getTestStatusErrorType();
                handleTestStateErrorEvent(e.getTestStatusErrorType());

                break;
            case STATUS_INFO:
                mStatusEventType = e.getTestStatusType();
                handleTestStateChange(e.getTestStatusType(), e.getTestEventData());
                break;
        }
        if (isVisible()) {
            mTestDriverEmmitter.onNext(this);
        }
    }

    private void initialize() {
        mResetUI = false;
        mIsCalBroken = false;
        mIsEditable = true;
        mCriticalsHandled = false;
        mCalculationDone = false;
        if (mTestMessageContext == null) {
            mTestMessageContext = new TestMessageContext();
        }else{
            mTestMessageContext.cleanUpp();
        }

        mTestMessageContext.createTestStateChangeMessages(TestStatusType.NEWTESTSTARTING);
        if (mTestFlow == null) {
            mTestFlow = new WorkflowRepository().getActiveWorkflow();
        }
        mTestDriverEmmitter = new RxUtil<TestUIDriver>().create(null);
        mMessageBoxRequestor = new RxUtil<EpocMessageBoxInfo>().create(null);
        EventBus.getDefault().register(this);
        //getWorkflowFields();
        derivedInit();
    }

    private void getWorkflowFields() {
       mWorkflowFields = new WorkflowRepository().getActiveWorkflowFields();
    }

    private void reinitializeDriver() {
        mResetUI = false;
        mIsEditable = true;
        mCriticalsHandled = false;
        mCalculationDone = false;
        isCardInserted = false;
        mTestMessageContext.cleanUpp();
        mTestMessageContext.createTestStateChangeMessages(TestStatusType.NEWTESTSTARTING);
        cleanTestInclusions();
        //getWorkflowFields(); not necessary
    }
    private boolean checkCompliance(){
        boolean retval = true;
        WorkflowItem wfi = getComplianceItem();
        if (wfi != null){
            if (wfi.getFieldList()!=null){
               if (!checkFactoryCompliance(wfi.getFieldList())){
                   return false;
               }

            }
            if (wfi.getCustomFieldList()!= null){
                if (!checkCustomCompliance(wfi.getCustomFieldList())){
                    return false;
                }
            }
        }

        return retval;

    }

    private boolean checkFactoryCompliance(RealmList<TestInputField> fieldList) {
        boolean retval = true;
        for (TestInputField tif :fieldList ) {
            if (!complies(tif)){
                retval = false;
                break;
            }
        }
        return retval;
    }

    private boolean complies(TestInputField tif) {
        boolean retval = true;
        switch (tif.getFieldType()){

            case UNKNOWN:
                retval = false;
                break;
            case PATIENTID:
                if (StringUtil.empty(getTestData().getSubjectId())) retval = false;
                break;
            case PATIENTID2:
                if (StringUtil.empty(getTestData().getId2())) retval = false;
                break;
            case TESTSELECTION:
                // always true
                break;
            case SAMPLETYPE:
                if (getTestData().getTestDetail().getSampleType().equals(BloodSampleType.Unspecified)) retval = false;
                break;
            case PATIENTTEMPERATURE:
                if (getTestData().getTestDetail().getPatientTemperature()== null) retval = false;
                break;
            case HEMODILUTION:
                if (getTestData().getTestDetail().getHemodilutionApplied() == null) retval = false;
                break;
            case COMMENTS:
                if (StringUtil.empty(getTestData().getTestDetail().getComment())) retval = false;
                break;
            case DRAWSITE:
                if (getTestData().getRespiratoryDetail().getDrawSite().equals(DrawSites.ENUM_UNINITIALIZED) &&
                        StringUtil.empty(getTestData().getRespiratoryDetail().getCustomDrawSite())) retval = false;
                break;
            case ALLENSTEST:
                if ((getTestData().getRespiratoryDetail().getRespAllensType().equals(AllensTest.ENUM_UNINITIALIZED) ||
                        getTestData().getRespiratoryDetail().getRespAllensType().equals(AllensTest.NA)) &&
                        StringUtil.empty(getTestData().getRespiratoryDetail().getCustomAllensTest())) retval = false;
                break;
            case DELIVERYSYSTEM:
                if (getTestData().getRespiratoryDetail().getDeliverySystem().equals(DeliverySystem.ENUM_UNINITIALIZED) &&
                        StringUtil.empty(getTestData().getRespiratoryDetail().getCustomDeliverySystem())) retval = false;
                break;
            case MODE:
                if (getTestData().getRespiratoryDetail().getRespiratoryMode().equals(RespiratoryMode.ENUM_UNINITIALIZED) &&
                        StringUtil.empty(getTestData().getRespiratoryDetail().getCustomMode())) retval = false;
                break;
            case FIO2:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getFiO2())) retval = false;
                break;
            case VT:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getTidalVolume())) retval = false;
                break;
            case RR:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getRespiratoryRate())) retval = false;
                break;
            case TR:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getTotalRespiratoryRate())) retval = false;
                break;
            case PEEP:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getPeep())) retval = false;
                break;
            case PS:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getPs())) retval = false;
                break;
            case IT:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getInspiratoryTime())) retval = false;
                break;
            case ET:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getExpiratoryTime())) retval = false;
                break;
            case PIP:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getPeakInspiratoryPressure())) retval = false;
                break;
            case MAP:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getMeanAirWayPressure())) retval = false;
                break;
            case ORDERINGPHYSICIAN:
                if (StringUtil.empty(getTestData().getTestDetail().getOrderingPhysician())) retval = false;
                break;
            case ORDERDATE:
                if (getTestData().getTestDetail().getOrderDate()==null) retval = false;
                break;
            case ORDERTIME:
                if (StringUtil.empty(getTestData().getTestDetail().getOrderTime())) retval = false;
                break;
            case COLLECTEDBY:
                if (StringUtil.empty(getTestData().getTestDetail().getCollectedBy())) retval = false;
                break;
            case COLLECTIONDATE:
                if (getTestData().getTestDetail().getCollectionDate()==null) retval = false;
                break;
            case COLLECTIONTIME:
                if (StringUtil.empty(getTestData().getTestDetail().getCollectionTime())) retval = false;
                break;
            case PATIENTLOCATION:
                if (StringUtil.empty(getTestData().getTestDetail().getPatientLocation())) retval = false;
                break;
            case FLOW:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getFlow())) retval = false;
                break;
            case PATIENTGENDER:
                if (getTestData().getGender().equals(Gender.ENUM_UNINITIALIZED)) retval = false;
                break;
            case PATIENTAGE:
                if (getTestData().getPatientAge()== null || getTestData().getPatientAge()<0) retval = false;
                break;
            case HERTZ:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getHertz())) retval = false;
                break;
            case DELTAP:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getDeltaP())) retval = false;
                break;
            case NOPPM:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getNoPPM())) retval = false;
                break;
            case RQ:
                if (StringUtil.empty(getTestData().getRespiratoryDetail().getRq())) retval = false;
                break;
            case LOTNUMBER:
                if (StringUtil.empty(getTestData().getCardLot().getLotNumber())) retval = false;
                break;
            case FLUIDTYPE:
                if (getTestData().getTestDetail().getQaSampleInfo().getQaFluidType().equals(QAFluidType.None)) retval = false;
                break;
            case TESTTYPE:
                if (getTestData().getType().equals(TestType.Unknown)) retval = false;
                break;
            case CUSTOM:
                break;
        }
        return retval;
    }

    private boolean checkCustomCompliance(RealmList<CustomTestInputField> customComplianceFieldList) {
        boolean retval = true;
        RealmList<CustomTestVariable> customs = getTestData().getTestDetail().getCustomTestVariables();
        if (customs == null){
            return false;
        }   else {
            for ( CustomTestInputField inputField : customComplianceFieldList ){
                retval = false;
                for ( CustomTestVariable customVariable: customs ) {
                    if (customVariable.getName().equals(inputField.getName()) && !StringUtil.empty(customVariable.getValue())){
                        retval = true;
                        break;
                    }
                }
                if (!retval) break;
            }
        }
        return retval;
    }

    private WorkflowItem getComplianceItem() {
        WorkflowItem retval = null;
        for (WorkflowItem wfi : mTestFlow.getWorkflowItems()) {
            if (wfi.getFieldGroupType().equals(EpocTestFieldGroupType.COMPLIANCE)){
                retval = wfi;
                break;
            }
        }
        return retval;
    }

    private void handleTestStateChange(TestStatusType testStatusType, Object testStatusObject) {
        mTestMessageContext.createTestStateChangeMessages(testStatusType, testStatusObject);
        mTestMessageContext.setClickHandler(null);
        try {
            mTestMessageContext.mBatteryLevel = mTestController.getTestStateDataObject().getTestDataProcessor().mRsrMsg.hwStatus.getBatteryLevel();
        } catch (Exception e) {
            Log.d(TAG,"Invalid battery level");
        }
        devMessage = testStatusType.toString();
        switch(testStatusType){

            case UNKNOWN:
                break;
            case NEWTESTSTARTING:
                setCardInserted(false);
                break;
            case CONNECTING:
                break;
            case CONFIGURATION:
                break;
            case READYTOTEST:
                break;
            case FLUIDICSCALIBRATION:
                mIsCalBroken = true;
                createData();
                break;
            case SAMPLEINTRODUCTION:
                break;
            case SAMPLEPROCESSING:
                break;
            case TESTCALCULATING:
                break;
            case TESTRECALCULATED:
            case TESTCALCULATED:
                mCalculationDone = true;
                // When calculation is done, the test moves to Completed state.
                TestManager.getInstance().setDeviceTestCompletedState(true);
                // editability change: Patient ID
                EventBus.getDefault().post(new UIChangeRequest(UIChangeRequestReason.EDITABILITY_CHANGED));
                break;
            case MISSINGDATAREQUIRED:
                break;
            case NOTESTRUNNING:
                break;
            case CALCULATEDANDUNEDITABLE:
                break;
            case READINGCARD:
                int i=0;
                break;
            case QCLOCKOUT:
                break;
            case QCEXPIRINGWARNING:
                break;
            case QCEXPIREDWARNING:
                break;
            case QCEXPIREDWARNINGCARDINREADER:
                break;
            case QCGRACEPERIODWARNING:
                break;
            case CONNECTED:
                updateReader();
                break;
            case DISCONNECTED:
                break;
            case DEVICEINFO:
                break;
            case CARDINSERTED:
                TestManager.getInstance().setDeviceTestState(true);
                handleCardInserted(testStatusObject);
                break;
            case CARDREMOVED:
                if (!mCalculationDone)
                    setCardInserted(false);
                break;
        }
    }
    private void handleCardInserted (final Object testStateObject){
        isCardInserted = true;
        mCalculationDone = false;
        boolean allowRecallData = new HostConfigurationModel().getUnmanagedHostConfiguration().isAllowRecallData();
        setCardInserted(true);
        setCriticalsHandled(false);
        // test started by card
        if (mResetUI){

            requestEpocMessageBoxRemoval();
            if (allowRecallData || mTestController.getTestStateDataObject().getTestDataProcessor().previousTestIncomplete()){
              actionPostponed = true;
                requestEpocMessageBox(
                        StringResourceUtil.getStringIDByName("start_test_keepdata"),
                        StringResourceUtil.getStringIDByName("yes"),
                        StringResourceUtil.getStringIDByName("no"),
                        EpocUIMessageType.USERACTION_REQUEST,
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                recallDataOptionHandler(true,testStateObject);
                            }
                        },
                        null,
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                recallDataOptionHandler(false,testStateObject);
                            }
                        }
                );
            } else {
                // reset testdata and uidriver
                resetData(false);
                this.setEditable(true);
                if (isVisible()) {
                    resetUI(UIChangeRequestReason.CARDINSERTED_NEWTEST_DONTKEEPDATA);
                }

            }

        } else {
        // new test

        }
        // always:
        setUITestPanels();
        mResetUI = true;



    }

    private void recallDataOptionHandler(boolean keepPreviousData, Object testStateObject) {
        // execute postponed steps
        if(keepPreviousData){
           resetData(true);
        } else {
            resetData(false);
            this.setEditable(true);
        }
        if (isVisible()) {
            if(keepPreviousData){
                resetUI(UIChangeRequestReason.CARDINSERTED_NEWTEST_KEEPDATA);

            } else {
                resetUI(UIChangeRequestReason.CARDINSERTED_NEWTEST_DONTKEEPDATA);
            }
        }
    actionPostponed = false;
        //
    }

    private void updateReader() {
        ReaderModel rm = new ReaderModel();
        Reader rdr;
        // reader must go into Realm if successfully connected to during epoctest
            rdr = mTestController.getTestStateDataObject().getTestDataProcessor().getTestRecord().getReader();
            rdr.setLastConnected(new Date());
            rm.saveReader(rdr);
    }

    private void handleTestStateErrorEvent(TestStatusErrorType testStatusErrorType) {
        setCardInserted(true);
        mTestMessageContext.createTestStateErrorMessages(testStatusErrorType);
        switch(testStatusErrorType) {
            case COMMUNICATIONFAILED:
            case UNABLETOCONNECT:
                break;
            default:
                mTestMessageContext.setClickHandler(null);
                break;
        }
        devMessage = testStatusErrorType.toString();
    }
    private void requestEpocMessageBoxRemoval(){
        if (isVisible()) {
            EpocMessageBoxInfo epocMessageBoxInfo = new EpocMessageBoxInfo();
            epocMessageBoxInfo.setToRemove(true);
            this.mMessageBoxRequestor.onNext(epocMessageBoxInfo);
        }
    }
    private void requestEpocMessageBox(int messageResId, int positiveTxtId, int negativeTxtId,
                                       EpocUIMessageType messageType, Action positiveAction, Action middleAction, Action negativeAction){
        if (isVisible()) {
        EpocMessageBoxInfo epocMessageBoxInfo = new EpocMessageBoxInfo();
        epocMessageBoxInfo.setMessageStringResId(messageResId);
        epocMessageBoxInfo.setPositiveTextResId(positiveTxtId);
        epocMessageBoxInfo.setNegativeTextResId(negativeTxtId);
        epocMessageBoxInfo.setToRemove(false);
        epocMessageBoxInfo.setEpocUIMessageType(messageType);
        epocMessageBoxInfo.setPositiveAction(positiveAction);
        epocMessageBoxInfo.setMiddleAction(middleAction);
        epocMessageBoxInfo.setNegativeAction(negativeAction);

            this.mMessageBoxRequestor.onNext(epocMessageBoxInfo);
        }

    }
    private void cleanTestInclusions(){
        getTestController().getTestStateDataObject().getTestDataProcessor().getDefaultTestSelectionInclusions().clear();
        getTestController().getTestStateDataObject().getTestDataProcessor().getRuntimeTestSelectionInclusions().clear();

    }

    abstract void derivedInit();

    public void saveCustomVariable(String customInputFieldName, String inputValue) {
        mTestController.getTestStateDataObject().getTestDataProcessor().saveCustomTestVariable(customInputFieldName,inputValue);
    }


    public void resetData(boolean preserveData) {
        mTestController.getTestStateDataObject().getTestDataProcessor().resetTestRecord(preserveData);
    }
    @Subscribe
    public  void onEventFromTestDataProcessor(TestDataProcessorCallback callback){
        switch (callback.getEventType()){
            case GENERAL_PROCESSOR_ERROR:
                break;
            case TESTINCLUSIONS_UPDATED:
                setUITestPanels();
                EventBus.getDefault().post(new UIChangeRequest(UIChangeRequestReason.UPDATE_TESTSELECTION));
                break;
        }
    }
    /**
     * Broadcasts message to the InactivityService which is taking care of user inactivity timer.
     *
     * @param value the flag to convey whether reset the timer (true) or stop the timer (false)
     */
    private void inactivityBroadcast(boolean value) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.epocal.host4.inactivity");
        broadcastIntent.putExtra("inactivity", value);
        GloabalObject.getApplication().getApplicationContext().sendBroadcast(broadcastIntent);
    }
}
