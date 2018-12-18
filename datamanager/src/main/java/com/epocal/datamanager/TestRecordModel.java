package com.epocal.datamanager;

import android.util.Log;

import com.epocal.common.CU;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.realmentities.CardLot;
import com.epocal.common.realmentities.CustomTestVariable;
import com.epocal.common.realmentities.QASampleInfo;
import com.epocal.common.realmentities.Range;
import com.epocal.common.realmentities.RangeValue;
import com.epocal.common.realmentities.Reader;
import com.epocal.common.realmentities.RespiratoryDetail;
import com.epocal.common.realmentities.TestDetail;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.TestResult;
import com.epocal.common.types.QAFluidType;
import com.epocal.common.types.ResultStatus;
import com.epocal.common.types.SyncState;
import com.epocal.common.types.TestStatus;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.DeliverySystem;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.am.Temperatures;
import com.epocal.common.types.am.TestMode;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by bmate on 5/29/2017.
 * NOTE: we cannot log to DB from here
 */

public class TestRecordModel {

    /**
     * String constants to define column name to search for TestRecord object
     */
    public static final String TESTDATE_FROM = "TESTDATE_FROM";
    public static final String TESTDATE_TO   = "TESTDATE_TO";
    public static final String TESTDATETIME  = "testDateTime";
    public static final String TESTTYPE       = "testType";
    public static final String TESTMODE       = "testModeValue";
    public static final String SYNC_STATE     = "synchronizationState";

