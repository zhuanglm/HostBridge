package com.epocal.testhistoryfeature.repository;

import com.epocal.common.realmentities.EqcInfo;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;

/**
 * This class is a helper class to interface with Realm Model class
 * to perform db query and update for Eqc object.
 */
public class THEqcInfoRepository {

    public EqcInfo fetchMostRecentBefore(Date dateTime) {
        Realm realm = Realm.getDefaultInstance();
        final EqcInfo info = realm.where(EqcInfo.class).lessThanOrEqualTo ("mCreated", dateTime).findFirst();
        return info;
    }

    public EqcInfo createMockEqcInfo() {
        Realm realm = Realm.getDefaultInstance();
        EqcInfo info = realm.where(EqcInfo.class).findFirst();
        if (info !=null){
            EqcInfo retval = realm.copyFromRealm(info);
            return retval;
        } else {
            info = new EqcInfo();
            info.setHasPassed(true);
            Date testDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365));
            info.setCreatedDate(testDate);
            info.setAmbientTemperature(37.0);
            info.setAmbientPressure(99);
            info.setBatteryLevel(95);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(info);
            realm.commitTransaction();
            realm.close();
        }
        return info;
    }
}
