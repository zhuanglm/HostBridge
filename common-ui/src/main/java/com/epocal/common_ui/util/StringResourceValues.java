package com.epocal.common_ui.util;

import android.content.Context;
import android.util.SparseIntArray;

import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.EpocTestPanelType;
import com.epocal.common.types.NotifyActionType;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.AllensTest;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.DeliverySystem;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.RespiratoryMode;
import com.epocal.common_ui.R;

/**
 * This class returns the type strings with their respective enums.
 * <p>
 * Created by Zeeshan A Zakaria on 9/27/2017.
 */

public final class StringResourceValues {

    public static SparseIntArray setEpocTestFieldGroupType() {
        SparseIntArray keyValues = new SparseIntArray();

        keyValues.put(EpocTestFieldGroupType.NONE.value, R.string.none);
        keyValues.put(EpocTestFieldGroupType.MANDATORY.value, R.string.mandatory);
        keyValues.put(EpocTestFieldGroupType.COMPLIANCE.value, R.string.compliance);
        keyValues.put(EpocTestFieldGroupType.PATIENTINFORMATION_OPTIONAL.value, R.string.patient_information_optional);
        keyValues.put(EpocTestFieldGroupType.SAMPLEINFORMATION_OPTIONAL.value, R.string.sample_information_optional);
        keyValues.put(EpocTestFieldGroupType.RESPIRATORY_OPTIONAL.value, R.string.respiratory_optional);
        keyValues.put(EpocTestFieldGroupType.ADDITIONALDOCUMENTATION_OPTIONAL.value, R.string.additional_documentation_optional);
        keyValues.put(EpocTestFieldGroupType.TESTSELECTION_OPTIONAL.value, R.string.test_selection_optional);
        keyValues.put(EpocTestFieldGroupType.CUSTOMTESTVARIABLES_OPTIONAL.value, R.string.custom_test_variables_optional);
        keyValues.put(EpocTestFieldGroupType.QA_OPTIONAL.value, R.string.qa_optional);
        keyValues.put(EpocTestFieldGroupType.DOCUMENTRESULTS_OPTIONAL.value, R.string.document_results);
        keyValues.put(EpocTestFieldGroupType.QA_TEST_TYPE.value, R.string.qa_testtype_optional);
        keyValues.put(EpocTestFieldGroupType.QA_COMMENTS.value, R.string.qa_comments_optional);

        return keyValues;
    }

    public static SparseIntArray setSampleType() {
        SparseIntArray keyValues = new SparseIntArray();

        keyValues.put(BloodSampleType.Arterial.value, R.string.arterial);
        keyValues.put(BloodSampleType.Venous.value, R.string.venous);
        keyValues.put(BloodSampleType.MixedVenous.value, R.string.mixed_venous);
        keyValues.put(BloodSampleType.Capillary.value, R.string.capillary);
        keyValues.put(BloodSampleType.Cord.value, R.string.cord);
        keyValues.put(BloodSampleType.CordArterial.value, R.string.cord_arterial);
        keyValues.put(BloodSampleType.CordVenous.value, R.string.cord_venous);

        return keyValues;
    }

    public static SparseIntArray setDrawSite() {
        SparseIntArray keyValues = new SparseIntArray();

        keyValues.put(DrawSites.ArtLine.value, R.string.art_line);
        keyValues.put(DrawSites.RRadial.value, R.string.r_radial);
        keyValues.put(DrawSites.LRadial.value, R.string.l_radial);
        keyValues.put(DrawSites.RBrach.value, R.string.r_brach);
        keyValues.put(DrawSites.LBrach.value, R.string.l_brach);
        keyValues.put(DrawSites.RFem.value, R.string.r_fem);
        keyValues.put(DrawSites.LFem.value, R.string.l_fem);

        return keyValues;
    }