    /**
     * delete a testrecord - cascading by its Id value
     * @return TestRecord
     **/
    public  void deleteTestResultsFromTestRecord(final TestRecord testRecord)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                    if (testRecord.getTestResults()!= null && testRecord.getId()!=-1){
                        final TestRecord tr = realm.where(TestRecord.class).equalTo("id",testRecord.getId()).findFirst();
                        tr.getTestResults().deleteAllFromRealm();

                    }


            }
        });
        testRecord.getTestResults().clear();
        //testRecord.setTestResults(null);


        realm.close();
    }

    /**
     * delete a testrecord - cascading by its Id value
     * @return TestRecord
     **/
    public  void deleteTestRecordById(long id)
    {
        Realm realm = Realm.getDefaultInstance();
        final TestRecord tr = realm.where(TestRecord.class).equalTo("id",id).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (tr !=null){
                    if (tr.getTestDetail()!= null){
                        if (tr.getTestDetail().getQaSampleInfo()!= null) {
                            tr.getTestDetail().getQaSampleInfo().deleteFromRealm();
                            }

                        if (tr.getTestDetail().getCustomTestVariables()!=null){
                            RealmResults<CustomTestVariable> customVariables = realm.where(CustomTestVariable.class).equalTo("testRecordId",tr.getId()).findAll();
                            customVariables.deleteAllFromRealm();
                        }
                        tr.getTestDetail().deleteFromRealm();
                    }
                    if (tr.getTestResults()!= null){
                            RealmResults<TestResult> results = realm.where(TestResult.class).equalTo("testRecordId",tr.getId()).findAll();
                            results.deleteAllFromRealm();
                    }
                    if (tr.getRespiratoryDetail()!= null){
                        tr.getRespiratoryDetail().deleteFromRealm();
                    }

                    tr.deleteFromRealm();
                }
            }
        });


        realm.close();
    }
    /**
     * get a testrecord (Managed) by its Id value
     * @return TestRecord
     **/
    public  TestRecord getTestRecordById(long id, Realm realm)
    {
        return realm.where(TestRecord.class).equalTo("id",id).findFirst();
    }
    /**
     * get a testrecord (Unmanaged!)by its Id value
     * @return TestRecord
     **/
    public  TestRecord getTestRecordById(long id){

        TestRecord retval = null;
        Realm realm = Realm.getDefaultInstance();
        TestRecord managedTR = realm.where(TestRecord.class).equalTo("id",id).findFirst();
        if (managedTR != null) {
            retval = realm.copyFromRealm(managedTR);
        }
        realm.close();
        return retval;
    }

    /**
     * get a testresult (Managed) by the testrecord id and the analyte name
     * @return TestResult
     **/
    public TestResult getTestResultById(long id, AnalyteName analyteName, Realm realm)
    {
        TestResult retval = null;
        TestRecord tr = realm.where(TestRecord.class).equalTo("testRecordId",id).findFirst();
        if (tr != null){
            if (tr.getTestResults().size()>0) {
                for (TestResult res : tr.getTestResults()) {
                    if (res.getAnalyteName().compareTo(analyteName) == 0) {
                        retval = res;
                        break;
                    }
                }
            }
        }
        return retval;
    }

    /**
     * get number of tests unsent
     * @return integer
     **/
    public int getUnsentTestCount(){
        int retval = 0;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TestRecord> unsentTests = realm.where(TestRecord.class).isNull("uploaded").findAll();
        if (unsentTests!=null){
            retval = unsentTests.size();
        }
        realm.close();
        return retval;
    }

    /**
     * get list of tests unsent
     * @return RealmResults<TestRecord>
     **/
    public RealmResults<TestRecord> getUnsentTests(Realm realm){
        return realm.where(TestRecord.class).isNull("uploaded").findAll();
    }

    /**
     * flag testrecord as sent at a given date
     * @return RealmResults<TestRecord>
     **/
    public void FlagTestRecordsAsSent(Date date, int[] ids){
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            for (int id: ids) {
                TestRecord tr = realm.where(TestRecord.class).equalTo("id",id).findFirst();
                tr.setUploaded(date);
            }
            realm.commitTransaction();
            realm.close();
        }catch(Exception e){
            Log.e("Realm Error",e.getMessage() );
            realm.cancelTransaction();
            realm.close();
        }
    }

    /**
     * create a new (empty)testrecord  - unmanaged in realm

     **/
    public TestRecord createUnmanagedTestRecord(){
        TestRecord tr = new TestRecord();
        TestDetail td  = new TestDetail();
        td.setQaSampleInfo(new QASampleInfoModel().getUnmanagedSampleInfo(QAFluidType.None));
        RespiratoryDetail rd = new RespiratoryDetail();
        tr.setTestDetail(td);
        tr.setRespiratoryDetail(rd);
        return tr;
    }

    /**
     * save an unmanaged TestRecord object to Realm for the first time
     **/
    public void createTestRecordFromUnmanaged(TestRecord testRecord) {
        Realm realm = Realm.getDefaultInstance();
        final long retval = PrimaryKeyFactory.getInstance().nextKey(TestRecord.class);
        try {
            realm.beginTransaction();
            testRecord.setId(retval);
            testRecord.getRespiratoryDetail().setId(PrimaryKeyFactory.getInstance().nextKey(RespiratoryDetail.class));
            testRecord.getTestDetail().setId(PrimaryKeyFactory.getInstance().nextKey(TestDetail.class));
            testRecord.getTestDetail().getQaSampleInfo().setId(PrimaryKeyFactory.getInstance().nextKey(QASampleInfo.class));
            prepCustomTestVars(testRecord);
            realm.copyToRealmOrUpdate(testRecord);
            realm.commitTransaction();
            realm.close();

        }catch(Exception e){
            Log.e("Realm Error",e.getMessage() );
            realm.cancelTransaction();
            realm.close();
        }

    }

    private void prepCustomTestVars(TestRecord testRecord) {
        if(testRecord.getTestDetail().getCustomTestVariables()!=null){
            for (CustomTestVariable ctv:testRecord.getTestDetail().getCustomTestVariables()) {
                ctv.setTestRecordId(testRecord.getId());
                ctv.setId(PrimaryKeyFactory.getInstance().nextKey((CustomTestVariable.class)));
            }
        }
    }


    public CardLot findCardLot(String lotNumber) {
        CardLot retval = null;
        Realm realm = Realm.getDefaultInstance();
        CardLot managedCL = realm.where(CardLot.class)
                .equalTo("lotNumber",lotNumber)
                .findFirst();
        if (managedCL!=null) {
            retval = realm.copyFromRealm(managedCL);
        }
        realm.close();
        return  retval;
    }
    /*
    create a cardlot object from an unmanaged one.
     */
    public void createCardLot(CardLot cl) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            cl.setId(PrimaryKeyFactory.getInstance().nextKey(CardLot.class));
            realm.insertOrUpdate(cl);
            realm.commitTransaction();
        }catch (Exception ex){
            Log.e("Realm Error",ex.getMessage() );
            realm.cancelTransaction();
            realm.close();
        }
    }

    // this will create duplicates for all relations without primary key!
    public void copyOrUpdate(TestRecord testRecord) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.insertOrUpdate(testRecord);
            realm.commitTransaction();
            realm.close();

        }catch(Exception e){
            Log.e("Realm Error",e.getMessage() );
            realm.cancelTransaction();
            realm.close();
        }
    }
    // this will Create, Update, Delete testresults into realm
    public void persistTestResults(TestRecord unmanagedTestRecord){
        deleteTestResults(unmanagedTestRecord);
        insertOrUpdateTestResults(unmanagedTestRecord);
    }
    private void deleteTestResults (TestRecord unmanagedTestRecord){
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            TestRecord managedTestRecord = realm.where(TestRecord.class).equalTo("id",unmanagedTestRecord.getId()).findFirst();
            if (managedTestRecord.getTestResults()!=null && managedTestRecord.getTestResults().size()>0){
               realm.where(TestResult.class).equalTo("testRecordId",managedTestRecord.getId())
                       .findAll()
                       .deleteAllFromRealm() ;
               managedTestRecord.getTestResults().clear();
            }
//
            realm.commitTransaction();
            realm.close();
        } catch(Exception e){
            Log.e("RErrdelTestRes",e.getMessage() );
            realm.cancelTransaction();
            realm.close();
        }

    }
    private void insertOrUpdateTestResults(TestRecord testRecord) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();

            TestRecord managedTestRecord = realm.where(TestRecord.class).equalTo("id",testRecord.getId()).findFirst();
            // create or update
            for (TestResult tr:testRecord.getTestResults()) {
                TestResult mtestresult = realm.where(TestResult.class).equalTo("testRecordId",testRecord.getId()).equalTo("analyte",tr.getAnalyteName().value).findFirst();
                // create new testresult
                if (mtestresult==null){
                    long id = PrimaryKeyFactory.getInstance().nextKey(TestResult.class);
                    mtestresult = realm.createObject(TestResult.class,id);
                    mtestresult.setTestRecordId(testRecord.getId());
                    tr.setId(id);
                    tr.setTestRecordId(testRecord.getId());
                    managedTestRecord.getTestResults().add(mtestresult);

                }
                // update testresult
                mtestresult.updateFrom(tr);
            }

            realm.commitTransaction();
            realm.close();

        }catch(Exception e){
            Log.e("Realm Error",e.getMessage() );
            realm.cancelTransaction();
            realm.close();
        }

    }

    // this will insert or update a Custom Test Variable belonging to a test record
    public void insertOrUpdateCustomVariable(TestRecord testRecord, String variableName){
        Realm realm = Realm.getDefaultInstance();
       final CustomTestVariable ctvar = getCustomVariable(testRecord,variableName);
        try {
            realm.beginTransaction();
                TestRecord managedTestRecord = realm.where(TestRecord.class).equalTo("id",testRecord.getId()).findFirst();
               CustomTestVariable managedCTVar = realm.where(CustomTestVariable.class).equalTo("testRecordId",testRecord.getId()).equalTo("name",ctvar.getName()).findFirst();
                //  operations on brand new
                if (managedCTVar==null){
                    long id = PrimaryKeyFactory.getInstance().nextKey(CustomTestVariable.class);
                    managedCTVar = realm.createObject(CustomTestVariable.class,id);
                    managedCTVar.setTestRecordId(testRecord.getId());
                    managedCTVar.setName(ctvar.getName());
                    managedCTVar.setDisplayOrder(ctvar.getDisplayOrder());
                    managedCTVar.setEpocTestFieldOptionType(ctvar.getEpocTestFieldOptionType());
                    managedCTVar.setFieldGroupType(ctvar.getFieldGroupType());
                    ctvar.setId(id);
                    managedTestRecord.getTestDetail().getCustomTestVariables().add(managedCTVar);
                }
                // operations on existing
                managedCTVar.setValue(ctvar.getValue());

            realm.commitTransaction();
            realm.close();

        }catch(Exception e){
            Log.e("Realm Error",e.getMessage() );
            realm.cancelTransaction();
            realm.close();
        }
    }

    private CustomTestVariable getCustomVariable(TestRecord testRecord, String variableName) {
        CustomTestVariable retval = null;
        for (CustomTestVariable ctv:testRecord.getTestDetail().getCustomTestVariables()) {
            if (ctv.getName().equals(variableName)){
                retval = ctv;
                break;
            }
        }
        return  retval;
    }

    //////////////////////////////////////////////
    // Static methods
    // -- Called from Test History where
    //    realm instance is managed by Activity
    //    for the live query results and update.
    //////////////////////////////////////////////

    // Sync call
    static public TestRecord fetch(Realm realm, long id) {
        if (realm == null) return null;

        return realm.where(TestRecord.class).equalTo("id", id).findFirst();
    }

    /**
     * Query the list of TestRecord matching searchCriterion (Map of column name -> value)
     * The criterion passed in map is interpreted as "equalTo".
     *
     * It is a Synchronous call (blocking call)
     *
     * @param realm -- Activity managed realm instance
     * @param searchCriterion - Map of search criterion containing: column name -(equalTo)-> value
     * @param sortFieldName - Column name to sort on
     * @param sortOrder - Sort order (ASC or DESC)
     * @return list of TestRecord matching the criteria (Realm managed results list is returned)
     */
    static public RealmResults<TestRecord> fetch(Realm realm,
                                                 Map<String, String> searchCriterion,
                                                 String sortFieldName,
                                                 Sort sortOrder)
    {
        if (realm == null) return null;

        // Build query
        RealmQuery<TestRecord> query = realm.where(TestRecord.class);

        for (String key : searchCriterion.keySet()) {
            String value = searchCriterion.get(key);

            if (key.equals(TESTDATE_FROM)) {
                // Date type value. If the date string cannot parse to Date, ignore this criteria
                try {
                    query = query.greaterThan(TESTDATETIME, CU.epocDateStringToDate(value));
                } catch (ParseException parseEx) {
                    parseEx.printStackTrace();
                }
            } else if (key.equals(TESTDATE_TO)) {
                try {
                    query = query.lessThan(TESTDATETIME, CU.epocDateStringToDate(value));
                } catch (ParseException parseEx) {
                    // Date type value. If the date string cannot parse to Date, ignore this criteria
                    parseEx.printStackTrace();
                }
            } else if (key.equals(SYNC_STATE)) {
                // int type value.
                query.equalTo(key, Integer.parseInt(value));
            } else if (key.equals(TESTMODE)) {
                // int type value.
                query.equalTo(key, Integer.parseInt(value));
            } else if (key.equals(TESTTYPE)) {
                // int type value.
                query.equalTo(key, Integer.parseInt(value));
            } else {
                // String type value.
                query = query.equalTo(key, value);
            }
        }

        return query.findAllSorted(sortFieldName, sortOrder);
    }

    /**
     * Query the list of TestRecord (of testMode=BloodTest) with "partial" string match
     * with any of the following fields:
     * - Subject ID (i.e. Patient name is stored in this field.)
     * - Operator ID
     * - Card lot number
     * - Reader serial number
     * String value search is Case Insensitive.
     *
     * It is a Synchronous call (blocking call)
     *
     * @param realm -- Activity managed realm instance
     * @param partialString - string to match
     * @param sortFieldName - Column name to sort on
     * @param sortOrder - Sort order (ASC or DESC)
     * @return - list of TestRecord matching the criteria (Realm managed results list is returned)
     */
    static public RealmResults<TestRecord> fetchMatchAnyPatientBloodTest(Realm realm,
                                                                         String partialString,
                                                                         String sortFieldName,
                                                                         Sort sortOrder)
    {
        RealmQuery<TestRecord> query = realm.where(TestRecord.class);
        // Only need Patient Blood Test - testMode=BloodTest and testType=Blood are fixed.
        query.equalTo("testModeValue", TestMode.BloodTest.value);
        query.equalTo("testType", TestType.Blood.value);

        // Add partial string match conditions
        query.beginGroup();
        query.contains("subjectId", partialString, Case.INSENSITIVE); // patient ID
        query.or().contains("user.UserId", partialString, Case.INSENSITIVE); // operator ID
        query.or().contains("cardLot.lotNumber", partialString, Case.INSENSITIVE); // CardLot number
        query.or().contains("reader.serialNumber", partialString, Case.INSENSITIVE); // Reader SN
        query.endGroup();

        return query.findAllSorted(sortFieldName, sortOrder);
    }

    /**
     * Query the list of TestRecord (of testMode=QA) with "partial" string match
     * with any of the following fields:
     * - Subject ID (i.e: Fluid Lot number is stored in this field.)
     * - comment
     * String value search is Case Insensitive.
     *
     * It is a Synchronous call (blocking call)
     *
     * @param realm -- Activity managed realm instance
     * @param partialString - string to match
     * @param sortFieldName - Column name to sort on
     * @param sortOrder - Sort order (ASC or DESC)
     * @return - list of TestRecord matching the criteria (Realm managed results list is returned)
     */
    static public RealmResults<TestRecord> fetchMatchAnyQATest(Realm realm,
                                                               String partialString,
                                                               String sortFieldName,
                                                               Sort sortOrder)
    {
        RealmQuery<TestRecord> query = realm.where(TestRecord.class);

        // Only need QA Test - testMode=QA
        query.equalTo("testModeValue", TestMode.QA.value);

        // Add partial string match conditions
        query.beginGroup();
        query.contains("subjectId", partialString, Case.INSENSITIVE); // fluid lot ID
        query.or().contains("user.UserId", partialString, Case.INSENSITIVE); // operator ID
        query.or().contains("testDetail.comment", partialString, Case.INSENSITIVE); // comment
        query.or().contains("cardLot.lotNumber", partialString, Case.INSENSITIVE); // CardLot number
        query.or().contains("reader.serialNumber", partialString, Case.INSENSITIVE); // Reader SN
        query.endGroup();

        return query.findAllSorted(sortFieldName, sortOrder);
    }

    /**
     *
     * Query the list of TestRecord whose SyncState is "Sent" (or "Unsent") in Test History
     * module's UI perspective.
     *
     * It is a Synchronous call (blocking call)
     *
     * @param realm -- Activity managed realm instance
     * @param isSent -- true means SyncState of TestRecord is "Sent" state in Test History
     *               module's UI perspective.
     *                  false means SyncState of TestRecord is "Unsent" state
     *               See THDataHelper.isTestRecordSent(TestRecord record) for
     *               the logic to determine "Sent" state.
     * @param sortFieldName - Column name to sort on
     * @param sortOrder - Sort order (ASC or DESC)
     * @return - list of TestRecord matching the criteria (TestRecord SyncState "Sent" or "Unsent")
     */
    // Sync
    static public RealmResults<TestRecord> fetchBySyncState(Realm realm,
                                                 boolean isSent,
                                                 boolean isQATest,
                                                 String sortFieldName,
                                                 Sort sortOrder) {
        if (realm == null) return null;

        // Build query
        RealmQuery<TestRecord> query = realm.where(TestRecord.class);
        if (isQATest) {
            query.equalTo("testModeValue", TestMode.QA.value);
        } else {
            // Patient Blood Test - testMode=BloodTest and testType=Blood are fixed.
            query.equalTo("testModeValue", TestMode.BloodTest.value);
            query.equalTo("testType", TestType.Blood.value);
        }

        if (isSent) {
            // Sent states
            query.equalTo("synchronizationState", SyncState.SentSuccessfully.value);
        } else {
            // Unsent states - is negation of above case.
            query.notEqualTo("synchronizationState", SyncState.SentSuccessfully.value);
        }

        return query.findAllSorted(sortFieldName, sortOrder);
    }

    // Async
    static public void delete(Realm realm,
                                 final Long[] idList,
                                 Realm.Transaction.OnSuccess onSuccess,
                                 Realm.Transaction.OnError onError)
    {
        // TODO: Check realm/onSuccess/onError null case and throw exception.
        // TODO: Caller needs to catch and print error message.
        // Input check
        if (realm == null) {
            String className = TestRecordModel.class.getSimpleName();
            String methodName = TestRecordModel.class.getEnclosingMethod().getName();
            String errorString = "realm object is null.";
            onError.onError(new IllegalArgumentException(className + "::" + methodName + " " + errorString));
        }

        if ((idList == null) || (idList.length == 0)) {
            onSuccess.onSuccess(); // no action to perform.
        }

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(TestRecord.class).in("id", idList).findAll().deleteAllFromRealm();
            }
        }, onSuccess, onError);
    }

    /**
     * This method is called by Test History module to update the SyncState of TestRecord in idList.
     * This is an administrator override for the SyncState. State is set as follows:
     * When isSent == true,  SyncState.SentAdminOverride
     * When isSent == false, SyncState.UnsentAdminOverride
     *
     * It is Async Call.
     *
     * @param realm -- Activity managed realm instance
     * @param isSent -- true means SyncState of TestRecord is "Sent" state in Test History
     *               module's UI perspective.
     *                  false means SyncState of TestRecord is "Unsent" state
     *               See THDataHelper.isTestRecordSent(TestRecord record) for
     *               the logic to determine "Sent" state.
     * @param idList -- list of TestRecord id to update SyncState.
     * @param onSuccess -- Callback on Realm db transaction success.
     * @param onError   -- Callback on Realm db transaction error.
     */
    // Async
    static public void updateSyncState(Realm realm,
                                       boolean isSent,
                                       final Long[] idList,
                                       Realm.Transaction.OnSuccess onSuccess,
                                       Realm.Transaction.OnError onError)
    {
        // Input check
        if (realm == null) {
            String className = TestRecordModel.class.getSimpleName();
            String methodName = TestRecordModel.class.getEnclosingMethod().getName();
            String errorString = "realm object is null.";
            onError.onError(new IllegalArgumentException(className + "::" + methodName + " " + errorString));
        }

        if ((idList == null) || (idList.length == 0)) {
            onSuccess.onSuccess(); // no action to perform.
        }

        final SyncState state = isSent ? SyncState.SentSuccessfully : SyncState.Unsent;
        realm.executeTransactionAsync(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                RealmResults<TestRecord> results = realm.where(TestRecord.class).in("id", idList).findAll();
                for (TestRecord record : results) {
                    record.setSyncState(state);
                }
            }
        }, onSuccess, onError);
    }

    // todo: remove for release
    public TestRecord instrumentMockTestResult(){
        Realm realm = Realm.getDefaultInstance();
        TestRecord tr = realm.where(TestRecord.class).equalTo("id2","Mock_Test").findFirst();
        if (tr !=null){
            TestRecord retval = realm.copyFromRealm(tr);
            return retval;
        } else {

            TestRecord mockTestRecord = createUnmanagedTestRecord();
            // add epoctestfields
            mockTestRecord.setTestMode(TestMode.BloodTest);
            mockTestRecord.setSubjectId("EpocPatient");
            mockTestRecord.setId2("Mock_Test");
            mockTestRecord.setPatientAge(41);
            mockTestRecord.setGender(Gender.Female);
            mockTestRecord.setTestDateTime(new Date());
            mockTestRecord.getTestDetail().setSampleType(BloodSampleType.MixedVenous);
            mockTestRecord.getTestDetail().setPatientName("EpocPatient");
            mockTestRecord.getTestDetail().setPatientTemperature(38.2);
            mockTestRecord.getTestDetail().setPatientTemperatureType(Temperatures.C);
            mockTestRecord.getTestDetail().setHemodilutionApplied(false);
            mockTestRecord.getTestDetail().setCollectedBy("epoc_NP");
            mockTestRecord.getTestDetail().setComment("This is a mock blood test for software stabilization purposes.");
            mockTestRecord.getTestDetail().setPatientLocation("Ottawa");
            mockTestRecord.getRespiratoryDetail().setCustomMode("mock_baloon");
            mockTestRecord.getRespiratoryDetail().setDeliverySystem(DeliverySystem.BiPAP);
            mockTestRecord.getRespiratoryDetail().setDrawSite(DrawSites.LFinger);
            mockTestRecord.getRespiratoryDetail().setFlow("MockFlow");
            mockTestRecord.getRespiratoryDetail().setInspiratoryTime("Mock_3min");
            mockTestRecord.getRespiratoryDetail().setDeltaP("MockDelt_a_p");
            mockTestRecord.getRespiratoryDetail().setExpiratoryTime("Mock_ET");

            // get the range
            Range mixedVenousRange = new RangeModel().getUnmanagedRange(BloodSampleType.MixedVenous, "default");
            // create a list of mock results
            RealmList<TestResult> testResults = new RealmList<>();
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Na, 142.78654d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pCO2, 27.123456, ResultStatus.ReferenceLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pCO2T, 3.7123456, ResultStatus.CriticalLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pO2, 95.123456, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Ca, null, ResultStatus.CNC));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.K, 5.789d, ResultStatus.ReferenceHigh));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pH, 7.456234d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Chloride, null, ResultStatus.FailediQC));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Lactate, 0.276834, ResultStatus.BelowReportableRange));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Glucose, 87.123456, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Creatinine, 17.123456, ResultStatus.AboveReportableRange));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.BUN, 117.123456, ResultStatus.CriticalHigh));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.ArtAlvOxRatio, 1d, ResultStatus.CriticalLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.cArtAlvOxRatio, -1d, ResultStatus.CriticalLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.eGFR, 28d, ResultStatus.ReferenceLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.eGFRa, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.eGFRj, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.GFRckd, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.GFRckda, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.GFRswz, 90d, ResultStatus.Normal));

            // save into Realm
            createTestRecordFromUnmanaged(mockTestRecord);

            // add the list to the testrecord
            mockTestRecord.setTestResults(testResults);

            // save test results
            persistTestResults(mockTestRecord);
            return mockTestRecord;
        }
    }



    // todo : remove for release
    private TestResult createTestResult(Range range, AnalyteName analyteName, Double value, ResultStatus resultStatus){
        // create TestResult

        TestResult tr = new TestResult();
        tr.setAnalyteName(analyteName);
        tr.setValue(value);
        // get ref and crt values
        RangeValue rv = range.getRangevalue(analyteName);
        tr.setCriticalHigh(rv.getCriticalHigh());
        tr.setCriticalLow(rv.getCriticalLow());
        tr.setReferenceHigh(rv.getReferenceHigh());
        tr.setReferenceLow(rv.getReferenceLow());

        // get reportables
        Analyte analyte = new AnalyteModel().getAnalyte(analyteName);
        tr.setReportableHigh(analyte.getReportableHigh());
        tr.setReportableLow (analyte.getReportableLow());
        tr.setUnitType(analyte.getUnitType());
        tr.setResultStatus(resultStatus);

        return tr;
    }
    // todo: remove for release
    // this is a direct-proportional adaptive scale algorithm for
    // calculating result bar width
    // note: percentage analyte values may result in strangely looking (but mathematically correct) rendering
    public double getTheBarLength(int x0, int x1, TestResult inputData){
       double retval = -1d;
        Double val = inputData.getValue();
        if (val == null || val< inputData.getReportableLow() ){
            retval = 0d;
        } else if (val < inputData.getReferenceLow()){
            retval = ((val - inputData.getReportableLow())*x0)/(inputData.getReferenceLow() - inputData.getReportableLow());
        } else if (val == inputData.getReferenceLow()) {
            retval = (double)x0;
        } else if (val > inputData.getReferenceLow() && val < inputData.getReferenceHigh()){
            double correction = ((val - inputData.getReferenceLow())*(x1-x0))/(inputData.getReferenceHigh() - inputData.getReferenceLow());
            retval = (x0 + correction);
        } else if (val == inputData.getReportableHigh()){
            retval = (double)x1;
        } else if (val > inputData.getReferenceHigh() && val < inputData.getReportableHigh()){
            double correction = ((val - inputData.getReferenceHigh())*x0)/(inputData.getReportableHigh() - inputData.getReferenceHigh());
            retval = (x1 + correction);
        } else if (val >= inputData.getReportableHigh()){
            retval = (double)(x0 + x1);
        }

        return retval;
    }

    // TODO: remove for release - create TestRecords and save in db for testing
    public void instrumentMockTestRecordTH() {
        int max_no_test_record = 50;

        Realm realm = Realm.getDefaultInstance();

        // If half records are already in db, just return
        RealmResults<TestRecord> results = realm.where(TestRecord.class).equalTo("testModeValue",TestMode.BloodTest.value).findAll();
        if (results.size() >= Math.round(max_no_test_record*0.5)) {
            return;
        }

        Reader realmReader = realm.where(Reader.class).equalTo("serialNumber", "1111-2222-3333-4444").findFirst();
        if (realmReader == null) {
            Reader reader = new Reader();
            reader.setSerialNumber("1111-2222-3333-4444");
            reader.setBluetoothAddress("0000:1111:2222:3333:4444");
            new ReaderModel().saveReader(reader);
        }
        Reader realmReader2 = realm.where(Reader.class).equalTo("serialNumber", "aaaa-bbbb-cccc-dddd").findFirst();
        if (realmReader2 == null) {
            Reader reader = new Reader();
            reader.setSerialNumber("aaaa-bbbb-cccc-dddd");
            reader.setBluetoothAddress("0000:1111:2222:3333:4444");
            new ReaderModel().saveReader(reader);
        }

        // Create test records.
        for (int i = 0; i < max_no_test_record; i++) {
            String subjectId = "Mock Patient "+i;
            String id2 = String.valueOf(Math.random() * 100);

            TestRecord mockTestRecord = createUnmanagedTestRecord();

            // set CardLot
            mockTestRecord.getCardLot().setLotNumber("00-16299-0"+i);

            // set Reader
            if (i % 2 == 0) {
                Reader tmpReader = realm.where(Reader.class).equalTo("serialNumber", "1111-2222-3333-4444").findFirst();
                mockTestRecord.setReader(tmpReader);
            }
            // add epoctestfields
            mockTestRecord.setTestMode(TestMode.BloodTest);
            mockTestRecord.setType(TestType.Blood);
            mockTestRecord.setStatus(TestStatus.Success);
            mockTestRecord.setSubjectId(subjectId);
            mockTestRecord.setId2(id2);
            mockTestRecord.setPatientAge(41);
            mockTestRecord.setGender(Gender.Female);
            // Randomize the date from today to 25 days in the past. 0 - 25 days in the past.
            // after 25, start 0 - 25 days in the past.
            int pastDays = (i > 25) ? i - 25 : i;
            Date testDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(pastDays));
            mockTestRecord.setTestDateTime(testDate);
            mockTestRecord.setLastEqcDateTime(testDate); // Assume Eqc has run at the same time as TestRecord creation.
            mockTestRecord.getTestDetail().setSampleType(BloodSampleType.MixedVenous);
            mockTestRecord.getTestDetail().setPatientName(subjectId);
            mockTestRecord.getTestDetail().setPatientTemperature(38.2);
            mockTestRecord.getTestDetail().setPatientTemperatureType(Temperatures.C);
            mockTestRecord.getTestDetail().setHemodilutionApplied(false);
            mockTestRecord.getTestDetail().setCollectedBy("epoc_NP");
            mockTestRecord.getTestDetail().setComment("This is a mock blood test for software stabilization purposes.");
            mockTestRecord.getTestDetail().setPatientLocation("Ottawa");
            mockTestRecord.getRespiratoryDetail().setCustomMode("mock_baloon");
            mockTestRecord.getRespiratoryDetail().setDeliverySystem(DeliverySystem.BiPAP);
            mockTestRecord.getRespiratoryDetail().setDrawSite(DrawSites.LFinger);
            mockTestRecord.getRespiratoryDetail().setFlow("MockFlow");
            mockTestRecord.getRespiratoryDetail().setInspiratoryTime("Mock_3min");
            mockTestRecord.getRespiratoryDetail().setDeltaP("MockDelt_a_p");
            mockTestRecord.getRespiratoryDetail().setExpiratoryTime("Mock_ET");

            // Randomize state
            if (i % 2 == 0) {
                mockTestRecord.setSyncState(SyncState.Unsent);
            } else {
                mockTestRecord.setSyncState(SyncState.SentSuccessfully);
            }

            // Randomize Dates
            Date oneweekago = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7));
            mockTestRecord.setLastEqcDateTime(oneweekago);

            WorkflowRepository wfr = new WorkflowRepository();
            // NOTE: WorkflowRepository.getItemizedActiveWorkflow() encode the list of
            // workflow into String format.
            // (e.g)
            // If the value is a number, then it is TestInputField from wfi.getFieldList().
            // If the value is a string name, it is CustomTestInputField from wfi.getCustomFieldList()
            mockTestRecord.setWorkflowItems(wfr.getItemizedActiveWorkflow());
            Log.v("#######", "String workflowitems: "+mockTestRecord.getWorkflowItems());
            wfr.setWorkflowValuesIntoTestRecord(mockTestRecord);
            mockTestRecord.setUser(new UserModel().getAnonymousUser());
            new SelenaModel().setupTestEnablement(mockTestRecord);

            // get the range
            Range mixedVenousRange = new RangeModel().getUnmanagedRange(BloodSampleType.MixedVenous, "default");
            // Fake Test Results
            RealmList<TestResult> testResults = new RealmList<>();
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Na, 142.78654d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pCO2, 27.123456, ResultStatus.ReferenceLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pCO2T, 3.7123456, ResultStatus.CriticalLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pO2, 95.123456, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Ca, null, ResultStatus.CNC));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.K, 5.789d, ResultStatus.ReferenceHigh));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pH, 7.456234d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Chloride, null, ResultStatus.FailediQC));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Lactate, 0.276834, ResultStatus.BelowReportableRange));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Glucose, 87.123456, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Creatinine, 17.123456, ResultStatus.AboveReportableRange));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.BUN, 117.123456, ResultStatus.CriticalHigh));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.ArtAlvOxRatio, 1d, ResultStatus.CriticalLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.cArtAlvOxRatio, -1d, ResultStatus.CriticalLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.eGFR, 28d, ResultStatus.ReferenceLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.eGFRa, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.eGFRj, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.GFRckd, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.GFRckda, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.GFRswz, 90d, ResultStatus.Normal));

            // save into Realm
            createTestRecordFromUnmanaged(mockTestRecord);
            // add the list to the testrecord
            mockTestRecord.setTestResults(testResults);
            // save test results
            persistTestResults(mockTestRecord);
        }
    }

    public void instrumentMockTestRecordTHQA() {
        int max_no_test_record = 50;

        Realm realm = Realm.getDefaultInstance();

        // If half records are already in db, just return
        RealmResults<TestRecord> results = realm.where(TestRecord.class).equalTo("testModeValue",TestMode.QA.value).findAll();
        if (results.size() >= Math.round(max_no_test_record*0.5)) {
            return;
        }

        Reader realmReader = realm.where(Reader.class).equalTo("serialNumber", "1111-2222-3333-4444").findFirst();
        if (realmReader == null) {
            Reader reader = new Reader();
            reader.setSerialNumber("1111-2222-3333-4444");
            reader.setBluetoothAddress("0000:1111:2222:3333:4444");
            new ReaderModel().saveReader(reader);
        }
        Reader realmReader2 = realm.where(Reader.class).equalTo("serialNumber", "aaaa-bbbb-cccc-dddd").findFirst();
        if (realmReader2 == null) {
            Reader reader = new Reader();
            reader.setSerialNumber("aaaa-bbbb-cccc-dddd");
            reader.setBluetoothAddress("0000:1111:2222:3333:4444");
            new ReaderModel().saveReader(reader);
        }

        // Create test records.
        for (int i = 0; i < max_no_test_record; i++) {
            String subjectId = "CV1:183-1-B7"+i;
            String id2 = String.valueOf(Math.random() * 100);

            TestRecord mockTestRecord = createUnmanagedTestRecord();

            // set CardLot
            mockTestRecord.getCardLot().setLotNumber("00-16299-0"+i);

            // set Reader
            if (i % 2 == 0) {
                Reader tmpReader = realm.where(Reader.class).equalTo("serialNumber", "1111-2222-3333-4444").findFirst();
                mockTestRecord.setReader(tmpReader);
            }
            // add epoctestfields
            mockTestRecord.setTestMode(TestMode.QA);
            mockTestRecord.setType(TestType.Proficiency);
            mockTestRecord.setStatus(TestStatus.Success);
            mockTestRecord.setSubjectId(subjectId);
            mockTestRecord.setId2(id2);

            // Randomize the date from today to 25 days in the past. 0 - 25 days in the past.
            // after 25, start 0 - 25 days in the past.
            int pastDays = (i > 25) ? i - 25 : i;
            Date testDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(pastDays));
            mockTestRecord.setTestDateTime(testDate);
            mockTestRecord.setLastEqcDateTime(testDate); // Assume Eqc has run at the same time as TestRecord creation.
            mockTestRecord.getTestDetail().setSampleType(BloodSampleType.MixedVenous);
            mockTestRecord.getTestDetail().setComment("This is a mock qa test for software testing purposes.");

            if (i % 10 == 1) {
                mockTestRecord.getTestDetail().setComment("This is a mock qa test 1111 run by anonymous user etc.");
            }

            // Randomize state
            if (i % 2 == 0) {
                mockTestRecord.setSyncState(SyncState.Unsent);
            } else {
                mockTestRecord.setSyncState(SyncState.SentSuccessfully);
            }

            // Randomize Dates
            Date oneweekago = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7));
            mockTestRecord.setLastEqcDateTime(oneweekago);

            mockTestRecord.setUser(new UserModel().getAnonymousUser());
            new SelenaModel().setupTestEnablement(mockTestRecord);

            // get the range
            Range mixedVenousRange = new RangeModel().getUnmanagedRange(BloodSampleType.MixedVenous, "default");
            // Fake Test Results
            RealmList<TestResult> testResults = new RealmList<>();
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Na, 142.78654d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pCO2, 27.123456, ResultStatus.ReferenceLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pCO2T, 3.7123456, ResultStatus.CriticalLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pO2, 95.123456, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Ca, null, ResultStatus.CNC));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.K, 5.789d, ResultStatus.ReferenceHigh));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.pH, 7.456234d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Chloride, null, ResultStatus.FailediQC));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Lactate, 0.276834, ResultStatus.BelowReportableRange));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Glucose, 87.123456, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.Creatinine, 17.123456, ResultStatus.AboveReportableRange));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.BUN, 117.123456, ResultStatus.CriticalHigh));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.ArtAlvOxRatio, 1d, ResultStatus.CriticalLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.cArtAlvOxRatio, -1d, ResultStatus.CriticalLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.eGFR, 28d, ResultStatus.ReferenceLow));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.eGFRa, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.eGFRj, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.GFRckd, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.GFRckda, 90d, ResultStatus.Normal));
            testResults.add(createTestResult(mixedVenousRange, AnalyteName.GFRswz, 90d, ResultStatus.Normal));

            // save into Realm
            createTestRecordFromUnmanaged(mockTestRecord);
            // add the list to the testrecord
            mockTestRecord.setTestResults(testResults);
            // save test results
            persistTestResults(mockTestRecord);
        }
    }

}
