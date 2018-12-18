package com.epocal.datamanager.realmrepository;

import com.epocal.common.realmentities.BarcodeSetting;
import com.epocal.common.realmentities.DMSetting;
import com.epocal.common.realmentities.DeviceSetting;
import com.epocal.common.realmentities.HostConfiguration;
import com.epocal.common.realmentities.Printer;
import com.epocal.common.realmentities.User;
import com.epocal.common.types.BarcodeSymbologiesType;
import com.epocal.common.types.ConnectionType;
import com.epocal.common.types.InputFieldType;
import com.epocal.common.types.Permissions;
import com.epocal.common.types.PrinterType;
import com.epocal.datamanager.AnalyteFactory;
import com.epocal.datamanager.EVADCardLotDescriptionModel;
import com.epocal.datamanager.EVADModel;
import com.epocal.datamanager.InputFieldConfigModel;
import com.epocal.datamanager.QARangeModel;
import com.epocal.datamanager.QASampleInfoModel;
import com.epocal.datamanager.RangeModel;
import com.epocal.datamanager.SelenaModel;
import com.epocal.datamanager.TestPanelModel;
import com.epocal.datamanager.WorkflowRepository;
import com.epocal.util.DateUtil;

import java.util.Date;

import io.realm.Realm;

/**
 * Created by bmate on 3/17/2017.
 * Class responsibility : create factory default data for first-time use:
 * factory device settings
 * factory hostconfiguration settings
 * factory predefinec users
 * factory preset bloodsampletype ranges (ref and crt)
 * factory preset analytes and their reportable ranges
 * factory defined test input fields (a.k.a test attributes)
 * factory defined test panels
 * !!!!! set HOST in Nextgen or Legacy mode !!!!
 */

public class RepositoryManager {
    /*
    //=========================================================================
    // !!!!! change NEXTGEN_MODE to false to compile a legacy edition !!!!! //
    //=========================================================================
     */
    public static boolean NEXTGEN_MODE = true;

    public static void CleanRealm (){
        Realm realm = Realm.getDefaultInstance();
        realm.deleteAll();
        realm.close();
    }

    public static void FactoryInitializeRealm(Realm realm) {
        //DeviceSettings
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){

                DeviceSetting deviceSetting = realm.createObject((DeviceSetting.class));
                deviceSetting.resetFactoryDefault();

            }
        });
        //HostConfiguration
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){

                HostConfiguration hf = realm.createObject((HostConfiguration.class));
                hf.resetFactoryDefault(NEXTGEN_MODE);
                hf.setSensorConfigVersion("27.1");


            }
        });
        //Users

        //create  noop and administrator
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                User noop = realm.createObject(User.class,PrimaryKeyFactory.getInstance().nextKey(User.class));
                noop.setUserId("anonymous");
                noop.setUserName("Anonymous");
                noop.setPassword("anonymous");
                noop.setStartDate(DateUtil.CreateDate("2017-01-01 00:00:00"));
                noop.setEndDate(DateUtil.CreateDate("2066-01-01 23:59:59"));
                noop.setCreated(new Date());
                noop.setActive(true);
                noop.setSpecial(true);
                noop.setPermission(Permissions.RUNQAANDPATIENTTESTS);
                noop.setEnabled(true);

                User admin = realm.createObject(User.class,PrimaryKeyFactory.getInstance().nextKey(User.class));
                admin.setUserId("administrator");
                admin.setPassword("administrator");
                admin.setUserName("Administrator");
                admin.setStartDate(DateUtil.CreateDate("2017-01-01 00:00:00"));
                admin.setEndDate(DateUtil.CreateDate("2066-01-01 23:59:59"));
                admin.setCreated(new Date());
                admin.setActive(true);
                admin.setSpecial(true);
                admin.setPermission(Permissions.HOSTADMINISTRATOR);
                admin.setEnabled(true);
// test user 1111:
                User u2 = realm.createObject(User.class,PrimaryKeyFactory.getInstance().nextKey(User.class));
                u2.setUserId("1111");
                u2.setUserName("Orion");
                u2.setPassword("1111");
                u2.setStartDate(DateUtil.CreateDate("2017-01-01 00:00:00"));
                u2.setEndDate(DateUtil.CreateDate("2066-01-01 23:59:59"));
                u2.setCreated(new Date());
                u2.setActive(true);
                u2.setSpecial(false);
                u2.setPermission(Permissions.RUNPATIENTTESTS);
                u2.setEnabled(true);
