package com.epocal.datamanager;

import com.epocal.common.epocobjects.AnalyteOption;
import com.epocal.common.epocobjects.UITestPanel;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.realmentities.EpocTestPanel;
import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.common.types.EpocTestPanelType;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.util.EnumSetUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by bmate on 22/09/2017.
 */

public class TestPanelModel {
    public void createFactoryDefault(Realm realm){
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                createTestPanels(realm);

            }
        });
    }

    private void createTestPanels(Realm realm) {
        // gases
        EpocTestPanel gases = realm.createObject(EpocTestPanel.class);

        gases.setAnalyteNamesEnumSet(EnumSet.of(AnalyteName.pO2, AnalyteName.pH, AnalyteName.pCO2));

        gases.setCustom(false);
        gases.setTestPanelType(EpocTestPanelType.GASES);
        gases.setDisplayOrder(1);

        // electrolytes
        EpocTestPanel electrolytes = realm.createObject(EpocTestPanel.class);
        electrolytes.setAnalyteNamesEnumSet(EnumSet.of(AnalyteName.Na, AnalyteName.K, AnalyteName.Chloride, AnalyteName.Ca));
        electrolytes.setCustom(false);
        electrolytes.setTestPanelType(EpocTestPanelType.ELECTROLYTES);
        electrolytes.setName("Electrolytes"); // not used
        electrolytes.setDisplayOrder(2);

        // extended metabolite panel
        EpocTestPanel metabolites = realm.createObject(EpocTestPanel.class);
        metabolites.setAnalyteNamesEnumSet(EnumSet.of(AnalyteName.Na, AnalyteName.K, AnalyteName.Chloride, AnalyteName.Ca,
                AnalyteName.Glucose, AnalyteName.Lactate,
                AnalyteName.Creatinine,AnalyteName.TCO2, AnalyteName.BUN));
        metabolites.setCustom(false);
        metabolites.setTestPanelType(EpocTestPanelType.EXTENDEDMETABOLITES);
        metabolites.setDisplayOrder(3);
    }

    public void restoreFactoryDefault(){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                RealmResults<EpocTestPanel> allTestPanels = realm.where(EpocTestPanel.class).findAll();
                allTestPanels.deleteAllFromRealm();
                createTestPanels(realm);
            }
        });
        realm.close();
    }
    // from client, via EDM synchronization
    public void updateTestPanel(String panelName, final ArrayList<AnalyteName> analyteNames){
        Realm realm = Realm.getDefaultInstance();
        final EpocTestPanel etp = realm.where(EpocTestPanel.class).equalTo("mName",panelName).findFirst();
        if (etp == null) {
            realm.close();
            return;
        }

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                EnumSet<AnalyteName> analyteSet = EnumSet.noneOf(AnalyteName.class);
                for (AnalyteName aname:analyteNames ) {
                    analyteSet.add(aname);
                }
                etp.setAnalyteNamesEnumSet(analyteSet);
            }
        });
        realm.close();
    }
    // from client, via EDM synchronization
    public void addTestPanel(final EpocTestPanel panel){
       Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                EpocTestPanel managedPanel = realm.createObject(EpocTestPanel.class);
                managedPanel.setCustom(true);
                managedPanel.setDisplayOrder(panel.getDisplayOrder());
                managedPanel.setName(panel.getName());
                managedPanel.setAnalyteNamesEnumSet(panel.getAnalyteNamesEnumSet());
            }
        });
        realm.close();
    }
    public ArrayList<AnalyteName> setRuntimeTestInclusions( EpocTestPanelType testPanelType){
        ArrayList<AnalyteName> retval = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        final EpocTestPanel etp = realm.where(EpocTestPanel.class).equalTo("testPanelTypeValue",testPanelType.value).findFirst();
        if (etp != null){
            for (AnalyteName anName:etp.getAnalyteNamesEnumSet()){
                retval.add(anName);
            }
        }
        realm.close();
        return retval;
    }
    public ArrayList<AnalyteName> setRuntimeTestInclusions(EnumSet<AnalyteName> fromAnalyteSet){
        ArrayList<AnalyteName> retval = new ArrayList<>();
        for (AnalyteName an:fromAnalyteSet) {
            retval.add(an);
        }
        return retval;
    }

    // when showing testpanels in UI during a test
    // after we have card information (default testinclusions)
    // return all testpanels that have all analytes supported, enabled and selected analytes
    // nothing will be saved in Realm, all's in memory
    public ArrayList<UITestPanel> getTestPanels(List<AnalyteOption> testInclusions){
        Realm realm = Realm.getDefaultInstance();
        ArrayList<UITestPanel> retval = new ArrayList<UITestPanel>();

        // first panel to add is the 'ALL' panel
        UITestPanel allPanel = new UITestPanel();
        allPanel.setTestPanelType(EpocTestPanelType.ALL);
        allPanel.setDisplayOrder(0);
        retval.add(allPanel);
        // the custom panel is a runtime creation,  will contain all enabled and supported analytes, displayed last.
        UITestPanel customPanel = new UITestPanel();
        customPanel.setTestPanelType(EpocTestPanelType.CUSTOM);
        RealmResults<EpocTestPanel> allPanelsFromDB = realm.where(EpocTestPanel.class).findAll();
        for (EpocTestPanel eTP:allPanelsFromDB ) {

          mapPanels(eTP,customPanel,retval,testInclusions);

        }
        // last panel to add is 'Custom' panel
        retval.add(customPanel);
        realm.close();
        return retval;
    }
    // conditional check and reconciliation of supported, enabled analytes
    private void mapPanels(EpocTestPanel epocPanel,UITestPanel customPanel,List<UITestPanel> uiTestPanels, List<AnalyteOption> testInclusions){
        final AnalyteModel am = new AnalyteModel();
        boolean addPanel = true;
        UITestPanel uiPanel = new UITestPanel();
        uiPanel.setTestPanelType(epocPanel.getTestPanelType());
        uiPanel.setDisplayOrder(epocPanel.getDisplayOrder());
        if (customPanel.getDisplayOrder()<= epocPanel.getDisplayOrder()){
            customPanel.setDisplayOrder((epocPanel.getDisplayOrder()+1));
        }
        EnumSet<AnalyteName> analytegroup = epocPanel.getAnalyteNamesEnumSet();
//        for (AnalyteName aName : analytegroup) {
//            Analyte analyteObject = am.getAnalyte(aName);
//            EnabledSelectedOptionType enablement = analyteObject.getOptionType();
//            if (testInclusions.contains(aName)&& !enablement.equals(EnabledSelectedOptionType.Disabled)){
//                AnalyteOption analyteOption = new AnalyteOption();
//                analyteOption.setDisplayOrder(analyteObject.getDisplayorder());
//                analyteOption.setAnalyteName(aName);
//                analyteOption.setOptionType(analyteObject.getOptionType());
//                if (!customPanel.hasOption(aName)){
//                customPanel.getAnalyteOptionArraList().add(analyteOption);
//                }
//                uiPanel.getAnalyteOptionArraList().add(analyteOption);
//            } else{
//                addPanel = false;
//            }
//        }
        for ( AnalyteName anaName:analytegroup ) {
            addPanel = false;
            for (AnalyteOption aop: testInclusions ) {
                if (aop.getAnalyteName().equals(anaName)){
                    addPanel = true;
                }
            }
            if (!addPanel){
                break;
            }
        }
        if (addPanel) {
            uiTestPanels.add(uiPanel);
        }
    }


}
