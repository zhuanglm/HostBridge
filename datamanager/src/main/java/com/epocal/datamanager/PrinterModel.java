package com.epocal.datamanager;

import com.epocal.common.realmentities.Printer;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class PrinterModel {

    Realm mRealm = null;

    public void openRealmInstance() {
        if (mRealm == null)
            mRealm = Realm.getDefaultInstance();
    }

    public void closeRealmInstance() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
    }

    public ArrayList<Printer> getAllPrinters() {
        ArrayList<Printer> allPrinters = new ArrayList<>();

        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        try {
            RealmResults<Printer> allPrintersInRealm = realm.where(Printer.class).findAll();
            allPrinters.addAll(allPrintersInRealm);
        } catch (Exception ex) {
            // todo handle exception
        }

        if (mRealm == null)
            realm.close();

        return allPrinters;
    }

    public Printer getPrinter(String printerName) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        Printer printer = realm.copyFromRealm(realm.where(Printer.class).equalTo("PrinterName", printerName).findFirst());

        if (mRealm == null)
            realm.close();

        return printer;
    }

    public Printer findPrinterById(int id) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        Printer printer = realm.where(Printer.class)
                .equalTo("id", id)
                .findFirst();

        if (mRealm == null)
            realm.close();

        return printer;
    }

    public Printer findPrinterByName(String printerName) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        Printer printer = realm.where(Printer.class)
                .equalTo("PrinterName", printerName)
                .findFirst();

        if (mRealm == null)
            realm.close();

        return printer;
    }

    public void savePrinter(final Printer unmanagedPrinter) {
        if (unmanagedPrinter.getId() == 0) {
            createNewPrinter(unmanagedPrinter);
        } else if (unmanagedPrinter.getId() > 0) {
            updatePrinter(unmanagedPrinter);
        }
    }

    private void createNewPrinter(final Printer unmanagedPrinter) {
        // here we got an unmanaged User with all fields validated
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        //create by administrator
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Printer printer = realm.createObject(Printer.class, PrimaryKeyFactory.getInstance().nextKey(Printer.class));
                if(printer != null) {
                    printer.setPrinterName(unmanagedPrinter.getPrinterName());
                    printer.setPrinterAddress(unmanagedPrinter.getPrinterAddress());
                    printer.setIsPrintCalculatedResult(unmanagedPrinter.getIsPrintCalculatedResult());
                    printer.setIsPrintCorrectedResult(unmanagedPrinter.getIsPrintCorrectedResult());
                    printer.setIsPrintTestInfo(unmanagedPrinter.getIsPrintTestInfo());
                    printer.setPrinterType(unmanagedPrinter.getPrinterType());
                    printer.setConnectionType(unmanagedPrinter.getConnectionType());
                    printer.setLastUsed(new Date());
                }
            }
        });

        if (mRealm == null)
            realm.close();
    }


    // deletes a user if no tests associated
    public boolean deletePrinter(final String printerName) {
        // exit condition
        if (printerName.isEmpty()) return false;

        boolean deleted = false;
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Printer> results = realm.where(Printer.class).equalTo("PrinterName", printerName).findAll();
                results.deleteAllFromRealm();
            }
        });

        deleted = true;

        if (mRealm == null)
            realm.close();

        return deleted;
    }

    public void updatePrinterLastUsed(final String printerName, final Date lastUsed) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Printer printer = realm.where(Printer.class).equalTo("PrinterName", printerName).findFirst();
                if(printer != null) {
                    printer.setLastUsed(lastUsed);
                }
            }
        });

        if (mRealm == null)
            realm.close();
    }

    // TODO: implement new version mechanism
    private void updatePrinter(final Printer unmanagedPrinter) {
        // here we got an unmanaged User with all fields validated
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        //create administrator
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Printer printer = realm.where(Printer.class).equalTo("PrinterName", unmanagedPrinter.getPrinterName()).findFirst();
                if (printer != null) {
                    printer.setPrinterAddress(unmanagedPrinter.getPrinterAddress());
                    printer.setIsPrintCalculatedResult(unmanagedPrinter.getIsPrintCalculatedResult());
                    printer.setIsPrintCorrectedResult(unmanagedPrinter.getIsPrintCorrectedResult());
                    printer.setIsPrintTestInfo(unmanagedPrinter.getIsPrintTestInfo());
                    printer.setPrinterType(unmanagedPrinter.getPrinterType());
                    printer.setConnectionType(unmanagedPrinter.getConnectionType());
                    //printer.setLastUsed(new Date()); // TODO: Should this be updated every change?
                }
            }
        });

        if (mRealm == null)
            realm.close();
    }

    static public RealmResults<Printer> getAllPrinters(Realm realm) {
        // Input check
        if (realm == null) {
            String className = TestRecordModel.class.getSimpleName();
            String methodName = TestRecordModel.class.getEnclosingMethod().getName();
            String errorString = "realm object is null.";
            throw new IllegalArgumentException(className + "::" + methodName + " " + errorString);
        }

        return realm.where(Printer.class).findAll();
    }
}
