package com.epocal.datamanager;

import com.epocal.common.realmentities.EVAD;
import com.epocal.common.realmentities.EVADCardLotDescription;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class EVADCardLotDescriptionModel {
    public EVADCardLotDescription getManagedCLDR( String name, Realm realm){
        Realm realmInstance = realm;
        if (realmInstance == null)
            realmInstance = Realm.getDefaultInstance();
        EVADCardLotDescription cldr = realmInstance.where(EVADCardLotDescription.class)
                .equalTo("CardLotDescriptor", name)
                .findFirst();
        return cldr;
    }

    public EVADCardLotDescription getUnmanagedCLDR( String name, Realm realm){
        return realm.copyFromRealm(getManagedCLDR(name, null));
    }

    public void restoreFactoryDefaults (Realm realm){

        final RealmResults<EVADCardLotDescription> existingSampleInfo = realm.where(EVADCardLotDescription.class).findAll();
        if (existingSampleInfo !=null && existingSampleInfo.size()>0){
            existingSampleInfo.deleteAllFromRealm();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                EVADCardLotDescription factoryEVADCardLotDescription = realm.createObject(EVADCardLotDescription.class, PrimaryKeyFactory.getInstance().nextKey(EVADCardLotDescription.class));
                factoryEVADCardLotDescription.setCardLotDescriptor("*");
                factoryEVADCardLotDescription.setEvad(new EVADModel().getEVAD("Default", realm));
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    factoryEVADCardLotDescription.setCreated(format.parse("2016-01-01 00:00:00"));
                } catch(ParseException e) {
                    //TODO: log error
                }
            }
        });
    }
}
