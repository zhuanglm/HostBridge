package com.epocal.testhistoryfeature;

import com.epocal.common.realmentities.EqcInfo;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.WorkFlow;
import com.epocal.datamanager.WorkflowRepository;
import com.epocal.testhistoryfeature.repository.THEqcInfoRepository;
import com.epocal.testhistoryfeature.repository.THPatientTestRepository;

import java.util.Arrays;
import java.util.Date;

import io.realm.Realm;

/**
 * This class implements Model for Details Screen.
 */
public class THDetailsModel implements THDetailsContract.Model {
    private Realm mRealm;
    private TestRecord mTestRecord;
    private THPatientTestRepository mPatientTestRepository;
    private THEqcInfoRepository mEqcInfoRepository;

    public THDetailsModel(Realm realm) {
        mRealm = realm;
        mPatientTestRepository = new THPatientTestRepository();
        mEqcInfoRepository = new THEqcInfoRepository();
    }

    @Override
    public TestRecord fetchTestRecord(long id) {
        // fetch from db only if we don't have a copy mTestRecord.
        if ((mTestRecord == null) || (mTestRecord.getId() != id)) {
            mTestRecord = mPatientTestRepository.fetch(mRealm, id);
        }

        return mTestRecord;
    }

    @Override
    public EqcInfo fetchEqc() {
        if (mTestRecord != null) {
            // TODO: EqcInfo fields are retrieved from the Reader and saved as the direct children of TestRecord.
            // TODO: and they not in Eqc table. (One in Eqc table is scheduled QC test.)
            // TODO: EqcInfo from the Reader is not implemented in 2018Nov release and below code
            // TODO: is just temporary.
           Date dateTime = mTestRecord.getLastEqcDateTime();
           return mEqcInfoRepository.fetchMostRecentBefore(dateTime);
        }
        return null;
    }

    @Override
    public WorkFlow fetchWorkFlow(String itemizedWorkflow) {
        return new WorkflowRepository().buildWorkFlowFromItemizedString(itemizedWorkflow);
    }

    @Override
    public void delete(Long id, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {
        mPatientTestRepository.delete(mRealm, Arrays.asList(id), onSuccess, onError);
    }

    @Override
    public void markSent(Long id, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {
        mPatientTestRepository.markSent(mRealm, Arrays.asList(id), onSuccess, onError);
    }

    @Override
    public void markUnsent(Long id, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {
        mPatientTestRepository.markUnsent(mRealm, Arrays.asList(id), onSuccess, onError);
    }
}
