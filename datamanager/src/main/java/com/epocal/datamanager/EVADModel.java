package com.epocal.datamanager;

import com.epocal.common.realmentities.EVAD;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class EVADModel {
    public EVAD getEVAD( String evadName, Realm realm){

        EVAD range = realm.where(EVAD.class)
                .equalTo("type",evadName)
                .findFirst();
        return range;

    }

    public void restoreFactoryDefaults(Realm realm){

        final RealmResults<EVAD> existingSampleInfo = realm.where(EVAD.class).findAll();
        if (existingSampleInfo !=null && existingSampleInfo.size()>0){
            existingSampleInfo.deleteAllFromRealm();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                EVAD factoryEVAD = realm.createObject(EVAD.class, PrimaryKeyFactory.getInstance().nextKey(EVAD.class));
                factoryEVAD.setQaSamples(0);
                factoryEVAD.setSensorConfig(0.0);
                factoryEVAD.setType("Default");
                factoryEVAD.setVersion("0.0");
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    factoryEVAD.setCreated(format.parse("2016-01-01 00:00:00"));
                    factoryEVAD.setExpiry(format.parse("2100-12-30 00:00:00"));
                } catch(ParseException e) {
                    //TODO: log error
                }
            }
        });
    }
}