    public static SparseIntArray setDeliverySystem() {
        SparseIntArray keyValues = new SparseIntArray();

        keyValues.put(DeliverySystem.AdultVent.value, R.string.adult_vent);
        keyValues.put(DeliverySystem.NeoVent.value, R.string.neo_vent);
        keyValues.put(DeliverySystem.RoomAir.value, R.string.room_airway);
        keyValues.put(DeliverySystem.NasalCannula.value, R.string.cannula);
        keyValues.put(DeliverySystem.VentiMask.value, R.string.venti_mask);
        keyValues.put(DeliverySystem.NRB.value, R.string.nrb);
        keyValues.put(DeliverySystem.AquinOx.value, R.string.aquinox);
        keyValues.put(DeliverySystem.FaceTent.value, R.string.facetent);
        keyValues.put(DeliverySystem.AeroMask.value, R.string.aeromask);
        keyValues.put(DeliverySystem.TCollar.value, R.string.t_collar);
        keyValues.put(DeliverySystem.ETTube.value, R.string.et_tube);
        keyValues.put(DeliverySystem.Bagging.value, R.string.bagging);
        keyValues.put(DeliverySystem.Vapotherm.value, R.string.vapotherm);
        keyValues.put(DeliverySystem.OxyHood.value, R.string.oxy_hood);
        keyValues.put(DeliverySystem.HFOV.value, R.string.hfov);
        keyValues.put(DeliverySystem.HFJV.value, R.string.hfjv);
        keyValues.put(DeliverySystem.Incubator.value, R.string.incubator);
        keyValues.put(DeliverySystem.AeroTx.value, R.string.aerotx);
        keyValues.put(DeliverySystem.BiPAP.value, R.string.bipap);
        keyValues.put(DeliverySystem.CPAP.value, R.string.cpap);
        keyValues.put(DeliverySystem.OxyMask.value, R.string.oxy_mask);
        keyValues.put(DeliverySystem.PediVent.value, R.string.pedi_vent);
        keyValues.put(DeliverySystem.PRB.value, R.string.prb);
        keyValues.put(DeliverySystem.TTube.value, R.string.t_tube);

        return keyValues;
    }

    public static SparseIntArray setAllensTest() {
        SparseIntArray keyValues = new SparseIntArray();

        keyValues.put(AllensTest.Positive.value, R.string.positive);
        keyValues.put(AllensTest.Negative.value, R.string.negative);
        keyValues.put(AllensTest.NA.value, R.string.none);

        return keyValues;
    }


    public static SparseIntArray setRespiratoryMode() {
        SparseIntArray keyValues = new SparseIntArray();

        keyValues.put(RespiratoryMode.SIMV.value, R.string.simv);
        keyValues.put(RespiratoryMode.AC.value, R.string.ac);
        keyValues.put(RespiratoryMode.PC.value, R.string.pc);
        keyValues.put(RespiratoryMode.PS.value, R.string.ps);
        keyValues.put(RespiratoryMode.VC.value, R.string.vc);
        keyValues.put(RespiratoryMode.BiLevel.value, R.string.bilevel);
        keyValues.put(RespiratoryMode.CPAPPS.value, R.string.cpapps);
        keyValues.put(RespiratoryMode.SIMVPC.value, R.string.simvpc);
        keyValues.put(RespiratoryMode.PAV.value, R.string.pav);
        keyValues.put(RespiratoryMode.PRVC.value, R.string.prvc);
        keyValues.put(RespiratoryMode.TC.value, R.string.tc);
        keyValues.put(RespiratoryMode.Other.value, R.string.other);
        keyValues.put(RespiratoryMode.BiVent.value, R.string.bivent);
        keyValues.put(RespiratoryMode.NCPAP.value, R.string.ncpap);
        keyValues.put(RespiratoryMode.NIV.value, R.string.niv);
        keyValues.put(RespiratoryMode.PCPS.value, R.string.pcps);
        keyValues.put(RespiratoryMode.PRVCPS.value, R.string.prvcps);
        keyValues.put(RespiratoryMode.SIMVPS.value, R.string.simvps);
        keyValues.put(RespiratoryMode.SIMVPCPS.value, R.string.simvpcps);
        keyValues.put(RespiratoryMode.SIMVPRVCPS.value, R.string.simvprvcps);
        keyValues.put(RespiratoryMode.SIMVVCPS.value, R.string.simvvcps);
        keyValues.put(RespiratoryMode.VS.value, R.string.vs);

        return keyValues;
    }

