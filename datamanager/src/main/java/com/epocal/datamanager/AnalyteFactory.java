package com.epocal.datamanager;

import com.epocal.common.realmentities.Analyte;
import com.epocal.common.realmentities.AnalyteUnitInfo;
import com.epocal.common.realmentities.UnitConversionFactor;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.AnalyteType;
import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.common.types.am.Units;

import java.util.EnumSet;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * This class creates / resets all supported analytes into Realm
 * to factory default values
 */

public class AnalyteFactory {
    /*
    *
    * create factory default analytes
    *
    * @return void
    * */
    public void createFactoryDefault(Realm realm) {
        final EnumSet<AnalyteName> allAnalytes = EnumSet.allOf(AnalyteName.class);
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                for (AnalyteName aName: allAnalytes ) {
                    if (    aName.compareTo(AnalyteName.Unknown)==0 ||
                            aName.compareTo(AnalyteName.HCO3Std)==0 ||
                            aName.compareTo(AnalyteName.CaIon)==0 ||
                            aName.compareTo(AnalyteName.OxygenConcentration)==0 ||
                            aName.compareTo(AnalyteName.UncorrectedHematocrit)==0 ||
                            aName.compareTo(AnalyteName.Creatine)==0 ||
                            aName.compareTo(AnalyteName.cO2SAT)==0 ||
                            aName.compareTo(AnalyteName.ENUM_UNINITIALIZED)==0
                            ){
                        continue;
                    }
                    createAnalyte(realm, aName);
                }


            }
        });
    }
    public void resetToFactoryDefault(Realm realm){
        final EnumSet<AnalyteName> allAnalytes = EnumSet.allOf(AnalyteName.class);
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                for (AnalyteName aName: allAnalytes ) {
                    Analyte analyte = realm.where(Analyte.class).equalTo("analyte",aName.value).findFirst();
                    setAnalyteToFactoryDefault(analyte);
                }


            }
        });

    }

    private void createAnalyte(Realm realm, AnalyteName analyteName){
       Analyte analyte =  realm.createObject(Analyte.class);
        analyte.setAnalyteName(analyteName);
        setAnalyteToFactoryDefault(analyte);
        // this switch is executed only if realm is empty
        switch (analyteName) {
            case Unknown:
                break;

            case Na:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Na, Units.mmolL, AnalyteType.Measured, 85d, 180d,"#0",true ));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Na, Units.mEqL, AnalyteType.Measured, 85d, 180d,"#0",false ));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Na, Units.mmolL, 1d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Na, Units.mEqL, 1d, Units.mmolL));
                break;
            case K:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.K, Units.mmolL, AnalyteType.Measured, 1.5d, 12d,"#0.0",true ));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.K, Units.mEqL, AnalyteType.Measured, 1.5d, 12d,"#0.0",false ));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.K, Units.mmolL, 1d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.K, Units.mEqL, 1d, Units.mmolL));
                break;
            case pCO2:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.pCO2, Units.mmhg, AnalyteType.Measured, 5d,250d,"#0.0",true ));
                analyte.getMetaInfo().add(  createAnalyteUnitInfo(realm, AnalyteName.pCO2, Units.kPa, AnalyteType.Measured, 0.7d,33.3d,"#0.00",false ));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.pCO2, Units.mmhg, 0.1333d, Units.kPa));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.pCO2, Units.kPa, 7.501875469d, Units.mmhg));
                break;
            case pO2:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.pO2, Units.mmhg, AnalyteType.Measured, 5d,750d,"#0.0",true ));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.pO2, Units.kPa, AnalyteType.Measured, 0.7d, 100d,"#0.00",false ));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.pO2, Units.mmhg, 0.1333d, Units.kPa));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.pO2, Units.kPa, 7.501875469d, Units.mmhg));
                break;
            case Ca:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Ca, Units.mmolL, AnalyteType.Measured, 0.25d, 4d,"#0.00",true ));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Ca, Units.mgdl, AnalyteType.Measured, 1d, 16d,"#0.0",false ));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Ca, Units.mEqL, AnalyteType.Measured, 0.5d, 8d,"#0.0",false ));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Ca, Units.mmolL, 4d, Units.mgdl));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Ca, Units.mmolL, 2d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Ca, Units.mgdl,0.25d, Units.mmolL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Ca, Units.mgdl, 0.5d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Ca, Units.mEqL, 0.5d, Units.mmolL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Ca, Units.mEqL, 2d, Units.mgdl));
                break;
            case pH:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.pH, Units.pH, AnalyteType.Measured, 6.5d,8d,"#0.000",true ));
                break;
            case Hct:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Hct, Units.Percent, AnalyteType.Measured, 10d, 75d,"#0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Hct, Units.LL, AnalyteType.Measured, 0.1d, 0.75d,"#0.00",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Hct, Units.Percent, 0.01d, Units.LL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Hct, Units.LL, 0.01d, Units.Percent));
                break;
            case pHT:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.pHT, Units.pH, AnalyteType.Corrected, 6.5d, 8d,"#0.000",true));
                break;
            case pCO2T:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.pCO2T, Units.mmhg, AnalyteType.Corrected, 5d, 250d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.pCO2T, Units.kPa, AnalyteType.Corrected, 0.7d, 33.3d,"#0.00",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.pCO2T, Units.mmhg, 0.1333d, Units.kPa));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.pCO2T, Units.kPa, 7.501875469d, Units.mmhg));
                break;
            case pO2T:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.pO2T, Units.mmhg, AnalyteType.Corrected, 5d, 750d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.pO2T, Units.kPa, AnalyteType.Corrected, 0.7d, 100d,"#0.00",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.pO2T, Units.mmhg, 0.1333d, Units.kPa));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.pO2T, Units.kPa, 7.501875469d, Units.mmhg));
                break;
            case HCO3act:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.HCO3act, Units.mmolL, AnalyteType.Calculated, 1d, 85d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.HCO3act, Units.mEqL, AnalyteType.Calculated, 1d, 85d,"#0.0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.HCO3act, Units.mmolL, 1d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.HCO3act, Units.mEqL, 1d, Units.mmolL));
                break;
            case BEecf:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.BEecf, Units.mmolL, AnalyteType.Calculated, -30d, 30d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.BEecf, Units.mEqL, AnalyteType.Calculated,-30d, 30d,"#0.0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.BEecf, Units.mmolL, 1d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.BEecf, Units.mEqL, 1d, Units.mmolL));
                break;
            case BEb:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.BEb, Units.mmolL, AnalyteType.Calculated, -30d, 30d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.BEb, Units.mEqL, AnalyteType.Calculated,-30d, 30d,"#0.0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.BEb, Units.mmolL, 1d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.BEb, Units.mEqL, 1d, Units.mmolL));
                break;
            case O2SAT:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.O2SAT, Units.Percent, AnalyteType.Calculated, 0d, 100d,"#0.0",true));
                break;
            case cTCO2:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cTCO2, Units.mmolL, AnalyteType.Calculated, 5d, 50d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cTCO2, Units.mEqL, AnalyteType.Calculated, 5d, 50d,"#0.0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cTCO2, Units.mmolL, 1d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cTCO2, Units.mEqL, 1d, Units.mmolL));
                break;
            case cHgb:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cHgb, Units.gldl, AnalyteType.Calculated, 3.3d, 25d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cHgb, Units.mmolL, AnalyteType.Calculated, 2d, 15.5d,"#0.0",false));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cHgb, Units.gL, AnalyteType.Calculated, 33d, 205d,"#0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cHgb, Units.gldl, 10d, Units.gL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cHgb, Units.gldl, 0.6206d, Units.mmolL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cHgb, Units.gL, 0.1d, Units.gldl));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cHgb, Units.gL, 0.06206d, Units.mmolL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cHgb, Units.mmolL, 1.611343861d, Units.gldl));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cHgb, Units.mmolL, 16.113438608d, Units.gL));
                break;
            case Glucose:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Glucose, Units.mgdl, AnalyteType.Measured, 20d, 700d,"#0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Glucose, Units.mmolL, AnalyteType.Measured, 1.1d, 38.5d,"#0.0",false));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Glucose, Units.gL, AnalyteType.Measured, 0.2d, 7d,"#0.00",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Glucose, Units.mgdl, 0.055005501d, Units.mmolL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Glucose, Units.mgdl, 0.01d, Units.gL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Glucose, Units.mmolL, 18.18d, Units.mgdl));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Glucose, Units.mmolL, 0.1818d, Units.gL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Glucose, Units.gL, 100d, Units.mgdl));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Glucose, Units.gL, 5.500550055d, Units.mmolL));
                break;
            case Chloride:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Chloride, Units.mmolL, AnalyteType.Measured, 65d, 140d,"#0",true ));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Chloride, Units.mEqL, AnalyteType.Measured, 65d, 140d,"#0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Chloride, Units.mmolL, 1d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Chloride, Units.mEqL, 1d, Units.mmolL));
                break;
            case Lactate:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Lactate, Units.mmolL, AnalyteType.Measured, 0.3d, 20d,"#0.00",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Lactate, Units.mgdl, AnalyteType.Measured, 2.7d, 180.2,"#0.0",false));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Lactate, Units.gL, AnalyteType.Measured, 0.03d, 1.8d,"#0.00",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Lactate, Units.mgdl, 0.110987791d, Units.mmolL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Lactate, Units.mgdl, 0.01d, Units.gL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Lactate, Units.mmolL, 9.01d, Units.mgdl));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Lactate, Units.mmolL, 0.0901d, Units.gL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Lactate, Units.gL, 100d, Units.mgdl));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Lactate, Units.gL, 11.098779134d, Units.mmolL));
                break;
            case Creatinine:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Creatinine, Units.mgdl, AnalyteType.Measured, 0.3d, 15d,"#0.00",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Creatinine, Units.umolL, AnalyteType.Measured, 27d, 1326d,"#0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Creatinine, Units.mgdl, 88.4d, Units.umolL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Creatinine, Units.umolL, 0.01131221d, Units.mgdl));
                break;
            case AnionGap:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.AnionGap, Units.mmolL, AnalyteType.Calculated, -14d, 95d,"#0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.AnionGap, Units.mEqL, AnalyteType.Calculated, -14d, 95d,"#0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.AnionGap, Units.mmolL, 1d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.AnionGap, Units.mEqL, 1d, Units.mmolL));
                break;
            case AnionGapK:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.AnionGapK, Units.mmolL, AnalyteType.Calculated, -10d, 99d,"#0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.AnionGapK, Units.mEqL, AnalyteType.Calculated, -10d, 99d,"#0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.AnionGapK, Units.mmolL, 1d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.AnionGapK, Units.mEqL, 1d, Units.mmolL));
                break;
            case eGFR:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.eGFR, Units.mlmin173m2, AnalyteType.Calculated, 2d, 60d,"#0",true));
                break;
            case eGFRa:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.eGFRa, Units.mlmin173m2, AnalyteType.Calculated, 2d, 60d,"#0",true));
                break;
            case eGFRj:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.eGFRj, Units.mlmin173m2, AnalyteType.Calculated, 2d, 60d,"#0",true));
                break;
            case GFRckd:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.GFRckd, Units.mlmin173m2, AnalyteType.Calculated, 2d, 60d,"#0",true));
                break;
            case GFRckda:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.GFRckda, Units.mlmin173m2, AnalyteType.Calculated, 2d, 60d,"#0",true));
                break;
            case GFRswz:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.GFRswz, Units.mlmin173m2, AnalyteType.Calculated, 2d, 60d,"#0",true));
                break;
            case AlveolarO2:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.AlveolarO2, Units.mmhg, AnalyteType.Corrected, 5d, 800d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.AlveolarO2, Units.kPa, AnalyteType.Corrected, 0.67d, 106.64d,"#0.00",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.AlveolarO2, Units.mmhg, 0.1333d, Units.kPa));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.AlveolarO2, Units.kPa, 7.501875469d, Units.mmhg));
                break;
            case ArtAlvOxDiff:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.ArtAlvOxDiff, Units.mmhg, AnalyteType.Calculated, 1d, 800d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.ArtAlvOxDiff, Units.kPa, AnalyteType.Calculated, 0.13d, 106.64d,"#0.00",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.ArtAlvOxDiff, Units.mmhg, 0.1333d, Units.kPa));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.ArtAlvOxDiff, Units.kPa, 7.501875469d, Units.mmhg));
                break;
            case ArtAlvOxRatio:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.ArtAlvOxRatio, Units.Percent, AnalyteType.Calculated, 0d, 100d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.ArtAlvOxRatio, Units.fraction, AnalyteType.Calculated, 0d, 1d,"#0.000",true));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.ArtAlvOxRatio, Units.Percent, 0.01d, Units.fraction));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.ArtAlvOxRatio, Units.fraction, 100d, Units.Percent));
                break;
            case cAlveolarO2:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cAlveolarO2, Units.mmhg, AnalyteType.Corrected, 5d, 800d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cAlveolarO2, Units.kPa, AnalyteType.Corrected, 0.67d, 106.64d,"#0.00",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cAlveolarO2, Units.mmhg, 0.1333d, Units.kPa));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cAlveolarO2, Units.kPa, 7.501875469d, Units.mmhg));
                break;
            case cArtAlvOxDiff:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cArtAlvOxDiff, Units.mmhg, AnalyteType.Corrected, 0d, 800d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cArtAlvOxDiff, Units.kPa, AnalyteType.Corrected, 0.13d, 106.64d,"#0.00",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cArtAlvOxDiff, Units.mmhg, 0.1333d, Units.kPa));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cArtAlvOxDiff, Units.kPa, 7.501875469d, Units.mmhg));
                break;
            case cArtAlvOxRatio:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cArtAlvOxRatio, Units.Percent, AnalyteType.Corrected, 0d, 100d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.cArtAlvOxRatio, Units.fraction, AnalyteType.Corrected, 0d, 1d,"#0.000",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cArtAlvOxRatio, Units.Percent, 0.01d, Units.fraction));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.cArtAlvOxRatio, Units.fraction, 100d, Units.Percent));
                break;
            case TCO2:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.TCO2, Units.mmolL, AnalyteType.Measured, 5d, 50d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.TCO2, Units.mEqL, AnalyteType.Measured, 5d, 50d,"#0.0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.TCO2, Units.mmolL, 1d, Units.mEqL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.TCO2, Units.mEqL, 1d, Units.mmolL));
                break;
            case Urea:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Urea, Units.mmolL, AnalyteType.Measured, 1.1d, 42.8d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Urea, Units.mgdl, AnalyteType.Measured, 6d, 257d,"#0",false));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.Urea, Units.gL, AnalyteType.Measured, 0.06d, 2.57d,"#0.0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Urea, Units.mgdl, 0.166666667d, Units.mmolL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Urea, Units.mgdl, 0.01d, Units.gL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Urea, Units.mmolL, 6d, Units.mgdl));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Urea, Units.mmolL, 0.06d, Units.gL));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Urea, Units.gL, 100d, Units.mgdl));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.Urea, Units.gL, 16.666666667d, Units.mmolL));
                break;
            case UreaCreaRatio:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.UreaCreaRatio, Units.MillimolePerMillimole, AnalyteType.Calculated, 0.8d, 1615.4d,"#0.0",true));
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.UreaCreaRatio, Units.MilligramPerMilligram, AnalyteType.Calculated, 0.4d, 856.8d,"#0.0",false));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.UreaCreaRatio, Units.MillimolePerMillimole, 0.5304d, Units.MilligramPerMilligram));
                analyte.getUnitConversionFactors().add(createUnitConversionFactor(realm, AnalyteName.UreaCreaRatio, Units.MilligramPerMilligram, 1.885369532d, Units.MillimolePerMillimole));
                break;
            case BUN:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.BUN, Units.mgdl, AnalyteType.Measured, 3d, 120d,"#0",true));
                break;
            case BUNCreaRatio:
                analyte.getMetaInfo().add( createAnalyteUnitInfo(realm, AnalyteName.BUNCreaRatio, Units.MilligramPerMilligram, AnalyteType.Calculated, 0.2d, 400d,"#0.0",true));
                break;
        }
    }

    private void setAnalyteToFactoryDefault( Analyte analyte){
        switch (analyte.getAnalyteName()) {
            case Unknown:
                break;
            case Na:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(85d);
                analyte.setReportableLow(85d);
                analyte.setDefaultUnitReportableHigh(180d);
                analyte.setReportableHigh(180d);
                analyte.setDisplayorder(1);
                break;
            case K:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(1.5d);
                analyte.setReportableLow(1.5d);
                analyte.setDefaultUnitReportableHigh(12d);
                analyte.setReportableHigh(12d);
                analyte.setDisplayorder(2);

                break;
            case pCO2:
                analyte.setUnitType(Units.mmhg);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(5d);
                analyte.setReportableLow(5d);
                analyte.setDefaultUnitReportableHigh(250d);
                analyte.setReportableHigh(250d);
                analyte.setDisplayorder(2);
                break;
            case pO2:
                analyte.setUnitType(Units.mmhg);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(5d);
                analyte.setReportableLow(5d);
                analyte.setDefaultUnitReportableHigh(750d);
                analyte.setReportableHigh(750d);
                analyte.setDisplayorder(3);
                break;
            case Ca:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(5d);
                analyte.setReportableLow(5d);
                analyte.setDefaultUnitReportableHigh(750d);
                analyte.setReportableHigh(750d);
                analyte.setDisplayorder(3);
                break;
            case pH:
                analyte.setUnitType(Units.pH);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(6.5d);
                analyte.setReportableLow(6.5d);
                analyte.setDefaultUnitReportableHigh(8d);
                analyte.setReportableHigh(8d);
                analyte.setDisplayorder(1);
                break;
            case Hct:
                analyte.setUnitType(Units.Percent);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(10d);
                analyte.setReportableLow(10d);
                analyte.setDefaultUnitReportableHigh(75d);
                analyte.setReportableHigh(75d);
                analyte.setDisplayorder(1);
                break;
            case pHT:
                analyte.setUnitType(Units.pH);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(6.5d);
                analyte.setReportableLow(6.5d);
                analyte.setDefaultUnitReportableHigh(8d);
                analyte.setReportableHigh(8d);
                analyte.setDisplayorder(4);
                break;
            case pCO2T:
                analyte.setUnitType(Units.mmhg);
                analyte.setOptionType(EnabledSelectedOptionType.Disabled);
                analyte.setDefaultUnitReportableLow(5d);
                analyte.setReportableLow(5d);
                analyte.setDefaultUnitReportableHigh(250d);
                analyte.setReportableHigh(250d);
                analyte.setDisplayorder(5);
                break;
            case pO2T:
                analyte.setUnitType(Units.mmhg);
                analyte.setOptionType(EnabledSelectedOptionType.Disabled);
                analyte.setDefaultUnitReportableLow(5d);
                analyte.setReportableLow(5d);
                analyte.setDefaultUnitReportableHigh(750d);
                analyte.setReportableHigh(750d);
                analyte.setDisplayorder(6);
                break;
            case HCO3act:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(1d);
                analyte.setReportableLow(1d);
                analyte.setDefaultUnitReportableHigh(85d);
                analyte.setReportableHigh(85d);
                analyte.setDisplayorder(7);
                break;
            case BEecf:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(-30d);
                analyte.setReportableLow(-30d);
                analyte.setDefaultUnitReportableHigh(30d);
                analyte.setReportableHigh(30d);
                analyte.setDisplayorder(9);
                break;
            case BEb:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(-30d);
                analyte.setReportableLow(-30d);
                analyte.setDefaultUnitReportableHigh(30d);
                analyte.setReportableHigh(30d);
                analyte.setDisplayorder(8);
                break;
            case O2SAT:
                analyte.setUnitType(Units.Percent);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(0d);
                analyte.setReportableLow(0d);
                analyte.setDefaultUnitReportableHigh(100d);
                analyte.setReportableHigh(100d);
                analyte.setDisplayorder(10);
                break;
            case cTCO2:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(5d);
                analyte.setReportableLow(5d);
                analyte.setDefaultUnitReportableHigh(50d);
                analyte.setReportableHigh(50d);
                analyte.setDisplayorder(6);
                break;
            case cHgb:
                analyte.setUnitType(Units.gldl);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(3.3d);
                analyte.setReportableLow(3.3d);
                analyte.setDefaultUnitReportableHigh(25d);
                analyte.setReportableHigh(25d);
                analyte.setDisplayorder(2);
                break;
            case Glucose:
                analyte.setUnitType(Units.mgdl);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(20d);
                analyte.setReportableLow(20d);
                analyte.setDefaultUnitReportableHigh(700d);
                analyte.setReportableHigh(700d);
                analyte.setDisplayorder(1);
                break;
            case Chloride:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(65d);
                analyte.setReportableLow(65d);
                analyte.setDefaultUnitReportableHigh(140d);
                analyte.setReportableHigh(140d);
                analyte.setDisplayorder(4);
                break;
            case Lactate:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(0.3d);
                analyte.setReportableLow(0.3d);
                analyte.setDefaultUnitReportableHigh(20d);
                analyte.setReportableHigh(20d);
                analyte.setDisplayorder(2);
                break;
            case Creatinine:
                analyte.setUnitType(Units.mgdl);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(0.3d);
                analyte.setReportableLow(0.3d);
                analyte.setDefaultUnitReportableHigh(15d);
                analyte.setReportableHigh(15d);
                analyte.setDisplayorder(3);
                break;
            case AnionGap:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.Disabled);
                analyte.setDefaultUnitReportableLow(-14d);
                analyte.setReportableLow(-14d);
                analyte.setDefaultUnitReportableHigh(95d);
                analyte.setReportableHigh(95d);
                analyte.setDisplayorder(7);
                break;
            case AnionGapK:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.Disabled);
                analyte.setDefaultUnitReportableLow(-10d);
                analyte.setReportableLow(-10d);
                analyte.setDefaultUnitReportableHigh(99d);
                analyte.setReportableHigh(99d);
                analyte.setDisplayorder(8);
                break;
            case eGFR:
                analyte.setUnitType(Units.mlmin173m2);
                analyte.setOptionType(EnabledSelectedOptionType.Disabled);
                analyte.setDefaultUnitReportableLow(2d);
                analyte.setReportableLow(2d);
                analyte.setDefaultUnitReportableHigh(60d);
                analyte.setReportableHigh(60d);
                analyte.setDisplayorder(4);
                break;
            case eGFRa:
                analyte.setUnitType(Units.mlmin173m2);
                analyte.setOptionType(EnabledSelectedOptionType.Disabled);
                analyte.setDefaultUnitReportableLow(2d);
                analyte.setReportableLow(2d);
                analyte.setDefaultUnitReportableHigh(60d);
                analyte.setReportableHigh(60d);
                analyte.setDisplayorder(5);
                break;
            case eGFRj:
                analyte.setUnitType(Units.mlmin173m2);
                analyte.setOptionType(EnabledSelectedOptionType.Disabled);
                analyte.setDefaultUnitReportableLow(2d);
                analyte.setReportableLow(2d);
                analyte.setDefaultUnitReportableHigh(60d);
                analyte.setReportableHigh(60d);
                analyte.setDisplayorder(6);
                break;
            case GFRckd:
                analyte.setUnitType(Units.mlmin173m2);
                analyte.setOptionType(EnabledSelectedOptionType.Disabled);
                analyte.setDefaultUnitReportableLow(2d);
                analyte.setReportableLow(2d);
                analyte.setDefaultUnitReportableHigh(60d);
                analyte.setReportableHigh(60d);
                analyte.setDisplayorder(7);
                break;
            case GFRckda:
                analyte.setUnitType(Units.mlmin173m2);
                analyte.setOptionType(EnabledSelectedOptionType.Disabled);
                analyte.setDefaultUnitReportableLow(2d);
                analyte.setReportableLow(2d);
                analyte.setDefaultUnitReportableHigh(60d);
                analyte.setReportableHigh(60d);
                analyte.setDisplayorder(8);
                break;
            case GFRswz:
                analyte.setUnitType(Units.mlmin173m2);
                analyte.setOptionType(EnabledSelectedOptionType.Disabled);
                analyte.setDefaultUnitReportableLow(2d);
                analyte.setReportableLow(2d);
                analyte.setDefaultUnitReportableHigh(60d);
                analyte.setReportableHigh(60d);
                analyte.setDisplayorder(9);
                break;
            case AlveolarO2:
                analyte.setUnitType(Units.mmhg);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(5d);
                analyte.setReportableLow(5d);
                analyte.setDefaultUnitReportableHigh(800d);
                analyte.setReportableHigh(800d);
                analyte.setDisplayorder(12);
                break;
            case ArtAlvOxDiff:
                analyte.setUnitType(Units.mmhg);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(1d);
                analyte.setReportableLow(1d);
                analyte.setDefaultUnitReportableHigh(800d);
                analyte.setReportableHigh(800d);
                analyte.setDisplayorder(13);
                break;
            case ArtAlvOxRatio:
                analyte.setUnitType(Units.Percent);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(0d);
                analyte.setReportableLow(0d);
                analyte.setDefaultUnitReportableHigh(100d);
                analyte.setReportableHigh(100d);
                analyte.setDisplayorder(14);
                break;
            case cAlveolarO2:
                analyte.setUnitType(Units.mmhg);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(5d);
                analyte.setReportableLow(5d);
                analyte.setDefaultUnitReportableHigh(800d);
                analyte.setReportableHigh(800d);
                analyte.setDisplayorder(15);
                break;
            case cArtAlvOxDiff:
                analyte.setUnitType(Units.mmhg);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(1d);
                analyte.setReportableLow(1d);
                analyte.setDefaultUnitReportableHigh(800d);
                analyte.setReportableHigh(800d);
                analyte.setDisplayorder(16);
                break;
            case cArtAlvOxRatio:
                analyte.setUnitType(Units.Percent);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(0d);
                analyte.setReportableLow(0d);
                analyte.setDefaultUnitReportableHigh(100d);
                analyte.setReportableHigh(100d);
                analyte.setDisplayorder(17);
                break;
            case TCO2:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(5d);
                analyte.setReportableLow(5d);
                analyte.setDefaultUnitReportableHigh(50d);
                analyte.setReportableHigh(50d);
                analyte.setDisplayorder(5);
                break;
            case Urea:
                analyte.setUnitType(Units.mmolL);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(1.1d);
                analyte.setReportableLow(1.1d);
                analyte.setDefaultUnitReportableHigh(42.8d);
                analyte.setReportableHigh(42.8d);
                analyte.setDisplayorder(7);
                break;
            case UreaCreaRatio:
                analyte.setUnitType(Units.MillimolePerMillimole);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(0.8d);
                analyte.setReportableLow(0.8d);
                analyte.setDefaultUnitReportableHigh(1615.4d);
                analyte.setReportableHigh(1615.4d);
                analyte.setDisplayorder(9);
                break;
            case BUN:
                analyte.setUnitType(Units.mgdl);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(3d);
                analyte.setReportableLow(3d);
                analyte.setDefaultUnitReportableHigh(120d);
                analyte.setReportableHigh(120d);
                analyte.setDisplayorder(6);
                break;
            case BUNCreaRatio:
                analyte.setUnitType(Units.MilligramPerMilligram);
                analyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                analyte.setDefaultUnitReportableLow(0.2d);
                analyte.setReportableLow(0.2d);
                analyte.setDefaultUnitReportableHigh(400d);
                analyte.setReportableHigh(400d);
                analyte.setDisplayorder(8);
                break;
        }
    }

    private UnitConversionFactor createUnitConversionFactor(Realm realm, AnalyteName analyteName, Units fromUnit, double factor, Units toUnit){
        UnitConversionFactor unitConversionFactor = realm.createObject(UnitConversionFactor.class);
        unitConversionFactor.setAnalyteName(analyteName);
        unitConversionFactor.setfromUnitType(fromUnit);
        unitConversionFactor.setConversionValue(factor);
        unitConversionFactor.settoUnitType(toUnit);
        return unitConversionFactor;

    }

    private AnalyteUnitInfo createAnalyteUnitInfo(Realm realm, AnalyteName analyteName, Units unitType, AnalyteType analyteType, Double factoryRepLow, Double factoryRepHigh, String displayPrecision, Boolean isReference){

        AnalyteUnitInfo analyteUnitInfo =  realm.createObject(AnalyteUnitInfo.class);
        analyteUnitInfo.setAnalyteName(analyteName);
        analyteUnitInfo.setUnitType(unitType);
        analyteUnitInfo.setAnalyteType(analyteType);
        analyteUnitInfo.setFactoryReportableLow(factoryRepLow);
        analyteUnitInfo.setFactoryReportableHigh(factoryRepHigh);
        analyteUnitInfo.setPrecision(displayPrecision);
        analyteUnitInfo.setFactoryReference(isReference);
        return analyteUnitInfo;

    }


}
