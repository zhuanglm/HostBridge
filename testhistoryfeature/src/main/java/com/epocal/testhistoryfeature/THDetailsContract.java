package com.epocal.testhistoryfeature;

import com.epocal.common.realmentities.EqcInfo;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.WorkFlow;

import io.realm.Realm;

/**
 * MVP Contract for Test History Details screen.
 */
public interface THDetailsContract {
    interface Model {
        TestRecord fetchTestRecord(long id);
        WorkFlow fetchWorkFlow(String itemizedWorkflow);
        EqcInfo fetchEqc();

        // Actions
        void delete(Long id, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError);
        void markSent(Long id, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError);
        void markUnsent(Long id, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError);
    }
}
