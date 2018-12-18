package com.epocal.testhistoryfeature;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.testhistoryfeature.repository.QATestRepository;
import com.epocal.testhistoryfeature.repository.THPatientTestRepository;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * This class implements Model for Main Screen.
 */
public class THModel implements THContract.Model {
    private Realm mRealm;
    private THPatientTestRepository mPatientTestRepository;
    private QATestRepository mQATestRepository;
    private boolean mIsQATest;

    public THModel(Realm realm) {
        mRealm = realm;
        mPatientTestRepository = new THPatientTestRepository();
        mQATestRepository = new QATestRepository();
        mIsQATest = false; // Initialize to PatientTest Mode
    }

    /**
     *  Set the type (testModeValue) of TestRecord used
     *  by Model. Two values are supported: Patient Test (Blood Test) or QA Test.
     *
     * @param isQATest - indicates TestRecord's testModeValue
     *        true => QA, false => Blood Test.
     */
    @Override
    public void setTestHistoryDataType(boolean isQATest) {
        this.mIsQATest = isQATest;
    }

    @Override
    public void delete(List<Long> idList, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError)
    {
        mPatientTestRepository.delete(mRealm, idList, onSuccess, onError);
    }

    @Override
    public void markSent(List<Long> idList, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError)
    {
        mPatientTestRepository.markSent(mRealm, idList, onSuccess, onError);
    }

    @Override
    public void markUnsent(List<Long> idList, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError)
    {
        mPatientTestRepository.markUnsent(mRealm, idList, onSuccess, onError);
    }

    @Override
    public RealmResults<TestRecord> fetchPatientTests() {
        if (mIsQATest) {
            return mQATestRepository.fetchAll(mRealm);
        } else {
            return mPatientTestRepository.fetchAll(mRealm);
        }
    }

    @Override
    public RealmResults<TestRecord> fetchMatchAny(String partialString) {
        if (mIsQATest) {
            return mQATestRepository.fetchMatchAny(mRealm, partialString);
        } else {
            return mPatientTestRepository.fetchMatchAny(mRealm, partialString);
        }
    }

    @Override
    public RealmResults<TestRecord> fetchBySyncState(boolean isSent) {
        if (mIsQATest) {
            return mQATestRepository.fetchBySyncState(mRealm, isSent);
        } else {
            return mPatientTestRepository.fetchBySyncState(mRealm, isSent);
        }
    }
}