// user 2222, disabled
                User u3 = realm.createObject(User.class,PrimaryKeyFactory.getInstance().nextKey(User.class));
                u3.setUserId("2222");
                u3.setUserName("4twos");
                u3.setPassword("2222");
                u3.setStartDate(DateUtil.CreateDate("2017-01-01 00:00:00"));
                u3.setEndDate(DateUtil.CreateDate("2018-12-01 23:59:59"));
                u3.setCreated(new Date());
                u3.setActive(true);
                u3.setSpecial(false);
                u3.setPermission(Permissions.RUNPATIENTTESTS);
                u3.setEnabled(false);

// user 3333, expires Jan 10 2018
                User u4 = realm.createObject(User.class,PrimaryKeyFactory.getInstance().nextKey(User.class));
                u4.setUserId("3333");
                u4.setUserName("4trees");
                u4.setPassword("3333");
                u4.setStartDate(DateUtil.CreateDate("2017-01-01 00:00:00"));
                u4.setEndDate(DateUtil.CreateDate("2018-04-19 23:59:59"));
                u4.setCreated(new Date());
                u4.setActive(true);
                u4.setSpecial(false);
                u4.setPermission(Permissions.RUNPATIENTTESTS);
                u4.setEnabled(true);
// user 4444, expired
                User u5 = realm.createObject(User.class,PrimaryKeyFactory.getInstance().nextKey(User.class));
                u5.setUserId("4444");
                u5.setUserName("4fours");
                u5.setPassword("4444");
                u5.setStartDate(DateUtil.CreateDate("2017-01-03 00:00:00"));
                u5.setEndDate(DateUtil.CreateDate("2017-02-01 00:00:00"));
                u5.setCreated(DateUtil.CreateDate("2017-01-02 00:00:00"));
                u5.setActive(true);
                u5.setSpecial(false);
                u5.setPermission(Permissions.RUNPATIENTTESTS);
                u5.setEnabled(true);
            }
        });

        //create default printer
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                Printer defPrinter = realm.createObject(Printer.class,PrimaryKeyFactory.getInstance().nextKey(Printer.class));
                defPrinter.setPrinterName("default printer");
                defPrinter.setPrinterAddress("192.168.0.1");
                defPrinter.setThePrinterType(PrinterType.Other);
                defPrinter.setTheConnectionType(ConnectionType.WiFi);
                defPrinter.setIsPrintCalculatedResult(false);
                defPrinter.setIsPrintCorrectedResult(false);
                defPrinter.setIsPrintTestInfo(false);
            }
        });

        //create DM setting
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                DMSetting dmSetting = realm.createObject(DMSetting.class,PrimaryKeyFactory.getInstance().nextKey(DMSetting.class));
                dmSetting.setAddress("0.0.0.0");
                dmSetting.setPort("");
                dmSetting.setPresent(true);
            }
        });

        //barcode symbologies
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                BarcodeSetting barcodeSetting_USER_ID = realm.createObject(BarcodeSetting.class,PrimaryKeyFactory.getInstance().nextKey(BarcodeSetting.class));
                barcodeSetting_USER_ID.setInputFieldType(InputFieldType.USER_ID);
                barcodeSetting_USER_ID.setCropBegin(0);
                barcodeSetting_USER_ID.setCropEnd(0);
                barcodeSetting_USER_ID.setSymbologies(BarcodeSymbologiesType.AllMasks.value);

                BarcodeSetting barcodeSetting_PASSWORD = realm.createObject(BarcodeSetting.class,PrimaryKeyFactory.getInstance().nextKey(BarcodeSetting.class));
                barcodeSetting_PASSWORD.setInputFieldType(InputFieldType.PASSWORD);
                barcodeSetting_PASSWORD.setCropBegin(0);
                barcodeSetting_PASSWORD.setCropEnd(0);
                barcodeSetting_PASSWORD.setSymbologies(BarcodeSymbologiesType.AllMasks.value);

                BarcodeSetting barcodeSetting_PATIENT_ID = realm.createObject(BarcodeSetting.class,PrimaryKeyFactory.getInstance().nextKey(BarcodeSetting.class));
                barcodeSetting_PATIENT_ID.setInputFieldType(InputFieldType.PATIENT_ID);
                barcodeSetting_PATIENT_ID.setCropBegin(0);
                barcodeSetting_PATIENT_ID.setCropEnd(0);
                barcodeSetting_PATIENT_ID.setSymbologies(BarcodeSymbologiesType.AllMasks.value);

                BarcodeSetting barcodeSetting_PATIENT_OR_LOT_ID = realm.createObject(BarcodeSetting.class,PrimaryKeyFactory.getInstance().nextKey(BarcodeSetting.class));
                barcodeSetting_PATIENT_OR_LOT_ID.setInputFieldType(InputFieldType.PATIENT_OR_LOT_ID);
                barcodeSetting_PATIENT_OR_LOT_ID.setCropBegin(0);
                barcodeSetting_PATIENT_OR_LOT_ID.setCropEnd(0);
                barcodeSetting_PATIENT_OR_LOT_ID.setSymbologies(BarcodeSymbologiesType.AllMasks.value);

                BarcodeSetting barcodeSetting_ID_2 = realm.createObject(BarcodeSetting.class,PrimaryKeyFactory.getInstance().nextKey(BarcodeSetting.class));
                barcodeSetting_ID_2.setInputFieldType(InputFieldType.ID_2);
                barcodeSetting_ID_2.setCropBegin(0);
                barcodeSetting_ID_2.setCropEnd(0);
                barcodeSetting_ID_2.setSymbologies(BarcodeSymbologiesType.AllMasks.value);

                BarcodeSetting barcodeSetting_COMMENT = realm.createObject(BarcodeSetting.class,PrimaryKeyFactory.getInstance().nextKey(BarcodeSetting.class));
                barcodeSetting_COMMENT.setInputFieldType(InputFieldType.COMMENT);
                barcodeSetting_COMMENT.setCropBegin(0);
                barcodeSetting_COMMENT.setCropEnd(0);
                barcodeSetting_COMMENT.setSymbologies(BarcodeSymbologiesType.AllMasks.value);

                BarcodeSetting barcodeSetting_OTHER = realm.createObject(BarcodeSetting.class,PrimaryKeyFactory.getInstance().nextKey(BarcodeSetting.class));
                barcodeSetting_OTHER.setInputFieldType(InputFieldType.OTHER);
                barcodeSetting_OTHER.setCropBegin(0);
                barcodeSetting_OTHER.setCropEnd(0);
                barcodeSetting_OTHER.setSymbologies(BarcodeSymbologiesType.AllMasks.value);
            }
        });

        // Ranges (rangevalues for all sample types and analytes)

        new RangeModel().restoreFactoryDefaultRanges(realm);
        new QARangeModel().restoreFactoryDefaultRanges(realm);

        // Analytes (analytes to factory values)
        new AnalyteFactory().createFactoryDefault(realm);

        // Create TestWorkflows
        new WorkflowRepository().createTestWorkflows(realm);

        // Create Factory InputFieldConfig objects
        new InputFieldConfigModel().createFactoryDefault(realm);
        // Create Factory Selenas
        new SelenaModel().createFactoryDefault(realm);

        new QASampleInfoModel().restoreFactoryDefaultQASamples(realm);

        new EVADModel().restoreFactoryDefaults(realm);
        new EVADCardLotDescriptionModel().restoreFactoryDefaults(realm);

        /*
        //For testing, create one dedicated reader
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                Reader rd = realm.createObject(Reader.class,PrimaryKeyFactory.getInstance().nextKey(Reader.class));
                rd.setActive(true);
                rd.setAlias("TestReaderBM");
                rd.setSerialNumber("TestSN12345");
                rd.setBluetoothAddress("00:80:98:E8:D7:6A");
                rd.setPin("1111");
                rd.setCreated(new Date());
                rd.setDedicated(true);

                Reader dummy = realm.createObject(Reader.class, PrimaryKeyFactory.getInstance().nextKey(Reader.class));
                dummy.setBluetoothAddress("AA:BB:CC:DD:EE:FF");
                dummy.setActive(true);
                dummy.setAlias("DummyA");
                dummy.setDedicated(true);
                dummy.setSerialNumber("DummyASN123456");

                Reader dummy2 = realm.createObject(Reader.class, PrimaryKeyFactory.getInstance().nextKey(Reader.class));
                dummy2.setBluetoothAddress("FF:BB:CC:DD:EE:AA");
                dummy2.setActive(true);
                dummy2.setAlias("DummyB");
                dummy2.setDedicated(true);
                dummy2.setDeviceName("DummyBSN543210");
            }
        });
        */
        new TestPanelModel().createFactoryDefault(realm);

    }
}
