package com.epocal.datamanager;

import android.support.annotation.NonNull;

import com.epocal.common.realmentities.EqcInfo;
import com.epocal.common.realmentities.EqcValue;
import com.epocal.common.realmentities.Host;
import com.epocal.common.realmentities.Reader;
import com.epocal.common.realmentities.User;
import com.epocal.common.types.PressureType;
import com.epocal.common.types.am.Temperatures;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;

/**
 * Created by rzhuang on Aug 7 2018.
 */

public class EqcInfoModel {

    private Realm mRealm = null;

    public void openRealmInstance() {
        if (mRealm == null)
            mRealm = Realm.getDefaultInstance();
    }

    public void closeRealmInstance() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
    }

    public ArrayList<EqcInfo> getAllEqcInfos() {
        ArrayList<EqcInfo> allEqcInfos = new ArrayList<>();

        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        try {
            RealmResults<EqcInfo> allEqcInfosInRealm = realm.where(EqcInfo.class).findAll();
            allEqcInfos.addAll(allEqcInfosInRealm);
        } catch (Exception ex) {
            // todo handle exception
        }

        if (mRealm == null)
            realm.close();

        return allEqcInfos;
    }

    public EqcInfo findEqcInfoById(int id) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        EqcInfo eqcInfo = realm.where(EqcInfo.class)
                .equalTo("id", id)
                .findFirst();
        EqcInfo copied = null;
        if(eqcInfo != null) {
            copied = realm.copyFromRealm(eqcInfo);
            if (mRealm == null)
                realm.close();
        }
        return copied;
    }

    public void saveEqcInfo(final EqcInfo unmanagedEqcInfo) {
        if (unmanagedEqcInfo.getId() <= 0) {
            createNewEqcInfo(unmanagedEqcInfo);
        }
    }

    private void createNewEqcInfo(final EqcInfo unmanagedEqcInfo) {
        // here we got an unmanaged User with all fields validated
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        //create by administrator
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                EqcInfo eqcInfo = realm.createObject(EqcInfo.class, PrimaryKeyFactory.getInstance().nextKey(EqcInfo.class));
                if(eqcInfo != null) {
                    eqcInfo.setCreatedDate(unmanagedEqcInfo.getCreatedDate());
                    //eqcInfo.setReader(new ReaderModel().getReader(unmanagedEqcInfo.getReader().getBluetoothAddress()));
                    eqcInfo.setAmbientPressure(unmanagedEqcInfo.getAmbientPressure());
                    eqcInfo.setAmbientTemperature(unmanagedEqcInfo.getAmbientTemperature());
                    eqcInfo.setPressureType(unmanagedEqcInfo.getPressureType());
                    eqcInfo.setBatteryLevel(unmanagedEqcInfo.getBatteryLevel());
                    eqcInfo.setCreated(unmanagedEqcInfo.getCreated());
                    eqcInfo.setHasPassed(unmanagedEqcInfo.isHasPassed());
                    //eqcInfo.setHost(unmanagedEqcInfo.getHost());
                    eqcInfo.setReturnCode(unmanagedEqcInfo.getReturnCode());
                    //eqcInfo.setUser(unmanagedEqcInfo.getUser());
                    eqcInfo.setUserOther(unmanagedEqcInfo.getUserOther());
                    eqcInfo.setSelfTestResult(unmanagedEqcInfo.isSelfTestResult());

                    for (EqcValue tr: unmanagedEqcInfo.mValues) {
                        long id = PrimaryKeyFactory.getInstance().nextKey(EqcValue.class);
                        EqcValue eqcValue = realm.createObject(EqcValue.class,id);
                        eqcValue.setEqcInfoId(eqcInfo.getId());
                        tr.setId(id);
                        tr.setEqcInfoId(eqcInfo.getId());
                        eqcValue.updateFrom(tr);
                        eqcInfo.mValues.add(eqcValue);
                    }
                }
            }
        });
        if (mRealm == null)
            realm.close();
    }


    public void deleteEqcInfoById(final long id) {

        Realm realm = Realm.getDefaultInstance();
        final EqcInfo tr = realm.where(EqcInfo.class).equalTo("id",id).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                if (tr !=null){
                    if (tr.mValues!= null){
                        RealmResults<EqcValue> results = realm.where(EqcValue.class).equalTo("mEqcInfoId",tr.getId()).findAll();
                        results.deleteAllFromRealm();
                    }
                    tr.deleteFromRealm();
                }
            }
        });
        realm.close();
    }
}
