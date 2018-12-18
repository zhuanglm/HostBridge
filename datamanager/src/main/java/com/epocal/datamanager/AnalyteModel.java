package com.epocal.datamanager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.epocal.common.epocobjects.AnalyteOption;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.realmentities.AnalyteUnitInfo;
import com.epocal.common.realmentities.HostConfiguration;
import com.epocal.common.realmentities.UnitConversionFactor;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.AnalyteType;
import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.common.types.SelenaOperationTypes;
import com.epocal.common.types.am.Units;
import com.epocal.util.DecimalConversionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * Created by bmate on 6/12/2017.
 * This class implements the business logic related to user actions:
 * - change analyte unit
 * - change reportable values
 * - select / unselect
 * - enable / disable
 */

public class AnalyteModel {

    /**
     * gets the type of the analyte
     *
     * @return AnalyteType enum
     **/
    public AnalyteType getAnalyteType(Analyte analyte) {
        if (analyte.getMetaInfo().size() > 0) {
            return analyte.getMetaInfo().first().getAnalyteType();
        }
        return AnalyteType.Measured;
    }
    public Units getAnalyteUnit (Analyte analyte){
        Units retval = Units.ENUM_UNINITIALIZED;
        retval = getAnalyte(analyte.getAnalyteName()).getUnitType();
        return retval;
    }

    /**
     * gets all unit types of a given analyte
     *
     * @return ArrayList<Units>
     **/
    public ArrayList<Units> getAllUnitTypes(Analyte analyte) {
        ArrayList<Units> retval = new ArrayList<Units>();
        if (analyte.getMetaInfo().size() > 0) {
            for (AnalyteUnitInfo metainfo : analyte.getMetaInfo()) {

                retval.add(metainfo.getUnitType());
            }
        }
        return retval;
    }

    /**
     * gets primary (default) unit type of an analyte
     *
     * @return enum Units
     **/
    public Units getPrimaryUnitType(Analyte analyte) {
        Units retval = Units.ENUM_UNINITIALIZED;
        if (analyte.getMetaInfo().size() > 0) {
            for (AnalyteUnitInfo metainfo : analyte.getMetaInfo()) {

                if (metainfo.getFactoryReference()) {
                    retval = metainfo.getUnitType();
                    break;
                }
            }
        }
        return retval;
    }

