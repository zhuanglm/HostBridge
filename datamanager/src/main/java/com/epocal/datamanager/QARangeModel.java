package com.epocal.datamanager;

import android.util.Log;

import com.epocal.common.realmentities.Analyte;
import com.epocal.common.realmentities.QARange;
import com.epocal.common.realmentities.QARangeValue;
import com.epocal.common.realmentities.Range;
import com.epocal.common.realmentities.RangeValue;
import com.epocal.common.types.RangeIgnoreInfo;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.Units;

import java.util.EnumSet;

import io.realm.QARangeValueRealmProxy;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by bmate on 6/22/2017.
 */

public class QARangeModel extends BaseRangeModel{
    /*
    * All rangevalues are kept in Realm at the value of default unit.
    *
    * We do convert from/to default unit each value according to the analyte's actual unit.
    * If a rangevalue is modified by user, at the current analyte's unit, we save in realm to default unit
    * @return void
    */
    private void convertValues (QARange range, boolean toDefaultUnit){
        for (int i = 0; i<range.getRangeValues().size();i++){
            Double conversionFactor = getConversionFactor(range.getRangeValues().get(i).getAnalyteName(), toDefaultUnit);
            if (conversionFactor !=null) {
                range.getRangeValues().get(i).setReferenceHigh(range.getRangeValues().get(i).getReferenceHigh()*conversionFactor);
                range.getRangeValues().get(i).setReferenceLow(range.getRangeValues().get(i).getReferenceLow()*conversionFactor);
            }
        }
    }
    private void convertValue (QARangeValue rangeValue, AnalyteName analyteName, boolean toDefaultUnit){

        Double conversionFactor = getConversionFactor(analyteName, toDefaultUnit);
        if (conversionFactor !=null) {
            rangeValue.setReferenceHigh(rangeValue.getReferenceHigh()*conversionFactor);
            rangeValue.setReferenceLow(rangeValue.getReferenceLow()*conversionFactor);
        }
    }
    private  Double getConversionFactor(AnalyteName analyteName, boolean toDefaultUnit){
        Double retval = null;
        Realm realm = Realm.getDefaultInstance();
        AnalyteModel analyteModel = new AnalyteModel();
        final Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte",analyteName.value).findFirst();
        Units primaryUnit = analyteModel.getPrimaryUnitType(managedAnalyte);
        if (managedAnalyte.getUnitType().compareTo(primaryUnit)!=0){

            if (toDefaultUnit){
                retval = analyteModel.getConversionFactor(managedAnalyte,managedAnalyte.getUnitType(),primaryUnit);
            }else{
                retval = analyteModel.getConversionFactor(managedAnalyte,primaryUnit,managedAnalyte.getUnitType());
            }

        }
        realm.close();
        return  retval;
    }

    /*
  * return a qarange object by name
  *
  * @return QARangeValue
  */
    public QARange getQARange( String qaRangeName, Realm realm){

        QARange range = realm.where(QARange.class)
                .equalTo("name",qaRangeName)
                .findFirst();
        convertValues(range,false);
        return range;

    }

     /*
     * updates an existing qarange object in bulk
     *
     * @return boolean
     */

    public boolean updateQARange (final QARange newValue){
        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        final QARange managedQARange = realm.where(QARange.class)
                .equalTo("name",newValue.getName())
                .findFirst();
        if (managedQARange!= null){
            try {
                realm.executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm) {
                        convertValues(newValue,true);
                        managedQARange.setCardTypes(newValue.getCardTypes());
                        managedQARange.setCustom(newValue.getCustom());
                        managedQARange.setName(newValue.getName());
                        managedQARange.setRangeValues(newValue.getRangeValues());
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

    /*
    * insert new QArange object in bulk
    * new ranges are always custom ranges
    * @return boolean
    */

    public boolean insertNewQARange (final QARange newValue){
        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        final QARange managedQARange = realm.where(QARange.class)
                .equalTo("name",newValue.getName())
                .findFirst();
        if (managedQARange!= null) {
            realm.close();
            return false;
        }else {
            try {
                realm.executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm) {
                        convertValues(newValue,true);
                        managedQARange.setCardTypes(newValue.getCardTypes());
                        managedQARange.setCustom(newValue.getCustom());
                        managedQARange.setName(newValue.getName());
                        managedQARange.setRangeValues(newValue.getRangeValues());
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
    /*
    * delete all QAranges from DB
    *
    * @return void
    */
    public void deleteAllQARanges(){
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<QARange> allQARanges = realm.where(QARange.class)
                .findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // Delete all matches
                allQARanges.deleteAllFromRealm();
            }
        });
        realm.close();
    }

    /*
    * remove an arbitrary realmlist of QAranges
    *
    * @return void
    */
    public void removeQARanges(final RealmList<QARange> ranges){
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ranges.deleteAllFromRealm();
            }
        });
        realm.close();
    }