    /**
     * Create a Map of TestType value -> string resource Id.
     * Only subset of enum value required in epoctestprocedure module is populated.
     * @return SparseIntArray to map TestType enum -> string resource Id
     */
    public static SparseIntArray setTestTypeForTestProcedure() {
        SparseIntArray keyValues = new SparseIntArray();

        //TODO make dynamically visible
        //keyValues.put(TestType.Unknown.value, R.string.testtype_unknown);
        //keyValues.put(TestType.Blood.value, R.string.testtype_blood);
        keyValues.put(TestType.QualityControl.value, R.string.testtype_qualitycontrol);
        keyValues.put(TestType.CalVer.value, R.string.testtype_calver);
        keyValues.put(TestType.Proficiency.value, R.string.testtype_proficiency);
        //keyValues.put(TestType.ThermalCheck.value, R.string.testtype_thermalcheck);
        //keyValues.put(TestType.EQC.value, R.string.testtype_eqc);
        keyValues.put(TestType.Other.value, R.string.testtype_other);

        return keyValues;
    }

    /**
     * Create a Map of TestType value -> string resource Id.
     * @return SparseIntArray to map TestType enum -> string resource Id
     */
    public static SparseIntArray setTestType() {
        SparseIntArray keyValues = new SparseIntArray();

        keyValues.put(TestType.Unknown.value, R.string.testtype_unknown);
        keyValues.put(TestType.Blood.value, R.string.testtype_blood);
        keyValues.put(TestType.QualityControl.value, R.string.testtype_qualitycontrol);
        keyValues.put(TestType.CalVer.value, R.string.testtype_calver);
        keyValues.put(TestType.Proficiency.value, R.string.testtype_proficiency);
        keyValues.put(TestType.ThermalCheck.value, R.string.testtype_thermalcheck);
        keyValues.put(TestType.EQC.value, R.string.testtype_eqc);
        keyValues.put(TestType.Other.value, R.string.testtype_other);

        return keyValues;
    }

    public static SparseIntArray setEpocTestFieldType() {
        SparseIntArray keyValues = new SparseIntArray();

        keyValues.put(EpocTestFieldType.UNKNOWN.value, R.string.unknown);
        keyValues.put(EpocTestFieldType.PATIENTID.value, R.string.patient_id);
        keyValues.put(EpocTestFieldType.PATIENTID2.value, R.string.patient_id2);
        keyValues.put(EpocTestFieldType.TESTSELECTION.value, R.string.test_selection);
        keyValues.put(EpocTestFieldType.SAMPLETYPE.value, R.string.sample_type);
        keyValues.put(EpocTestFieldType.PATIENTTEMPERATURE.value, R.string.temperature);
        keyValues.put(EpocTestFieldType.HEMODILUTION.value, R.string.hemodilution);
        keyValues.put(EpocTestFieldType.COMMENTS.value, R.string.comments);
        keyValues.put(EpocTestFieldType.DRAWSITE.value, R.string.draw_site);
        keyValues.put(EpocTestFieldType.ALLENSTEST.value, R.string.allens_test);
        keyValues.put(EpocTestFieldType.DELIVERYSYSTEM.value, R.string.delivery_system);
        keyValues.put(EpocTestFieldType.MODE.value, R.string.mode);
        keyValues.put(EpocTestFieldType.FIO2.value, R.string.fi02);
        keyValues.put(EpocTestFieldType.VT.value, R.string.vt);
        keyValues.put(EpocTestFieldType.RR.value, R.string.rr);
        keyValues.put(EpocTestFieldType.TR.value, R.string.tr);
        keyValues.put(EpocTestFieldType.PEEP.value, R.string.peep);
        keyValues.put(EpocTestFieldType.PS.value, R.string.ps);
        keyValues.put(EpocTestFieldType.IT.value, R.string.it);
        keyValues.put(EpocTestFieldType.ET.value, R.string.et);
        keyValues.put(EpocTestFieldType.PIP.value, R.string.pip);
        keyValues.put(EpocTestFieldType.MAP.value, R.string.map);
        keyValues.put(EpocTestFieldType.ORDERINGPHYSICIAN.value, R.string.ordering_physician);
        keyValues.put(EpocTestFieldType.ORDERDATE.value, R.string.order_date);
        keyValues.put(EpocTestFieldType.ORDERTIME.value, R.string.order_time);
        keyValues.put(EpocTestFieldType.COLLECTEDBY.value, R.string.collected_by);
        keyValues.put(EpocTestFieldType.COLLECTIONDATE.value, R.string.collection_date);
        keyValues.put(EpocTestFieldType.COLLECTIONTIME.value, R.string.collection_time);
        keyValues.put(EpocTestFieldType.PATIENTLOCATION.value, R.string.patient_location);
        keyValues.put(EpocTestFieldType.FLOW.value, R.string.flow);
        keyValues.put(EpocTestFieldType.PATIENTGENDER.value, R.string.gender);
        keyValues.put(EpocTestFieldType.PATIENTAGE.value, R.string.age);
        keyValues.put(EpocTestFieldType.HERTZ.value, R.string.hertz);
        keyValues.put(EpocTestFieldType.DELTAP.value, R.string.deltap);
        keyValues.put(EpocTestFieldType.NOPPM.value, R.string.no_ppm);
        keyValues.put(EpocTestFieldType.RQ.value, R.string.rq);
        keyValues.put(EpocTestFieldType.NOTIFYACTION.value, R.string.notify_action);
        keyValues.put(EpocTestFieldType.NOTIFYNAME.value, R.string.notify_name);
        keyValues.put(EpocTestFieldType.NOTIFYDATE.value, R.string.notify_date);
        keyValues.put(EpocTestFieldType.NOTIFYTIME.value, R.string.notify_time);
        keyValues.put(EpocTestFieldType.READBACK.value, R.string.read_back);
        keyValues.put(EpocTestFieldType.REJECTTEST.value, R.string.reject_test);
        keyValues.put(EpocTestFieldType.LOTNUMBER.value, R.string.lot_number);
        keyValues.put(EpocTestFieldType.FLUIDTYPE.value, R.string.fluid_type);
        keyValues.put(EpocTestFieldType.TESTTYPE.value, R.string.test_type);
        keyValues.put(EpocTestFieldType.PATIENTHEIGHT.value, R.string.height);
        return keyValues;
    }

