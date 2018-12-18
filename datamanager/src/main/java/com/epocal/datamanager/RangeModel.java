package com.epocal.datamanager;

import android.util.Log;

import com.epocal.common.realmentities.Analyte;
import com.epocal.common.realmentities.Range;
import com.epocal.common.realmentities.RangeValue;
import com.epocal.common.types.AgeUnitType;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.RangeIgnoreInfo;
import com.epocal.common.types.RangeValueMetaInfo;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.Units;

import com.epocal.util.DateUtil;
import com.epocal.util.EnumSetUtil;

import java.util.EnumSet;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by bmate on 6/22/2017.
 */

public class RangeModel extends BaseRangeModel {
    /*
    * All rangevalues are kept in Realm at the value of default unit.
    *
    * We do convert from/to default unit each value according to the analyte's actual unit.
    * If a rangevalue is modified by user, at the current analyte's unit, we save in realm to default unit
    * @return void
    */
    private void convertValues (Range range, boolean toDefaultUnit){
       for (int i = 0; i<range.getRangeValues().size();i++){
           Double conversionFactor = getConversionFactor(range.getRangeValues().get(i).getAnalyteName(), toDefaultUnit);
            if (conversionFactor !=null) {
                range.getRangeValues().get(i).setCriticalHigh(range.getRangeValues().get(i).getCriticalHigh()*conversionFactor);
                range.getRangeValues().get(i).setCriticalLow(range.getRangeValues().get(i).getCriticalLow()*conversionFactor);
                range.getRangeValues().get(i).setReferenceHigh(range.getRangeValues().get(i).getReferenceHigh()*conversionFactor);
                range.getRangeValues().get(i).setReferenceLow(range.getRangeValues().get(i).getReferenceLow()*conversionFactor);
            }
       }
    }
    private void convertValue (RangeValue rangeValue,AnalyteName analyteName, boolean toDefaultUnit){

            Double conversionFactor = getConversionFactor(analyteName, toDefaultUnit);
            if (conversionFactor !=null) {
                rangeValue.setCriticalHigh(rangeValue.getCriticalHigh()*conversionFactor);
                rangeValue.setCriticalLow(rangeValue.getCriticalLow()*conversionFactor);
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
   * returns the default rangevalue object given a sample type, a range , an analyte
   * convert value to match analyte unit
   * @return RangeValue
   */
    public RangeValue getDefaultRangeValue(BloodSampleType sampleType, String rangeName, AnalyteName analyteName, Realm realm){

        RangeValue rangeValue = realm.where(RangeValue.class)
                        .equalTo("mRange.name",rangeName)
                        .equalTo("mRange.bloodSampleType", sampleType.value)
                        .equalTo("analyte",analyteName.value)
                        .equalTo("isDefaultRangeValue",true)
                        .findFirst();
        convertValue(rangeValue,analyteName,false);
        return rangeValue;

    }
    /*
    * returns a rangevalue object based on a query on dependencies like age, gender, fasting etc
    * if there are multiple dependencies, we either find the exact match, or return default
    * if age has nontrivial value, then we check that age unit is also non trivial (days, months or years)
    * convert value to match analyte unit
    * @return RangeValue
    */
    public RangeValue getRangeValue(BloodSampleType sampleType, String rangeName, AnalyteName analyteName, Integer age, AgeUnitType ageUnitType, Boolean fasting, Gender gender, Realm realm){
        // no dependency

        RangeValue retval = realm.where(RangeValue.class)
                .equalTo("mRange.name",rangeName)
                .equalTo("mRange.bloodSampleType", sampleType.value)
                .equalTo("analyte",analyteName.value)
                .equalTo("isDefaultRangeValue",true)
                 .findFirst();

        // age
        if (age > 0 && (fasting ==null || !fasting) && gender.compareTo(Gender.Unknown)==0 && ageUnitType.compareTo(AgeUnitType.UNKNOWN)!=0){

         RealmResults<RangeValue> allAgeDependentRangeValues = realm.where(RangeValue.class)
                 .equalTo("mRange.name",rangeName)
                 .equalTo("mRange.bloodSampleType", sampleType.value)
                 .equalTo("analyte",analyteName.value)
                 .equalTo("isDefaultRangeValue",false)
                 .equalTo("dependencyInfoValue", EnumSetUtil.encode(EnumSet.of(RangeValueMetaInfo.DEPENDS_ON_AGE)))
                 .equalTo("ageUnitValue",ageUnitType.value)
                 .findAll();
            if (allAgeDependentRangeValues.size()>0) {
                int patientAgeInDays;

                switch (ageUnitType){
                    case DAYS:
                        for (RangeValue rval : allAgeDependentRangeValues) {
                            if (rval.getAgeLowLimit()<= age && rval.getAgeHighLimit()> age){
                                retval = rval;
                                break;
                            }
                        }
                        break;
                    case MONTHS:
                        patientAgeInDays = DateUtil.daysFromMonthsToNow(age);
                        for (RangeValue rval : allAgeDependentRangeValues) {
                            if (DateUtil.daysFromMonthsToNow(rval.getAgeLowLimit())<= patientAgeInDays && DateUtil.daysFromMonthsToNow(rval.getAgeHighLimit())> patientAgeInDays){
                                retval = rval;
                                break;
                            }
                        }
                        break;
                    case YEARS:
                        patientAgeInDays = DateUtil.daysFromYearsToNow(age);
                        for (RangeValue rval : allAgeDependentRangeValues) {
                            if (DateUtil.daysFromYearsToNow(rval.getAgeLowLimit())<= patientAgeInDays && DateUtil.daysFromYearsToNow(rval.getAgeHighLimit())> patientAgeInDays){
                                retval = rval;
                                break;
                            }
                        }
                        break;

                }

            }

        }
        // gender
        else if ((age==null || age == 0)&& (fasting ==null || !fasting) && gender.compareTo(Gender.Unknown)!=0 ){
                RangeValue   genderDependantRangeValue = realm.where(RangeValue.class)
                    .equalTo("mRange.name",rangeName)
                    .equalTo("mRange.bloodSampleType", sampleType.value)
                    .equalTo("analyte",analyteName.value)
                    .equalTo("isDefaultRangeValue",false)
                    .equalTo("dependencyInfoValue", EnumSetUtil.encode(EnumSet.of(RangeValueMetaInfo.DEPENDS_ON_GENDER)))
                    .equalTo("genderValue",gender.value)
                    .findFirst();
                if (genderDependantRangeValue!=null){
                    retval = genderDependantRangeValue;
                }
        }
        // fasting
        else if ((age==null || age == 0)  && (fasting ) && gender.compareTo(Gender.Unknown)==0 ){
            RangeValue  fastingDependantRangeValue = realm.where(RangeValue.class)
                    .equalTo("mRange.name",rangeName)
                    .equalTo("mRange.bloodSampleType", sampleType.value)
                    .equalTo("analyte",analyteName.value)
                    .equalTo("isDefaultRangeValue",false)
                    .equalTo("dependencyInfoValue", EnumSetUtil.encode(EnumSet.of(RangeValueMetaInfo.DEPENDS_ON_FASTING)))
                    .findFirst();
            if (fastingDependantRangeValue!=null){
                retval = fastingDependantRangeValue;
            }
        }
        // age and gender
        else if (age > 0 && (fasting ==null || !fasting) && gender.compareTo(Gender.Unknown)!=0 && ageUnitType.compareTo(AgeUnitType.UNKNOWN)!=0){
            RealmResults<RangeValue> allAgeAndGenderDependentRangeValues = realm.where(RangeValue.class)
                    .equalTo("mRange.name",rangeName)
                    .equalTo("mRange.bloodSampleType", sampleType.value)
                    .equalTo("analyte",analyteName.value)
                    .equalTo("isDefaultRangeValue",false)
                    .equalTo("dependencyInfoValue", EnumSetUtil.encode(EnumSet.of(RangeValueMetaInfo.DEPENDS_ON_AGE,RangeValueMetaInfo.DEPENDS_ON_GENDER)))
                    .equalTo("ageUnitValue",ageUnitType.value)
                    .findAll();
            if (allAgeAndGenderDependentRangeValues.size()>0) {
                int patientAgeInDays;

                switch (ageUnitType){
                    case DAYS:
                        for (RangeValue rval : allAgeAndGenderDependentRangeValues) {
                            if (rval.getAgeLowLimit()<= age && rval.getAgeHighLimit()> age){
                                retval = rval;
                                break;
                            }
                        }
                        break;
                    case MONTHS:
                        patientAgeInDays = DateUtil.daysFromMonthsToNow(age);
                        for (RangeValue rval : allAgeAndGenderDependentRangeValues) {
                            if (DateUtil.daysFromMonthsToNow(rval.getAgeLowLimit())<= patientAgeInDays && DateUtil.daysFromMonthsToNow(rval.getAgeHighLimit())> patientAgeInDays){
                                retval = rval;
                                break;
                            }
                        }
                        break;
                    case YEARS:
                        patientAgeInDays = DateUtil.daysFromYearsToNow(age);
                        for (RangeValue rval : allAgeAndGenderDependentRangeValues) {
                            if (DateUtil.daysFromYearsToNow(rval.getAgeLowLimit())<= patientAgeInDays && DateUtil.daysFromYearsToNow(rval.getAgeHighLimit())> patientAgeInDays){
                                retval = rval;
                                break;
                            }
                        }
                        break;

                }

            }

        }
        // gender and fasting
        else if ((age==null || age == 0)&& (fasting) && gender.compareTo(Gender.Unknown)!=0 ){
            RangeValue   genderAndFastingDependantRangeValue = realm.where(RangeValue.class)
                    .equalTo("mRange.name",rangeName)
                    .equalTo("mRange.bloodSampleType", sampleType.value)
                    .equalTo("analyte",analyteName.value)
                    .equalTo("isDefaultRangeValue",false)
                    .equalTo("dependencyInfoValue", EnumSetUtil.encode(EnumSet.of(RangeValueMetaInfo.DEPENDS_ON_GENDER, RangeValueMetaInfo.DEPENDS_ON_FASTING)))
                    .equalTo("genderValue",gender.value)
                    .findFirst();
            if (genderAndFastingDependantRangeValue!=null){
                retval = genderAndFastingDependantRangeValue;
            }
        }
        // age and fasting
        else if ((age > 0)&& (fasting) && gender.compareTo(Gender.Unknown)==0 && ageUnitType.compareTo(AgeUnitType.UNKNOWN)!=0){
            RealmResults<RangeValue> allAgeAndFastingDependentRangeValues = realm.where(RangeValue.class)
                    .equalTo("mRange.name",rangeName)
                    .equalTo("mRange.bloodSampleType", sampleType.value)
                    .equalTo("analyte",analyteName.value)
                    .equalTo("isDefaultRangeValue",false)
                    .equalTo("dependencyInfoValue", EnumSetUtil.encode(EnumSet.of(RangeValueMetaInfo.DEPENDS_ON_AGE, RangeValueMetaInfo.DEPENDS_ON_FASTING)))
                    .equalTo("ageUnitValue",ageUnitType.value)
                    .findAll();
            if (allAgeAndFastingDependentRangeValues.size()>0) {
                int patientAgeInDays;

                switch (ageUnitType){
                    case DAYS:
                        for (RangeValue rval : allAgeAndFastingDependentRangeValues) {
                            if (rval.getAgeLowLimit()<= age && rval.getAgeHighLimit()> age){
                                retval = rval;
                                break;
                            }
                        }
                        break;
                    case MONTHS:
                        patientAgeInDays = DateUtil.daysFromMonthsToNow(age);
                        for (RangeValue rval : allAgeAndFastingDependentRangeValues) {
                            if (DateUtil.daysFromMonthsToNow(rval.getAgeLowLimit())<= patientAgeInDays && DateUtil.daysFromMonthsToNow(rval.getAgeHighLimit())> patientAgeInDays){
                                retval = rval;
                                break;
                            }
                        }
                        break;
                    case YEARS:
                        patientAgeInDays = DateUtil.daysFromYearsToNow(age);
                        for (RangeValue rval : allAgeAndFastingDependentRangeValues) {
                            if (DateUtil.daysFromYearsToNow(rval.getAgeLowLimit())<= patientAgeInDays && DateUtil.daysFromYearsToNow(rval.getAgeHighLimit())> patientAgeInDays){
                                retval = rval;
                                break;
                            }
                        }
                        break;

                }

            }

        }
        // age, gender and fasting
        else if (age > 0 && (fasting) && gender.compareTo(Gender.Unknown)!=0 && ageUnitType.compareTo(AgeUnitType.UNKNOWN)!=0){
            RealmResults<RangeValue> allDependentRangeValues = realm.where(RangeValue.class)
                    .equalTo("mRange.name",rangeName)
                    .equalTo("mRange.bloodSampleType", sampleType.value)
                    .equalTo("analyte",analyteName.value)
                    .equalTo("isDefaultRangeValue",false)
                    .equalTo("dependencyInfoValue", EnumSetUtil.encode(EnumSet.of(RangeValueMetaInfo.DEPENDS_ON_AGE, RangeValueMetaInfo.DEPENDS_ON_GENDER, RangeValueMetaInfo.DEPENDS_ON_FASTING)))
                    .equalTo("ageUnitValue",ageUnitType.value)
                    .equalTo("genderValue",gender.value)
                    .findAll();
            if (allDependentRangeValues.size()>0) {
                int patientAgeInDays;

                switch (ageUnitType){
                    case DAYS:
                        for (RangeValue rval : allDependentRangeValues) {
                            if (rval.getAgeLowLimit()<= age && rval.getAgeHighLimit()> age){
                                retval = rval;
                                break;
                            }
                        }
                        break;
                    case MONTHS:
                        patientAgeInDays = DateUtil.daysFromMonthsToNow(age);
                        for (RangeValue rval : allDependentRangeValues) {
                            if (DateUtil.daysFromMonthsToNow(rval.getAgeLowLimit())<= patientAgeInDays && DateUtil.daysFromMonthsToNow(rval.getAgeHighLimit())> patientAgeInDays){
                                retval = rval;
                                break;
                            }
                        }
                        break;
                    case YEARS:
                        patientAgeInDays = DateUtil.daysFromYearsToNow(age);
                        for (RangeValue rval : allDependentRangeValues) {
                            if (DateUtil.daysFromYearsToNow(rval.getAgeLowLimit())<= patientAgeInDays && DateUtil.daysFromYearsToNow(rval.getAgeHighLimit())> patientAgeInDays){
                                retval = rval;
                                break;
                            }
                        }
                        break;

                }

            }

        }

        convertValue(retval,analyteName,false);
        return retval;

    }
    /*
     * return a range object given a sample type and the range name
     *
     * @return RangeValue
     */
    public Range getRange(BloodSampleType sampleType, String rangeName, Realm realm){

        Range range = realm.where(Range.class)
                .equalTo("name",rangeName)
                .equalTo("bloodSampleType", sampleType.value)
                .findFirst();
        convertValues(range,false);
        return range;

    }

     /*
     * updates an existing range object in bulk
     *
     * @return boolean
     */

     public boolean updateRange (final Range newValue){
         boolean retval = false;
         Realm realm = Realm.getDefaultInstance();
         final Range managedRange = realm.where(Range.class)
                 .equalTo("name",newValue.getName())
                 .equalTo("bloodSampleType",newValue.getSampleType().value)
                 .findFirst();
         if (managedRange!= null){
             try {
                 realm.executeTransaction(new Realm.Transaction(){
                     @Override
                     public void execute(Realm realm) {
                  convertValues(newValue,true);
                         managedRange.setCardTypes(newValue.getCardTypes());
                         managedRange.setCustom(newValue.getCustom());
                         managedRange.setName(newValue.getName());
                         managedRange.setSampleType(newValue.getSampleType());
                         managedRange.setRangeValues(newValue.getRangeValues());
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
     * insert new range object in bulk
     * new ranges are always custom ranges
     * @return boolean
     */

     public boolean insertNewRange (final Range newValue){
         boolean retval = false;
         Realm realm = Realm.getDefaultInstance();
         final Range managedRange = realm.where(Range.class)
                 .equalTo("name",newValue.getName())
                 .equalTo("bloodSampleType",newValue.getSampleType().value)
                 .findFirst();
         if (managedRange!= null) {
             realm.close();
             return false;
         }else {
             try {
                 realm.executeTransaction(new Realm.Transaction(){
                     @Override
                     public void execute(Realm realm) {
                         convertValues(newValue,true);
                         managedRange.setCardTypes(newValue.getCardTypes());
                         managedRange.setCustom(newValue.getCustom());
                         managedRange.setName(newValue.getName());
                         managedRange.setSampleType(newValue.getSampleType());
                         managedRange.setRangeValues(newValue.getRangeValues());
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
     * delete all custom ranges from DB
     *
     * @return void
     */
     public void deleteAllCustomRanges(){
         Realm realm = Realm.getDefaultInstance();
         final RealmResults<Range> allCustomRanges = realm.where(Range.class)
                 .equalTo("isCustom",false)
                 .findAll();
         realm.executeTransaction(new Realm.Transaction() {
             @Override
             public void execute(Realm realm) {

                 // Delete all matches
                 allCustomRanges.deleteAllFromRealm();
             }
         });
         realm.close();
     }

     /*
     * out of an arbitrary realmlist of ranges, remove from DB only the custom ones.
     *
     * @return void
     */
     public void removeCustomRanges(RealmList<Range> ranges){
         Realm realm = Realm.getDefaultInstance();
         final RealmResults<Range> allCustom = ranges.where().equalTo("isCustom",false).findAll();

         realm.executeTransaction(new Realm.Transaction() {
             @Override
             public void execute(Realm realm) {

                 // Delete all matches
                 allCustom.deleteAllFromRealm();
             }
         });
         realm.close();
     }

     /*
     * delete one custom range from DB.
     *
     * @return void
     */

     public void Remove(final Range range){

         Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
             @Override
             public void execute(Realm realm) {

                 // Delete if custom
                 if (range.getCustom()){
                     range.deleteFromRealm();
                 }

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

        final RealmResults<Range> existingRanges = realm.where(Range.class).findAll();
        final EnumSet<BloodSampleType> factorySampleTypes = EnumSet.of
                        (BloodSampleType.Unspecified,
                                BloodSampleType.Unknown,
                                BloodSampleType.Arterial,
                                BloodSampleType.Venous,
                                BloodSampleType.MixedVenous,
                                BloodSampleType.Capillary,
                                BloodSampleType.Cord,
                                BloodSampleType.CordArterial,
                                BloodSampleType.CordVenous
                );
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                if (existingRanges !=null && existingRanges.size()>0){
                    existingRanges.deleteAllFromRealm();
                }
                for (BloodSampleType sampleType:factorySampleTypes) {

                    Range factoryRange = realm.createObject(Range.class);
                    factoryRange.setName("default");
                    factoryRange.setSampleType(sampleType);
                    factoryRange.setCustom(false);
                    factoryRange.setRangeValues(createFactoryDefaultRangeValues(factoryRange,realm));
                }

            }
        });

    }
    private RealmList<RangeValue> createFactoryDefaultRangeValues(Range parent, Realm realm){
        RealmList<RangeValue> retval = new RealmList<RangeValue>();
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
    private RangeValue createFactoryDefaultRangeValue(Realm realm,Range parent, AnalyteName analyteName ){
        RangeValue retval = realm.createObject(RangeValue.class);
        retval.setRange(parent);
        retval.setIgnoreInfo(EnumSet.of(RangeIgnoreInfo.IgnoreNone));
        retval.setDefaultRangeValue(true);
        DefaultRangeValues rangeValues = defaultRanges.get(analyteName);
        if(rangeValues != null) {
            retval.setAnalyteName(analyteName);
            retval.setReferenceLow(rangeValues.referenceLow);
            retval.setReferenceHigh(rangeValues.referenceHigh);
            retval.setCriticalLow(rangeValues.criticalLow);
            retval.setCriticalHigh(rangeValues.criticalHigh);
        }
        return retval;
    }
    public Range getUnmanagedRange(BloodSampleType sampleType, String rangeName){
        Realm realm = Realm.getDefaultInstance();
        Range range = getRange(sampleType,rangeName,realm);
        Range unmanagedRange = null;
        if(range!=null) {
         unmanagedRange = realm.copyFromRealm(range);
        }
        realm.close();
        return unmanagedRange;
    }
    public RangeValue getUnmanagedRangeValue(Range range, AnalyteName analyteName){
        RangeValue retval = null;
        Realm realm = Realm.getDefaultInstance();
        Range range1 = getRange(range.getSampleType(),range.getName(),realm);
        if (range1.getRangeValues()!=null && range1.getRangeValues().size()>0){
            for (RangeValue rv: range1.getRangeValues()){
                if (rv.getAnalyteName().equals(analyteName)){
                    retval = realm.copyFromRealm(rv);
                    break;
                }
            }
        }
        realm.close();
        return retval;
    }
    public BloodSampleType getSampleType(Range range){
        BloodSampleType retval = BloodSampleType.ENUM_UNINITIALIZED;
        Realm realm = Realm.getDefaultInstance();
        Range range1 = realm.copyFromRealm(getRange(range.getSampleType(),range.getName(),realm));
        if (range1!=null ){
            retval = range1.getSampleType();
        }
        realm.close();
        return retval;
    }
}





