     /*
     * delete one custom range from DB.
     *
     * @return void
     */

    public void Remove(final QARange range){

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                    range.deleteFromRealm();


            }
        });
        realm.close();
    }



    /*
     * restore factoryDefault Ranges
     *  there are 9 default ranges
     * @return void
     */
    public void restoreFactoryDefaultRanges (Realm realm){

        final RealmResults<QARange> existingRanges = realm.where(QARange.class).findAll();
        if (existingRanges !=null && existingRanges.size()>0){
            existingRanges.deleteAllFromRealm();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QARange factoryRange = realm.createObject(QARange.class);
                factoryRange.setName("default");
                factoryRange.setCustom(false);
                factoryRange.setRangeValues(createFactoryDefaultRangeValues(factoryRange, realm));
            }
        });
    }

    private RealmList<QARangeValue> createFactoryDefaultRangeValues(QARange parent, Realm realm){
        RealmList<QARangeValue> retval = new RealmList<>();
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.Na));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.K));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.pCO2));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.pO2));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.Ca));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.pH));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.Hct));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.pHT));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.pCO2T));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.pO2T));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.HCO3act));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.BEecf));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.BEb));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.O2SAT));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.cTCO2));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.cHgb));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.Glucose));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.Chloride));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.Lactate));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.Creatinine));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.AnionGap));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.AnionGapK));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.eGFR));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.eGFRa));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.eGFRj));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.GFRckd));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.GFRckda));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.GFRswz));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.AlveolarO2));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.ArtAlvOxDiff));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.ArtAlvOxRatio));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.cAlveolarO2));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.cArtAlvOxDiff));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.cArtAlvOxRatio));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.TCO2));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.Urea));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.UreaCreaRatio));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.BUN));
        retval.add(createFactoryDefaultRangeValue(realm, parent, AnalyteName.BUNCreaRatio));

        return retval;
    }

    private QARangeValue createFactoryDefaultRangeValue(Realm realm,QARange parent, AnalyteName analyteName ){
        QARangeValue retval = realm.createObject(QARangeValue.class);
        retval.setRange(parent);
        retval.setAnalyteName(analyteName);
        DefaultRangeValues rangeValues = defaultRanges.get(analyteName);
        if(rangeValues != null) {
            retval.setReferenceLow(rangeValues.referenceLow);
            retval.setReferenceHigh(rangeValues.referenceHigh);
        }
        return retval;
    }

    public QARange getUnmanagedRange(String rangeName){
        Realm realm = Realm.getDefaultInstance();
        QARange range = getQARange(rangeName,realm);
        QARange unmanagedRange = null;
        if(range!=null) {
            unmanagedRange = realm.copyFromRealm(range);
        }
        realm.close();
        return unmanagedRange;
    }

    public QARangeValue getUnmanagedRangeValue(QARange range, AnalyteName analyteName){
        QARangeValue retval = null;
        Realm realm = Realm.getDefaultInstance();
        QARange range1 = getQARange(range.getName(),realm);
        if (range1.getRangeValues()!=null && range1.getRangeValues().size()>0){
            for (QARangeValue rv: range1.getRangeValues()){
                if (rv.getAnalyteName().equals(analyteName)){
                    retval = realm.copyFromRealm(rv);
                    break;
                }
            }
        }
        realm.close();
        return retval;
    }


}