    public static SparseIntArray setTestPanels() {
        SparseIntArray keyValues = new SparseIntArray();
        keyValues.put(EpocTestPanelType.UNKNOWN.value, R.string.unknown);
        keyValues.put(EpocTestPanelType.ALL.value, R.string.tp_all);
        keyValues.put(EpocTestPanelType.GASES.value, R.string.tp_gases);
        keyValues.put(EpocTestPanelType.ELECTROLYTES.value, R.string.tp_electrolytes);
        keyValues.put(EpocTestPanelType.EXTENDEDMETABOLITES.value, R.string.tp_emp);
        keyValues.put(EpocTestPanelType.CUSTOM.value, R.string.tp_custom);

        return keyValues;
    }

    public static SparseIntArray setNotifyActionTypes() {
        SparseIntArray keyValues = new SparseIntArray();
        keyValues.put(NotifyActionType.None.value, R.string.none);
        keyValues.put(NotifyActionType.NotifyPhysician.value, R.string.notify_physician);
        keyValues.put(NotifyActionType.NotifyRegisteredNurse.value, R.string.notify_registered_nurse);
        keyValues.put(NotifyActionType.RepeatedTest.value, R.string.repeated_test);
        keyValues.put(NotifyActionType.SentToLab.value, R.string.send_to_lab);
        keyValues.put(NotifyActionType.Other.value, R.string.other);
        keyValues.put(NotifyActionType.ExpectedValues.value, R.string.expected_values);

        return keyValues;
    }

    /**
     * This method receives a list or resource IDs and converts them into their respective String values from the strings.xml file
     *
     * @param context     the application context
     * @param resourceIds the string resource ids
     * @return the array of String values
     */
    public static String[] getStringValues(Context context, Integer[] resourceIds) {
        String selectList[] = new String[resourceIds.length];
        int count = 0;
        for (int resourceId : resourceIds) {
            selectList[count++] = context.getString(resourceId);
        }
        return selectList;
    }

    /**
     * Given the value, this method will return the key of this value
     *
     * @param array the SparseIntArray
     * @param value   the value
     * @return the key
     */
    public static int getKey(SparseIntArray array, String value, Context context) {
        int size = array.size();
        int k = -1;
        for(int i = 0; i < size; i++) {
            int key = array.keyAt(i);
            String stringValue = context.getString(array.get(key));
            if (stringValue.equals(value)) {
                k = key;
                break;
            }
        }
        return k;
    }

    /**
     * This method returns values from a SparseIntArray
     *
     * @param sparseIntArray    the sparse int array
     * @return                  the returned array
     */
    public static Integer[] getValues(SparseIntArray sparseIntArray) {
        if (sparseIntArray == null) return null;
        int size = sparseIntArray.size();
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = (sparseIntArray.valueAt(i));
        }
        return array;
    }
}
