package com.epocal.datamanager;

import android.util.Log;

import com.epocal.common.realmentities.HostConfiguration;
import com.epocal.common.realmentities.Selena;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.AllowCustomEntryType;
import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.common.types.SelenaFamilyType;
import com.epocal.common.types.SelenaOperationTypes;
import com.epocal.common.types.am.AllensTest;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.DeliverySystem;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.RespiratoryMode;

import java.util.ArrayList;
import java.util.EnumSet;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * The Selena Model class
 *
 * Created by bmate on 8/8/2017.
 */

public class SelenaModel {

    public void createFactoryDefault(Realm realm) {

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                createSelenaFamilies(realm);

            }
        });

    }

    public  void resetToFactoryDefault(){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction(){
          @Override
                  public void execute(Realm realm){
              RealmResults<Selena> allSelenas = realm.where(Selena.class).findAll();
              allSelenas.deleteAllFromRealm();
              createSelenaFamilies(realm);
            }
        });
        realm.close();
    }
    public void addCustomSelena(final Selena unmanagedCustom){
     Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                Selena managedSelena = realm.createObject(Selena.class);
                managedSelena.setName(unmanagedCustom.getName());
                managedSelena.setCustom(true);
                managedSelena.setSelenaFamilyType(unmanagedCustom.getSelenaFamilyType());
                managedSelena.setOrder(unmanagedCustom.getOrder());
                managedSelena.setEnabledSelectedOptionType(unmanagedCustom.getEnabledSelectedOptionType());
                }
        });
        realm.close();
    }
    /*
     * returns a managed list of Selenas belonging to same family, ordered by orderNr ascending
     */
    public RealmResults<Selena> getSelenaFamily (Realm realm, SelenaFamilyType selenaFamilyType){
        RealmResults<Selena> retval = realm.where(Selena.class).findAllSorted("mOrder", Sort.ASCENDING);
        return retval;
    }

    public ArrayList<Selena> getSelenaFamily (SelenaFamilyType selenaFamilyType){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Selena> retval = realm.where(Selena.class).equalTo("mSelenaFamilyTypeValue", selenaFamilyType.value).findAll();
        ArrayList<Selena> results = new ArrayList<>(realm.copyFromRealm(retval));
        return results;
    }

    /**
     * apply option to single Selena
     *
     **/
    public void applySelenaSelectionRules(SelenaFamilyType selenaFamilyType, String selenaName, SelenaOperationTypes operation) {
        applyRules(selenaFamilyType, selenaName, operation);
    }

    /**
     * apply Selena selection rules for the selected Selena
     *
     **/
    private void applyRules(SelenaFamilyType selenaFamilyType, String selenaName, SelenaOperationTypes operation) {

        if (operation == SelenaOperationTypes.Select) {
            unselectAll(selenaFamilyType);
            updateOptionType(selenaName, EnabledSelectedOptionType.EnabledSelected);
        } else if (operation == SelenaOperationTypes.UnSelect) {
            updateOptionType(selenaName, EnabledSelectedOptionType.EnabledUnselected);
        } else if (operation == SelenaOperationTypes.Enable) {
            updateOptionType(selenaName, EnabledSelectedOptionType.EnabledUnselected);
        } else if (operation == SelenaOperationTypes.Disable) {
            updateOptionType(selenaName, EnabledSelectedOptionType.Disabled);
        }
    }

    private void unselectAll(SelenaFamilyType selenaFamilyType) {
        Realm realm = Realm.getDefaultInstance();
        ArrayList<Selena> selenas = getSelenaFamily(selenaFamilyType);
        try {
            realm.beginTransaction();
            for (Selena selena : selenas) {
                Selena managedSelena = realm.where(Selena.class).equalTo("mName", selena.getName()).findFirst();

                // Unselect all the Selanas which are enabled and selected.
                if (managedSelena.getEnabledSelectedOptionType() != EnabledSelectedOptionType.Disabled) {

                    if (managedSelena.getEnabledSelectedOptionType() == EnabledSelectedOptionType.EnabledSelected) {
                        managedSelena.setEnabledSelectedOptionType(EnabledSelectedOptionType.EnabledUnselected);
                    }
                }
            }
            realm.commitTransaction();
            realm.close();
        } catch (Exception e) {
            Log.e("Realm Error", e.getMessage());
            realm.cancelTransaction();
            realm.close();
        }
    }

    private void updateOptionType(String selenaName, EnabledSelectedOptionType enabledSelectedOptionType) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            Selena managedSelena = realm.where(Selena.class).equalTo("mName", selenaName).findFirst();
            managedSelena.setEnabledSelectedOptionType(enabledSelectedOptionType);
            realm.commitTransaction();
            realm.close();
        } catch (Exception e) {
            Log.e("Realm Error", e.getMessage());
            realm.cancelTransaction();
            realm.close();
        }
    }

    private void createSelenaFamilies(Realm realm){
        EnumSet<SelenaFamilyType> allSelenas = EnumSet.allOf(SelenaFamilyType.class);

        for (SelenaFamilyType familyType : allSelenas ) {
            createFactorySelenas(realm, familyType);
        }

    }
    private void createFactorySelenas(Realm realm, SelenaFamilyType familyType) {

        switch (familyType) {
            case UNKNOWN:
                break;
            case DELIVERYSYSTEM:
                createSelena(realm,familyType, "AdultVent",1, DeliverySystem.AdultVent.value);
                createSelena(realm,familyType, "NeoVent",2, DeliverySystem.NeoVent.value);
                createSelena(realm,familyType, "RoomAir",3, DeliverySystem.RoomAir.value);
                createSelena(realm,familyType, "Cannula",4, DeliverySystem.NasalCannula.value);
                createSelena(realm,familyType, "HFNC",5, DeliverySystem.HFNC.value);
                createSelena(realm,familyType, "VentiMask",6, DeliverySystem.VentiMask.value);
                createSelena(realm,familyType, "NRB",7, DeliverySystem.NRB.value);
                createSelena(realm,familyType, "AquinOx",8, DeliverySystem.AquinOx.value);
                createSelena(realm,familyType, "FaceTent",9, DeliverySystem.AeroMask.value);
                createSelena(realm,familyType, "AeroMask",10, DeliverySystem.AeroMask.value);
                createSelena(realm,familyType, "TCollar",11, DeliverySystem.TCollar.value);
                createSelena(realm,familyType, "ETTube",12, DeliverySystem.ETTube.value);
                createSelena(realm,familyType, "Bagging",13, DeliverySystem.Bagging.value);
                createSelena(realm,familyType, "Vapotherm",14, DeliverySystem.Vapotherm.value);
                createSelena(realm,familyType, "OxyHood",15, DeliverySystem.OxyHood.value);
                createSelena(realm,familyType, "HFOV",16, DeliverySystem.HFOV.value);
                createSelena(realm,familyType, "HFJV",17, DeliverySystem.HFJV.value);
                createSelena(realm,familyType, "Incubator",18, DeliverySystem.Incubator.value);
                createSelena(realm,familyType, "Other",19, DeliverySystem.Other.value);
                createSelena(realm,familyType, "AeroTx",20, DeliverySystem.AeroTx.value);
                createSelena(realm,familyType, "BiPAP",21, DeliverySystem.BiPAP.value);
                createSelena(realm,familyType, "CPAP",22, DeliverySystem.CPAP.value);
                createSelena(realm,familyType, "OxyMask",23, DeliverySystem.OxyMask.value);
                createSelena(realm,familyType, "PediVent",24, DeliverySystem.PediVent.value);
                createSelena(realm,familyType, "PRB",25, DeliverySystem.PRB.value);
                createSelena(realm,familyType, "TTube",26, DeliverySystem.TTube.value);
                break;
            case RESPIRATORYMODE:
                createSelena(realm,familyType, "SIMV",1, RespiratoryMode.SIMV.value);
                createSelena(realm,familyType, "AC",2, RespiratoryMode.AC.value );
                createSelena(realm,familyType, "PC",3, RespiratoryMode.PC.value);
                createSelena(realm,familyType, "PS",4, RespiratoryMode.PS.value);
                createSelena(realm,familyType, "VC",5, RespiratoryMode.VC.value);
                createSelena(realm,familyType, "BiLevel",6, RespiratoryMode.BiLevel.value);
                createSelena(realm,familyType, "CPAPPS",7, RespiratoryMode.CPAPPS.value);
                createSelena(realm,familyType, "SIMVPC",8, RespiratoryMode.SIMVPC.value);
                createSelena(realm,familyType, "PAV",9, RespiratoryMode.PAV.value);
                createSelena(realm,familyType, "PRVC",10, RespiratoryMode.PRVC.value);
                createSelena(realm,familyType, "TC",11, RespiratoryMode.TC.value);
                createSelena(realm,familyType, "Other",12, RespiratoryMode.Other.value);
                createSelena(realm,familyType, "BiVent",13, RespiratoryMode.BiVent.value);
                createSelena(realm,familyType, "NCPAP",14, RespiratoryMode.NCPAP.value);
                createSelena(realm,familyType, "NIV",15, RespiratoryMode.NIV.value);
                createSelena(realm,familyType, "PCPS",16, RespiratoryMode.PCPS.value);
                createSelena(realm,familyType, "PRVCPS",17, RespiratoryMode.PRVCPS.value);
                createSelena(realm,familyType, "SIMVPS",18, RespiratoryMode.SIMVPS.value);
                createSelena(realm,familyType, "SIMVPCPS",19, RespiratoryMode.SIMVPCPS.value);
                createSelena(realm,familyType, "SIMVPRVCPS",20, RespiratoryMode.SIMVPRVCPS.value);
                createSelena(realm,familyType, "SIMVVCPS",21, RespiratoryMode.SIMVVCPS.value);
                createSelena(realm,familyType, "VS",22, RespiratoryMode.VS.value);
                break;
            case DRAWSITE:
                createSelena(realm,familyType, "ArtLine",1, DrawSites.ArtLine.value);
                createSelena(realm,familyType, "RRadial",2, DrawSites.RRadial.value);
                createSelena(realm,familyType, "LRadial",3, DrawSites.LRadial.value);
                createSelena(realm,familyType, "RBrach",4, DrawSites.RBrach.value);
                createSelena(realm,familyType, "LBrach",5, DrawSites.LBrach.value);
                createSelena(realm,familyType, "RFem",6, DrawSites.RFem.value);
                createSelena(realm,familyType, "LFem",7, DrawSites.LFem.value);
                createSelena(realm,familyType, "CentralLine",8, DrawSites.CentLine.value);
                createSelena(realm,familyType, "RHeel",9, DrawSites.RHeel.value);
                createSelena(realm,familyType, "LHeel",10, DrawSites.LHeel.value);
                createSelena(realm,familyType, "RFinger",11, DrawSites.RFinger.value);
                createSelena(realm,familyType, "LFinger",12, DrawSites.LFinger.value);
                createSelena(realm,familyType, "RToe",13, DrawSites.RToe.value);
                createSelena(realm,familyType, "LToe",14, DrawSites.LToe.value);
                createSelena(realm,familyType, "PICC",15, DrawSites.PICC.value);
                createSelena(realm,familyType, "PA",16, DrawSites.PA.value);
                createSelena(realm,familyType, "RA",17, DrawSites.RA.value);
                createSelena(realm,familyType, "RV",18, DrawSites.RV.value);
                createSelena(realm,familyType, "SwanGanz",19, DrawSites.SwanGanz.value);
                createSelena(realm,familyType, "UAC",20, DrawSites.UAC.value);
                createSelena(realm,familyType, "UVC",21, DrawSites.UVC.value);
                createSelena(realm,familyType, "Other",22, DrawSites.Other.value);
                break;
            case SAMPLETYPE:
                createSelena(realm,familyType, "Unknown",1, BloodSampleType.Unknown.value);
                createSelena(realm,familyType, "Arterial",2, BloodSampleType.Arterial.value);
                createSelena(realm,familyType, "Venous",3, BloodSampleType.Venous.value);
                createSelena(realm,familyType, "MixedVenous",4, BloodSampleType.MixedVenous.value);
                createSelena(realm,familyType, "Capillary",5, BloodSampleType.Capillary.value);
                createSelena(realm,familyType, "Cord",6, BloodSampleType.Cord.value);
                createSelena(realm,familyType, "CordArterial",7, BloodSampleType.CordArterial.value);
                createSelena(realm,familyType, "CordVenous",8, BloodSampleType.CordVenous.value);

                break;
            case ALLENSTYPE:
                createSelena(realm,familyType, "Positive",1, AllensTest.Positive.value);
                createSelena(realm,familyType, "Negative",2, AllensTest.Negative.value);
                createSelena(realm,familyType, "NotApplicable",3,AllensTest.NA.value);
                break;
        }
    }
    private void createSelena(Realm realm,SelenaFamilyType familyType, String name,int order , int mapId){
        Selena newSelena = realm.createObject(Selena.class);
        newSelena.setEnabledSelectedOptionType(EnabledSelectedOptionType.EnabledUnselected);
        newSelena.setCustom(false);
        newSelena.setSelenaFamilyType(familyType);
        newSelena.setName(name);
        newSelena.setOrder(order);
        newSelena.setMapId(mapId);
    }

    public boolean isCustomEntryAllowed (SelenaFamilyType selenaFamilyType){
        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        EnumSet<AllowCustomEntryType> currentSetting = realm.where(HostConfiguration.class).findFirst().getAllowCustomEntryTypes();
        switch(selenaFamilyType){

            default:
                break;
            case DELIVERYSYSTEM:
                retval = currentSetting.contains(AllowCustomEntryType.AllowCustomDeliverySystemEntry);
                break;
            case RESPIRATORYMODE:
                retval = currentSetting.contains(AllowCustomEntryType.AllowCustomRespiratoryModeEntry);
                break;
            case DRAWSITE:
                retval = currentSetting.contains(AllowCustomEntryType.AllowCustomDrawSiteEntry);
                break;
            case SAMPLETYPE:
                retval = currentSetting.contains(AllowCustomEntryType.AllowCustomSampleTypeEntry);
                break;
            case ALLENSTYPE:
                retval = currentSetting.contains(AllowCustomEntryType.AllowCustomAllensTestEntry);
                break;
        }
        realm.close();
        return retval;
    }

    public void setAllowCustomEntry(SelenaFamilyType selenaFamilyType, boolean allow){
        Realm realm = Realm.getDefaultInstance();
        final HostConfiguration hc = realm.where(HostConfiguration.class).findFirst();
        final EnumSet<AllowCustomEntryType> currentSetting = hc.getAllowCustomEntryTypes();
        switch(selenaFamilyType){

            default:
                break;
            case DELIVERYSYSTEM:
                if (allow){
                    currentSetting.add(AllowCustomEntryType.AllowCustomDeliverySystemEntry);
                }else{
                    currentSetting.remove(AllowCustomEntryType.AllowCustomDeliverySystemEntry);
                }
                break;
            case RESPIRATORYMODE:
                if (allow){
                    currentSetting.add(AllowCustomEntryType.AllowCustomRespiratoryModeEntry);
                }else{
                    currentSetting.remove(AllowCustomEntryType.AllowCustomRespiratoryModeEntry);
                }
                break;
            case DRAWSITE:
                if (allow){
                    currentSetting.add(AllowCustomEntryType.AllowCustomDrawSiteEntry);
                }else{
                    currentSetting.remove(AllowCustomEntryType.AllowCustomDrawSiteEntry);
                }
                break;
            case SAMPLETYPE:
                if (allow){
                    currentSetting.add(AllowCustomEntryType.AllowCustomSampleTypeEntry);
                }else{
                    currentSetting.remove(AllowCustomEntryType.AllowCustomSampleTypeEntry);
                }
                break;
            case ALLENSTYPE:
                if (allow){
                    currentSetting.add(AllowCustomEntryType.AllowCustomAllensTestEntry);
                }else{
                    currentSetting.remove(AllowCustomEntryType.AllowCustomAllensTestEntry);
                }
                break;
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                hc.setAllowCustomEntryTypes(currentSetting);
            }
        });
        realm.close();
    }

    public void setupTestEnablement(TestRecord mTestRecord) {
        Realm realm = Realm.getDefaultInstance();
        //check if we have enabledSelected sampletype
        Selena se = realm.where(Selena.class)
                .equalTo("mSelenaFamilyTypeValue",SelenaFamilyType.SAMPLETYPE.value)
                .equalTo("mEnabledSelectedOptionTypeValue",EnabledSelectedOptionType.EnabledSelected.value)
                .findFirst();
        if (se!=null){
            mTestRecord.getTestDetail().setSampleType(BloodSampleType.fromInt(se.getMapId()));
        }
        //check if we have enabledSelected AllensType
         se = realm.where(Selena.class)
                .equalTo("mSelenaFamilyTypeValue",SelenaFamilyType.ALLENSTYPE.value)
                .equalTo("mEnabledSelectedOptionTypeValue",EnabledSelectedOptionType.EnabledSelected.value)
                .findFirst();
        if (se!=null){
            if(!se.isCustom()){
                mTestRecord.getRespiratoryDetail().setRespAllensType(AllensTest.fromInt(se.getMapId()));
            }else if(se.isCustom() && isCustomEntryAllowed(SelenaFamilyType.ALLENSTYPE)){
                mTestRecord.getRespiratoryDetail().setCustomAllensTest(se.getName());
            }
        }
        //check if we have enabledSelected DeliverySystem
         se = realm.where(Selena.class)
                .equalTo("mSelenaFamilyTypeValue",SelenaFamilyType.DELIVERYSYSTEM.value)
                .equalTo("mEnabledSelectedOptionTypeValue",EnabledSelectedOptionType.EnabledSelected.value)
                .findFirst();
        if (se!=null){
            if(!se.isCustom()){
                mTestRecord.getRespiratoryDetail().setDeliverySystem(DeliverySystem.fromInt(se.getMapId()));
            }else if(se.isCustom() && isCustomEntryAllowed(SelenaFamilyType.DELIVERYSYSTEM)){
                mTestRecord.getRespiratoryDetail().setCustomDeliverySystem(se.getName());
            }
        }
        //check if we have enabledSelected DrawSite
         se = realm.where(Selena.class)
                .equalTo("mSelenaFamilyTypeValue",SelenaFamilyType.DRAWSITE.value)
                .equalTo("mEnabledSelectedOptionTypeValue",EnabledSelectedOptionType.EnabledSelected.value)
                .findFirst();
        if (se!=null){
            if(!se.isCustom()){
                mTestRecord.getRespiratoryDetail().setDrawSite(DrawSites.fromInt(se.getMapId()));
            }else if(se.isCustom() && isCustomEntryAllowed(SelenaFamilyType.DRAWSITE)){
                mTestRecord.getRespiratoryDetail().setCustomDrawSite(se.getName());
            }
        }
        //check if we have enabledSelected RespiratoryMode
         se = realm.where(Selena.class)
                .equalTo("mSelenaFamilyTypeValue",SelenaFamilyType.RESPIRATORYMODE.value)
                .equalTo("mEnabledSelectedOptionTypeValue",EnabledSelectedOptionType.EnabledSelected.value)
                .findFirst();
        if (se!=null){
            if(!se.isCustom()){
                mTestRecord.getRespiratoryDetail().setRespiratoryMode(RespiratoryMode.fromInt(se.getMapId()));
            }else if(se.isCustom() && isCustomEntryAllowed(SelenaFamilyType.RESPIRATORYMODE)){
                mTestRecord.getRespiratoryDetail().setCustomMode(se.getName());
            }
        }
        realm.close();

    }
}