    /**
     * gets value conversion factor if changing unit
     *
     * @return Double
     **/
    public Double getConversionFactor(Analyte analyte, Units fromUnit, Units toUnit) {
        Double retval = null;
        for (UnitConversionFactor conversionFactor : analyte.getUnitConversionFactors()) {

            Units from = conversionFactor.getfromUnitType();
            Units to = conversionFactor.gettoUnitType();

            if (from.equals(fromUnit) && to.equals(toUnit)) {
                retval = conversionFactor.getConversionValue();
                break;
            }
        }
        return retval;
    }
    /**
     * get all analytes from realm (unmanaged)
     *
     * @return List of Analyte
     **/
    public List<Analyte> getAllAnalytes(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Analyte> allAnalytes = realm.where(Analyte.class).findAll();
        List<Analyte> retval = realm.copyFromRealm(allAnalytes);
        realm.close();
        return retval;
    }
    /**
     * gets precision formatter string (e.g.:#.##) depending on actual unittype
     * if the analyte has no meta-info, precision defaults to no decimals displayed
     *
     * @return String
     **/
    public String getDisplayPrecision(Analyte analyte) {
        String retval = "#0";
        if (analyte.getMetaInfo().size() > 0) {
            for (AnalyteUnitInfo metainfo : analyte.getMetaInfo()) {

                if (metainfo.getUnitType().compareTo(analyte.getUnitType()) == 0) {
                    retval = metainfo.getPrecision();
                    break;
                }
            }
        }
        return retval;

    }
    /**
     * filters out all enabled analytes
     *
     * @return ArrayList of AnalyteName enums
     **/
    public ArrayList<AnalyteOption> getDefaultTestInclusions() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Analyte> allEnabledAnalytes = realm.where(Analyte.class).
                notEqualTo("enablement",EnabledSelectedOptionType.Disabled.value)
                .findAll();
        int displayOrder = 0;
        ArrayList<AnalyteOption> retval = new ArrayList<>();
        for (Analyte an : allEnabledAnalytes ) {
            AnalyteOption aop = new AnalyteOption();
            aop.setDisplayOrder(displayOrder);
            displayOrder++;
            aop.setOptionType(an.getOptionType());
            aop.setAnalyteName(an.getAnalyteName());
            if (!an.getAnalyteName().equals(AnalyteName.ENUM_UNINITIALIZED) && getAnalyteType(an).equals(AnalyteType.Measured)){
                retval.add(aop);
            }
        }
        realm.close();
        return retval;
    }

    /**
     * Converts reportable high to String, applying decimal precision formatting (e.g.:#.##)
     *
     * @return String
     **/
    public String displayReportableHigh(Analyte analyte) {
        return DecimalConversionUtil.DisplayDouble(analyte.getReportableHigh(), getDisplayPrecision(analyte));
    }

    /**
     * Converts reportable low to String, applying decimal precision formatting (e.g.:#.##)
     *
     * @return String
     **/
    public String displayReportableLow(Analyte analyte) {
        return DecimalConversionUtil.DisplayDouble(analyte.getReportableLow(), getDisplayPrecision(analyte));
    }

    /**
     * full update of all managed analyte objects to the values of a list of input (unmanaged) analytes - most likely from a synchronization.
     * if any of updates failed, the entire update is canceled
     *
     * @return boolean
     **/
    public boolean updateAnalytes(final List<Analyte> newAnalytes) {
        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            for (Analyte newvalue : newAnalytes) {
                Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", newvalue.getAnalyteName().value).findFirst();
                // set reportables
                managedAnalyte.setReportableHigh(newvalue.getReportableHigh());
                managedAnalyte.setReportableLow(newvalue.getReportableLow());
                // set unitType
                managedAnalyte.setUnitType(newvalue.getUnitType());
                // set enablement
                managedAnalyte.setOptionType(newvalue.getOptionType());
                // set defaultunitreportables
                if (getPrimaryUnitType(managedAnalyte).compareTo(newvalue.getUnitType()) == 0) {
                    managedAnalyte.setDefaultUnitReportableHigh(newvalue.getReportableHigh());
                    managedAnalyte.setDefaultUnitReportableLow(newvalue.getReportableLow());
                } else {
                    double conversion_factor = getConversionFactor(managedAnalyte, newvalue.getUnitType(), getPrimaryUnitType(managedAnalyte));
                    managedAnalyte.setDefaultUnitReportableHigh(newvalue.getReportableHigh() * conversion_factor);
                    managedAnalyte.setDefaultUnitReportableLow(newvalue.getReportableLow() * conversion_factor);
                }
            }
            realm.commitTransaction();
            retval = true;
        } catch (Exception e) {
            Log.e("Realm Error", e.getMessage());
            realm.cancelTransaction();
            retval = false;
        }
        realm.close();
        return retval;
    }

    /**
     * full update of a managed analyte object to the values of an input (unmanaged) analyte - most likely from a synchronization.
     *
     * @return boolean
     **/
    public boolean updateAnalyte(final Analyte newvalue) {

        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        final Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", newvalue.getAnalyteName().value).findFirst();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // set reportables
                    managedAnalyte.setReportableHigh(newvalue.getReportableHigh());
                    managedAnalyte.setReportableLow(newvalue.getReportableLow());
                    // set unitType
                    managedAnalyte.setUnitType(newvalue.getUnitType());
                    // set enablement
                    managedAnalyte.setOptionType(newvalue.getOptionType());
                    // set defaultunitreportables
                    if (getPrimaryUnitType(managedAnalyte).compareTo(newvalue.getUnitType()) == 0) {
                        managedAnalyte.setDefaultUnitReportableHigh(newvalue.getReportableHigh());
                        managedAnalyte.setDefaultUnitReportableLow(newvalue.getReportableLow());
                    } else {
                        double conversion_factor = getConversionFactor(managedAnalyte, newvalue.getUnitType(), getPrimaryUnitType(managedAnalyte));
                        managedAnalyte.setDefaultUnitReportableHigh(newvalue.getReportableHigh() * conversion_factor);
                        managedAnalyte.setDefaultUnitReportableLow(newvalue.getReportableLow() * conversion_factor);
                    }
                }
            });
            retval = true;
        } catch (Exception e) {
            retval = false;
            Log.e("Realm Error", e.getMessage());
        } finally {
            realm.close();
        }
        return retval;
    }

    /**
     * update the analyte unittype
     * recalculate reportables
     *
     * @return boolean
     **/
    public boolean updateAnalyteUnitType(AnalyteName analyteName, final Units newUnit) {

        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        final Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", analyteName.value).findFirst();
        if (managedAnalyte.getUnitType().equals(newUnit)) {
            realm.close();
            return true;
        } else {

            try {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        // set unitType
                        managedAnalyte.setUnitType(newUnit);

                        // If resetting back to the default unit, return the default values with no change
                        if (newUnit.equals(getPrimaryUnitType(managedAnalyte))) {
                            managedAnalyte.setReportableHigh(managedAnalyte.getDefaultUnitReportableHigh());
                            managedAnalyte.setReportableLow(managedAnalyte.getDefaultUnitReportableLow());

                        } else {
                            // recalculate reportables
                            double conversion_factor = getConversionFactor(managedAnalyte, getPrimaryUnitType(managedAnalyte), newUnit);
                            managedAnalyte.setReportableHigh(managedAnalyte.getDefaultUnitReportableHigh() * conversion_factor);
                            managedAnalyte.setReportableLow(managedAnalyte.getDefaultUnitReportableLow() * conversion_factor);
                        }

                    }
                });
                retval = true;
            } catch (Exception e) {
                e.printStackTrace();
                retval = false;
                // TODO: log
            } finally {
                realm.close();
            }
        }
        return retval;


    }

    /**
     * update  reportable low of an analyte
     *
     * @return boolean
     **/
    public boolean updateAnalyteReportableLow(AnalyteName analyteName, final double newReportableLow) {

        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        final Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", analyteName.value).findFirst();
        if (managedAnalyte.getReportableLow() == newReportableLow) {
            realm.close();
            return true;
        } else {

            try {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        // set reportablelow
                        managedAnalyte.setReportableLow(newReportableLow);
                        // set or recalculate DefaultUnitReportableLow
                        if (managedAnalyte.getUnitType().compareTo(getPrimaryUnitType(managedAnalyte)) == 0) {
                            managedAnalyte.setDefaultUnitReportableLow(newReportableLow);
                        } else {
                            double conversion_factor = getConversionFactor(managedAnalyte, managedAnalyte.getUnitType(), getPrimaryUnitType(managedAnalyte));
                            managedAnalyte.setDefaultUnitReportableLow(newReportableLow * conversion_factor);
                        }

                    }
                });
                retval = true;
            } catch (Exception exptn) {
                retval = false;
                //TODO: log
            } finally {
                realm.close();
            }
        }
        return retval;


    }

    /**
     * update  reportable high of an analyte
     *
     * @return boolean
     **/
    public boolean updateAnalyteReportableHigh(AnalyteName analyteName, final double newReportableHigh) {

        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        final Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", analyteName.value).findFirst();
        if (managedAnalyte.getReportableHigh() == newReportableHigh) {
            realm.close();
            return true;
        } else {

            try {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        // set reportable high
                        managedAnalyte.setReportableHigh(newReportableHigh);
                        // set or recalculate DefaultUnitReportableLow
                        if (managedAnalyte.getUnitType().compareTo(getPrimaryUnitType(managedAnalyte)) == 0) {
                            managedAnalyte.setDefaultUnitReportableHigh(newReportableHigh);
                        } else {
                            double conversion_factor = getConversionFactor(managedAnalyte, managedAnalyte.getUnitType(), getPrimaryUnitType(managedAnalyte));
                            managedAnalyte.setDefaultUnitReportableHigh(newReportableHigh * conversion_factor);
                        }

                    }
                });
                retval = true;
            } catch (Exception exptn) {
                retval = false;
                // TODO: log
            } finally {
                realm.close();
            }
        }
        return retval;
    }

    /**
     * clone analyte object
     *
     * @return Analyte (unmanaged)
     **/
    private Analyte cloneAnalyte(Analyte input) {
        Analyte retval = new Analyte();
        retval.setDefaultUnitReportableLow(input.getDefaultUnitReportableLow());
        retval.setDefaultUnitReportableHigh(input.getDefaultUnitReportableHigh());
        retval.setOptionType(input.getOptionType());
        retval.setReportableHigh(input.getReportableHigh());
        retval.setReportableLow(input.getReportableLow());
        retval.setDisplayorder(input.getDisplayorder());
        retval.setAnalyteName(input.getAnalyteName());
        retval.setUnitType(input.getUnitType());
        if (retval.getUnitConversionFactors() == null) {
            retval.setUnitConversionFactors(new RealmList<UnitConversionFactor>());
        }
        for (UnitConversionFactor ucf : input.getUnitConversionFactors()) {

            retval.getUnitConversionFactors().add(ucf);
        }
        if (retval.getMetaInfo() == null) {
            retval.setMetaInfo(new RealmList<AnalyteUnitInfo>());
        }
        for (AnalyteUnitInfo aui : input.getMetaInfo()) {
            retval.getMetaInfo().add(aui);
        }
        return retval;
    }

    private AnalyteUnitInfo cloneAUI(AnalyteUnitInfo aui) {
        AnalyteUnitInfo retval = new AnalyteUnitInfo();
        retval.setAnalyteName(aui.getAnalyteName());
        retval.setAnalyteType(aui.getAnalyteType());
        retval.setFactoryReference(aui.getFactoryReference());
        retval.setFactoryReportableHigh(aui.getFactoryReportableHigh());
        retval.setFactoryReportableLow(aui.getFactoryReportableLow());
        retval.setPrecision(aui.getPrecision());
        retval.setUnitType(aui.getUnitType());

        return retval;
    }


    /**
     * get analyte object by name
     *
     * @return Analyte
     **/
    public Analyte getAnalyte(AnalyteName analyteName) {
        Analyte retval = null;
        Realm realm = Realm.getDefaultInstance();
        final Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", analyteName.value).findFirst();
       // retval = cloneAnalyte(managedAnalyte);
        retval  = realm.copyFromRealm(managedAnalyte);
        realm.close();
        return retval;
    }
    /**
     * get unitconversion factor list from analyte
     *
     * @return Analyte
     **/
    public ArrayList<UnitConversionFactor> getUnitConversionFactors(AnalyteName analyteName) {
        ArrayList<UnitConversionFactor> retval = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        final Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", analyteName.value).findFirst();
        for (UnitConversionFactor ucf: managedAnalyte.getUnitConversionFactors() ) {
            retval.add(realm.copyFromRealm(ucf));
        }
        realm.close();
        return retval;
    }
    /**
     * switch from Urea(or UreaCreaRatio) to BUN (or BUNCreaRatio)
     * doesn't consider isBUNEnabled flag
     *
     * @return AnalyteName enum
     **/
    public AnalyteName analytesUreaToBUNInternalSwitch(AnalyteName analyteName) {
        if (analyteName.compareTo(AnalyteName.Urea) == 0) {
            return AnalyteName.BUN;
        }
        if (analyteName.compareTo(AnalyteName.UreaCreaRatio) == 0) {
            return AnalyteName.BUNCreaRatio;
        }
        return analyteName;
    }

    /**
     * switch from BUN (or BUNCreaRatio) to Urea(or UreaCreaRatio) and viceVersa
     * takes in consideration isBUNenabled hostconfiguration flag
     *
     * @return AnalyteName enum
     **/
    public AnalyteName analytesBUNToUreaTwowaySwitch(AnalyteName analyteName) {

        Realm realm = Realm.getDefaultInstance();
        HostConfiguration hc = realm.where(HostConfiguration.class).findFirst();
        final boolean useBun = hc.isBUNEnabled();
        realm.close();
        if (analyteName.compareTo(AnalyteName.BUN) == 0 && (!useBun)) {
            return AnalyteName.Urea;
        } else if (analyteName.compareTo(AnalyteName.BUNCreaRatio) == 0 && (!useBun)) {
            return AnalyteName.UreaCreaRatio;
        } else if (analyteName.compareTo(AnalyteName.Urea) == 0 && (useBun)) {
            return AnalyteName.BUN;
        } else if (analyteName.compareTo(AnalyteName.UreaCreaRatio) == 0 && (useBun)) {
            return AnalyteName.BUNCreaRatio;
        }

        return analyteName;
    }

    /**
     * if measured then enable-unselect, else disable analytes
     *
     * @return void
     **/
    private void unselectAnalytes(AnalyteName... analytes) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            for (AnalyteName anaName : analytes) {
                Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", anaName.value).findFirst();
                if (managedAnalyte.getOptionType().compareTo(EnabledSelectedOptionType.EnabledSelected) == 0) {
                    // set enablement
                    if (getAnalyteType(managedAnalyte).compareTo(AnalyteType.Measured) == 0) {
                        managedAnalyte.setOptionType(EnabledSelectedOptionType.EnabledUnselected);
                    } else {
                        managedAnalyte.setOptionType(EnabledSelectedOptionType.Disabled);
                    }

                }
            }
            realm.commitTransaction();

        } catch (Exception e) {
            Log.e("Realm Error", e.getMessage());
            realm.cancelTransaction();
            realm.close();
        }
    }

    /**
     * if measured and enabledUnselected then enable-select, else enable-select analytes
     *
     * @return void
     **/
    private void selectAnalytes(AnalyteName... analytes) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            for (AnalyteName anaName : analytes) {
                Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", anaName.value).findFirst();

                // set enablement
                if (getAnalyteType(managedAnalyte).compareTo(AnalyteType.Measured) == 0 && managedAnalyte.getOptionType().compareTo(EnabledSelectedOptionType.EnabledUnselected) == 0) {
                    managedAnalyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
                } else {
                    managedAnalyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
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

    /**
     * disable analytes regardless of type or option
     *
     * @return void
     **/
    private void disableAnalytes(AnalyteName... analytes) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            for (AnalyteName anaName : analytes) {
                Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", anaName.value).findFirst();

                // set enablement
                managedAnalyte.setOptionType(EnabledSelectedOptionType.Disabled);

            }
            realm.commitTransaction();
            realm.close();
        } catch (Exception e) {
            Log.e("Realm Error", e.getMessage());
            realm.cancelTransaction();
            realm.close();
        }
    }

    /**
     * enable analytes if they are disabled. if measured, then enabledUnselected, else enabledSelected
     *
     * @return void
     **/
    private void enableAnalytes(AnalyteName... analytes) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            for (AnalyteName anaName : analytes) {
                Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", anaName.value).findFirst();

                // set enablement
                if (managedAnalyte.getOptionType().compareTo(EnabledSelectedOptionType.Disabled) == 0) {
                    if (getAnalyteType(managedAnalyte).compareTo(AnalyteType.Measured) == 0) {
                        managedAnalyte.setOptionType(EnabledSelectedOptionType.EnabledUnselected);
                    } else {
                        managedAnalyte.setOptionType(EnabledSelectedOptionType.EnabledSelected);
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

    /**
     * check if the analyte is enabledSelected
     *
     * @return boolean
     **/
    private boolean isSelected(AnalyteName analyteName) {
        Realm realm = Realm.getDefaultInstance();
        Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", analyteName.value).findFirst();
        boolean retval = false;
        if (managedAnalyte.getOptionType().compareTo(EnabledSelectedOptionType.EnabledSelected) == 0) {
            retval = true;
        }
        realm.close();
        return retval;
    }

    /**
     * check if one or more analytes are enabled
     * if any one of the analytes is not enabled, returns false
     *
     * @return boolean
     **/
    public boolean isEnabled(AnalyteName... analyteName) {
        boolean retval = true;
        Realm realm = Realm.getDefaultInstance();
        for (AnalyteName anaName : analyteName) {
            Analyte managedAnalyte = realm.where(Analyte.class).equalTo("analyte", anaName.value).findFirst();
            if (managedAnalyte.getOptionType().compareTo(EnabledSelectedOptionType.Disabled) == 0) {
                retval = false;
                break;
            }
        }
        return retval;
    }

    /**
     * apply option to single analyte
     **/
    public void applyAnalyteSelectionRules(AnalyteName analyteName, SelenaOperationTypes operation) {
        applyRules(analyteName, operation, false);
    }

    /**
     * apply option to a group of analytes
     **/
    public void applyAnalyteSelectionRules(EnumSet<AnalyteName> group, SelenaOperationTypes operationType) {
        for (AnalyteName anaName : group) {
            applyRules(anaName, operationType, true);
        }
    }

    /**
     * apply option to a group of analytes
     **/
    public void applyAnalyteSelectionRules(ArrayList<AnalyteName> group, SelenaOperationTypes operationType) {
        for (AnalyteName anaName : group) {
            applyRules(anaName, operationType, true);
        }
    }

    /**
     * check if an analyte is selectable
     *
     * @return boolean
     **/
    public boolean isSelectable(Analyte analyte) {
        boolean retval = false;
        if (getAnalyteType(analyte).compareTo(AnalyteType.Measured) == 0) {
            return true;
        }
        switch (analyte.getAnalyteName()) {
            case pHT:
                retval = isEnabled(AnalyteName.pH);
                break;
            case pO2T:
                retval = isEnabled(AnalyteName.pO2);
                break;
            case pCO2T:
                retval = isEnabled(AnalyteName.pCO2);
                break;
            case HCO3act:
                retval = isEnabled(AnalyteName.pH, AnalyteName.pCO2);
                break;
            case BEecf:
                retval = isEnabled(AnalyteName.pH, AnalyteName.HCO3act);
                break;
            case BEb:
                retval = isEnabled(AnalyteName.pH, AnalyteName.HCO3act);
                break;
            case O2SAT:
                retval = isEnabled(AnalyteName.pO2, AnalyteName.pH, AnalyteName.HCO3act);
                break;
            case cTCO2:
                retval = isEnabled(AnalyteName.pCO2, AnalyteName.HCO3act);
                break;
            case AlveolarO2:
                retval = isEnabled(AnalyteName.pCO2);
                break;
            case ArtAlvOxDiff:
                retval = isEnabled(AnalyteName.pO2, AnalyteName.AlveolarO2);
                break;
            case ArtAlvOxRatio:
                retval = isEnabled(AnalyteName.pO2, AnalyteName.AlveolarO2);
                break;
            case cAlveolarO2:
                retval = isEnabled(AnalyteName.pCO2T);
                break;
            case cArtAlvOxDiff:
                retval = isEnabled(AnalyteName.pO2T, AnalyteName.cAlveolarO2);
                break;
            case cArtAlvOxRatio:
                retval = isEnabled(AnalyteName.pO2T, AnalyteName.cAlveolarO2);
                break;
            case AnionGap:
                if (isEnabled(AnalyteName.TCO2)) {
                    retval = isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.TCO2);
                } else {
                    retval = isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.HCO3act);
                }
                break;
            case AnionGapK:
                if (isEnabled(AnalyteName.TCO2)) {
                    retval = isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.TCO2, AnalyteName.K);
                } else {
                    retval = isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.HCO3act, AnalyteName.K);
                }
                break;
            case eGFR:
                retval = isEnabled(AnalyteName.Creatinine);
                break;
            case eGFRa:
                retval = isEnabled(AnalyteName.Creatinine);
                break;
            case eGFRj:
                retval = isEnabled(AnalyteName.Creatinine);
                break;
            case GFRckd:
                retval = isEnabled(AnalyteName.Creatinine);
                break;
            case GFRckda:
                retval = isEnabled(AnalyteName.Creatinine);
                break;
            case GFRswz:
                retval = isEnabled(AnalyteName.Creatinine);
                break;
            case BUNCreaRatio:
                retval = isEnabled(AnalyteName.BUN, AnalyteName.Creatinine);
                break;
            case UreaCreaRatio:
                retval = isEnabled(AnalyteName.Urea, AnalyteName.Creatinine);
                break;
            case cHgb:
                retval = isEnabled(AnalyteName.Hct);
                break;
            default:
                break;
        }
        return retval;
    }


    /**
     * apply analyte selection rules
     **/
    private void applyRules(AnalyteName analyteName, SelenaOperationTypes operation, boolean bulk) {
        switch (analyteName) {
            case Unknown:
                break;
            case Na:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.Na);
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.Na, AnalyteName.AnionGap, AnalyteName.AnionGapK);
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.Na);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.Na);
                        break;
                }
                break;
            case K:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.K);
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.K, AnalyteName.AnionGapK);
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.K);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.K);
                        break;
                }
                break;
            case pCO2:
                if (!bulk) {
                    switch (operation) {
                        case None:
                            break;
                        case Enable:
                            enableAnalytes(AnalyteName.pH, AnalyteName.pO2, AnalyteName.pCO2);
                            break;
                        case Disable:
                            disableAnalytes(AnalyteName.pH, AnalyteName.pO2, AnalyteName.pCO2,
                                    AnalyteName.pHT, AnalyteName.HCO3act, AnalyteName.BEecf, AnalyteName.BEb, AnalyteName.O2SAT,
                                    AnalyteName.pO2T, AnalyteName.ArtAlvOxDiff, AnalyteName.ArtAlvOxRatio,
                                    AnalyteName.pCO2T, AnalyteName.cTCO2, AnalyteName.AlveolarO2,
                                    AnalyteName.cArtAlvOxDiff, AnalyteName.cArtAlvOxRatio,
                                    AnalyteName.cAlveolarO2);
                            if (!isSelected(AnalyteName.TCO2)) {
                                disableAnalytes(AnalyteName.AnionGap, AnalyteName.AnionGapK);
                            }

                            break;
                        case Select:
                            selectAnalytes(AnalyteName.pH, AnalyteName.pO2, AnalyteName.pCO2);
                            break;
                        case UnSelect:
                            unselectAnalytes(AnalyteName.pH, AnalyteName.pO2, AnalyteName.pCO2);
                            break;
                    }
                }
                break;
            case pO2:

                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.pH, AnalyteName.pO2, AnalyteName.pCO2);
                        break;
                    case Disable:
                        if (!bulk) {
                            disableAnalytes(AnalyteName.pH, AnalyteName.pO2, AnalyteName.pCO2,
                                    AnalyteName.pHT, AnalyteName.HCO3act, AnalyteName.BEecf, AnalyteName.BEb, AnalyteName.O2SAT,
                                    AnalyteName.pO2T, AnalyteName.ArtAlvOxDiff, AnalyteName.ArtAlvOxRatio,
                                    AnalyteName.pCO2T, AnalyteName.cTCO2, AnalyteName.AlveolarO2,
                                    AnalyteName.cArtAlvOxDiff, AnalyteName.cArtAlvOxRatio,
                                    AnalyteName.cAlveolarO2);
                            if (!isSelected(AnalyteName.TCO2)) {
                                disableAnalytes(AnalyteName.AnionGap, AnalyteName.AnionGapK);
                            }
                        }
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.pH, AnalyteName.pO2, AnalyteName.pCO2);
                        break;
                    case UnSelect:
                        if (!bulk) {
                            unselectAnalytes(AnalyteName.pH, AnalyteName.pO2, AnalyteName.pCO2);
                        }
                        break;
                }

                break;

            case pH:

                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (!bulk) {
                            enableAnalytes(AnalyteName.pH);
                        }
                        break;
                    case Disable:

                        disableAnalytes(AnalyteName.pH, AnalyteName.pO2, AnalyteName.pCO2,
                                AnalyteName.pHT, AnalyteName.HCO3act, AnalyteName.BEecf, AnalyteName.BEb, AnalyteName.O2SAT,
                                AnalyteName.pO2T, AnalyteName.ArtAlvOxDiff, AnalyteName.ArtAlvOxRatio,
                                AnalyteName.pCO2T, AnalyteName.cTCO2, AnalyteName.AlveolarO2,
                                AnalyteName.cArtAlvOxDiff, AnalyteName.cArtAlvOxRatio,
                                AnalyteName.cAlveolarO2);
                        if (!isSelected(AnalyteName.TCO2)) {
                            disableAnalytes(AnalyteName.AnionGap, AnalyteName.AnionGapK);
                        }

                        break;
                    case Select:
                        if (!bulk) {
                            selectAnalytes(AnalyteName.pH);
                        }
                        break;
                    case UnSelect:

                        unselectAnalytes(AnalyteName.pH, AnalyteName.pO2, AnalyteName.pCO2);

                        break;
                }

                break;

            case Ca:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.Ca);
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.Ca);
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.Ca);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.Ca);
                        break;
                }
                break;
            case Hct:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.Hct);
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.Hct, AnalyteName.cHgb);
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.Hct);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.Hct);
                        break;
                }
                break;
            case Creatinine:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.Creatinine);
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.Creatinine, AnalyteName.eGFR, AnalyteName.eGFRa, AnalyteName.eGFRj, AnalyteName.GFRckd, AnalyteName.GFRckda, AnalyteName.GFRswz, AnalyteName.BUNCreaRatio, AnalyteName.UreaCreaRatio);
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.Creatinine);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.Creatinine);
                        break;
                }
                break;
            case Glucose:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.Glucose);
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.Glucose);
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.Glucose);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.Glucose);
                        break;
                }
                break;

            case Chloride:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.Chloride, AnalyteName.AnionGap, AnalyteName.AnionGapK);
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.Chloride);
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.Chloride);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.Chloride);
                        break;
                }
                break;
            case Lactate:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.Lactate);
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.Lactate);
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.Lactate);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.Lactate);
                        break;
                }
                break;
            case cHgb:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.Hct)) {
                            enableAnalytes(AnalyteName.cHgb);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.cHgb);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.Hct)) {
                            selectAnalytes(AnalyteName.cHgb);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.cHgb);
                        break;
                }
                break;
            case eGFR:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            enableAnalytes(AnalyteName.eGFR);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.eGFR);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            selectAnalytes(AnalyteName.eGFR);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.eGFR);
                        break;
                }
                break;
            case eGFRa:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            enableAnalytes(AnalyteName.eGFRa);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.eGFRa);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            selectAnalytes(AnalyteName.eGFRa);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.eGFRa);
                        break;
                }
                break;
            case eGFRj:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            enableAnalytes(AnalyteName.eGFRj);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.eGFRj);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            selectAnalytes(AnalyteName.eGFRj);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.eGFRj);
                        break;
                }
                break;
            case GFRckd:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            enableAnalytes(AnalyteName.GFRckd);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.GFRckd);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            selectAnalytes(AnalyteName.GFRckd);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.GFRckd);
                        break;
                }
                break;
            case GFRckda:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            enableAnalytes(AnalyteName.GFRckda);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.GFRckda);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            selectAnalytes(AnalyteName.GFRckda);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.GFRckda);
                        break;
                }
                break;
            case GFRswz:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            enableAnalytes(AnalyteName.GFRswz);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.GFRswz);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.Creatinine)) {
                            selectAnalytes(AnalyteName.GFRswz);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.GFRswz);
                        break;
                }
                break;
            case AlveolarO2:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pCO2)) {
                            enableAnalytes(AnalyteName.AlveolarO2);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.AlveolarO2, AnalyteName.ArtAlvOxDiff, AnalyteName.ArtAlvOxRatio);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pCO2)) {
                            selectAnalytes(AnalyteName.AlveolarO2);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.AlveolarO2, AnalyteName.ArtAlvOxDiff, AnalyteName.ArtAlvOxRatio);
                        break;
                }
                break;

            case ArtAlvOxDiff:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pO2, AnalyteName.AlveolarO2)) {
                            enableAnalytes(AnalyteName.ArtAlvOxDiff);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.ArtAlvOxDiff);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pO2, AnalyteName.AlveolarO2)) {
                            selectAnalytes(AnalyteName.ArtAlvOxDiff);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.ArtAlvOxDiff);
                        break;
                }
                break;

            case ArtAlvOxRatio:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pO2, AnalyteName.AlveolarO2)) {
                            enableAnalytes(AnalyteName.ArtAlvOxRatio);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.ArtAlvOxRatio);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pO2, AnalyteName.AlveolarO2)) {
                            selectAnalytes(AnalyteName.ArtAlvOxRatio);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.ArtAlvOxRatio);
                        break;
                }
                break;

            case cAlveolarO2:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pCO2T)) {
                            enableAnalytes(AnalyteName.cAlveolarO2);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.cAlveolarO2, AnalyteName.cArtAlvOxDiff, AnalyteName.cArtAlvOxRatio);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pCO2T)) {
                            selectAnalytes(AnalyteName.cAlveolarO2);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.cAlveolarO2, AnalyteName.cArtAlvOxDiff, AnalyteName.cArtAlvOxRatio);
                        break;
                }
                break;

            case cArtAlvOxDiff:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pCO2T, AnalyteName.cAlveolarO2)) {
                            enableAnalytes(AnalyteName.cArtAlvOxDiff);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.cArtAlvOxDiff);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pCO2T, AnalyteName.cAlveolarO2)) {
                            selectAnalytes(AnalyteName.cArtAlvOxDiff);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.cArtAlvOxDiff);
                        break;
                }
                break;

            case cArtAlvOxRatio:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pCO2T, AnalyteName.cAlveolarO2)) {
                            enableAnalytes(AnalyteName.cArtAlvOxRatio);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.cArtAlvOxRatio);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pCO2T, AnalyteName.cAlveolarO2)) {
                            selectAnalytes(AnalyteName.cArtAlvOxRatio);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.cArtAlvOxRatio);
                        break;
                }
                break;

            case AnionGap:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.HCO3act) || isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.TCO2)) {
                            enableAnalytes(AnalyteName.AnionGap);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.AnionGap);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.HCO3act) || isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.TCO2)) {
                            selectAnalytes(AnalyteName.AnionGap);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.AnionGap);
                        break;
                }
                break;

            case AnionGapK:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.K, AnalyteName.HCO3act) || isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.K, AnalyteName.TCO2)) {
                            enableAnalytes(AnalyteName.AnionGapK);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.AnionGapK);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.K, AnalyteName.HCO3act) || isEnabled(AnalyteName.Na, AnalyteName.Chloride, AnalyteName.K, AnalyteName.TCO2)) {
                            selectAnalytes(AnalyteName.AnionGapK);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.AnionGapK);
                        break;
                }
                break;

            case pHT:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pH)) {
                            enableAnalytes(AnalyteName.pHT);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.pHT);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pH)) {
                            selectAnalytes(AnalyteName.pHT);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.pHT);
                        break;
                }
                break;

            case pCO2T:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pCO2)) {
                            enableAnalytes(AnalyteName.pCO2T);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.pCO2T);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pCO2)) {
                            selectAnalytes(AnalyteName.pCO2T);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.pCO2T);
                        break;
                }
                break;

            case pO2T:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pO2)) {
                            enableAnalytes(AnalyteName.pO2T);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.pO2T, AnalyteName.cArtAlvOxDiff, AnalyteName.cArtAlvOxRatio);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pO2)) {
                            selectAnalytes(AnalyteName.pO2T);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.pO2T, AnalyteName.cArtAlvOxDiff, AnalyteName.cArtAlvOxRatio);
                        break;
                }
                break;

            case HCO3act:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pH, AnalyteName.pCO2)) {
                            enableAnalytes(AnalyteName.HCO3act);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.HCO3act, AnalyteName.BEecf, AnalyteName.BEb, AnalyteName.O2SAT, AnalyteName.cTCO2);
                        if (!isSelected(AnalyteName.TCO2)) {
                            disableAnalytes(AnalyteName.AnionGap, AnalyteName.AnionGapK);
                        }
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pH, AnalyteName.pCO2)) {
                            selectAnalytes(AnalyteName.HCO3act);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.HCO3act, AnalyteName.BEecf, AnalyteName.BEb, AnalyteName.O2SAT, AnalyteName.cTCO2);
                        if (!isSelected(AnalyteName.TCO2)) {
                            unselectAnalytes(AnalyteName.AnionGap, AnalyteName.AnionGapK);
                        }
                        break;
                }
                break;
            case BEecf:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pH, AnalyteName.HCO3act)) {
                            enableAnalytes(AnalyteName.BEecf);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.BEecf);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pH, AnalyteName.HCO3act)) {
                            selectAnalytes(AnalyteName.BEecf);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.BEecf);
                        break;
                }
                break;
            case BEb:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pH, AnalyteName.HCO3act)) {
                            enableAnalytes(AnalyteName.BEb);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.BEb);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pH, AnalyteName.HCO3act)) {
                            selectAnalytes(AnalyteName.BEb);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.BEb);
                        break;
                }
                break;
            case O2SAT:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pO2, AnalyteName.pH, AnalyteName.HCO3act)) {
                            enableAnalytes(AnalyteName.O2SAT);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.O2SAT);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pO2, AnalyteName.pH, AnalyteName.HCO3act)) {
                            selectAnalytes(AnalyteName.O2SAT);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.O2SAT);
                        break;
                }
                break;
            case cTCO2:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.pCO2, AnalyteName.HCO3act)) {
                            enableAnalytes(AnalyteName.cTCO2);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.cTCO2);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.pCO2, AnalyteName.HCO3act)) {
                            selectAnalytes(AnalyteName.cTCO2);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.cTCO2);
                        break;
                }
                break;
            case TCO2:
                switch (operation) {
                    case None:
                        break;
                    case Enable:

                        enableAnalytes(AnalyteName.TCO2);

                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.TCO2);
                        if (!isSelected(AnalyteName.HCO3act)) {
                            disableAnalytes(AnalyteName.AnionGapK, AnalyteName.AnionGap);
                        }
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.TCO2);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.TCO2);
                        break;
                }
                break;

            case BUN:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.BUN);
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.BUN, AnalyteName.BUNCreaRatio);
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.BUN);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.BUN);
                        break;
                }
                break;


            case BUNCreaRatio:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.BUN, AnalyteName.Creatinine)) {
                            enableAnalytes(AnalyteName.BUNCreaRatio);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.BUNCreaRatio);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.BUN, AnalyteName.Creatinine)) {
                            selectAnalytes(AnalyteName.BUNCreaRatio);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.BUNCreaRatio);
                        break;
                }
                break;

            case Urea:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        enableAnalytes(AnalyteName.Urea);
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.Urea, AnalyteName.UreaCreaRatio);
                        break;
                    case Select:
                        selectAnalytes(AnalyteName.Urea);
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.Urea);
                        break;
                }
                break;

            case UreaCreaRatio:
                switch (operation) {
                    case None:
                        break;
                    case Enable:
                        if (isEnabled(AnalyteName.Urea, AnalyteName.Creatinine)) {
                            enableAnalytes(AnalyteName.UreaCreaRatio);
                        }
                        break;
                    case Disable:
                        disableAnalytes(AnalyteName.UreaCreaRatio);
                        break;
                    case Select:
                        if (isEnabled(AnalyteName.Urea, AnalyteName.Creatinine)) {
                            selectAnalytes(AnalyteName.UreaCreaRatio);
                        }
                        break;
                    case UnSelect:
                        unselectAnalytes(AnalyteName.UreaCreaRatio);
                        break;
                }
                break;
        }
    }
}
