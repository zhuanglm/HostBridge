package com.epocal.datamanager;

import android.util.Log;

import com.epocal.common.realmentities.QASampleInfo;
import com.epocal.common.types.QAFluidType;
import com.epocal.common.types.TestType;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;

import java.util.EnumSet;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class QASampleInfoModel {

    public List<QASampleInfo> getUnmanagedMatchingQASamples(String[] names) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<QASampleInfo> r = realm.where(QASampleInfo.class).in("name", names).findAll();
        return realm.copyFromRealm(r);
    }

    public QASampleInfo getManagedQASampleInfo(String qaSampleInfoName, Realm realm){

        QASampleInfo sample = realm.where(QASampleInfo.class)
                .equalTo("name",qaSampleInfoName)
                .findFirst();
        return sample;

    }

    public QASampleInfo getManagedQASampleInfo(QAFluidType type, Realm realm){

        QASampleInfo sample = realm.where(QASampleInfo.class)
                .equalTo("qaFluid",type.value)
                .findFirst();
        return sample;

    }

    public boolean updateQASampleInfo (final QASampleInfo newValue){
        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        final QASampleInfo managedQASampleInfo = realm.where(QASampleInfo.class)
                .equalTo("name",newValue.getName())
                .findFirst();
        if (managedQASampleInfo!= null){
            try {
                realm.executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm) {
                        managedQASampleInfo.setDescription(newValue.getDescription());
                        managedQASampleInfo.setCustom(newValue.getCustom());
                        managedQASampleInfo.setName(newValue.getName());
                        managedQASampleInfo.setTestType(newValue.getTestType());
                        managedQASampleInfo.setQaFluidType(newValue.getQaFluidType());
                        managedQASampleInfo.setQaRange(newValue.getQaRange());
                    }
                });
                retval = true;
            }
            catch (Exception e){
                retval = false;
                Log.e("Realm Error",e.getMessage() );
            }
            finally {
                realm.close();
            }
            return retval;
        }

        realm.close();
        return  retval;
    }

    public boolean insertNewQASampleInfo (final QASampleInfo newValue){
        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        final QASampleInfo managedQASampleInfo = realm.where(QASampleInfo.class)
                .equalTo("name",newValue.getName())
                .findFirst();
        if (managedQASampleInfo!= null) {
            realm.close();
            return false;
        }else {
            try {
                realm.executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm) {
                        managedQASampleInfo.setDescription(newValue.getDescription());
                        managedQASampleInfo.setCustom(newValue.getCustom());
                        managedQASampleInfo.setName(newValue.getName());
                        managedQASampleInfo.setTestType(newValue.getTestType());
                        managedQASampleInfo.setQaFluidType(newValue.getQaFluidType());
                        managedQASampleInfo.setQaRange(newValue.getQaRange());
                    }
                });
                retval = true;
            }
            catch (Exception e){
                retval = false;
                Log.e("Realm Error",e.getMessage() );
            }
            finally {
                realm.close();
            }

        }

        realm.close();
        return  retval;
    }

    public void deleteAllQASampleInfo(){
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<QASampleInfo> allQASampleInfo = realm.where(QASampleInfo.class)
                .findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // Delete all matches
                allQASampleInfo.deleteAllFromRealm();
            }
        });
        realm.close();
    }

    /*
     * delete one custom range from DB.
     *
     * @return void
     */

    public void Remove(final QASampleInfo sampleInfo){

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                sampleInfo.deleteFromRealm();


            }
        });
        realm.close();
    }



    public void restoreFactoryDefaultQASamples (Realm realm){

        final RealmResults<QASampleInfo> existingSampleInfo = realm.where(QASampleInfo.class).findAll();
        if (existingSampleInfo !=null && existingSampleInfo.size()>0){
            existingSampleInfo.deleteAllFromRealm();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                createSampleInfo(realm, "No QA sample", "None", QAFluidType.None);
                createSampleInfo(realm, "Unknown QA sample", "Unknown", QAFluidType.Unknown);
                createSampleInfo(realm, "Default QA sample", "Default", QAFluidType.Default);
                createSampleInfo(realm, "Metabolites Level 1", "L1:Default", QAFluidType.QCLevel1);
                createSampleInfo(realm, "Metabolites Level 2", "L2:Default", QAFluidType.QCLevel2);
                createSampleInfo(realm, "Metabolites Level 3", "L3:Default", QAFluidType.QCLevel3);
                createSampleInfo(realm, "Metabolites Level 4", "L4:Default", QAFluidType.QCLevel4);
                createSampleInfo(realm, "Metabolites Level 5", "L5:Default", QAFluidType.QCLevel5);
                createSampleInfo(realm, "Calibration Verification CV1", "CV1:Default", QAFluidType.CV1);
                createSampleInfo(realm, "Calibration Verification CV2", "CV2:Default", QAFluidType.CV2);
                createSampleInfo(realm, "Calibration Verification CV3", "CV3:Default", QAFluidType.CV3);
                createSampleInfo(realm, "Calibration Verification CV4", "CV4:Default", QAFluidType.CV4);
                createSampleInfo(realm, "Calibration Verification CV5", "CV5:Default", QAFluidType.CV5);
                createSampleInfo(realm, "HCT Control Level A", "HA:Default", QAFluidType.QCHCTA);
                createSampleInfo(realm, "HCT Control Level B", "HB:Default", QAFluidType.QCHCTB);
                createSampleInfo(realm, "HCT Control Level C", "HC:Default", QAFluidType.QCHCTC);
                createSampleInfo(realm, "HCT Verification Fluid H1", "H1:Default", QAFluidType.H1);
                createSampleInfo(realm, "HCT Verification Fluid H2", "H2:Default", QAFluidType.H2);
                createSampleInfo(realm, "HCT Verification Fluid H3", "H3:Default", QAFluidType.H3);
                createSampleInfo(realm, "HCT Verification Fluid H4", "H4:Default", QAFluidType.H4);
                createSampleInfo(realm, "HCT Verification Fluid H5", "H5:Default", QAFluidType.H5);
                createSampleInfo(realm, "Hypoxic QC", "HPB:Default", QAFluidType.QCHPB);
                createSampleInfo(realm, "Hyperbaric QC", "HPX:Default", QAFluidType.QCHPX);
            }
        });
    }

    private QASampleInfo createSampleInfo(Realm realm, String desc, String name, QAFluidType fluidType) {
        QASampleInfo factorySampleInfo = realm.createObject(QASampleInfo.class, PrimaryKeyFactory.getInstance().nextKey(QASampleInfo.class));
        factorySampleInfo.setName(name);
        factorySampleInfo.setDescription(desc);
        factorySampleInfo.setCustom(false);
        factorySampleInfo.setQaFluidType(fluidType);
        factorySampleInfo.setTestType(EnumSet.of(TestType.Blood, TestType.QualityControl, TestType.CalVer, TestType.EQC));
        factorySampleInfo.setQaRange(new QARangeModel().getQARange("default", realm));
        factorySampleInfo.setCardLotDescriptor(new EVADCardLotDescriptionModel().getManagedCLDR("*", realm));
        return factorySampleInfo;
    }

    public QASampleInfo getUnmanagedSampleInfo(String sampleName){
        Realm realm = Realm.getDefaultInstance();
        QASampleInfo sample = getManagedQASampleInfo(sampleName,realm);
        QASampleInfo unmanagedSample = null;
        if(sample!=null) {
            unmanagedSample = realm.copyFromRealm(sample);
        }
        realm.close();
        return unmanagedSample;
    }

    public QASampleInfo getUnmanagedSampleInfo(QAFluidType type){
        Realm realm = Realm.getDefaultInstance();
        QASampleInfo sample = getManagedQASampleInfo(type,realm);
        QASampleInfo unmanagedSample = null;
        if(sample!=null) {
            unmanagedSample = realm.copyFromRealm(sample);
        }
        realm.close();
        return unmanagedSample;
    }
}
