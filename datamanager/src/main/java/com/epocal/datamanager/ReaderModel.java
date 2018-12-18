package com.epocal.datamanager;

import android.util.Log;

import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.common.realmentities.Reader;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;
import com.epocal.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by bmate on 6/19/2017.
 */

public class ReaderModel {

    /**
     * get a realmresults list of persisted readers
     * all readers in the database, ordered first by alias and second by lastConnected descending
     *
     * @return RealmResults<Reader>
     **/
    public RealmResults<Reader> getStoredReaders(Realm realm){
        return realm.where(Reader.class).findAllSorted("alias", Sort.ASCENDING,"lastConnected", Sort.DESCENDING);
    }

    /**
     * get a realmresults list of persisted active readers
     * all active readers in the database, ordered first by Name and second by Lastconnected descending
     *
     * @return RealmResults<Reader>
     **/
    public RealmResults<Reader> getActiveReaders(Realm realm){
        return realm.where(Reader.class)
                .equalTo("isActive",true)
                .findAllSorted("alias", Sort.ASCENDING,"lastConnected", Sort.DESCENDING);
    }

    /**
     * delete all inactive readers from database
     *
     * @return void
     **/
    public  void deleteInactiveReaders(){
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Reader> inactiveReaders = realm.where(Reader.class).equalTo("isActive",false).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                inactiveReaders.deleteAllFromRealm();
            }
        });
        realm.close();
    }

    /**
     * delete all readers from database
     *
     * @return void
     **/
    public  void deleteReaders(){

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Reader> allReaders = realm.where(Reader.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                allReaders.deleteAllFromRealm();
            }
        });
        realm.close();
    }

    /**
     * get all active readers, with the list of dedicated readers listed first
     *
     * @return RealmResults<Reader>
     **/
    public ArrayList<ReaderDevice> getActiveReadersOrderedByDedicatedFirst(){
        ArrayList<ReaderDevice> allActiveReaders = new ArrayList<>();

        ArrayList<ReaderDevice> dedicatedReaders = getDedicatedReaders();
        if (dedicatedReaders != null)
            allActiveReaders.addAll(dedicatedReaders);

        ArrayList<ReaderDevice> undedicatedReaderDevices = getUndedicatedReaders();
        if (undedicatedReaderDevices != null)
            allActiveReaders.addAll(undedicatedReaderDevices);

        return allActiveReaders;
    }

    /**
     * get all undedicated readers, ordered by most recently connected
     *
     * @return list of undedicated reader devices
     **/
    public ArrayList<ReaderDevice> getUndedicatedReaders() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Reader> undedicatedReaders = realm.where(Reader.class)
                .equalTo("isActive",true)
                .equalTo("isDedicated",false)
                .findAllSorted("lastConnected", Sort.DESCENDING);

        ArrayList<ReaderDevice> dedicatedReaderDevices = convertReaderRealmResults(undedicatedReaders);

        realm.close();

        return dedicatedReaderDevices;
    }

    /**
     * get all dedicated readers, ordered by most recently connected
     *
     * @return list of dedicated reader devices
     **/
    public ArrayList<ReaderDevice> getDedicatedReaders() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Reader> dedicatedReaders = realm.where(Reader.class)
                .equalTo("isActive",true)
                .equalTo("isDedicated",true)
                .findAllSorted("lastConnected",Sort.DESCENDING);

        ArrayList<ReaderDevice> dedicatedReaderDevices = convertReaderRealmResults(dedicatedReaders);

        realm.close();

        return dedicatedReaderDevices;
    }

    /**
     * If there is only one dedicated reader, get this reader
     *
     * @return the single dedicated reader; otherwise null
     */
    public ReaderDevice getSingleDedicatedReaderDevice() {
        ReaderDevice readerDevice = null;

        ArrayList<ReaderDevice> allDedicatedReaders = getDedicatedReaders();

        if (allDedicatedReaders.size() == 1)
            readerDevice = allDedicatedReaders.get(0);

        return readerDevice;
    }

    public boolean readerExistsInRealm(String readerBTA){
        boolean retval = false;
        Realm realm = Realm.getDefaultInstance();
        Reader rdr = realm.where(Reader.class)
                .equalTo("bluetoothAddress",readerBTA)
                .equalTo("isActive",true)
                .findFirst();
        if (rdr !=null){
            retval = true;
        }
        realm.close();
        return retval;
    }
    /**
     * get active reader (managed) by bta
     *
     * @return Reader
     **/
    public Reader getReader(String readerBTA,Realm realm){
        return realm.where(Reader.class)
                .equalTo("isActive",true)
                .equalTo("bluetoothAddress",readerBTA)
                .findFirst();
    }

    /**
     * get active reader (unmanaged) by bta
     *
     * @return Reader
     **/
    public Reader getReader(String readerBTA){
        Reader retval = null;
        Realm realm = Realm.getDefaultInstance();
        Reader managedRDR = realm.where(Reader.class)
                .equalTo("isActive",true)
                .equalTo("bluetoothAddress",readerBTA)
                .findFirst();

        if (managedRDR!=null) {
            retval = realm.copyFromRealm(managedRDR);
        }
        realm.close();
        return  retval;
    }
    /**
     * dedicate or undedicate reader
     * if the reader is not found in the DB, return false
     *
     * @return  true = read was found in the DB
     *         false = reader was not found in the DB, the update could not be applied
     **/
    public boolean dedicateReader(String readerSerialNumber, final boolean dedicate){
        boolean foundReaderInDB = false;
        Realm realm = Realm.getDefaultInstance();
        final Reader managedReader = realm.where(Reader.class)
                .equalTo("serialNumber",readerSerialNumber)
                .equalTo("isActive",true).findFirst();
        if (managedReader != null){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    managedReader.setDedicated(dedicate);
                }
            });
            foundReaderInDB = true;
        }

        realm.close();

        return foundReaderInDB;
    }

    /**
     *  persist unmanaged reader entity (insert or update)
     *  if an active reader with same serialnr exists : if no version change needed, then update, else create new version
     *  else create new reader
     *
     **/
    public void saveReader(final Reader reader){
        Realm realm = Realm.getDefaultInstance();
        final Reader managedreader = realm.where(Reader.class)
                .equalTo("bluetoothAddress",reader.getBluetoothAddress())
                .equalTo("isActive",true).findFirst();
        if (managedreader!=null ){
            if(isNewVersion (managedreader,reader)){
                // new version needed
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        managedreader.setActive(false);
                    }
                });

            }else{
                // simple update needed
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        managedreader.setBluetoothAddress(reader.getBluetoothAddress());
                        managedreader.setCvExpiryDateTime(reader.getCvExpiryDateTime());
                        managedreader.setCvInfoUpdateDateTime(reader.getCvInfoUpdateDateTime());
                        managedreader.setDedicated(reader.getDedicated());
                        managedreader.setLastConnected(reader.getLastConnected());
                        managedreader.setLastEQCDateTime(reader.getLastEQCDateTime());
                        managedreader.setLastEQCPassFail(reader.getLastEQCPassFail());
                        managedreader.setActive(reader.getActive());
                        managedreader.setPin(reader.getPin());
                        managedreader.setQcExpiryDateTime(reader.getQcExpiryDateTime());
                        managedreader.setQcInfoUpdateDateTime(reader.getQcInfoUpdateDateTime());
                        managedreader.setLastTQADateTime(reader.getLastTQADateTime());
                        managedreader.setLastTQAPassFail(reader.getLastTQAPassFail());
                        managedreader.setLastUpdated(reader.getLastUpdated());
                        managedreader.setReaderQCContentVersion(reader.getReaderQCContentVersion());
                        managedreader.setReaderQCFluidInfos(reader.getReaderQCFluidInfos());
                        managedreader.setSyncedStatus(reader.getSyncedStatus());
                    }
                });
                realm.close();
                return;
            }
        }
        createNewReader(reader,realm);
        realm.close();
    }

    private void createNewReader (Reader reader, Realm realm){
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//
//            }
//        });
        try {
            realm.beginTransaction();
            reader.setId(PrimaryKeyFactory.getInstance().nextKey(Reader.class));
            reader.setCreated(new Date());
            reader.setActive(true);
            reader.setLastConnected(new Date());
            realm.insertOrUpdate(reader);
            realm.commitTransaction();
        }catch (Exception ex){
            Log.e("Realm Error",ex.getMessage() );
            realm.cancelTransaction();
        }
    }
    /**
     *  compares reader members and decides if changes require to persist a new version
     *
     * @return boolean
     **/
    private boolean isNewVersion(Reader original, Reader newReader){
        boolean retval = false;
        if (!retval){
             if (!StringUtil.empty(original.getAlias()) && !StringUtil.empty(newReader.getAlias())){
                 retval = original.getAlias().compareTo(newReader.getAlias())!=0;
             }
        }
        if (!retval){
            if (!StringUtil.empty(original.getHardwareVersion()) && !StringUtil.empty(newReader.getHardwareVersion())){
                retval = original.getHardwareVersion().compareTo(newReader.getHardwareVersion())!=0;
            }
        }
        if (!retval){
            if (!StringUtil.empty(original.getMechanicalVersion()) && !StringUtil.empty(newReader.getMechanicalVersion())){
                retval = original.getMechanicalVersion().compareTo(newReader.getMechanicalVersion())!=0;
            }
        }
        if (!retval){
            if (!StringUtil.empty(original.getSoftwareVersion()) && !StringUtil.empty(newReader.getSoftwareVersion())){
                retval = original.getSoftwareVersion().compareTo(newReader.getSoftwareVersion())!=0;
            }
        }
        return retval;
    }

    private ArrayList<ReaderDevice> convertReaderRealmResults(RealmResults<Reader> readers) {
        ArrayList<ReaderDevice> readerDevices = new ArrayList<>();

        for (Reader reader: readers) {
            readerDevices.add(reader.toReaderDevice());
        }

        return readerDevices;
    }
}
